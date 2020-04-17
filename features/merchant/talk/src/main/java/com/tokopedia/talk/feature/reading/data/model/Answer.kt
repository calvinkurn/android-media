package com.tokopedia.talk.feature.reading.data.model

import com.google.gson.annotations.SerializedName

data class Answer(
        @SerializedName("content")
        val content: String = "",
        @SerializedName("userName")
        val userName: String = "",
        @SerializedName("userThumbnail")
        val userThumbnail: String = "",
        @SerializedName("userID")
        val userId: String = "",
        @SerializedName("isSeller")
        val isSeller: Boolean = false,
        @SerializedName("createTime")
        val createTime: String = "",
        @SerializedName("createTimeFormatted")
        val createTimeFormatted: String = "",
        @SerializedName("attachedProductCount")
        val attachedProductCount: Int = 0
)