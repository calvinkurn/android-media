package com.tokopedia.talk.feature.reply.data.model.discussion

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DiscussionDataByQuestionID(
        @SerializedName("question")
        @Expose
        val question: Question = Question(),
        @SerializedName("maxAnswerLength")
        @Expose
        val maxAnswerLength: Int = 0
)