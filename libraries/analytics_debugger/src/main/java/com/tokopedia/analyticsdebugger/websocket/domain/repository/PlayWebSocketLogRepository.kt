package com.tokopedia.analyticsdebugger.websocket.domain.repository
import com.tokopedia.analyticsdebugger.websocket.data.local.entity.PlayWebSocketLogEntity

/**
 * Created By : Jonathan Darwin on December 01, 2021
 */
interface PlayWebSocketLogRepository {
    suspend fun get(query: String = "", source: String, limit: Int, offset: Int): List<PlayWebSocketLogEntity>
    suspend fun insert(playWebSocketLogEntity: PlayWebSocketLogEntity)
    suspend fun deleteAll()
    suspend fun getSources(): List<String>
}
