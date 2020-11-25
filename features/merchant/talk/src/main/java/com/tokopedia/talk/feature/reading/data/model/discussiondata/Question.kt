package com.tokopedia.talk.feature.reading.data.model.discussiondata

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.talk.feature.reading.data.model.discussiondata.Answer
import com.tokopedia.talk.feature.reading.data.model.discussiondata.State

data class Question(
        @SerializedName("questionID")
        @Expose
        val questionID: String= "",
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
        val userId: Int = 0,
        @SerializedName("createTime")
        @Expose
        val createTime: String = "",
        @SerializedName("createTimeFormatted")
        @Expose
        val createTimeFormatted: String = "",
        @SerializedName("state")
        @Expose
        val state: State = State(),
        @SerializedName("totalAnswer")
        @Expose
        val totalAnswer: Int = 0,
        @SerializedName("answer")
        @Expose
        val answer: Answer = Answer()
)