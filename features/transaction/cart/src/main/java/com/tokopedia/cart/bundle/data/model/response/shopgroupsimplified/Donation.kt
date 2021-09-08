package com.tokopedia.cart.bundle.data.model.response.shopgroupsimplified

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Donation(
    @SerializedName("Title")
    val title: String = "",
    @SerializedName("Nominal")
    val nominal: Int = 0,
    @SerializedName("Description")
    val description: String = ""
)
