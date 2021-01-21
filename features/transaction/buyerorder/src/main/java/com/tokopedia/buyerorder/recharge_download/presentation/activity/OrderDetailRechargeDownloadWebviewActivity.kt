package com.tokopedia.buyerorder.recharge_download.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.buyerorder.recharge_download.di.DaggerOrderDetailRechargeDownloadWebviewComponent
import com.tokopedia.buyerorder.recharge_download.di.OrderDetailRechargeDownloadWebviewComponent
import com.tokopedia.buyerorder.recharge_download.presentation.fragment.OrderDetailRechargeDownloadWebviewFragment
import com.tokopedia.webview.download.BaseDownloadHtmlActivity

class OrderDetailRechargeDownloadWebviewActivity : BaseDownloadHtmlActivity(), HasComponent<OrderDetailRechargeDownloadWebviewComponent> {

    private var categoryName: String = ""
    private var productName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        savedInstanceState?.let {
            categoryName = it.getString(KEY_CATEGORY_NAME, "")
            productName = it.getString(KEY_PRODUCT_NAME, "")
        }

        intent.getStringExtra(KEY_CATEGORY_NAME)?.let {
            if (it.isNotEmpty()) categoryName = it
        }
        intent.getStringExtra(KEY_PRODUCT_NAME)?.let {
            if (it.isNotEmpty()) productName = it
        }

        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(KEY_CATEGORY_NAME, categoryName)
        outState.putString(KEY_PRODUCT_NAME, productName)
    }

    override fun getNewFragment(): Fragment =
            OrderDetailRechargeDownloadWebviewFragment.getInstance(invoiceId, htmlContent, categoryName, productName).also {
                baseDownloadFragment = it
            }

    override fun getComponent(): OrderDetailRechargeDownloadWebviewComponent =
            DaggerOrderDetailRechargeDownloadWebviewComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .build()

    companion object {

        const val KEY_CATEGORY_NAME = "KEY_CATEGORY_NAME"
        const val KEY_PRODUCT_NAME = "KEY_PRODUCT_NAME"

        fun getIntent(context: Context, title: String,
                      invoiceId: String,
                      htmlContent: String,
                      categoryName: String,
                      productName: String): Intent =
                Intent(context, OrderDetailRechargeDownloadWebviewActivity::class.java)
                        .putExtra(KEY_WEBVIEW_TITLE, title)
                        .putExtra(KEY_INVOICE_ID, invoiceId)
                        .putExtra(KEY_HTML_CONTENT, htmlContent)
                        .putExtra(KEY_CATEGORY_NAME, categoryName)
                        .putExtra(KEY_PRODUCT_NAME, productName)

    }

}