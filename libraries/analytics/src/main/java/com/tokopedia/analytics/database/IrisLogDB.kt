package com.tokopedia.analytics.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val SAVE_TABLE_IRIS_NAME = "iris_save_log"
const val SEND_TABLE_IRIS_NAME = "iris_send_log"

@Entity(tableName = SAVE_TABLE_IRIS_NAME)
class IrisSaveLogDB {
    @PrimaryKey
    var timestamp: Long = 0
    @ColumnInfo(name = "data")
    var data: String? = null
}

@Entity(tableName = SEND_TABLE_IRIS_NAME)
class IrisSendLogDB {
    @PrimaryKey
    var timestamp: Long = 0
    @ColumnInfo(name = "data")
    var data: String? = null

}