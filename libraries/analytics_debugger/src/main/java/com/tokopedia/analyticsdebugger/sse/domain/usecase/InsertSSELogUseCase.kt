package com.tokopedia.analyticsdebugger.sse.domain.usecase

import com.tokopedia.analyticsdebugger.sse.data.local.entity.SSELogEntity
import com.tokopedia.analyticsdebugger.sse.domain.repository.SSELogRepository
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 11, 2021
 */
class InsertSSELogUseCase @Inject constructor(
    private val sseLogRepository: SSELogRepository
): UseCase<Unit>() {

    private var event: String = ""
    private var message: String = ""

    fun setParam(event: String, message: String) {
        this.event = event
        this.message = message
    }

    override suspend fun executeOnBackground() {
        if(event.isEmpty() || message.isEmpty()) return

        val request = SSELogEntity(
            event = event,
            message = message,
            timestamp = System.currentTimeMillis()
        )
        sseLogRepository.insert(request)
    }
}