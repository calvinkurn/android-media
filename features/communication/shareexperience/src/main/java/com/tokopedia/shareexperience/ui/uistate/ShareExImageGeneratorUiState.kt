package com.tokopedia.shareexperience.ui.uistate

data class ShareExImageGeneratorUiState(
    val selectedImageUrl: String = "",
    val sourceId: String = "",
    val args: Map<String, String> = mapOf()
)
