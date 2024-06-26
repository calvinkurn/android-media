package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class Button(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("code")
    val code: String = "",
    @SerializedName("message")
    val message: String = "",
    @SerializedName("color")
    val color: String = ""
)
