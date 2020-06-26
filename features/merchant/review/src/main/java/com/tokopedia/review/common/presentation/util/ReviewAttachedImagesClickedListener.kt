package com.tokopedia.review.common.presentation.util

interface ReviewAttachedImagesClickedListener {
    fun onAttachedImagesClicked(productName: String, attachedImages: List<String>, position: Int)
}