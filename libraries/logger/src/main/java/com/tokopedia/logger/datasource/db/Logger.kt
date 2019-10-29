package com.tokopedia.logger.datasource.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "log_table")
data class Logger (
    @PrimaryKey @ColumnInfo(name = "TimeStamp") var timeStamp: Long,
    @ColumnInfo(name = "Priority") var priority: Int,
    @ColumnInfo(name = "Message", typeAffinity = ColumnInfo.TEXT) var message: String
)