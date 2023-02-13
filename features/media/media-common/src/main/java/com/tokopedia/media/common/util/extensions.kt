package com.tokopedia.media.common.util

fun String.isValidUrl(): Boolean {
    return this.isNotEmpty() && (this.contains("https") || this.contains("http"))
}
