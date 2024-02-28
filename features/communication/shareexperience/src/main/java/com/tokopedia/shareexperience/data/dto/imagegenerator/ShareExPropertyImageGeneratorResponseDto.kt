package com.tokopedia.shareexperience.data.dto.imagegenerator

import com.google.gson.annotations.SerializedName

data class ShareExPropertyImageGeneratorResponseDto(
    @SerializedName("args")
    val args: List<ShareExPropertyImageGeneratorArgResponseDto> = listOf(),
    @SerializedName("sourceId")
    val sourceId: String = ""
)
