package com.tokopedia.product.detail.data.model.review

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Review(
        @SerializedName("imageAttachments")
        @Expose
        val imageAttachments: List<ImageAttachment> = listOf(),

        @SerializedName("isAnonymous")
        @Expose
        val isAnonymous: Boolean = false,

        @SerializedName("isReportable")
        @Expose
        val isReportable: Boolean = false,

        @SerializedName("isUpdated")
        @Expose
        val isUpdated: Boolean = false,

        @SerializedName("likeDislike")
        @Expose
        val likeDislike: LikeDislike = LikeDislike(),

        @SerializedName("message")
        @Expose
        val message: String = "",

        @SerializedName("productRating")
        @Expose
        val productRating: Int = 0,

        @SerializedName("productRatingDescription")
        @Expose
        val productRatingDescription: String = "",

        @SerializedName("reputationId")
        @Expose
        val reputationId: Int = 0,

        @SerializedName("reviewCreateTime")
        @Expose
        val reviewCreateTime: String = "",

        @SerializedName("reviewCreateTimestamp")
        @Expose
        val reviewCreateTimestamp: String = "",

        @SerializedName("reviewId")
        @Expose
        val reviewId: Int = 0,

        @SerializedName("reviewResponse")
        @Expose
        val reviewResponse: ReviewResponse = ReviewResponse(),

        @SerializedName("reviewUpdateTime")
        @Expose
        val reviewUpdateTime: String = "",

        @SerializedName("reviewUpdateTimestamp")
        @Expose
        val reviewUpdateTimestamp: String = "",

        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("user")
        @Expose
        val user: User = User()
){
    data class Response(
            @SerializedName("ProductMostHelpfulReviewQuery")
            @Expose
            val productMostHelpfulReviewQuery: ProductMostHelpfulReviewQuery = ProductMostHelpfulReviewQuery()
    )

    data class ProductMostHelpfulReviewQuery(
            @SerializedName("list")
            @Expose
            val list: List<Review> = listOf()
    )
}