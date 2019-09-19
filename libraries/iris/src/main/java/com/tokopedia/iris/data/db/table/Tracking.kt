package com.tokopedia.iris.data.db.table

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.tokopedia.iris.util.TABLE_TRACKING
import java.util.*

@Entity(tableName = TABLE_TRACKING)
data class Tracking (
    val event: String,
    val userId: String,
    val deviceId: String,
    val timeStamp: Long = Calendar.getInstance().timeInMillis
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var trackingId: Int = 0

    override fun toString(): String {
        return "event: $event, userId: $userId, deviceId: $deviceId, timeStamp: $timeStamp"
    }
}
