package com.tokopedia.flight.cancellationdetail.presentation.activity

import android.content.Context
import android.content.Intent
import android.view.Menu
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.flight.cancellationdetail.presentation.fragment.FlightOrderCancellationListFragment
import com.tokopedia.flight.common.view.BaseFlightActivity
import com.tokopedia.flight.orderdetail.di.DaggerFlightOrderDetailComponent
import com.tokopedia.flight.orderdetail.di.FlightOrderDetailComponent

class FlightOrderCancellationListActivity : BaseFlightActivity(),
        HasComponent<FlightOrderDetailComponent> {

    override fun getNewFragment(): Fragment? =
            FlightOrderCancellationListFragment.getInstance(
                    intent.getStringExtra(EXTRA_INVOICE_ID) ?: ""
            )

    override fun getComponent(): FlightOrderDetailComponent =
            DaggerFlightOrderDetailComponent.builder()
                    .flightComponent(flightComponent)
                    .build()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean = true

    companion object {
        const val EXTRA_INVOICE_ID = "EXTRA_INVOICE_ID"

        fun createIntent(context: Context, invoiceId: String): Intent =
                Intent(context, FlightOrderCancellationListActivity::class.java)
                        .putExtra(EXTRA_INVOICE_ID, invoiceId)
    }
}