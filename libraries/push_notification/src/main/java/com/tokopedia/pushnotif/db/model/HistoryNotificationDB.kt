package com.tokopedia.pushnotif.db.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.tokopedia.pushnotif.db.HISTORY_NOTIFICATION_TABLE
import org.jetbrains.annotations.Nullable

@Entity(tableName = HISTORY_NOTIFICATION_TABLE)
data class HistoryNotificationDB(
    @ColumnInfo(name = "sender_name")
    @Nullable
    var senderName: String?,
    @ColumnInfo(name = "message")
    @Nullable
    var message: String?,
    @ColumnInfo(name = "notification_type")
    @Nullable
    var notificationType: Int?,
    @ColumnInfo(name = "notification_id")
    @Nullable
    var notificationId: Int?
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
}
