package com.tokopedia.flight.orderdetail.presentation.fragment

import android.os.Bundle
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.flight.orderdetail.di.FlightOrderDetailComponent

/**
 * @author by furqan on 19/10/2020
 */
class FlightOrderDetailFragment : BaseDaggerFragment() {
    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(FlightOrderDetailComponent::class.java).inject(this)
    }

    companion object {

        private const val EXTRA_INVOICE_ID = "EXTRA_INVOICE_ID"
        private const val EXTRA_IS_CANCELLATION = "EXTRA_IS_CANCELLATION"
        private const val EXTRA_REQUEST_CANCEL = "EXTRA_REQUEST_CANCEL"

        fun createInstance(invoiceId: String,
                           isCancellation: Boolean,
                           isRequestCancellation: Boolean): FlightOrderDetailFragment =
                FlightOrderDetailFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_INVOICE_ID, invoiceId)
                        putBoolean(EXTRA_IS_CANCELLATION, isCancellation)
                        putBoolean(EXTRA_REQUEST_CANCEL, isRequestCancellation)
                    }
                }
    }
}