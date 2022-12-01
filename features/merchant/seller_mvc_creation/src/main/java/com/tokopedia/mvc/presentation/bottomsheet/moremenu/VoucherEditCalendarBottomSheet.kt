package com.tokopedia.mvc.presentation.bottomsheet.moremenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.SubTitle
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcBottomsheetEditPeriodCalendarBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*

class VoucherEditCalendarBottomSheet: BottomSheetUnify() {

    private var binding by autoClearedNullable<SmvcBottomsheetEditPeriodCalendarBinding>()

    var calendar: CalendarPickerView? = null

    init {
        isFullpage = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = SmvcBottomsheetEditPeriodCalendarBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
        setTitle(context?.getString(R.string.edit_period_calender_title) ?: "")

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calendar = binding?.voucherCreationCalendar?.calendarPickerView
        renderCalendar()
    }


    private fun renderCalendar() {
        val nextYear = Calendar.getInstance()
        nextYear.add(Calendar.YEAR, 1)

        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DATE, -10)

        var selectedDates: ArrayList<Date> = arrayListOf(nextYear.time)

//        checkIn?.let { checkIn ->
//            checkOut?.let { checkOut ->
//                selectedDates = getRangeBetween(checkIn, checkOut)
//            }
//        }

        val arraySubTitles = arrayListOf<SubTitle>()

        val monthCounter = Calendar.getInstance()

        var date = monthCounter.time
        while (monthCounter.before(nextYear)) {
            if (monthCounter.get(Calendar.DATE) % 7 == 0) {
                arraySubTitles.add(SubTitle(date, "1000", "#42b549"))
            } else {
                arraySubTitles.add(SubTitle(date, "1000"))
            }

            monthCounter.add(Calendar.DATE, 1)
            date = monthCounter.time
        }

        val activeDates: ArrayList<Date> = ArrayList()
        activeDates.add(Calendar.getInstance().time)
        var activeDate = Calendar.getInstance()
        activeDate.add(Calendar.DATE, 1)
        activeDates.add(activeDate.time)
        activeDate = Calendar.getInstance()
        activeDate.add(Calendar.DATE, 2)
        activeDates.add(activeDate.time)
        activeDate.add(Calendar.DATE, 5)
        activeDates.add(activeDate.time)

        calendar?.init(yesterday.time, nextYear.time, ArrayList())
            ?.inMode(CalendarPickerView.SelectionMode.RANGE)
//            ?.maxRange(30)
            ?.withSelectedDates(selectedDates)

        calendar?.setSubTitles(arraySubTitles)


        calendar?.setMaxRangeListener(object : CalendarPickerView.OnMaxRangeListener {
            override fun onNotifyMax() {
                Toast.makeText(activity, "woy udh max", Toast.LENGTH_SHORT).show()
            }

        })

//        calendar?.setSubTitles(arraySubTitles)

//        calendar?.onScrollMonthListener = object : CalendarPickerView.OnScrollMonthListener {
//            override fun onScrolled(date: Date) {
//                Toast.makeText(activity, dateFormat.format(date), Toast.LENGTH_SHORT).show()
//            }
//        }
    }


    companion object {
        @JvmStatic
        fun newInstance(): VoucherEditCalendarBottomSheet {
            return VoucherEditCalendarBottomSheet().apply {

            }
        }
    }

}
