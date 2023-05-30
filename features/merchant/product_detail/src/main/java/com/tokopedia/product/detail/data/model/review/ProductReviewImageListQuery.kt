package com.tokopedia.product.detail.data.model.review

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductReviewImageListQuery(
    @SerializedName("list")
    @Expose
    var list: List<Item>? = null,
    @SerializedName("detail")
    @Expose
    var detail: Detail? = null,
    @SerializedName("hasNext")
    @Expose
    var isHasNext: Boolean = false
) {
    data class Item(
        @SerializedName("reviewID")
        @Expose
        var reviewID: String = "0"
    )

    class Detail(
        @SerializedName("reviews")
        @Expose
        var reviews: List<Review>? = null,
        @SerializedName("images")
        @Expose
        var images: List<Image>? = null,
        @SerializedName("videos")
        @Expose
        var videos: List<Video>? = null,
        @SerializedName("mediaCountFmt")
        @Expose
        var mediaCountFmt: String = "",
        @SerializedName("mediaCount")
        @Expose
        var mediaCount: String = "",
        @SerializedName("mediaCountTitle")
        @Expose
        var mediaTitle: String = ""
    ) {
        data class Image(
            @SerializedName("imageAttachmentID")
            @Expose
            var imageAttachmentID: String = "0",
            @SerializedName("description")
            @Expose
            var description: String? = null,
            @SerializedName("uriThumbnail")
            @Expose
            var uriThumbnail: String? = null,
            @SerializedName("uriLarge")
            @Expose
            var uriLarge: String? = null,
            @SerializedName("reviewID")
            @Expose
            var reviewID: String = ""
        )

        data class Video(
            @SerializedName("attachmentID")
            @Expose
            var attachmentID: String = "0",
            @SerializedName("url")
            @Expose
            var url: String? = "",
            @SerializedName("feedbackID")
            @Expose
            var feedbackID: String? = ""
        )

        data class Review(
            @SerializedName("message")
            @Expose
            var message: String? = null,
            @SerializedName("rating")
            @Expose
            var rating: Int = 0,
            @SerializedName("updateTime")
            @Expose
            var updateTime: Int = 0
        )
    }
}
