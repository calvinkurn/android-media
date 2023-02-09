package com.tokopedia.analyticsdebugger.websocket.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
* Created By : Jonathan Darwin on December 01, 2021
*/

const val WEBSOCKET_LOG_TABLE_NAME = "play_websocket_log"

@Entity(tableName = WEBSOCKET_LOG_TABLE_NAME)
data class PlayWebSocketLogEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "source")
    val source: String,

    @ColumnInfo(name = "channel_id")
    val channelId: String,

    @ColumnInfo(name = "gc_token")
    val gcToken: String,

    @ColumnInfo(name = "event")
    val event: String,

    @ColumnInfo(name = "message")
    val message: String,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long,

    @ColumnInfo(name = "warehouse_id")
    val warehouseId: String,
)
