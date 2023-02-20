package com.tokopedia.tokochat.domain.response.extension

import com.google.gson.annotations.SerializedName

data class TokoChatImageResult(
    @SerializedName("data")
    var data: TokoChatImageData? = TokoChatImageData(),

    @SerializedName("success")
    var success: Boolean? = false,

    @SerializedName("error")
    var error: List<TokoChatImageError>? = listOf()
)
