package com.tokopedia.reviewseller.feature.reviewdetail.data

import com.google.gson.annotations.SerializedName

data class ProductFeedbackDetailResponse(
        @SerializedName("productrevFeedbackDataPerProduct")
        val productFeedbackDataPerProduct: ProductFeedbackDataPerProduct = ProductFeedbackDataPerProduct()
) {
    data class ProductFeedbackDataPerProduct(
            val aggregatedRating: List<AggregatedRating> = listOf(),
            val filterBy: String? = "",
            val hasNext: Boolean = false,
            val limit: Int? = -1,
            val list: List<FeedbackList> = listOf(),
            val page: Int? = -1,
            val sortBy: String? = "",
            val topics: List<Topic> = listOf()
    ) {
        data class FeedbackList(
                @SerializedName("attachments")
                val attachments: List<Attachment> = listOf(),
                @SerializedName("autoReply")
                val autoReply: String? = "",
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
                val count: Int,
                val formatted: String,
                val title: String
        )

        data class AggregatedRating(
                val rating: Int,
                val ratingCount: Int
        )
    }
}