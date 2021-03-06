package de.lukweb.twitchchat.twitch.messages;

import de.lukweb.twitchchat.TwitchRank;
import de.lukweb.twitchchat.TwitchUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MessageAttributes {

    private List<MessageBadge> badges;
    private String color;
    private String displayName;
    private List<MessageEmote> emotes;
    private String messageId;
    private boolean mod;
    private boolean subscriber;
    private boolean turbo;
    private int roomId;
    private int userId;
    private TwitchRank rank;
    private int bits;

    public MessageAttributes(Map<String, String> tags) {

        badges = new ArrayList<>();
        emotes = new ArrayList<>();

        if (tags.containsKey("badges")) {
            for (String badgeString : tags.get("badges").split("[,]")) {
                badges.add(new MessageBadge(badgeString));
            }
        }

        color = tags.get("color");
        displayName = tags.get("display-name");

        if (tags.containsKey("emotes") && tags.get("emotes").length() > 0) {
            for (String emoteString : tags.get("emotes").split("/")) {
                emotes.add(new MessageEmote(emoteString));
            }
        }

        messageId = tags.get("id");

        if (tags.containsKey("mod")) {
            mod = tags.get("mod").equals("1");
        }

        if (tags.containsKey("subscriber")) {
            subscriber = tags.get("subscriber").equals("1");
        }

        if (tags.containsKey("turbo")) {
            turbo = tags.get("turbo").equals("1");
        }

        if (tags.containsKey("room-id")) {
            roomId = Integer.parseInt(tags.get("room-id"));
        }

        if (tags.containsKey("user-id")) {
            userId = Integer.parseInt(tags.get("user-id"));
        }

        rank = TwitchRank.get(tags.getOrDefault("user-type", ""));

        if (tags.containsKey("bits")) {
            bits = Integer.parseInt(tags.get("bits"));
        }
    }

    public List<MessageBadge> getBadges() {
        return badges;
    }

    public String getColor() {
        return color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<MessageEmote> getEmotes() {
        return emotes;
    }

    public String getMessageId() {
        return messageId;
    }

    public boolean isMod() {
        return mod;
    }

    public boolean isSubscriber() {
        return subscriber;
    }

    public int getSubscriberMonths() {
        if (!isSubscriber()) return -1;
        for (MessageBadge badge : badges) {
            if (badge.getName().equalsIgnoreCase("subscriber")) return badge.getValue();
        }
        return 0;
    }

    public boolean isTurbo() {
        return turbo;
    }

    public boolean isPrime() {
        for (MessageBadge badge : badges) {
            if (badge.getName().equalsIgnoreCase("premium") && badge.getValue() == 1) return true;
        }
        return false;
    }

    public boolean isBroadcaster() {
        for (MessageBadge badge : badges) {
            if (badge.getName().equalsIgnoreCase("broadcaster") && badge.getValue() == 1) return true;
        }
        return false;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getUserId() {
        return userId;
    }

    /**
     * Gets the rank of the message sender
     *
     * @return rank of the message sender
     */
    public TwitchRank getRank() {
        return rank;
    }

    /**
     * Gets the amount of bits donated with this message
     *
     * @return the amount of bits donated with this message
     */
    public int getBits() {
        return bits;
    }

    /**
     * Gets the amount of all bits donated to this channel
     *
     * @return the amount of all bits donated to this channel
     */
    public int getAllBits() {
        for (MessageBadge badge : badges) if (badge.getName().equals("bits")) return badge.getValue();
        return 0;
    }

    /**
     * Compares whether the attributes of {@link TwitchUser} are equals to this ones.
     *
     * @param user the user to compare with
     * @return whether the attributes are equal
     */
    public boolean compareTo(TwitchUser user) {
        if (user.getUserId() != userId) return false;
        if (!Objects.equals(user.getDisplayName(), displayName)) return false;
        if (!Objects.equals(user.getRank(), rank)) return false;
        if (user.isBroadcaster() != isBroadcaster()) return false;
        if (user.isTurbo() != isTurbo()) return false;
        if (user.isPrime() != isPrime()) return false;
        if (user.isMod() != isMod()) return false;
        if (user.isSubscriber() != isSubscriber()) return false;
        if (!Objects.equals(user.getColor(), color)) return false;
        if (user.getDonatedBitAmount() != getAllBits()) return false;
        return true;
    }
}

