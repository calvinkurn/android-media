package com.tokopedia.analyticsdebugger.debugger.ws

import android.content.Context
import com.tokopedia.analyticsdebugger.debugger.BaseWebSocketLogger
import com.tokopedia.analyticsdebugger.websocket.domain.param.InsertWebSocketLogParam
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.info.TopchatUiModel
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogPageSource
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogUiModel
import kotlinx.coroutines.launch

class TopchatWebSocketLogger constructor(mContext: Context) : BaseWebSocketLogger<TopchatUiModel>(mContext) {

    private var detail: TopchatUiModel? = null

    override fun init(data: TopchatUiModel) {
        detail = data
    }

    override fun send(event: String, message: String) {
        launch {
            val param = InsertWebSocketLogParam(
                pageSource = WebSocketLogPageSource.TOPCHAT,
                info = WebSocketLogUiModel(
                    event = event,
                    message = message.jsonHumanized(),
                    topchat = detail ?: TopchatUiModel()
                )
            )

            insertWebSocketLogUseCase(param)
        }
    }
}
