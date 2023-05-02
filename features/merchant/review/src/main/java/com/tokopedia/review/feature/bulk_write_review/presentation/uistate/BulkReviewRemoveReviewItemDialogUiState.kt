package com.tokopedia.review.feature.bulk_write_review.presentation.uistate

sealed interface BulkReviewRemoveReviewItemDialogUiState {
    object Dismissed : BulkReviewRemoveReviewItemDialogUiState
    data class Showing(val inboxID: String) : BulkReviewRemoveReviewItemDialogUiState
}
