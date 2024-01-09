package com.tokopedia.shareexperience.data.dto.imagegenerator

import com.google.gson.annotations.SerializedName

data class ShareExImageGeneratorWrapperResponseDto(
    @SerializedName("imagenerator_generate_image")
    val imageGeneratorModel: ShareExImageGeneratorResponseDto = ShareExImageGeneratorResponseDto()
)
