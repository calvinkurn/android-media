package com.tokopedia.shop.info.domain.entity

data class ShopReview(
    val totalReviews: Int,
    val reviews: List<Review>
) {
    data class Review(
        val rating: Int,
        val reviewTime: String,
        val reviewText: String,
        val reviewerName: String,
        val reviewerLabel: String,
        val likeDislike: LikeDislike,
        val avatar: String
    ) {
        data class LikeDislike(
            val totalLike: Int,
            val likeStatus: Int
        )
    }
}
