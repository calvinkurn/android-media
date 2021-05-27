package com.tokopedia.flight.cancellation_navigation.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellation.di.DaggerFlightCancellationComponent
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent
import com.tokopedia.flight.cancellation.presentation.fragment.FlightCancellationPassengerFragment
import com.tokopedia.flight.cancellation_navigation.presentation.fragment.FlightCancellationPassengerFragment.Companion.EXTRA_CANCEL_JOURNEY
import com.tokopedia.flight.cancellation_navigation.presentation.fragment.FlightCancellationPassengerFragment.Companion.EXTRA_INVOICE_ID
import com.tokopedia.flight.cancellation_navigation.presentation.listener.FlightCancellationNavResult
import com.tokopedia.flight.orderlist.view.viewmodel.FlightCancellationJourney
import java.util.*
import kotlin.properties.Delegates

class FlightCancellationActivity : BaseSimpleActivity(), HasComponent<FlightCancellationComponent> {

    override fun getNewFragment(): Fragment? = null

    override fun getLayoutRes(): Int = R.layout.activity_flight_cancellation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container_cancellation_navigation) as NavHostFragment
        val argBundle = Bundle().apply {
            val flightCancellationJourney: List<FlightCancellationJourney> = intent.extras?.getParcelableArrayList(FlightCancellationPassengerFragment.EXTRA_CANCEL_JOURNEY)
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

    fun popBackStackWithResult(fragmentStatus: Int, fragmentOrigin: String, extras: Bundle = Bundle()) {
        val childFragmentManager = supportFragmentManager
                .findFragmentById(R.id.container_cancellation_navigation)?.childFragmentManager
        var backstackListener: FragmentManager.OnBackStackChangedListener by Delegates.notNull()
        backstackListener = FragmentManager.OnBackStackChangedListener {
            val result = extras.also {
                it.putInt(FRAGMENT_RESULT_STATUS, fragmentStatus)
                it.putString(FRAGMENT_RESULT_ORIGIN, fragmentOrigin)
            }
            (childFragmentManager?.fragments?.get(0) as FlightCancellationNavResult).onNavigationResult(result)
            childFragmentManager.removeOnBackStackChangedListener(backstackListener)
        }
        childFragmentManager?.addOnBackStackChangedListener(backstackListener)
        findNavController(R.id.container_cancellation_navigation).popBackStack()
    }

    companion object {

        const val FRAGMENT_RESULT_STATUS = "FRAGMENT_RESULT_STATUS"
        const val FRAGMENT_RESULT_ORIGIN = "FRAGMENT_RESULT_ORIGIN"

        fun createIntent(context: Context,
                         invoiceId: String,
                         flightCancellationJourney: List<FlightCancellationJourney>)
                : Intent =
                Intent(context, FlightCancellationActivity::class.java)
                        .putExtra(FlightCancellationPassengerFragment.EXTRA_INVOICE_ID, invoiceId)
                        .putParcelableArrayListExtra(FlightCancellationPassengerFragment.EXTRA_CANCEL_JOURNEY, flightCancellationJourney as ArrayList<out Parcelable>)
    }
}