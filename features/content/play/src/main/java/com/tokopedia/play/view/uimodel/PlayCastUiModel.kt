package com.tokopedia.play.view.uimodel


data class PlayCastUiModel (
    var previousState: PlayCastState = PlayCastState.NOT_CONNECTED,
    var currentState: PlayCastState = PlayCastState.NOT_CONNECTED
)

enum class PlayCastState {
    CONNECTING, CONNECTED, NOT_CONNECTED, NO_DEVICE_AVAILABLE
}