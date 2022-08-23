package com.tokopedia.review.feature.media.detail.presentation.uimodel

import com.tokopedia.review.feature.media.gallery.detailed.domain.usecase.ToggleLikeReviewUseCase

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
    val reviewerLabel: String = "",
    val variant: String = ""
) {
    fun getInvertedLikeStatus(): Int {
        return if (isLiked) ToggleLikeReviewUseCase.NEUTRAL else ToggleLikeReviewUseCase.LIKED
    }
}