package com.tokopedia.play.broadcaster.ui.event

sealed interface PlayBroadcastEvent {

    object ShowLoading : PlayBroadcastEvent
    object ShowResumeLiveDialog : PlayBroadcastEvent
    object ShowLiveEndedDialog : PlayBroadcastEvent

    data class ShowError(
        val error: Throwable,
        val onRetry: () -> Unit = {},
    ) : PlayBroadcastEvent

    data class ShowScheduleError(
        val error: Throwable
    ) : PlayBroadcastEvent

    data class SetScheduleSuccess(val isEdit: Boolean) : PlayBroadcastEvent
    object DeleteScheduleSuccess : PlayBroadcastEvent

    object BroadcastStarted : PlayBroadcastEvent
    data class BroadcastReady(val ingestUrl: String) : PlayBroadcastEvent
}