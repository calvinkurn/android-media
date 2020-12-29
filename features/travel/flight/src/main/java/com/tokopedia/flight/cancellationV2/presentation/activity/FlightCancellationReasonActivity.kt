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
import com.tokopedia.flight.cancellationV2.presentation.fragment.FlightCancellationReasonFragment
import com.tokopedia.flight.cancellationV2.presentation.model.FlightCancellationWrapperModel

class FlightCancellationReasonActivity : BaseSimpleActivity(),
        HasComponent<FlightCancellationComponent> {

    private lateinit var cancellationWrapperModel: FlightCancellationWrapperModel

    override fun getNewFragment(): Fragment =
            FlightCancellationReasonFragment.newInstance(cancellationWrapperModel)

    override fun getComponent(): FlightCancellationComponent =
            DaggerFlightCancellationComponent.builder()
                    .flightComponent(FlightComponentInstance.getFlightComponent(application))
                    .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        cancellationWrapperModel = intent.getParcelableExtra(EXTRA_CANCELLATION_MODEL)
        super.onCreate(savedInstanceState)
        setupToolbar()
    }

    private fun setupToolbar() {
        toolbar.contentInsetStartWithNavigation = 0
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N200))
        val title = getString(com.tokopedia.flight.R.string.activity_label_flight_cancellation)
        val subtitle = String.format(
                getString(com.tokopedia.flight.R.string.flight_cancellation_subtitle_order_id),
                cancellationWrapperModel.invoiceId
        )
        updateTitle(title, subtitle)
    }

    companion object {
        const val EXTRA_CANCELLATION_MODEL = "EXTRA_CANCELLATION_VIEW_MODEL"

        fun getCallingIntent(context: Context, cancellationWrapperModel: FlightCancellationWrapperModel): Intent =
                Intent(context, FlightCancellationReasonActivity::class.java)
                        .putExtra(EXTRA_CANCELLATION_MODEL, cancellationWrapperModel)
    }
}
