package com.tokopedia.reviewseller.feature.reviewreply.view.adapter

interface ReviewReplyListener {
    fun onImageItemClicked(imageUrls: List<String>, thumbnailsUrl: List<String>, title: String, feedbackId: String, position: Int)
}