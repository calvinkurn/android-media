package com.tokopedia.review.feature.credibility.presentation.uistate

import com.tokopedia.review.feature.credibility.presentation.uimodel.ReviewCredibilityHeaderUiModel

sealed interface ReviewCredibilityHeaderUiState {
    object Hidden : ReviewCredibilityHeaderUiState
    object Loading : ReviewCredibilityHeaderUiState
    data class Showed(val data: ReviewCredibilityHeaderUiModel) : ReviewCredibilityHeaderUiState
}