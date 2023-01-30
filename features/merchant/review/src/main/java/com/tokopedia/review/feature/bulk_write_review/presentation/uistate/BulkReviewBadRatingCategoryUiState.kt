package com.tokopedia.review.feature.bulk_write_review.presentation.uistate

import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewBadRatingCategoryUiModel

sealed interface BulkReviewBadRatingCategoryUiState {
    object Hidden : BulkReviewBadRatingCategoryUiState
    data class Showing(
        val badRatingCategory: List<BulkReviewBadRatingCategoryUiModel>
    ) : BulkReviewBadRatingCategoryUiState
}
