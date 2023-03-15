package com.tokopedia.epharmacy.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.tokopedia.applink.RouteManager
import com.tokopedia.webview.GOOGLE_DOCS_PDF_URL
import com.tokopedia.webview.ext.decode
import java.lang.Exception
import java.net.URLEncoder

fun Context.openPdf(url: String) {
    val uri = Uri.parse(url)
    if (uri.path?.endsWith(".pdf") == true && url.startsWith("http")) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.parse(uri.toString().replace(GOOGLE_DOCS_PDF_URL, "").decode()), "application/pdf")
        try {
            startActivity(intent)
        } catch (e: Exception) {
            val encodeUrl = URLEncoder.encode(url, "UTF-8")
            val secondEncodeUrl = URLEncoder.encode("https://docs.google.com/viewer?url=$encodeUrl", "UTF-8")
            RouteManager.route(this, "${WEB_LINK_PREFIX}$secondEncodeUrl")
        }
    } else {
        RouteManager.route(this, "${WEB_LINK_PREFIX}$url")
    }
}
