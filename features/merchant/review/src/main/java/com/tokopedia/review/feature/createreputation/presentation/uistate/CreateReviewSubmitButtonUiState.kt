package com.tokopedia.review.feature.createreputation.presentation.uistate

sealed interface CreateReviewSubmitButtonUiState {
    object Loading: CreateReviewSubmitButtonUiState
    object Disabled: CreateReviewSubmitButtonUiState
    object Enabled: CreateReviewSubmitButtonUiState
    object Sending: CreateReviewSubmitButtonUiState
}