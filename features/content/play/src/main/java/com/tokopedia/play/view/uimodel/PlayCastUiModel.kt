package com.tokopedia.play.view.uimodel


data class PlayCastUiModel (
    var previousState: PlayCastState? = null,
    var currentState: PlayCastState? = null
)

enum class PlayCastState {
    CONNECTING, CONNECTED, NOT_CONNECTED, NO_DEVICE_AVAILABLE
}