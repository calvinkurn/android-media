package com.tokopedia.review.feature.ovoincentive.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TickerResponse(
    @SerializedName("subtitle")
    @Expose
    var subtitle: String = ""
) : Serializable