package com.tokopedia.review.feature.createreputation.presentation.uistate

sealed interface CreateReviewAnonymousInfoBottomSheetUiState {
    object Hidden: CreateReviewAnonymousInfoBottomSheetUiState
    object Showing: CreateReviewAnonymousInfoBottomSheetUiState
}
