package com.tokopedia.content.product.preview.data

data class ContentUiModel(
    val id: String = "",
    val selected: Boolean = false,
    val type: MediaType = MediaType.Unknown,
    val url: String = "",
) {
    enum class MediaType(val value: String) {
        Image(""), Video(""), Unknown(""),
    }
}
