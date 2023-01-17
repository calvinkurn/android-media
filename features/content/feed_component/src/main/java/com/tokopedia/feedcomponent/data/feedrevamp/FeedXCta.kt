package com.tokopedia.feedcomponent.data.feedrevamp

import com.google.gson.annotations.SerializedName

data class FeedXCta (
    @SerializedName("text")
    val text: String = "",
    @SerializedName("subtitle")
    val subtitle: List<String> = emptyList(),
    @SerializedName("color")
    val color: String = "",
    @SerializedName("colorGradient")
    val colorGradient: List<FeedXCtaColorGradient> = emptyList()
)

data class FeedXCtaColorGradient(
    @SerializedName("color")
    val color: String = "",
    @SerializedName("position")
    val position: Float,
)
