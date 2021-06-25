package com.tokopedia.review.feature.reading.presentation.adapter

interface ReadReviewItemListener {
    fun onThreeDotsClicked(reviewId: String, shopId: String)
    fun onLikeButtonClicked(reviewId: String, shopId: String, likeStatus: Int, index: Int)
}