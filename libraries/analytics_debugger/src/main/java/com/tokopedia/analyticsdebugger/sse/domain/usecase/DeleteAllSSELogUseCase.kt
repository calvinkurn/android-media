package com.tokopedia.analyticsdebugger.sse.domain.usecase

import com.tokopedia.analyticsdebugger.sse.domain.repository.SSELogRepository
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 11, 2021
 */
class DeleteAllSSELogUseCase @Inject constructor(
    private val sseLogRepository: SSELogRepository
): UseCase<Unit>() {

    override suspend fun executeOnBackground() {
        sseLogRepository.deleteAll()
    }
}