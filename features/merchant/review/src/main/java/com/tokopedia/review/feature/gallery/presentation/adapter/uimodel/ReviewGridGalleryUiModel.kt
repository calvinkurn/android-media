package com.tokopedia.review.feature.gallery.presentation.adapter.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.review.feature.gallery.presentation.adapter.ReviewGridGalleryAdapterTypeFactory

data class ReviewGridGalleryUiModel(
    val imageUrl: String,
    val rating: Int,
    val variantName: String = ""
) : Visitable<ReviewGridGalleryAdapterTypeFactory> {

    override fun type(typeFactory: ReviewGridGalleryAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}