package com.tokopedia.groupchat.chatroom.websocket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author : Steven 09/10/18
 */
public class EventHandler {

    public static final String FREEZE = "freeze";
    public static final String BANNED = "banned";

    @SerializedName("is_freeze")
    @Expose
    private boolean isFreeze;
    @SerializedName("is_banned")
    @Expose
    private boolean isBanned;

    public boolean isFreeze() {
        return isFreeze;
    }

    public boolean isBanned() {
        return isBanned;
    }
}
