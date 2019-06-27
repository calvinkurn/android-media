package com.tokopedia.topads.common.view

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.tokopedia.datepicker.range.view.constant.DatePickerConstant
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.domain.interactor.TopAdsDatePickerInteractor
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class TopAdsDatePickerViewModel @Inject
    constructor(private val topAdsDatePickerInteractor: TopAdsDatePickerInteractor): ViewModel(){

    val dateRange = MutableLiveData<DateRange>()

    val lastSelectionDatePickerIndex: Int
        get() = topAdsDatePickerInteractor.lastSelectionDatePickerIndex

    val lastSelectionDatePickerType: Int
        get() = topAdsDatePickerInteractor.lastSelectionDatePickerType

    private val _startDate: Date
        get() {
            val startCalendar = Calendar.getInstance()
            startCalendar.add(Calendar.DAY_OF_YEAR, -DatePickerConstant.DIFF_ONE_WEEK)
            return topAdsDatePickerInteractor.getStartDate(startCalendar.time)
        }

    private val _endDate: Date
        get() {
            val endCalendar = Calendar.getInstance()
            return topAdsDatePickerInteractor.getEndDate(endCalendar.time)
        }

    fun checkUpdatedDate(){
        if (isDateNeedUpdated()){
            dateRange.value = DateRange(_startDate, _endDate)
        }
    }

    private fun isDateNeedUpdated(): Boolean {
        val range = dateRange.value?.copy() ?: return true

        var dateText = SimpleDateFormat(TopAdsCommonConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(range.startDate)
        var cachedDateText = SimpleDateFormat(TopAdsCommonConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(_startDate)
        if (!dateText.equals(cachedDateText, true)) return true

        dateText = SimpleDateFormat(TopAdsCommonConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(range.endDate)
        cachedDateText = SimpleDateFormat(TopAdsCommonConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(_endDate)
        return !dateText.equals(cachedDateText, true)
    }

    fun saveDate(startDate: Date, endDate: Date) {
        topAdsDatePickerInteractor.saveDate(startDate, endDate)
        dateRange.value = DateRange(startDate, endDate)
    }

    fun saveSelectionDatePicker(selectionType: Int, lastSelection: Int) {
        topAdsDatePickerInteractor.saveSelectionDatePicker(selectionType, lastSelection)
    }

    fun resetDate() {
        topAdsDatePickerInteractor.resetDate()
    }

    data class DateRange(val startDate: Date, val endDate: Date)
}