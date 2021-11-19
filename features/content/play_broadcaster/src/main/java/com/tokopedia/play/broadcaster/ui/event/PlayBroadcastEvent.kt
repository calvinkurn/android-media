package com.tokopedia.play.broadcaster.ui.event

sealed class PlayBroadcastEvent {

    data class ShowError(
        val error: Throwable
    ) : PlayBroadcastEvent()
}