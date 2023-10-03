package com.tokopedia.iris.data.db.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tokopedia.iris.util.TABLE_PERF_TRACKING
import com.tokopedia.iris.util.TABLE_TRACKING
import java.util.*

@Entity(tableName = TABLE_TRACKING)
data class Tracking (
    val event: String,
    val userId: String,
    val deviceId: String,
    val timeStamp: Long = Calendar.getInstance().timeInMillis,
    @ColumnInfo(defaultValue = "")
    val appVersion:String = ""
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var trackingId: Int = 0

    override fun toString(): String {
        return "event: $event, userId: $userId, deviceId: $deviceId, timeStamp: $timeStamp " +
                ", appVersion: $appVersion"
    }
}

@Entity(tableName = TABLE_PERF_TRACKING)
data class PerformanceTracking (
    val event: String,
    val userId: String,
    val deviceId: String,
    val timeStamp: Long = Calendar.getInstance().timeInMillis,
    @ColumnInfo(defaultValue = "")
    val appVersion:String = "",
    val carrier:String,
    val lowPower:Boolean
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var trackingId: Int = 0

    override fun toString(): String {
        return "event: $event, userId: $userId, deviceId: $deviceId, timeStamp: $timeStamp " +
                ", appVersion: $appVersion"
    }
}
