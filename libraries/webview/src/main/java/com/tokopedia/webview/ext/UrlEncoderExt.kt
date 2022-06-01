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
/**
 * If String url is encoded, do decode to url string,
 * if String url is already decoded, no need to do decode again
 * because if String url contains `+` and is already decoded, when we do decode
 * will change it to space and will break the string url
 */
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
/**
 * Check the String url is encoded or not
 */
fun String.isUrlEncoded() : Boolean {
    return contains("%2F") || contains("%252F") ||
            contains("%3F") || contains("%253F")
}
