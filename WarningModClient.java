package com.example.playerwarning;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class WarningModClient implements ClientModInitializer {
    private static final double RANGE = 70.0;       // kratki
    private static final float MIN_HEALTH = 70.0f;  // 35 serc = 70 HP

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register((context, tickCounter) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            ClientPlayerEntity self = client.player;
            if (self == null || client.world == null) return;

            boolean danger = false;
            for (PlayerEntity other : client.world.getPlayers()) {
                if (other == self) continue;
                if (other.getHealth() > MIN_HEALTH && self.distanceTo(other) < RANGE) {
                    danger = true;
                    break;
                }
            }
            if (!danger) return;

            Text text = Text.literal("⚠ UWAGA! ZBLIŻA SIĘ GRACZ!");
            int screenW = client.getWindow().getScaledWidth();
            int x = (screenW - client.textRenderer.getWidth(text)) / 2;
            context.drawTextWithShadow(client.textRenderer, text, x, 20, 0xFFFF0000);
        });
    }
}
