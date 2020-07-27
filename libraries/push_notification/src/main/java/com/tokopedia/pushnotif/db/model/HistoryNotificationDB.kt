package com.tokopedia.pushnotif.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tokopedia.pushnotif.db.HISTORY_NOTIFICATION_TABLE
import org.jetbrains.annotations.Nullable

@Entity(tableName = HISTORY_NOTIFICATION_TABLE)
data class HistoryNotificationDB(

    @Nullable
    @ColumnInfo(name = "sender_name")
    var senderName: String? = "",

    @Nullable
    @ColumnInfo(name = "message")
    var message: String? = "",

    @Nullable
    @ColumnInfo(name = "notification_type")
    var notificationType: Int? = 0,

    @Nullable
    @ColumnInfo(name = "notification_id")
    var notificationId: Int? = 0,

    @Nullable
    @ColumnInfo(name = "trans_id")
    var transId: Int? = 0

) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

}
