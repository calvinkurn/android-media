package com.tokopedia.review.feature.createreputation.presentation.uistate

sealed interface CreateReviewTopicsUiState {
    object Hidden : CreateReviewTopicsUiState
    data class Showing(
        val topics: List<String>,
        val animatePeek: Boolean
    ) : CreateReviewTopicsUiState
}