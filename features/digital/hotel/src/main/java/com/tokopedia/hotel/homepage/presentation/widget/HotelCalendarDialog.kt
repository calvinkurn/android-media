package com.tokopedia.hotel.homepage.presentation.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.Legend
import com.tokopedia.hotel.R
import com.tokopedia.unifycomponents.bottomsheet.RoundedBottomSheetDialogFragment
import kotlinx.android.synthetic.main.layout_calendar_dialog.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * @author by jessica on 03/07/19
 */

class HotelCalendarDialog : RoundedBottomSheetDialogFragment() {

    var listener: OnDateClickListener? = null
    var title: String = "Pilih Tanggal"
    var selectedDate: Date? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_calendar_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_close.setOnClickListener({ view1 -> dismissAllowingStateLoss() })

        date_in.keyListener = null
        date_out.keyListener = null

        val calendar = calendar_unify.calendarPickerView

        val nextYear = Calendar.getInstance()
        nextYear.add(Calendar.YEAR, 1)

        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DATE, 1)
        yesterday.set(Calendar.HOUR_OF_DAY, 0)
        yesterday.set(Calendar.MINUTE, 0)
        yesterday.set(Calendar.SECOND, 0)
        yesterday.set(Calendar.MILLISECOND, 0)

        val arrayList = ArrayList<Legend>()
        var dueDate = Date()
        try {
            val dateformat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

            val strdate = "22-7-2019"
            val strdate2 = "26-7-2019"
            val strdate3 = "1-8-2019"
            val strdate4 = "8-8-2019"

            val newdate = dateformat.parse(strdate)
            val newdate2 = dateformat.parse(strdate2)
            val newdate3 = dateformat.parse(strdate3)
            val newdate4 = dateformat.parse(strdate4)
            arrayList.add(Legend(newdate, "Desc Day 1"))
            arrayList.add(Legend(newdate2, "Desc Day 2"))
            arrayList.add(Legend(newdate3, "Desc Day 3"))
            arrayList.add(Legend(newdate4, "Desc Day 4"))

            dueDate = dateformat.parse("28-06-2019")

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        calendar.init(yesterday.time, nextYear.time, arrayList)
                .inMode(CalendarPickerView.SelectionMode.RANGE)

        val defaultIndLocale = Locale("id", "ID")
        val dateFormat = SimpleDateFormat("E, d MMM", defaultIndLocale)
        dateFormat.timeZone = TimeZone.getDefault()

        var dateIn: Date? = null
        if (selectedDate != null) {
            dateIn = selectedDate
            calendar.selectDate(selectedDate)
        }

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

    interface OnDateClickListener {
        fun onDateClick(dateIn: Date, dateOut: Date)
    }
}