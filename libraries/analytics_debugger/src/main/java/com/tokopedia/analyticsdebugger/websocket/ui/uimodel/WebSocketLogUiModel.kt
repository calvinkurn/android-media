package com.tokopedia.analyticsdebugger.websocket.ui.uimodel


/**
 * Created By : Jonathan Darwin on December 01, 2021
 */

sealed class WebSocketLog

data class WebSocketLogUiModel(
    val id: Long = 0,
    val event: String,
    val message: String,
    val dateTime: String = "",
    val play: PlayWebSocketLogGeneralInfoUiModel? = null,
    val topchat: TopchatWebSocketLogDetailInfoUiModel? = null
) : WebSocketLog()

// Play
data class PlayWebSocketLogGeneralInfoUiModel(
    val source: String = "",
    val channelId: String = "",
    val warehouseId: String = "",
    val gcToken: String = "",
)

// Topchat
data class TopchatWebSocketLogDetailInfoUiModel(
    val source: String = "",
    val code: String = "",
    val messageId: String = ""
)
