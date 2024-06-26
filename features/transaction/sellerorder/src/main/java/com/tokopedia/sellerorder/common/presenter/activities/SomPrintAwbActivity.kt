package com.tokopedia.sellerorder.common.presenter.activities

import android.content.ActivityNotFoundException
import android.print.PrintAttributes
import android.print.PrintJob
import android.print.PrintManager
import android.view.Menu
import android.webkit.JavascriptInterface
import androidx.core.content.ContextCompat
import com.tokopedia.sellerorder.R
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.webview.BaseSimpleWebViewActivity
import com.tokopedia.webview.BaseWebViewFragment
import com.tokopedia.webview.TkpdWebView

class SomPrintAwbActivity : BaseSimpleWebViewActivity() {

    companion object {
        private const val JAVASCRIPT_INTERFACE_NAME = "Android"
        private const val PRINT_JOB_NAME = "Shipping Label"

        private val MEDIA_SIZES = mapOf<String, PrintAttributes.MediaSize>(
            "ISO_A0" to PrintAttributes.MediaSize.ISO_A0,
            "ISO_A1" to PrintAttributes.MediaSize.ISO_A1,
            "ISO_A2" to PrintAttributes.MediaSize.ISO_A2,
            "ISO_A3" to PrintAttributes.MediaSize.ISO_A3,
            "ISO_A4" to PrintAttributes.MediaSize.ISO_A4,
            "ISO_A5" to PrintAttributes.MediaSize.ISO_A5,
            "ISO_A6" to PrintAttributes.MediaSize.ISO_A6,
            "ISO_A7" to PrintAttributes.MediaSize.ISO_A7,
            "ISO_A8" to PrintAttributes.MediaSize.ISO_A8,
            "ISO_A9" to PrintAttributes.MediaSize.ISO_A9,
            "ISO_A10" to PrintAttributes.MediaSize.ISO_A10
        )
    }

    private var printJob: PrintJob? = null
    private var webView: TkpdWebView? = null

    override fun onDestroy() {
        super.onDestroy()
        removeWebView()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        configureWebView()
        val result = super.onPrepareOptionsMenu(menu)
        menu?.clear()
        return result
    }

    private fun configureWebView() {
        (fragment as? BaseWebViewFragment)?.webView?.let {
            it.addJavascriptInterface(SomPrintAwbJavascriptInterface(), JAVASCRIPT_INTERFACE_NAME)
            it.settings.builtInZoomControls = true
            it.settings.loadWithOverviewMode = true
            it.settings.textZoom = 100
            webView = it
        }
    }

    private fun doPrint(mediaSizeId: String = "") {
        runOnUiThread {
            try {
                if (printJob == null || printJob?.isCompleted == true || printJob?.isFailed == true || printJob?.isCancelled == true) {
                    webView?.run {
                        val printManager = ContextCompat.getSystemService(
                            context,
                            PrintManager::class.java
                        )
                        val printAdapter = createPrintDocumentAdapter(PRINT_JOB_NAME)
                        val builder = PrintAttributes.Builder()
                        if (mediaSizeId.isNotEmpty()) {
                            builder.setMediaSize(mediaSizeId.toPrintAttributeMediaSize())
                        } else {
                            builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                        }
                        if (!isFinishing) {
                            printJob = printManager?.print(
                                PRINT_JOB_NAME,
                                printAdapter,
                                builder.build()
                            )
                        }
                    }
                }
            } catch (t: Throwable) {
                if (t is ActivityNotFoundException) {
                    showToaster(getString(R.string.som_print_awb_error_message_unsupported_operation))
                } else {
                    showToaster(getString(R.string.som_print_awb_error_message))
                }
            }
        }
    }

    private fun String.toPrintAttributeMediaSize(): PrintAttributes.MediaSize {
        return MEDIA_SIZES[this] ?: PrintAttributes.MediaSize.UNKNOWN_PORTRAIT
    }

    private fun showToaster(message: String) {
        webView?.let {
            Toaster.build(it, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
        }
    }

    private fun removeWebView() {
        try {
            webView?.clearHistory()
            webView?.destroy()
            webView = null
        } catch (ignored: Exception) {
            // NO-OP
        }
    }

    private inner class SomPrintAwbJavascriptInterface {
        @JavascriptInterface
        fun printAwb() {
            doPrint()
        }

        @JavascriptInterface
        fun printAwb(mediaSizeId: String) {
            doPrint(mediaSizeId = mediaSizeId)
        }
    }
}
