package com.tokopedia.review.feature.createreputation.presentation.uistate

import com.tokopedia.review.feature.ovoincentive.presentation.model.IncentiveOvoBottomSheetUiModel

sealed interface CreateReviewIncentiveBottomSheetUiState {
    object Hidden: CreateReviewIncentiveBottomSheetUiState
    data class Showing(val data: IncentiveOvoBottomSheetUiModel): CreateReviewIncentiveBottomSheetUiState
}