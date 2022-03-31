package com.tokopedia.review.feature.reviewreply.view.adapter

interface ReviewReplyListener {
    fun onImageItemClicked(imageUrls: List<String>, videoUrls: List<String>, title: String, feedbackId: String, productID: String, position: Int)
}