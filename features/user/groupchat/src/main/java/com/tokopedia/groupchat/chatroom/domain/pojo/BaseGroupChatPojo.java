package com.tokopedia.groupchat.chatroom.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author : Steven 09/10/18
 */
public class BaseGroupChatPojo {

    @SerializedName("channel_id")
    @Expose
    private String channelId;

    @SerializedName("msg_id")
    @Expose
    private String messageId;

    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    @SerializedName("user")
    @Expose
    private UserData user;

    public String getTimestampString() {
        return timestamp;
    }

    public Long getTimestamp(){
        return Long.valueOf(timestamp);
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }
}
