package com.tokopedia.analyticsdebugger.sse.ui.mapper

import com.tokopedia.analyticsdebugger.sse.data.local.entity.SSELogEntity
import com.tokopedia.analyticsdebugger.sse.ui.uimodel.SSELogUiModel

/**
 * Created By : Jonathan Darwin on November 09, 2021
 */
class SSELogMapper {

    fun mapEntityToUiModel(entities: List<SSELogEntity>) = entities.map {
        SSELogUiModel(
            id = it.id,
            event = it.event,
            message = it.message,
            timestamp = it.timestamp
        )
    }
}