package com.tokopedia.play.broadcaster.ui.event

sealed class PlayBroadcastEvent {

    object ShowLoading : PlayBroadcastEvent()
    object ShowResumeLiveDialog : PlayBroadcastEvent()
    object ShowLiveEndedDialog : PlayBroadcastEvent()

    data class ShowError(
        val error: Throwable,
        val onRetry: () -> Unit = {},
    ) : PlayBroadcastEvent()
}