package com.tokopedia.mvc.presentation.bottomsheet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.toFormattedString
import java.util.*
import javax.inject.Inject

class VoucherEditPeriodViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val DATE_FORMAT = "yyyy-MM-dd"
        private const val HOUR_FORMAT = "HH:mm"
    }

    private val _fullStartDateLiveData = MutableLiveData<String>()
    val fullStartDateLiveData: LiveData<String>
        get() = _fullStartDateLiveData
    private val _fullEndDateLiveData = MutableLiveData<String>()
    val fullEndDateLiveData: LiveData<String>
        get() = _fullEndDateLiveData

    private val _dateStartLiveData = MutableLiveData<String>()
    private val _dateEndLiveData = MutableLiveData<String>()
    private val _hourStartLiveData = MutableLiveData<String>()
    val hourStartLiveData: LiveData<String>
        get() = _hourStartLiveData
    private val _hourEndLiveData = MutableLiveData<String>()

    private val _startDateCalendarLiveData = MutableLiveData<Calendar>()
    val startDateCalendarLiveData: LiveData<Calendar>
        get() = _startDateCalendarLiveData
    private val _endDateCalendarLiveData = MutableLiveData<Calendar>()
    val endDateCalendarLiveData: LiveData<Calendar>
        get() = _endDateCalendarLiveData

    fun setStartDateTime(startDate: Calendar?) {
        _startDateCalendarLiveData.value = startDate
        _dateStartLiveData.value = startDate?.time?.toFormattedString(DATE_FORMAT)
        _hourStartLiveData.value = startDate?.time?.toFormattedString(HOUR_FORMAT)
    }

    fun setEndDateTime(endDate: Calendar?) {
        _endDateCalendarLiveData.value = endDate
        _dateEndLiveData.value = endDate?.time?.toFormattedString(DATE_FORMAT)
        _hourEndLiveData.value = endDate?.time?.toFormattedString(HOUR_FORMAT)
    }

    fun setStartDate(startDate: String) {
        _fullStartDateLiveData.value = startDate
    }

    fun setEndDate(endDate: String) {
        _fullEndDateLiveData.value = endDate
    }
}
