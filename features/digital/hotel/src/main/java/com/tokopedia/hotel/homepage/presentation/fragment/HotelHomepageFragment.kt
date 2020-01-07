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
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.banner.Indicator
import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.analytics.TrackingHotelUtil
import com.tokopedia.hotel.common.presentation.HotelBaseFragment
import com.tokopedia.hotel.common.util.HotelUtils
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity
import com.tokopedia.hotel.homepage.data.cloud.entity.HotelPromoEntity
import com.tokopedia.hotel.homepage.di.HotelHomepageComponent
import com.tokopedia.hotel.homepage.presentation.activity.HotelHomepageActivity.Companion.TYPE_PROPERTY
import com.tokopedia.hotel.homepage.presentation.adapter.HotelPromoAdapter
import com.tokopedia.hotel.homepage.presentation.model.HotelHomepageModel
import com.tokopedia.hotel.homepage.presentation.model.viewmodel.HotelHomepageViewModel
import com.tokopedia.hotel.homepage.presentation.widget.HotelRoomAndGuestBottomSheets
import com.tokopedia.hotel.hoteldetail.presentation.activity.HotelDetailActivity
import com.tokopedia.hotel.search.presentation.activity.HotelSearchResultActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.travelcalendar.selectionrangecalendar.SelectionRangeCalendarWidget
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_hotel_homepage.*
import java.util.*
import javax.inject.Inject

/**
 * @author by furqan on 28/03/19
 */
