package com.tokopedia.analyticsdebugger.debugger.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

import com.tokopedia.analyticsdebugger.database.FpmLogDB

import rx.Observable

/**
 * Created by meta on 23/05/19.
 */
@Dao
interface FpmLogDao {

    @get:Query("SELECT * FROM fpm_log LIMIT 5")
    val data: List<FpmLogDB>

    @get:Query("SELECT * FROM fpm_log ORDER BY timestamp DESC")
    val allData: List<FpmLogDB>

    @get:Query("SELECT COUNT(id) FROM fpm_log")
    val count: Int

    @Query("DELETE FROM fpm_log")
    fun deleteAll()

    @Insert
    fun insertAll(vararg fpmLogDbs: FpmLogDB)

    @Query("SELECT * FROM fpm_log WHERE tracename LIKE :keyword OR attributes LIKE :keyword OR metrics LIKE :keyword " + "ORDER BY timestamp DESC LIMIT 20 OFFSET :offset")
    fun getData(keyword: String, offset: Int): List<FpmLogDB>
}
