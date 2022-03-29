package com.tokopedia.reviewcommon.feature.media.detail.presentation.uimodel

data class ReviewDetailUiModel(
    val basicInfoUiModel: ReviewDetailBasicInfoUiModel = ReviewDetailBasicInfoUiModel(),
    val supplementaryInfoUiModel: ReviewDetailSupplementaryInfoUiModel = ReviewDetailSupplementaryInfoUiModel(),
    val isReportable: Boolean = false,
    val feedbackID: String = "",
    val shopID: String = ""
)
