package com.tokopedia.analyticsdebugger.sse.ui.mapper

import com.tokopedia.analyticsdebugger.sse.data.local.entity.SSELogEntity
import com.tokopedia.analyticsdebugger.sse.util.DateTimeUtil
import com.tokopedia.analyticsdebugger.sse.ui.uimodel.SSELogUiModel
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 09, 2021
 */
class SSELogMapper @Inject constructor() {

    fun mapEntityToUiModel(entities: List<SSELogEntity>) = entities.map {
        SSELogUiModel(
            id = it.id,
            event = it.event,
            message = it.message,
            dateTime = DateTimeUtil.formatDate(it.timestamp)
        )
    }
}