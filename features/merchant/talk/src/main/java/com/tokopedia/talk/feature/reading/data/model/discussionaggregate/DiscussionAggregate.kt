package com.tokopedia.talk.feature.reading.data.model.discussionaggregate

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DiscussionAggregate(
        @SerializedName("productName")
        @Expose
        val productName: String = "",
        @SerializedName("thumbnail")
        @Expose
        val thumbnail: String = "",
        @SerializedName("url")
        @Expose
        val url: String = "",
        @SerializedName("category")
        @Expose
        val category: List<DiscussionAggregateCategory> = listOf()
)

