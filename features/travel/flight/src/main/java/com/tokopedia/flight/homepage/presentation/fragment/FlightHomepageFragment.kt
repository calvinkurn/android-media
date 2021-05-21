package com.tokopedia.flight.homepage.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.DeeplinkMapper.getRegisteredNavigation
import com.tokopedia.applink.RouteManager
import com.tokopedia.banner.Indicator
import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.common.travel.presentation.model.TravelVideoBannerModel
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
import com.tokopedia.common.travel.widget.TravelVideoBannerWidget
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.view.model.FlightAirportModel
import com.tokopedia.flight.airportv2.presentation.bottomsheet.FlightAirportPickerBottomSheet
import com.tokopedia.flight.common.constant.FlightUrl.FLIGHT_PROMO_APPLINK
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.homepage.di.FlightHomepageComponent
import com.tokopedia.flight.homepage.presentation.bottomsheet.FlightSelectClassBottomSheet
import com.tokopedia.flight.homepage.presentation.bottomsheet.FlightSelectPassengerBottomSheet
import com.tokopedia.flight.homepage.presentation.model.FlightClassModel
import com.tokopedia.flight.homepage.presentation.model.FlightHomepageModel
import com.tokopedia.flight.homepage.presentation.model.FlightPassengerModel
import com.tokopedia.flight.homepage.presentation.viewmodel.FlightHomepageViewModel
import com.tokopedia.flight.homepage.presentation.widget.FlightCalendarOneWayWidget
import com.tokopedia.flight.homepage.presentation.widget.FlightCalendarRoundTripWidget
import com.tokopedia.flight.search.presentation.activity.FlightSearchActivity
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataModel
import com.tokopedia.flight.search_universal.presentation.widget.FlightSearchFormView
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.travelcalendar.selectionrangecalendar.SelectionRangeCalendarWidget
import com.tokopedia.travelcalendar.singlecalendar.SinglePickCalendarWidget
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_flight_homepage.*
import java.util.*
import javax.inject.Inject

/**
 * @author by furqan on 27/03/2020
 */
