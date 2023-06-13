package com.tokopedia.analyticsdebugger.websocket.ui.uimodel

enum class WebSocketLogPageSource(val value: String) {
    PLAY("Play"),
    TOPCHAT("Topchat"),
    NONE("");

    companion object {
        private val map = values().associateBy(WebSocketLogPageSource::value)

        @JvmStatic
        fun fromString(value: String) = map[value]?: NONE
    }
}
