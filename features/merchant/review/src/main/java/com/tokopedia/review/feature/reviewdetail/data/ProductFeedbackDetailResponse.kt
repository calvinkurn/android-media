package com.tokopedia.review.feature.reviewdetail.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductFeedbackDetailResponse(
        @SerializedName("productrevFeedbackDataPerProductV2")
        @Expose
        val productrevFeedbackDataPerProduct: ProductFeedbackDataPerProduct = ProductFeedbackDataPerProduct()
) {
    data class ProductFeedbackDataPerProduct(
            @SerializedName("aggregatedRating")
            @Expose
            val aggregatedRating: List<AggregatedRating> = listOf(),
            @SerializedName("filterBy")
            @Expose
            val filterBy: String? = "",
            @SerializedName("hasNext")
            @Expose
            val hasNext: Boolean = false,
            @SerializedName("limit")
            @Expose
            val limit: Int? = 0,
            @SerializedName("list")
            @Expose
            val list: List<FeedbackList> = listOf(),
            @SerializedName("page")
            @Expose
            val page: Int? = 0,
            @SerializedName("sortBy")
            @Expose
            val sortBy: String? = "",
            @SerializedName("topics")
            @Expose
            val topics: List<Topic> = listOf(),
            @SerializedName("reviewCount")
            @Expose
            val reviewCount: Long = 0
    ) {
        data class FeedbackList(
                @SerializedName("attachments")
                @Expose
                val imageAttachments: List<ImageAttachment> = listOf(),
                @SerializedName("videoAttachments")
                @Expose
                val videoAttachments: List<VideoAttachment> = listOf(),
                @SerializedName("autoReply")
                @Expose
                val autoReply: Boolean = false,
                @SerializedName("feedbackIDStr")
                @Expose
                val feedbackID: String = "",
                @SerializedName("rating")
                @Expose
                val rating: Int? = 0,
                @SerializedName("replyText")
                @Expose
                val replyText: String? = "",
                @SerializedName("replyTime")
                @Expose
                val replyTime: String? = "",
                @SerializedName("reviewText")
                @Expose
                val reviewText: String? = "",
                @SerializedName("reviewTime")
                @Expose
                val reviewTime: String? = "",
                @SerializedName("reviewerName")
                @Expose
                val reviewerName: String? = "",
                @SerializedName("variantName")
                @Expose
                val variantName: String? = "",
                @SerializedName("isKejarUlasan")
                @Expose
                val isKejarUlasan: Boolean = false,
                @SerializedName("badRatingReasonFmt")
                @Expose
                val badRatingReasonFmt: String = ""
        ) {
            data class ImageAttachment(
                    @SerializedName("thumbnailURL")
                    @Expose
                    val thumbnailURL: String? = "",
                    @SerializedName("fullsizeURL")
                    @Expose
                    val fullSizeURL: String? = ""
            )

            data class VideoAttachment(
                    @SerializedName("videoUrl")
                    @Expose
                    val videoUrl: String? = ""
            )
        }

        data class Topic(
                @SerializedName("count")
                @Expose
                val count: Int? = 0,
                @SerializedName("formatted")
                @Expose
                val formatted: String? = "",
                @SerializedName("title")
                @Expose
                val title: String? = ""
        )

        data class AggregatedRating(
                @SerializedName("rating")
                @Expose
                val rating: Int = 0,
                @SerializedName("ratingCount")
                @Expose
                val ratingCount: Int = 0
        )
    }
}