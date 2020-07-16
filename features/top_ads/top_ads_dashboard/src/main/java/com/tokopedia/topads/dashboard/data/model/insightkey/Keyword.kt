package com.tokopedia.topads.dashboard.data.model.insightkey


import com.google.gson.annotations.SerializedName

data class Keyword(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("priceBid")
    val priceBid: Int = 0,
    @SerializedName("source")
    val source: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("tag")
    val tag: String = "",
    @SerializedName("type")
    val type: String = ""
)