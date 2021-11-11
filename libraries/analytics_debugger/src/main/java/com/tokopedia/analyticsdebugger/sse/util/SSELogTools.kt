package com.tokopedia.analyticsdebugger.sse.util

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.analyticsdebugger.sse.domain.usecase.InsertSSELogUseCase
import com.tokopedia.analyticsdebugger.sse.ui.uimodel.SSELogGeneralInfoUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

/**
 * Created By : Jonathan Darwin on November 11, 2021
 */
class SSELogTools @Inject constructor(
    private val insertSSELogUseCase: InsertSSELogUseCase,
    private val dispatchers: CoroutineDispatchers,
    @Named("SSELogGsonPrettyPrinting") private val gson: Gson,
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
                insertSSELogUseCase.setParam(event, beautifyMessage(message), it)
                insertSSELogUseCase.executeOnBackground()
            }
        }
    }

    private fun beautifyMessage(message: String): String {
        return if(message.isEmpty()) return message
        else gson.toJson(JsonParser.parseString(message))
    }
}