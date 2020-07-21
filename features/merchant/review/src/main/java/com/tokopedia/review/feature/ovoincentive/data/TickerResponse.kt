package com.tokopedia.review.feature.ovoincentive.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TickerResponse(
        @SerializedName("title")
        @Expose
        var title: String = "",
        @SerializedName("subtitle")
        @Expose
        var subtitle: String = ""
)