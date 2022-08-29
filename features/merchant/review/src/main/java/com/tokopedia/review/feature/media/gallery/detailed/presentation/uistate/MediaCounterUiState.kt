package com.tokopedia.review.feature.media.gallery.detailed.presentation.uistate

sealed interface MediaCounterUiState {
    object Loading: MediaCounterUiState
    object Hidden: MediaCounterUiState
    data class Showing(
        val count: Int,
        val total: Int
    ): MediaCounterUiState
}