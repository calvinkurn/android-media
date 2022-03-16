package com.tokopedia.buyerorder.detail.data

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import java.io.Serializable

class ActionColor(
    @SerializedName("textColor")
    @Expose
    val textColor: String = "",

    @SerializedName("border")
    @Expose
    val border: String = "",

    @SerializedName("background")
    @Expose
    val background: String = ""
) : Serializable