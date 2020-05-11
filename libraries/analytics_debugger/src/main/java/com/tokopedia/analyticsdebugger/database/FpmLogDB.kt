package com.tokopedia.analyticsdebugger.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val FPM_LOG_TABLE_NAME = "fpm_log"

@Entity(tableName = FPM_LOG_TABLE_NAME)
class FpmLogDB {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ColumnInfo(name = "tracename")
    var traceName: String? = null

    @ColumnInfo(name = "metrics")
    var metrics: String? = null

    @ColumnInfo(name = "attributes")
    var attributes: String? = null

    @ColumnInfo(name = "duration")
    var duration: Long = 0

    @ColumnInfo(name = "timestamp")
    var timestamp: Long = 0
}