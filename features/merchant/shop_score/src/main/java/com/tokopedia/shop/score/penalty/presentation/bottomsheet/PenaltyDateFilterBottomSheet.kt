package com.tokopedia.shop.score.penalty.presentation.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.common.format
import com.tokopedia.shop.score.common.getLocale
import com.tokopedia.shop.score.common.getNPastDaysTimeStamp
import com.tokopedia.shop.score.common.getNowTimeStamp
import com.tokopedia.shop.score.common.presentation.bottomsheet.BaseBottomSheetShopScore
import com.tokopedia.shop.score.databinding.BottomSheetDateFilterPenaltyBinding
import java.text.SimpleDateFormat
import java.util.*

class PenaltyDateFilterBottomSheet :
    BaseBottomSheetShopScore<BottomSheetDateFilterPenaltyBinding>() {

    private var mode: CalendarPickerView.SelectionMode = CalendarPickerView.SelectionMode.RANGE

    private var calenderFilterListener: CalenderListener? = null

    private var startDateParam = ""
    private var endDateParam = ""
    private var startDateEditText = ""
    private var endDateEditText = ""

    private var minDate: Date? = null
    private var maxDate: Date? = null

    private var maxStartDate: Date? = null
    private var maxEndDate: Date? = null

    override fun bind(view: View) = BottomSheetDateFilterPenaltyBinding.bind(view)

    override fun getLayoutResId(): Int = R.layout.bottom_sheet_date_filter_penalty

    override fun getTitleBottomSheet(): String = getString(R.string.title_penalty_date_filter)

    override fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            if (!isVisible) {
                show(it, PenaltyDateFilterBottomSheetTag)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromArguments()
    }

    private fun getDataFromArguments() {
        val startDateParam = arguments?.getString(KEY_START_DATE_PENALTY) ?: ""
        val endDateParam = arguments?.getString(KEY_END_DATE_PENALTY) ?: ""
        val maxStartDateParam = arguments?.getString(KEY_MAX_START_DATE_PENALTY)
        val maxEndDateParam = arguments?.getString(KEY_MAX_END_DATE_PENALTY)

        minDate = SimpleDateFormat(ShopScoreConstant.PATTERN_DATE_PARAM, getLocale()).parse(startDateParam)
        maxDate = SimpleDateFormat(ShopScoreConstant.PATTERN_DATE_PARAM, getLocale()).parse(endDateParam)
        maxStartDateParam?.let {
            maxStartDate = SimpleDateFormat(ShopScoreConstant.PATTERN_DATE_PARAM, getLocale()).parse(it)
        }
        maxEndDateParam?.let {
            maxEndDate = SimpleDateFormat(ShopScoreConstant.PATTERN_DATE_PARAM, getLocale()).parse(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clearContentPadding = true
        isFullpage = true
        showCloseIcon = true
        setStyle(DialogFragment.STYLE_NORMAL, R.style.PenaltyFilterDialogStyle)
        initView(binding)
        setupCalendarView()
        setDefaultSelectedDate()
    }

    private fun initView(binding: BottomSheetDateFilterPenaltyBinding?) = binding?.run {
        tfStartDate.textFieldInput.isClickable = false
        tfEndDate.textFieldInput.isClickable = false
        tfStartDate.textFieldInput.isFocusable = false
        tfEndDate.textFieldInput.isFocusable = false
    }

    private fun setDefaultSelectedDate() {
        binding?.penaltyFilterCalendar?.calendarPickerView?.let { cpv ->
            minDate?.let {
                selectDate(cpv, it)
                selectStartDate(it)
            }
            maxDate?.let {
                selectDate(cpv, it)
                selectEndDate(it)
            }
        }
    }


    private fun selectDate(cpv: CalendarPickerView, date: Date, smoothScroll: Boolean = false) {
        try {
            cpv.selectDate(date, smoothScroll)
        } catch (e: IllegalArgumentException) {
        }
    }

    private fun setupCalendarView() {
        val initMinDate = maxStartDate ?: getNPastDaysTimeStamp(NINETY_DAYS)
        val initMaxDate = maxEndDate ?: Date(getNowTimeStamp())
        binding?.penaltyFilterCalendar?.calendarPickerView?.let { cpv ->
            cpv.init(initMinDate, initMaxDate, emptyList()).inMode(mode)
            cpv.scrollToDate(initMinDate)
            cpv.selectDateClickListener()
        }
    }

    private fun selectEndDate(date: Date) = binding?.run {
        endDateParam = getSelectedDate(date, ShopScoreConstant.PATTERN_DATE_PARAM)
        endDateEditText = getSelectedDate(date, ShopScoreConstant.PATTER_DATE_EDT)
        tfEndDate.textFieldInput.setText(getSelectedDate(date, ShopScoreConstant.PATTER_DATE_EDT))
        tfEndDate.textFieldInput.setSelection(tfEndDate.textFieldInput.text?.length ?: 0)
        tfEndDate.textFieldInput.requestFocus()
    }

    private fun selectStartDate(date: Date) = binding?.run {
        startDateParam = getSelectedDate(date, ShopScoreConstant.PATTERN_DATE_PARAM)
        startDateEditText = getSelectedDate(date, ShopScoreConstant.PATTER_DATE_EDT)
        tfStartDate.textFieldInput.setText(getSelectedDate(date, ShopScoreConstant.PATTER_DATE_EDT))
        tfStartDate.textFieldInput.setSelection(tfStartDate.textFieldInput.text?.length ?: 0)
        tfStartDate.textFieldInput.requestFocus()
    }

    private fun getSelectedDate(date: Date, patternDate: String): String {
        return format(date.time, patternDate)
    }

    private fun setActionButton(isActive: Boolean) {
        if (isActive) {
            bottomSheetAction.run {
                show()
                text = getString(R.string.btn_save)
                setOnClickListener {
                    saveSelectedDate()
                }
            }
        } else {
            bottomSheetAction.run {
                gone()
                text = String.EMPTY
            }
        }
    }

    private fun CalendarPickerView.selectDateClickListener() {
        setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
            override fun onDateSelected(date: Date) {
                if (mode == CalendarPickerView.SelectionMode.RANGE) {
                    if ((minDate == null || maxDate != null) || (maxDate == null && date.before(
                            minDate
                        ))
                    ) {
                        minDate = date
                        maxDate = null
                        selectStartDate(date)
                        setActionButton(false)
                    } else if ((minDate != null || maxDate == null) && (date.after(minDate) || !date.before(
                            minDate
                        ))
                    ) {
                        maxDate = date
                        selectEndDate(date)
                        setActionButton(true)
                    }
                }
            }

            override fun onDateUnselected(date: Date) {}
        })
    }

    private fun saveSelectedDate() {
        calenderFilterListener?.onSaveCalendarClicked(
            Pair(
                startDateParam,
                startDateEditText
            ), Pair(endDateParam, endDateEditText)
        )
        dismissAllowingStateLoss()
    }

    fun setCalendarListener(calenderFilterListener: CalenderListener) {
        this.calenderFilterListener = calenderFilterListener
    }

    interface CalenderListener {
        fun onSaveCalendarClicked(startDate: Pair<String, String>, endDate: Pair<String, String>)
    }

    companion object {
        const val PenaltyDateFilterBottomSheetTag = "PenaltyDateFilterBottomSheetTag"
        const val KEY_START_DATE_PENALTY = "key_start_date_penalty"
        const val KEY_END_DATE_PENALTY = "key_end_date_penalty"
        const val KEY_MAX_START_DATE_PENALTY = "key_max_start_date_penalty"
        const val KEY_MAX_END_DATE_PENALTY = "key_max_end_date_penalty"
        const val DELAY_SELECTED_FILTER_DATE_PENALTY = 300L
        const val NINETY_DAYS = 90

        fun newInstance(
            startDate: String,
            endDate: String,
            maxStartDate: String? = null,
            maxEndDate: String? = null
        ): PenaltyDateFilterBottomSheet {
            return PenaltyDateFilterBottomSheet().apply {
                val args = Bundle()
                args.putString(KEY_START_DATE_PENALTY, startDate)
                args.putString(KEY_END_DATE_PENALTY, endDate)
                maxStartDate?.let {
                    args.putString(KEY_MAX_START_DATE_PENALTY, it)
                }
                maxEndDate?.let {
                    args.putString(KEY_MAX_END_DATE_PENALTY, it)
                }
                arguments = args
            }
        }
    }

}
