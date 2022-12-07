package com.tokopedia.review.feature.credibility.presentation.uistate

import com.tokopedia.review.feature.credibility.presentation.uimodel.ReviewCredibilityStatisticBoxUiModel

sealed interface ReviewCredibilityStatisticBoxUiState {
    object Hidden : ReviewCredibilityStatisticBoxUiState
    object Loading : ReviewCredibilityStatisticBoxUiState
    data class Showed(
        val data: ReviewCredibilityStatisticBoxUiModel
    ) : ReviewCredibilityStatisticBoxUiState
}