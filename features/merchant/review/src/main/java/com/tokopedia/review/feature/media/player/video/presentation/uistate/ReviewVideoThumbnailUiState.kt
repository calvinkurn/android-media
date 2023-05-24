package com.tokopedia.review.feature.media.player.video.presentation.uistate

sealed interface ReviewVideoThumbnailUiState {
    object Showed : ReviewVideoThumbnailUiState
    object Hidden : ReviewVideoThumbnailUiState
}
