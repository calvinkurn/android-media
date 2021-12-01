package com.tokopedia.analyticsdebugger.websocket.domain.usecase

import com.tokopedia.analyticsdebugger.websocket.domain.repository.WebSocketLogRespository
import com.tokopedia.analyticsdebugger.websocket.ui.mapper.WebSocketLogMapper
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogUiModel
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on December 01, 2021
 */
class GetWebSocketLogUseCase @Inject constructor(
    private val webSocketLogRepository: WebSocketLogRespository,
    private val webSocketLogMapper: WebSocketLogMapper
): UseCase<List<WebSocketLogUiModel>>() {

    private var query: String = ""

    fun setParam(query: String) {
        this.query = "%$query%"
    }

    override suspend fun executeOnBackground(): List<WebSocketLogUiModel> {
        val response = webSocketLogRepository.get(query)
        return webSocketLogMapper.mapEntityToUiModel(response)
    }
}