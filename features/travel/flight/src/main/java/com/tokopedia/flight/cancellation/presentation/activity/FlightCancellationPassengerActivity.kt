package com.tokopedia.flight.cancellation.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.cancellation.di.DaggerFlightCancellationComponent
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent
import com.tokopedia.flight.cancellation.presentation.fragment.FlightCancellationPassengerFragment
import com.tokopedia.flight.orderlist.view.viewmodel.FlightCancellationJourney
import java.util.*

class FlightCancellationPassengerActivity : BaseSimpleActivity(), HasComponent<FlightCancellationComponent> {

    override fun getNewFragment(): Fragment {
        val flightCancellationJourney: List<FlightCancellationJourney> = intent.extras?.getParcelableArrayList(FlightCancellationPassengerFragment.EXTRA_CANCEL_JOURNEY)
                ?: arrayListOf()
        return FlightCancellationPassengerFragment.createInstance(
                intent.extras?.getString(FlightCancellationPassengerFragment.EXTRA_INVOICE_ID)
                        ?: "",
                flightCancellationJourney
        )
    }

    override fun getComponent(): FlightCancellationComponent =
            DaggerFlightCancellationComponent.builder()
                    .flightComponent(FlightComponentInstance.getFlightComponent(application))
                    .build()

    companion object {
        fun createIntent(context: Context,
                         invoiceId: String,
                         flightCancellationJourney: List<FlightCancellationJourney>)
                : Intent =
                Intent(context, FlightCancellationPassengerActivity::class.java)
                        .putExtra(FlightCancellationPassengerFragment.EXTRA_INVOICE_ID, invoiceId)
                        .putParcelableArrayListExtra(FlightCancellationPassengerFragment.EXTRA_CANCEL_JOURNEY, flightCancellationJourney as ArrayList<out Parcelable>)
    }
}
