package com.tokopedia.analyticsdebugger.websocket.data.repository

import com.tokopedia.analyticsdebugger.websocket.data.local.database.WebSocketLogDatabase
import com.tokopedia.analyticsdebugger.websocket.data.local.entity.TopchatWebSocketLogEntity
import com.tokopedia.analyticsdebugger.websocket.domain.repository.TopchatWebSocketLogRepository
import javax.inject.Inject

class TopchatWebSocketLogRepositoryImpl @Inject constructor(
    private val database: WebSocketLogDatabase
) : TopchatWebSocketLogRepository {

    override suspend fun get(
        query: String,
        source: String,
        limit: Int,
        offset: Int
    ) = database.topchatWebSocketLogDao().get(query, source, limit, offset)

    override suspend fun insert(
        entity: TopchatWebSocketLogEntity
    ) = database.topchatWebSocketLogDao().insert(entity)

    override suspend fun deleteAll() = database.topchatWebSocketLogDao().deleteAll()

    override suspend fun getSources() = database.topchatWebSocketLogDao().getSources()
}
