package com.tokopedia.hotel.orderdetail.presentation.activity

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import androidx.core.content.ContextCompat
import com.tokopedia.hotel.R
import com.tokopedia.webview.BaseSimpleWebViewActivity
import com.tokopedia.webview.KEY_URL
import com.tokopedia.webview.TkpdWebView

class SeeInvoiceActivity: BaseSimpleWebViewActivity() {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        initWebSettingsConfig()
        val inflater = menuInflater
        inflater.inflate(R.menu.hotel_invoice_menu, menu)
        return true
    }

    private fun initWebSettingsConfig() {
        val webView = findViewById<TkpdWebView>(com.tokopedia.webview.R.id.webview)
        webView.settings.run {
            builtInZoomControls = true
            displayZoomControls = false
            useWideViewPort = true
            loadWithOverviewMode = true
        }
        webView.setInitialScale(1)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_download -> {
                onDownloadClick()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @Suppress("DEPRECATION")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun onDownloadClick() {
        val webView = findViewById<TkpdWebView>(com.tokopedia.webview.R.id.webview)
        webView?.let {
            val printManager = ContextCompat.getSystemService(this, PrintManager::class.java)

            var lastNoInvoice = ""
            val invoiceRefNum = intent?.getStringExtra(INVOICE_REF_NUM) ?: ""
            if (invoiceRefNum.isNotEmpty()) {
                val splitInvoice = invoiceRefNum.split("/")
                if (splitInvoice.isNotEmpty()) {
                    lastNoInvoice = splitInvoice[splitInvoice.size - 1]
                }
            }
            val jobName = "Invoice $lastNoInvoice"

            val printAdapter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                it.createPrintDocumentAdapter(jobName)
            } else {
                it.createPrintDocumentAdapter()
            }
            val printAttr = PrintAttributes.Builder()
                    .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                    .build()
            try {
                printManager?.print(jobName, printAdapter, printAttr)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        const val INVOICE_REF_NUM = "invoice_ref_num"
        fun newInstance(context: Context, invoiceUrl: String, invoiceRefNum: String) =
                Intent(context, SeeInvoiceActivity::class.java).apply {
                    putExtra(KEY_URL, invoiceUrl)
                    putExtra(INVOICE_REF_NUM, invoiceRefNum)
                }
    }
}