package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class ProductTrackerData(
        @SerializedName("attribution")
        val attribution: String = ""
)