package com.tokopedia.analyticsdebugger.sse.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created By : Jonathan Darwin on November 09, 2021
 */

const val SSE_LOG_TABLE_NAME = "sse_log"

@Entity(tableName = SSE_LOG_TABLE_NAME)
data class SSELogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "channel_id")
    val channelId: String,

    @ColumnInfo(name = "page_source")
    val pageSource: String,

    @ColumnInfo(name = "gc_token")
    val gcToken: String,

    @ColumnInfo(name = "event")
    val event: String,

    @ColumnInfo(name = "message")
    val message: String,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long,
)