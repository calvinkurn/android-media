package com.tokopedia.gallery.networkmodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ImageReviewGqlResponse {

    @SerializedName("ProductReviewImageListQuery")
    @Expose
    val productReviewImageListQuery: ProductReviewImageListQuery? = null

    class ProductReviewImageListQuery {
        @SerializedName("list")
        @Expose
        var list: List<Item>? = null
        @SerializedName("detail")
        @Expose
        var detail: Detail? = null
        @SerializedName("hasNext")
        @Expose
        var isHasNext: Boolean = false
        @SerializedName("hasPrev")
        @Expose
        var isHasPrev: Boolean = false
    }

    class Item {

        @SerializedName("imageID")
        @Expose
        var imageID: Int = 0
        @SerializedName("reviewID")
        @Expose
        var reviewID: Int = 0
        @SerializedName("imageSibling")
        @Expose
        var imageSibling: List<Int>? = null
    }

    class Detail {

        @SerializedName("reviews")
        @Expose
        var reviews: List<Review>? = null
        @SerializedName("images")
        @Expose
        var images: List<Image>? = null
    }

    class Image {

        @SerializedName("imageAttachmentID")
        @Expose
        var imageAttachmentID: Int = 0
        @SerializedName("description")
        @Expose
        var description: String? = null
        @SerializedName("uriThumbnail")
        @Expose
        var uriThumbnail: String? = null
        @SerializedName("uriLarge")
        @Expose
        var uriLarge: String? = null
        @SerializedName("reviewID")
        @Expose
        var reviewID: Int = 0
    }

    class Review {

        @SerializedName("reviewId")
        @Expose
        var reviewId: Int = 0
        @SerializedName("message")
        @Expose
        var message: String? = null
        @SerializedName("ratingDescription")
        @Expose
        var ratingDescription: String? = null
        @SerializedName("rating")
        @Expose
        var rating: Int = 0
        @SerializedName("time_format")
        @Expose
        var timeFormat: TimeFormat? = null
        @SerializedName("updateTime")
        @Expose
        var updateTime: Int = 0
        @SerializedName("isAnonymous")
        @Expose
        var isIsAnonymous: Boolean = false
        @SerializedName("isReportable")
        @Expose
        var isIsReportable: Boolean = false
        @SerializedName("isUpdated")
        @Expose
        var isIsUpdated: Boolean = false
        @SerializedName("reviewer")
        @Expose
        var reviewer: Reviewer? = null

    }

    class Reviewer {

        @SerializedName("userID")
        @Expose
        var userID: Int = 0
        @SerializedName("fullName")
        @Expose
        var fullName: String? = null
        @SerializedName("profilePicture")
        @Expose
        var profilePicture: String? = null
        @SerializedName("url")
        @Expose
        var url: String? = null

    }


    class TimeFormat {

        @SerializedName("date_time_fmt1")
        @Expose
        var dateTimeFmt1: String? = null

    }
}
