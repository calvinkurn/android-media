package com.tokopedia.sellerorder.detail.presentation.activity

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintManager
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import com.tokopedia.sellerorder.R
import com.tokopedia.webview.BaseSimpleWebViewActivity
import com.tokopedia.webview.KEY_URL


open class SomSeeInvoiceActivity : BaseSimpleWebViewActivity() {

    private var mWebView: WebView? = null

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.print_web_view, menu)
        return true
    }

    private fun doWebViewPrint() {
        val webView = WebView(this)
        webView.settings.javaScriptEnabled
        webView.settings.domStorageEnabled
        webView.settings.builtInZoomControls
        webView.settings.displayZoomControls
        val data = intent.extras?.getString(KEY_URL, "defaultKey")
        webView.loadUrl(data)
        mWebView = webView
        onPrintClicked(mWebView!!)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_print -> {
               doWebViewPrint()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @Suppress("DEPRECATION")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun onPrintClicked(webView: WebView){
        val printManager = getSystemService(Context.PRINT_SERVICE) as PrintManager
        val jobName = getString(R.string.app_name) + " Document"
        val printAdapter: PrintDocumentAdapter
        printAdapter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.createPrintDocumentAdapter(jobName)
        } else {
            webView.createPrintDocumentAdapter()
        }
        val builder = PrintAttributes.Builder()
        builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4)
        printManager.print(jobName, printAdapter, builder.build())
    }

}
