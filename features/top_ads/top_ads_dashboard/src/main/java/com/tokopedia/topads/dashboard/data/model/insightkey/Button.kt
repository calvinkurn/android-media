package com.tokopedia.topads.dashboard.data.model.insightkey


import com.google.gson.annotations.SerializedName

data class Button(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("url")
    val url: String = ""
)