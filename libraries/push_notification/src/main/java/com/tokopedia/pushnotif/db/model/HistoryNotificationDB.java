package com.tokopedia.pushnotif.db.model;

/**
 * @author ricoharisin .
 */

public class HistoryNotificationDB {

    private int Id;

    private String SenderName;

    private String Message;

    private int NotificationType;

    private int NotificationId;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getSenderName() {
        return SenderName;
    }

    public void setSenderName(String senderName) {
        SenderName = senderName;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public int getNotificationType() {
        return NotificationType;
    }

    public void setNotificationType(int notificationType) {
        NotificationType = notificationType;
    }


    public int getNotificationId() {
        return NotificationId;
    }

    public void setNotificationId(int notificationId) {
        NotificationId = notificationId;
    }
}
