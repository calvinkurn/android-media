package com.tokopedia.review.common.presentation.util

interface ReviewAttachedImagesClickListener {
    fun onAttachedImagesClicked(productName: String, attachedImages: List<String>, position: Int)
}