package com.tokopedia.buyerorder.recharge_download.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.buyerorder.recharge_download.di.DaggerOrderDetailRechargeDownloadWebviewComponent
import com.tokopedia.buyerorder.recharge_download.di.OrderDetailRechargeDownloadWebviewComponent
import com.tokopedia.buyerorder.recharge_download.presentation.fragment.OrderDetailRechargeDownloadWebviewFragment
import com.tokopedia.webview.download.BaseDownloadHtmlActivity

class OrderDetailRechargeDownloadWebviewActivity : BaseDownloadHtmlActivity(), HasComponent<OrderDetailRechargeDownloadWebviewComponent> {

    override fun getNewFragment(): Fragment =
            OrderDetailRechargeDownloadWebviewFragment.getInstance(invoiceId, htmlContent).also {
                baseDownloadFragment = it
            }

    override fun getComponent(): OrderDetailRechargeDownloadWebviewComponent =
            DaggerOrderDetailRechargeDownloadWebviewComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .build()

    companion object {

        fun getIntent(context: Context, title: String,
                      invoiceId: String,
                      htmlContent: String): Intent =
                Intent(context, OrderDetailRechargeDownloadWebviewActivity::class.java)
                        .putExtra(KEY_WEBVIEW_TITLE, title)
                        .putExtra(KEY_INVOICE_ID, invoiceId)
                        .putExtra(KEY_HTML_CONTENT, htmlContent)

    }

}