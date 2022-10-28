package com.tokopedia.buyerorder.detail.data

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class PassengerInformation(
    @SerializedName("name")
    @Expose
    val name: String = "",

    @SerializedName("value")
    @Expose
    val value: String = "",

    @SerializedName("title")
    @Expose
    val title: String = ""
)