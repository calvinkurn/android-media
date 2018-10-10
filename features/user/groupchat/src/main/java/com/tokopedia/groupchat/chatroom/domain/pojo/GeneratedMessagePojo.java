package com.tokopedia.groupchat.chatroom.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by nisie on 3/29/18.
 */

public class GeneratedMessagePojo extends BaseGroupChatPojo{

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("is_vibrate")
    @Expose
    private boolean isVibrate;
    @SerializedName("send_pns")
    @Expose
    private boolean sendPns;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isVibrate() {
        return isVibrate;
    }

    public void setVibrate(boolean vibrate) {
        isVibrate = vibrate;
    }

    public boolean isSendPns() {
        return sendPns;
    }

    public void setSendPns(boolean sendPns) {
        this.sendPns = sendPns;
    }
}
