package com.tokopedia.talk.feature.reply.data.model.discussion

import com.google.gson.annotations.SerializedName

data class DiscussionDataByQuestionID(
        @SerializedName("question")
        val question: Question = Question()
)