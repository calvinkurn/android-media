package com.tokopedia.shareexperience.domain.model

data class ShareExImageGeneratorModel(
    val sourceId: String = "",
    val payload: Map<String, Any> = mapOf()
)