class FlightHomepageFragment : BaseDaggerFragment(),
        FlightSearchFormView.FlightSearchFormListener, TravelVideoBannerWidget.ActionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var flightHomepageViewModel: FlightHomepageViewModel

    private lateinit var remoteConfig: RemoteConfig
    private lateinit var performanceMonitoring: PerformanceMonitoring
    private var isTraceStop: Boolean = false
    private var applinkErrorTextResource = -1
    private var isSearchFromWidget: Boolean = false
    private var bannerWidthInPixels = 0

    override fun getScreenName(): String = FlightHomepageFragment::class.java.simpleName

    override fun initInjector() {
        getComponent(FlightHomepageComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        remoteConfig = FirebaseRemoteConfigImpl(context)
        performanceMonitoring = PerformanceMonitoring.start(FLIGHT_HOMEPAGE_TRACE)

        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        bannerWidthInPixels = displayMetrics.widthPixels
        bannerWidthInPixels -= resources.getDimensionPixelSize(R.dimen.banner_offset)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            flightHomepageViewModel = viewModelProvider.get(FlightHomepageViewModel::class.java)
            flightHomepageViewModel.init()

            arguments?.let {
                if (it.getString(EXTRA_TRIP, "").isNotEmpty() &&
                        it.getString(EXTRA_ADULT, "").isNotEmpty() &&
                        it.getString(EXTRA_CHILD, "").isNotEmpty() &&
                        it.getString(EXTRA_INFANT, "").isNotEmpty() &&
                        it.getString(EXTRA_CLASS, "").isNotEmpty() &&
                        it.getString(EXTRA_AUTO_SEARCH, "").isNotEmpty()) {
                    applinkErrorTextResource = flightHomepageViewModel.setupApplinkParams(
                            it.getString(EXTRA_TRIP, ""),
                            it.getString(EXTRA_ADULT, ""),
                            it.getString(EXTRA_CHILD, ""),
                            it.getString(EXTRA_INFANT, ""),
                            it.getString(EXTRA_CLASS, ""),
                            it.getString(EXTRA_AUTO_SEARCH, "")
                    )
                }
            }

            flightHomepageViewModel.fetchBannerData(true)
            flightHomepageViewModel.fetchTickerData()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        flightHomepageViewModel.bannerList.observe(viewLifecycleOwner, Observer {
            flightHomepageViewModel.fetchVideoBannerData()
            when (it) {
                is Success -> {
                    renderBannerTitle(it.data.meta.label)
                    renderBannerView(it.data.banners)
                }
                is Fail -> {
                    hideBannerView()
                }
            }
            stopTrace()
        })

        flightHomepageViewModel.videoBanner.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    renderVideoBannerView(it.data)
                }
                is Fail -> {
                    hideVideoBannerView()
                }
            }
        })

        flightHomepageViewModel.homepageData.observe(viewLifecycleOwner, Observer {
            renderSearchForm(it)
        })

        flightHomepageViewModel.tickerData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (it.data.message.isNotEmpty()) {
                        renderTickerView(it.data)
                    } else {
                        hideTickerView()
                    }
                }
                is Fail -> {
                    hideTickerView()
                }
            }
        })

        flightHomepageViewModel.autoSearch.observe(viewLifecycleOwner, Observer {
            if (it) {
                isSearchFromWidget = true
                flightHomepageSearchForm.autoSearch()
            }
        })

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_flight_homepage, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        flightHomepageSearchForm.listener = this

        if (applinkErrorTextResource != -1) {
            showMessageErrorInSnackbar(applinkErrorTextResource)
        }

        if (::flightHomepageViewModel.isInitialized)
            flightHomepageViewModel.sendTrackingOpenScreen(FlightAnalytics.Screen.HOMEPAGE)
    }

    override fun onRoundTripSwitchChanged(isRoundTrip: Boolean) {
        flightHomepageViewModel.sendTrackingRoundTripSwitchChanged(getString(
                if (isRoundTrip) R.string.flight_dashboard_analytic_round_trip
                else R.string.flight_dashboard_analytic_one_way))
    }

    override fun onDepartureAirportClicked() {
        val flightAirportPickerBottomSheet = FlightAirportPickerBottomSheet.getInstance()
        flightAirportPickerBottomSheet.listener = object : FlightAirportPickerBottomSheet.Listener {
            override fun onAirportSelected(selectedAirport: FlightAirportModel) {
                flightHomepageViewModel.onDepartureAirportChanged(selectedAirport)
            }
        }
        fragmentManager?.let {
            flightAirportPickerBottomSheet.show(it, FlightAirportPickerBottomSheet.TAG_FLIGHT_AIRPORT_PICKER)
        }
    }

    override fun onDestinationAirportClicked() {
        val flightAirportPickerBottomSheet = FlightAirportPickerBottomSheet.getInstance()
        flightAirportPickerBottomSheet.listener = object : FlightAirportPickerBottomSheet.Listener {
            override fun onAirportSelected(selectedAirport: FlightAirportModel) {
                flightHomepageViewModel.onArrivalAirportChanged(selectedAirport)
            }
        }
        flightAirportPickerBottomSheet.setShowListener { flightAirportPickerBottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
        fragmentManager?.let {
            flightAirportPickerBottomSheet.show(it, FlightAirportPickerBottomSheet.TAG_FLIGHT_AIRPORT_PICKER)
        }
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
                    TAG_DEPARTURE_CALENDAR,
                    departureAirport,
                    arrivalAirport,
                    flightClassId
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
            flightCalendarDialog.setListener(object : SinglePickCalendarWidget.ActionListener {
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

    override fun onReturnDateClicked(departureDate: Date, returnDate: Date,
                                     departureAirport: String, arrivalAirport: String, flightClassId: Int) {
        val minMaxDate = flightHomepageViewModel.generatePairOfMinAndMaxDateForReturn(departureDate)
        setCalendarDatePicker(null,
                minMaxDate.first,
                minMaxDate.second,
                getString(com.tokopedia.travelcalendar.R.string.travel_calendar_label_choose_return_trip_date),
                TAG_RETURN_CALENDAR,
                departureAirport,
                arrivalAirport,
                flightClassId
        )
    }

    override fun onPassengerClicked(passengerModel: FlightPassengerModel?) {
        val flightSelectPassengerBottomSheet = FlightSelectPassengerBottomSheet()
        flightSelectPassengerBottomSheet.listener = object : FlightSelectPassengerBottomSheet.Listener {
            override fun onSavedPassenger(passengerModel: FlightPassengerModel) {
                flightHomepageViewModel.onPassengerChanged(passengerModel)
            }
        }
        flightSelectPassengerBottomSheet.passengerModel = passengerModel
        flightSelectPassengerBottomSheet.setShowListener { flightSelectPassengerBottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
        flightSelectPassengerBottomSheet.show(requireFragmentManager(), FlightSelectPassengerBottomSheet.TAG_SELECT_PASSENGER)
    }

    override fun onClassClicked(flightClassId: Int) {
        val flightSelectClassBottomSheet = FlightSelectClassBottomSheet()
        flightSelectClassBottomSheet.listener = object : FlightSelectClassBottomSheet.Listener {
            override fun onClassSelected(classEntity: FlightClassModel) {
                flightHomepageViewModel.onClassChanged(classEntity)
            }
        }
        flightSelectClassBottomSheet.setSelectedClass(flightClassId)
        flightSelectClassBottomSheet.setShowListener { flightSelectClassBottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
        flightSelectClassBottomSheet.show(requireFragmentManager(), FlightSelectClassBottomSheet.TAG_SELECT_CLASS)
    }

    override fun onSaveSearch(flightSearchData: FlightSearchPassDataModel) {
        flightHomepageViewModel.onSearchTicket(flightSearchData)
        flightSearchData.linkUrl = arguments?.getString(EXTRA_FROM_DEEPLINK_URL) ?: ""
        if (validateSearchPassData(flightSearchData)) {
            navigateToSearchPage(flightSearchData)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SEARCH) {
            flightHomepageSearchForm.init()
        }

    }

    override fun onVideoBannerClicked(bannerData: TravelVideoBannerModel) {
        flightHomepageViewModel.sendTrackingVideoBannerClick(bannerData)
    }

    private fun renderVideoBannerView(bannerData: TravelCollectiveBannerModel) {
        flightHomepageVideoBanner.listener = this
        flightHomepageVideoBanner.setData(bannerData)
        flightHomepageVideoBanner.build()
        flightHomepageViewModel.sendTrackingVideoBannerImpression(flightHomepageVideoBanner.getData())
    }

    private fun hideVideoBannerView() {
        flightHomepageVideoBanner.hideTravelVideoBanner()
    }

    private fun renderBannerTitle(title: String) {
        if (title.isNotEmpty()) {
            flightHomepageBannerTitle.text = title
            flightHomepageBannerTitle.visibility = View.VISIBLE
        } else {
            flightHomepageBannerTitle.visibility = View.GONE
        }
    }

    private fun renderBannerView(bannerList: List<TravelCollectiveBannerModel.Banner>) {
        if (bannerList.isNotEmpty()) {
            showBannerView()
            flightHomepageBanner.customWidth = if (bannerWidthInPixels > 0) {
                bannerWidthInPixels
            } else {
                resources.getDimensionPixelSize(R.dimen.banner_width)
            }
            flightHomepageBanner.customHeight = if (bannerWidthInPixels > 0) {
                measureBannerHeightBasedOnRatio()
            } else {
                resources.getDimensionPixelSize(R.dimen.banner_height)
            }
            flightHomepageBanner.setBannerSeeAllTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_G500))
            flightHomepageBanner.setBannerIndicator(Indicator.GREEN)
            flightHomepageBanner.setOnPromoScrolledListener { position ->
                flightHomepageViewModel.sendTrackingPromoScrolled(position)
            }
            flightHomepageBanner.setOnPromoClickListener { position -> onBannerClicked(position) }
            flightHomepageBanner.setOnPromoAllClickListener { onAllBannerClicked() }

            val bannerUrls = arrayListOf<String>()
            for (banner in bannerList) {
                bannerUrls.add(banner.attribute.imageUrl)
            }
            flightHomepageBanner.setPromoList(bannerUrls)
            flightHomepageBanner.buildView()
        }
    }

    private fun showBannerView() {
        flightHomepageBannerLayout.visibility = View.VISIBLE
    }

    private fun hideBannerView() {
        flightHomepageBannerLayout.visibility = View.GONE
    }

    private fun renderTickerView(travelTickerModel: TravelTickerModel) {
        flightHomepageTicker.setHtmlDescription(travelTickerModel.message)
        flightHomepageTicker.tickerType = Ticker.TYPE_WARNING
        flightHomepageTicker.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                if (linkUrl.isNotEmpty()) {
                    RouteManager.route(context, linkUrl.toString())
                }
            }

            override fun onDismiss() {}

        })
        if (travelTickerModel.url.isNotEmpty()) {
            flightHomepageTicker.setOnClickListener {
                RouteManager.route(requireContext(), travelTickerModel.url)
            }
        }

        showTickerView()
    }

    private fun showTickerView() {
        flightHomepageTicker.visibility = View.VISIBLE
    }

    private fun hideTickerView() {
        flightHomepageTicker.visibility = View.GONE
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
        RouteManager.route(context, FLIGHT_PROMO_APPLINK)
    }

    private fun renderSearchForm(homepageData: FlightHomepageModel) {
        homepageData.departureAirport?.let {
            flightHomepageSearchForm.setOriginAirport(it)
        }
        homepageData.arrivalAirport?.let {
            flightHomepageSearchForm.setDestinationAirport(it)
        }
        homepageData.flightPassengerViewModel?.let {
            flightHomepageSearchForm.setPassengerView(it)
        }
        homepageData.flightClass?.let {
            flightHomepageSearchForm.setClassView(it)
        }
    }

    private fun setCalendarDatePicker(selectedDate: Date?, minDate: Date, maxDate: Date, title: String, tag: String,
                                      departureCode: String, arrivalCode: String,
                                      classFlight: Int) {
        val minDateStr = FlightDateUtil.dateToString(minDate, FlightDateUtil.DEFAULT_FORMAT)
        val maxDateStr = FlightDateUtil.dateToString(maxDate, FlightDateUtil.DEFAULT_FORMAT)

        val selectedDateStr = if (selectedDate != null) FlightDateUtil.dateToString(selectedDate, FlightDateUtil.DEFAULT_FORMAT) else null

        val flightCalendarDialog = FlightCalendarRoundTripWidget.getInstance(
                minDateStr, selectedDateStr,
                SelectionRangeCalendarWidget.DEFAULT_RANGE_CALENDAR_YEAR,
                SelectionRangeCalendarWidget.DEFAULT_RANGE_DATE_SELECTED.toLong(),
                getString(R.string.flight_min_date_label),
                getString(R.string.flight_max_date_label),
                SelectionRangeCalendarWidget.DEFAULT_MIN_SELECTED_DATE_TODAY,
                true,
                departureCode,
                arrivalCode,
                classFlight,
                maxDateStr
        )
        flightCalendarDialog.listener = object : SelectionRangeCalendarWidget.OnDateClickListener {
            override fun onDateClick(dateIn: Date, dateOut: Date) {
                val departureErrorResourceId = flightHomepageViewModel.validateDepartureDate(dateIn)
                if (departureErrorResourceId == -1) {
                    flightHomepageSearchForm.setDepartureDate(dateIn)

                    val returnErrorResourceId = flightHomepageViewModel.validateReturnDate(dateIn, dateOut)
                    if (returnErrorResourceId == -1) {
                        flightHomepageSearchForm.setReturnDate(dateOut)
                    } else {
                        showMessageErrorInSnackbar(returnErrorResourceId)
                    }
                } else {
                    showMessageErrorInSnackbar(departureErrorResourceId)
                }
            }
        }
        flightCalendarDialog.show(requireFragmentManager(), tag)
    }

    private fun showMessageErrorInSnackbar(resourceId: Int) {
        view?.let {
            Toaster.build(it,
                    getString(resourceId),
                    Toaster.LENGTH_SHORT,
                    Toaster.TYPE_ERROR,
                    getString(R.string.flight_booking_action_okay)).show()
        }
    }

    private fun stopTrace() {
        if (!isTraceStop) {
            performanceMonitoring.stopTrace()
            isTraceStop = true
        }
    }


    private fun validateSearchPassData(flightSearchData: FlightSearchPassDataModel): Boolean {
        var isValid = true

        if (flightSearchData.departureAirport.cityCode != null &&
                flightSearchData.departureAirport.cityCode.isNotEmpty() &&
                flightSearchData.arrivalAirport.cityCode != null &&
                flightSearchData.arrivalAirport.cityCode.isNotEmpty() &&
                flightSearchData.departureAirport.cityCode == flightSearchData.arrivalAirport.cityCode) {
            isValid = false
            showMessageErrorInSnackbar(R.string.flight_dashboard_arrival_departure_same_error)
        } else if (flightSearchData.departureAirport.cityAirports != null &&
                flightSearchData.departureAirport.cityAirports.isNotEmpty() &&
                flightSearchData.departureAirport.cityAirports.contains(flightSearchData.arrivalAirport.airportCode)) {
            isValid = false
            showMessageErrorInSnackbar(R.string.flight_dashboard_arrival_departure_same_error)
        } else if (flightSearchData.arrivalAirport.cityAirports != null &&
                flightSearchData.arrivalAirport.cityAirports.isNotEmpty() &&
                flightSearchData.arrivalAirport.cityAirports.contains(flightSearchData.departureAirport.airportCode)) {
            isValid = false
            showMessageErrorInSnackbar(R.string.flight_dashboard_arrival_departure_same_error)
        } else if (flightSearchData.departureAirport.airportCode != null &&
                flightSearchData.departureAirport.airportCode.isNotEmpty() &&
                flightSearchData.arrivalAirport.airportCode != null &&
                flightSearchData.arrivalAirport.airportCode.isNotEmpty() &&
                flightSearchData.departureAirport.airportCode == flightSearchData.arrivalAirport.airportCode) {
            isValid = false
            showMessageErrorInSnackbar(R.string.flight_dashboard_arrival_departure_same_error)
        } else if (flightSearchData.departureAirport.cityName != null &&
                flightSearchData.departureAirport.cityName.isNotEmpty() &&
                flightSearchData.arrivalAirport.cityName != null &&
                flightSearchData.arrivalAirport.cityName.isNotEmpty() &&
                flightSearchData.departureAirport.cityName == flightSearchData.arrivalAirport.cityName) {
            isValid = false
            showMessageErrorInSnackbar(R.string.flight_dashboard_arrival_departure_same_error)
        }

        return isValid
    }

    private fun navigateToSearchPage(flightSearchData: FlightSearchPassDataModel) {
        startActivityForResult(FlightSearchActivity.getCallingIntent(requireContext(), flightSearchData, isSearchFromWidget),
                REQUEST_CODE_SEARCH)
    }

    private fun measureBannerHeightBasedOnRatio(): Int =
            (bannerWidthInPixels * BANNER_HEIGHT_RATIO / BANNER_WIDTH_RATIO).toInt()

    companion object {
        // Banner Ratio = 414 : 139
        private const val BANNER_WIDTH_RATIO = 414f
        private const val BANNER_HEIGHT_RATIO = 139f

        private const val TAG_DEPARTURE_CALENDAR = "flightCalendarDeparture"
        private const val TAG_RETURN_CALENDAR = "flightCalendarReturn"

        private const val EXTRA_FROM_DEEPLINK_URL = "EXTRA_FROM_DEEPLINK_URL"
        private const val EXTRA_TRIP = "EXTRA_TRIP"
        private const val EXTRA_ADULT = "EXTRA_ADULT"
        private const val EXTRA_CHILD = "EXTRA_CHILD"
        private const val EXTRA_INFANT = "EXTRA_INFANT"
        private const val EXTRA_CLASS = "EXTRA_CLASS"
        private const val EXTRA_AUTO_SEARCH = "EXTRA_AUTO_SEARCH"

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