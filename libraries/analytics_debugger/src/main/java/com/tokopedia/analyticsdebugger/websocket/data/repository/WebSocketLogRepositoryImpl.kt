package com.tokopedia.analyticsdebugger.websocket.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.analyticsdebugger.websocket.data.local.database.WebSocketLogDatabase
import com.tokopedia.analyticsdebugger.websocket.data.local.entity.WebSocketLogEntity
import com.tokopedia.analyticsdebugger.websocket.domain.repository.WebSocketLogRespository
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on December 01, 2021
 */
class WebSocketLogRepositoryImpl @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val database: WebSocketLogDatabase
): WebSocketLogRespository {

    override suspend fun get(query: String, source: String, limit: Int, offset: Int): List<WebSocketLogEntity> = withContext(dispatchers.io) {
        database.webSocketLogDao().get(query, source, limit, offset)
    }

    override suspend fun insert(webSocketLogEntity: WebSocketLogEntity) {
        withContext(dispatchers.io) {
            database.webSocketLogDao().insert(webSocketLogEntity)
        }
    }

    override suspend fun deleteAll() {
        withContext(dispatchers.io) {
            database.webSocketLogDao().deleteAll()
        }
    }

    override suspend fun getSources(): List<String> = withContext(dispatchers.io) {
        database.webSocketLogDao().getSources()
    }
}