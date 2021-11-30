package com.tokopedia.play.view.uimodel


data class PlayCastUiModel (
    var previousState: PlayCastState = PlayCastState.NOT_CONNECTED,
    var currentState: PlayCastState = PlayCastState.NOT_CONNECTED
) {
    fun connectFailed(): Boolean =
        (currentState == PlayCastState.NOT_CONNECTED || currentState == PlayCastState.NO_DEVICE_AVAILABLE)
        && previousState == PlayCastState.CONNECTING
}

enum class PlayCastState {
    CONNECTING, CONNECTED, NOT_CONNECTED, NO_DEVICE_AVAILABLE
}