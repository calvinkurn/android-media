package com.tokopedia.analyticsdebugger.debugger.ws

import android.content.Context
import com.tokopedia.analyticsdebugger.debugger.BaseWebSocketLogger
import com.tokopedia.analyticsdebugger.websocket.domain.param.InsertWebSocketLogParam
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.TopchatWebSocketLogDetailInfoUiModel
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogPageSource
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogUiModel
import kotlinx.coroutines.launch

class TopchatWebSocketLogger constructor(mContext: Context) : BaseWebSocketLogger(mContext) {

    private var detail: TopchatWebSocketLogDetailInfoUiModel? = null

    override fun init(data: String) {
        detail = gson.parseDetailInfo(data)
    }

    override fun send(event: String, message: String) {
        launch {
            val param = InsertWebSocketLogParam(
                pageSource = WebSocketLogPageSource.PLAY,
                info = WebSocketLogUiModel(
                    event = event,
                    message = message.jsonHumanized(),
                    topchat = detail
                )
            )

            insertWebSocketLogUseCase(param)
        }
    }
}
