package com.tokopedia.play.view.uimodel


data class PlayCastUiModel (
    var previousState: PlayCastState = PlayCastState.NOT_CONNECTED,
    var currentState: PlayCastState = PlayCastState.NOT_CONNECTED
) {
    fun isClick(): Boolean =
        previousState == PlayCastState.NO_DEVICE_AVAILABLE && currentState == PlayCastState.CONNECTING

    fun isSuccessConnect(): Boolean =
        currentState == PlayCastState.CONNECTED
}

enum class PlayCastState {
    CONNECTING, CONNECTED, NOT_CONNECTED, NO_DEVICE_AVAILABLE
}