package com.tokopedia.shopdiscount.bulk.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shopdiscount.bulk.data.response.GetSlashPriceBenefitResponse
import com.tokopedia.shopdiscount.bulk.domain.entity.DiscountSettings
import com.tokopedia.shopdiscount.bulk.domain.entity.DiscountType
import com.tokopedia.shopdiscount.bulk.domain.usecase.GetSlashPriceBenefitUseCase
import com.tokopedia.shopdiscount.utils.constant.EMPTY_STRING
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class DiscountBulkApplyViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getSlashPriceBenefitUseCase: GetSlashPriceBenefitUseCase
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val MINIMUM_DISCOUNT_AMOUNT = 100
        private const val MINIMUM_DISCOUNT_PERCENTAGE = 1
        private const val MAXIMUM_DISCOUNT_PERCENTAGE = 99
        private const val MAXIMUM_PURCHASE_QUANTITY = 99_999
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

    private val _areInputValid = MutableLiveData<ValidationState>()
    val areInputValid: LiveData<ValidationState>
        get() = _areInputValid

    private val _discountType = MutableLiveData<DiscountType>()
    val discountType: LiveData<DiscountType>
        get() = _discountType

    private var benefitPackageName = EMPTY_STRING

    sealed class ValidationState {
        object InvalidDiscountAmount : ValidationState()
        object InvalidDiscountPercentage : ValidationState()
        object InvalidMaxPurchase : ValidationState()
        object Valid : ValidationState()
    }

    private var selectedStartDate : Date? = null
    private var selectedEndDate : Date? = null
    private var selectedDiscountType = DiscountType.RUPIAH
    private var selectedDiscountAmount = 0
    private var selectedMaxQuantity = 1
    private var mode : DiscountBulkApplyBottomSheet.Mode = DiscountBulkApplyBottomSheet.Mode.SHOW_ALL_FIELDS

    private val _benefit = MutableLiveData<Result<GetSlashPriceBenefitResponse>>()
    val benefit: LiveData<Result<GetSlashPriceBenefitResponse>>
        get() = _benefit


    fun getSlashPriceBenefit() {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                getSlashPriceBenefitUseCase.execute()
            }
            _benefit.value = Success(result)
        }, onError = {
            _benefit.value = Fail(it)
        })
    }

    fun validateInput() {
        if (selectedDiscountType == DiscountType.RUPIAH && selectedDiscountAmount < MINIMUM_DISCOUNT_AMOUNT) {
            _areInputValid.value = ValidationState.InvalidDiscountAmount
            return
        }

        if (selectedDiscountType == DiscountType.PERCENTAGE && selectedDiscountAmount !in MINIMUM_DISCOUNT_PERCENTAGE..MAXIMUM_DISCOUNT_PERCENTAGE) {
            _areInputValid.value = ValidationState.InvalidDiscountPercentage
            return
        }

        if (selectedMaxQuantity >= MAXIMUM_PURCHASE_QUANTITY) {
            _areInputValid.value = ValidationState.InvalidMaxPurchase
            return
        }

        _areInputValid.value = ValidationState.Valid
    }
    
    fun onOneYearPeriodSelected() {
        val startDate = getDefaultStartDate()
        val endDate = Calendar.getInstance()
        endDate.add(Calendar.YEAR, ONE_YEAR)

        this.selectedStartDate = startDate
        this.selectedEndDate = endDate.time

        _startDate.value = startDate
        _endDate.value = endDate.time
    }

    fun onSixMonthPeriodSelected() {
        val startDate = getDefaultStartDate()
        val endDate = Calendar.getInstance()
        endDate.add(Calendar.MONTH, SIX_MONTH)

        this.selectedStartDate = startDate
        this.selectedEndDate = endDate.time

        _startDate.value = startDate
        _endDate.value = endDate.time
    }

    fun onOneMonthPeriodSelected() {
        val startDate = getDefaultStartDate()
        val endDate = Calendar.getInstance()
        endDate.add(Calendar.MONTH, ONE_MONTH)

        this.selectedStartDate = startDate
        this.selectedEndDate = endDate.time

        _startDate.value = startDate
        _endDate.value = endDate.time
    }

    fun onCustomSelectionPeriodSelected() {
        val startDate = getDefaultStartDate()
        val endDate = Calendar.getInstance()
        endDate.add(Calendar.MONTH, SIX_MONTH)

        this.selectedStartDate = startDate
        this.selectedEndDate = endDate.time

        _startDate.value = startDate
        _endDate.value = endDate.time
    }

    fun onDiscountTypeChanged(discountType: DiscountType) {
        this.selectedDiscountType = discountType
        _discountType.value = discountType
    }

    fun onDiscountAmountChanged(discountAmount: Int) {
        this.selectedDiscountAmount = discountAmount
    }

    fun onMaxPurchaseQuantityChanged(quantity: Int) {
        this.selectedMaxQuantity = quantity
    }

    fun setMode(mode: DiscountBulkApplyBottomSheet.Mode) {
        this.mode = mode
    }

    fun getCurrentSelection(): DiscountSettings {
        return DiscountSettings(
            selectedStartDate,
            selectedEndDate,
            selectedDiscountType,
            selectedDiscountAmount,
            selectedMaxQuantity
        )
    }

    private fun getDefaultStartDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, START_TIME_OFFSET_IN_MINUTES)
        return calendar.time
    }

    fun getSelectedStartDate() : Date {
        return selectedStartDate ?: Date()
    }

    fun getSelectedEndDate() : Date {
        return selectedEndDate ?: Date()
    }

    fun setSelectedStartDate(startDate: Date) {
        selectedStartDate = startDate
        _startDate.value = startDate
    }

    fun setSelectedEndDate(endDate: Date) {
        selectedEndDate = endDate
        _endDate.value = endDate
    }

    fun setBenefitPackageName(benefitPackageName : String) {
        this.benefitPackageName = benefitPackageName
    }

    fun getBenefitPackageName(): String {
        return benefitPackageName
    }
}