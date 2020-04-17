package com.tokopedia.talk.feature.reading.data.model

import com.google.gson.annotations.SerializedName

data class DiscussionDataResponse(
        @SerializedName("hasNext")
        val hasNext: Boolean =  false,
        @SerializedName("totalQuestion")
        val totalQuestion: Int = 0,
        @SerializedName("question")
        val question: List<Question> = listOf()
)