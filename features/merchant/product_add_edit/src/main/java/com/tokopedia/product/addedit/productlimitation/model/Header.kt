package com.tokopedia.product.addedit.productlimitation.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Header {
    @SerializedName("reason")
    @Expose
    var reason: String? = null

    @SerializedName("messages")
    @Expose
    var messages: List<Any>? = null

    @SerializedName("errorCode")
    @Expose
    var errorCode: String? = null
}