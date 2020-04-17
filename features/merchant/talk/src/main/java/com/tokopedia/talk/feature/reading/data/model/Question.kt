package com.tokopedia.talk.feature.reading.data.model

import com.google.gson.annotations.SerializedName

data class Question(
        @SerializedName("content")
        val content: String = "",
        @SerializedName("maskedContent")
        val maskedContent: String = "",
        @SerializedName("userName")
        val userName: String = "",
        @SerializedName("userID")
        val userId: Int = 0,
        @SerializedName("createTime")
        val createTime: String = "",
        @SerializedName("createTimeFormatted")
        val createTimeFormatted: String = "",
        @SerializedName("likeCount")
        val likeCount: Int = 0,
        @SerializedName("state")
        val state: State = State(),
        @SerializedName("totalAnswer")
        val totalAnswer: Int = 0,
        @SerializedName("answer")
        val answer: Answer = Answer()
)