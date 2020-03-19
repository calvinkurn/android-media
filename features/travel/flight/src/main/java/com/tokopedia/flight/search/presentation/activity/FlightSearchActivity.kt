package com.tokopedia.flight.search.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.view.viewmodel.FlightAirportViewModel
import com.tokopedia.flight.bookingV3.presentation.activity.FlightBookingActivity
import com.tokopedia.flight.common.constant.FlightFlowConstant
import com.tokopedia.flight.common.constant.FlightFlowExtraConstant
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.common.util.FlightFlowUtil
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel
import com.tokopedia.flight.search.presentation.fragment.FlightSearchFragment
import com.tokopedia.flight.search.presentation.model.FlightPriceViewModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataViewModel
import com.tokopedia.flight.search_universal.presentation.bottomsheet.FlightSearchUniversalBottomSheet
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import kotlinx.android.synthetic.main.activity_flight_search.*

open class FlightSearchActivity : BaseSimpleActivity(),
        FlightSearchFragment.OnFlightSearchFragmentListener,
        FlightSearchUniversalBottomSheet.Listener {

    protected lateinit var dateString: String
    protected lateinit var passengerString: String
    protected lateinit var classString: String
    protected lateinit var passDataViewModel: FlightSearchPassDataViewModel

    private lateinit var remoteConfig: RemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar)
        initializeDataFromExtras()
        super.onCreate(savedInstanceState)

        setupSearchToolbarAction()
        setupSearchToolbarText()

        remoteConfig = FirebaseRemoteConfigImpl(this)
    }

    override fun getNewFragment(): Fragment = FlightSearchFragment.newInstance(passDataViewModel)

    override fun getScreenName(): String = FlightAnalytics.Screen.SEARCH

    override fun getLayoutRes(): Int = R.layout.activity_flight_search

    override fun getToolbarResourceID(): Int = R.id.flight_search_header

    override fun getParentViewResourceID(): Int = R.id.flight_search_parent_view

    private fun initializeDataFromExtras() {
        passDataViewModel = intent.extras.getParcelable(EXTRA_PASS_DATA)
        initializeToolbarData()
    }

    open fun initializeToolbarData() {
        dateString = FlightDateUtil.formatDate(
                FlightDateUtil.DEFAULT_FORMAT,
                FlightDateUtil.DEFAULT_VIEW_FORMAT,
                passDataViewModel.departureDate
        )
        passengerString = buildPassengerTextFormatted(passDataViewModel.flightPassengerViewModel)
        classString = passDataViewModel.flightClass.title
    }

    protected fun buildPassengerTextFormatted(passData: FlightPassengerViewModel): String {
        var passengerFmt = ""

        if (passData.adult > 0) {
            passengerFmt += "${passData.adult}  ${getString(R.string.flight_dashboard_adult_passenger)}"
            if (passData.children > 0) {
                passengerFmt += ", ${passData.children} ${getString(R.string.flight_dashboard_adult_children)}"
            }
            if (passData.infant > 0) {
                passengerFmt += ", ${passData.infant} ${getString(R.string.flight_dashboard_adult_infant)}"
            }
        }

        return passengerFmt
    }

    open fun getDepartureAirport(): FlightAirportViewModel = passDataViewModel.departureAirport

    open fun getArrivalAirport(): FlightAirportViewModel = passDataViewModel.arrivalAirport

    private fun setupSearchToolbarAction() {
        flight_search_header.addRightIcon(R.drawable.ic_flight_edit)
                .setOnClickListener {
                    showChangeSearchBottomSheet()
                }
    }

    private fun setupSearchToolbarText() {
        val title = "${getDepartureAirport().cityName} ➝ ${getArrivalAirport().cityName}"
        val subtitle = "$dateString | $passengerString | $classString"

        flight_search_header.title = title
        flight_search_header.subtitle = subtitle
    }

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
                        FlightFlowConstant.CHANGE_SEARCH_PARAM -> {
                            if (fragment is FlightSearchFragment) {
                                (fragment as FlightSearchFragment).flightSearchPresenter
                                        .attachView(fragment as FlightSearchFragment)
                                (fragment as FlightSearchFragment)
                                        .setSearchPassData((data.getParcelableExtra(EXTRA_PASS_DATA) as FlightSearchPassDataViewModel))
                                (fragment as FlightSearchFragment).refreshData()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun changeDate(flightSearchPassDataViewModel: FlightSearchPassDataViewModel) {
        passDataViewModel = flightSearchPassDataViewModel
        initializeToolbarData()
        setupSearchToolbarText()
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

    override fun onSaveSearchParams(flightSearchParams: FlightSearchPassDataViewModel) {
        passDataViewModel.departureAirport = flightSearchParams.departureAirport
        passDataViewModel.arrivalAirport = flightSearchParams.arrivalAirport
        passDataViewModel.isOneWay = flightSearchParams.isOneWay
        passDataViewModel.departureDate = flightSearchParams.departureDate
        passDataViewModel.returnDate = flightSearchParams.returnDate
        passDataViewModel.flightPassengerViewModel = flightSearchParams.flightPassengerViewModel
        passDataViewModel.flightClass = flightSearchParams.flightClass

        if (isReturnPage()) {
            val intent = Intent()
            intent.putExtra(EXTRA_PASS_DATA, passDataViewModel)
            FlightFlowUtil.actionSetResultAndClose(this, intent, FlightFlowConstant.CHANGE_SEARCH_PARAM)
        } else {
            if (fragment is FlightSearchFragment) {
                (fragment as FlightSearchFragment).flightSearchPresenter
                        .attachView(fragment as FlightSearchFragment)
                (fragment as FlightSearchFragment).setSearchPassData(passDataViewModel)
                (fragment as FlightSearchFragment).refreshData()
            }
        }
    }

    open fun isReturnPage(): Boolean = false

    private fun showChangeSearchBottomSheet() {
        val flightChangeSearchBottomSheet = FlightSearchUniversalBottomSheet.getInstance()
        flightChangeSearchBottomSheet.listener = this
        flightChangeSearchBottomSheet.setShowListener { flightChangeSearchBottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
        flightChangeSearchBottomSheet.show(supportFragmentManager, FlightSearchUniversalBottomSheet.TAG_SEARCH_FORM)
    }

    fun setupAndShowCoachMark() {
        val view = flight_search_header.rightIcons?.get(0)
        val coachMarkItems = arrayListOf<CoachMarkItem>()
        coachMarkItems.add(CoachMarkItem(
                view,
                getString(R.string.flight_search_coach_mark_change_title),
                getString(R.string.flight_search_coach_mark_change_description)
        ))

        val coachMark = CoachMarkBuilder().build()
        coachMark.show(this, TAG_CHANGE_COACH_MARK, coachMarkItems)
        Handler().postDelayed({
            if (coachMark.isAdded && coachMark.isVisible) {
                coachMark.dismiss()
            }
        }, DELAY_THREE_SECONDS)
    }

    companion object {
        const val TAG_CHANGE_COACH_MARK = "TagChangeSearchCoachMark"

        const val EXTRA_PASS_DATA = "EXTRA_PASS_DATA"

        private const val DELAY_THREE_SECONDS: Long = 3000

        private const val REQUEST_CODE_BOOKING = 10
        private const val REQUEST_CODE_RETURN = 11

        fun getCallingIntent(context: Context, passDataViewModel: FlightSearchPassDataViewModel): Intent {
            val intent = Intent(context, FlightSearchActivity::class.java)
            intent.putExtra(EXTRA_PASS_DATA, passDataViewModel)
            return intent
        }
    }

}