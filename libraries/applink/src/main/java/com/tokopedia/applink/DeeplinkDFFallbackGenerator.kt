package com.tokopedia.applink

import android.net.Uri

object DeeplinkDFFallbackGenerator {
    // generate fallback for report product
    // example: https://m.tokopedia.com/mitimeshop/sarung-tangan-plastik-isi-100-50-pasang/report
    fun generateProductReportFallback(deeplink: String): String {
        val uri = Uri.parse(deeplink)
        var fallbackUrl = uri.getQueryParameter("url") ?: return ""
        if (!fallbackUrl.endsWith("/")) {
            fallbackUrl += "/"
        }
        fallbackUrl.replace("www.", "m.")
        fallbackUrl += "report/"
        return fallbackUrl
    }
}