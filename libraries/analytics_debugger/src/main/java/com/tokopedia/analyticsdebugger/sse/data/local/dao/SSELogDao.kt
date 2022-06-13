package com.tokopedia.analyticsdebugger.sse.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tokopedia.analyticsdebugger.sse.data.local.entity.SSELogEntity

/**
 * Created By : Jonathan Darwin on November 09, 2021
 */
@Dao
interface SSELogDao {

    @Insert
    fun insert(sseLog: SSELogEntity)

    @Query("DELETE FROM sse_log")
    fun deleteAll()

    @Query("SELECT * FROM sse_log WHERE channel_id LIKE :query OR page_source LIKE :query OR gc_token lIKE :query OR event LIKE :query OR message LIKE :query ORDER BY timestamp DESC LIMIT 50")
    fun get(query: String): List<SSELogEntity>
}