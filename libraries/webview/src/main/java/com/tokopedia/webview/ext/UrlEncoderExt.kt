package com.tokopedia.webview.ext

import com.tokopedia.webview.WebViewHelper.normalizeSymbol
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

fun String.encode(): String {
    try {
        return URLEncoder.encode(this, "UTF-8")
    } catch (e: Exception) {
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
fun String.isUrlEncoded(): Boolean {
    return contains("%2F") || contains("%252F") ||
        contains("%3F") || contains("%253F")
}

/**
 * input:
 * https://accounts.tokopedia.com/oauth/redirect?redirect_uri=https://web.stage.halodoc.com/tanya-dokter/konsultasi-resep?title=Chat-Dokter&source=tokopedia&consultation_id=448153&_single=true&redirect_tokopedia=tokopedia%3A%2F%2Fback
 *
 * output:
 * https://accounts.tokopedia.com/oauth/redirect?redirect_uri=https%3A%2F%2Fweb.stage.halodoc.com%2Ftanya-dokter%2Fkonsultasi-resep%3Ftitle%3DChat-Dokter%26source%3Dtokopedia%26consultation_id%3D448153%26_single%3Dtrue%26redirect_tokopedia%3Dtokopedia%253A%252F%252Fback
 */
fun String.encodeQueryNested(): String {
    try {
        var url = this.decodeNested()
        var strBefore: String
        var strAfter: String
        var scheme: String
        var indexEqual: Int
        var encodedUrl: String
        var isInQueryParam: Boolean
        var indexScheme = url.lastIndexOf("://")
        while (indexScheme > -1) {
            strBefore = url.substring(0, indexScheme)
            indexEqual = strBefore.lastIndexOf("=")
            isInQueryParam = indexEqual > -1
            if (isInQueryParam) {
                scheme = strBefore.substring(indexEqual + 1)
            } else {
                scheme = strBefore
            }
            if (isInQueryParam) {
                strAfter = url.substring(indexScheme + 3)
                encodedUrl = "$scheme://$strAfter".encodeNormalized()
                url = "${strBefore.substring(0, indexEqual)}=$encodedUrl"
            } else {
                break
            }
            indexScheme = url.lastIndexOf("://")
        }
        return url
    } catch (e: Exception) {
        return this
    }
}

// case where url has parameter & after query param
// example: back_url=https://tokopedia.com/help&user=abc
private fun String.encodeNormalized(): String {
    val strAfterNormalized = normalizeSymbol()
    return if (strAfterNormalized != this) {
        strAfterNormalized.encode() + "&" + substringAfter("&")
    } else {
        encode()
    }
}

fun String.decodeNested(): String {
    var decodedString: String = this
    while (decodedString.isUrlEncoded()) {
        decodedString = decodedString.decode()
    }
    return decodedString
}
