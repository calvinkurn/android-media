package com.tokopedia.review.feature.createreputation.presentation.uistate

sealed interface CreateReviewTextAreaBottomSheetUiState {
    object Hidden : CreateReviewTextAreaBottomSheetUiState
    object Showing : CreateReviewTextAreaBottomSheetUiState
}