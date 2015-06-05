package creeperbomb;
 
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * CreeperBomb is a small bukkit plugin for MineCraft
 * that simply drops a predefined number of creepers from a
 * set height at random locations around the intended target(s). 
 * The target can be another player, all players or even yourself!
 *
 * @author Mike Trentman
 * @version 1.0 
 */
 
public class CreeperBomb extends JavaPlugin 
{ 
    public static Logger log = Logger.getLogger("Minecraft");
  
    public static final String BOMB_MESSAGE = "Death from above!!!";
    public static final int NUMBER_OF_BOMBS = 24;
  
    public boolean onCommand(CommandSender sender, 
                            Command command, 
                            String commandLabel, 
                            String[] args) 
    {         
        if (commandLabel.equalsIgnoreCase("creeperbomb")) 
        { 
            if (sender instanceof Player) 
            { 
                Player me = (Player)sender;
        
                Location location = null;
                Player target = null;
    
                // Check arguments
                //   If no arguments then drop the creepers around your own location!
                if (args.length == 0) 
                {
                    location = me.getLocation();
                    me.sendMessage(BOMB_MESSAGE);
                    deliverBomb(me, location);
                } 
                else if (args.length == 1) 
                {
                    // Check if we are targeting all players on the server
       	            if (args[0].equalsIgnoreCase("all")) 
                    {
                        Player[] playerList = getServer().getOnlinePlayers();
                        // For all players...
                        for (Player player : playerList) 
                        {
                            location = player.getLocation();
                            player.sendMessage(BOMB_MESSAGE);
                            deliverBomb(me, location);
                        }
                    } 
                    else 
                    {
                        // Attempting to target an individual player
                        target = getServer().getPlayer(args[0]);
                        if (target == null) 
                        {
                            // The target player was not found
                            sender.sendMessage(args[0] + " is not online!");
                            return true;
                        }
                        else 
                        {
                            // Found the player... bombs away!
                            location = target.getLocation();
                            target.sendMessage(BOMB_MESSAGE);
                            deliverBomb(me, location);
                        }
                    }
                } 
                else 
                {
                    sender.sendMessage("Incorrect arguments!");
                    return false;
                }
                return true;
            }
        }
        return false;
    }
  
    private void deliverBomb(Player me, Location location) 
    {
        // Attempt to randomize coordinates a bit 
        //    so that the creepers drop all around the target location
        for (int i = 0; i < NUMBER_OF_BOMBS; i++) 
        {
            double plusX = Math.random() * 5;
            if ((Math.random() * 10) < 6) 
            {
  	            plusX = (0 - plusX);
            }			
            double plusZ = Math.random() * 5;
            if ((Math.random() * 10) < 6) 
            {
                plusZ = (0 - plusZ);
            }
            Location newLocation = new Location(location.getWorld(),
            location.getX() + plusX, 
            location.getY() + (Math.random() + 20),
            location.getZ() + plusZ);
            // This spawns the creeper at the location provided
            me.getWorld().spawn(newLocation, Creeper.class);
        }        
    }
}