package com.tokopedia.analyticsdebugger.websocket.domain.repository

import com.tokopedia.analyticsdebugger.websocket.data.local.entity.TopchatWebSocketLogEntity

interface TopchatWebSocketLogRepository {
    suspend fun get(query: String = "", source: String, limit: Int, offset: Int): List<TopchatWebSocketLogEntity>
    suspend fun insert(entity: TopchatWebSocketLogEntity)
    suspend fun deleteAll()
    suspend fun getSources(): List<String>
}
