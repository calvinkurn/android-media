package com.tokopedia.hotel.homepage.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.DeeplinkMapper.getRegisteredNavigation
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTravel
import com.tokopedia.banner.Indicator
import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.common.travel.data.entity.TravelRecentSearchModel
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.analytics.TrackingHotelUtil
import com.tokopedia.hotel.common.data.HotelSourceEnum
import com.tokopedia.hotel.common.data.HotelTypeEnum
import com.tokopedia.hotel.common.presentation.HotelBaseFragment
import com.tokopedia.hotel.common.util.HotelUtils
import com.tokopedia.hotel.common.util.TRACKING_HOTEL_HOMEPAGE
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity
import com.tokopedia.hotel.homepage.data.cloud.entity.HotelPropertyDefaultHome
import com.tokopedia.hotel.homepage.di.HotelHomepageComponent
import com.tokopedia.hotel.homepage.presentation.activity.HotelHomepageActivity.Companion.HOMEPAGE_SCREEN_NAME
import com.tokopedia.hotel.homepage.presentation.adapter.HotelLastSearchAdapter
import com.tokopedia.hotel.homepage.presentation.adapter.viewholder.HotelLastSearchViewHolder
import com.tokopedia.hotel.homepage.presentation.model.HotelHomepageModel
import com.tokopedia.hotel.homepage.presentation.model.HotelRecentSearchModel
import com.tokopedia.hotel.homepage.presentation.model.viewmodel.HotelHomepageViewModel
import com.tokopedia.hotel.homepage.presentation.widget.HotelRoomAndGuestBottomSheets
import com.tokopedia.hotel.hoteldetail.presentation.activity.HotelDetailActivity
import com.tokopedia.hotel.search.data.model.HotelSearchModel
import com.tokopedia.hotel.search.presentation.activity.HotelSearchResultActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.travelcalendar.selectionrangecalendar.SelectionRangeCalendarWidget
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_hotel_homepage.*
import java.util.*
import javax.inject.Inject

/**
 * @author by furqan on 28/03/19
 */
