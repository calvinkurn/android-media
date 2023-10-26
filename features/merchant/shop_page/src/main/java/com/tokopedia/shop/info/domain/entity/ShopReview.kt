package com.tokopedia.shop.info.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShopReview(
    val totalReviews: Int,
    val reviews: List<Review>
) : Parcelable {
    @Parcelize
    data class Review(
        val rating: Int,
        val reviewTime: String,
        val reviewText: String,
        val reviewerName: String,
        val reviewerLabel: String,
        val likeDislike: LikeDislike,
        val avatar: String
    ) : Parcelable {
        @Parcelize
        data class LikeDislike(val totalLike: Int, val likeStatus: Int) : Parcelable
    }
}
