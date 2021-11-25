package com.tokopedia.analyticsdebugger.sse.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.analyticsdebugger.sse.data.local.database.SSELogDatabase
import com.tokopedia.analyticsdebugger.sse.data.local.entity.SSELogEntity
import com.tokopedia.analyticsdebugger.sse.domain.repository.SSELogRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 09, 2021
 */
class SSELogRepositoryImpl @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val database: SSELogDatabase
): SSELogRepository {
    override suspend fun get(query: String): List<SSELogEntity> = withContext(dispatchers.io) {
        database.sseLogDao().get(query)
    }

    override suspend fun insert(sseLogEntity: SSELogEntity) {
        withContext(dispatchers.io) {
            database.sseLogDao().insert(sseLogEntity)
        }
    }

    override suspend fun deleteAll() {
        withContext(dispatchers.io) {
            database.sseLogDao().deleteAll()
        }
    }
}