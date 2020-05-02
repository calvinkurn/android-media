package com.tokopedia.talk.feature.reading.data.model.discussiondata

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Answer(
        @SerializedName("answerID")
        @Expose
        val answerID: String = "",
        @SerializedName("content")
        @Expose
        val content: String = "",
        @SerializedName("maskedContent")
        @Expose
        val maskedContent: String = "",
        @SerializedName("userName")
        @Expose
        val userName: String = "",
        @SerializedName("userThumbnail")
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
        @SerializedName("likeCount")
        @Expose
        val likeCount: Int = 0,
        @SerializedName("state")
        @Expose
        val state: AnswerState = AnswerState(),
        @SerializedName("attachedProductCount")
        @Expose
        val attachedProductCount: Int = 0
)