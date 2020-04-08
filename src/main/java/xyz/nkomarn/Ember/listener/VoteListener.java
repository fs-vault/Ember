package xyz.nkomarn.Ember.listener;

import com.vexsoftware.votifier.bungee.events.VotifierEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import xyz.nkomarn.Ember.Ember;
import xyz.nkomarn.Ember.data.PlayerData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VoteListener implements Listener {
    @EventHandler
    public void onVote(final VotifierEvent event) {
        final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(event.getVote().getUsername());

        ProxyServer.getInstance().getScheduler().runAsync(Ember.getEmber(), () -> {
            Connection connection = null;

            try {
                connection = PlayerData.getConnection();
                PreparedStatement statement = connection.prepareStatement("UPDATE `playerdata` SET " +
                        "`votes` = `votes` + 1 WHERE `uuid` = ?;");
                statement.setString(1, player.getUniqueId().toString());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
