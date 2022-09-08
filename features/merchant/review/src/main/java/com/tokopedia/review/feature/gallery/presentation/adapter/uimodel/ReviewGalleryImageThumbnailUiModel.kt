package com.tokopedia.review.feature.gallery.presentation.adapter.uimodel

import android.net.Uri
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.review.feature.gallery.presentation.adapter.ReviewGalleryAdapterTypeFactory
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.UserReviewStats

data class ReviewGalleryImageThumbnailUiModel(
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
    override val badRatingReason: String = "",
    override val impressHolder: ImpressHolder = ImpressHolder()
) : ReviewGalleryMediaThumbnailUiModel {
    override fun type(typeFactory: ReviewGalleryAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun getMediaName(): String {
        return if (mediaUrl.isNotBlank()) {
            Uri.parse(mediaUrl).lastPathSegment.orEmpty()
        } else {
            Uri.parse(thumbnailUrl).lastPathSegment.orEmpty()
        }
    }
}