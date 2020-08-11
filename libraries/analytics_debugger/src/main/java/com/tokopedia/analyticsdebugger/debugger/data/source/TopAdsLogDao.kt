package com.tokopedia.analyticsdebugger.debugger.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

import com.tokopedia.analyticsdebugger.database.TopAdsLogDB

import rx.Observable

/**
 * Created by meta on 23/05/19.
 */
@Dao
interface TopAdsLogDao {

    @get:Query("SELECT * FROM topads_log LIMIT 5")
    val data: List<TopAdsLogDB>

    @get:Query("SELECT COUNT(id) FROM topads_log")
    val count: Int

    @Query("DELETE FROM topads_log")
    fun deleteAll()

    @Insert
    fun insertAll(vararg topAdsLogDbs: TopAdsLogDB)

    @Query("SELECT * FROM topads_log WHERE url LIKE :keyword OR eventType LIKE :keyword OR sourceName LIKE :keyword " + "ORDER BY timestamp DESC LIMIT 20 OFFSET :offset")
    fun getData(keyword: String, offset: Int): List<TopAdsLogDB>

    @Query("SELECT * FROM topads_log WHERE url LIKE :keyword OR eventType LIKE :keyword OR sourceName LIKE :keyword " + "ORDER BY timestamp DESC")
    fun getAllData(keyword: String): List<TopAdsLogDB>

    @Update
    fun updateItem(vararg item: TopAdsLogDB)
}
