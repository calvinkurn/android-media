package com.tokopedia.people.model

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on May 12, 2023
 */
data class GetUserReviewListResponse(
    @SerializedName("productrevGetUserReviewList")
    val data: Data = Data(),
) {
    data class Data(
        @SerializedName("list")
        val reviewList: List<ReviewList> = emptyList(),

        @SerializedName("hasNext")
        val hasNext: Boolean = false,
    )

    data class ReviewList(
        @SerializedName("feedbackID")
        val feedbackID: String = "",

        @SerializedName("product")
        val product: Product = Product(),

        @SerializedName("rating")
        val rating: Int = 0,

        @SerializedName("reviewText")
        val reviewText: String = "",

        @SerializedName("reviewTime")
        val reviewTime: String = "",

        @SerializedName("attachments")
        val attachments: List<Attachment> = emptyList(),

        @SerializedName("videoAttachments")
        val videoAttachments: List<VideoAttachment> = emptyList(),

        @SerializedName("likeDislike")
        val likeDislike: LikeDislike = LikeDislike(),
    )

    data class Product(
        @SerializedName("productID")
        val productID: String = "",

        @SerializedName("productName")
        val productName: String = "",

        @SerializedName("productImageURL")
        val productImageURL: String = "",

        @SerializedName("productPageURL")
        val productPageURL: String = "",

        @SerializedName("productStatus")
        val productStatus: Int = 0,

        @SerializedName("productVariant")
        val productVariant: ProductVariant = ProductVariant()
    )

    data class ProductVariant(
        @SerializedName("variantID")
        val variantID: String = "",

        @SerializedName("variantName")
        val variantName: String = "",
    )

    data class Attachment(
        @SerializedName("attachmentID")
        val attachmentID: String = "",

        @SerializedName("thumbnailURL")
        val thumbnailURL: String = "",

        @SerializedName("fullsizeURL")
        val fullsizeURL: String = "",
    )

    data class VideoAttachment(
        @SerializedName("attachmentID")
        val attachmentID: String = "",

        @SerializedName("videoUrl")
        val videoUrl: String = "",
    )

    data class LikeDislike(
        @SerializedName("totalLike")
        val totalLike: Int = 0,

        @SerializedName("likeStatus")
        val likeStatus: Int = 0,
    )
}
