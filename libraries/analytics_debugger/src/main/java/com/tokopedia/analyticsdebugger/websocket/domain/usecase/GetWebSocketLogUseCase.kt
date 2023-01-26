package com.tokopedia.analyticsdebugger.websocket.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.analyticsdebugger.websocket.domain.param.GetWebSocketLogParam
import com.tokopedia.analyticsdebugger.websocket.domain.repository.PlayWebSocketLogRepository
import com.tokopedia.analyticsdebugger.websocket.domain.repository.TopchatWebSocketLogRepository
import com.tokopedia.analyticsdebugger.websocket.ui.mapper.WebSocketLogMapper
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.PageSource
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogUiModel
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on December 01, 2021
 */
class GetWebSocketLogUseCase @Inject constructor(
    private val playWebSocketLogRepository: PlayWebSocketLogRepository,
    private val topchatWebSocketLogRepository: TopchatWebSocketLogRepository,
    private val webSocketLogMapper: WebSocketLogMapper,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<GetWebSocketLogParam, List<WebSocketLogUiModel>>(dispatchers.io) {

    override suspend fun execute(params: GetWebSocketLogParam): List<WebSocketLogUiModel> {
        val offset = params.limit * params.page

        return if (params.pageSource == PageSource.PLAY) {
            webSocketLogMapper.mapPlayEntityToUiModel(
                playWebSocketLogRepository.get(
                    params.query,
                    params.source,
                    params.limit,
                    offset
                )
            )
        } else if (params.pageSource == PageSource.TOPCHAT) {
            webSocketLogMapper.mapTopchatEntityToUiModel(
                topchatWebSocketLogRepository.get(
                    params.query,
                    params.source,
                    params.limit,
                    offset
                )
            )
        } else {
            emptyList()
        }
    }

    override fun graphqlQuery() = "" // no-op
}