class HotelHomepageFragment : HotelBaseFragment(),
        HotelRoomAndGuestBottomSheets.HotelGuestListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var homepageViewModel: HotelHomepageViewModel

    @Inject
    lateinit var trackingHotelUtil: TrackingHotelUtil

    private var hotelHomepageModel: HotelHomepageModel = HotelHomepageModel()

    private lateinit var promoAdapter: HotelPromoAdapter
    private var promoDataList: List<HotelPromoEntity> = listOf()

    private lateinit var remoteConfig: RemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            homepageViewModel = viewModelProvider.get(HotelHomepageViewModel::class.java)
        }


        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_HOTEL_MODEL)) {
            hotelHomepageModel = savedInstanceState.getParcelable(EXTRA_HOTEL_MODEL)
                    ?: HotelHomepageModel()
        } else if (arguments != null && arguments?.containsKey(EXTRA_PARAM_TYPE) == true) {
            hotelHomepageModel.locId = arguments?.getInt(EXTRA_PARAM_ID) ?: 4712
            hotelHomepageModel.locName = arguments?.getString(EXTRA_PARAM_NAME) ?: "Bali"
            hotelHomepageModel.locType = arguments?.getString(EXTRA_PARAM_TYPE) ?: "region"
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
            if (hotelHomepageModel.checkInDate.isNotBlank() && hotelHomepageModel.checkOutDate.isNotBlank()) hotelHomepageModel.nightCounter = countRoomDuration()
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
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        homepageViewModel.promoData.observe(this, Observer {
            when (it) {
                is Success -> {
                    if (remoteConfig.getBoolean(RemoteConfigKey.CUSTOMER_HOTEL_SHOW_PROMO) && it.data.banners.isNotEmpty()) {
                        renderHotelPromo(it.data.banners)
                    } else {
                        hidePromoContainer()
                    }
                }
            }
        })
    }

    override fun onErrorRetryClicked() {
        loadPromoData()
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
        trackingHotelUtil.hotelSelectRoomGuest(room, adult)

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
                } else {
                    onDestinationChanged(data.getStringExtra(HotelDestinationActivity.HOTEL_DESTINATION_NAME),
                            data.getIntExtra(HotelDestinationActivity.HOTEL_DESTINATION_ID, 0),
                            data.getStringExtra(HotelDestinationActivity.HOTEL_DESTINATION_TYPE))
                }
            }
        }
    }

    private fun initView() {
        val todayWithoutTime = TravelDateUtil.removeTime(TravelDateUtil.getCurrentCalendar().time)
        val tomorrow = TravelDateUtil.addTimeToSpesificDate(todayWithoutTime, Calendar.DATE, 1)
        val dayAfterTomorrow = TravelDateUtil.addTimeToSpesificDate(todayWithoutTime, Calendar.DATE, 2)

        if (hotelHomepageModel.checkInDate.isBlank() && hotelHomepageModel.checkOutDate.isBlank()) {
            hotelHomepageModel.checkInDate = TravelDateUtil.dateToString(
                    TravelDateUtil.YYYY_MM_DD, tomorrow)
            hotelHomepageModel.checkInDateFmt = TravelDateUtil.dateToString(
                    TravelDateUtil.DEFAULT_VIEW_FORMAT, tomorrow)
            hotelHomepageModel.checkOutDate = TravelDateUtil.dateToString(
                    TravelDateUtil.YYYY_MM_DD, dayAfterTomorrow)
            hotelHomepageModel.checkOutDateFmt = TravelDateUtil.dateToString(
                    TravelDateUtil.DEFAULT_VIEW_FORMAT, dayAfterTomorrow)
            hotelHomepageModel.nightCounter = countRoomDuration()
        } else if (hotelHomepageModel.checkInDate.isBlank()) {
            val checkout = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, hotelHomepageModel.checkOutDate)
            val dayBeforeCheckOut = TravelDateUtil.addTimeToSpesificDate(checkout, Calendar.DATE, -1)
            hotelHomepageModel.checkInDate = TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, dayBeforeCheckOut)
            hotelHomepageModel.checkInDateFmt = TravelDateUtil.dateToString(TravelDateUtil.DEFAULT_VIEW_FORMAT, dayBeforeCheckOut)
            hotelHomepageModel.nightCounter = countRoomDuration()

        } else if (hotelHomepageModel.checkOutDate.isBlank()) {
            val checkin = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, hotelHomepageModel.checkInDate)
            val dayAfterCheckIn = TravelDateUtil.addTimeToSpesificDate(checkin, Calendar.DATE, 1)
            hotelHomepageModel.checkOutDate = TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, dayAfterCheckIn)
            hotelHomepageModel.checkOutDateFmt = TravelDateUtil.dateToString(TravelDateUtil.DEFAULT_VIEW_FORMAT, dayAfterCheckIn)
            hotelHomepageModel.nightCounter = countRoomDuration()
        }

        tv_hotel_homepage_destination.setOnClickListener { onDestinationChangeClicked() }
        tv_hotel_homepage_checkin_date.setOnClickListener { configAndRenderCheckInDate() }
        tv_hotel_homepage_checkout_date.setOnClickListener { configAndRenderCheckOutDate() }
        tv_hotel_homepage_guest_info.setOnClickListener { onGuestInfoClicked() }
        btn_hotel_homepage_search.setOnClickListener { onSearchButtonClicked() }
        tv_hotel_homepage_all_promo.setOnClickListener { RouteManager.route(context, ApplinkConst.PROMO_LIST) }

        renderView()
    }

    private fun renderView() {
        tv_hotel_homepage_destination.setText(hotelHomepageModel.locName)
        tv_hotel_homepage_checkin_date.setText(hotelHomepageModel.checkInDateFmt)
        tv_hotel_homepage_checkout_date.setText(hotelHomepageModel.checkOutDateFmt)
        tv_hotel_homepage_night_count.text = hotelHomepageModel.nightCounter.toString()
        tv_hotel_homepage_guest_info.setText(String.format(getString(R.string.hotel_homepage_guest_detail_without_child),
                hotelHomepageModel.roomCount, hotelHomepageModel.adultCount))
    }

    private fun onDestinationChangeClicked() {
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
        trackingHotelUtil.hotelSelectStayDate(hotelHomepageModel.checkInDate, dateRange.toInt())
    }

    private fun onDestinationNearBy(longitude: Double, latitude: Double) {
        hotelHomepageModel.locName = getString(R.string.hotel_homepage_near_by_destination)
        hotelHomepageModel.locLat = latitude
        hotelHomepageModel.locLong = longitude
        hotelHomepageModel.locId = 0
        hotelHomepageModel.locType = ""
        renderView()
    }

    private fun onDestinationChanged(name: String, destinationId: Int, type: String) {
        trackingHotelUtil.hotelSelectDestination(type, name)

        hotelHomepageModel.locName = name
        hotelHomepageModel.locId = destinationId
        hotelHomepageModel.locType = type
        hotelHomepageModel.locLat = 0.0
        hotelHomepageModel.locLong = 0.0
        renderView()
    }

    private fun onSearchButtonClicked() {
        trackingHotelUtil.searchHotel(
                hotelHomepageModel.locType,
                hotelHomepageModel.locName,
                hotelHomepageModel.roomCount,
                hotelHomepageModel.adultCount,
                hotelHomepageModel.checkInDate,
                hotelHomepageModel.nightCounter.toInt()
        )

        context?.run {
            if (hotelHomepageModel.locType.equals(TYPE_PROPERTY, false)) {
                startActivityForResult(HotelDetailActivity.getCallingIntent(this, hotelHomepageModel.checkInDate,
                        hotelHomepageModel.checkOutDate, hotelHomepageModel.locId, hotelHomepageModel.roomCount, hotelHomepageModel.adultCount,
                        hotelHomepageModel.locType, hotelHomepageModel.locName),
                        REQUEST_CODE_DETAIL)
            } else {
                startActivityForResult(HotelSearchResultActivity.createIntent(this, hotelHomepageModel.locName,
                        hotelHomepageModel.locId, hotelHomepageModel.locType, hotelHomepageModel.locLat.toFloat(),
                        hotelHomepageModel.locLong.toFloat(), hotelHomepageModel.checkInDate, hotelHomepageModel.checkOutDate,
                        hotelHomepageModel.roomCount, hotelHomepageModel.adultCount),
                        REQUEST_CODE_SEARCH)
            }
        }
    }

    private fun countRoomDuration(): Long = HotelUtils.countDayDifference(hotelHomepageModel.checkInDate, hotelHomepageModel.checkOutDate)

    private fun loadPromoData() {
        homepageViewModel.getHotelPromo(GraphqlHelper.loadRawString(resources, com.tokopedia.common.travel.R.raw.query_travel_collective_banner))
    }

    private fun renderHotelPromo(promoDataList: List<TravelCollectiveBannerModel.Banner>) {
        showPromoContainer()

        banner_hotel_homepage_promo.setBannerIndicator(Indicator.GREEN)
        banner_hotel_homepage_promo.setOnPromoScrolledListener { position ->
            trackingHotelUtil.hotelBannerImpression(promoDataList.getOrNull(position)
                    ?: TravelCollectiveBannerModel.Banner(), position)
        }
        if (promoDataList.isNotEmpty()) trackingHotelUtil.hotelBannerImpression(promoDataList.first(), 0)

        banner_hotel_homepage_promo.setOnPromoClickListener { position ->
            onPromoClicked(promoDataList.getOrNull(position)
                    ?: TravelCollectiveBannerModel.Banner(), position)
            RouteManager.route(context, promoDataList.getOrNull(position)?.attribute?.appUrl ?: "")
        }

        renderBannerView(promoDataList)
    }

    private fun renderBannerView(bannerList: List<TravelCollectiveBannerModel.Banner>) {
        showPromoContainer()
        val promoUrls = ArrayList<String>()
        for ((_, _, attribute) in bannerList) {
            promoUrls.add(attribute.imageUrl)
        }

        banner_hotel_homepage_promo.setPromoList(promoUrls)
        banner_hotel_homepage_promo.buildView()
        banner_hotel_homepage_promo.bannerSeeAll.hide()
        banner_hotel_homepage_promo.bannerIndicator.hide()
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
        fragmentManager?.run {
            hotelCalendarDialog.show(this, "test")
        }

    }

    private fun showPromoContainer() {
        hotel_container_promo.visibility = View.VISIBLE
    }

    private fun hidePromoContainer() {
        hotel_container_promo.visibility = View.GONE
    }

    fun onPromoClicked(promo: TravelCollectiveBannerModel.Banner, position: Int) {
        trackingHotelUtil.hotelClickBanner(promo, position)
    }

    companion object {
        const val REQUEST_CODE_DESTINATION = 101
        const val REQUEST_CODE_SEARCH = 102
        const val REQUEST_CODE_DETAIL = 103

        const val EXTRA_HOTEL_MODEL = "EXTRA_HOTEL_MODEL"

        const val EXTRA_PARAM_ID = "param_id"
        const val EXTRA_PARAM_NAME = "param_name"
        const val EXTRA_PARAM_TYPE = "param_type"
        const val EXTRA_PARAM_CHECKIN = "param_check_in"
        const val EXTRA_PARAM_CHECKOUT = "param_check_out"
        const val EXTRA_ADULT = "param_adult"
        const val EXTRA_ROOM = "param_room"

        const val TAG_GUEST_INFO = "guestHotelInfo"

        fun getInstance(): HotelHomepageFragment = HotelHomepageFragment()

        fun getInstance(id: Int, name: String, type: String, checkIn: String, checkOut: String, adult: Int, room: Int): HotelHomepageFragment =
                HotelHomepageFragment().also {
                    it.arguments = Bundle().apply {
                        putInt(EXTRA_PARAM_ID, id)
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