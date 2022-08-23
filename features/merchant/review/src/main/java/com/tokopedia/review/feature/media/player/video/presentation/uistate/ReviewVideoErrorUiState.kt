package com.tokopedia.review.feature.media.player.video.presentation.uistate

sealed interface ReviewVideoErrorUiState {
    object Hidden : ReviewVideoErrorUiState
    data class Showing(
        val errorCode: String
    ) : ReviewVideoErrorUiState
}