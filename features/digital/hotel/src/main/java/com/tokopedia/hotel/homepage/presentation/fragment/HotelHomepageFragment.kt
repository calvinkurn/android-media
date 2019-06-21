package com.tokopedia.hotel.homepage.presentation.fragment

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
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
import com.tokopedia.travelcalendar.view.bottomsheet.TravelCalendarBottomSheet
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_hotel_homepage.*
import java.util.*
import javax.inject.Inject

/**
 * @author by furqan on 28/03/19
 */
class HotelHomepageFragment : HotelBaseFragment(),
        HotelRoomAndGuestBottomSheets.HotelGuestListener, HotelPromoAdapter.PromoClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var homepageViewModel: HotelHomepageViewModel

    @Inject
    lateinit var trackingHotelUtil: TrackingHotelUtil

    private var hotelHomepageModel: HotelHomepageModel = HotelHomepageModel()

    private lateinit var promoAdapter: HotelPromoAdapter
    private var promoDataList: List<HotelPromoEntity> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            homepageViewModel = viewModelProvider.get(HotelHomepageViewModel::class.java)
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_HOTEL_MODEL)) {
            hotelHomepageModel = savedInstanceState.getParcelable(EXTRA_HOTEL_MODEL)!!
        } else if (arguments != null && arguments!!.containsKey(EXTRA_PARAM_TYPE)) {
            hotelHomepageModel.locId = arguments!!.getInt(EXTRA_PARAM_ID)
            hotelHomepageModel.locName = arguments!!.getString(EXTRA_PARAM_NAME)
            hotelHomepageModel.locType = arguments!!.getString(EXTRA_PARAM_TYPE)
        }
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
                    if (it.data.size > 0) {
                        promoDataList = it.data
                        renderHotelPromo(promoDataList)
                    } else {
                        hidePromoContainer()
                    }
                }
                is Fail -> {
                    showErrorState(it.throwable)
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
        trackingHotelUtil.hotelSelectRoomGuest(room, adult, 0)

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

        hotelHomepageModel.checkInDate = TravelDateUtil.dateToString(
                TravelDateUtil.YYYY_MM_DD, tomorrow)
        hotelHomepageModel.checkInDateFmt = TravelDateUtil.dateToString(
                TravelDateUtil.DEFAULT_VIEW_FORMAT, tomorrow)
        hotelHomepageModel.checkOutDate = TravelDateUtil.dateToString(
                TravelDateUtil.YYYY_MM_DD, dayAfterTomorrow)
        hotelHomepageModel.checkOutDateFmt = TravelDateUtil.dateToString(
                TravelDateUtil.DEFAULT_VIEW_FORMAT, dayAfterTomorrow)
        hotelHomepageModel.nightCounter = countRoomDuration()

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
        startActivityForResult(HotelDestinationActivity.createInstance(activity!!), REQUEST_CODE_DESTINATION)
    }

    private fun configAndRenderCheckInDate() {
        val minDate = TravelDateUtil.removeTime(TravelDateUtil.addTimeToSpesificDate(
                TravelDateUtil.getCurrentCalendar().time, Calendar.DATE, 1))

        val maxDate = TravelDateUtil.addTimeToSpesificDate(TravelDateUtil.getCurrentCalendar().time,
                Calendar.YEAR, 1)
        val maxDateCalendar = TravelDateUtil.getCurrentCalendar()
        maxDateCalendar.time = maxDate
        maxDateCalendar.set(Calendar.HOUR_OF_DAY, DEFAULT_LAST_HOUR_IN_DAY)
        maxDateCalendar.set(Calendar.MINUTE, DEFAULT_LAST_MIN_SEC_IN_DAY)
        maxDateCalendar.set(Calendar.SECOND, DEFAULT_LAST_MIN_SEC_IN_DAY)

        val selectedDate = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, hotelHomepageModel.checkInDate)

        renderTravelCalendar(selectedDate, minDate, maxDateCalendar.time, getString(R.string.hotel_check_in_calendar_title), TAG_CALENDAR_CHECK_IN)
    }

    private fun configAndRenderCheckOutDate() {
        val minDate = TravelDateUtil.addTimeToSpesificDate(TravelDateUtil.stringToDate(
                TravelDateUtil.YYYY_MM_DD, hotelHomepageModel.checkInDate), Calendar.DATE, 1)

        val maxDate = TravelDateUtil.addTimeToSpesificDate(TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD,
                hotelHomepageModel.checkInDate), Calendar.DATE, MAX_SELECTION_DATE)
        val maxDateCalendar = TravelDateUtil.getCurrentCalendar()
        maxDateCalendar.time = maxDate
        maxDateCalendar.set(Calendar.HOUR_OF_DAY, DEFAULT_LAST_HOUR_IN_DAY)
        maxDateCalendar.set(Calendar.MINUTE, DEFAULT_LAST_MIN_SEC_IN_DAY)
        maxDateCalendar.set(Calendar.SECOND, DEFAULT_LAST_MIN_SEC_IN_DAY)

        val selectedDate = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, hotelHomepageModel.checkOutDate)

        renderTravelCalendar(selectedDate, minDate, maxDateCalendar.time, getString(R.string.hotel_check_out_calendar_title), TAG_CALENDAR_CHECK_OUT)
    }

    private fun renderTravelCalendar(selectedDate: Date, minDate: Date, maxDate: Date, title: String, calendarTag: String) {
        val travelCalendarBottomSheet = TravelCalendarBottomSheet.Builder()
                .setMinDate(minDate)
                .setMaxDate(maxDate)
                .setSelectedDate(selectedDate)
                .setShowHoliday(true)
                .setTitle(title)
                .build()
        travelCalendarBottomSheet.setListener(object : TravelCalendarBottomSheet.ActionListener {
            override fun onClickDate(dateSelected: Date) {
                val calendarSelected = Calendar.getInstance()
                calendarSelected.time = dateSelected
                if (calendarTag == TAG_CALENDAR_CHECK_IN) {
                    onCheckInDateChanged(dateSelected)
                    configAndRenderCheckOutDate()
                } else if (calendarTag == TAG_CALENDAR_CHECK_OUT) {
                    onCheckOutDateChanged(dateSelected)
                }
            }
        })
        travelCalendarBottomSheet.show(activity!!.supportFragmentManager, calendarTag)
    }

    private fun onGuestInfoClicked() {
        val hotelRoomAndGuestBottomSheets = HotelRoomAndGuestBottomSheets()
        hotelRoomAndGuestBottomSheets.listener = this
        hotelRoomAndGuestBottomSheets.roomCount = hotelHomepageModel.roomCount
        hotelRoomAndGuestBottomSheets.adultCount = hotelHomepageModel.adultCount
        hotelRoomAndGuestBottomSheets.show(activity!!.supportFragmentManager, TAG_GUEST_INFO)
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

        trackRoomDates()
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
        val dayDiff = HotelUtils.countCurrentDayDifference(hotelHomepageModel.checkInDate)
        val dateRange = "${hotelHomepageModel.checkInDate} - ${hotelHomepageModel.checkOutDate}"
        trackingHotelUtil.hotelSelectStayDate(dayDiff.toInt(), dateRange)
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
        trackingHotelUtil.hotelSelectDestination(name)

        hotelHomepageModel.locName = name
        hotelHomepageModel.locId = destinationId
        hotelHomepageModel.locType = type
        hotelHomepageModel.locLat = 0.0
        hotelHomepageModel.locLong = 0.0
        renderView()
    }

    private fun onSearchButtonClicked() {
        trackingHotelUtil.searchHotel(
                "",
                hotelHomepageModel.locName,
                hotelHomepageModel.roomCount,
                hotelHomepageModel.adultCount,
                HotelUtils.countCurrentDayDifference(hotelHomepageModel.checkInDate).toInt(),
                hotelHomepageModel.nightCounter.toInt()
        )

        if (hotelHomepageModel.locType.equals(TYPE_PROPERTY, false)) {
            startActivityForResult(HotelDetailActivity.getCallingIntent(activity!!, hotelHomepageModel.checkInDate,
                    hotelHomepageModel.checkOutDate, hotelHomepageModel.locId, hotelHomepageModel.roomCount, hotelHomepageModel.adultCount),
                    REQUEST_CODE_DETAIL)
        } else {
            startActivityForResult(HotelSearchResultActivity.createIntent(activity!!, hotelHomepageModel.locName,
                    hotelHomepageModel.locId, hotelHomepageModel.locType, hotelHomepageModel.locLat.toFloat(),
                    hotelHomepageModel.locLong.toFloat(), hotelHomepageModel.checkInDate, hotelHomepageModel.checkOutDate,
                    hotelHomepageModel.roomCount, hotelHomepageModel.adultCount),
                    REQUEST_CODE_SEARCH)
        }
    }

    private fun countRoomDuration(): Long = HotelUtils.countDayDifference(hotelHomepageModel.checkInDate, hotelHomepageModel.checkOutDate)

    private fun loadPromoData() {
        homepageViewModel.getHotelPromo(GraphqlHelper.loadRawString(resources, R.raw.gql_query_hotel_home_promo))
    }

    private fun renderHotelPromo(promoDataList: List<HotelPromoEntity>) {
//        trackingHotelUtil.hotelBannerImpression("hotel", mapToHotelPromotionsView(promoDataList))

        showPromoContainer()
        if (!::promoAdapter.isInitialized) {
            promoAdapter = HotelPromoAdapter(promoDataList)
        }
        promoAdapter.promoClickListener = this

        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rv_hotel_homepage_promo.layoutManager = layoutManager
        rv_hotel_homepage_promo.setHasFixedSize(true)
        rv_hotel_homepage_promo.isNestedScrollingEnabled = false
        rv_hotel_homepage_promo.adapter = promoAdapter
    }

    private fun showPromoContainer() {
        hotel_container_promo.visibility = View.VISIBLE
    }

    private fun hidePromoContainer() {
        hotel_container_promo.visibility = View.GONE
    }

