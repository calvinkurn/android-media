package com.tokopedia.logger.datasource.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "log_table")
data class Logger (
    @PrimaryKey
    @ColumnInfo(name = "timestamp")
    var timeStamp: Long,

    @ColumnInfo(name = "server_channel")
    var serverChannel: String,

    @ColumnInfo(name = "priority")
    var priority: Int,

    @ColumnInfo(name = "message", typeAffinity = ColumnInfo.TEXT)
    var message: String
)