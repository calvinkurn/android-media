package com.tokopedia.review.common.util

interface ReviewAttachedImagesClickListener {
    fun onAttachedMediaClicked(
        productID: String,
        feedbackID: String,
        position: Int,
        images: List<String>,
        videos: List<String>
    )
}