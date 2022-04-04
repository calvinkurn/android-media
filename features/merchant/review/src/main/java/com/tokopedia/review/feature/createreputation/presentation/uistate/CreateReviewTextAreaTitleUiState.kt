package com.tokopedia.review.feature.createreputation.presentation.uistate

import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewStringRes

sealed interface CreateReviewTextAreaTitleUiState {
    object Loading: CreateReviewTextAreaTitleUiState
    data class Showing(val textRes: CreateReviewStringRes): CreateReviewTextAreaTitleUiState
}