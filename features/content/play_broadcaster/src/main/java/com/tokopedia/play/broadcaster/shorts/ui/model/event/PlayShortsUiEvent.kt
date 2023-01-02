package com.tokopedia.play.broadcaster.shorts.ui.model.event

/**
 * Created By : Jonathan Darwin on November 09, 2022
 */
sealed interface PlayShortsUiEvent {

    object Unknown : PlayShortsUiEvent

    object ErrorPreparingPage : PlayShortsUiEvent

    data class ErrorUploadTitle(
        val throwable: Throwable,
        val onRetry: () -> Unit
    ) : PlayShortsUiEvent

    data class ErrorSwitchAccount(
        val throwable: Throwable
    ) : PlayShortsUiEvent

    data class UGCOnboarding(val hasUsername: Boolean) : PlayShortsUiEvent

    object AccountNotEligible : PlayShortsUiEvent

    object SellerNotEligible : PlayShortsUiEvent

    object SwitchAccount : PlayShortsUiEvent

    object GoToSummary : PlayShortsUiEvent

    data class ErrorUploadMedia(
        val throwable: Throwable
    ) : PlayShortsUiEvent
}
