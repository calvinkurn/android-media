package com.tokopedia.media.loader.data

data class Header(
    val key: String,
    val values: List<String>
) {
    fun key() = key.lowercase()

    fun firstValue() = values
        .first()
        .lowercase()
}
