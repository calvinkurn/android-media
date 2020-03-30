package com.tokopedia.flight.dashboardV2.presentation.fragment

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
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.DeeplinkMapper.getRegisteredNavigation
import com.tokopedia.applink.RouteManager
import com.tokopedia.banner.Indicator
import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.view.activity.FlightAirportPickerActivity
import com.tokopedia.flight.airport.view.fragment.FlightAirportPickerFragment
import com.tokopedia.flight.airport.view.model.FlightAirportModel
import com.tokopedia.flight.dashboard.view.activity.FlightClassesActivity
import com.tokopedia.flight.dashboard.view.activity.FlightSelectPassengerActivity
import com.tokopedia.flight.dashboard.view.fragment.model.FlightClassModel
import com.tokopedia.flight.dashboard.view.fragment.model.FlightDashboardModel
import com.tokopedia.flight.dashboard.view.fragment.model.FlightPassengerModel
import com.tokopedia.flight.dashboardV2.di.FlightDashboardV2Component
import com.tokopedia.flight.dashboardV2.presentation.viewmodel.FlightDashboardViewModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataModel
import com.tokopedia.flight.search_universal.presentation.widget.FlightSearchFormView
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_flight_dashboard_new.*
import java.util.*
import javax.inject.Inject

/**
 * @author by furqan on 27/03/2020
 */
