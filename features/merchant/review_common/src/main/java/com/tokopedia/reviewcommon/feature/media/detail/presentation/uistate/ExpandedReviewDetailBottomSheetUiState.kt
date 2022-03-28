package com.tokopedia.reviewcommon.feature.media.detail.presentation.uistate

sealed interface ExpandedReviewDetailBottomSheetUiState {
    data class Hidden(
        val basicInfoUiState: ReviewDetailBasicInfoUiState,
        val supplementaryUiState: ReviewDetailSupplementaryUiState
    ) : ExpandedReviewDetailBottomSheetUiState

    data class Showing(
        val basicInfoUiState: ReviewDetailBasicInfoUiState,
        val supplementaryUiState: ReviewDetailSupplementaryUiState
    ) : ExpandedReviewDetailBottomSheetUiState
}