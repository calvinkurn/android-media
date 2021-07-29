package com.tokopedia.saldodetails.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.utils.SaldoDateUtil.setMidnight
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.saldo_bottomsheet_choose_date.*
import java.util.*

class DateRangePickerBottomSheet : BottomSheetUnify() {

    val maxDate = Date()
    val minDate = Date(System.currentTimeMillis() - (10 * 365 * 24 * 3600 * 1000L))

    var defaultStartDate: Date? = null
    var defaultEndDate: Date? = null
    lateinit var childView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments?.containsKey(ARG_START_DATE) == true
            && arguments?.containsKey(ARG_END_DATE) == true
        ) {
            defaultStartDate = arguments?.getSerializable(ARG_START_DATE) as Date
            defaultEndDate = arguments?.getSerializable(ARG_END_DATE) as Date
        } else {
            defaultStartDate = Date()
            defaultEndDate = Date()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setDefaultParams()
        childView = View.inflate(context, R.layout.saldo_bottomsheet_choose_date, null)
        setChild(childView)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childView.layoutParams.height = (getScreenHeight() / 3 * 2)
        initCalender()
        unifyButtonSelect.setOnClickListener {
            if (defaultStartDate != null && defaultEndDate != null) {
                if (parentFragment is OnDateRangeSelectListener) {
                    (parentFragment as OnDateRangeSelectListener)
                        .onDateRangeSelected(defaultStartDate!!, defaultEndDate!!)
                    dismissAllowingStateLoss()
                }
            }
        }
    }

    private fun initCalender() {
        calendar_unify.calendarPickerView?.init(minDate, maxDate, arrayListOf())
            ?.inMode(CalendarPickerView.SelectionMode.RANGE)
            ?.maxRange(30)
            ?.withSelectedDates(arrayListOf(defaultStartDate!!, defaultEndDate!!))
        calendar_unify.calendarPickerView?.selectDateClickListener()
        calendar_unify.calendarPickerView?.outOfRange()
    }

    private fun CalendarPickerView.selectDateClickListener() {
        setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
            override fun onDateSelected(date: Date) {
                val selectedDates = calendar_unify.calendarPickerView?.selectedDates
                when {
                    selectedDates.isNullOrEmpty() -> {
                        defaultStartDate = null
                        defaultEndDate = null
                    }
                    selectedDates.size == 1 -> {
                        defaultEndDate = selectedDates[0]
                        defaultStartDate = selectedDates[0]
                    }
                    else -> {
                        defaultStartDate = selectedDates.first()
                        defaultEndDate = selectedDates.last()
                    }
                }
            }

            override fun onDateUnselected(date: Date) {

            }
        })
    }

    private fun CalendarPickerView.outOfRange() {
        setMaxRangeListener(object : CalendarPickerView.OnMaxRangeListener {
            override fun onNotifyMax() {
                //todo post out of 30 days range
            }

        })
    }

    private fun setDefaultParams() {
        isDragable = true
        showCloseIcon = true
        showHeader = true
        isHideable = true
        isFullpage = false
        customPeekHeight = (getScreenHeight() / 3 * 2)
        setTitle("Pilih tanggal")
    }

    companion object {
        val ARG_START_DATE = "ARG_START_DATE"
        val ARG_END_DATE = "ARG_END_DATE"
        fun getInstance(startDate: Date?, endDate: Date?): DateRangePickerBottomSheet {
            return DateRangePickerBottomSheet().apply {
                val bundle = Bundle()
                bundle.putSerializable(ARG_START_DATE, startDate ?: Date())
                bundle.putSerializable(ARG_END_DATE, endDate ?: Date())
                arguments = bundle
            }
        }
    }


}

interface OnDateRangeSelectListener {
    fun onDateRangeSelected(startDate: Date, endDate: Date)
}