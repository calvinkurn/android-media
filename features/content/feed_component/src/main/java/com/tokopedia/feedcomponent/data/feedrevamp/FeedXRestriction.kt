package com.tokopedia.feedcomponent.data.feedrevamp

import com.google.gson.annotations.SerializedName

data class FeedXRestriction(
    @SerializedName("label")
    var label: String = "",
    @SerializedName("isActive")
    var isActive: Boolean = false,
)
