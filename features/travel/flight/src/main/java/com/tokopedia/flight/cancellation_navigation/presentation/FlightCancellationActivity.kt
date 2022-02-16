package com.tokopedia.flight.cancellation_navigation.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellation.data.FlightCancellationResponseEntity
import com.tokopedia.flight.cancellation.di.DaggerFlightCancellationComponent
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent
import com.tokopedia.flight.cancellation.presentation.fragment.FlightCancellationPassengerFragment
import com.tokopedia.flight.cancellation_navigation.presentation.fragment.FlightCancellationPassengerFragment.Companion.EXTRA_CANCEL_JOURNEY
import com.tokopedia.flight.cancellation_navigation.presentation.fragment.FlightCancellationPassengerFragment.Companion.EXTRA_INVOICE_ID
import java.util.*

class FlightCancellationActivity : BaseSimpleActivity(), HasComponent<FlightCancellationComponent> {

    override fun getNewFragment(): Fragment? = null

    override fun getLayoutRes(): Int = R.layout.activity_flight_cancellation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container_cancellation_navigation) as NavHostFragment
        val argBundle = Bundle().apply {
            val flightCancellationJourney: List<FlightCancellationResponseEntity> = intent.extras?.getParcelableArrayList(FlightCancellationPassengerFragment.EXTRA_CANCEL_JOURNEY)
                    ?: arrayListOf()
            val invoiceId = intent.extras?.getString(FlightCancellationPassengerFragment.EXTRA_INVOICE_ID)
                    ?: ""

            putString(EXTRA_INVOICE_ID, invoiceId)
            putParcelableArrayList(EXTRA_CANCEL_JOURNEY, flightCancellationJourney as ArrayList<out Parcelable>)
        }
        navHostFragment.navController.setGraph(R.navigation.cancellation_nav_graph, argBundle)
    }

    override fun getComponent(): FlightCancellationComponent =
            DaggerFlightCancellationComponent.builder()
                    .flightComponent(FlightComponentInstance.getFlightComponent(application))
                    .build()

    companion object {

        const val FRAGMENT_RESULT_STATUS = "FRAGMENT_RESULT_STATUS"
        const val FRAGMENT_RESULT_ORIGIN = "FRAGMENT_RESULT_ORIGIN"

        fun createIntent(context: Context,
                         invoiceId: String,
                         flightCancellationJourney: List<FlightCancellationResponseEntity>)
                : Intent =
                Intent(context, FlightCancellationActivity::class.java)
                        .putExtra(FlightCancellationPassengerFragment.EXTRA_INVOICE_ID, invoiceId)
                        .putParcelableArrayListExtra(FlightCancellationPassengerFragment.EXTRA_CANCEL_JOURNEY, flightCancellationJourney as ArrayList<out Parcelable>)
    }
}