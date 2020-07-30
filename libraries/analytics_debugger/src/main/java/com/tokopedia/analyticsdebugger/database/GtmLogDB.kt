package com.tokopedia.analyticsdebugger.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author okasurya on 5/14/18.
 */

const val GTM_LOG_TABLE_NAME = "gtm_log"
@Entity(tableName = GTM_LOG_TABLE_NAME)
class GtmLogDB {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ColumnInfo(name = "data")
    var data: String? = null

    @ColumnInfo(name = "name")
    var name: String? = null

    @ColumnInfo(name = "category")
    var category: String? = null

    @ColumnInfo(name = "timestamp")
    var timestamp: Long = 0
}