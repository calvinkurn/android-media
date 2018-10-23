package com.tokopedia.groupchat.chatroom.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.groupchat.chatroom.domain.pojo.BaseGroupChatPojo;

/**
 * @author : Steven 08/10/18
 */
public class AdminMsg extends BaseGroupChatPojo{

    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}