package com.tokopedia.review.feature.gallery.presentation.adapter

import com.tokopedia.review.feature.gallery.presentation.adapter.uimodel.ReviewGalleryUiModel

interface ReviewGalleryTypeFactory {
    fun type(reviewGalleryUiModel: ReviewGalleryUiModel): Int
}