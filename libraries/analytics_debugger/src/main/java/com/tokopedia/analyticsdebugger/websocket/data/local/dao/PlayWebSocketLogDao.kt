package com.tokopedia.analyticsdebugger.websocket.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tokopedia.analyticsdebugger.websocket.data.local.entity.PlayWebSocketLogEntity

/**
 * Created By : Jonathan Darwin on December 01, 2021
 */

@Dao
interface PlayWebSocketLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(websocketLog: PlayWebSocketLogEntity)

    @Query("DELETE FROM play_websocket_log")
    fun deleteAll()

    @Query("SELECT * FROM play_websocket_log WHERE (channel_id LIKE :query OR gc_token lIKE :query OR event LIKE :query OR message LIKE :query) AND source LIKE :source ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    fun get(query: String, source: String, limit: Int, offset: Int): List<PlayWebSocketLogEntity>

    @Query("SELECT DISTINCT source FROM play_websocket_log")
    fun getSources(): List<String>
}
