package com.tokopedia.flight.orderdetail.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.orderdetail.di.DaggerFlightOrderDetailComponent
import com.tokopedia.flight.orderdetail.di.FlightOrderDetailComponent
import com.tokopedia.flight.orderdetail.presentation.fragment.FlightOrderDetailFragment
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class FlightOrderDetailActivity : BaseSimpleActivity(), HasComponent<FlightOrderDetailComponent> {

    @Inject
    lateinit var userSession: UserSessionInterface

    private var invoiceId: String = ""
    private var isCancellation: String = "0"
    private var isRequestCancellation: Boolean = false

    override fun getNewFragment(): Fragment? =
            FlightOrderDetailFragment.createInstance(
                    invoiceId,
                    isCancellation == "1",
                    isRequestCancellation
            )

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)

        if (userSession.isLoggedIn) {
            intent.data?.let {
                if (it.lastPathSegment.equals(PATH_CANCELLATION)) {
                    val tempInvoiceId = it.getQueryParameter(EXTRA_INVOICE_ID)
                    if (tempInvoiceId != null && tempInvoiceId.isNotEmpty()) {
                        invoiceId = tempInvoiceId
                        isRequestCancellation = true
                    } else {
                        finish()
                    }
                } else {
                    invoiceId = it.lastPathSegment ?: ""
                    val tempIsCancellation = it.getQueryParameter(EXTRA_IS_CANCELLATION)
                    if (tempIsCancellation != null && tempIsCancellation.isNotEmpty()) {
                        isCancellation = tempIsCancellation
                    }
                }
            }
        } else {
            startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN)
        }

        if (invoiceId.isEmpty()) finish()

        super.onCreate(savedInstanceState)
    }

    override fun getComponent(): FlightOrderDetailComponent =
            DaggerFlightOrderDetailComponent.builder()
                    .flightComponent(FlightComponentInstance.getFlightComponent(application))
                    .build()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_LOGIN) {
            if (userSession.isLoggedIn) recreate()
            else finish()
        }
    }

    companion object {
        private const val EXTRA_INVOICE_ID = "iv"
        private const val PATH_CANCELLATION = "cancellation"
        private const val EXTRA_IS_CANCELLATION = "is_cancellation"

        private const val REQUEST_CODE_LOGIN = 6
    }

}