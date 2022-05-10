package com.tokopedia.review.feature.media.player.video.presentation.uistate

sealed interface ReviewVideoErrorUiState {
    object Hidden: ReviewVideoErrorUiState
    object ShowingUnknownError: ReviewVideoErrorUiState
    object ShowingInvalidError: ReviewVideoErrorUiState
}