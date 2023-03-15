package com.tokopedia.epharmacy.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.tokopedia.applink.RouteManager
import com.tokopedia.webview.ext.encode
import java.lang.Exception

fun Context.openDocument(url: String) {
    val uri = Uri.parse(url)
    if (uri.path?.endsWith(".pdf") == true && url.startsWith("http")) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/pdf")
        try {
            startActivity(intent)
        } catch (e: Exception) {
            val appLinkPdfWebView = "${WEB_LINK_PREFIX}${getPdfInWebViewUrl(url)}"
            RouteManager.route(this, appLinkPdfWebView)
        }
    } else {
        RouteManager.route(this, "${WEB_LINK_PREFIX}$url")
    }
}

fun getPdfInWebViewUrl(url: String): String {
    return "${getGoogleDocEmbeddedUrl()}${url.encode()}".encode()
}

fun getGoogleDocEmbeddedUrl(): String {
    return "https://docs.google.com/viewer?url="
}
