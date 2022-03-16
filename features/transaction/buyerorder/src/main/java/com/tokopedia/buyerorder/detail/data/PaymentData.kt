package com.tokopedia.buyerorder.detail.data

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class PaymentData(
    @SerializedName("label")
    @Expose
    val label: String = "",

    @SerializedName("value")
    @Expose
    val value: String = "",

    @SerializedName("textColor")
    @Expose
    val textColor: String = ""
)