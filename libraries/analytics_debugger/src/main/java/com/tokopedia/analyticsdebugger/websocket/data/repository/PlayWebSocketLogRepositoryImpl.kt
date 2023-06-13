package com.tokopedia.analyticsdebugger.websocket.data.repository

import com.tokopedia.analyticsdebugger.websocket.data.local.database.WebSocketLogDatabase
import com.tokopedia.analyticsdebugger.websocket.data.local.entity.PlayWebSocketLogEntity
import com.tokopedia.analyticsdebugger.websocket.domain.repository.PlayWebSocketLogRepository
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on December 01, 2021
 */
class PlayWebSocketLogRepositoryImpl @Inject constructor(
    private val database: WebSocketLogDatabase
) : PlayWebSocketLogRepository {

    override suspend fun get(
        query: String,
        source: String,
        limit: Int,
        offset: Int
    ) = database.playWebSocketLogDao().get(query, source, limit, offset)

    override suspend fun insert(
        playWebSocketLogEntity: PlayWebSocketLogEntity
    ) = database.playWebSocketLogDao().insert(playWebSocketLogEntity)

    override suspend fun deleteAll() = database.playWebSocketLogDao().deleteAll()

    override suspend fun getSources() = database.playWebSocketLogDao().getSources()
}
