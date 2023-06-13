package com.tokopedia.pushnotif.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tokopedia.pushnotif.data.constant.TRANSACTION_TABLE
import org.jetbrains.annotations.Nullable

@Entity(tableName = TRANSACTION_TABLE)
data class TransactionNotification(
        @Nullable @ColumnInfo(name = "sender_name") var senderName: String? = "",
        @Nullable @ColumnInfo(name = "message") var message: String? = "",
        @Nullable @ColumnInfo(name = "notification_type") var notificationType: Int? = 0,
        @Nullable @ColumnInfo(name = "notification_id") var notificationId: Int? = 0,
        @Nullable @ColumnInfo(name = "transaction_id") var transactionId: String? = "",
        @Nullable @ColumnInfo(name = "avatar_url") var avatarUrl: String? = "",
        @Nullable @ColumnInfo(name = "applink") var appLink: String?
) {
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Int = 0
}
