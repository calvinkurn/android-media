package com.tokopedia.universal_sharing.model

import com.google.gson.annotations.SerializedName

data class ImagePolicyResponse(
    @SerializedName("args")
    val response: List<ImagePolicy>
)

data class ImagePolicy(
    @SerializedName("key")
    val key: String = "",

    @SerializedName("type")
    val type: String = "",

    @SerializedName("required")
    val required: Boolean
) {
    fun toRequestParam(value: String): ImageGeneratorRequestData = ImageGeneratorRequestData(key, value)
}




