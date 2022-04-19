package com.tokopedia.product.detail.data.model.talk

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Answer(
        @SerializedName("answerID")
        @Expose
        val answerID: String = "",
        @SerializedName("content")
        @Expose
        val content: String = "",
        @SerializedName("userName")
        @Expose
        val userName: String = "",
        @SerializedName("userThumbnail")
        @Expose
        val userThumbnailOld: String = "",
        @SerializedName("userThumbnail2")
        @Expose
        val userThumbnail: String = "",
        @SerializedName("userID")
        @Expose
        val userId: String = "",
        @SerializedName("isSeller")
        @Expose
        val isSeller: Boolean = false,
        @SerializedName("createTime")
        @Expose
        val createTime: String = "",
        @SerializedName("createTimeFormatted")
        @Expose
        val createTimeFormatted: String = "",
        @SerializedName("attachedProductCount")
        @Expose
        val attachedProductCount: Int = 0
)