package com.tokopedia.review.feature.gallery.presentation.adapter.uimodel

import com.tokopedia.review.feature.gallery.presentation.adapter.ReviewGalleryAdapterTypeFactory
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.UserReviewStats

data class ReviewGalleryVideoThumbnailUiModel(
    override val mediaUrl: String = "",
    override val thumbnailUrl: String = "",
    override val rating: Int = 0,
    override val variantName: String = "",
    override val feedbackId: String = "",
    override val reviewerName: String = "",
    override var isLiked: Boolean = false,
    override var totalLiked: Int = 0,
    override val review: String = "",
    override val reviewTime: String = "",
    override val isReportable: Boolean = false,
    override val mediaNumber: Int = 0,
    override val attachmentId: String = "",
    override val userId: String = "",
    override val userStats: List<UserReviewStats> = listOf(),
    override val isAnonymous: Boolean = false,
    override val userImage: String = "",
    override val badRatingReason: String = ""
) : ReviewGalleryMediaThumbnailUiModel {
    override fun type(typeFactory: ReviewGalleryAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}