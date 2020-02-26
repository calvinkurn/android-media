package com.tokopedia.analyticsdebugger.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val GTM_ERROR_TABLE_NAME = "gtm_error"

@Entity(tableName = GTM_ERROR_TABLE_NAME)
class GtmErrorLogDB {
    @PrimaryKey
    var timestamp: Long = 0
    @ColumnInfo(name = "data")
    var data: String? = null
}