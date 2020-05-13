package com.tokopedia.reviewseller.feature.reviewdetail.data

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
            val limit: Int? = -1,
            @SerializedName("list")
            val list: List<FeedbackList> = listOf(),
            @SerializedName("page")
            val page: Int? = -1,
            @SerializedName("sortBy")
            val sortBy: String? = "",
            @SerializedName("topics")
            val topics: List<Topic> = listOf(),
            @SerializedName("reviewCount")
            val reviewCount: Int = 0
    ) {
        data class FeedbackList(
                @SerializedName("attachments")
                val attachments: List<Attachment> = listOf(),
                @SerializedName("autoReply")
                val autoReply: Boolean = false,
                @SerializedName("feedbackID")
                val feedbackID: Int? = -1,
                @SerializedName("rating")
                val rating: Int? = -1,
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
                val variantName: String? = ""
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
                val count: Int,
                @SerializedName("formatted")
                val formatted: String,
                @SerializedName("title")
                val title: String
        )

        data class AggregatedRating(
                @SerializedName("rating")
                val rating: Int,
                @SerializedName("ratingCount")
                val ratingCount: Int
        )
    }
}