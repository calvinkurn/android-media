package com.tokopedia.minicart.common.data.response.updatecart

import com.google.gson.annotations.SerializedName

data class ToasterAction(
        @SerializedName("text")
        val text: String = "",
        @SerializedName("show_cta")
        val showCta: Boolean = false
)