package com.tokopedia.cart.data.model.response.updatecart

import com.google.gson.annotations.SerializedName

data class Button(
        @SerializedName("text")
        val text: String = "",
        @SerializedName("link")
        val link: String = "",
        @SerializedName("action")
        val action: String = "",
        @SerializedName("color")
        val color: String = ""
)