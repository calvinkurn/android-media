package com.tokopedia.affiliate.sse.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AffiliateSSEResponse(
    @SerializedName("event")
    @Expose
    val event: String = "",

    @SerializedName("message")
    @Expose
    val message: String = ""
)
