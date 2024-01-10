package com.tokopedia.shareexperience.domain.model.request.imagegenerator

data class ShareExImageGeneratorWrapperRequest(
    val params: ShareExImageGeneratorRequest? = null,
    val originalImageUrl: String = ""
)
