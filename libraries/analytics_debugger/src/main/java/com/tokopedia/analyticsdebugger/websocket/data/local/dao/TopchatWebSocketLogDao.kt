package com.tokopedia.analyticsdebugger.websocket.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tokopedia.analyticsdebugger.websocket.data.local.entity.TopchatWebSocketLogEntity

@Dao
interface TopchatWebSocketLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(websocketLog: TopchatWebSocketLogEntity)

    @Query("DELETE FROM topchat_websocket_log")
    suspend fun deleteAll()

    @Query("SELECT * FROM topchat_websocket_log WHERE (event LIKE :query OR message LIKE :query) AND source LIKE :source ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    suspend fun get(query: String, source: String, limit: Int, offset: Int): List<TopchatWebSocketLogEntity>

    @Query("SELECT DISTINCT source FROM topchat_websocket_log")
    suspend fun getSources(): List<String>
}
