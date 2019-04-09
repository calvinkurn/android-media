package com.tokopedia.hotel.homepage.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.hotel.R
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity
import com.tokopedia.hotel.homepage.di.HotelHomepageComponent
import com.tokopedia.hotel.homepage.presentation.model.HotelHomepageModel
import com.tokopedia.hotel.homepage.presentation.widget.HotelRoomAndGuestBottomSheets
import com.tokopedia.travelcalendar.view.bottomsheet.TravelCalendarBottomSheet
import kotlinx.android.synthetic.main.fragment_hotel_homepage.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author by furqan on 28/03/19
 */
class HotelHomepageFragment : BaseDaggerFragment(), HotelRoomAndGuestBottomSheets.HotelGuestListener {

    private val hotelHomepageModel: HotelHomepageModel = HotelHomepageModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_homepage, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
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

    override fun showGuestErrorTicker(resId: Int) {
        NetworkErrorHelper.showRedCloseSnackbar(activity, getString(resId))
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
        hotelHomepageModel.nightCounter = (dayAfterTomorrow.time - tomorrow.time) / ONE_DAY

        tv_hotel_homepage_destination.setOnClickListener { onDestinationChangeClicked() }
        tv_hotel_homepage_checkin_date.setOnClickListener { configAndRenderCheckInDate() }
        tv_hotel_homepage_checkout_date.setOnClickListener { configAndRenderCheckOutDate() }
        tv_hotel_homepage_guest_info.setOnClickListener { onGuestInfoClicked() }

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
                Calendar.DATE, MAX_SELECTION_DATE)
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

        renderView()
    }

    private fun onCheckOutDateChanged(newCheckOutDate: Date) {
        hotelHomepageModel.checkInDate = TravelDateUtil.dateToString(
                TravelDateUtil.YYYY_MM_DD, newCheckOutDate)
        hotelHomepageModel.checkInDateFmt = TravelDateUtil.dateToString(
                TravelDateUtil.DEFAULT_VIEW_FORMAT, newCheckOutDate)

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

    companion object {
        val ONE_DAY: Long = TimeUnit.DAYS.toMillis(1)
        val MAX_SELECTION_DATE = 30
        val DEFAULT_LAST_HOUR_IN_DAY = 23
        val DEFAULT_LAST_MIN_SEC_IN_DAY = 59

        val REQUEST_CODE_DESTINATION = 101

        val TAG_CALENDAR_CHECK_IN = "calendarHotelCheckIn"
        val TAG_CALENDAR_CHECK_OUT = "calendarHotelCheckOut"
        val TAG_GUEST_INFO = "guestHotelInfo"

        fun getInstance(): HotelHomepageFragment = HotelHomepageFragment()
    }
}