package com.tokopedia.analyticsdebugger.websocket.ui.mapper

import com.tokopedia.analyticsdebugger.util.DateTimeUtil
import com.tokopedia.analyticsdebugger.websocket.data.local.entity.PlayWebSocketLogEntity
import com.tokopedia.analyticsdebugger.websocket.data.local.entity.TopchatWebSocketLogEntity
import com.tokopedia.analyticsdebugger.websocket.domain.usecase.GetSourcesLogUseCase
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.info.PlayUiModel
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.info.TopchatUiModel
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogUiModel
import com.tokopedia.analyticsdebugger.websocket.ui.view.ChipModel
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on December 01, 2021
 */
class WebSocketLogMapper @Inject constructor() {

    fun mapPlayEntityToUiModel(entities: List<PlayWebSocketLogEntity>) = entities.map {
        WebSocketLogUiModel(
            id = it.id,
            play = PlayUiModel(
                source = it.source.ifEmpty { "-" },
                channelId = it.channelId.ifEmpty { "-" },
                gcToken = it.gcToken.ifEmpty { "-" },
                warehouseId = it.warehouseId.ifEmpty { "-" },
                header = it.header.ifEmpty { "-" },
            ),
            event = it.event,
            message = it.message,
            dateTime = DateTimeUtil.formatDate(it.timestamp)
        )
    }

    fun mapTopchatEntityToUiModel(entities: List<TopchatWebSocketLogEntity>) = entities.map {
        WebSocketLogUiModel(
            id = it.id,
            topchat = TopchatUiModel(
                source = it.source.ifEmpty { "-" },
                code = it.code.ifEmpty { "-" },
                messageId = it.messageId.ifEmpty { "-" },
                header = it.header
            ),
            event = it.event,
            message = it.message,
            dateTime = DateTimeUtil.formatDate(it.timestamp)
        )
    }

    fun mapSources(sources: List<String>) = sources.map {
        ChipModel(
            label = it,
            value = if (it == GetSourcesLogUseCase.ALL) "" else it,
            selected = it == GetSourcesLogUseCase.ALL,
        )
    }
}
