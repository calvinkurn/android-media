package com.tokopedia.tokochat.domain.response.extension

import com.google.gson.annotations.SerializedName

data class TokoChatImageResult(
    @SerializedName("data")
    var data: String = "",

    @SerializedName("success")
    var success: Boolean = false,

    @SerializedName("error")
    var error: String = ""
)
