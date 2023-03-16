package com.tokopedia.review.feature.bulk_write_review.presentation.uistate

import com.tokopedia.reviewcommon.uimodel.StringRes

sealed interface BulkReviewStickyButtonUiState {
    object Hidden : BulkReviewStickyButtonUiState
    data class Showing(
        val text: StringRes,
        val anonymous: Boolean
    ) : BulkReviewStickyButtonUiState

    data class Submitting(
        val text: StringRes,
        val anonymous: Boolean
    ) : BulkReviewStickyButtonUiState
}
