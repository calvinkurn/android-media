package com.tokopedia.analyticsdebugger.websocket.domain.usecase

import com.tokopedia.analyticsdebugger.websocket.data.local.entity.PlayWebSocketLogEntity
import com.tokopedia.analyticsdebugger.websocket.data.local.entity.TopchatWebSocketLogEntity
import com.tokopedia.analyticsdebugger.websocket.domain.repository.PlayWebSocketLogRepository
import com.tokopedia.analyticsdebugger.websocket.domain.repository.TopchatWebSocketLogRepository
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.PlayWebSocketLogGeneralInfoUiModel
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.TopchatWebSocketLogDetailInfoUiModel
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogUiModel
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on December 01, 2021
 */
class InsertWebSocketLogUseCase @Inject constructor(
    private val playWebSocketLogRepository: PlayWebSocketLogRepository,
    private val topchatWebSocketLogRepository: TopchatWebSocketLogRepository,
): UseCase<Unit>()  {

    private var param: WebSocketLogUiModel? = null

    fun setParam(event: String, message: String, generalInfo: PlayWebSocketLogGeneralInfoUiModel) {
        param = WebSocketLogUiModel(
            event = event,
            playGeneralInfo = generalInfo,
            message = message,
            dateTime = "",
        )
    }

    fun setParam(event: String, message: String, detailInfo: TopchatWebSocketLogDetailInfoUiModel) {
        param = WebSocketLogUiModel(
            event = event,
            topchatDetailInfo = detailInfo,
            message = message,
            dateTime = "",
        )
    }

    override suspend fun executeOnBackground() {
        param?.let {
            if (it.playGeneralInfo != null) {
                playWebSocketLogRepository.insert(
                    PlayWebSocketLogEntity(
                        source = it.playGeneralInfo.source,
                        channelId = it.playGeneralInfo.channelId,
                        gcToken = it.playGeneralInfo.gcToken,
                        event = it.event,
                        message = it.message,
                        timestamp = System.currentTimeMillis(),
                        warehouseId = it.playGeneralInfo.warehouseId,
                    )
                )
            } else if (it.topchatDetailInfo != null) {
                topchatWebSocketLogRepository.insert(
                    TopchatWebSocketLogEntity(
                        source = it.topchatDetailInfo.source,
                        url = it.topchatDetailInfo.url,
                        code = it.topchatDetailInfo.code,
                        messageId = it.topchatDetailInfo.messageId,
                        event = it.event,
                        message = it.message,
                        timestamp = System.currentTimeMillis()
                    )
                )
            }
        }
    }
}
