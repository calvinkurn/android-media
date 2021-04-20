package com.tokopedia.flight.orderdetail.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.R
import com.tokopedia.flight.orderdetail.di.DaggerFlightOrderDetailComponent
import com.tokopedia.flight.orderdetail.di.FlightOrderDetailComponent
import com.tokopedia.flight.orderdetail.presentation.fragment.FlightOrderDetailWebCheckInFragment

class FlightOrderDetailWebCheckInActivity : BaseSimpleActivity(), HasComponent<FlightOrderDetailComponent> {

    private var invoiceId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        invoiceId = intent.getStringExtra(EXTRA_INVOICE_ID) ?: ""

        super.onCreate(savedInstanceState)
        toolbar?.title = getString(R.string.flight_order_detail_check_in_label)
    }

    override fun getNewFragment(): Fragment = FlightOrderDetailWebCheckInFragment.getInstance(invoiceId)

    override fun getComponent(): FlightOrderDetailComponent =
            DaggerFlightOrderDetailComponent.builder()
                    .flightComponent(FlightComponentInstance.getFlightComponent(application))
                    .build()

    companion object {
        private const val EXTRA_INVOICE_ID = "EXTRA_INVOICE_ID"

        fun getIntent(context: Context, invoiceId: String): Intent =
                Intent(context, FlightOrderDetailWebCheckInActivity::class.java)
                        .putExtra(EXTRA_INVOICE_ID, invoiceId)
    }
}