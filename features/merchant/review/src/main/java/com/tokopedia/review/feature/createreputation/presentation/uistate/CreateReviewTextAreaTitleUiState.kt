package com.tokopedia.review.feature.createreputation.presentation.uistate

import com.tokopedia.reviewcommon.uimodel.StringRes

sealed interface CreateReviewTextAreaTitleUiState {
    object Loading: CreateReviewTextAreaTitleUiState
    data class Showing(val textRes: StringRes): CreateReviewTextAreaTitleUiState
}