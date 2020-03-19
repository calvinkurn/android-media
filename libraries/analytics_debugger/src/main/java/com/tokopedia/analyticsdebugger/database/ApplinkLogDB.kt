package com.tokopedia.analyticsdebugger.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val APPLINK_LOG_TABLE_NAME = "applink_log"

@Entity(tableName = APPLINK_LOG_TABLE_NAME)
class ApplinkLogDB {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ColumnInfo(name = "applink")
    var applink: String? = null

    @ColumnInfo(name = "traces")
    var traces: String? = null

    @ColumnInfo(name = "timestamp")
    var timestamp: Long = 0
}