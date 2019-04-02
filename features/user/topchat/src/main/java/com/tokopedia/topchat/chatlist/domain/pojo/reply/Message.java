package com.tokopedia.topchat.chatlist.domain.pojo.reply;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by stevenfredian on 11/1/17.
 */

public class Message {

    @SerializedName("censored_reply")
    @Expose
    private String censoredReply;
    @SerializedName("original_reply")
    @Expose
    private String originalReply;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("timestamp_fmt")
    @Expose
    private String timestampFmt;
    @SerializedName("timestamp_unix_nano")
    @Expose
    private String timeStampUnixNano;
    @SerializedName("timestamp_unix")
    @Expose
    private String timeStampUnix;

    public String getCensoredReply() {
        return censoredReply;
    }

    public String getOriginalReply() {
        return originalReply;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestampFmt() {
        return timestampFmt;
    }

    public String getTimeStampUnix() {
        return timeStampUnix;
    }

    public String getTimeStampUnixNano() {
        return timeStampUnixNano;
    }
}
