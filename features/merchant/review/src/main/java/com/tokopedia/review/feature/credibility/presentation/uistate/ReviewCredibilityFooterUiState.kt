package com.tokopedia.review.feature.credibility.presentation.uistate

import com.tokopedia.review.feature.credibility.presentation.uimodel.ReviewCredibilityFooterUiModel

sealed interface ReviewCredibilityFooterUiState {
    object Hidden : ReviewCredibilityFooterUiState
    object Loading : ReviewCredibilityFooterUiState
    data class Showed(val data: ReviewCredibilityFooterUiModel) : ReviewCredibilityFooterUiState
}