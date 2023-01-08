package com.tokopedia.journeydebugger.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val JOURNEY_LOG_TABLE_NAME = "journey_log"

@Entity(tableName = JOURNEY_LOG_TABLE_NAME)
class JourneyLogDB {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ColumnInfo(name = "journey")
    var journey: String? = null

    @ColumnInfo(name = "timestamp")
    var timestamp: Long = 0
}
