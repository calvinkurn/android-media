package com.tokopedia.review.feature.bulk_write_review.presentation.uistate

sealed interface BulkReviewCancelReviewSubmissionDialogUiState {
    object Dismissed : BulkReviewCancelReviewSubmissionDialogUiState
    object Showing : BulkReviewCancelReviewSubmissionDialogUiState
}
