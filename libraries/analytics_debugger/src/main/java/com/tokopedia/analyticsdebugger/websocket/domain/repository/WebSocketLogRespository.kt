package com.tokopedia.analyticsdebugger.websocket.domain.repository
import com.tokopedia.analyticsdebugger.websocket.data.local.entity.WebSocketLogEntity

/**
 * Created By : Jonathan Darwin on December 01, 2021
 */
interface WebSocketLogRespository {
    suspend fun get(query: String = "", source: String, limit: Int, offset: Int): List<WebSocketLogEntity>

    suspend fun insert(webSocketLogEntity: WebSocketLogEntity)

    suspend fun deleteAll()

    suspend fun getSources(): List<String>
}