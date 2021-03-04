package com.tokopedia.flight.orderdetail.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.orderdetail.di.DaggerFlightOrderDetailComponent
import com.tokopedia.flight.orderdetail.di.FlightOrderDetailComponent
import com.tokopedia.flight.orderdetail.presentation.fragment.FlightOrderDetailBrowserFragment
import com.tokopedia.webview.download.BaseDownloadHtmlActivity

class FlightOrderDetailBrowserActivity : BaseDownloadHtmlActivity(), HasComponent<FlightOrderDetailComponent> {

    private lateinit var orderStatus: String

    override fun getNewFragment(): Fragment =
            FlightOrderDetailBrowserFragment.getInstance(invoiceId, htmlContent, orderStatus).also {
                baseDownloadFragment = it
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            orderStatus = it.getString(EXTRA_ORDER_STATUS, "")
        }

        intent.getStringExtra(EXTRA_ORDER_STATUS)?.let {
            orderStatus = it
        }

        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(EXTRA_ORDER_STATUS, orderStatus)
    }

    override fun getComponent(): FlightOrderDetailComponent =
            DaggerFlightOrderDetailComponent.builder()
                    .flightComponent(FlightComponentInstance.getFlightComponent(application))
                    .build()

    companion object {
        private const val EXTRA_ORDER_STATUS = "EXTRA_ORDER_STATUS"

        fun getIntent(context: Context, title: String,
                      invoiceId: String,
                      htmlContent: String,
                      orderStatus: String): Intent =
                Intent(context, FlightOrderDetailBrowserActivity::class.java)
                        .putExtra(KEY_WEBVIEW_TITLE, title)
                        .putExtra(KEY_INVOICE_ID, invoiceId)
                        .putExtra(KEY_HTML_CONTENT, htmlContent)
                        .putExtra(EXTRA_ORDER_STATUS, orderStatus)
    }
}