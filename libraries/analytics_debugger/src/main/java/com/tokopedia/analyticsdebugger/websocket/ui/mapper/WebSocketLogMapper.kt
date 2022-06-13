package com.tokopedia.analyticsdebugger.websocket.ui.mapper

import com.tokopedia.analyticsdebugger.util.DateTimeUtil
import com.tokopedia.analyticsdebugger.websocket.data.local.entity.WebSocketLogEntity
import com.tokopedia.analyticsdebugger.websocket.domain.usecase.GetSourcesLogUseCase
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogGeneralInfoUiModel
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogUiModel
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketSourceUiModel
import com.tokopedia.analyticsdebugger.websocket.ui.view.ChipModel
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on December 01, 2021
 */
class WebSocketLogMapper @Inject constructor() {

    fun mapEntityToUiModel(entities: List<WebSocketLogEntity>) = entities.map {
        WebSocketLogUiModel(
            id = it.id,
            generalInfo = WebSocketLogGeneralInfoUiModel(
                source = if(it.source.isEmpty()) "-" else it.source,
                channelId = if(it.channelId.isEmpty()) "-" else it.channelId,
                gcToken = if(it.gcToken.isEmpty()) "-" else it.gcToken,
            ),
            event = it.event,
            message = it.message,
            dateTime = DateTimeUtil.formatDate(it.timestamp)
        )
    }

    fun mapSources(sources: List<String>) = sources.map {
        ChipModel(
            label = it,
            value = if(it == GetSourcesLogUseCase.ALL) "" else it,
            selected = it == GetSourcesLogUseCase.ALL,
        )
    }
}