package com.tokopedia.saldodetails.transactionDetailPages.invoice

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import androidx.core.content.ContextCompat
import com.tokopedia.saldodetails.R
import com.tokopedia.webview.BaseSimpleWebViewActivity
import com.tokopedia.webview.KEY_URL

class InvoiceDetailActivity : BaseSimpleWebViewActivity() {
    private var invoice: String = ""

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent.extras
        if (bundle != null) {
            invoice = bundle.get(PARAM_INVOICE_NUMBER).toString()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.saldo_print_web_view, menu)
        return true
    }

    private fun doWebViewPrint() {
        val webView = WebView(this)
        webView.settings.javaScriptEnabled
        webView.settings.domStorageEnabled
        webView.settings.builtInZoomControls
        webView.settings.displayZoomControls
        val data = intent.extras?.getString(KEY_URL, "defaultKey") ?: ""
        webView.loadUrl(data)

        onPrintClicked(webView)
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
        val printManager = ContextCompat.getSystemService(this, PrintManager::class.java)

        var lastNoInvoice = ""
        if (invoice.isNotEmpty()) {
            val splitInvoice = invoice.split("/")
            if (splitInvoice.isNotEmpty()) {
                lastNoInvoice = splitInvoice[splitInvoice.size-1]
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
            printManager?.print(jobName, printAdapter, builder.build())
        } catch (e: Throwable) {
            //e.printStackTrace()
        }
    }

    companion object {
        const val PARAM_INVOICE_NUMBER = "invoice"
    }

}
