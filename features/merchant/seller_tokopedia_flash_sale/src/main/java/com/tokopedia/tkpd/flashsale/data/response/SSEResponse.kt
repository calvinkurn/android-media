package com.tokopedia.tkpd.flashsale.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SSEResponse(
    @SerializedName("event")
    @Expose
    val event: String = "",

    @SerializedName("message")
    @Expose
    val message: String = ""
)
