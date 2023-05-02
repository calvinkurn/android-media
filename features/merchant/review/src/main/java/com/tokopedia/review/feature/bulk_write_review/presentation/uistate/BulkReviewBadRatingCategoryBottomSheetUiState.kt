package com.tokopedia.review.feature.bulk_write_review.presentation.uistate

import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewBadRatingCategoryUiModel

sealed interface BulkReviewBadRatingCategoryBottomSheetUiState {
    object Dismissed : BulkReviewBadRatingCategoryBottomSheetUiState
    data class Showing(
        val inboxID: String,
        val badRatingCategories: List<BulkReviewBadRatingCategoryUiModel>
    ) : BulkReviewBadRatingCategoryBottomSheetUiState
}
