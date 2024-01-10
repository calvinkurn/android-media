package com.tokopedia.sellerorder.detail.presentation.activity

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintJob
import android.print.PrintManager
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import androidx.core.content.ContextCompat
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.analytics.SomAnalytics
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_INVOICE
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_ORDER_CODE
import com.tokopedia.webview.BaseSimpleWebViewActivity
import com.tokopedia.webview.BaseWebViewFragment

open class SomSeeInvoiceActivity : BaseSimpleWebViewActivity() {
    private var orderCode: String = ""
    private var invoice: String = ""
    private var printJob: PrintJob? = null

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent.extras
        if (bundle != null) {
            orderCode = bundle.get(PARAM_ORDER_CODE).toString()
            invoice = bundle.get(PARAM_INVOICE).toString()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.print_web_view, menu)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        removeWebView()
    }

    private fun doWebViewPrint() {
        val fragment = fragment
        if (fragment is BaseWebViewFragment) {
            onPrintClicked(fragment.webView)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_print -> {
                doWebViewPrint()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @Suppress("DEPRECATION")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun onPrintClicked(webView: WebView) {
        if (printJob == null || printJob?.isCompleted == true || printJob?.isFailed == true || printJob?.isCancelled == true) {
            val printManager = ContextCompat.getSystemService(this, PrintManager::class.java)

            var lastNoInvoice = ""
            if (invoice.isNotEmpty()) {
                val splitInvoice = invoice.split("/")
                if (splitInvoice.isNotEmpty()) {
                    lastNoInvoice = splitInvoice[splitInvoice.size - 1]
                }
            }
            val jobName = "Invoice $lastNoInvoice"

            val printAdapter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webView.createPrintDocumentAdapter(jobName)
            } else {
                webView.createPrintDocumentAdapter()
            }
            val builder = PrintAttributes.Builder()
            builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4)
            try {
                printJob = printManager?.print(jobName, printAdapter, builder.build())
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            SomAnalytics.eventClickButtonDownloadInvoice(orderCode)
        }
    }

    private fun removeWebView() {
        try {
            (fragment as? BaseWebViewFragment)?.run {
                webView?.clearHistory()
                webView?.destroy()
                webView = null
            }
        } catch (ignored: Exception) {
            // NO-OP
        }
    }
}
