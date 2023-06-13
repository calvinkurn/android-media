package com.tokopedia.review.feature.bulk_write_review.presentation.uistate

import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewMiniActionUiModel

sealed interface BulkReviewMiniActionsUiState {
    object Hidden : BulkReviewMiniActionsUiState
    data class Showing(
        val inboxID: String,
        val miniActions: List<BulkReviewMiniActionUiModel>
    ) : BulkReviewMiniActionsUiState
}
