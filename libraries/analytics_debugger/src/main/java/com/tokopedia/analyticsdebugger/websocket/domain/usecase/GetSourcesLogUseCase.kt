package com.tokopedia.analyticsdebugger.websocket.domain.usecase

import com.tokopedia.analyticsdebugger.websocket.domain.repository.WebSocketLogRespository
import com.tokopedia.analyticsdebugger.websocket.ui.mapper.WebSocketLogMapper
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketSourceUiModel
import com.tokopedia.analyticsdebugger.websocket.ui.view.ChipModel
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on December 21, 2021
 */
class GetSourcesLogUseCase @Inject constructor(
    private val webSocketLogRepository: WebSocketLogRespository,
    private val webSocketLogMapper: WebSocketLogMapper
): UseCase<List<ChipModel>>() {

    override suspend fun executeOnBackground(): List<ChipModel> {
        return webSocketLogMapper.mapSources(
            listOf(ALL) + webSocketLogRepository.getSources()
        )
    }

    companion object {
        const val ALL = "All"
    }
}