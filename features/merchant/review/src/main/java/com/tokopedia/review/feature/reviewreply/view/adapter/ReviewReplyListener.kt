package com.tokopedia.review.feature.reviewreply.view.adapter

interface ReviewReplyListener {
    fun onImageItemClicked(imageUrls: List<String>, thumbnailsUrl: List<String>, title: String, feedbackId: String, productID: String, position: Int)
}