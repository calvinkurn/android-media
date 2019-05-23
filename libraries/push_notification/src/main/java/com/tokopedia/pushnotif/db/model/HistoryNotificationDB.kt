package com.tokopedia.pushnotif.db.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.tokopedia.pushnotif.db.HISTORY_NOTIFICATION_TABLE

@Entity(tableName = HISTORY_NOTIFICATION_TABLE)
data class HistoryNotificationDB(
    @ColumnInfo(name = "sender_name")
    val senderName: String,
    @ColumnInfo(name = "message")
    val message: String,
    @ColumnInfo(name = "notification_type")
    val notificationType: Int,
    @ColumnInfo(name = "notification_id")
    val notificationId: Int
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
}
