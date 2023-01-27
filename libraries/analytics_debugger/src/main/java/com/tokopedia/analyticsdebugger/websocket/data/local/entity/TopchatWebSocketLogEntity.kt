package com.tokopedia.analyticsdebugger.websocket.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created By : Jonathan Darwin on December 01, 2021
 */

const val WS_TOPCHAT_TABLE_NAME = "topchat_websocket_log"

@Entity(tableName = WS_TOPCHAT_TABLE_NAME)
data class TopchatWebSocketLogEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "source")
    val source: String,

    @ColumnInfo(name = "message_id")
    val messageId: String,

    @ColumnInfo(name = "code")
    val code: String,

    @ColumnInfo(name = "event")
    val event: String,

    @ColumnInfo(name = "message")
    val message: String,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long,
)
