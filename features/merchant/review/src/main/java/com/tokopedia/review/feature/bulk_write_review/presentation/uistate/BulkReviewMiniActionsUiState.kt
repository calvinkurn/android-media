package com.tokopedia.review.feature.bulk_write_review.presentation.uistate

sealed interface BulkReviewMiniActionsUiState {
    object Hidden : BulkReviewMiniActionsUiState
    data class Showing(
        val inboxID: String,
        val miniActionTestimony: BulkReviewMiniActionUiState,
        val miniActionAttachment: BulkReviewMiniActionUiState
    ) : BulkReviewMiniActionsUiState
}
