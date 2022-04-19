package com.tokopedia.review.feature.gallery.presentation.adapter.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.review.feature.gallery.presentation.adapter.ReviewGalleryAdapterTypeFactory
import com.tokopedia.review.feature.reading.data.UserReviewStats

data class ReviewGalleryUiModel(
    val imageUrl: String = "",
    val rating: Int = 0,
    val variantName: String = "",
    val feedbackId: String = "",
    val fullImageUrl: String = "",
    val reviewerName: String = "",
    var isLiked: Boolean = false,
    var totalLiked: Int = 0,
    val review: String = "",
    val reviewTime: String = "",
    val isReportable: Boolean = false,
    val imageNumber: Int = 0,
    val attachmentId: String = "",
    val userId: String = "",
    val userStats: List<UserReviewStats> = listOf(),
    val isAnonymous: Boolean = false,
    val userImage: String = "",
    val badRatingReason: String = ""
) : Visitable<ReviewGalleryAdapterTypeFactory> {

    override fun type(typeFactory: ReviewGalleryAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}