package com.tokopedia.flight.homepage.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.DeeplinkMapper.getRegisteredNavigation
import com.tokopedia.applink.RouteManager
import com.tokopedia.banner.Indicator
import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.view.activity.FlightAirportPickerActivity
import com.tokopedia.flight.airport.view.fragment.FlightAirportPickerFragment
import com.tokopedia.flight.airport.view.model.FlightAirportModel
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.dashboard.view.activity.FlightClassesActivity
import com.tokopedia.flight.dashboard.view.activity.FlightSelectPassengerActivity
import com.tokopedia.flight.dashboard.view.fragment.model.FlightClassModel
import com.tokopedia.flight.dashboard.view.fragment.model.FlightDashboardModel
import com.tokopedia.flight.dashboard.view.fragment.model.FlightPassengerModel
import com.tokopedia.flight.dashboard.view.widget.FlightCalendarOneWayWidget
import com.tokopedia.flight.homepage.di.FlightHomepageComponent
import com.tokopedia.flight.homepage.presentation.viewmodel.FlightHomepageViewModel
import com.tokopedia.flight.search.presentation.activity.FlightSearchActivity
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataModel
import com.tokopedia.flight.search_universal.presentation.widget.FlightSearchFormView
import com.tokopedia.travelcalendar.selectionrangecalendar.SelectionRangeCalendarWidget
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_flight_homepage.*
import java.util.*
import javax.inject.Inject

/**
 * @author by furqan on 27/03/2020
 */
