package com.tokopedia.analyticsdebugger.debugger.ws

import android.content.Context
import com.tokopedia.analyticsdebugger.debugger.BaseWebSocketLogger
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.TopchatWebSocketLogDetailInfoUiModel
import kotlinx.coroutines.launch

class TopchatWebSocketLogger constructor(mContext: Context) : BaseWebSocketLogger(mContext) {

    private var detailInfo: TopchatWebSocketLogDetailInfoUiModel? = null

    override fun init(data: String) {
        detailInfo = parseDetailInfo(data)
    }

    override fun send(event: String, message: String) {
        launch {
            detailInfo?.let {
                insertWebSocketLogUseCase.setParam(event, beautifyMessage(message), it)
                insertWebSocketLogUseCase.executeOnBackground()
            }
        }
    }

    private fun parseDetailInfo(detailInfo: String): TopchatWebSocketLogDetailInfoUiModel? {
        return try {
            gson.fromJson(detailInfo, TopchatWebSocketLogDetailInfoUiModel::class.java)
        } catch (e: Exception) {
            null
        }
    }
}
