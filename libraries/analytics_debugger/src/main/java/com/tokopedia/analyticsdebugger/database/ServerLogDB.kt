package com.tokopedia.analyticsdebugger.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val SERVER_LOG_TABLE_NAME = "server_log"

@Entity(tableName = SERVER_LOG_TABLE_NAME)
class ServerLogDB {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var timestamp: Long = 0
    @ColumnInfo(name = "data")
    var data: String? = null
}