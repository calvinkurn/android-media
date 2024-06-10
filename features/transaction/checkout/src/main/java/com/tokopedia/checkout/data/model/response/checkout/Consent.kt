package com.tokopedia.checkout.data.model.response.checkout

import com.google.gson.annotations.SerializedName

data class Consent(
    @SerializedName("show")
    val show: Boolean = false,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("text")
    val text: String = ""
)
