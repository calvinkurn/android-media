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
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.hotel.R
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity
import com.tokopedia.hotel.homepage.data.cloud.entity.HotelPromoEntity
import com.tokopedia.hotel.homepage.di.HotelHomepageComponent
import com.tokopedia.hotel.homepage.presentation.adapter.HotelPromoAdapter
import com.tokopedia.hotel.homepage.presentation.model.HotelHomepageModel
import com.tokopedia.hotel.homepage.presentation.model.viewmodel.HotelHomepageViewModel
import com.tokopedia.hotel.homepage.presentation.widget.HotelRoomAndGuestBottomSheets
import com.tokopedia.hotel.search.presentation.activity.HotelSearchResultActivity
import com.tokopedia.travelcalendar.view.bottomsheet.TravelCalendarBottomSheet
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_hotel_homepage.*
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author by furqan on 28/03/19
 */
class HotelHomepageFragment : BaseDaggerFragment(), HotelRoomAndGuestBottomSheets.HotelGuestListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var homepageViewModel: HotelHomepageViewModel

    private var hotelHomepageModel: HotelHomepageModel = HotelHomepageModel()

    private lateinit var promoAdapter: HotelPromoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            homepageViewModel = viewModelProvider.get(HotelHomepageViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_homepage, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_HOTEL_MODEL)) {
            hotelHomepageModel = savedInstanceState.getParcelable(EXTRA_HOTEL_MODEL)!!
        }

        initView()
        hidePromoContainer()
        loadPromoData()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        homepageViewModel.promoData.observe(this, Observer {
            when(it) {
                is Success -> {
                    if (it.data.size > 0) {
                        renderHotelPromo(it.data)
                    } else {
                        hidePromoContainer()
                    }
                }
                is Fail -> {}
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(EXTRA_HOTEL_MODEL, hotelHomepageModel)
    }

    override fun initInjector() {
        getComponent(HotelHomepageComponent::class.java).inject(this)
    }

    override fun getScreenName(): String = ""

    override fun onSaveGuest(room: Int, adult: Int, child: Int) {
        hotelHomepageModel.roomCount = room
        hotelHomepageModel.adultCount = adult
        hotelHomepageModel.childCount = child

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
        hotelHomepageModel.nightCounter = countNightDifference()

        tv_hotel_homepage_destination.setOnClickListener { onDestinationChangeClicked() }
        tv_hotel_homepage_checkin_date.setOnClickListener { configAndRenderCheckInDate() }
        tv_hotel_homepage_checkout_date.setOnClickListener { configAndRenderCheckOutDate() }
        tv_hotel_homepage_guest_info.setOnClickListener { onGuestInfoClicked() }
        btn_hotel_homepage_search.setOnClickListener { onSearchButtonClicked() }

        renderView()
    }

    private fun renderView() {
        tv_hotel_homepage_destination.setText(hotelHomepageModel.locName)
        tv_hotel_homepage_checkin_date.setText(hotelHomepageModel.checkInDateFmt)
        tv_hotel_homepage_checkout_date.setText(hotelHomepageModel.checkOutDateFmt)
        tv_hotel_homepage_night_count.text = hotelHomepageModel.nightCounter.toString()
        tv_hotel_homepage_guest_info.setText(String.format(getString(R.string.hotel_homepage_guest_detail),
                hotelHomepageModel.roomCount, hotelHomepageModel.adultCount, hotelHomepageModel.childCount))
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
        hotelHomepageModel.nightCounter = countNightDifference()

        renderView()
    }

    private fun onCheckOutDateChanged(newCheckOutDate: Date) {
        hotelHomepageModel.checkOutDate = TravelDateUtil.dateToString(
                TravelDateUtil.YYYY_MM_DD, newCheckOutDate)
        hotelHomepageModel.checkOutDateFmt = TravelDateUtil.dateToString(
                TravelDateUtil.DEFAULT_VIEW_FORMAT, newCheckOutDate)
        hotelHomepageModel.nightCounter = countNightDifference()

        renderView()
    }

    private fun onDestinationNearBy(longitude: Double, latitude: Double) {
        hotelHomepageModel.locName = getString(R.string.hotel_homepage_near_by_destination)
        hotelHomepageModel.locLat = latitude
        hotelHomepageModel.locLong = longitude
        renderView()
    }

    private fun onDestinationChanged(name: String, destinationId: Int, type: String) {
        hotelHomepageModel.locName = name
        hotelHomepageModel.locId = destinationId
        hotelHomepageModel.locType = type
        renderView()
    }

    private fun onSearchButtonClicked() {
            startActivityForResult(HotelSearchResultActivity.createIntent(activity!!, hotelHomepageModel.locName,
                    hotelHomepageModel.locId, hotelHomepageModel.locType, hotelHomepageModel.locLat.toFloat(),
                    hotelHomepageModel.locLong.toFloat(), hotelHomepageModel.checkInDate, hotelHomepageModel.checkOutDate,
                    hotelHomepageModel.roomCount, hotelHomepageModel.adultCount, hotelHomepageModel.childCount),
                    REQUEST_CODE_SEARCH)
    }

    private fun countNightDifference(): Long =
        (TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, hotelHomepageModel.checkOutDate).time -
                TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, hotelHomepageModel.checkOutDate).time) / ONE_DAY

    private fun loadPromoData() {
        homepageViewModel.getHotelPromo(GraphqlHelper.loadRawString(resources, R.raw.gql_query_hotel_home_promo))
    }

    private fun renderHotelPromo(promoDataList: List<HotelPromoEntity>) {
        showPromoContainer()
        if (!::promoAdapter.isInitialized) {
            promoAdapter = HotelPromoAdapter(promoDataList)
        }

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

    companion object {
        val ONE_DAY: Long = TimeUnit.DAYS.toMillis(1)
        val MAX_SELECTION_DATE = 30
        val DEFAULT_LAST_HOUR_IN_DAY = 23
        val DEFAULT_LAST_MIN_SEC_IN_DAY = 59

        val REQUEST_CODE_DESTINATION = 101
        val REQUEST_CODE_SEARCH = 102

        val EXTRA_HOTEL_MODEL = "EXTRA_HOTEL_MODEL"

        val TAG_CALENDAR_CHECK_IN = "calendarHotelCheckIn"
        val TAG_CALENDAR_CHECK_OUT = "calendarHotelCheckOut"
        val TAG_GUEST_INFO = "guestHotelInfo"

        fun getInstance(): HotelHomepageFragment = HotelHomepageFragment()
    }
}