package com.tokopedia.review.feature.reviewdetail.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductFeedbackDetailResponse(
        @SerializedName("productrevFeedbackDataPerProduct")
        val productrevFeedbackDataPerProduct: ProductFeedbackDataPerProduct = ProductFeedbackDataPerProduct()
) {
    data class ProductFeedbackDataPerProduct(
            @SerializedName("aggregatedRating")
            val aggregatedRating: List<AggregatedRating> = listOf(),
            @SerializedName("filterBy")
            val filterBy: String? = "",
            @SerializedName("hasNext")
            val hasNext: Boolean = false,
            @SerializedName("limit")
            val limit: Int? = 0,
            @SerializedName("list")
            val list: List<FeedbackList> = listOf(),
            @SerializedName("page")
            val page: Int? = 0,
            @SerializedName("sortBy")
            val sortBy: String? = "",
            @SerializedName("topics")
            val topics: List<Topic> = listOf(),
            @SerializedName("reviewCount")
            val reviewCount: Long = 0
    ) {
        data class FeedbackList(
                @SerializedName("attachments")
                val attachments: List<Attachment> = listOf(),
                @SerializedName("autoReply")
                val autoReply: Boolean = false,
                @SerializedName("feedbackID")
                val feedbackID: Int? = 0,
                @SerializedName("rating")
                val rating: Int? = 0,
                @SerializedName("replyText")
                val replyText: String? = "",
                @SerializedName("replyTime")
                val replyTime: String? = "",
                @SerializedName("reviewText")
                val reviewText: String? = "",
                @SerializedName("reviewTime")
                val reviewTime: String? = "",
                @SerializedName("reviewerName")
                val reviewerName: String? = "",
                @SerializedName("variantName")
                val variantName: String? = "",
                @SerializedName("isKejarUlasan")
                @Expose
                val isKejarUlasan: Boolean = false
        ) {
            data class Attachment(
                    @SerializedName("thumbnailURL")
                    val thumbnailURL: String? = "",
                    @SerializedName("fullsizeURL")
                    val fullSizeURL: String? = ""
            )
        }

        data class Topic(
                @SerializedName("count")
                val count: Int? = 0,
                @SerializedName("formatted")
                val formatted: String? = "",
                @SerializedName("title")
                val title: String? = ""
        )

        data class AggregatedRating(
                @SerializedName("rating")
                val rating: Int = 0,
                @SerializedName("ratingCount")
                val ratingCount: Int = 0
        )
    }
}