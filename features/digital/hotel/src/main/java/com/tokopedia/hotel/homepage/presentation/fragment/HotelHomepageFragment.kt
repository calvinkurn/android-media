package com.tokopedia.hotel.homepage.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.hotel.R
import com.tokopedia.hotel.homepage.di.HotelHomepageComponent
import com.tokopedia.hotel.homepage.presentation.model.HotelHomepageModel
import com.tokopedia.travelcalendar.view.bottomsheet.TravelCalendarBottomSheet
import kotlinx.android.synthetic.main.fragment_hotel_homepage.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author by furqan on 28/03/19
 */
class HotelHomepageFragment : BaseDaggerFragment() {

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

        tv_hotel_homepage_checkin_date.setOnClickListener { configAndRenderCheckInDate() }
        tv_hotel_homepage_checkout_date.setOnClickListener { configAndRenderCheckOutDate() }

        renderView()
    }

    private fun renderView() {
        tv_hotel_homepage_checkin_date.setText(hotelHomepageModel.checkInDateFmt)
        tv_hotel_homepage_checkout_date.setText(hotelHomepageModel.checkOutDateFmt)
        tv_hotel_homepage_night_count.text = hotelHomepageModel.nightCounter.toString()
        tv_hotel_homepage_guest_info.setText(String.format(getString(R.string.hotel_homepage_guest_detail),
                hotelHomepageModel.roomCount, hotelHomepageModel.adultCount, hotelHomepageModel.childCount))
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

        val maxDate = TravelDateUtil.addTimeToSpesificDate(TravelDateUtil.getCurrentCalendar().time,
                Calendar.DATE, MAX_SELECTION_DATE)
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
                    // TODO add action if check in date selected
                } else if (calendarTag == TAG_CALENDAR_CHECK_OUT) {
                    // TODO add action if check out date selected
                }
            }
        })
        travelCalendarBottomSheet.show(activity!!.supportFragmentManager, calendarTag)
    }

    companion object {
        val ONE_DAY: Long = TimeUnit.DAYS.toMillis(1)
        val MAX_SELECTION_DATE = 30
        val DEFAULT_LAST_HOUR_IN_DAY = 23
        val DEFAULT_LAST_MIN_SEC_IN_DAY = 59
        val TAG_CALENDAR_CHECK_IN = "calendarHotelCheckIn"
        val TAG_CALENDAR_CHECK_OUT = "calendarHotelCheckOut"

        fun getInstance(): HotelHomepageFragment = HotelHomepageFragment()
    }
}