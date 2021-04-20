package com.tokopedia.buyerorder.detail.view.activity

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
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.detail.data.Invoice
import com.tokopedia.buyerorder.detail.data.Status
import com.tokopedia.buyerorder.detail.di.DaggerOrderDetailsComponent
import com.tokopedia.buyerorder.detail.view.OrderDetailRechargeDownloadWebviewAnalytics
import com.tokopedia.buyerorder.detail.view.OrderListAnalytics
import com.tokopedia.buyerorder.list.data.OrderCategory
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.webview.BaseSimpleWebViewActivity
import com.tokopedia.webview.KEY_TITLE
import com.tokopedia.webview.KEY_URL
import javax.inject.Inject

class SeeInvoiceActivity : BaseSimpleWebViewActivity() {

    var orderListAnalytics: OrderListAnalytics? = null
        @Inject set

    @Inject
    lateinit var rechargeOrderAnalytics: OrderDetailRechargeDownloadWebviewAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerOrderDetailsComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build().inject(this)

        if (intent.hasExtra(KEY_ORDER_CATEGORY) && intent.getStringExtra(KEY_ORDER_CATEGORY) == OrderCategory.DIGITAL) {
            rechargeOrderAnalytics.rechargeDownloadOpenScreen(
                    userSession.isLoggedIn,
                    intent.getStringExtra(KEY_CATEGORY_NAME) ?: "",
                    intent.getStringExtra(KEY_PRODUCT_NAME) ?: "",
                    userSession.userId
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_unduh_invoice, menu)
        return true
    }

    private fun doWebPrint() {
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
        return when (item.itemId) {
            R.id.action_download -> {
                if (intent.hasExtra(KEY_ORDER_CATEGORY) && intent.getStringExtra(KEY_ORDER_CATEGORY) == OrderCategory.DIGITAL) {
                    rechargeOrderAnalytics.rechargeInvoiceClickDownload(
                            intent.getStringExtra(KEY_CATEGORY_NAME) ?: "",
                            intent.getStringExtra(KEY_PRODUCT_NAME) ?: "",
                            userSession.userId
                    )
                } else {
                    orderListAnalytics?.sendDownloadEventData(intent.getStringExtra(STATUS) ?: "")
                }
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
            val prinAttr = PrintAttributes.Builder()
                    .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                    .build()
            try {
                printManager?.print(jobName, printAdapter, prinAttr)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        const val KEY_ORDER_CATEGORY = "KEY_ORDER_CATEGORY"
        const val KEY_CATEGORY_NAME = "KEY_CATEGORY_NAME"
        const val KEY_PRODUCT_NAME = "KEY_PRODUCT_NAME"
        const val KEY_ORDER_ID = "KEY_ORDER_ID"
        const val STATUS = "status"
        const val INVOICE_REF_NUM = "invoice_ref_num"
        const val BOUGHT_DATE = "bought_date"

        @JvmStatic
        fun newInstance(context: Context, status: Status, invoice: Invoice,
                        invoiceRefNum: String, boughtDate: String, title: String,
                        orderCategory: String): Intent =
                Intent(context, SeeInvoiceActivity::class.java).apply {
                    putExtra(STATUS, status.status())
                    putExtra(KEY_URL, invoice.invoiceUrl())
                    putExtra(KEY_TITLE, title)
                    putExtra(INVOICE_REF_NUM, invoiceRefNum)
                    putExtra(BOUGHT_DATE, boughtDate)
                    putExtra(KEY_ORDER_CATEGORY, orderCategory)
                }

        @JvmStatic
        fun newInstance(context: Context, categoryName: String, productName: String,
                        orderId: String, invoiceUrl: String, invoiceRefNum: String,
                        title: String, orderCategory: String): Intent =
                Intent(context, SeeInvoiceActivity::class.java).apply {
                    putExtra(KEY_URL, invoiceUrl)
                    putExtra(KEY_TITLE, title)
                    putExtra(INVOICE_REF_NUM, invoiceRefNum)
                    putExtra(KEY_CATEGORY_NAME, categoryName)
                    putExtra(KEY_PRODUCT_NAME, productName)
                    putExtra(KEY_ORDER_ID, orderId)
                    putExtra(KEY_ORDER_CATEGORY, orderCategory)
                }
    }

}