class HotelHomepageFragment : HotelBaseFragment(),
        HotelRoomAndGuestBottomSheets.HotelGuestListener,
        HotelLastSearchViewHolder.LastSearchListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var homepageViewModel: HotelHomepageViewModel

    @Inject
    lateinit var trackingHotelUtil: TrackingHotelUtil
    private var performanceMonitoring: PerformanceMonitoring? = null
    private var isTraceStop = false

    private var hotelHomepageModel: HotelHomepageModel = HotelHomepageModel()

    private lateinit var remoteConfig: RemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        performanceMonitoring = PerformanceMonitoring.start(TRACKING_HOTEL_HOMEPAGE)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            homepageViewModel = viewModelProvider.get(HotelHomepageViewModel::class.java)
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_HOTEL_MODEL)) {
            hotelHomepageModel = savedInstanceState.getParcelable(EXTRA_HOTEL_MODEL)
                    ?: HotelHomepageModel()
        } else if (arguments != null) {
            //for applink with param searchId and searchType
            if (arguments?.containsKey(EXTRA_PARAM_SEARCH_ID) == true) {
                hotelHomepageModel.searchId = arguments?.getString(EXTRA_PARAM_SEARCH_ID) ?: ""
                hotelHomepageModel.searchType = arguments?.getString(EXTRA_PARAM_SEARCH_TYPE) ?: ""

                hotelHomepageModel.locId = 0
                hotelHomepageModel.locType = ""
            }

            //for older applink
            if (arguments?.containsKey(EXTRA_PARAM_ID) == true) {
                hotelHomepageModel.locId = arguments?.getLong(EXTRA_PARAM_ID) ?: 0
                hotelHomepageModel.locType = arguments?.getString(EXTRA_PARAM_TYPE) ?: ""
            }

            hotelHomepageModel.locName = arguments?.getString(EXTRA_PARAM_NAME) ?: ""
            hotelHomepageModel.adultCount = arguments?.getInt(EXTRA_ADULT, 1) ?: 1
            hotelHomepageModel.roomCount = arguments?.getInt(EXTRA_ROOM, 1) ?: 1
            hotelHomepageModel.checkInDate = arguments?.getString(EXTRA_PARAM_CHECKIN) ?: ""
            hotelHomepageModel.checkOutDate = arguments?.getString(EXTRA_PARAM_CHECKOUT) ?: ""

            if (hotelHomepageModel.checkInDate.isNotBlank()) {
                hotelHomepageModel.checkInDateFmt =
                        TravelDateUtil.dateToString(TravelDateUtil.DEFAULT_VIEW_FORMAT,
                                TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, hotelHomepageModel.checkInDate))
            }
            if (hotelHomepageModel.checkOutDate.isNotBlank()) {
                hotelHomepageModel.checkOutDateFmt =
                        TravelDateUtil.dateToString(TravelDateUtil.DEFAULT_VIEW_FORMAT,
                                TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, hotelHomepageModel.checkOutDate))
            }
            if (hotelHomepageModel.checkInDate.isNotBlank() && hotelHomepageModel.checkOutDate.isNotBlank())
                hotelHomepageModel.nightCounter = countRoomDuration()
        }

        remoteConfig = FirebaseRemoteConfigImpl(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_homepage, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        hidePromoContainer()
        loadPromoData()
        if (hotelHomepageModel.locName.isEmpty()) {
            homepageViewModel.getDefaultHomepageParameter(GraphqlHelper.loadRawString(resources, R.raw.gql_query_hotel_get_default_homepage_parameter))
        }
    }

    override fun onResume() {
        super.onResume()

        // last search need to reload every time user back to homepage
        hideHotelLastSearchContainer()
        loadRecentSearchData()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        homepageViewModel.homepageDefaultParam.observe(viewLifecycleOwner, Observer {
            renderHotelParam(it)
        })

        homepageViewModel.promoData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
//                    if (remoteConfig.getBoolean(RemoteConfigKey.CUSTOMER_HOTEL_SHOW_PROMO) && it.data.banners.isNotEmpty()) {
                    if (it.data.banners.isNotEmpty()) {
                        renderHotelPromo(it.data.banners)
                    } else {
                        hidePromoContainer()
                    }
                }
            }
            stopTrace()
        })

        homepageViewModel.recentSearch.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    renderHotelLastSearch(it.data)
                }
            }
        })

        homepageViewModel.deleteRecentSearch.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (it.data) {
                        loadRecentSearchData()
                    }
                }
            }
        })
    }

    private fun renderHotelParam(hotelPropertyDefaultHome: HotelPropertyDefaultHome) {
        hotelHomepageModel.checkInDate = hotelPropertyDefaultHome.checkIn
        hotelHomepageModel.checkOutDate = hotelPropertyDefaultHome.checkOut
        checkCheckInAndCheckOutDate()
        hotelHomepageModel.roomCount = hotelPropertyDefaultHome.totalRoom
        hotelHomepageModel.adultCount = hotelPropertyDefaultHome.totalGuest
        onDestinationChanged(hotelPropertyDefaultHome.label, searchId = hotelPropertyDefaultHome.searchId,
                searchType = hotelPropertyDefaultHome.searchType)
    }

    override fun onErrorRetryClicked() {
        loadPromoData()
    }

    private fun stopTrace() {
        if (!isTraceStop) {
            performanceMonitoring?.stopTrace()
            isTraceStop = true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(EXTRA_HOTEL_MODEL, hotelHomepageModel)
    }

    override fun initInjector() {
        getComponent(HotelHomepageComponent::class.java).inject(this)
    }

    override fun getScreenName(): String = ""

    override fun onSaveGuest(room: Int, adult: Int) {
        trackingHotelUtil.hotelSelectRoomGuest(context, room, adult, HOMEPAGE_SCREEN_NAME)

        hotelHomepageModel.roomCount = room
        hotelHomepageModel.adultCount = adult

        renderView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_DESTINATION -> if (resultCode == Activity.RESULT_OK && data != null) {
                if (data.hasExtra(HotelDestinationActivity.HOTEL_CURRENT_LOCATION_LANG)) {
                    onDestinationNearBy(data.getDoubleExtra(HotelDestinationActivity.HOTEL_CURRENT_LOCATION_LANG, 0.0),
                            data.getDoubleExtra(HotelDestinationActivity.HOTEL_CURRENT_LOCATION_LAT, 0.0))
                } else if (data.hasExtra(HotelDestinationActivity.HOTEL_DESTINATION_SEARCH_ID)) {
                    onDestinationChanged(data.getStringExtra(HotelDestinationActivity.HOTEL_DESTINATION_NAME) ?: "",
                            searchId = data.getStringExtra(HotelDestinationActivity.HOTEL_DESTINATION_SEARCH_ID) ?: "",
                            searchType = data.getStringExtra(HotelDestinationActivity.HOTEL_DESTINATION_SEARCH_TYPE) ?: "")
                }
            }
        }
    }

    override fun onItemBind(item: TravelRecentSearchModel.Item, position: Int) {
        trackingHotelUtil.hotelLastSearchImpression(context, item, position, HOMEPAGE_SCREEN_NAME)
    }

    override fun onItemClick(item: TravelRecentSearchModel.Item, position: Int) {
        trackingHotelUtil.hotelLastSearchClick(context, item, position, HOMEPAGE_SCREEN_NAME)
    }

    private fun initView() {
        checkCheckInAndCheckOutDate()
        tv_hotel_homepage_destination.setOnClickListener { onDestinationChangeClicked() }
        tv_hotel_homepage_checkin_date.setOnClickListener { configAndRenderCheckInDate() }
        tv_hotel_homepage_checkout_date.setOnClickListener { configAndRenderCheckOutDate() }
        tv_hotel_homepage_guest_info.setOnClickListener { onGuestInfoClicked() }
        btn_hotel_homepage_search.setOnClickListener { onSearchButtonClicked() }
        tv_hotel_homepage_all_promo.setOnClickListener { RouteManager.route(context, ApplinkConstInternalTravel.HOTEL_PROMO_LIST) }

        renderView()
    }

    private fun checkCheckInAndCheckOutDate() {
        val updatedCheckInCheckOutDate = HotelUtils.validateCheckInAndCheckOutDate(hotelHomepageModel.checkInDate, hotelHomepageModel.checkOutDate)
        hotelHomepageModel.checkInDate = updatedCheckInCheckOutDate.first
        hotelHomepageModel.checkOutDate = updatedCheckInCheckOutDate.second
        hotelHomepageModel.checkInDateFmt = TravelDateUtil.dateToString(TravelDateUtil.DEFAULT_VIEW_FORMAT, TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, hotelHomepageModel.checkInDate))
        hotelHomepageModel.checkOutDateFmt = TravelDateUtil.dateToString(TravelDateUtil.DEFAULT_VIEW_FORMAT, TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, hotelHomepageModel.checkOutDate))
        hotelHomepageModel.nightCounter = countRoomDuration()
    }

    private fun renderView() {
        btn_hotel_homepage_search.isEnabled = hotelHomepageModel.locName.isNotEmpty()
        tv_hotel_homepage_destination.setText(hotelHomepageModel.locName)
        tv_hotel_homepage_checkin_date.setText(hotelHomepageModel.checkInDateFmt)
        tv_hotel_homepage_checkout_date.setText(hotelHomepageModel.checkOutDateFmt)
        tv_hotel_homepage_night_count.text = hotelHomepageModel.nightCounter.toString()
        tv_hotel_homepage_guest_info.setText(String.format(getString(R.string.hotel_homepage_guest_detail_without_child),
                hotelHomepageModel.roomCount, hotelHomepageModel.adultCount))
    }

    private fun onDestinationChangeClicked() {
        trackingHotelUtil.hotelClickChangeDestination(context, HOMEPAGE_SCREEN_NAME)
        context?.run {
            startActivityForResult(HotelDestinationActivity.createInstance(this), REQUEST_CODE_DESTINATION)
        }
        activity?.overridePendingTransition(com.tokopedia.common.travel.R.anim.travel_slide_up_in,
                com.tokopedia.common.travel.R.anim.travel_anim_stay)
    }

    private fun configAndRenderCheckInDate() {
        openCalendarDialog(hotelHomepageModel.checkInDate, hotelHomepageModel.checkOutDate)
    }

    private fun configAndRenderCheckOutDate() {
        openCalendarDialog(checkIn = hotelHomepageModel.checkInDate)
    }

    private fun onGuestInfoClicked() {
        activity?.let {
            val hotelRoomAndGuestBottomSheets = HotelRoomAndGuestBottomSheets()
            hotelRoomAndGuestBottomSheets.listener = this
            hotelRoomAndGuestBottomSheets.roomCount = hotelHomepageModel.roomCount
            hotelRoomAndGuestBottomSheets.adultCount = hotelHomepageModel.adultCount
            hotelRoomAndGuestBottomSheets.show(it.supportFragmentManager, TAG_GUEST_INFO)
        }

    }

    private fun onCheckInDateChanged(newCheckInDate: Date) {
        hotelHomepageModel.checkInDate = TravelDateUtil.dateToString(
                TravelDateUtil.YYYY_MM_DD, newCheckInDate)
        hotelHomepageModel.checkInDateFmt = TravelDateUtil.dateToString(
                TravelDateUtil.DEFAULT_VIEW_FORMAT, newCheckInDate)

        if (newCheckInDate >= TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, hotelHomepageModel.checkOutDate)) {
            val tomorrow = TravelDateUtil.addTimeToSpesificDate(newCheckInDate,
                    Calendar.DATE, 1)
            hotelHomepageModel.checkOutDate = TravelDateUtil.dateToString(
                    TravelDateUtil.YYYY_MM_DD, tomorrow)
            hotelHomepageModel.checkOutDateFmt = TravelDateUtil.dateToString(
                    TravelDateUtil.DEFAULT_VIEW_FORMAT, tomorrow)
        }
        hotelHomepageModel.nightCounter = countRoomDuration()

        renderView()
    }

    private fun onCheckOutDateChanged(newCheckOutDate: Date) {
        hotelHomepageModel.checkOutDate = TravelDateUtil.dateToString(
                TravelDateUtil.YYYY_MM_DD, newCheckOutDate)
        hotelHomepageModel.checkOutDateFmt = TravelDateUtil.dateToString(
                TravelDateUtil.DEFAULT_VIEW_FORMAT, newCheckOutDate)
        hotelHomepageModel.nightCounter = countRoomDuration()

        trackRoomDates()
        renderView()
    }

    private fun trackRoomDates() {
        val dateRange = HotelUtils.countDayDifference(hotelHomepageModel.checkInDate, hotelHomepageModel.checkOutDate)
        trackingHotelUtil.hotelSelectStayDate(context, hotelHomepageModel.checkInDate, dateRange.toInt(), HOMEPAGE_SCREEN_NAME)
    }

    private fun onDestinationNearBy(longitude: Double, latitude: Double) {
        hotelHomepageModel.locName = getString(R.string.hotel_homepage_near_by_destination)
        hotelHomepageModel.locLat = latitude
        hotelHomepageModel.locLong = longitude
        hotelHomepageModel.locId = 0
        hotelHomepageModel.locType = ""
        hotelHomepageModel.searchType = HotelTypeEnum.COORDINATE.value
        hotelHomepageModel.searchId = ""
        renderView()
    }

    private fun onDestinationChanged(name: String, id: Long = 0, type: String = "", searchType: String = "", searchId: String = "") {
        val tempType = if (searchType.isNotEmpty()) searchType else type
        trackingHotelUtil.hotelSelectDestination(context, tempType, name, HOMEPAGE_SCREEN_NAME)

        hotelHomepageModel.locName = name
        hotelHomepageModel.locId = id
        hotelHomepageModel.locType = type
        hotelHomepageModel.locLat = 0.0
        hotelHomepageModel.locLong = 0.0
        hotelHomepageModel.searchType = searchType
        hotelHomepageModel.searchId = searchId
        renderView()
    }

    private fun onSearchButtonClicked() {
        val type = if (hotelHomepageModel.searchType.isNotEmpty()) hotelHomepageModel.searchType
        else hotelHomepageModel.locType
        trackingHotelUtil.searchHotel(context,
                type,
                hotelHomepageModel.locName,
                hotelHomepageModel.roomCount,
                hotelHomepageModel.adultCount,
                hotelHomepageModel.checkInDate,
                hotelHomepageModel.nightCounter.toInt(),
                HOMEPAGE_SCREEN_NAME
        )

        context?.run {
            when {
                hotelHomepageModel.locType.equals(HotelTypeEnum.PROPERTY.value, false) -> {
                    startActivityForResult(HotelDetailActivity.getCallingIntent(this,
                            hotelHomepageModel.checkInDate,
                            hotelHomepageModel.checkOutDate,
                            hotelHomepageModel.locId,
                            hotelHomepageModel.roomCount,
                            hotelHomepageModel.adultCount,
                            hotelHomepageModel.locType,
                            hotelHomepageModel.locName,
                            source = HotelSourceEnum.HOMEPAGE.value),
                            REQUEST_CODE_DETAIL)
                }

                hotelHomepageModel.searchType.equals(HotelTypeEnum.PROPERTY.value, false) -> {
                    startActivityForResult(HotelDetailActivity.getCallingIntent(this,
                            hotelHomepageModel.checkInDate,
                            hotelHomepageModel.checkOutDate,
                            hotelHomepageModel.searchId.toLong(),
                            hotelHomepageModel.roomCount,
                            hotelHomepageModel.adultCount,
                            hotelHomepageModel.searchType,
                            hotelHomepageModel.locName,
                            source = HotelSourceEnum.HOMEPAGE.value),
                            REQUEST_CODE_DETAIL)
                }

                else -> {
                    val hotelSearchModel = HotelSearchModel(name = hotelHomepageModel.locName,
                            id = hotelHomepageModel.locId,
                            type = hotelHomepageModel.locType,
                            lat = hotelHomepageModel.locLat.toFloat(),
                            long = hotelHomepageModel.locLong.toFloat(),
                            checkIn = hotelHomepageModel.checkInDate,
                            checkOut = hotelHomepageModel.checkOutDate,
                            room = hotelHomepageModel.roomCount,
                            adult = hotelHomepageModel.adultCount,
                            searchType = hotelHomepageModel.searchType,
                            searchId = hotelHomepageModel.searchId)
                    startActivityForResult(HotelSearchResultActivity.createIntent(this, hotelSearchModel), REQUEST_CODE_SEARCH)
                }
            }
        }
    }

    private fun countRoomDuration(): Long = HotelUtils.countDayDifference(hotelHomepageModel.checkInDate, hotelHomepageModel.checkOutDate)

    private fun loadPromoData() {
        homepageViewModel.getHotelPromo(GraphqlHelper.loadRawString(resources, com.tokopedia.common.travel.R.raw.query_travel_collective_banner))
    }

    private fun loadRecentSearchData() {
        homepageViewModel.getRecentSearch(GraphqlHelper.loadRawString(resources, R.raw.gql_query_hotel_recent_search))
    }

    private fun renderHotelPromo(promoDataList: List<TravelCollectiveBannerModel.Banner>) {
        showPromoContainer()
        banner_hotel_homepage_promo.setBannerIndicator(Indicator.GREEN)

        banner_hotel_homepage_promo.setOnPromoScrolledListener {
            trackingHotelUtil.hotelBannerImpression(context, promoDataList.getOrNull(it), it, HOMEPAGE_SCREEN_NAME)
        }

        banner_hotel_homepage_promo.setOnPromoClickListener { position ->
            onPromoClicked(promoDataList.getOrNull(position)
                    ?: TravelCollectiveBannerModel.Banner(), position)

            if (RouteManager.isSupportApplink(context, promoDataList.getOrNull(position)?.attribute?.appUrl
                            ?: "")) {
                RouteManager.route(context, promoDataList.getOrNull(position)?.attribute?.appUrl
                        ?: "")
            } else if (!getRegisteredNavigation(context!!, promoDataList.getOrNull(position)?.attribute?.appUrl
                            ?: "").isEmpty()) {
                RouteManager.route(context, getRegisteredNavigation(context!!, promoDataList.getOrNull(position)?.attribute?.appUrl
                        ?: ""))
            } else if ((promoDataList.getOrNull(position)?.attribute?.webUrl ?: "").isNotEmpty()) {
                RouteManager.route(context, promoDataList.getOrNull(position)?.attribute?.webUrl)
            }
        }

        renderBannerView(promoDataList)
    }

    private fun renderBannerView(bannerList: List<TravelCollectiveBannerModel.Banner>) {
        showPromoContainer()
        val promoUrls = ArrayList<String>()
        for ((_, _, attribute) in bannerList) {
            promoUrls.add(attribute.imageUrl)
        }

        banner_hotel_homepage_promo.customWidth = resources.getDimensionPixelSize(R.dimen.hotel_banner_width)
        banner_hotel_homepage_promo.customHeight = resources.getDimensionPixelSize(R.dimen.hotel_banner_height)
        banner_hotel_homepage_promo.setPromoList(promoUrls)
        banner_hotel_homepage_promo.buildView()
        banner_hotel_homepage_promo.bannerSeeAll.hide()
        banner_hotel_homepage_promo.bannerIndicator.hide()
        if (bannerList.size > 1) {
            banner_hotel_homepage_promo.bannerIndicator.show()
        } else {
            banner_hotel_homepage_promo.bannerIndicator.hide()
        }
    }

    private fun renderHotelLastSearch(data: HotelRecentSearchModel) {
        if (data.items.isEmpty()) {
            hideHotelLastSearchContainer()
            return
        }

        showHotelLastSearchContainer()

        tv_hotel_last_search_title.text = data.title
        tv_hotel_homepage_delete_last_search.setOnClickListener {
            homepageViewModel.deleteRecentSearch(GraphqlHelper.loadRawString(resources, R.raw.gql_mutation_hotel_delete_recent_search))
        }
        rv_hotel_homepage_last_search.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        rv_hotel_homepage_last_search.adapter = HotelLastSearchAdapter(data.items, this)
    }

    private fun openCalendarDialog(checkIn: String? = null, checkOut: String? = null) {
        var minSelectDateFromToday = SelectionRangeCalendarWidget.DEFAULT_MIN_SELECTED_DATE_TODAY
        if (!(remoteConfig.getBoolean(RemoteConfigKey.CUSTOMER_HOTEL_BOOK_FOR_TODAY, true))) minSelectDateFromToday = SelectionRangeCalendarWidget.DEFAULT_MIN_SELECTED_DATE_PLUS_1_DAY

        val hotelCalendarDialog = SelectionRangeCalendarWidget.getInstance(checkIn, checkOut, SelectionRangeCalendarWidget.DEFAULT_RANGE_CALENDAR_YEAR,
                SelectionRangeCalendarWidget.DEFAULT_RANGE_DATE_SELECTED_ONE_MONTH.toLong(),
                getString(R.string.hotel_min_date_label), getString(R.string.hotel_max_date_label), minSelectDateFromToday)

        hotelCalendarDialog.listener = object : SelectionRangeCalendarWidget.OnDateClickListener {
            override fun onDateClick(dateIn: Date, dateOut: Date) {
                onCheckInDateChanged(dateIn)
                onCheckOutDateChanged(dateOut)
            }
        }
        hotelCalendarDialog.listenerMaxRange = object : SelectionRangeCalendarWidget.OnNotifyMaxRange {
            override fun onNotifyMax() {
                Toast.makeText(context, R.string.hotel_calendar_error_max_range, Toast.LENGTH_SHORT).show()
            }
        }
        fragmentManager?.let { hotelCalendarDialog.show(it, "test") }
    }

    private fun showPromoContainer() {
        hotel_container_promo.visibility = View.VISIBLE
    }

    private fun hidePromoContainer() {
        hotel_container_promo.visibility = View.GONE
    }

    private fun onPromoClicked(promo: TravelCollectiveBannerModel.Banner, position: Int) {
        trackingHotelUtil.hotelClickBanner(context, promo, position, HOMEPAGE_SCREEN_NAME)
    }

    private fun showHotelLastSearchContainer() {
        hotel_container_last_search.visibility = View.VISIBLE
    }

    private fun hideHotelLastSearchContainer() {
        hotel_container_last_search.visibility = View.GONE
    }

    companion object {
        const val REQUEST_CODE_DESTINATION = 101
        const val REQUEST_CODE_SEARCH = 102
        const val REQUEST_CODE_DETAIL = 103

        const val EXTRA_HOTEL_MODEL = "EXTRA_HOTEL_MODEL"

        const val EXTRA_PARAM_SEARCH_ID = "param_search_id"
        const val EXTRA_PARAM_SEARCH_TYPE = "param_search_type"
        const val EXTRA_PARAM_NAME = "param_name"
        const val EXTRA_PARAM_CHECKIN = "param_check_in"
        const val EXTRA_PARAM_CHECKOUT = "param_check_out"
        const val EXTRA_ADULT = "param_adult"
        const val EXTRA_ROOM = "param_room"

        //for older applink
        const val EXTRA_PARAM_ID = "param_id"
        const val EXTRA_PARAM_TYPE = "param_type"

        const val TAG_GUEST_INFO = "guestHotelInfo"

        fun getInstance(): HotelHomepageFragment = HotelHomepageFragment()

        fun getInstance(searchId: String, name: String, searchType: String, checkIn: String, checkOut: String, adult: Int, room: Int): HotelHomepageFragment =
                HotelHomepageFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_PARAM_SEARCH_ID, searchId)
                        putString(EXTRA_PARAM_NAME, name)
                        putString(EXTRA_PARAM_SEARCH_TYPE, searchType)
                        if (checkIn.isNotBlank()) putString(EXTRA_PARAM_CHECKIN, checkIn)
                        if (checkOut.isNotBlank()) putString(EXTRA_PARAM_CHECKOUT, checkOut)
                        if (adult != 0) putInt(EXTRA_ADULT, adult)
                        if (room != 0) putInt(EXTRA_ROOM, room)
                    }
                }

        // for older applink
        fun getInstance(id: Long, name: String, type: String, checkIn: String, checkOut: String, adult: Int, room: Int): HotelHomepageFragment =
                HotelHomepageFragment().also {
                    it.arguments = Bundle().apply {
                        putLong(EXTRA_PARAM_ID, id)
                        putString(EXTRA_PARAM_NAME, name)
                        putString(EXTRA_PARAM_TYPE, type)
                        if (checkIn.isNotBlank()) putString(EXTRA_PARAM_CHECKIN, checkIn)
                        if (checkOut.isNotBlank()) putString(EXTRA_PARAM_CHECKOUT, checkOut)
                        if (adult != 0) putInt(EXTRA_ADULT, adult)
                        if (room != 0) putInt(EXTRA_ROOM, room)
                    }
                }
    }
}