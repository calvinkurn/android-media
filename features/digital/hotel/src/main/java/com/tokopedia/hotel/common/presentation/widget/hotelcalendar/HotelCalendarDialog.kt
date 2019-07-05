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

    @Inject
    lateinit var useCase: TravelCalendarHolidayUseCase

    var isFirstTime: Boolean = true

    var listener: OnDateClickListener? = null
    var title: String = "Pilih Tanggal"
    var selectedDate: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component.inject(this)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            hotelCalendarDialogViewModel = viewModelProvider.get(HotelCalendarDialogViewModel::class.java)
        }

        hotelCalendarDialogViewModel.getTravelHolidayDate()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_calendar_dialog, container, false)
    }

    override fun getComponent(): HotelComponent
            = DaggerHotelComponent.builder().baseAppComponent((activity!!.application as BaseMainApplication).baseAppComponent).build()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hotelCalendarDialogViewModel.holidayResult.observe(this, Observer {
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


        val defaultIndLocale = Locale("id", "ID")
        val dateFormat = SimpleDateFormat("E, d MMM", defaultIndLocale)
        dateFormat.timeZone = TimeZone.getDefault()

        var dateIn: Date? = null
        if (selectedDate != null) {
            dateIn = selectedDate
            calendar.selectDate(selectedDate)
        }

        calendar.init(yesterday.time, nextYear.time, legends)
                .inMode(CalendarPickerView.SelectionMode.RANGE)
                .maxRange(30)

        calendar.setMaxRangeListener(object : CalendarPickerView.OnMaxRangeListener {
            override fun onNotifyMax() {
                Toast.makeText(context, R.string.hotel_calendar_error_max_range, Toast.LENGTH_SHORT).show()
            }

        })

        calendar.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {

            override fun onDateSelected(date: Date) {
                if (dateIn != null && date.after(dateIn) && date_in.isFocused()) {
                    date_out.setText(dateFormat.format(date))
                    date_out.requestFocus()
                    if (listener != null) listener!!.onDateClick(dateIn!!, date)

                    GlobalScope.launch {
                        delay(300)
                        dismissAllowingStateLoss()
                    }
                } else {
                    dateIn = date
                    date_out.setText("")
                    date_in.setText(dateFormat.format(date))
                    date_in.requestFocus()
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
}