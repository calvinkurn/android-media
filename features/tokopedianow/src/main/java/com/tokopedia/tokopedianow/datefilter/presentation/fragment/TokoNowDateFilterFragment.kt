package com.tokopedia.tokopedianow.datefilter.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.date_filter.DateFilterUnify
import com.example.date_filter.DateFilterUnifyDateItem
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.kotlin.extensions.getCalculatedFormattedDate
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.util.DateUtil
import com.tokopedia.tokopedianow.common.util.DateUtil.getGregorianCalendar
import com.tokopedia.tokopedianow.datefilter.presentation.activity.TokoNowDateFilterActivity.Companion.EXTRA_SELECTED_DATE_FILTER
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseSortFilterUiModel.*
import java.util.GregorianCalendar
import java.util.Calendar
import java.util.Date

class TokoNowDateFilterFragment: Fragment() {

    companion object {
        const val ALL_DATE_TRANSACTION_POSITION = 0
        const val LAST_ONE_MONTH_POSITION = 1
        const val LAST_THREE_MONTHS_POSITION = 2
        const val CUSTOM_DATE_POSITION = 3

        private const val MIN_30_DAYS = -30
        private const val MIN_90_DAYS = -90
        private const val DATE_FORMAT = "yyyy-MM-dd"
        private const val DEFAULT_MIN_DATE = "2018-09-01"

        private val TAG = TokoNowDateFilterFragment::class.simpleName

        fun newInstance(selectedFilter: SelectedDateFilter?): TokoNowDateFilterFragment {
            return TokoNowDateFilterFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_SELECTED_DATE_FILTER, selectedFilter)
                }
            }
        }
    }

    private var dateFilterBottomSheet: DateFilterUnify? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tokopedianow_date_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomSheet()
    }

    private fun setupBottomSheet() {
        context?.apply {
            val selectedFilter = arguments?.getParcelable<SelectedDateFilter>(EXTRA_SELECTED_DATE_FILTER)
            dateFilterBottomSheet = DateFilterUnify(this,  childFragmentManager, TAG.orEmpty())
            setupDataFilterOptions(selectedFilter)
            setupCustomDatePicker(selectedFilter)
            dateFilterBottomSheet?.submitButtonView?.text = getString(R.string.tokopedianow_date_filter_apply_filter_bottomshet)
            dateFilterBottomSheet?.onSubmit = { position, startDate, endDate ->
                setupSubmittingData(position, startDate, endDate, selectedFilter)
            }
            dateFilterBottomSheet?.setOnDismissListener {
                activity?.finish()
            }
            dateFilterBottomSheet?.show()
        }
    }

    private fun setupDataFilterOptions(selectedFilter: SelectedDateFilter?) {
        dateFilterBottomSheet?.addDataFilterOption(
            title = getString(R.string.tokopedianow_date_filter_all_date_transactions_chip_and_item_bottomsheet_title),
            isSelected = selectedFilter?.position == ALL_DATE_TRANSACTION_POSITION
        )
        dateFilterBottomSheet?.addDataFilterOption(
            title = getString(R.string.tokopedianow_date_filter_last_one_month_chip_and_item_bottomsheet_title),
            isSelected = selectedFilter?.position == LAST_ONE_MONTH_POSITION
        )
        dateFilterBottomSheet?.addDataFilterOption(
            title = getString(R.string.tokopedianow_date_filter_last_three_months_chip_and_item_bottomsheet_title),
            isSelected = selectedFilter?.position == LAST_THREE_MONTHS_POSITION
        )
    }

    private fun setupCustomDatePicker(selectedFilter: SelectedDateFilter?) {
        context?.apply {
            val startDate = selectedCustomDate(
                selectedDate = selectedFilter?.startDate.orEmpty(),
                defaultDate = DEFAULT_MIN_DATE
            )
            val endDate = selectedCustomDate(
                selectedDate = selectedFilter?.endDate.orEmpty(),
                defaultDate = LocaleUtils.getCurrentLocale(this).toString()
            )
            val startDatePicker = DateFilterUnifyDateItem(
                minDate = getGregorianCalendar(DEFAULT_MIN_DATE),
                defaultDate = startDate,
                maxDate = endDate,
                datePickerType = DateTimePickerUnify.TYPE_DATEPICKER
            )
            val endDatePicker = DateFilterUnifyDateItem(
                minDate = startDate,
                defaultDate = endDate,
                maxDate = GregorianCalendar(LocaleUtils.getCurrentLocale(this)),
                datePickerType = DateTimePickerUnify.TYPE_DATEPICKER
            )
            dateFilterBottomSheet?.setCustomDatePicker(
                startDateData = startDatePicker,
                endDateData= endDatePicker,
                isSelected = selectedFilter?.position == CUSTOM_DATE_POSITION
            )

            dateFilterBottomSheet?.onCustomDateChanged = { newDate, isStartDate ->
                if(isStartDate){
                    endDatePicker.minDate = newDate
                } else {
                    startDatePicker.maxDate = newDate
                }
            }
        }
    }

    private fun setupSubmittingData(position: Int, newStartDate: Calendar?, newEndDate: Calendar?, selectedFilter: SelectedDateFilter?) {
        when (position) {
            ALL_DATE_TRANSACTION_POSITION -> {
                selectedFilter?.startDate = ""
                selectedFilter?.endDate = ""
            }
            LAST_ONE_MONTH_POSITION -> {
                selectedFilter?.startDate = getCalculatedFormattedDate(DATE_FORMAT, MIN_30_DAYS).toString()
                selectedFilter?.endDate = Date().toFormattedString(DATE_FORMAT)
            }
            LAST_THREE_MONTHS_POSITION -> {
                selectedFilter?.startDate = getCalculatedFormattedDate(DATE_FORMAT, MIN_90_DAYS).toString()
                selectedFilter?.endDate = Date().toFormattedString(DATE_FORMAT)
            }
            CUSTOM_DATE_POSITION -> {
                selectedFilter?.startDate = convertCalendarToStringWithFormat(newStartDate)
                selectedFilter?.endDate = convertCalendarToStringWithFormat(newEndDate)
            }
        }
        selectedFilter?.position = position

        val intent = Intent()
        intent.putExtra(EXTRA_SELECTED_DATE_FILTER, selectedFilter)
        activity?.setResult(Activity.RESULT_OK, intent)
        dateFilterBottomSheet?.dismiss()
    }

    private fun selectedCustomDate(selectedDate: String, defaultDate: String): Calendar {
        return if (selectedDate.isBlank()) {
            getGregorianCalendar(defaultDate)
        } else {
            getGregorianCalendar(selectedDate)
        }
    }

    private fun convertCalendarToStringWithFormat(date: Calendar?): String {
        return date?.let { DateUtil.calendarToStringFormat(it, DATE_FORMAT) }.toString()
    }
}