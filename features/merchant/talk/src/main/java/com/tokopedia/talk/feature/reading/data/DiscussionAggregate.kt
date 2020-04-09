package com.tokopedia.talk.feature.reading.data

import com.google.gson.annotations.SerializedName

data class DiscussionAggregate(
        @SerializedName("productName")
        val productName: String = "",
        @SerializedName("thumbnail")
        val thumbnail: String = "",
        @SerializedName("category")
        val category: DiscussionAggregateCategory
)

