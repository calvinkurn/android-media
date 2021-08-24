package com.tokopedia.review.feature.gallery.presentation.adapter.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.review.feature.gallery.presentation.adapter.ReviewGalleryAdapterTypeFactory

data class ReviewGalleryUiModel(
    val imageUrl: String = "",
    val rating: Int = 0,
    val variantName: String = "",
    val feedbackId: String = "",
    val fullImageUrl: String = "",
    val reviewerName: String = "",
    var isLiked: Boolean = false,
    val totalLiked: Long = 0L,
    val review: String = "",
    val reviewTime: String = "",
    val isReportable: Boolean = false,
    val imageNumber: Int = 0,
    val attachmentId: String = ""
) : Visitable<ReviewGalleryAdapterTypeFactory> {

    override fun type(typeFactory: ReviewGalleryAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}