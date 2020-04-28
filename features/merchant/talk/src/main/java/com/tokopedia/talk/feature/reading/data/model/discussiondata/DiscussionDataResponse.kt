package com.tokopedia.talk.feature.reading.data.model.discussiondata

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DiscussionDataResponse(
        @SerializedName("hasNext")
        @Expose
        val hasNext: Boolean =  false,
        @SerializedName("totalQuestion")
        @Expose
        val totalQuestion: Int = 0,
        @SerializedName("question")
        @Expose
        val question: List<Question> = listOf()
)