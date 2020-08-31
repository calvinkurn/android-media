package com.tokopedia.analyticsdebugger.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val STATUS_PENDING = "Pending"
const val STATUS_DATA_NOT_FOUND = "Data not found"
const val STATUS_NOT_MATCH = "Not match"
const val STATUS_MATCH = "Match"

const val TOPADS_LOG_TABLE_NAME = "topads_log"

@Entity(tableName = TOPADS_LOG_TABLE_NAME)
data class TopAdsLogDB (

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    @ColumnInfo(name = "url")
    var url: String = "",

    @ColumnInfo(name = "eventType")
    var eventType: String = "",

    @ColumnInfo(name = "sourceName")
    var sourceName: String = "",

    @ColumnInfo(name = "productId")
    var productId: String = "",

    @ColumnInfo(name = "productName")
    var productName: String = "",

    @ColumnInfo(name = "imageUrl")
    var imageUrl: String = "",

    @ColumnInfo(name = "eventStatus")
    var eventStatus: String = "",

    @ColumnInfo(name = "timestamp")
    var timestamp: Long = 0,

    @ColumnInfo(name = "fullResponse")
    var fullResponse: String = ""
)