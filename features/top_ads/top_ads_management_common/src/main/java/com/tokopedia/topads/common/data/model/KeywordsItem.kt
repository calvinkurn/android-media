package com.tokopedia.topads.common.data.model

import com.google.gson.annotations.SerializedName

open class KeywordsItem(
        @SerializedName("keywordTag")
        var keywordTag: String = "",
        @SerializedName("keywordTypeID")
        var keywordTypeID: String = "21",
        @SerializedName("toggle")
        var toggle: String = "1",
        @SerializedName("source")
        var source: String = "dashboard_add_product",
        @SerializedName("priceBid")
        var priceBid: Double = 0.0,
        @SerializedName("status")
        var status: String = "1"
)