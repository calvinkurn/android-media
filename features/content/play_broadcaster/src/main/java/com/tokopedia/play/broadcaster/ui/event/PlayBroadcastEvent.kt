package com.tokopedia.play.broadcaster.ui.event

sealed interface PlayBroadcastEvent {

    data class ShowError(
        val error: Throwable
    ) : PlayBroadcastEvent

    data class ShowScheduleError(
        val error: Throwable
    ) : PlayBroadcastEvent
}