/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2020.
 */
package de.rexlmanu.autojump;

import net.labymod.api.LabyModAddon;
import net.labymod.core.LabyModCore;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Material;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

public class AutoJumpAddon extends LabyModAddon {

    private boolean enabled;

    @Override
    public void onEnable() {
        this.getApi().registerForgeListener(this);
    }

    @SubscribeEvent
    public void handle(TickEvent.ClientTickEvent event) {
        EntityPlayerSP player = LabyModCore.getMinecraft().getPlayer();
        if (!this.enabled || LabyModCore.getMinecraft().getWorld() == null || player == null || !player.onGround || player.isSneaking())
            return;

        if (LabyModCore.getMinecraft().getWorld().getCollidingBoundingBoxes(player,
                player.getEntityBoundingBox().offset(0.0, -0.5, 0.0).expand(-0.5, 0.0, -0.05)).isEmpty()) {
            player.jump();
        }
    }

    @Override
    public void loadConfig() {
        this.enabled = this.addConfigDefault("enabled", false);
    }

    @Override
    protected void fillSettings(List<SettingsElement> settingsElements) {
        settingsElements.add(new BooleanElement("Enabled", this,
                new ControlElement.IconData(Material.LEVER), "enabled", this.enabled));
    }

    private boolean addConfigDefault(String key, boolean value) {
        if (!this.getConfig().has(key)) {
            this.getConfig().addProperty(key, value);
            return value;
        }
        return this.getConfig().get(key).getAsBoolean();
    }

}
