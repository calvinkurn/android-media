package com.tokopedia.review.feature.reading.presentation.listener

interface ReadReviewItemListener {
    fun onThreeDotsClicked(reviewId: String, shopId: String)
    fun onLikeButtonClicked(reviewId: String, shopId: String, likeStatus: Int, index: Int)
    fun onItemImpressed(reviewId: String, position: Int, characterCount: Int, imageCount: Int)
}