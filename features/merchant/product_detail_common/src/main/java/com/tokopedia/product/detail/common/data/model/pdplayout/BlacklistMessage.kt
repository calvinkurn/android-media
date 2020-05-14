package com.tokopedia.product.detail.common.data.model.pdplayout


import com.google.gson.annotations.SerializedName

data class BlacklistMessage(
    @SerializedName("button")
    val button: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("url")
    val url: String = ""
)