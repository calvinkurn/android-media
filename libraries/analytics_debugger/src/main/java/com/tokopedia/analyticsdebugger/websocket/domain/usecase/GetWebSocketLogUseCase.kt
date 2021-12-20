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
    private var page: Int = 0
    private var offset: Int = 0

    fun setParam(query: String, page: Int, offset: Int) {
        this.query = "%$query%"
        this.page = page
        this.offset = offset
    }

    override suspend fun executeOnBackground(): List<WebSocketLogUiModel> {
        val response = webSocketLogRepository.get(query, page, offset)
        return webSocketLogMapper.mapEntityToUiModel(response)
    }
}