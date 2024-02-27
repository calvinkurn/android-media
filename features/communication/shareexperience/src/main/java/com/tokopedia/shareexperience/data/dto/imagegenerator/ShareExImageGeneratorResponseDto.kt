package com.tokopedia.shareexperience.data.dto.imagegenerator

import com.google.gson.annotations.SerializedName

data class ShareExImageGeneratorResponseDto(
    @SerializedName("image_url")
    val imageUrl: String = "",

    @SerializedName("source_id")
    val sourceId: String = ""
)
