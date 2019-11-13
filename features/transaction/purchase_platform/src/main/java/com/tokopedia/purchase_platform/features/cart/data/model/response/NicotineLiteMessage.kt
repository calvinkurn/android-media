package com.tokopedia.purchase_platform.features.cart.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NicotineLiteMessage(
        @field:Expose
        @field:SerializedName("text")
        val text: String = "",
        @field:Expose
        @field:SerializedName("url")
        val url: String = ""
)