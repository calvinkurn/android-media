package com.tokopedia.topads.dashboard.data.model.insightkey


import com.google.gson.annotations.SerializedName

data class InsightKeyResponse(
    @SerializedName("data")
    val `data`: List<Data> = listOf(),
    @SerializedName("header")
    val header: Header = Header(),
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("text")
    val text: String = ""
)