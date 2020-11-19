package com.tokopedia.flight.orderdetail.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.R
import com.tokopedia.flight.orderdetail.di.DaggerFlightOrderDetailComponent
import com.tokopedia.flight.orderdetail.di.FlightOrderDetailComponent
import com.tokopedia.flight.orderdetail.presentation.fragment.FlightOrderDetailBrowserFragment

class FlightOrderDetailBrowserActivity : BaseSimpleActivity(), HasComponent<FlightOrderDetailComponent> {

    private lateinit var title: String
    private lateinit var invoiceId: String
    private lateinit var htmlContent: String
    private lateinit var orderStatus: String

    private lateinit var orderDetailFragment: FlightOrderDetailBrowserFragment

    override fun getNewFragment(): Fragment? {
        orderDetailFragment = FlightOrderDetailBrowserFragment.getInstance(invoiceId, htmlContent, orderStatus)
        return orderDetailFragment
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_flight_order_detail_browser)

        title = intent.getStringExtra(EXTRA_TITLE) ?: ""
        invoiceId = intent.getStringExtra(EXTRA_INVOICE_ID) ?: ""
        htmlContent = intent.getStringExtra(EXTRA_HTML_CONTENT) ?: ""
        orderStatus = intent.getStringExtra(EXTRA_ORDER_STATUS) ?: ""

        savedInstanceState?.let {
            title = it.getString(EXTRA_TITLE, "")
            invoiceId = it.getString(EXTRA_INVOICE_ID, "")
            htmlContent = it.getString(EXTRA_HTML_CONTENT, "")
            orderStatus = it.getString(EXTRA_ORDER_STATUS, "")
        }

        super.onCreate(savedInstanceState)

        toolbar.title = title
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_HTML_CONTENT, htmlContent)
        outState.putString(EXTRA_TITLE, title)
        outState.putString(EXTRA_INVOICE_ID, invoiceId)
        outState.putString(EXTRA_ORDER_STATUS, orderStatus)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        menuInflater.inflate(R.menu.menu_flight_e_ticket, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_flight_download -> {
                if (::orderDetailFragment.isInitialized)
                    orderDetailFragment.printETicket()
                return true
            }
        }
        return false
    }

    override fun getComponent(): FlightOrderDetailComponent =
            DaggerFlightOrderDetailComponent.builder()
                    .flightComponent(FlightComponentInstance.getFlightComponent(application))
                    .build()

    companion object {
        private const val EXTRA_TITLE = "EXTRA_TITLE"
        private const val EXTRA_INVOICE_ID = "EXTRA_INVOICE_ID"
        private const val EXTRA_HTML_CONTENT = "EXTRA_HTML_CONTENT"
        private const val EXTRA_ORDER_STATUS = "EXTRA_ORDER_STATUS"

        fun getIntent(context: Context, title: String,
                      invoiceId: String,
                      htmlContent: String,
                      orderStatus: String): Intent =
                Intent(context, FlightOrderDetailBrowserActivity::class.java)
                        .putExtra(EXTRA_TITLE, title)
                        .putExtra(EXTRA_INVOICE_ID, invoiceId)
                        .putExtra(EXTRA_HTML_CONTENT, htmlContent)
                        .putExtra(EXTRA_ORDER_STATUS, orderStatus)
    }
}