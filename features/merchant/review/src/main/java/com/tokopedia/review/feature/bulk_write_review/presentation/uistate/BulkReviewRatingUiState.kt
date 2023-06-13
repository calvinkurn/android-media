package com.tokopedia.review.feature.bulk_write_review.presentation.uistate

sealed interface BulkReviewRatingUiState {
    data class Showing(
        val rating: Int,
        val animate: Boolean
    ) : BulkReviewRatingUiState
}
