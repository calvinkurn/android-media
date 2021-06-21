package com.tokopedia.product.addedit.productlimitation.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Header {
    @SerializedName("reason")
    @Expose
    var reason: String = ""

    @SerializedName("messages")
    @Expose
    var messages: List<String> = emptyList()

    @SerializedName("errorCode")
    @Expose
    var errorCode: String = ""
}