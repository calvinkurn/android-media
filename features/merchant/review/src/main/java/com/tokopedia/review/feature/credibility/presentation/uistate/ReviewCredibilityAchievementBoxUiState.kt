package com.tokopedia.review.feature.credibility.presentation.uistate

import com.tokopedia.review.feature.credibility.presentation.uimodel.ReviewCredibilityAchievementBoxUiModel

sealed interface ReviewCredibilityAchievementBoxUiState {
    object Hidden : ReviewCredibilityAchievementBoxUiState
    data class Showed(
        val data: ReviewCredibilityAchievementBoxUiModel
    ) : ReviewCredibilityAchievementBoxUiState
}