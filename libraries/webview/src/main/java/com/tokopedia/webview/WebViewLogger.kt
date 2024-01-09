package com.tokopedia.webview

import android.app.Activity
import android.content.Context
import android.webkit.WebView
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

internal fun Context?.logUrlTooLongToOpen(
    argUrl: String,
    loadedUrl: String?, duration: Long
) {
    ServerLogger.log(
        Priority.P1, "WEBVIEW_ERROR", webviewMapOf(
            "type" to "longOpen",
            "duration" to duration.toString(),
            "url" to argUrl,
            "web_url" to (loadedUrl ?: "")
        )
    )
}

internal fun Context?.logInvalidWebviewUrl(url: String) {
    ServerLogger.log(
        Priority.P1, "INVALID_WEBVIEW_URL", webviewMapOf(
            "type" to "browser",
            "url" to url
        )
    )
}

private fun Context?.webviewMapOf(
    vararg pairs: Pair<String, String>
): MutableMap<String, String> {
    val ctx = this
    return mutableMapOf<String, String>().apply {
        this.putAll(pairs)
        this.addWebviewVersion(ctx)
        if (ctx != null && ctx is Activity) {
            ctx.let { act ->
                this["activity"] = (act::class.java.simpleName.toString())
            }
            if (ctx is BaseActivity) {
                try {
                    val fragmentName = ctx.supportFragmentManager.fragments[0].javaClass.simpleName
                    this["source"] = fragmentName
                } catch (ignored: Exception) { }
            }
        }
    }
}

private fun MutableMap<String, String>?.addWebviewVersion(context: Context?) {
    this?.let { map ->
        val webviewPackage = WebViewHelper.getCurrentWebViewPackage(context)
        map["webviewPackage"] = ((webviewPackage?.packageName?.toString()) ?: "")
        map["webviewVersion"] = ((webviewPackage?.versionName?.toString()) ?: "")
    }
}

internal fun Context?.logWebReceivedError(
    webView: WebView,
    failingUrl: String?,
    errorCodeString: String,
    description: String?,
) {
    val ctx = this
    ServerLogger.log(
        Priority.P1, "WEBVIEW_ERROR", ctx.webviewMapOf(
            "type" to (failingUrl ?: ""),
            "error_code" to errorCodeString,
            "desc" to (description ?: ""),
            "web_url" to (webView.url ?: "")
        )
    )
}

internal fun Context?.logApplinkErrorOpen(
    previousUriString: String,
    url: String
) {
    ServerLogger.log(
        Priority.P1, "APPLINK_OPEN_ERROR", webviewMapOf(
            "referrer" to previousUriString,
            "uri" to url
        )
    )
}
