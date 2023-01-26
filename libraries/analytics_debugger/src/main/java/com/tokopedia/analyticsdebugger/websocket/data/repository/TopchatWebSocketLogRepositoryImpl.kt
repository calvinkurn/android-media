package com.tokopedia.analyticsdebugger.websocket.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.analyticsdebugger.websocket.data.local.database.WebSocketLogDatabase
import com.tokopedia.analyticsdebugger.websocket.data.local.entity.PlayWebSocketLogEntity
import com.tokopedia.analyticsdebugger.websocket.data.local.entity.TopchatWebSocketLogEntity
import com.tokopedia.analyticsdebugger.websocket.domain.repository.TopchatWebSocketLogRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TopchatWebSocketLogRepositoryImpl @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val database: WebSocketLogDatabase
) : TopchatWebSocketLogRepository {

    override suspend fun get(
        query: String,
        source: String,
        limit: Int,
        offset: Int
    ): List<TopchatWebSocketLogEntity> = withContext(dispatchers.io) {
        database.topchatWebSocketLogDao().get(query, source, limit, offset)
    }

    override suspend fun insert(entity: TopchatWebSocketLogEntity) {
        withContext(dispatchers.io) {
            database.topchatWebSocketLogDao().insert(entity)
        }
    }

    override suspend fun deleteAll() {
        withContext(dispatchers.io) {
            database.topchatWebSocketLogDao().deleteAll()
        }
    }

    override suspend fun getSources(): List<String> = withContext(dispatchers.io) {
        database.topchatWebSocketLogDao().getSources()
    }
}
