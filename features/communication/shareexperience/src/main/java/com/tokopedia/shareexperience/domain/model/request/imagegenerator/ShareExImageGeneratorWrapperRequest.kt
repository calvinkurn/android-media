package com.tokopedia.shareexperience.domain.model.request.imagegenerator

data class ShareExImageGeneratorWrapperRequest(
    val params: ShareExImageGeneratorRequest,
    val originalImageUrl: String,
    val platform: String,
    val imageResolution: String
)
