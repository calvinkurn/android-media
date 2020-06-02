package com.tokopedia.buyerorder.detail.view.activity

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintManager
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.detail.data.Invoice
import com.tokopedia.buyerorder.detail.data.Status
import com.tokopedia.buyerorder.detail.di.DaggerOrderDetailsComponent
import com.tokopedia.buyerorder.detail.view.OrderListAnalytics
import com.tokopedia.webview.BaseSimpleWebViewActivity
import com.tokopedia.webview.KEY_TITLE
import com.tokopedia.webview.KEY_URL
import javax.inject.Inject

class SeeInvoiceActivity : BaseSimpleWebViewActivity() {

    var orderListAnalytics: OrderListAnalytics? = null
        @Inject set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerOrderDetailsComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build().inject(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_unduh_invoice, menu)
        return true
    }

    private fun doWebPrint(){
        val webView = WebView(this)
        webView.settings.javaScriptEnabled
        webView.settings.domStorageEnabled
        webView.settings.builtInZoomControls
        webView.settings.displayZoomControls
        val data = intent?.extras?.getString(KEY_URL, "defaultKey")
        webView.loadUrl(data)
        onPrintClicked(webView)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_download -> {
                orderListAnalytics?.sendDownloadEventData(intent.getStringExtra(STATUS) ?: "")
                doWebPrint()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @Suppress("DEPRECATION")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun onPrintClicked(webView: WebView?) {
        webView?.let {
            val printManager = getSystemService(Context.PRINT_SERVICE) as PrintManager
            val jobName = getString(R.string.app_name) + " Document"
            val printAdapter: PrintDocumentAdapter
            printAdapter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                it.createPrintDocumentAdapter(jobName)
            } else {
                it.createPrintDocumentAdapter()
            }

            val prinAttr = PrintAttributes.Builder()
                    .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                    .build()
            printManager.print(jobName, printAdapter, prinAttr)
        }
    }

    companion object {
        const val STATUS = "status"
        @JvmStatic
        fun newInstance(context: Context, status: Status, invoice: Invoice, title: String): Intent =
                Intent(context, SeeInvoiceActivity::class.java).apply {
                    putExtra(STATUS, status.status())
                    putExtra(KEY_URL, invoice.invoiceUrl())
                    putExtra(KEY_TITLE, title)
                }
    }

}