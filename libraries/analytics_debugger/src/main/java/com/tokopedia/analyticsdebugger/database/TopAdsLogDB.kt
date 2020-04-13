package com.tokopedia.analyticsdebugger.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val TOPADS_LOG_TABLE_NAME = "topads_log"

@Entity(tableName = TOPADS_LOG_TABLE_NAME)
class TopAdsLogDB {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ColumnInfo(name = "url")
    var url: String? = null

    @ColumnInfo(name = "eventType")
    var eventType: String? = null

    @ColumnInfo(name = "sourceName")
    var sourceName: String? = null

    @ColumnInfo(name = "timestamp")
    var timestamp: Long = 0
}