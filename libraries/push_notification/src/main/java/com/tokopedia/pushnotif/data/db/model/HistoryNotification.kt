package com.tokopedia.pushnotif.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tokopedia.pushnotif.data.constant.HISTORY_NOTIFICATION_TABLE
import org.jetbrains.annotations.Nullable

@Entity(tableName = HISTORY_NOTIFICATION_TABLE)
data class HistoryNotification(
        @Nullable @ColumnInfo(name = "sender_name") var senderName: String?,
        @Nullable @ColumnInfo(name = "message") var message: String?,
        @Nullable @ColumnInfo(name = "notification_type") var notificationType: Int?,
        @Nullable @ColumnInfo(name = "notification_id") var notificationId: Int?,
        @Nullable @ColumnInfo(name = "avatar_url") var avatarUrl: String?,
        @Nullable @ColumnInfo(name = "applink") var appLink: String?
) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0
}
