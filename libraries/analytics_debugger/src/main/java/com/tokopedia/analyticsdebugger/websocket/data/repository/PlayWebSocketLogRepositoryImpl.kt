package com.tokopedia.analyticsdebugger.websocket.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.analyticsdebugger.websocket.data.local.database.WebSocketLogDatabase
import com.tokopedia.analyticsdebugger.websocket.data.local.entity.PlayWebSocketLogEntity
import com.tokopedia.analyticsdebugger.websocket.domain.repository.PlayWebSocketLogRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on December 01, 2021
 */
class PlayWebSocketLogRepositoryImpl @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val database: WebSocketLogDatabase
) : PlayWebSocketLogRepository {

    override suspend fun get(
        query: String,
        source: String,
        limit: Int,
        offset: Int
    ): List<PlayWebSocketLogEntity> = withContext(dispatchers.io) {
        database.playWebSocketLogDao().get(query, source, limit, offset)
    }

    override suspend fun insert(playWebSocketLogEntity: PlayWebSocketLogEntity) {
        withContext(dispatchers.io) {
            database.playWebSocketLogDao().insert(playWebSocketLogEntity)
        }
    }

    override suspend fun deleteAll() {
        withContext(dispatchers.io) {
            database.playWebSocketLogDao().deleteAll()
        }
    }

    override suspend fun getSources(): List<String> = withContext(dispatchers.io) {
        database.playWebSocketLogDao().getSources()
    }
}
