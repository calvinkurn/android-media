package com.tokopedia.topchat.chatroom.domain.pojo.getuserstatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hendri on 31/07/18.
 */
public class GetUserStatusDataPojo {
    @SerializedName("timestamp")
    @Expose
    long timestamp;

    @SerializedName("is_online")
    @Expose
    boolean isOnline;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}
