package com.tokopedia.webview.ext

import java.net.URLDecoder
import java.net.URLEncoder

fun String.encodeOnce(): String {
    if (contains(Regex("[/?]"))) {
        try {
            return URLEncoder.encode(this, "UTF-8")
        } catch (e: Exception) {
            return this
        }
    } else {
        return this
    }
}

fun String.decode(): String {
    return if (isUrlEncoded()) {
        try {
            URLDecoder.decode(this, "UTF-8")
        } catch (e: Exception) {
            this
        }
    } else {
        this
    }
}

fun String.isUrlEncoded() : Boolean {
    return contains("%2F") || contains("%252F")
}
