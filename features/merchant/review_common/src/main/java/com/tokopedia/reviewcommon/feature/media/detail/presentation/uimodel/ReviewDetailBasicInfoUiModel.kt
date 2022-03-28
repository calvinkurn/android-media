package com.tokopedia.reviewcommon.feature.media.detail.presentation.uimodel

data class ReviewDetailBasicInfoUiModel(
    val feedbackId: String = "",
    val rating: Int = 0,
    val createTimeStr: String = "",
    val likeCount: Int = 0,
    val isLiked: Boolean = false,
    val userId: String = "",
    val anonymous: Boolean = false,
    val profilePicture: String = "",
    val reviewerName: String = "",
    val reviewerStatsSummary: String = "",
    val expanded: Boolean = true
)