package com.tokopedia.pushnotif.db.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.tokopedia.pushnotif.db.HISTORY_NOTIFICATION_TABLE

@Entity(tableName = HISTORY_NOTIFICATION_TABLE)
data class HistoryNotificationTable(
    @ColumnInfo(name = "sender_name")
    val SenderName: String,
    @ColumnInfo(name = "message")
    val Message: String,
    @ColumnInfo(name = "notification_type")
    val NotificationType: Int,
    @ColumnInfo(name = "notification_id")
    val NotificationId: Int
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var Id: Int = 0
}
