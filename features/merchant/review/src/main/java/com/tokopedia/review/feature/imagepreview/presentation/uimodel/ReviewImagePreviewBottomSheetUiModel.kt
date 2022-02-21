package com.tokopedia.review.feature.imagepreview.presentation.uimodel

import com.tokopedia.review.feature.reading.data.UserReviewStats

data class ReviewImagePreviewBottomSheetUiModel(
    val rating: Int = 0,
    val timeStamp: String = "",
    val reviewerName: String = "",
    val reviewMessage: String = "",
    val variantName: String = "",
    val userStats: List<UserReviewStats> = listOf(),
    val userId: String = "",
    val isAnonymous: Boolean = false,
    val isProductReview: Boolean = false,
    val feedbackId: String = "",
    val productId: String = "",
    val isFromGallery: Boolean = false,
    val currentUserId: String = "",
    val reviewerImage: String = "",
    val source: String = "",
    val badRatingReason: String = ""
)