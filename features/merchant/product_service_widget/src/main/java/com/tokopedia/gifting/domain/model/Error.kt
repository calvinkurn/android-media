package com.tokopedia.gifting.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Error {
    @SerializedName("messages")
    @Expose
    var messages: String = ""

    @SerializedName("reason")
    @Expose
    var reason: List<String> = emptyList()

    @SerializedName("errorCode")
    @Expose
    var errorCode: String = ""
}