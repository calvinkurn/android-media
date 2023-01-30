package com.tokopedia.review.feature.bulk_write_review.presentation.uistate

import com.tokopedia.reviewcommon.uimodel.StringRes

sealed interface BulkReviewExpandedTextAreaBottomSheetUiState {
    object Dismissed : BulkReviewExpandedTextAreaBottomSheetUiState
    data class Showing(
        val inboxID: String,
        val title: StringRes,
        val hint: StringRes,
        val text: String,
        val allowEmpty: Boolean
    ) : BulkReviewExpandedTextAreaBottomSheetUiState
}
