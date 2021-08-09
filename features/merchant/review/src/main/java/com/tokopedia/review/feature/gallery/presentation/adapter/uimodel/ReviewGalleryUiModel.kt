package com.tokopedia.review.feature.gallery.presentation.adapter.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.review.feature.gallery.presentation.adapter.ReviewGalleryAdapterTypeFactory

data class ReviewGalleryUiModel(
    val imageUrl: String,
    val rating: Int,
    val variantName: String = "",
    val feedbackId: String = "",
    val fullImageUrl: String = ""
) : Visitable<ReviewGalleryAdapterTypeFactory> {

    override fun type(typeFactory: ReviewGalleryAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}