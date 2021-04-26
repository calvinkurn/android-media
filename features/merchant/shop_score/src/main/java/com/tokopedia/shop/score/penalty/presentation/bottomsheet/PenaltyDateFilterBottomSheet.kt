package com.tokopedia.shop.score.penalty.presentation.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.UnifyCalendar
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.format
import com.tokopedia.shop.score.common.getNPastMonthTimeStamp
import com.tokopedia.shop.score.common.getNowTimeStamp
import com.tokopedia.shop.score.common.presentation.BaseBottomSheetShopScore
import com.tokopedia.unifycomponents.TextFieldUnify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import kotlin.math.max

class PenaltyDateFilterBottomSheet : BaseBottomSheetShopScore() {

    private var tfStartDate: TextFieldUnify? = null
    private var tfEndDate: TextFieldUnify? = null
    private var penaltyFilterCalendar: UnifyCalendar? = null
    private var calendarView: CalendarPickerView? = null

    private var mode: CalendarPickerView.SelectionMode = CalendarPickerView.SelectionMode.RANGE

    private var calenderFilterListener: CalenderListener? = null

    private var startDateParam = ""
    private var endDateParam = ""
    private var startDateEditText = ""
    private var endDateEditText = ""

    private var minDate: Date? = null
    private var maxDate: Date? = null

    override fun getLayoutResId(): Int = R.layout.bottom_sheet_date_filter_penalty

    override fun getTitleBottomSheet(): String = getString(R.string.title_penalty_date_filter)

    override fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            if (!isVisible) {
                show(it, PenaltyDateFilterBottomSheetTag)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clearContentPadding = true
        isFullpage = true
        bottomSheetClose.setImageDrawable(context?.let { getIconUnifyDrawable(it, IconUnify.ARROW_BACK) })
        setStyle(DialogFragment.STYLE_NORMAL, R.style.PenaltyFilterDialogStyle)
        initView(view)
        setupCalendarView()
        setDefaultSelectedDate()
    }

    private fun initView(view: View) {
        tfStartDate = view.findViewById(R.id.tfStartDate)
        tfEndDate = view.findViewById(R.id.tfEndDate)
        penaltyFilterCalendar = view.findViewById(R.id.penaltyFilterCalendar)
        calendarView = penaltyFilterCalendar?.calendarPickerView
        tfStartDate?.textFieldInput?.isClickable = false
        tfEndDate?.textFieldInput?.isClickable = false
        tfStartDate?.textFieldInput?.isFocusable = false
        tfEndDate?.textFieldInput?.isFocusable = false
    }

    private fun setDefaultSelectedDate() {
        calendarView?.let { cpv ->
            minDate = Date(getNowTimeStamp())
            minDate?.let {
                selectDate(cpv, it)
                selectStartDate(it)
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
        val initMinDate = getNPastMonthTimeStamp(3)
        val initMaxDate = Date(getNowTimeStamp())

        calendarView?.let { cpv ->
            cpv.init(initMinDate, initMaxDate, emptyList()).inMode(mode)
            cpv.scrollToDate(initMaxDate)

            cpv.selectDateClickListener()
        }
    }

    private fun selectEndDate(date: Date) {
        endDateParam = getSelectedDate(date, PATTERN_DATE_PARAM)
        endDateEditText = getSelectedDate(date, PATTER_DATE_EDT)
        tfEndDate?.textFieldInput?.setText(getSelectedDate(date, PATTER_DATE_EDT))
        tfEndDate?.textFieldInput?.setSelection(tfEndDate?.textFieldInput?.text?.length ?: 0)
        tfEndDate?.textFieldInput?.requestFocus()
    }

    private fun selectStartDate(date: Date) {
        startDateParam = getSelectedDate(date, PATTERN_DATE_PARAM)
        startDateEditText = getSelectedDate(date, PATTER_DATE_EDT)
        tfStartDate?.textFieldInput?.setText(getSelectedDate(date, PATTER_DATE_EDT))
        tfStartDate?.textFieldInput?.setSelection(tfStartDate?.textFieldInput?.text?.length ?: 0)
        tfStartDate?.textFieldInput?.requestFocus()
    }

    private fun getSelectedDate(date: Date, patternDate: String): String {
        return format(date.time, patternDate)
    }

    private fun CalendarPickerView.selectDateClickListener() {
        setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
            override fun onDateSelected(date: Date) {
                when (mode) {
                    CalendarPickerView.SelectionMode.RANGE -> {
                        if ((minDate == null || maxDate != null) || (maxDate == null && date.before(minDate))) {
                            minDate = date
                            maxDate = null
                            selectStartDate(date)
                        } else if ((minDate != null || maxDate == null) && (date.after(minDate) || !date.before(minDate))) {
                            maxDate = date
                            selectEndDate(date)
                            GlobalScope.launch(Dispatchers.Main) {
                                delay(300)
                                calenderFilterListener?.onSaveCalendarClicked(Pair(startDateParam, startDateEditText), Pair(endDateParam, endDateEditText))
                                dismissAllowingStateLoss()
                            }
                        }
                    }
                    else -> {
                    }
                }
            }

            override fun onDateUnselected(date: Date) {}
        })
    }

    fun setCalendarListener(calenderFilterListener: CalenderListener) {
        this.calenderFilterListener = calenderFilterListener
    }

    interface CalenderListener {
        fun onSaveCalendarClicked(startDate: Pair<String, String>, endDate: Pair<String, String>)
    }

    companion object {
        const val PenaltyDateFilterBottomSheetTag = "PenaltyDateFilterBottomSheetTag"
        const val PATTER_DATE_EDT = "dd MMM yyyy"
        const val PATTERN_DATE_PARAM = "dd/MM/yyyy"

        fun newInstance(): PenaltyDateFilterBottomSheet {
            return PenaltyDateFilterBottomSheet()
        }
    }

}
