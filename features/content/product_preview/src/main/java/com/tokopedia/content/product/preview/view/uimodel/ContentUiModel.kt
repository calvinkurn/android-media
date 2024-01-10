package com.tokopedia.content.product.preview.view.uimodel

data class ContentUiModel(
    val contentId: String = "",
    val selected: Boolean = false,
    val type: MediaType = MediaType.Unknown,
    val url: String = "",
) {
    enum class MediaType(val value: String) {
        Image(""), Video(""), Unknown(""),
    }
}
