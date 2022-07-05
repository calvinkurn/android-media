package com.tokopedia.hotel.globalsearch.presentation.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.hotel.R
import com.tokopedia.hotel.databinding.FragmentHotelGlobalSearchBinding
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelGlobalSearchActivity.Companion.CHECK_IN_DATE
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelGlobalSearchActivity.Companion.CHECK_OUT_DATE
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelGlobalSearchActivity.Companion.EXTRA_CHECK_IN_DATE
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelGlobalSearchActivity.Companion.EXTRA_CHECK_OUT_DATE
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelGlobalSearchActivity.Companion.EXTRA_NUM_OF_GUESTS
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelGlobalSearchActivity.Companion.EXTRA_NUM_OF_ROOMS
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelGlobalSearchActivity.Companion.NUM_OF_GUESTS
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelGlobalSearchActivity.Companion.NUM_OF_ROOMS
import com.tokopedia.hotel.globalsearch.presentation.model.HotelGlobalSearchModel
import com.tokopedia.hotel.homepage.presentation.widget.HotelRoomAndGuestBottomSheets
import com.tokopedia.travelcalendar.selectionrangecalendar.SelectionRangeCalendarWidget
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.date.*
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*

/**
 * @author by furqan on 19/11/2019
 */
open class HotelGlobalSearchFragment : TkpdBaseV4Fragment(),
    HotelRoomAndGuestBottomSheets.HotelGuestListener {

    protected val globalSearchModel: HotelGlobalSearchModel = HotelGlobalSearchModel()
    protected var data: HotelGlobalSearchModel = HotelGlobalSearchModel()
    private var binding by autoClearedNullable<FragmentHotelGlobalSearchBinding>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHotelGlobalSearchBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.let {
            context?.let { ctx ->
                it.tvHotelHomepageCheckinDate.typeface =
                    Typography.getFontType(ctx, false, Typography.DISPLAY_1)
                it.tvHotelHomepageCheckoutDate.typeface =
                    Typography.getFontType(ctx, false, Typography.DISPLAY_1)
                it.tvHotelHomepageGuestInfo.typeface =
                    Typography.getFontType(ctx, false, Typography.DISPLAY_1)
            }

            it.tvHotelHomepageCheckinDate.setOnClickListener { configAndRenderCheckInDate() }
            it.tvHotelHomepageCheckoutDate.setOnClickListener { configAndRenderCheckOutDate() }
            it.tvHotelHomepageGuestInfo.setOnClickListener { onGuestInfoClicked() }
            it.btnHotelHomepageSearch.setOnClickListener { onCheckAvailabilityClicked() }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            globalSearchModel.checkInDate = it.getString(EXTRA_CHECK_IN_DATE)
                ?: DateUtil.getCurrentDate().addTimeToSpesificDate(Calendar.DATE, 1)
                    .toString(DateUtil.YYYY_MM_DD)
            globalSearchModel.checkInDateFmt =
                it.getString(EXTRA_CHECK_IN_DATE).toDate(DateUtil.YYYY_MM_DD)
                    .toString(DateUtil.DEFAULT_VIEW_FORMAT)
            globalSearchModel.checkOutDate = it.getString(EXTRA_CHECK_OUT_DATE)
                ?: DateUtil.getCurrentDate().addTimeToSpesificDate(Calendar.DATE, 2)
                    .toString(DateUtil.YYYY_MM_DD)
            globalSearchModel.checkOutDateFmt =
                it.getString(EXTRA_CHECK_OUT_DATE).toDate(DateUtil.YYYY_MM_DD)
                    .toString(DateUtil.DEFAULT_VIEW_FORMAT)
            globalSearchModel.numOfGuests = it.getInt(EXTRA_NUM_OF_GUESTS)
            globalSearchModel.numOfRooms = it.getInt(EXTRA_NUM_OF_ROOMS)

            globalSearchModel.nightCount = countRoomDuration()

            renderView()
        }
    }

    override fun getScreenName(): String = ""

    override fun onSaveGuest(room: Int, adult: Int) {
        globalSearchModel.numOfRooms = room
        globalSearchModel.numOfGuests = adult
        renderView()
    }

    open fun renderView() {
        data = globalSearchModel

        val todayWithoutTime = DateUtil.getCurrentCalendar().time.removeTime()
        val tomorrow = todayWithoutTime.addTimeToSpesificDate(Calendar.DATE, 1)
        val dayAfterTomorrow = todayWithoutTime.addTimeToSpesificDate(Calendar.DATE, 2)

        // check in date is less than today
        if (todayWithoutTime.after(data.checkInDate.toDate(DateUtil.YYYY_MM_DD))) {
            globalSearchModel.checkInDate = tomorrow.toString(DateUtil.YYYY_MM_DD)
            globalSearchModel.checkInDateFmt = tomorrow.toString(DateUtil.DEFAULT_VIEW_FORMAT)

            if (tomorrow.after(data.checkOutDate.toDate(DateUtil.YYYY_MM_DD))) {
                globalSearchModel.checkOutDate = dayAfterTomorrow.toString(DateUtil.YYYY_MM_DD)
                globalSearchModel.checkOutDateFmt =
                    dayAfterTomorrow.toString(DateUtil.DEFAULT_VIEW_FORMAT)
            }
        }

        binding?.let {
            it.tvHotelHomepageCheckinDate.setText(data.checkInDateFmt)
            it.tvHotelHomepageCheckoutDate.setText(data.checkOutDateFmt)
            it.tvHotelHomepageNightCount.text = data.nightCount.toString()
            it.tvHotelHomepageGuestInfo.setText(
                String.format(
                    getString(R.string.hotel_homepage_guest_detail_without_child),
                    data.numOfRooms, data.numOfGuests
                )
            )
        }
    }

    protected fun countRoomDuration(): Long =
        DateUtil.getDayDiff(globalSearchModel.checkInDate, globalSearchModel.checkOutDate)

    fun configAndRenderCheckInDate() {
        openCalendarDialog(globalSearchModel.checkInDate, globalSearchModel.checkOutDate)
    }

    fun configAndRenderCheckOutDate() {
        openCalendarDialog(checkIn = globalSearchModel.checkInDate)
    }

    fun onGuestInfoClicked() {
        activity?.let {
            val hotelRoomAndGuestBottomSheets = HotelRoomAndGuestBottomSheets()
            hotelRoomAndGuestBottomSheets.listener = this
            hotelRoomAndGuestBottomSheets.roomCount = globalSearchModel.numOfRooms
            hotelRoomAndGuestBottomSheets.adultCount = globalSearchModel.numOfGuests
            hotelRoomAndGuestBottomSheets.show(it.supportFragmentManager, TAG_GUEST_INFO)
        }

    }

    private fun onCheckInDateChanged(newCheckInDate: Date) {
        globalSearchModel.checkInDate = newCheckInDate.toString(DateUtil.YYYY_MM_DD)
        globalSearchModel.checkInDateFmt = newCheckInDate.toString(DateUtil.DEFAULT_VIEW_FORMAT)

        if (newCheckInDate.after(globalSearchModel.checkOutDate.toDate(DateUtil.YYYY_MM_DD))) {
            val tomorrow = newCheckInDate.addTimeToSpesificDate(Calendar.DATE, 1)
            globalSearchModel.checkOutDate = tomorrow.toString(DateUtil.YYYY_MM_DD)
            globalSearchModel.checkOutDateFmt = tomorrow.toString(DateUtil.DEFAULT_VIEW_FORMAT)
        }
        globalSearchModel.nightCount = countRoomDuration()

        renderView()
    }

    private fun onCheckOutDateChanged(newCheckOutDate: Date) {
        globalSearchModel.checkOutDate = newCheckOutDate.toString(DateUtil.YYYY_MM_DD)
        globalSearchModel.checkOutDateFmt = newCheckOutDate.toString(DateUtil.DEFAULT_VIEW_FORMAT)
        globalSearchModel.nightCount = countRoomDuration()

        renderView()
    }

    private fun openCalendarDialog(checkIn: String? = null, checkOut: String? = null) {
        val minSelectDateFromToday = SelectionRangeCalendarWidget.DEFAULT_MIN_SELECTED_DATE_TODAY

        val hotelCalendarDialog = SelectionRangeCalendarWidget.getInstance(
            checkIn,
            checkOut,
            SelectionRangeCalendarWidget.DEFAULT_RANGE_CALENDAR_YEAR,
            SelectionRangeCalendarWidget.DEFAULT_RANGE_DATE_SELECTED_ONE_MONTH.toLong(),
            getString(R.string.hotel_min_date_label),
            getString(R.string.hotel_max_date_label),
            minSelectDateFromToday
        )

        hotelCalendarDialog.listener = object : SelectionRangeCalendarWidget.OnDateClickListener {
            override fun onDateClick(dateIn: Date, dateOut: Date) {
                onCheckInDateChanged(dateIn)
                onCheckOutDateChanged(dateOut)
            }
        }
        hotelCalendarDialog.listenerMaxRange =
            object : SelectionRangeCalendarWidget.OnNotifyMaxRange {
                override fun onNotifyMax() {
                    Toast.makeText(
                        context,
                        R.string.hotel_calendar_error_max_range,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        parentFragmentManager.let { hotelCalendarDialog.show(it, TAG_RANGE_DATE_CALENDAR) }
    }

    open fun onCheckAvailabilityClicked() {
        val intent = Intent().apply {
            putExtra(CHECK_IN_DATE, globalSearchModel.checkInDate)
            putExtra(CHECK_OUT_DATE, globalSearchModel.checkOutDate)
            putExtra(NUM_OF_GUESTS, globalSearchModel.numOfGuests)
            putExtra(NUM_OF_ROOMS, globalSearchModel.numOfRooms)
        }

        activity?.setResult(RESULT_OK, intent)
        activity?.finish()
    }

    companion object {

        const val TAG_GUEST_INFO = "guestHotelInfo"
        const val TAG_RANGE_DATE_CALENDAR = "rangeDateCalendar"

        fun getInstance(
            checkInDate: String,
            checkOutDate: String,
            numOfGuests: Int,
            numOfRooms: Int
        ) =
            HotelGlobalSearchFragment().also {
                it.arguments = Bundle().apply {
                    putString(EXTRA_CHECK_IN_DATE, checkInDate)
                    putString(EXTRA_CHECK_OUT_DATE, checkOutDate)
                    putInt(EXTRA_NUM_OF_GUESTS, numOfGuests)
                    putInt(EXTRA_NUM_OF_ROOMS, numOfRooms)
                }
            }
    }

}