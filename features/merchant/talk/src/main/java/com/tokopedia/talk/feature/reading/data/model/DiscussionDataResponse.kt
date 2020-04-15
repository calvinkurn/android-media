package com.tokopedia.talk.feature.reading.data.model

import com.google.gson.annotations.SerializedName

data class DiscussionDataResponse(
        @SerializedName("hasNext")
        val hasNext: Boolean,
        @SerializedName("totalQuestion")
        val totalQuestion: Int,
        @SerializedName("question")
        val question: Question
)