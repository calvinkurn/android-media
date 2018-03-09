package com.tokopedia.tkpd.fcm.applink.model;

/**
 * @author ricoharisin .
 */

public class HistoryNotificationModel {
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    private long timeStamp;

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    private String senderName;

    public HistoryNotificationModel(String message, long timeStamp, String senderName) {
        this.message = message;
        this.timeStamp = timeStamp;
        this.senderName = senderName;
    }
}
