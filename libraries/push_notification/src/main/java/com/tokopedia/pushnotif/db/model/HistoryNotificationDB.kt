package com.tokopedia.pushnotif.db.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.tokopedia.pushnotif.db.HISTORY_NOTIFICATION_TABLE

@Entity(tableName = HISTORY_NOTIFICATION_TABLE)
data class HistoryNotificationDB(
    @ColumnInfo(name = "sender_name")
    var senderName: String?,
    @ColumnInfo(name = "message")
    var message: String?,
    @ColumnInfo(name = "notification_type")
    var notificationType: Int?,
    @ColumnInfo(name = "notification_id")
    var notificationId: Int?
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
}
