package com.tokopedia.shareexperience.data.dto.response.imagegenerator

import com.google.gson.annotations.SerializedName

data class ShareExImageGeneratorResponseDto(
    @SerializedName("args")
    val args: List<ShareExImageGeneratorArgResponseDto> = listOf(),
    @SerializedName("sourceId")
    val sourceId: String = ""
)
