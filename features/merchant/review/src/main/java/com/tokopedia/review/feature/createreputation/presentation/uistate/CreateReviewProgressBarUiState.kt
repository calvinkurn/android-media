package com.tokopedia.review.feature.createreputation.presentation.uistate

import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewProgressBarState

sealed interface CreateReviewProgressBarUiState {
    object Loading : CreateReviewProgressBarUiState
    data class Showing(val state: CreateReviewProgressBarState) : CreateReviewProgressBarUiState
}