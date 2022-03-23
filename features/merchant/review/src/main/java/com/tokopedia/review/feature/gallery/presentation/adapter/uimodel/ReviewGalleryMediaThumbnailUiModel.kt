package com.tokopedia.review.feature.gallery.presentation.adapter.uimodel

import com.tokopedia.review.feature.reading.data.UserReviewStats

interface ReviewGalleryMediaThumbnailUiModel {
    val rating: Int
    val variantName: String
    val feedbackId: String
    val reviewerName: String
    var isLiked: Boolean
    var totalLiked: Int
    val review: String
    val reviewTime: String
    val isReportable: Boolean
    val mediaNumber: Int
    val attachmentId: String
    val userId: String
    val userStats: List<UserReviewStats>
    val isAnonymous: Boolean
    val userImage: String
    val badRatingReason: String
}