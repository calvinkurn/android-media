package com.tokopedia.talk.feature.reading.data.model

import com.google.gson.annotations.SerializedName

data class DiscussionAggregate(
        @SerializedName("productName")
        val productName: String = "",
        @SerializedName("thumbnail")
        val thumbnail: String = "",
        @SerializedName("url")
        val url: String = "",
        @SerializedName("category")
        val category: List<DiscussionAggregateCategory> = listOf()
)

