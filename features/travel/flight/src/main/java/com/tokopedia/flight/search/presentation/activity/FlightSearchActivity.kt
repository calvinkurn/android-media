package com.tokopedia.flight.search.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.view.viewmodel.FlightAirportViewModel
import com.tokopedia.flight.bookingV3.presentation.activity.FlightBookingActivity
import com.tokopedia.flight.common.constant.FlightFlowConstant
import com.tokopedia.flight.common.constant.FlightFlowExtraConstant
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.common.util.FlightFlowUtil
import com.tokopedia.flight.common.view.BaseFlightActivity
import com.tokopedia.flight.search.presentation.fragment.FlightSearchFragment
import com.tokopedia.flight.search.presentation.model.FlightPriceViewModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataViewModel
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey

open class FlightSearchActivity : BaseFlightActivity(),
        FlightSearchFragment.OnFlightSearchFragmentListener {

    protected lateinit var passDataViewModel: FlightSearchPassDataViewModel

    private lateinit var remoteConfig: RemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar)
        initializeDataFromExtras()
        super.onCreate(savedInstanceState)

        remoteConfig = FirebaseRemoteConfigImpl(this)
    }

    override fun getNewFragment(): Fragment = FlightSearchFragment.newInstance(passDataViewModel)

    override fun getScreenName(): String = FlightAnalytics.Screen.SEARCH

    private fun initializeDataFromExtras() {
        passDataViewModel = intent.extras.getParcelable(EXTRA_PASS_DATA)
    }

    open fun getDepartureAirport(): FlightAirportViewModel = passDataViewModel.departureAirport

    open fun getArrivalAirport(): FlightAirportViewModel = passDataViewModel.arrivalAirport

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_RETURN, REQUEST_CODE_BOOKING -> {
                if (data != null) {
                    when (data.getIntExtra(FlightFlowExtraConstant.EXTRA_FLOW_DATA, 0)) {
                        FlightFlowConstant.PRICE_CHANGE -> {
                            if (fragment is FlightSearchFragment) {
                                (fragment as FlightSearchFragment).flightSearchPresenter
                                        .attachView(fragment as FlightSearchFragment)
                                (fragment as FlightSearchFragment).refreshData()
                            }
                        }
                        FlightFlowConstant.EXPIRED_JOURNEY -> {
                            FlightFlowUtil.actionSetResultAndClose(this, intent,
                                    FlightFlowConstant.EXPIRED_JOURNEY)
                        }
                    }
                }
            }
        }
    }

    override fun changeDate(flightSearchPassDataViewModel: FlightSearchPassDataViewModel) {
        passDataViewModel = flightSearchPassDataViewModel
    }

    override fun selectFlight(selectedFlightID: String, selectedTerm: String, flightPriceViewModel: FlightPriceViewModel,
                              isBestPairing: Boolean, isCombineDone: Boolean) {
        if (passDataViewModel.isOneWay) {
            if (remoteConfig.getBoolean(RemoteConfigKey.ANDROID_CUSTOMER_FLIGHT_BOOKING_NEW_FLOW, true)) {
                startActivityForResult(FlightBookingActivity
                        .getCallingIntent(this, passDataViewModel, selectedFlightID, selectedTerm,
                                flightPriceViewModel),
                        REQUEST_CODE_BOOKING)
            } else {
                startActivityForResult(com.tokopedia.flight.bookingV2.presentation.activity.FlightBookingActivity
                        .getCallingIntent(this, passDataViewModel, selectedFlightID,
                                flightPriceViewModel),
                        REQUEST_CODE_BOOKING)
            }
        } else {
            startActivityForResult(FlightSearchReturnActivity
                    .getCallingIntent(this, passDataViewModel, selectedFlightID, selectedTerm,
                            isBestPairing, flightPriceViewModel, isCombineDone),
                    REQUEST_CODE_RETURN)
        }
    }

    companion object {

        const val TAG_CHANGE_COACH_MARK = "TagChangeSearchCoachMark"

        const val EXTRA_PASS_DATA = "EXTRA_PASS_DATA"

        private const val REQUEST_CODE_BOOKING = 10
        private const val REQUEST_CODE_RETURN = 11

        fun getCallingIntent(context: Context, passDataViewModel: FlightSearchPassDataViewModel): Intent {
            val intent = Intent(context, FlightSearchActivity::class.java)
            intent.putExtra(EXTRA_PASS_DATA, passDataViewModel)
            return intent
        }
    }

}
