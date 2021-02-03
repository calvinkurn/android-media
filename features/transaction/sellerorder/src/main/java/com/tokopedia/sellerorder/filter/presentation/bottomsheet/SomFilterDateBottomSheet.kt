package com.tokopedia.sellerorder.filter.presentation.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.UnifyCalendar
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts.PATTERN_DATE_PARAM
import com.tokopedia.sellerorder.common.util.Utils
import com.tokopedia.sellerorder.filter.presentation.bottomsheet.SomFilterBottomSheet.Companion.SOM_FILTER_DATE_BOTTOM_SHEET_TAG
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton
import timber.log.Timber
import java.util.*


class SomFilterDateBottomSheet : BottomSheetUnify() {

    companion object {
        const val PATTERN_DATE = "EEE, dd MMM"
        const val PATTER_DATE_EDT = "dd MMM yyyy"
        const val TITLE_FILTER_DATE = "Pilih Tanggal"

        fun newInstance(): SomFilterDateBottomSheet {
            return SomFilterDateBottomSheet()
        }
    }

    var selectedDates: List<Date> = emptyList()

    private var calendarViewFilter: UnifyCalendar? = null
    private var tfStartDate: TextFieldUnify? = null
    private var tfEndDate: TextFieldUnify? = null
    private var calendarView: CalendarPickerView? = null
    private var btnSaveCalendar: UnifyButton? = null
    private var cvSaveCalendar: CardView? = null

    private var calenderFilterListener: CalenderListener? = null

    private var mode: CalendarPickerView.SelectionMode = CalendarPickerView.SelectionMode.RANGE

    private var fm: FragmentManager? = null

    private var startDateParam = ""
    private var endDateParam = ""
    private var startDateEditText = ""
    private var endDateEditText = ""

    private var minDate: Date? = null
    private var maxDate: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val child: View = View.inflate(context, R.layout.bottom_sheet_filter_date, null)
        setTitle(TITLE_FILTER_DATE)
        clearContentPadding = true
        isFullpage = true
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SomFilterDialogStyle)
        setChild(child)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupCalendarView()
        setDefaultSelectedDate()
        btnSaveCalendar()
        toggleBtnShowOrder()
    }

    fun show() {
        fm?.let {
            show(it, SOM_FILTER_DATE_BOTTOM_SHEET_TAG)
        }
    }

    fun setFragmentManager(fm: FragmentManager): SomFilterDateBottomSheet {
        this.fm = fm
        return this
    }

    private fun initView(view: View) {
        calendarViewFilter = view.findViewById(R.id.somFilterCalendar)
        tfStartDate = view.findViewById(R.id.tfStartDate)
        tfEndDate = view.findViewById(R.id.tfEndDate)
        btnSaveCalendar = view.findViewById(R.id.btnSaveCalendar)
        cvSaveCalendar = view.findViewById(R.id.cvSaveCalendar)
        calendarView = calendarViewFilter?.calendarPickerView
        tfStartDate?.textFieldInput?.isClickable = false
        tfEndDate?.textFieldInput?.isClickable = false
        tfStartDate?.textFieldInput?.isFocusable = false
        tfEndDate?.textFieldInput?.isFocusable = false
    }

    private fun toggleBtnShowOrder() {
        val isShowStartDate = tfStartDate?.textFieldInput?.text?.trim().toString().isNotBlank()
        val isShowEndDate = tfEndDate?.textFieldInput?.text?.trim().toString().isNotBlank()

        btnSaveCalendar?.isEnabled = (isShowStartDate || isShowEndDate)
    }

    private fun setDefaultSelectedDate() {
        calendarView?.let { cpv ->
            val startDate = selectedDates.firstOrNull()
            val endDate = selectedDates.lastOrNull()
            startDate?.let {
                selectDate(cpv, it)
                selectStartDate(it)
            }
            endDate?.let {
                selectDate(cpv, it)
                selectEndDate(it)
            }
        }
    }

    private fun selectDate(cpv: CalendarPickerView, date: Date, smoothScroll: Boolean = false) {
        try {
            cpv.selectDate(date, smoothScroll)
        } catch (e: IllegalArgumentException) {
            Timber.w(e)
        }
    }

    private fun setupCalendarView() {
        val minDate = Utils.getNPastYearTimeStamp(2)
        val maxDate = Date(Utils.getNowTimeStamp())

        calendarView?.let { cpv ->
            cpv.init(minDate, maxDate, emptyList()).inMode(mode)
            cpv.scrollToDate(maxDate)
            cpv.selectDateClickListener()
        }
    }

    private fun CalendarPickerView.selectDateClickListener() {
        setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
            override fun onDateSelected(date: Date) {
                when (mode) {
                    CalendarPickerView.SelectionMode.RANGE -> {
                        if ((minDate != null && maxDate == null) && (date.after(minDate) || !date.before(minDate))) {
                            maxDate = date
                            selectEndDate(date)
                        } else if ((minDate == null || maxDate != null) || (maxDate == null && date.before(minDate))) {
                            minDate = date
                            maxDate = null
                            selectStartDate(date)
                        }
                        this@SomFilterDateBottomSheet.selectedDates = selectedDates
                        toggleBtnShowOrder()
                    }
                    else -> {
                    }

                }
            }

            override fun onDateUnselected(date: Date) {}
        })
    }

    private fun selectEndDate(date: Date) {
        endDateParam = getSelectedDate(date, PATTERN_DATE_PARAM)
        endDateEditText = getSelectedDate(date, PATTER_DATE_EDT)
        tfEndDate?.textFieldInput?.setText(getSelectedDate(date, PATTERN_DATE))
        tfEndDate?.textFieldInput?.setSelection(tfEndDate?.textFieldInput?.text?.length ?: 0)
        tfEndDate?.textFieldInput?.requestFocus()
    }

    private fun selectStartDate(date: Date) {
        startDateParam = getSelectedDate(date, PATTERN_DATE_PARAM)
        startDateEditText = getSelectedDate(date, PATTER_DATE_EDT)
        tfStartDate?.textFieldInput?.setText(getSelectedDate(date, PATTERN_DATE))
        tfStartDate?.textFieldInput?.setSelection(tfStartDate?.textFieldInput?.text?.length ?: 0)
        tfStartDate?.textFieldInput?.requestFocus()
    }

    private fun getSelectedDate(date: Date, patternDate: String): String {
        return Utils.format(date.time, patternDate)
    }

    private fun btnSaveCalendar() {
        btnSaveCalendar?.setOnClickListener {
            calenderFilterListener?.onBtnSaveCalendarClicked(Pair(startDateParam, startDateEditText), Pair(endDateParam, endDateEditText))
            dismiss()
        }
    }

    fun setCalendarListener(calenderFilterListener: CalenderListener) {
        this.calenderFilterListener = calenderFilterListener
    }

    interface CalenderListener {
        fun onBtnSaveCalendarClicked(startDate: Pair<String, String>, endDate: Pair<String, String>)
    }
}