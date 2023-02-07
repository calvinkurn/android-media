package com.tokopedia.analyticsdebugger.debugger.ws

import android.content.Context
import com.tokopedia.analyticsdebugger.debugger.BaseWebSocketLogger
import com.tokopedia.analyticsdebugger.websocket.domain.param.InsertWebSocketLogParam
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.info.PlayUiModel
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogPageSource
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogUiModel
import kotlinx.coroutines.launch

class PlayWebSocketLogger constructor(mContext: Context) : BaseWebSocketLogger(mContext) {

    private var detail: PlayUiModel? = null

    override fun init(data: String) {
        detail = gson
            .create()
            .parseDetailInfo(data)
    }

    override fun send(event: String, message: String) {
        launch {
            val param = InsertWebSocketLogParam(
                pageSource = WebSocketLogPageSource.PLAY,
                info = WebSocketLogUiModel(
                    event = event,
                    message = message.jsonHumanized(),
                    play = detail
                )
            )

            insertWebSocketLogUseCase(param)
        }
    }
}
