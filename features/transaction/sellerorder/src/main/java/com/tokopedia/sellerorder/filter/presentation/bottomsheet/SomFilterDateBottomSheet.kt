package com.tokopedia.sellerorder.filter.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts.PATTERN_DATE_PARAM
import com.tokopedia.sellerorder.common.util.Utils
import com.tokopedia.sellerorder.common.util.Utils.updateShopActive
import com.tokopedia.sellerorder.databinding.BottomSheetFilterDateBinding
import com.tokopedia.sellerorder.filter.presentation.bottomsheet.SomFilterBottomSheet.Companion.SOM_FILTER_DATE_BOTTOM_SHEET_TAG
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import timber.log.Timber
import java.util.*

class SomFilterDateBottomSheet : BottomSheetUnify() {

    companion object {
        const val PATTERN_DATE = "EEE, dd MMM"
        const val PATTER_DATE_EDT = "dd MMM yyyy"
        const val TITLE_FILTER_DATE = "Pilih Tanggal"

        const val YEARS_BACK = 2
        const val MONTHS_AHEAD = 1

        fun newInstance(): SomFilterDateBottomSheet {
            return SomFilterDateBottomSheet()
        }
    }

    var selectedDates: List<Date> = emptyList()

    private var binding by autoClearedNullable<BottomSheetFilterDateBinding>()

    private var calenderFilterListener: CalenderListener? = null

    private var mode: CalendarPickerView.SelectionMode = CalendarPickerView.SelectionMode.RANGE

    private var startDateParam = ""
    private var endDateParam = ""
    private var startDateEditText = ""
    private var endDateEditText = ""

    private var minDate: Date? = null
    private var maxDate: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(TITLE_FILTER_DATE)
        clearContentPadding = true
        isFullpage = true
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SomFilterDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetFilterDateBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setupCalendarView()
        setDefaultSelectedDate()
        btnSaveCalendar()
        toggleBtnShowOrder()
    }

    override fun onResume() {
        super.onResume()
        updateShopActive()
    }

    override fun onDestroy() {
        cleanupResources()
        super.onDestroy()
    }

    fun show(fm: FragmentManager?) {
        if (!isVisible) {
            fm?.let {
                show(it, SOM_FILTER_DATE_BOTTOM_SHEET_TAG)
            }
        }
    }

    private fun cleanupResources() {
        selectedDates = emptyList()
        calenderFilterListener = null
        startDateParam = ""
        endDateParam = ""
        startDateEditText = ""
        endDateEditText = ""
        minDate = null
        maxDate = null
    }

    private fun initView() {
        binding?.tfStartDate?.textFieldInput?.isClickable = false
        binding?.tfEndDate?.textFieldInput?.isClickable = false
        binding?.tfStartDate?.textFieldInput?.isFocusable = false
        binding?.tfEndDate?.textFieldInput?.isFocusable = false
    }

    private fun toggleBtnShowOrder() {
        val isShowStartDate = binding?.tfStartDate?.textFieldInput?.text?.trim().toString().isNotBlank()
        val isShowEndDate = binding?.tfEndDate?.textFieldInput?.text?.trim().toString().isNotBlank()

        binding?.btnSaveCalendar?.isEnabled = (isShowStartDate || isShowEndDate)
    }

    private fun setDefaultSelectedDate() {
        binding?.somFilterCalendar?.calendarPickerView?.let { cpv ->
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
        val minDate = Utils.getNPastYearTimeStamp(YEARS_BACK)
        val maxDate = Utils.getNFutureMonthsTimeStamp(MONTHS_AHEAD)

        binding?.somFilterCalendar?.calendarPickerView?.let { cpv ->
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
        binding?.tfEndDate?.textFieldInput?.setText(getSelectedDate(date, PATTERN_DATE))
        binding?.tfEndDate?.textFieldInput?.setSelection(binding?.tfEndDate?.textFieldInput?.text?.length ?: 0)
        binding?.tfEndDate?.textFieldInput?.requestFocus()
    }

    private fun selectStartDate(date: Date) {
        startDateParam = getSelectedDate(date, PATTERN_DATE_PARAM)
        startDateEditText = getSelectedDate(date, PATTER_DATE_EDT)
        binding?.tfStartDate?.textFieldInput?.setText(getSelectedDate(date, PATTERN_DATE))
        binding?.tfStartDate?.textFieldInput?.setSelection(binding?.tfStartDate?.textFieldInput?.text?.length ?: 0)
        binding?.tfStartDate?.textFieldInput?.requestFocus()
    }

    private fun getSelectedDate(date: Date, patternDate: String): String {
        return Utils.format(date.time, patternDate)
    }

    private fun btnSaveCalendar() {
        binding?.btnSaveCalendar?.setOnClickListener {
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
