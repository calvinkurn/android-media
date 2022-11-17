package com.tokopedia.play.broadcaster.shorts.ui.model.event

/**
 * Created By : Jonathan Darwin on November 09, 2022
 */
data class PlayShortsUiEvent(
    val toaster: PlayShortsToaster,
    val bottomSheet: PlayShortsBottomSheet,
    val oneTimeEvent: PlayShortsOneTimeEvent,
) {
    companion object {
        val Empty: PlayShortsUiEvent
            get() = PlayShortsUiEvent(
                toaster = PlayShortsToaster.Unknown,
                bottomSheet = PlayShortsBottomSheet.Unknown,
                oneTimeEvent = PlayShortsOneTimeEvent.Unknown,
            )
    }
}

sealed interface PlayShortsToaster {

    object Unknown : PlayShortsToaster

    data class ErrorUploadTitle(
        val throwable: Throwable,
        val onRetry: () -> Unit
    ) : PlayShortsToaster

    data class ErrorSwitchAccount(
        val throwable: Throwable
    ) : PlayShortsToaster

    data class ErrorUploadMedia(
        val throwable: Throwable
    ) : PlayShortsToaster
}

sealed interface PlayShortsBottomSheet {

    object Unknown : PlayShortsBottomSheet

    data class UGCOnboarding(val hasUsername: Boolean) : PlayShortsBottomSheet

    object AccountNotEligible : PlayShortsBottomSheet

    object SellerNotEligible : PlayShortsBottomSheet

    object SwitchAccount : PlayShortsBottomSheet
}

sealed interface PlayShortsOneTimeEvent {

    object Unknown : PlayShortsOneTimeEvent

    object GoToSummary : PlayShortsOneTimeEvent
}
