package com.tokopedia.tokochat.domain.response.upload_image

import com.google.gson.annotations.SerializedName

data class TokoChatUploadImageResponse(
    @SerializedName("data")
    var data: TokoChatUploadImageData? = TokoChatUploadImageData(),

    @SerializedName("errors")
    var error: List<String>? = listOf()
)
