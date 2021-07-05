package com.tokopedia.product.detail.data.model.review

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LikeDislike(
        @SerializedName("likeStatus")
        @Expose
        val likeStatus: Int = 0,

        @SerializedName("totalDislike")
        @Expose
        val totalDislike: Int = 0,

        @SerializedName("totalLike")
        @Expose
        val totalLike: Int = 0,

        @SerializedName("isShowable")
        @Expose
        val isShowable: Boolean = false
)

data class User(
        @SerializedName("fullName")
        @Expose
        val fullName: String = "",

        @SerializedName("image")
        @Expose
        val image: String = "",

        @SerializedName("label")
        @Expose
        val label: String = "",

        @SerializedName("url")
        @Expose
        val url: String = "",

        @SerializedName("userId")
        @Expose
        val userId: Int = 0
)

data class ImageAttachment(
        @SerializedName("attachmentId")
        @Expose
        val attachmentId: Int = 0,

        @SerializedName("imageThumbnailUrl")
        @Expose
        val imageThumbnailUrl: String = "",

        @SerializedName("imageUrl")
        @Expose
        val imageUrl: String = ""
)