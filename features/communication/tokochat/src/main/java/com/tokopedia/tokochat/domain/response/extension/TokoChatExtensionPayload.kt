package com.tokopedia.tokochat.domain.response.extension

import com.google.gson.annotations.SerializedName

data class TokoChatExtensionPayload (
    @SerializedName("extension")
    var extension: String? = "",

    @SerializedName("duration_in_ms")
    var duration: Long? = 0,

    @SerializedName("id")
    var id: String? = ""
)
