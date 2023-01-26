package com.tokopedia.analyticsdebugger.websocket.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.analyticsdebugger.websocket.domain.repository.PlayWebSocketLogRepository
import com.tokopedia.analyticsdebugger.websocket.domain.repository.TopchatWebSocketLogRepository
import com.tokopedia.analyticsdebugger.websocket.ui.mapper.WebSocketLogMapper
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogPageSource
import com.tokopedia.analyticsdebugger.websocket.ui.view.ChipModel
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on December 21, 2021
 */
class GetSourcesLogUseCase @Inject constructor(
    private val playWebSocketLogRepository: PlayWebSocketLogRepository,
    private val topchatWebSocketLogRepository: TopchatWebSocketLogRepository,
    private val webSocketLogMapper: WebSocketLogMapper,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<WebSocketLogPageSource, List<ChipModel>>(dispatchers.io) {

    override suspend fun execute(params: WebSocketLogPageSource): List<ChipModel> {
        if (params == WebSocketLogPageSource.NONE) return emptyList()

        val sources = when (params) {
            WebSocketLogPageSource.PLAY -> playWebSocketLogRepository.getSources()
            WebSocketLogPageSource.TOPCHAT -> topchatWebSocketLogRepository.getSources()
            else -> emptyList()
        }

        return webSocketLogMapper.mapSources(sources)
    }

    override fun graphqlQuery() = "" // no-op

    companion object {
        const val ALL = "All"
    }
}
