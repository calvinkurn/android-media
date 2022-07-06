package com.tokopedia.review.feature.media.detail.presentation.uistate

import com.tokopedia.review.feature.media.detail.presentation.uimodel.ReviewDetailBasicInfoUiModel

sealed interface ReviewDetailBasicInfoUiState {
    object Hidden : ReviewDetailBasicInfoUiState
    object Loading : ReviewDetailBasicInfoUiState
    data class Showing(
        val data: ReviewDetailBasicInfoUiModel
    ) : ReviewDetailBasicInfoUiState
}