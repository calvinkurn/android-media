package com.tokopedia.topchat.chatroom.domain.pojo.chatactionballoon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hendri on 18/07/18.
 */
public class ChatActionPojo {

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
