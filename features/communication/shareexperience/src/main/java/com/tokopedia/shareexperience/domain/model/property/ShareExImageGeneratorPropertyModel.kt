package com.tokopedia.shareexperience.domain.model.property

data class ShareExImageGeneratorPropertyModel(
    val sourceId: String = "",
    val args: Map<String, String> = mapOf() // Convert to map to make sure no duplicate args
)
