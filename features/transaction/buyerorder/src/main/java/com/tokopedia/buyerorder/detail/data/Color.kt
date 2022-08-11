package com.tokopedia.buyerorder.detail.data

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Color(
    @SerializedName("border")
    @Expose
    val border: String = "",

    @SerializedName("background")
    @Expose
    val background: String = "",

    @SerializedName("textColor")
    @Expose
    val textColor: String = ""
)