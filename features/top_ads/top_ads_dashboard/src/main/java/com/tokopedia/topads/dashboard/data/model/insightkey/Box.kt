package com.tokopedia.topads.dashboard.data.model.insightkey


import com.google.gson.annotations.SerializedName

data class Box(
    @SerializedName("button")
    val button: Button = Button(),
    @SerializedName("desc")
    val desc: String = "",
    @SerializedName("img")
    val img: String = "",
    @SerializedName("title")
    val title: String = ""
)