package com.tokopedia.shareexperience.data.dto.imagegenerator

import com.google.gson.annotations.SerializedName

data class ShareExPropertyImageGeneratorArgResponseDto(
    @SerializedName("key")
    val key: String = "",
    @SerializedName("value")
    val value: String = ""
)
