package com.tokopedia.review.feature.reading.presentation.listener

interface ReadReviewItemListener {
    fun onThreeDotsClicked(reviewId: String, shopId: String)
    fun onThreeDotsProductInfoClicked(reviewId: String, shopId: String)
    fun onProductInfoClicked(
            reviewId: String,
            shopName: String,
            productName: String,
            position: Int,
            shopId: String,
            productId: String
    )
    fun onLikeButtonClicked(reviewId: String, likeStatus: Int, index: Int)
    fun onShopReviewLikeButtonClicked(reviewId: String, shopId: String, productId: String, likeStatus: Int, index: Int)
    fun onItemImpressed(reviewId: String, position: Int, characterCount: Int, imageCount: Int)
}