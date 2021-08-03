package com.tokopedia.review.feature.gallery.presentation.adapter

import com.tokopedia.review.feature.gallery.presentation.adapter.uimodel.ReviewGridGalleryUiModel

interface ReviewGridGalleryTypeFactory {
    fun type(reviewGridGalleryUiModel: ReviewGridGalleryUiModel): Int
}