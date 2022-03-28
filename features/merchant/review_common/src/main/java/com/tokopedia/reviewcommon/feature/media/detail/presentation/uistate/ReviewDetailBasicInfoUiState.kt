package com.tokopedia.reviewcommon.feature.media.detail.presentation.uistate

import com.tokopedia.reviewcommon.feature.media.detail.presentation.uimodel.ReviewDetailBasicInfoUiModel

sealed interface ReviewDetailBasicInfoUiState {
    object Hidden : ReviewDetailBasicInfoUiState
    object Loading : ReviewDetailBasicInfoUiState
    data class Showing(
        val data: ReviewDetailBasicInfoUiModel
    ) : ReviewDetailBasicInfoUiState
}