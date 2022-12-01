package com.tokopedia.mvc.presentation.bottomsheet.moremenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.Legend
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcBottomsheetEditPeriodCalendarBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class VoucherEditCalendarBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<SmvcBottomsheetEditPeriodCalendarBinding>()

    var calendar: CalendarPickerView? = null

    private var startDate: GregorianCalendar? = null
    private var endDate: GregorianCalendar? = null
    private var startCalendar: GregorianCalendar? = null

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
        renderCalendar(arrayListOf())

        calendar?.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
            override fun onDateSelected(date: Date) {
                // put your implementation here
                // Save the currentDate to ViewModel
                // Navigate To Time Picker Bottom Sheet

                GlobalScope.launch {
                    delay(300)
                    dismissAllowingStateLoss()
                }
            }

            override fun onDateUnselected(date: Date) {
                // put your implementation here
            }
        })
    }

    private fun renderCalendar(holidayArrayList: ArrayList<Legend>) {
        var selectedDates = startCalendar?.let {
            arrayListOf(it.time)
        }

        startDate?.time?.let {
            endDate?.time?.let { it1 ->
                calendar?.init(it, it1, holidayArrayList)
                    ?.inMode(CalendarPickerView.SelectionMode.SINGLE)
                    ?.withSelectedDates(selectedDates)
            }
        }

        calendar?.setMaxRangeListener(object : CalendarPickerView.OnMaxRangeListener {
            override fun onNotifyMax() {
                Toast.makeText(activity, "woy udh max", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(
            startCalendar: GregorianCalendar?,
            minDate: GregorianCalendar,
            maxDate: GregorianCalendar
        ): VoucherEditCalendarBottomSheet {
            return VoucherEditCalendarBottomSheet().apply {
                this.startDate = minDate
                this.endDate = maxDate
                this.startCalendar = startCalendar
            }
        }
    }
}
