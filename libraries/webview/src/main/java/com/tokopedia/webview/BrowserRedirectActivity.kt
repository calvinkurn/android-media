package com.tokopedia.webview

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.webview.ext.decode

/**
 * Class act as intermediary/bridging to redirect to browser or native activity (webview)
 * tokopedia-android-internal://global/browser
 */
class BrowserRedirectActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val uri = intent.data
        if (uri != null) {
            val redirectIntent = getCallingIntentOpenBrowser(this, uri)
            startActivity(redirectIntent)
        }
        super.onCreate(savedInstanceState)
        finish()
    }

    fun getCallingIntentOpenBrowser(context: Context?, uri: Uri): Intent {
        val webUrl = WebViewHelper.getUrlQuery(uri) ?: TokopediaUrl.getInstance().WEB
        val ext = "true" == uri.getQueryParameter(KEY_EXT)
        val webUri = Uri.parse(webUrl)

        val destinationIntent = Intent(Intent.ACTION_VIEW)
        if (context == null) return destinationIntent.apply { data = webUri }

        if (ext) {
            val intent = WebViewHelper.actionViewIntent(context, webUri)
            if (intent != null) {
                return intent
            }
        }

        // hacky way: to avoid looping forever
        destinationIntent.data = Uri.parse(EXAMPLE_DOMAIN)

        val resolveInfos = context.packageManager.queryIntentActivities(destinationIntent, 0)
        // remove package tokopedia if any
        for (i in resolveInfos.indices.reversed()) {
            val resolveInfo = resolveInfos[i]
            val packageName = resolveInfo.activityInfo.packageName
            if (packageName == DeeplinkIntent.CUSTOMERAPP_PACKAGE || packageName == DeeplinkIntent.SELLERAPP_PACKAGE) {
                resolveInfos.removeAt(i)
            }
        }

        // return when the device has a browser app
        return if (resolveInfos.size >= 1) {
            // open chrome app by default
            val resolveInfo = resolveInfos.find { it.resolvePackageName == DeeplinkIntent.CHROME_PACKAGE }
                ?: resolveInfos.first()
            getBrowserIntent(resolveInfo, webUri)
        } else getSimpleWebViewActivityIntent(context, webUrl)
    }

    private fun getBrowserIntent(resolveInfo: ResolveInfo, webUri: Uri) =
        Intent().apply {
            setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name)
            data = webUri
        }

    private fun getSimpleWebViewActivityIntent(context: Context, webUrl: String) =
        // param external set to false (if any) is to
        // prevent infinite loop from webview -> browser -> webview
        Intent(context, BaseSimpleWebViewActivity::class.java).apply {
            putExtra(KEY_URL, webUrl.replaceFirst(PARAM_EXTERNAL_TRUE, PARAM_EXTERNAL_FALSE))
        }

    object DeeplinkIntent {

        const val SELLERAPP_PACKAGE = "com.tokopedia.sellerapp"
        const val CUSTOMERAPP_PACKAGE = "com.tokopedia.tkpd"
        const val CHROME_PACKAGE = "com.android.chrome"

    }
}