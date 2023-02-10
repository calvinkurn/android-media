package com.tokopedia.media.loader.utils

import android.net.Uri

// convert String to Uri
fun String.toUri(): Uri? {
    return Uri.parse(this)
}

fun String.isValidUrl(): Boolean {
    return this.isNotEmpty() && (this.contains("https") || this.contains("http"))
}
