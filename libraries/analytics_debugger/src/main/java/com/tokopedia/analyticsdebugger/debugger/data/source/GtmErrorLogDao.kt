package com.tokopedia.analyticsdebugger.debugger.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tokopedia.analyticsdebugger.database.GTM_ERROR_TABLE_NAME
import com.tokopedia.analyticsdebugger.database.GtmErrorLogDB

@Dao
interface GtmErrorLogDao {
    @Query("DELETE FROM $GTM_ERROR_TABLE_NAME")
    fun deleteAll()

    @Insert
    fun insertAll(vararg gtmErrorLogDB: GtmErrorLogDB)

    @get:Query("SELECT * FROM $GTM_ERROR_TABLE_NAME LIMIT 100")
    val data: List<GtmErrorLogDB?>?

    @Query("SELECT * FROM $GTM_ERROR_TABLE_NAME WHERE data LIKE :keyword ORDER BY timestamp DESC LIMIT 100 OFFSET :offset")
    fun getData(keyword: String?, offset: Int): List<GtmErrorLogDB?>?

    @get:Query("SELECT COUNT(timestamp) FROM $GTM_ERROR_TABLE_NAME")
    val count: Int
}
