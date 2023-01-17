package com.tokopedia.universal_sharing.model

import com.google.gson.annotations.SerializedName

data class ImagePolicyResponse(
    @SerializedName("imagenerator_policy")
    val response: ImagePolicyArgs = ImagePolicyArgs()
)

data class ImagePolicyArgs(
    @SerializedName("args")
    val args: List<ImagePolicy> = listOf()
)

data class ImagePolicy(
    @SerializedName("key")
    val key: String = "",

    @SerializedName("type")
    val type: String = "",

    @SerializedName("required")
    val required: Boolean = false
) {
    fun toRequestParam(value: String): ImageGeneratorRequestData = ImageGeneratorRequestData(key, value)
}




