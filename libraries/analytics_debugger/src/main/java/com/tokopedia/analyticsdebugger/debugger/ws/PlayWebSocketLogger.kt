package com.tokopedia.analyticsdebugger.debugger.ws

import android.content.Context
import com.tokopedia.analyticsdebugger.debugger.BaseWebSocketLogger
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.PlayWebSocketLogGeneralInfoUiModel
import kotlinx.coroutines.launch

class PlayWebSocketLogger constructor(mContext: Context) : BaseWebSocketLogger(mContext) {

    private var generalInfo: PlayWebSocketLogGeneralInfoUiModel? = null

    override fun init(data: String) {
        generalInfo = parseGeneralInfo(data)
    }

    override fun send(event: String, message: String) {
        launch {
            generalInfo?.let {
                insertWebSocketLogUseCase.setParam(event, beautifyMessage(message), it)
                insertWebSocketLogUseCase.executeOnBackground()
            }
        }
    }

    private fun parseGeneralInfo(generalInfo: String): PlayWebSocketLogGeneralInfoUiModel? {
        return try {
            gson.fromJson(generalInfo, PlayWebSocketLogGeneralInfoUiModel::class.java)
        } catch (e: Exception) {
            null
        }
    }
}
