package com.tokopedia.review.feature.credibility.presentation.uistate

sealed interface ReviewCredibilityGlobalErrorUiState {
    object Hidden : ReviewCredibilityGlobalErrorUiState
    object Showed : ReviewCredibilityGlobalErrorUiState
}