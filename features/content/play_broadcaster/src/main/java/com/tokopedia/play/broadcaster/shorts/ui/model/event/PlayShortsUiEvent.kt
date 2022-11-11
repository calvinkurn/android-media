package com.tokopedia.play.broadcaster.shorts.ui.model.event

/**
 * Created By : Jonathan Darwin on November 09, 2022
 */
data class PlayShortsUiEvent(
    val toaster: PlayShortsToaster,
    val oneTimeEvent: PlayShortsOneTimeEvent,
) {
    companion object {
        val Empty: PlayShortsUiEvent
            get() = PlayShortsUiEvent(
                toaster = PlayShortsToaster.Unknown,
                oneTimeEvent = PlayShortsOneTimeEvent.Unknown,
            )
    }
}

sealed interface PlayShortsToaster {

    object Unknown : PlayShortsToaster

    data class ErrorSubmitTitle(
        val throwable: Throwable,
        val onRetry: () -> Unit,
    ) : PlayShortsToaster
}

sealed interface PlayShortsOneTimeEvent {

    object Unknown : PlayShortsOneTimeEvent

    object GoToSummary : PlayShortsOneTimeEvent
}
