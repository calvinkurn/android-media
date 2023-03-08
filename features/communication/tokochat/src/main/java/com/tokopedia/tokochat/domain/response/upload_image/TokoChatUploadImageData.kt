package com.tokopedia.tokochat.domain.response.upload_image

import com.google.gson.annotations.SerializedName

data class TokoChatUploadImageData(
    @SerializedName("success")
    var success: Boolean? = false,

    @SerializedName("image_id")
    var imageId: String? = ""
)
