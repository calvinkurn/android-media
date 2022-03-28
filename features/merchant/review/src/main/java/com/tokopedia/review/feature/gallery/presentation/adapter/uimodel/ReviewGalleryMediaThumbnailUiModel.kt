package com.tokopedia.review.feature.gallery.presentation.adapter.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.review.feature.gallery.presentation.adapter.ReviewGalleryAdapterTypeFactory
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.UserReviewStats

interface ReviewGalleryMediaThumbnailUiModel : Visitable<ReviewGalleryAdapterTypeFactory> {
    val thumbnailUrl: String
    val mediaUrl: String
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