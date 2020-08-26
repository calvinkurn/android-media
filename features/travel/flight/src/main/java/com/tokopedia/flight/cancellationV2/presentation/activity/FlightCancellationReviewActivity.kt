package com.tokopedia.flight.cancellationV2.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellationV2.di.DaggerFlightCancellationComponent
import com.tokopedia.flight.cancellationV2.di.FlightCancellationComponent
import com.tokopedia.flight.cancellationV2.presentation.fragment.FlightCancellationReviewFragment
import com.tokopedia.flight.cancellationV2.presentation.model.FlightCancellationWrapperModel

class FlightCancellationReviewActivity : BaseSimpleActivity(),
        HasComponent<FlightCancellationComponent> {

    private lateinit var invoiceId: String
    private lateinit var cancellationWrapperModel: FlightCancellationWrapperModel

    override fun onCreate(savedInstanceState: Bundle?) {
        invoiceId = intent.getStringExtra(EXTRA_INVOICE_ID)
        cancellationWrapperModel = intent.getParcelableExtra(EXTRA_CANCEL_WRAPPER)

        super.onCreate(savedInstanceState)

        setupToolbar()
    }

    override fun getNewFragment(): Fragment =
            FlightCancellationReviewFragment.createInstance(invoiceId, cancellationWrapperModel)

    override fun getComponent(): FlightCancellationComponent =
            DaggerFlightCancellationComponent.builder()
                    .flightComponent(FlightComponentInstance.getFlightComponent(application))
                    .build()

    private fun setupToolbar() {
        toolbar.contentInsetStartWithNavigation = 0
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, R.color.flight_subtitle_text_color))
        val title = getString(R.string.activity_label_flight_review_cancellation)
        val subtitle = String.format(
                getString(R.string.flight_cancellation_subtitle_order_id),
                invoiceId
        )
        updateTitle(title, subtitle)
    }

    companion object {
        const val EXTRA_INVOICE_ID = "EXTRA_INVOICE_ID"
        const val EXTRA_CANCEL_WRAPPER = "EXTRA_CANCEL_WRAPPER"

        fun getCallingIntent(context: Context,
                             invoiceId: String,
                             cancellationWrapperModel: FlightCancellationWrapperModel)
                : Intent =
                Intent(context, FlightCancellationReviewActivity::class.java)
                        .putExtra(EXTRA_INVOICE_ID, invoiceId)
                        .putExtra(EXTRA_CANCEL_WRAPPER, cancellationWrapperModel)
    }
}
