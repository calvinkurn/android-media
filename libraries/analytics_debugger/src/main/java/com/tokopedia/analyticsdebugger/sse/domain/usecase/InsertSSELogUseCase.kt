package com.tokopedia.analyticsdebugger.sse.domain.usecase

import com.tokopedia.analyticsdebugger.sse.data.local.entity.SSELogEntity
import com.tokopedia.analyticsdebugger.sse.domain.repository.SSELogRepository
import com.tokopedia.analyticsdebugger.sse.ui.uimodel.SSELogGeneralInfoUiModel
import com.tokopedia.analyticsdebugger.sse.ui.uimodel.SSELogUiModel
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 11, 2021
 */
class InsertSSELogUseCase @Inject constructor(
    private val sseLogRepository: SSELogRepository,
): UseCase<Unit>() {

    private var param: SSELogUiModel? = null

    fun setParam(event: String, message: String, generalInfo: SSELogGeneralInfoUiModel) {
        param = SSELogUiModel(
            event = event,
            generalInfo = generalInfo,
            message = message,
            dateTime = "",
        )
    }

    override suspend fun executeOnBackground() {
        param?.let {
            val request = SSELogEntity(
                channelId = it.generalInfo.channelId,
                pageSource = it.generalInfo.pageSource,
                gcToken = it.generalInfo.gcToken,
                event = it.event,
                message = it.message,
                timestamp = System.currentTimeMillis()
            )
            sseLogRepository.insert(request)
        }
    }
}