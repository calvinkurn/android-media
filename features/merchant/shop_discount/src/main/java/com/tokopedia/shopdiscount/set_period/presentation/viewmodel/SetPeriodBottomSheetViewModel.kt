package com.tokopedia.shopdiscount.set_period.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shopdiscount.bulk.data.response.GetSlashPriceBenefitResponse
import com.tokopedia.shopdiscount.bulk.domain.usecase.GetSlashPriceBenefitUseCase
import com.tokopedia.shopdiscount.set_period.data.uimodel.SetPeriodResultUiModel
import com.tokopedia.shopdiscount.utils.constant.EMPTY_STRING
import com.tokopedia.shopdiscount.utils.extension.toCalendar
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class SetPeriodBottomSheetViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getSlashPriceBenefitUseCase: GetSlashPriceBenefitUseCase
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val ONE_YEAR = 1
        private const val SIX_MONTH = 6
        private const val ONE_MONTH = 1
        private const val START_TIME_OFFSET_IN_MINUTES = 10
    }

    private val _startDate = MutableLiveData<Date>()
    val startDate: LiveData<Date>
        get() = _startDate

    private val _endDate = MutableLiveData<Date>()
    val endDate: LiveData<Date>
        get() = _endDate

    private var selectedStartDate: Date = Date()
    private var selectedEndDate: Date = Date()
    private var maxDate: Date = Date()

    private val _benefit = MutableLiveData<Result<GetSlashPriceBenefitResponse>>()
    val benefit: LiveData<Result<GetSlashPriceBenefitResponse>>
        get() = _benefit
    private var benefitPackageName = EMPTY_STRING

    val defaultStartDate: Date by lazy {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, START_TIME_OFFSET_IN_MINUTES)
        calendar.time
    }

    val defaultMembershipEndDate: Date by lazy {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, ONE_YEAR)
        calendar.time
    }

    fun getSlashPriceBenefit() {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                getSlashPriceBenefitUseCase.setParams()
                getSlashPriceBenefitUseCase.executeOnBackground()
            }
            _benefit.value = Success(result)
        }, onError = {
                _benefit.value = Fail(it)
            })
    }

    fun onOneYearPeriodSelected() {
        val endDateCalendar = defaultStartDate.toCalendar()
        endDateCalendar.add(Calendar.YEAR, ONE_YEAR)
        this.selectedStartDate = defaultStartDate
        this.selectedEndDate = endDateCalendar.time

        _startDate.value = defaultStartDate
        _endDate.value = endDateCalendar.time
    }

    fun onSixMonthPeriodSelected() {
        val endDateCalendar = defaultStartDate.toCalendar()
        endDateCalendar.add(Calendar.MONTH, SIX_MONTH)

        this.selectedStartDate = defaultStartDate
        this.selectedEndDate = endDateCalendar.time

        _startDate.value = defaultStartDate
        _endDate.value = endDateCalendar.time
    }

    fun onOneMonthPeriodSelected() {
        val endDateCalendar = defaultStartDate.toCalendar()
        endDateCalendar.add(Calendar.MONTH, ONE_MONTH)

        this.selectedStartDate = defaultStartDate
        this.selectedEndDate = endDateCalendar.time

        _startDate.value = defaultStartDate
        _endDate.value = endDateCalendar.time
    }

    fun getCurrentSelection(): SetPeriodResultUiModel {
        return SetPeriodResultUiModel(
            selectedStartDate,
            selectedEndDate
        )
    }

    fun getSelectedStartDate(): Date {
        return selectedStartDate
    }

    fun getSelectedEndDate(): Date {
        return selectedEndDate
    }

    fun setSelectedStartDate(startDate: Date) {
        selectedStartDate = startDate
        _startDate.value = startDate
    }

    fun setSelectedEndDate(endDate: Date) {
        selectedEndDate = endDate
        _endDate.value = endDate
    }

    fun setBenefitPackageName(benefitPackageName: String) {
        this.benefitPackageName = benefitPackageName
    }

    fun getBenefitPackageName(): String {
        return benefitPackageName
    }

    fun setMaxDate(date: Date) {
        maxDate = date
    }

    fun getMaxDate(): Date {
        return maxDate
    }
}