//    private fun mapToHotelPromotionsView(data: List<HotelPromoEntity>): List<TrackingHotelUtil.HotelPromotionsView> {
//        return data.mapIndexed { index, it ->
//            TrackingHotelUtil.HotelPromotionsView(
//                    bannerId = it.promoId,
//                    position = index,
//                    bannerName = it.attributes.description
//            )
//        }
//    }
//
    override fun onPromoClicked(promo: HotelPromoEntity) {
//        trackingHotelUtil.hotelClickBanner(promo.attributes.description, mapToHotelPromotionsClick(promoDataList))
    }
//
//    private fun mapToHotelPromotionsClick(data: List<HotelPromoEntity>): List<TrackingHotelUtil.HotelPromotionsClick> {
//        return data.mapIndexed { index, it ->
//            TrackingHotelUtil.HotelPromotionsClick(
//                    bannerId = it.promoId,
//                    position = index,
//                    bannerName = it.attributes.description,
//                    promoId = it.promoId,
//                    promoCode = it.attributes.promoCode
//            )
//        }
//    }

    companion object {
        const val MAX_SELECTION_DATE = 30
        const val DEFAULT_LAST_HOUR_IN_DAY = 23
        const val DEFAULT_LAST_MIN_SEC_IN_DAY = 59

        const val REQUEST_CODE_DESTINATION = 101
        const val REQUEST_CODE_SEARCH = 102
        const val REQUEST_CODE_DETAIL = 103

        const val EXTRA_HOTEL_MODEL = "EXTRA_HOTEL_MODEL"

        const val EXTRA_PARAM_ID = "param_id"
        const val EXTRA_PARAM_NAME = "param_name"
        const val EXTRA_PARAM_TYPE = "param_type"

        const val TAG_CALENDAR_CHECK_IN = "calendarHotelCheckIn"
        const val TAG_CALENDAR_CHECK_OUT = "calendarHotelCheckOut"
        const val TAG_GUEST_INFO = "guestHotelInfo"

        fun getInstance(): HotelHomepageFragment = HotelHomepageFragment()

        fun getInstance(id: Int, name: String, type: String): HotelHomepageFragment =
                HotelHomepageFragment().also {
                    it.arguments = Bundle().apply {
                        putInt(EXTRA_PARAM_ID, id)
                        putString(EXTRA_PARAM_NAME, name)
                        putString(EXTRA_PARAM_TYPE, type)
                    }
                }
    }
}