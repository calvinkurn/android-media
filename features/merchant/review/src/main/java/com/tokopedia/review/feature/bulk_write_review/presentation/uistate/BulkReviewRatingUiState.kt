package com.tokopedia.review.feature.bulk_write_review.presentation.uistate

sealed interface BulkReviewRatingUiState {
    object Hidden : BulkReviewRatingUiState
    data class Showing(
        val rating: Int
    ) : BulkReviewRatingUiState
}
