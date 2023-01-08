package com.tokopedia.review.feature.credibility.presentation.uistate

import com.tokopedia.review.feature.credibility.presentation.uimodel.ReviewCredibilityGlobalErrorUiModel

sealed interface ReviewCredibilityGlobalErrorUiState {
    object Hidden : ReviewCredibilityGlobalErrorUiState
    data class Showed(
        val data: ReviewCredibilityGlobalErrorUiModel
    ) : ReviewCredibilityGlobalErrorUiState
}