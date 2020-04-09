package com.tokopedia.talk.feature.reading.data

import com.google.gson.annotations.SerializedName

data class DiscussionAggregateCategory(
        @SerializedName("name")
        val name: String = "",
        @SerializedName("text")
        val text: String = "",
        @SerializedName("counter")
        val counter: Int = 0
)
