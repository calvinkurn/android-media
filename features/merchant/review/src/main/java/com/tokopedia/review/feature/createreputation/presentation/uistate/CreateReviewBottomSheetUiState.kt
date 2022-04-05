package com.tokopedia.review.feature.createreputation.presentation.uistate

import com.tokopedia.reviewcommon.uimodel.StringRes

sealed interface CreateReviewBottomSheetUiState {
    object Showing : CreateReviewBottomSheetUiState
    data class ShouldDismiss(
        val success: Boolean, val message: StringRes, val feedbackId: String
    ) : CreateReviewBottomSheetUiState
}