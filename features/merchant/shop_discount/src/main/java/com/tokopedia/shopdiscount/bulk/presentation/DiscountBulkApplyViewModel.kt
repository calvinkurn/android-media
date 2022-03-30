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
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class DiscountBulkApplyViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getSlashPriceBenefitUseCase: GetSlashPriceBenefitUseCase
) : BaseViewModel(dispatchers.main) {

    private val _startDate = MutableLiveData<Date>()
    val startDate: LiveData<Date>
        get() = _startDate

    private val _endDate = MutableLiveData<Date>()
    val endDate: LiveData<Date>
        get() = _endDate

    private val _areInputValid = MutableLiveData<Boolean>()
    val areInputValid: LiveData<Boolean>
        get() = _areInputValid

    private var currentlySelectedStartDate = Date()
    private var currentlySelectedEndDate = Date()
    private var selectedDiscountType = DiscountType.RUPIAH
    private var selectedDiscountAmount = 0
    private var selectedMaxQuantity = 1
    private var mode : DiscountBulkApplyBottomSheet.Mode = DiscountBulkApplyBottomSheet.Mode.SHOW_ALL_FIELDS

    private val _benefit = MutableLiveData<Result<GetSlashPriceBenefitResponse>>()
    val benefit: LiveData<Result<GetSlashPriceBenefitResponse>>
        get() = _benefit


    fun getSlashPriceBenefit() {
        launchCatchError(block = {
            delay(2000)
            val result = withContext(dispatchers.io) {
                getSlashPriceBenefitUseCase.execute()
            }
            _benefit.value = Success(result)
        }, onError = {
            _benefit.value = Fail(it)
        })
    }

    fun validateInput() {
        if (mode == DiscountBulkApplyBottomSheet.Mode.SHOW_ALL_FIELDS) {
            val isValid = isValidDiscount(getCurrentSelection())
            _areInputValid.value = isValid
            return
        }

        if (mode == DiscountBulkApplyBottomSheet.Mode.HIDE_PERIOD_FIELDS) {
            val isValid = isValidNonPeriodDiscount(getCurrentSelection())
            _areInputValid.value = isValid
            return
        }
    }

    private fun isValidDiscount(discountSettings: DiscountSettings): Boolean {
        if (discountSettings.startDate == null) {
            return false
        }

        if (discountSettings.endDate == null) {
            return false
        }

        if (discountSettings.discountAmount == 0) {

            return false
        }

        if (discountSettings.maxPurchaseQuantity == 0) {
            return false
        }

        return true
    }

    private fun isValidNonPeriodDiscount(discountSettings: DiscountSettings): Boolean {
        if (discountSettings.discountAmount == 0) {
            return false
        }

        if (discountSettings.maxPurchaseQuantity == 0) {
            return false
        }

        return true
    }

    fun onOneYearPeriodSelected() {
        val now = Date()
        val endDate = Calendar.getInstance()
        endDate.add(Calendar.YEAR, 1)

        this.currentlySelectedStartDate = now
        this.currentlySelectedEndDate = endDate.time

        _startDate.value = now
        _endDate.value = endDate.time
    }

    fun onSixMonthPeriodSelected() {
        val now = Date()
        val endDate = Calendar.getInstance()
        endDate.add(Calendar.MONTH, 6)

        this.currentlySelectedStartDate = now
        this.currentlySelectedEndDate = endDate.time

        _startDate.value = now
        _endDate.value = endDate.time
    }

    fun onOneMonthPeriodSelected() {
        val now = Date()
        val endDate = Calendar.getInstance()
        endDate.add(Calendar.MONTH, 1)

        this.currentlySelectedStartDate = now
        this.currentlySelectedEndDate = endDate.time

        _startDate.value = now
        _endDate.value = endDate.time
    }

    fun onCustomSelectionPeriodSelected(startDate: Date, endDate: Date) {
        this.currentlySelectedStartDate = startDate
        this.currentlySelectedEndDate = endDate

        _startDate.value = startDate
        _endDate.value = endDate
    }

    fun onDiscountTypeChanged(discountType: DiscountType) {
        this.selectedDiscountType = discountType
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

    fun setCurrentlySelectedStartDate(currentlySelectedStartDate: Date) {
        this.currentlySelectedStartDate = currentlySelectedStartDate
    }

    fun setCurrentlySelectedEndDate(currentlySelectedEndDate: Date) {
        this.currentlySelectedEndDate = currentlySelectedEndDate
    }


    fun getCurrentSelection(): DiscountSettings {
        return DiscountSettings(
            currentlySelectedStartDate,
            currentlySelectedEndDate,
            selectedDiscountType,
            selectedDiscountAmount,
            selectedMaxQuantity
        )
    }

}