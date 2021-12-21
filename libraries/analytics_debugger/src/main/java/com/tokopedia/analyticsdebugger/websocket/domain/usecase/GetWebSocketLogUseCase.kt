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
    private var source: String = ""
    private var page: Int = 0
    private var limit: Int = 0

    fun setParam(query: String, source: String, page: Int, limit: Int) {
        this.query = "%$query%"
        this.source = "%$source%"
        this.page = page
        this.limit = limit
    }

    override suspend fun executeOnBackground(): List<WebSocketLogUiModel> {
        val offset = limit * page
        val response = webSocketLogRepository.get(query, source, limit, offset)
        return webSocketLogMapper.mapEntityToUiModel(response)
    }
}