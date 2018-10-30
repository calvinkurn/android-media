package com.tokopedia.groupchat.chatroom.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author : Steven 09/10/18
 */
public class EventHandlerPojo {

    public static final String FREEZE = "freeze";
    public static final String BANNED = "banned";

    @SerializedName("is_freeze")
    @Expose
    private boolean isFreeze;
    @SerializedName("is_banned")
    @Expose
    private boolean isBanned;
    @SerializedName("channel_id")
    @Expose
    private String channelId;
    @SerializedName("user_id")
    @Expose
    private String userId;

    public boolean isFreeze() {
        return isFreeze;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getUserId() {
        return userId;
    }
}
