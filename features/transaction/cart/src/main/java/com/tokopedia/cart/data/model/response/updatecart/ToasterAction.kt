package com.tokopedia.cart.data.model.response.updatecart

import com.google.gson.annotations.SerializedName

data class ToasterAction(
        @SerializedName("text")
        val text: String = "",
        @SerializedName("show_cta")
        val showCta: Boolean = false
)