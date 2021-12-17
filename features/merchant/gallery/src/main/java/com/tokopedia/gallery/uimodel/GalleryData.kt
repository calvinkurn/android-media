package com.tokopedia.gallery.uimodel

import com.tokopedia.gallery.viewmodel.ImageReviewItem

data class GalleryData(
    val reviewItems: List<ImageReviewItem>,
    val hasNext: Boolean
)