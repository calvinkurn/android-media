package com.tokopedia.review.feature.createreputation.presentation.uistate

sealed interface CreateReviewAnonymousUiState {
    object Loading : CreateReviewAnonymousUiState
    data class Showing(
        val checked: Boolean,
        val trackerData: TrackerData
    ) : CreateReviewAnonymousUiState {
        data class TrackerData(
            val orderId: String,
            val productId: String,
            val editMode: Boolean,
            val feedbackId: String
        )
    }
}