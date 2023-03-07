package com.tokopedia.tokochat.domain.response.extension

import com.google.gson.annotations.SerializedName

data class TokoChatImageData (
    @SerializedName("url")
    var url: String? = "",

    @SerializedName("expiry")
    var expiry: Long? = 0L,

    @SerializedName("content_type")
    var contentType: String? = ""
)
