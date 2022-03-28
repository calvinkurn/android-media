package com.tokopedia.reviewcommon.feature.media.detail.presentation.uistate

import com.tokopedia.reviewcommon.feature.media.detail.presentation.uimodel.ReviewDetailSupplementaryInfoUiModel

sealed interface ReviewDetailSupplementaryUiState {
    object Hidden : ReviewDetailSupplementaryUiState
    object Loading : ReviewDetailSupplementaryUiState
    data class Showing(
        val data: ReviewDetailSupplementaryInfoUiModel
    ) : ReviewDetailSupplementaryUiState
}