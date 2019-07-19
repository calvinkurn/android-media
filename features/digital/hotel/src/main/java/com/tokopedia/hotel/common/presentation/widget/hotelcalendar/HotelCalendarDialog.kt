package com.tokopedia.hotel.common.presentation.widget.hotelcalendar

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent

import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.Legend
import com.tokopedia.common.travel.data.entity.TravelCalendarHoliday
import com.tokopedia.common.travel.domain.TravelCalendarHolidayUseCase
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.di.component.DaggerHotelComponent
import com.tokopedia.hotel.common.di.component.HotelComponent
import com.tokopedia.unifycomponents.bottomsheet.RoundedBottomSheetDialogFragment
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.layout_calendar_dialog.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

import javax.inject.Inject

/**
 * @author by jessica on 03/07/19
 */

class HotelCalendarDialog : RoundedBottomSheetDialogFragment(), HasComponent<HotelComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var hotelCalendarDialogViewModel: HotelCalendarDialogViewModel

    var isFirstTime: Boolean = true

    var listener: OnDateClickListener? = null
    var title: String = "Pilih Tanggal"

    var checkIn: Date? = null
    var checkOut: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component.inject(this)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            hotelCalendarDialogViewModel = viewModelProvider.get(HotelCalendarDialogViewModel::class.java)
        }

        arguments?.let {
            if (it.getString(ARG_CHECK_IN) != null)
                checkIn = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, it.getString(ARG_CHECK_IN))

            if (it.getString(ARG_CHECK_OUT) != null)
                checkOut = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, it.getString(ARG_CHECK_OUT))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_calendar_dialog, container, false)
    }

    override fun getComponent(): HotelComponent
            = DaggerHotelComponent.builder().baseAppComponent((activity!!.application as BaseMainApplication).baseAppComponent).build()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loading_progress_bar.visibility = View.VISIBLE

        hotelCalendarDialogViewModel.getTravelHolidayDate()

        hotelCalendarDialogViewModel.holidayResult.observe(this, Observer {
            loading_progress_bar.visibility = View.GONE
            when (it) {
                is Success -> {
                    if (isFirstTime && it.data.data.isNotEmpty()) {
                        renderCalendar(mappingHolidayData(it.data))
                        isFirstTime = false
                    }
                }
                is Fail -> {
                    renderCalendar(arrayListOf())
                }
            }
        })

        btn_close.setOnClickListener({ view1 -> dismissAllowingStateLoss() })

        date_in.keyListener = null
        date_out.keyListener = null

    }

    fun renderCalendar(legends: ArrayList<Legend>) {
        val calendar = calendar_unify.calendarPickerView

        val nextYear = Calendar.getInstance()
        nextYear.add(Calendar.YEAR, 1)

        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DATE, 1)
        yesterday.set(Calendar.HOUR_OF_DAY, 0)
        yesterday.set(Calendar.MINUTE, 0)
        yesterday.set(Calendar.SECOND, 0)
        yesterday.set(Calendar.MILLISECOND, 0)

        if (checkIn != null && checkOut != null) {
            calendar.init(yesterday.time, nextYear.time, legends)
                    .inMode(CalendarPickerView.SelectionMode.RANGE)
                    .maxRange(30)
                    .withSelectedDates(listOf(checkIn, checkOut))
            date_in.requestFocus()
        } else if (checkIn != null && checkOut == null) {
            calendar.init(yesterday.time, nextYear.time, legends)
                    .inMode(CalendarPickerView.SelectionMode.RANGE)
                    .maxRange(30)
                    .withSelectedDate(checkIn)
            date_out.requestFocus()
        }

        val defaultIndLocale = Locale("id", "ID")
        val dateFormat = SimpleDateFormat("E, d MMM", defaultIndLocale)
        dateFormat.timeZone = TimeZone.getDefault()

        if (checkIn != null) date_in.setText(dateFormat.format(checkIn))
        if (checkOut != null) date_out.setText(dateFormat.format(checkOut))

        calendar.setMaxRangeListener(object : CalendarPickerView.OnMaxRangeListener {
            override fun onNotifyMax() {
                Toast.makeText(context, R.string.hotel_calendar_error_max_range, Toast.LENGTH_SHORT).show()
            }

        })

        
        date_in.setOnFocusChangeListener { view, hasFocus ->
            if (checkOut == null) {
                if (date_in.isFocused) date_out.requestFocus()
            } else {
                if (date_out.isFocused) date_in.requestFocus()
            }
        }

        calendar.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {

            override fun onDateSelected(date: Date) {

                if ((checkIn != null && checkOut != null) || (checkOut == null && date.before(checkIn))) {
                    date_in.setText(dateFormat.format(date))
                    date_out.setText("")
                    checkIn = date
                    checkOut = null
                    date_out.requestFocus()
                } else if (checkIn != null && checkOut == null && date.after(checkIn)) {
                    date_out.setText(dateFormat.format(date))
                    checkOut = date
                    date_out.requestFocus()
                    if (listener != null) listener!!.onDateClick(checkIn!!, checkOut!!)

                    runBlocking {
                        launch {
                            delay(300)
                            dismissAllowingStateLoss()
                        }
                    }
                }
            }

            override fun onDateUnselected(date: Date) {

            }
        })
    }

    fun mappingHolidayData(holidayData: TravelCalendarHoliday.HolidayData): ArrayList<Legend> {
        val legendList = arrayListOf<Legend>()
        for (holiday in holidayData.data) {
            legendList.add(Legend(TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, holiday.attribute.date),
                    holiday.attribute.label))
        }
        return legendList
    }

    interface OnDateClickListener {
        fun onDateClick(dateIn: Date, dateOut: Date)
    }

    companion object {

        const val ARG_CHECK_IN = "arg_check_in"
        const val ARG_CHECK_OUT = "arg_check_out"

        fun getInstance(checkIn: String?, checkOut: String?): HotelCalendarDialog =
                HotelCalendarDialog().also {
                    it.arguments = Bundle().apply {
                        putString(ARG_CHECK_IN, checkIn)
                        putString(ARG_CHECK_OUT, checkOut)
                    }
                }
    }
}