class FlightHomepageFragment : BaseDaggerFragment(), FlightSearchFormView.FlightSearchFormListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var flightHomepageViewModel: FlightHomepageViewModel

    private lateinit var performanceMonitoring: PerformanceMonitoring
    private var isTraceStop: Boolean = false

    override fun getScreenName(): String = FlightHomepageFragment::class.java.simpleName

    override fun initInjector() {
        getComponent(FlightHomepageComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        performanceMonitoring = PerformanceMonitoring.start(FLIGHT_HOMEPAGE_TRACE)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            flightHomepageViewModel = viewModelProvider.get(FlightHomepageViewModel::class.java)
            flightHomepageViewModel.fetchBannerData(GraphqlHelper.loadRawString(resources, com.tokopedia.common.travel.R.raw.query_travel_collective_banner), true)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        flightHomepageViewModel.bannerList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    renderBannerView(it.data.banners)
                }
                is Fail -> {
                    hideBannerView()
                }
            }
            stopTrace()
        })

        flightHomepageViewModel.dashboardData.observe(viewLifecycleOwner, Observer {
            renderSearchForm(it)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_flight_homepage, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        flightHomepageSearchForm.listener = this
    }

    override fun onDepartureAirportClicked() {
        val intent = FlightAirportPickerActivity.createInstance(requireContext(), getString(R.string.flight_airportpicker_departure_title))
        startActivityForResult(intent, REQUEST_CODE_AIRPORT_DEPARTURE)
    }

    override fun onDestinationAirportClicked() {
        val intent = FlightAirportPickerActivity.createInstance(requireContext(), getString(R.string.flight_airportpicker_arrival_title))
        startActivityForResult(intent, REQUEST_CODE_AIRPORT_DESTINATION)
    }

    override fun onDepartureDateClicked(departureAirport: String, arrivalAirport: String, flightClassId: Int,
                                        departureDate: Date, returnDate: Date, isRoundTrip: Boolean) {
        val minMaxDate = flightHomepageViewModel.generatePairOfMinAndMaxDateForDeparture()
        if (isRoundTrip) {
            // if round trip, use selected date as a mindate and return date as selected date
            setCalendarDatePicker(
                    returnDate,
                    departureDate,
                    minMaxDate.second,
                    getString(com.tokopedia.travelcalendar.R.string.travel_calendar_label_choose_departure_trip_date),
                    TAG_DEPARTURE_CALENDAR
            )
        } else {
            val flightCalendarDialog = FlightCalendarOneWayWidget.newInstance(
                    FlightDateUtil.dateToString(minMaxDate.first, FlightDateUtil.DEFAULT_FORMAT),
                    FlightDateUtil.dateToString(minMaxDate.second, FlightDateUtil.DEFAULT_FORMAT),
                    FlightDateUtil.dateToString(departureDate, FlightDateUtil.DEFAULT_FORMAT),
                    departureAirport,
                    arrivalAirport,
                    flightClassId
            )
            flightCalendarDialog.setListener(object : FlightCalendarOneWayWidget.ActionListener {
                override fun onDateSelected(dateSelected: Date) {
                    val errorResourceId = flightHomepageViewModel.validateDepartureDate(dateSelected)
                    if (errorResourceId == -1) {
                        flightHomepageSearchForm.setDepartureDate(dateSelected)
                    } else {
                        showMessageErrorInSnackbar(errorResourceId)
                    }
                }

            })
            flightCalendarDialog.show(requireFragmentManager(), TAG_DEPARTURE_CALENDAR)
        }
    }

    override fun onReturnDateClicked(departureDate: Date, returnDate: Date) {
        val minMaxDate = flightHomepageViewModel.generatePairOfMinAndMaxDateForReturn(departureDate)
        setCalendarDatePicker(null,
                minMaxDate.first,
                minMaxDate.second,
                getString(com.tokopedia.travelcalendar.R.string.travel_calendar_label_choose_return_trip_date),
                TAG_RETURN_CALENDAR
        )
    }

    override fun onPassengerClicked(passengerModel: FlightPassengerModel?) {
        passengerModel?.let {
            val intent = FlightSelectPassengerActivity.getCallingIntent(requireContext(), it)
            startActivityForResult(intent, REQUEST_CODE_SELECT_PASSENGER)
        }
    }

    override fun onClassClicked(flightClassId: Int) {
        val intent = FlightClassesActivity.getCallingIntent(requireContext(), flightClassId)
        startActivityForResult(intent, REQUEST_CODE_SELECT_CLASSES)
    }

    override fun onSaveSearch(flightSearchData: FlightSearchPassDataModel) {
        flightSearchData.linkUrl = arguments?.getString(EXTRA_FROM_DEEPLINK_URL) ?: ""
        startActivityForResult(FlightSearchActivity.getCallingIntent(requireContext(), flightSearchData),
                REQUEST_CODE_SEARCH)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_AIRPORT_DEPARTURE -> {
                    val departureAirport = data?.getParcelableExtra<FlightAirportModel>(FlightAirportPickerFragment.EXTRA_SELECTED_AIRPORT)
                    departureAirport?.let {
                        flightHomepageViewModel.onDepartureAirportChanged(it)
                    }
                }
                REQUEST_CODE_AIRPORT_DESTINATION -> {
                    val destinationAirport = data?.getParcelableExtra<FlightAirportModel>(FlightAirportPickerFragment.EXTRA_SELECTED_AIRPORT)
                    destinationAirport?.let {
                        flightHomepageViewModel.onArrivalAirportChanged(it)
                    }
                }
                REQUEST_CODE_SELECT_CLASSES -> {
                    val classModel = data?.getParcelableExtra<FlightClassModel>(FlightClassesActivity.EXTRA_FLIGHT_CLASS)
                    classModel?.let {
                        flightHomepageViewModel.onClassChanged(it)
                    }
                }
                REQUEST_CODE_SELECT_PASSENGER -> {
                    val passengerModel = data?.getParcelableExtra<FlightPassengerModel>(FlightSelectPassengerActivity.EXTRA_PASS_DATA)
                    passengerModel?.let {
                        flightHomepageViewModel.onPassengerChanged(it)
                    }
                }
            }
        }
    }

    private fun renderBannerView(bannerList: List<TravelCollectiveBannerModel.Banner>) {
        if (bannerList.isNotEmpty()) {
            showBannerView()
            flightHomepageBanner.customWidth = resources.getDimensionPixelSize(R.dimen.banner_width)
            flightHomepageBanner.customHeight = resources.getDimensionPixelSize(R.dimen.banner_height)
            flightHomepageBanner.setBannerSeeAllTextColor(resources.getColor(com.tokopedia.unifycomponents.R.color.Green_G500))
            flightHomepageBanner.setBannerIndicator(Indicator.GREEN)
            flightHomepageBanner.setOnPromoScrolledListener { position ->
                // send impression if banner not null
            }
            flightHomepageBanner.setOnPromoClickListener { position -> onBannerClicked(position) }
            flightHomepageBanner.setOnPromoAllClickListener { onAllBannerClicked() }

            val bannerUrls = arrayListOf<String>()
            for (banner in bannerList) {
                bannerUrls.add(banner.attribute.imageUrl)
            }
            flightHomepageBanner.setPromoList(bannerUrls)
            flightHomepageBanner.buildView()
            KeyboardHandler.hideSoftKeyboard(requireActivity())
            KeyboardHandler.DropKeyboard(requireContext(), requireView())
        } else {
            hideBannerView()
        }
    }

    private fun showBannerView() {
        flightHomepageBannerLayout.visibility = View.VISIBLE
        flightHomepageBanner.visibility = View.VISIBLE
    }

    private fun hideBannerView() {
        flightHomepageBannerLayout.visibility = View.GONE
        flightHomepageBanner.visibility = View.GONE
    }

    private fun onBannerClicked(position: Int) {
        context?.let {
            val bannerData = flightHomepageViewModel.getBannerData(position)
            bannerData?.let { banner ->
                flightHomepageViewModel.onBannerClicked(position, banner)
                when {
                    RouteManager.isSupportApplink(it, banner.attribute.appUrl) -> {
                        RouteManager.route(it, banner.attribute.appUrl)
                    }
                    getRegisteredNavigation(it, banner.attribute.appUrl).isNotEmpty() -> {
                        RouteManager.route(it, getRegisteredNavigation(it, banner.attribute.appUrl))
                    }
                    banner.attribute.webUrl.isNotEmpty() -> {
                        RouteManager.route(it, banner.attribute.webUrl)
                    }
                    else -> {
                    }
                }
            }
        }
    }

    private fun onAllBannerClicked() {
        RouteManager.route(context, ApplinkConst.PROMO_LIST)
    }

    private fun renderSearchForm(dashboardData: FlightDashboardModel) {
        dashboardData.departureAirport?.let {
            flightHomepageSearchForm.setOriginAirport(it)
        }
        dashboardData.arrivalAirport?.let {
            flightHomepageSearchForm.setDestinationAirport(it)
        }
        dashboardData.flightPassengerViewModel?.let {
            flightHomepageSearchForm.setPassengerView(it)
        }
        dashboardData.flightClass?.let {
            flightHomepageSearchForm.setClassView(it)
        }
    }

    private fun setCalendarDatePicker(selectedDate: Date?, minDate: Date, maxDate: Date, title: String, tag: String) {
        val minDateStr = FlightDateUtil.dateToString(minDate, FlightDateUtil.DEFAULT_FORMAT)
        val selectedDateStr = if (selectedDate != null) FlightDateUtil.dateToString(selectedDate, FlightDateUtil.DEFAULT_FORMAT) else null

        val flightCalendarDialog = SelectionRangeCalendarWidget.getInstance(
                minDateStr, selectedDateStr,
                SelectionRangeCalendarWidget.DEFAULT_RANGE_CALENDAR_YEAR,
                SelectionRangeCalendarWidget.DEFAULT_RANGE_DATE_SELECTED.toLong(),
                getString(R.string.flight_min_date_label),
                getString(R.string.flight_max_date_label),
                SelectionRangeCalendarWidget.DEFAULT_MIN_SELECTED_DATE_TODAY,
                true
        )
        flightCalendarDialog.listener = object : SelectionRangeCalendarWidget.OnDateClickListener {
            override fun onDateClick(dateIn: Date, dateOut: Date) {
                val departureErrorResourceId = flightHomepageViewModel.validateDepartureDate(dateIn)
                if (departureErrorResourceId == -1) {
                    flightHomepageSearchForm.setDepartureDate(dateIn)
                } else {
                    showMessageErrorInSnackbar(departureErrorResourceId)
                }

                val returnErrorResourceId = flightHomepageViewModel.validateReturnDate(dateIn, dateOut)
                if (returnErrorResourceId == -1) {
                    flightHomepageSearchForm.setReturnDate(dateOut)
                } else {
                    showMessageErrorInSnackbar(returnErrorResourceId)
                }
            }
        }
        flightCalendarDialog.show(requireFragmentManager(), tag)
    }

    private fun showMessageErrorInSnackbar(resourceId: Int) {
        NetworkErrorHelper.showRedCloseSnackbar(activity, getString(resourceId))
    }

    private fun stopTrace() {
        if (!isTraceStop) {
            performanceMonitoring.stopTrace()
            isTraceStop = true
        }
    }

    companion object {
        private const val TAG_DEPARTURE_CALENDAR = "flightCalendarDeparture"
        private const val TAG_RETURN_CALENDAR = "flightCalendarReturn"

        private const val EXTRA_FROM_DEEPLINK_URL = "EXTRA_FROM_DEEPLINK_URL"
        private const val EXTRA_TRIP = "EXTRA_TRIP"
        private const val EXTRA_ADULT = "EXTRA_ADULT"
        private const val EXTRA_CHILD = "EXTRA_CHILD"
        private const val EXTRA_INFANT = "EXTRA_INFANT"
        private const val EXTRA_CLASS = "EXTRA_CLASS"
        private const val EXTRA_AUTO_SEARCH = "EXTRA_AUTO_SEARCH"

        private const val REQUEST_CODE_AIRPORT_DEPARTURE = 1
        private const val REQUEST_CODE_AIRPORT_DESTINATION = 2
        private const val REQUEST_CODE_SELECT_PASSENGER = 3
        private const val REQUEST_CODE_SELECT_CLASSES = 4
        private const val REQUEST_CODE_SEARCH = 5

        private const val FLIGHT_HOMEPAGE_TRACE = "tr_flight_homepage"

        fun getInstance(linkUrl: String): FlightHomepageFragment =
                FlightHomepageFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_FROM_DEEPLINK_URL, linkUrl)
                    }
                }

        fun getInstance(extrasTrip: String,
                        extrasAdultPassenger: String,
                        extrasChildPassenger: String,
                        extrasInfantPassenger: String,
                        extrasClass: String,
                        extrasAutoSearch: String,
                        linkUrl: String): FlightHomepageFragment =
                FlightHomepageFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_TRIP, extrasTrip)
                        putString(EXTRA_ADULT, extrasAdultPassenger)
                        putString(EXTRA_CHILD, extrasChildPassenger)
                        putString(EXTRA_INFANT, extrasInfantPassenger)
                        putString(EXTRA_CLASS, extrasClass)
                        putString(EXTRA_AUTO_SEARCH, extrasAutoSearch)
                        putString(EXTRA_FROM_DEEPLINK_URL, linkUrl)
                    }
                }
    }

}