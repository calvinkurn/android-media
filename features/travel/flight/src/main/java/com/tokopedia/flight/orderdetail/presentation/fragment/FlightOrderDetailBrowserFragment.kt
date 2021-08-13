package com.tokopedia.flight.orderdetail.presentation.fragment

import android.os.Bundle
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.orderdetail.di.FlightOrderDetailComponent
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.webview.download.BaseDownloadHtmlActivity
import com.tokopedia.webview.download.BaseDownloadHtmlFragment
import javax.inject.Inject


/**
 * @author by furqan on 16/11/2020
 */
class FlightOrderDetailBrowserFragment : BaseDownloadHtmlFragment() {

    @Inject
    lateinit var flightAnalytics: FlightAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    private lateinit var orderStatus: String

    override fun onCreate(savedInstanceState: Bundle?) {

        savedInstanceState?.let {
            if (it.containsKey(EXTRA_ORDER_STATUS))
                orderStatus = it.getString(EXTRA_ORDER_STATUS) ?: ""
        }

        arguments?.let { args ->
            args.getString(EXTRA_ORDER_STATUS)?.let {
                orderStatus = it
            }
        }

        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(EXTRA_ORDER_STATUS, orderStatus)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(FlightOrderDetailComponent::class.java).inject(this)
    }

    override fun sendDownloadTrack() {
        flightAnalytics.eventDownloadETicketOrderDetail(
                "$orderStatus - $invoiceId",
                userSession.userId
        )
    }

    companion object {
        private const val EXTRA_ORDER_STATUS = "EXTRA_ORDER_STATUS"

        fun getInstance(invoiceId: String, htmlContent: String, orderStatus: String)
                : FlightOrderDetailBrowserFragment =
                FlightOrderDetailBrowserFragment().also {
                    it.arguments = Bundle().apply {
                        putString(BaseDownloadHtmlActivity.KEY_INVOICE_ID, invoiceId)
                        putString(BaseDownloadHtmlActivity.KEY_HTML_CONTENT, htmlContent)
                        putString(EXTRA_ORDER_STATUS, orderStatus)
                    }
                }
    }
}