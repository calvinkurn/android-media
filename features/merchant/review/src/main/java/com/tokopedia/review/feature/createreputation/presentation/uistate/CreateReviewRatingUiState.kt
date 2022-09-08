package com.tokopedia.review.feature.createreputation.presentation.uistate

sealed interface CreateReviewRatingUiState {
    object Loading : CreateReviewRatingUiState
    data class Showing(val rating: Int, val trackerData: TrackerData) : CreateReviewRatingUiState {
        data class TrackerData(
            val orderId: String,
            val productId: String,
            val editMode: Boolean,
            val feedbackId: String
        )
    }
}