package com.tokopedia.sellerorder.detail.presentation.activity

import android.annotation.TargetApi
import android.content.ActivityNotFoundException
import android.os.Build
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import androidx.core.content.ContextCompat
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.analytics.SomAnalytics
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_ORDER_CODE
import com.tokopedia.webview.BaseSimpleWebViewActivity
import com.tokopedia.webview.KEY_URL


open class SomSeeInvoiceActivity : BaseSimpleWebViewActivity() {
    private var orderCode: String = ""

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent.extras
        if (bundle != null) {
            orderCode = bundle.get(PARAM_ORDER_CODE).toString()
        }
    }

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
        val jobName = getString(R.string.app_name) + " Document"
        val printAdapter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.createPrintDocumentAdapter(jobName)
        } else {
            webView.createPrintDocumentAdapter()
        }
        val builder = PrintAttributes.Builder()
        builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4)
        try {
            printManager?.print(jobName, printAdapter, builder.build())
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
        SomAnalytics.eventClickButtonDownloadInvoice(orderCode)
    }

}
