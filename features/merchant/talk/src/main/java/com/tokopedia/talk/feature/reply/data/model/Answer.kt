package com.tokopedia.talk.feature.reply.data.model

import com.google.gson.annotations.SerializedName

data class Answer(
        @SerializedName("answerID")
        val answerID: String = "",
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
        @SerializedName("likeCount")
        val likeCount: Int = 0,
        @SerializedName("attachedProductCount")
        val attachedProductCount: Int = 0
)