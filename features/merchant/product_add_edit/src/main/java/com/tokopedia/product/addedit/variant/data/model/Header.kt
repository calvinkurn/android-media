package com.tokopedia.product.addedit.variant.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Header (
        @SerializedName("reason")
        @Expose
        var reason: String = "",
        @SerializedName("messages")
        @Expose
        var messages: List<String> = emptyList()
)
