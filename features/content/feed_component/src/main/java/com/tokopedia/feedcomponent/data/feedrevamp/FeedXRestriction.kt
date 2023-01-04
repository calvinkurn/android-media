package com.tokopedia.feedcomponent.data.feedrevamp

import com.google.gson.annotations.SerializedName

data class FeedXRestriction(
    @SerializedName("label")
    val label: String = "",
    @SerializedName("isActive")
    val isActive: Boolean = false,
)
