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
        val reviewId: String,
        val rating: Int,
        val reviewTime: String,
        val reviewText: String,
        val reviewerName: String,
        val reviewerId: String,
        val reviewerLabel: String,
        val likeDislike: LikeDislike,
        val avatar: String,
        val attachments: List<Attachment>,
        val product: Product,
        val badRatingReasonFmt: String,
        val state: State
    ) : Parcelable {
        @Parcelize
        data class LikeDislike(val totalLike: Int, val likeStatus: Int) : Parcelable

        @Parcelize
        data class Attachment(
            val attachmentId: String,
            val thumbnailURL: String,
            val fullSizeURL: String
        ) : Parcelable

        @Parcelize
        data class Product(
            val productId: String,
            val productName: String,
            val productVariant: ProductVariant
        ) : Parcelable {
            @Parcelize
            data class ProductVariant(
                val variantId: String,
                val variantName: String
            ) : Parcelable
        }

        @Parcelize
        data class State(
            val isReportable: Boolean,
            val isAutoReply: Boolean,
            val isAnonymous: Boolean
        ) : Parcelable
    }
}
