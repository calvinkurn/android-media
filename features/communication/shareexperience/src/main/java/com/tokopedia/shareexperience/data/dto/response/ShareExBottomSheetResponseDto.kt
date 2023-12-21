package com.tokopedia.shareexperience.data.dto.response

import com.google.gson.annotations.SerializedName
import com.tokopedia.shareexperience.data.dto.response.imagegenerator.ShareExImageGeneratorResponseDto

data class ShareExBottomSheetResponseDto(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("subtitle")
    val subtitle: String = "",
    @SerializedName("chips")
    val chips: List<String> = listOf(),
    @SerializedName("properties")
    val properties: List<ShareExPropertyResponseDto> = listOf(),
    @SerializedName("imageGeneratorPayload")
    val imageGeneratorPayload: ShareExImageGeneratorResponseDto = ShareExImageGeneratorResponseDto()
)
