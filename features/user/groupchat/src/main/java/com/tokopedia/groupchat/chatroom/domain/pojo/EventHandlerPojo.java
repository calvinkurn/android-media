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

    public boolean isFreeze() {
        return isFreeze;
    }

    public boolean isBanned() {
        return isBanned;
    }
}
