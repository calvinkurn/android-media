package com.tokopedia.review.feature.bulk_write_review.presentation.uimodel

data class BulkReviewItemBadRatingCategoryUiModel(
    val inboxID: String,
    val badRatingCategory: List<BulkReviewBadRatingCategoryUiModel>
)
