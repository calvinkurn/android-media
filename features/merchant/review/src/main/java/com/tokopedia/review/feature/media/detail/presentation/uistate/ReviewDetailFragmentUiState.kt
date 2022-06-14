package com.tokopedia.review.feature.media.detail.presentation.uistate

sealed interface ReviewDetailFragmentUiState {
    object Hidden : ReviewDetailFragmentUiState
    data class Showing(
        val basicInfoUiState: ReviewDetailBasicInfoUiState,
        val supplementaryUiState: ReviewDetailSupplementaryUiState
    ) : ReviewDetailFragmentUiState
}