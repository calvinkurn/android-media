package com.tokopedia.analyticsdebugger.sse.ui.mapper

import com.tokopedia.analyticsdebugger.sse.data.local.entity.SSELogEntity
import com.tokopedia.analyticsdebugger.sse.ui.uimodel.SSELogGeneralInfoUiModel
import com.tokopedia.analyticsdebugger.util.DateTimeUtil
import com.tokopedia.analyticsdebugger.sse.ui.uimodel.SSELogUiModel
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 09, 2021
 */
class SSELogMapper @Inject constructor() {

    fun mapEntityToUiModel(entities: List<SSELogEntity>) = entities.map {
        SSELogUiModel(
            id = it.id,
            generalInfo = SSELogGeneralInfoUiModel(
                channelId = if(it.channelId.isEmpty()) "-" else it.channelId,
                pageSource = if(it.pageSource.isEmpty()) "-" else it.pageSource,
                gcToken = if(it.gcToken.isEmpty()) "-" else it.gcToken,
            ),
            event = it.event,
            message = it.message,
            dateTime = DateTimeUtil.formatDate(it.timestamp)
        )
    }
}