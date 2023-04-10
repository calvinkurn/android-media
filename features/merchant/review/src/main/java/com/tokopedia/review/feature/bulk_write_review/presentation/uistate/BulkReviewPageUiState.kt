package com.tokopedia.review.feature.bulk_write_review.presentation.uistate

import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.typefactory.BulkReviewAdapterTypeFactory
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewVisitable

sealed interface BulkReviewPageUiState {
    object Loading : BulkReviewPageUiState
    object Submitting : BulkReviewPageUiState
    object Cancelled: BulkReviewPageUiState
    data class Submitted(
        val userName: String
    ) : BulkReviewPageUiState

    data class Showing(
        val items: List<BulkReviewVisitable<BulkReviewAdapterTypeFactory>>,
        val stickyButtonUiState: BulkReviewStickyButtonUiState
    ) : BulkReviewPageUiState

    data class Error(
        val throwable: Throwable?
    ) : BulkReviewPageUiState
}
