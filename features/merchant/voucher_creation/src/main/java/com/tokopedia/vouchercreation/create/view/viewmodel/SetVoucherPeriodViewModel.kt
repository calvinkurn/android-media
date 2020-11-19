package com.tokopedia.vouchercreation.create.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.vouchercreation.create.domain.usecase.validation.PeriodValidationUseCase
import com.tokopedia.vouchercreation.create.view.uimodel.validation.PeriodValidation
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class SetVoucherPeriodViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val periodValidationUseCase: PeriodValidationUseCase
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val DATE_FORMAT = "yyyy-MM-dd"
        private const val HOUR_FORMAT = "HH:mm"
    }

    private val mDateStartLiveData = MutableLiveData<String>()
    val dateStartLiveData: LiveData<String>
        get() = mDateStartLiveData
    private val mDateEndLiveData = MutableLiveData<String>()
    val dateEndLiveData: LiveData<String>
        get() = mDateEndLiveData
    private val mHourStartLiveData = MutableLiveData<String>()
    val hourStartLiveData: LiveData<String>
        get() = mHourStartLiveData
    private val mHourEndLiveData = MutableLiveData<String>()
    val hourEndLiveData: LiveData<String>
        get() = mHourEndLiveData

    private val mPeriodValidationLiveData = MutableLiveData<Result<PeriodValidation>>()
    val periodValidationLiveData: LiveData<Result<PeriodValidation>>
        get() = mPeriodValidationLiveData

    private val mStartDateCalendarLiveData = MutableLiveData<Calendar>()
    val startDateCalendarLiveData: LiveData<Calendar>
        get() = mStartDateCalendarLiveData
    private val mEndDateCalendarLiveData = MutableLiveData<Calendar>()
    val endDateCalendarLiveData: LiveData<Calendar>
        get() = mEndDateCalendarLiveData

    fun setStartDateCalendar(startDate: Calendar) {
        mStartDateCalendarLiveData.value = startDate
        mDateStartLiveData.value = startDate.time.toFormattedString(DATE_FORMAT)
        mHourStartLiveData.value = startDate.time.toFormattedString(HOUR_FORMAT)
    }

    fun setEndDateCalendar(endDate: Calendar) {
        mEndDateCalendarLiveData.value = endDate
        mDateEndLiveData.value = endDate.time.toFormattedString(DATE_FORMAT)
        mHourEndLiveData.value = endDate.time.toFormattedString(HOUR_FORMAT)
    }

    fun validateVoucherPeriod() {
        mDateStartLiveData.value?.let { dateStart ->
            mDateEndLiveData.value?.let { dateEnd ->
                mHourStartLiveData.value?.let { hourStart ->
                    mHourEndLiveData.value?.let { hourEnd ->
                        launchCatchError(
                                block = {
                                    mPeriodValidationLiveData.value = Success(withContext(dispatchers.io) {
                                        periodValidationUseCase.params = PeriodValidationUseCase.createRequestParam(dateStart, dateEnd, hourStart, hourEnd)
                                        periodValidationUseCase.executeOnBackground()
                                    })
                                },
                                onError = {
                                    mPeriodValidationLiveData.value = Fail(it)
                                }
                        )
                    }
                }
            }
        }
    }

}