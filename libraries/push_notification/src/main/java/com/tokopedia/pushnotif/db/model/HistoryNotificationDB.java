package com.tokopedia.pushnotif.db.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.pushnotif.db.PushNotificationDatabase;

/**
 * @author ricoharisin .
 */

@Table(database = PushNotificationDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class HistoryNotificationDB extends BaseModel{

    @PrimaryKey(autoincrement = true)
    @Column(name = "id")
    private int Id;

    @Column(name = "sender_name")
    private String SenderName;

    @Column(name = "message")
    private String Message;

    @Column(name = "notification_type")
    private int NotificationType;

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


}
