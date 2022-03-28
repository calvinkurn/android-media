package com.tokopedia.review.common.util

interface ReviewAttachedImagesClickListener {
    fun onAttachedImagesClicked(
        productID: String,
        feedbackID: String,
        position: Int,
        images: List<String>,
        videos: List<String>
    )
}