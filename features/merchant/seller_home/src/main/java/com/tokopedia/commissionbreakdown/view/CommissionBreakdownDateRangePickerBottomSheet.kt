package com.tokopedia.commissionbreakdown.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.commissionbreakdown.util.CommissionBreakdownDateUtil
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.databinding.CommissionBreakdownBottomsheetChooseDateBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.date.DateUtil
import java.text.SimpleDateFormat
import java.util.*

class CommissionBreakdownDateRangePickerBottomSheet : BottomSheetUnify() {

    companion object {
        const val MAX_RANGE_90 = 90L
        const val TAG = "CommissionBreakdownDateRangePickerBottomSheet"

        private const val MAX_RANGE = 30L
        private const val ARG_DATE_FROM = "ARG_DATE_FROM"
        private const val ARG_DATE_TO = "ARG_DATE_TO"
        private const val ARG_MAX_RANGE = "ARG_MAX_RANGE"
        private const val BOTTOM_SHEET_HEIGHT_3 = 3
        private const val BOTTOM_SHEET_HEIGHT_2 = 2
        private const val DATE_PATTERN = "dd MMMM yyyy"
        private const val JUNE_6_2020_IN_MILLIS = 1591012800000L
        private const val TOASTER_BOTTOM_MARGIN = 64

        fun getInstanceRange(
            dateFrom: Date?,
            dateTo: Date?,
            range: Long
        ): CommissionBreakdownDateRangePickerBottomSheet {
            return CommissionBreakdownDateRangePickerBottomSheet().apply {
                isFullpage = true
                val bundle = Bundle()
                bundle.putSerializable(ARG_DATE_FROM, dateFrom ?: Date())
                bundle.putSerializable(ARG_DATE_TO, dateTo ?: Date())
                bundle.putLong(ARG_MAX_RANGE, range)
                arguments = bundle
            }
        }
    }

    private val maxDate = Date()
    private val minDate = Date(JUNE_6_2020_IN_MILLIS)

    private var defaultDateFrom: Date? = null
    private var defaultDateTo: Date? = null
    private var newSelectedDateFrom: Date? = Date()
    private var newSelectedDateTo: Date? = Date()
    private var maxRange: Long = MAX_RANGE

    private var binding: CommissionBreakdownBottomsheetChooseDateBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        defaultDateFrom = (arguments?.getSerializable(ARG_DATE_FROM) as? Date) ?: Date()
        defaultDateTo = (arguments?.getSerializable(ARG_DATE_TO) as? Date) ?: Date()

        if (arguments?.containsKey(ARG_MAX_RANGE) == true) {
            arguments?.getLong(ARG_MAX_RANGE)?.let {
                maxRange = it
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setDefaultParams()
        binding = CommissionBreakdownBottomsheetChooseDateBinding.inflate(
            inflater,
            container,
            false
        )
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCalender()
        binding?.unifyButtonSelect?.setOnClickListener {
            val selectedStartDate = newSelectedDateFrom
            val selectedEndDate = newSelectedDateTo

            val isNewSelectedStartDateSame = CommissionBreakdownDateUtil.areTheDatesSame(
                selectedStartDate, defaultDateFrom
            )
            val isNewSelectedEndDateSame = CommissionBreakdownDateUtil.areTheDatesSame(
                selectedEndDate, defaultDateTo
            )
            if (selectedStartDate != null && selectedEndDate != null &&
                !(isNewSelectedStartDateSame && isNewSelectedEndDateSame)
            ) {
                (parentFragment as? OnDateRangeSelectListener)?.onDateRangeSelected(
                    selectedStartDate,
                    selectedEndDate
                )
            }
            dismissAllowingStateLoss()
        }

        binding?.tvSahCommissionStartDate?.isEnabled = false
        binding?.tvSahCommissionEndDate?.isEnabled = false
    }

    private fun initCalender() {
        newSelectedDateFrom = defaultDateFrom
        newSelectedDateTo = defaultDateTo
        val (startDate, endDate) = (newSelectedDateFrom to newSelectedDateTo)

        if (startDate != null && endDate != null) {
            binding?.calendarSahCommission?.run {
                calendarPickerView?.init(minDate, maxDate, arrayListOf())
                    ?.inMode(CalendarPickerView.SelectionMode.RANGE)
                    ?.maxRange(maxRange)
                    ?.withSelectedDates(arrayListOf(startDate, startDate))
                calendarPickerView?.selectDateClickListener()
                calendarPickerView?.outOfRange()
            }
        }
    }

    private fun CalendarPickerView.selectDateClickListener() {
        setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {

            override fun onDateSelected(date: Date) {
                val selectedDates = binding?.calendarSahCommission?.calendarPickerView
                    ?.selectedDates
                when (selectedDates?.size) {
                    Int.ONE -> {
                        newSelectedDateFrom = selectedDates.firstOrNull()
                        newSelectedDateTo = selectedDates.firstOrNull()
                    }
                    else -> {
                        newSelectedDateFrom = selectedDates?.firstOrNull()
                        newSelectedDateTo = selectedDates?.lastOrNull()
                    }
                }
                setDatePlaceholder()
            }

            override fun onDateUnselected(date: Date) {}
        })
    }

    private fun setDatePlaceholder() {
        val dateFormat = SimpleDateFormat(DATE_PATTERN, DateUtil.DEFAULT_LOCALE)

        binding?.tvSahCommissionStartDate?.apply {
            newSelectedDateFrom?.let {
                this.setPlaceholder(dateFormat.format(it))
            }
        }
        binding?.tvSahCommissionEndDate?.apply {
            newSelectedDateTo?.let {
                this.setPlaceholder(dateFormat.format(it))
            }
        }
    }

    private fun CalendarPickerView.outOfRange() {
        setMaxRangeListener(object : CalendarPickerView.OnMaxRangeListener {
            override fun onNotifyMax() {
                activity?.let {
                    showErrorToaster(
                        errorMessage = it.getString(
                            R.string.sp_title_max_day,
                            maxRange
                        )
                    )
                }
            }
        })
    }

    private fun showErrorToaster(errorMessage: String) {
        dialog?.window?.decorView?.let {
            Toaster.toasterCustomBottomHeight = context?.dpToPx(TOASTER_BOTTOM_MARGIN).toIntSafely()
            Toaster.build(it, errorMessage, Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
        }
    }

    private fun setDefaultParams() {
        showCloseIcon = true
        showHeader = true
        isHideable = true
        customPeekHeight = (getScreenHeight() / BOTTOM_SHEET_HEIGHT_3 * BOTTOM_SHEET_HEIGHT_2)
        setTitle(getString(R.string.sah_select_date_range))
    }

    interface OnDateRangeSelectListener {
        fun onDateRangeSelected(dateFrom: Date, dateTo: Date)
    }
}