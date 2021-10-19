package com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.commom.utils.SaldoDateUtil
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.saldo_bottomsheet_choose_date.*
import java.util.*

class DateRangePickerBottomSheet : BottomSheetUnify() {

    private val maxDate = Date()
    private val minDate = Date(System.currentTimeMillis() - FIFTY_YEAR_MILLIS)

    private var defaultDateFrom: Date? = null
    private var defaultDateTo: Date? = null
    private var newSelectedDateFrom : Date? = Date()
    private var newSelectedDateTO : Date? = Date()

    lateinit var childView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments?.containsKey(ARG_DATE_FROM) == true
            && arguments?.containsKey(ARG_DATE_TO) == true
        ) {
            defaultDateFrom = arguments?.getSerializable(ARG_DATE_FROM) as Date
            defaultDateTo = arguments?.getSerializable(ARG_DATE_TO) as Date
        } else {
            defaultDateFrom = Date()
            defaultDateTo = Date()
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
        childView.layoutParams.height = (getScreenHeight() / BOTTOM_SHEET_HEIGHT_3 * BOTTOM_SHEET_HEIGHT_2)
        initCalender()
        unifyButtonSelect.setOnClickListener {
            if (newSelectedDateFrom != null && newSelectedDateTO != null &&
                !(SaldoDateUtil.isDatesAreSame(newSelectedDateTO, defaultDateTo)
                        && SaldoDateUtil.isDatesAreSame(newSelectedDateFrom, defaultDateFrom))) {
                if (parentFragment is OnDateRangeSelectListener) {
                    (parentFragment as OnDateRangeSelectListener)
                        .onDateRangeSelected(newSelectedDateFrom!!, newSelectedDateTO!!)

                }
            }
            dismissAllowingStateLoss()
        }
    }

    private fun initCalender() {
        newSelectedDateFrom = defaultDateFrom
        newSelectedDateTO = defaultDateTo
        calendar_unify.calendarPickerView?.init(minDate, maxDate, arrayListOf())
            ?.inMode(CalendarPickerView.SelectionMode.RANGE)
            ?.maxRange(MAX_RANGE)
            ?.withSelectedDates(arrayListOf(defaultDateFrom!!, defaultDateTo!!))
        calendar_unify.calendarPickerView?.selectDateClickListener()
        calendar_unify.calendarPickerView?.outOfRange()
    }

    private fun CalendarPickerView.selectDateClickListener() {
        setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
            override fun onDateSelected(date: Date) {
                val selectedDates = calendar_unify.calendarPickerView?.selectedDates
                when {
                    selectedDates.isNullOrEmpty() -> {
                        newSelectedDateFrom = null
                        newSelectedDateTO = null
                    }
                    selectedDates.size == 1 -> {
                        newSelectedDateFrom = selectedDates[0]
                        newSelectedDateTO = selectedDates[0]
                    }
                    else -> {
                        newSelectedDateFrom = selectedDates.first()
                        newSelectedDateTO = selectedDates.last()
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
                showMaxDaysDialog()
            }
        })
    }

    private fun showMaxDaysDialog(){
        DialogUnify(context = this.requireContext(),
            actionType = DialogUnify.SINGLE_ACTION,
            imageType = DialogUnify.NO_IMAGE).apply {
            setTitle(getString(R.string.saldo_calendar_range_error_title))
            setDescription(getString(R.string.sp_title_max_day))
            setPrimaryCTAText(getString(R.string.saldo_btn_oke))
            setPrimaryCTAClickListener {
                cancel()
            }
            show()
        }
    }

    private fun setDefaultParams() {
        isDragable = true
        showCloseIcon = true
        showHeader = true
        isHideable = true
        isFullpage = false
        customPeekHeight = (getScreenHeight() / BOTTOM_SHEET_HEIGHT_3 * BOTTOM_SHEET_HEIGHT_2)
        setTitle("Pilih tanggal")
    }

    companion object {
        const val FIFTY_YEAR_MILLIS = (50 * 365 * 24 * 3600 * 1000L)
        const val MAX_RANGE = 30L
        const val ARG_DATE_FROM = "ARG_DATE_FROM"
        const val ARG_DATE_TO = "ARG_DATE_TO"
        const val BOTTOM_SHEET_HEIGHT_3 = 3
        const val BOTTOM_SHEET_HEIGHT_2 = 2
        fun getInstance(dateFrom: Date?, dateTo: Date?): DateRangePickerBottomSheet {
            return DateRangePickerBottomSheet().apply {
                val bundle = Bundle()
                bundle.putSerializable(ARG_DATE_FROM, dateFrom ?: Date())
                bundle.putSerializable(ARG_DATE_TO, dateTo ?: Date())
                arguments = bundle
            }
        }
    }
}

interface OnDateRangeSelectListener {
    fun onDateRangeSelected(dateFrom: Date, dateTo: Date)
}