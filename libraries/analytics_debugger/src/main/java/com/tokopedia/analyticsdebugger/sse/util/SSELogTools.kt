package com.tokopedia.analyticsdebugger.sse.util

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.analyticsdebugger.sse.domain.usecase.InsertSSELogUseCase
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

    fun sendLog(event: String, channelId: String, pageSource: String, gcToken: String) {
        sendLog(event, convertToJson(channelId, pageSource, gcToken))
    }

    fun sendLog(event: String, message: String) {
        CoroutineScope(dispatchers.io).launch {
            insertSSELogUseCase.setParam(event, message)
            insertSSELogUseCase.executeOnBackground()
        }
    }

    private fun convertToJson(channelId: String, pageSource: String, gcToken: String) = mapOf(
        "channelId" to channelId,
        "pageSource" to pageSource,
        "gcToken" to gcToken
    ).toString()
}