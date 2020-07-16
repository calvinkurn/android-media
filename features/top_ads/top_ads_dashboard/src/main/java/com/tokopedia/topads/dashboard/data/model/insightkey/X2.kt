package com.tokopedia.topads.dashboard.data.model.insightkey


import com.google.gson.annotations.SerializedName

data class X2(
    @SerializedName("text")
    val text: String = "",
    @SerializedName("value")
    val value: Int = 0
)