class FlightDashboardV2Fragment : BaseDaggerFragment(), FlightSearchFormView.FlightSearchFormListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var flightDashboardViewModel: FlightDashboardViewModel

    override fun getScreenName(): String = FlightDashboardV2Fragment::class.java.simpleName

    override fun initInjector() {
        getComponent(FlightDashboardV2Component::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            flightDashboardViewModel = viewModelProvider.get(FlightDashboardViewModel::class.java)
            flightDashboardViewModel.fetchBannerData(GraphqlHelper.loadRawString(resources, com.tokopedia.common.travel.R.raw.query_travel_collective_banner), true)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        flightDashboardViewModel.bannerList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    renderBannerView(it.data.banners)
                }
                is Fail -> {
                    hideBannerView()
                }
            }
        })

        flightDashboardViewModel.dashboardData.observe(viewLifecycleOwner, Observer {
            renderSearchForm(it)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_flight_dashboard_new, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        flightDashboardSearchForm.listener = this
    }

    override fun onDepartureAirportClicked() {
        val intent = FlightAirportPickerActivity.createInstance(requireContext(), getString(R.string.flight_airportpicker_departure_title))
        startActivityForResult(intent, REQUEST_CODE_AIRPORT_DEPARTURE)
    }

    override fun onDestinationAirportClicked() {
        val intent = FlightAirportPickerActivity.createInstance(requireContext(), getString(R.string.flight_airportpicker_arrival_title))
        startActivityForResult(intent, REQUEST_CODE_AIRPORT_DESTINATION)
    }

    override fun onDepartureDateClicked(departureAirport: String, arrivalAirport: String, flightClassId: Int, departureDate: Date, returnDate: Date, isRoundTrip: Boolean) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onReturnDateClicked(departureDate: Date, returnDate: Date) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPassengerClicked(passengerModel: FlightPassengerModel?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClassClicked(flightClassId: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSaveSearch(flightSearchData: FlightSearchPassDataModel) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_AIRPORT_DEPARTURE -> {
                    val departureAirport = data?.getParcelableExtra<FlightAirportModel>(FlightAirportPickerFragment.EXTRA_SELECTED_AIRPORT)
                    departureAirport?.let {
                        flightDashboardViewModel.onDepartureAirportChanged(it)
                    }
                }
                REQUEST_CODE_AIRPORT_DESTINATION -> {
                    val destinationAirport = data?.getParcelableExtra<FlightAirportModel>(FlightAirportPickerFragment.EXTRA_SELECTED_AIRPORT)
                    destinationAirport?.let {
                        flightDashboardViewModel.onArrivalAirportChanged(it)
                    }
                }
                REQUEST_CODE_SELECT_CLASSES -> {
                    val classModel = data?.getParcelableExtra<FlightClassModel>(FlightClassesActivity.EXTRA_FLIGHT_CLASS)
                    classModel?.let {
                        flightDashboardViewModel.onClassChanged(it)
                    }
                }
                REQUEST_CODE_SELECT_PASSENGER -> {
                    val passengerModel = data?.getParcelableExtra<FlightPassengerModel>(FlightSelectPassengerActivity.EXTRA_PASS_DATA)
                    passengerModel?.let {
                        flightDashboardViewModel.onPassengerChanged(it)
                    }
                }
            }
        }
    }

    private fun renderBannerView(bannerList: List<TravelCollectiveBannerModel.Banner>) {
        if (bannerList.isNotEmpty()) {
            showBannerView()
            flightDashboardBanner.customWidth = resources.getDimensionPixelSize(R.dimen.banner_width)
            flightDashboardBanner.customHeight = resources.getDimensionPixelSize(R.dimen.banner_height)
            flightDashboardBanner.setBannerSeeAllTextColor(resources.getColor(com.tokopedia.unifycomponents.R.color.Green_G500))
            flightDashboardBanner.setBannerIndicator(Indicator.GREEN)
            flightDashboardBanner.setOnPromoScrolledListener { position ->
                // send impression if banner not null
            }
            flightDashboardBanner.setOnPromoClickListener { position -> onBannerClicked(position) }
            flightDashboardBanner.setOnPromoAllClickListener { onAllBannerClicked() }

            val bannerUrls = arrayListOf<String>()
            for (banner in bannerList) {
                bannerUrls.add(banner.attribute.imageUrl)
            }
            flightDashboardBanner.setPromoList(bannerUrls)
            flightDashboardBanner.buildView()
            KeyboardHandler.hideSoftKeyboard(requireActivity())
            KeyboardHandler.DropKeyboard(requireContext(), requireView())
        } else {
            hideBannerView()
        }
    }

    private fun showBannerView() {
        flightDashboardBannerLayout.visibility = View.VISIBLE
        flightDashboardBanner.visibility = View.VISIBLE
    }

    private fun hideBannerView() {
        flightDashboardBannerLayout.visibility = View.GONE
        flightDashboardBanner.visibility = View.GONE
    }

    private fun onBannerClicked(position: Int) {
        context?.let {
            val bannerData = flightDashboardViewModel.getBannerData(position)
            bannerData?.let { banner ->
                flightDashboardViewModel.onBannerClicked(position, banner)
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
            flightDashboardSearchForm.setOriginAirport(it)
        }
        dashboardData.arrivalAirport?.let {
            flightDashboardSearchForm.setDestinationAirport(it)
        }
        dashboardData.flightPassengerViewModel?.let {
            flightDashboardSearchForm.setPassengerView(it)
        }
        dashboardData.flightClass?.let {
            flightDashboardSearchForm.setClassView(it)
        }
    }

    companion object {

        private const val EXTRA_FROM_DEEPLINK_URL = "EXTRA_FROM_DEEPLINK_URL"
        private const val EXTRA_TRIP = "EXTRA_TRIP"
        private const val EXTRA_ADULT = "EXTRA_ADULT"
        private const val EXTRA_CHILD = "EXTRA_CHILD"
        private const val EXTRA_INFANT = "EXTRA_INFANT"
        private const val EXTRA_CLASS = "EXTRA_CLASS"
        private const val EXTRA_AUTO_SEARCH = "EXTRA_AUTO_SEARCH"

        const val REQUEST_CODE_AIRPORT_DEPARTURE = 1
        const val REQUEST_CODE_AIRPORT_DESTINATION = 2
        const val REQUEST_CODE_SELECT_PASSENGER = 3
        const val REQUEST_CODE_SELECT_CLASSES = 4

        fun getInstance(linkUrl: String): FlightDashboardV2Fragment =
                FlightDashboardV2Fragment().also {
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
                        linkUrl: String): FlightDashboardV2Fragment =
                FlightDashboardV2Fragment().also {
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