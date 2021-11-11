package com.tokopedia.analyticsdebugger.sse.util

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.analyticsdebugger.sse.domain.usecase.InsertSSELogUseCase
import com.tokopedia.analyticsdebugger.sse.ui.uimodel.SSELogGeneralInfoUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 11, 2021
 */
class SSELogTools @Inject constructor(
    private val insertSSELogUseCase: InsertSSELogUseCase,
    private val dispatchers: CoroutineDispatchers,
) {

    private var generalInfo: SSELogGeneralInfoUiModel? = null

    fun initLog(channelId: String, pageSource: String, gcToken: String) {
        generalInfo = SSELogGeneralInfoUiModel(
            channelId = channelId,
            pageSource = pageSource,
            gcToken = gcToken
        )
    }

    fun sendLog(event: String, message: String = "") {
        CoroutineScope(dispatchers.io).launch {
            generalInfo?.let {
                insertSSELogUseCase.setParam(event, message, it)
                insertSSELogUseCase.executeOnBackground()
            }
        }
    }
}