package com.tokopedia.analyticsdebugger.sse.domain.repository

import com.tokopedia.analyticsdebugger.sse.data.local.entity.SSELogEntity

/**
 * Created By : Jonathan Darwin on November 09, 2021
 */
interface SSELogRepository {

    suspend fun get(query: String = ""): List<SSELogEntity>

    suspend fun insert(sseLogEntity: SSELogEntity)

    suspend fun deleteAll()
}