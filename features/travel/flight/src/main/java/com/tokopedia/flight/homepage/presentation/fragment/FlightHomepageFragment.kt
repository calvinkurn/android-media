package com.tokopedia.flight.homepage.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.common.travel.presentation.model.TravelVideoBannerModel
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
import com.tokopedia.common.travel.widget.TravelVideoBannerWidget
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.presentation.bottomsheet.FlightAirportPickerBottomSheet
import com.tokopedia.flight.airport.presentation.model.FlightAirportModel
import com.tokopedia.flight.common.constant.FlightUrl
import com.tokopedia.flight.common.util.FlightAnalyticsScreenName
import com.tokopedia.flight.databinding.FragmentFlightHomepageBinding
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
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.travelcalendar.selectionrangecalendar.SelectionRangeCalendarWidget
import com.tokopedia.travelcalendar.singlecalendar.SinglePickCalendarWidget
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.toString
import com.tokopedia.utils.lifecycle.autoClearedNullable
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
    private var binding by autoClearedNullable<FragmentFlightHomepageBinding>()
    private var timerFlight = Timer()

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
        bannerWidthInPixels = (displayMetrics.widthPixels / BANNER_SHOW_SIZE).toInt()
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
                binding?.flightHomepageSearchForm?.autoSearch()
            }
        })

    }

    override fun onResume() {
        super.onResume()
        flightHomepageViewModel.fetchBannerData(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFlightHomepageBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.flightHomepageSearchForm?.listener = this
        binding?.flightHomepageSearchForm?.setDate(false)

        if (applinkErrorTextResource != -1) {
            showMessageErrorInSnackbar(applinkErrorTextResource)
        }

        if (::flightHomepageViewModel.isInitialized)
            flightHomepageViewModel.sendTrackingOpenScreen(FlightAnalyticsScreenName.HOMEPAGE)
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

    override fun onReverseAirportClicked(departureAirport: FlightAirportModel, arrivalAirport: FlightAirportModel) {
        flightHomepageViewModel.onReverseAirportChanged(departureAirport, arrivalAirport)
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
                    minMaxDate.first.toString(DateUtil.YYYY_MM_DD),
                    minMaxDate.second.toString(DateUtil.YYYY_MM_DD),
                    departureDate.toString(DateUtil.YYYY_MM_DD),
                    departureAirport,
                    arrivalAirport,
                    flightClassId
            )
            flightCalendarDialog.setListener(object : SinglePickCalendarWidget.ActionListener {
                override fun onDateSelected(dateSelected: Date) {
                    val errorResourceId = flightHomepageViewModel.validateDepartureDate(dateSelected)
                    if (errorResourceId == -1) {
                        binding?.flightHomepageSearchForm?.setDepartureDate(dateSelected)
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
            binding?.flightHomepageSearchForm?.init()
        }

    }

    override fun onVideoBannerClicked(bannerData: TravelVideoBannerModel) {
        flightHomepageViewModel.sendTrackingVideoBannerClick(bannerData)
    }

    private fun renderVideoBannerView(bannerData: TravelCollectiveBannerModel) {
        binding?.flightHomepageVideoBanner?.listener = this
        binding?.flightHomepageVideoBanner?.setData(bannerData)
        binding?.flightHomepageVideoBanner?.build()
        binding?.flightHomepageVideoBanner?.getData()?.let {
            flightHomepageViewModel.sendTrackingVideoBannerImpression(
                it
            )
        }
    }

    private fun hideVideoBannerView() {
        binding?.flightHomepageVideoBanner?.hideTravelVideoBanner()
    }

    private fun renderBannerTitle(title: String) {
        if (title.isNotEmpty()) {
            binding?.flightHomepageBannerTitle?.text = title
            binding?.flightHomepageBannerTitle?.visibility = View.VISIBLE
        } else {
            binding?.flightHomepageBannerTitle?.visibility = View.GONE
        }
    }

    private fun renderBannerView(bannerList: List<TravelCollectiveBannerModel.Banner>) {
        if (bannerList.isNotEmpty()) {
            showBannerView()
            binding?.flightHomepageAllPromo?.setOnClickListener { onAllBannerClicked() }
            binding?.flightHomepageBanner?.apply {
                timer = Timer()
                freeMode = false
                centerMode = true
                slideToScroll = 1
                indicatorPosition = CarouselUnify.INDICATOR_BL

                if (bannerList.size == 1) {
                    autoplay = false
                    infinite = false
                    slideToShow = 1.0f
                    setMargin(left = 12.toPx(), top = 8.toPx(), bottom = 0, right = 12.toPx())
                } else {
                    slideToShow = BANNER_SHOW_SIZE
                    timerFlight.scheduleAtFixedRate(object : TimerTask() {
                        override fun run() {
                            Handler(Looper.getMainLooper()).post {
                                nextSlide()
                            }
                        }
                    }, autoplayDuration, autoplayDuration)
                    autoplay = false
                    infinite = true
                }
                onActiveIndexChangedListener = object : CarouselUnify.OnActiveIndexChangedListener {
                    override fun onActiveIndexChanged(prev: Int, current: Int) {
                        flightHomepageViewModel.sendTrackingPromoScrolled(current)
                    }
                }
                val itemParam = { view: View, data: Any ->
                    data as TravelCollectiveBannerModel.Banner

                    val image = view.findViewById<ImageUnify>(R.id.flightHomepagePromoImageCarousel)
                    if (bannerWidthInPixels > 0) {
                        image.layoutParams.height = measureBannerHeightBasedOnRatio()
                        image.layoutParams.width = bannerWidthInPixels
                    } else {
                        image.layoutParams.height = resources.getDimensionPixelSize(R.dimen.banner_height)
                        image.layoutParams.width = resources.getDimensionPixelSize(R.dimen.banner_width)
                    }
                    image.loadImage(data.attribute.imageUrl)
                    image.setOnClickListener {
                        onBannerClicked(data.position)
                    }
                }
                bannerList.forEachIndexed { index, banner -> banner.position = index }
                addItems(R.layout.flight_homepage_carousel_item, ArrayList(bannerList), itemParam)
            }
        }
    }

    private fun showBannerView() {
        binding?.flightHomepageBannerLayout?.visibility = View.VISIBLE
    }

    private fun hideBannerView() {
        binding?.flightHomepageBannerLayout?.visibility = View.GONE
    }

    private fun renderTickerView(travelTickerModel: TravelTickerModel) {
        binding?.flightHomepageTicker?.setTextDescription(travelTickerModel.message)
        binding?.flightHomepageTicker?.tickerType = Ticker.TYPE_WARNING
        binding?.flightHomepageTicker?.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                if (linkUrl.isNotEmpty()) {
                    RouteManager.route(context, linkUrl.toString())
                }
            }

            override fun onDismiss() {}

        })
        if (travelTickerModel.url.isNotEmpty()) {
            binding?.flightHomepageTicker?.setOnClickListener {
                RouteManager.route(requireContext(), travelTickerModel.url)
            }
        }

        showTickerView()
    }

    private fun showTickerView() {
        binding?.flightHomepageTicker?.visibility = View.VISIBLE
    }

    private fun hideTickerView() {
        binding?.flightHomepageTicker?.visibility = View.GONE
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
        RouteManager.route(context, FlightUrl.FLIGHT_PROMO_APPLINK)
    }

    private fun renderSearchForm(homepageData: FlightHomepageModel) {
        homepageData.departureAirport?.let {
            binding?.flightHomepageSearchForm?.setOriginAirport(it)
        }
        homepageData.arrivalAirport?.let {
            binding?.flightHomepageSearchForm?.setDestinationAirport(it)
        }
        homepageData.flightPassengerViewModel?.let {
            binding?.flightHomepageSearchForm?.setPassengerView(it)
        }
        homepageData.flightClass?.let {
            binding?.flightHomepageSearchForm?.setClassView(it)
        }
    }

    private fun setCalendarDatePicker(selectedDate: Date?, minDate: Date, maxDate: Date, title: String, tag: String,
                                      departureCode: String, arrivalCode: String,
                                      classFlight: Int) {
        val minDateStr = minDate.toString(DateUtil.YYYY_MM_DD)
        val maxDateStr = maxDate.toString(DateUtil.YYYY_MM_DD)

        val selectedDateStr = selectedDate?.toString(DateUtil.YYYY_MM_DD)

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
                    binding?.flightHomepageSearchForm?.setDepartureDate(dateIn)

                    val returnErrorResourceId = flightHomepageViewModel.validateReturnDate(dateIn, dateOut)
                    if (returnErrorResourceId == -1) {
                        binding?.flightHomepageSearchForm?.setReturnDate(dateOut)
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

    override fun onDestroy() {
        timerFlight.cancel()
        super.onDestroy()
    }

    companion object {
        // Banner Ratio = 414 : 139
        private const val BANNER_WIDTH_RATIO = 414f
        private const val BANNER_HEIGHT_RATIO = 139f
        private const val BANNER_SHOW_SIZE = 1.1f

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