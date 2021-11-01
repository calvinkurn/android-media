package com.tokopedia.review.common.util

interface ReviewAttachedImagesClickListener {
    fun onAttachedImagesClicked(productName: String, attachedImages: List<String>, position: Int)
}