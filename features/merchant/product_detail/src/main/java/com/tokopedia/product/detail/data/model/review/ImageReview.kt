package com.tokopedia.product.detail.data.model.review

import com.tokopedia.gallery.viewmodel.ImageReviewItem

data class ImageReview(
        val imageReviewItems: List<ImageReviewItem> = listOf(),
        val imageCount: String = ""
)