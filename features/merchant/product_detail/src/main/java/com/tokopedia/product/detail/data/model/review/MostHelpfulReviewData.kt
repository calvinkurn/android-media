package com.tokopedia.product.detail.data.model.review

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MostHelpfulReviewData(
        @SerializedName("list")
        @Expose
        val list: List<Review> = listOf()
)

data class Review(
        @SerializedName("reviewId")
        @Expose
        val reviewId: Long = 0,

        @SerializedName("message")
        @Expose
        val message: String = "",

        @SerializedName("productRating")
        @Expose
        val productRating: Int = 0,

        @SerializedName("reviewCreateTime")
        @Expose
        val reviewCreateTime: String = "",

        @SerializedName("user")
        @Expose
        val user: User = User(),

        @SerializedName("imageAttachments")
        @Expose
        val imageAttachments: List<ImageAttachment> = listOf(),

        @SerializedName("likeDislike")
        @Expose
        val likeDislike: LikeDislike = LikeDislike(),

        @SerializedName("variant")
        @Expose
        val variant: ProductVariantReview = ProductVariantReview()
)

data class ProductVariantReview(
        @SerializedName("name")
        @Expose
        val variantTitle: String = ""
)