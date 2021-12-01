package com.tokopedia.analyticsdebugger.websocket.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tokopedia.analyticsdebugger.websocket.data.local.entity.WebSocketLogEntity

/**
 * Created By : Jonathan Darwin on December 01, 2021
 */

@Dao
interface WebSocketLogDao {

    @Insert
    fun insert(websocketLog: WebSocketLogEntity)

    @Query("DELETE FROM websocket_log")
    fun deleteAll()

    @Query("SELECT * FROM websocket_log WHERE channel_id LIKE :query OR page_source LIKE :query OR gc_token lIKE :query OR event LIKE :query OR message LIKE :query ORDER BY timestamp DESC LIMIT 50")
    fun get(query: String): List<WebSocketLogEntity>
}