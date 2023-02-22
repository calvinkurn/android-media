package com.tokopedia.review.feature.bulk_write_review.presentation.uistate

import com.tokopedia.reviewcommon.uimodel.StringRes

sealed interface BulkReviewTextAreaUiState {

    fun areContentsTheSame(other: BulkReviewTextAreaUiState): Boolean

    object Hidden : BulkReviewTextAreaUiState {
        override fun areContentsTheSame(other: BulkReviewTextAreaUiState): Boolean {
            return other is Hidden
        }
    }

    data class Showing(
        val text: String,
        val hint: StringRes,
        val focused: Boolean
    ) : BulkReviewTextAreaUiState {
        override fun areContentsTheSame(other: BulkReviewTextAreaUiState): Boolean {
            return other is Showing && text == other.text && hint == other.hint
        }
    }
}
