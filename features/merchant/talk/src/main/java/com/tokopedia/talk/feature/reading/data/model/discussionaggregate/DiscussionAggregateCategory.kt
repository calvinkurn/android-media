package com.tokopedia.talk.feature.reading.data.model.discussionaggregate

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DiscussionAggregateCategory(
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("text")
        @Expose
        val text: String = "",
        @SerializedName("counter")
        @Expose
        val counter: Int = 0
)
