package com.tokopedia.analyticsdebugger.debugger.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tokopedia.analyticsdebugger.AnalyticsSource

import com.tokopedia.analyticsdebugger.database.GtmLogDB

/**
 * Created by meta on 23/05/19.
 */
@Dao
interface GtmLogDao {

    @get:Query("SELECT * FROM gtm_log LIMIT 5")
    val data: List<GtmLogDB>

    @get:Query("SELECT COUNT(id) FROM gtm_log")
    val count: Int

    @Query("DELETE FROM gtm_log")
    fun deleteAll()

    @Insert
    fun insertAll(vararg gtmLogDbs: GtmLogDB)

    @Query("SELECT * FROM gtm_log WHERE name LIKE :keyword OR data LIKE :keyword OR category LIKE :keyword " + "ORDER BY timestamp DESC LIMIT 20 OFFSET :offset")
    fun getData(keyword: String, offset: Int): List<GtmLogDB>

    @Query("SELECT * FROM gtm_log")
    fun getAll(): List<GtmLogDB>

    @Query("SELECT * FROM gtm_log WHERE source IS :analyticsSource")
    fun getAll(@AnalyticsSource analyticsSource:String): List<GtmLogDB>

}
