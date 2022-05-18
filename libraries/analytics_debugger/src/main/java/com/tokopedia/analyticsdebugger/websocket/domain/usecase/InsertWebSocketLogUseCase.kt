package com.tokopedia.analyticsdebugger.websocket.domain.usecase

import com.tokopedia.analyticsdebugger.websocket.data.local.entity.WebSocketLogEntity
import com.tokopedia.analyticsdebugger.websocket.domain.repository.WebSocketLogRespository
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogGeneralInfoUiModel
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogUiModel
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on December 01, 2021
 */
class InsertWebSocketLogUseCase @Inject constructor(
    private val webSocketLogRespository: WebSocketLogRespository,
): UseCase<Unit>()  {

    private var param: WebSocketLogUiModel? = null

    fun setParam(event: String, message: String, generalInfo: WebSocketLogGeneralInfoUiModel) {
        param = WebSocketLogUiModel(
            event = event,
            generalInfo = generalInfo,
            message = message,
            dateTime = "",
        )
    }

    override suspend fun executeOnBackground() {
        param?.let {
            val request = WebSocketLogEntity(
                source = it.generalInfo.source,
                channelId = it.generalInfo.channelId,
                gcToken = it.generalInfo.gcToken,
                event = it.event,
                message = it.message,
                timestamp = System.currentTimeMillis()
            )
            webSocketLogRespository.insert(request)
        }
    }
}