package com.tokopedia.analyticsdebugger.websocket.domain.usecase

import com.tokopedia.analyticsdebugger.websocket.domain.repository.WebSocketLogRespository
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on December 01, 2021
 */
class DeleteAllWebSocketLogUseCase @Inject constructor(
    private val webSocketLogRepository: WebSocketLogRespository,
): UseCase<Unit>() {

    override suspend fun executeOnBackground() {
        webSocketLogRepository.deleteAll()
    }
}