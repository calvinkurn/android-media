package com.tokopedia.sellerorder.common.presenter.activities

import android.annotation.SuppressLint
import android.os.Build
import android.print.PrintAttributes
import android.print.PrintManager
import android.view.Menu
import android.webkit.JavascriptInterface
import androidx.core.content.ContextCompat
import com.tokopedia.webview.BaseSimpleWebViewActivity
import com.tokopedia.webview.BaseWebViewFragment
import com.tokopedia.webview.TkpdWebView

@SuppressLint("LogNotTimber")
class SomPrintAwbActivity : BaseSimpleWebViewActivity() {

    companion object {
        private const val JAVASCRIPT_INTERFACE_NAME = "Android"
        private const val PRINT_JOB_NAME = "Shipping Label"
    }

    private var webView: TkpdWebView? = null

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        configureWebView()
        val result = super.onPrepareOptionsMenu(menu)
        menu?.clear()
        return result
    }

    private fun configureWebView() {
        (fragment as BaseWebViewFragment).webView?.let {
            it.addJavascriptInterface(JavaScriptInterface(this), JAVASCRIPT_INTERFACE_NAME)
            it.settings.builtInZoomControls = true
            it.settings.loadWithOverviewMode = true
            webView = it
        }
    }

    private fun doPrint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView?.run {
                runOnUiThread {
                    val printManager = ContextCompat.getSystemService(context, PrintManager::class.java)
                    val printAdapter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        createPrintDocumentAdapter(PRINT_JOB_NAME)
                    } else {
                        createPrintDocumentAdapter()
                    }
                    val builder = PrintAttributes.Builder()
                    builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                    try {
                        printManager?.print(PRINT_JOB_NAME, printAdapter, builder.build())
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    class JavaScriptInterface(val activity: SomPrintAwbActivity) {
        @JavascriptInterface
        fun printAwb() {
            activity.doPrint()
        }
    }
}