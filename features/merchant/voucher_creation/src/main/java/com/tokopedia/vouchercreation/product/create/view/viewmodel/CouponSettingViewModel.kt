package com.tokopedia.vouchercreation.product.create.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import com.tokopedia.vouchercreation.common.utils.ResourceProvider
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponSettings
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponType
import com.tokopedia.vouchercreation.product.create.domain.entity.DiscountType
import com.tokopedia.vouchercreation.product.create.domain.entity.MinimumPurchaseType
import javax.inject.Inject

class CouponSettingViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _areInputValid = MutableLiveData<Boolean>()
    val areInputValid: LiveData<Boolean>
        get() = _areInputValid

    private val _couponType = MutableLiveData<CouponType>()
    val couponType: LiveData<CouponType>
        get() = _couponType

    private val _maxExpenseEstimation = MutableLiveData<Long>()
    val maxExpenseEstimation: LiveData<Long>
        get() = _maxExpenseEstimation

    private val _saveCoupon = SingleLiveEvent<CouponSettings>()
    val saveCoupon: SingleLiveEvent<CouponSettings>
        get() = _saveCoupon


    sealed class CashbackPercentageState {
        object BelowAllowedMinimumPercentage : CashbackPercentageState()
        object ExceedAllowedMaximumPercentage : CashbackPercentageState()
        object ValidPercentage : CashbackPercentageState()
    }

    sealed class CashbackAmountState {
        object BelowAllowedMinimumAmount : CashbackAmountState()
        object ExceedAllowedMinimumAmount : CashbackAmountState()
        object ValidAmount : CashbackAmountState()
    }

    sealed class QuotaState {
        object BelowAllowedQuotaAmount : QuotaState()
        object ExceedAllowedQuotaAmount : QuotaState()
        object ValidQuota : QuotaState()
    }

    companion object {
        private const val MINIMUM_CASHBACK_DISCOUNT_AMOUNT = 5_000
        private const val MINIMUM_FREE_SHIPPING_DISCOUNT_AMOUNT = 5_000
        private const val MINIMUM_FREE_SHIPPING_QUOTA = 1
        private const val MAXIMUM_FREE_SHIPPING_QUOTA = 999
        private const val MINIMUM_CASHBACK_PERCENTAGE = 2
        private const val MAXIMUM_CASHBACK_PERCENTAGE = 100
        private const val MINIMUM_CASHBACK_AMOUNT = 5_000
        private const val MAXIMUM_CASHBACK_AMOUNT = 99_999_999
        private const val ZERO = 0
    }

    fun validateInput(
        selectedCouponType: CouponType,
        selectedDiscountType: DiscountType,
        selectedMinimumPurchaseType: MinimumPurchaseType,
        cashbackPercentage : Int,
        cashbackMaximumAmount : Int,
        cashbackDiscountAmount: Int,
        cashbackMinimumPurchase: Int,
        cashbackQuota: Int,
        freeShippingDiscountAmount: Int,
        freeShippingMinimumPurchase: Int,
        freeShippingQuota: Int
    ) {

        if (selectedCouponType == CouponType.NONE) {
            _areInputValid.value = false
            return
        }

        if (selectedCouponType == CouponType.CASHBACK) {
            val discountTypeSelected = isDiscountTypeSelected(selectedDiscountType)
            val minimumPurchaseSelected = isMinimumPurchaseSelected(selectedMinimumPurchaseType)
            val validQuota = isValidQuota(cashbackQuota) is QuotaState.ValidQuota

            val validCashback = when (selectedDiscountType) {
                DiscountType.NOMINAL -> {
                    val validMinimumPurchase = isValidCashbackMinimumPurchase(
                        cashbackMinimumPurchase,
                        cashbackDiscountAmount,
                        selectedMinimumPurchaseType
                    )
                    val validDiscountAmount = isValidCashbackDiscountAmount(cashbackDiscountAmount)
                    discountTypeSelected && validDiscountAmount && minimumPurchaseSelected && validMinimumPurchase && validQuota
                }
                DiscountType.PERCENTAGE -> {
                    val validMinimumPurchase = isValidCashbackMinimumPurchase(
                        cashbackMinimumPurchase,
                        cashbackMaximumAmount,
                        selectedMinimumPurchaseType
                    )
                    val validCashbackPercentage = isValidCashbackPercentage(cashbackPercentage) is CashbackPercentageState.ValidPercentage
                    val validMaxDiscountAmount = isValidMaximumCashbackAmount(cashbackMaximumAmount) is CashbackAmountState.ValidAmount
                    discountTypeSelected && validCashbackPercentage && validMaxDiscountAmount && minimumPurchaseSelected && validMinimumPurchase && validQuota
                }
                DiscountType.NONE -> false

            }

            _areInputValid.value = validCashback
        } else {

            val validDiscountAmount = isValidFreeShippingDiscountAmount(freeShippingDiscountAmount)
            val validMinimumPurchase = isValidFreeShippingMinimumPurchase(
                freeShippingMinimumPurchase,
                freeShippingDiscountAmount
            )
            val validQuota = isValidQuota(freeShippingQuota) is QuotaState.ValidQuota
            val validFreeShipping = validDiscountAmount && validMinimumPurchase && validQuota

            _areInputValid.value = validFreeShipping
        }

    }


    private fun isDiscountTypeSelected(selectedDiscountType: DiscountType): Boolean {
        return selectedDiscountType != DiscountType.NONE
    }

    private fun isMinimumPurchaseSelected(selectedMinimumPurchaseType: MinimumPurchaseType): Boolean {
        return selectedMinimumPurchaseType != MinimumPurchaseType.NONE
    }

    fun isValidCashbackDiscountAmount(cashbackDiscountAmount: Int): Boolean {
        return cashbackDiscountAmount >= MINIMUM_CASHBACK_DISCOUNT_AMOUNT
    }

    fun isValidCashbackMinimumPurchase(
        minimumPurchase: Int,
        discountAmount: Int,
        selectedMinimumPurchaseType: MinimumPurchaseType
    ): Boolean {
        return when (selectedMinimumPurchaseType) {
            MinimumPurchaseType.NONE -> false
            MinimumPurchaseType.NOMINAL -> minimumPurchase >= discountAmount
            MinimumPurchaseType.QUANTITY -> minimumPurchase > ZERO
            MinimumPurchaseType.NOTHING -> true
        }
    }

    fun isValidFreeShippingDiscountAmount(freeShippingDiscountAmount: Int): Boolean {
        return freeShippingDiscountAmount >= MINIMUM_FREE_SHIPPING_DISCOUNT_AMOUNT
    }


    fun isValidCashbackPercentage(cashbackPercentage: Int): CashbackPercentageState {
        return when {
            cashbackPercentage < MINIMUM_CASHBACK_PERCENTAGE -> CashbackPercentageState.BelowAllowedMinimumPercentage
            cashbackPercentage > MAXIMUM_CASHBACK_PERCENTAGE -> CashbackPercentageState.ExceedAllowedMaximumPercentage
            else -> CashbackPercentageState.ValidPercentage
        }
    }

    fun isValidMaximumCashbackAmount(cashbackDiscountAmount: Int): CashbackAmountState {
        return when {
            cashbackDiscountAmount < MINIMUM_CASHBACK_AMOUNT -> CashbackAmountState.BelowAllowedMinimumAmount
            cashbackDiscountAmount > MAXIMUM_CASHBACK_AMOUNT -> CashbackAmountState.ExceedAllowedMinimumAmount
            else -> CashbackAmountState.ValidAmount
        }
    }

    fun isValidFreeShippingMinimumPurchase(
        freeShippingMinimumPurchase: Int,
        freeShippingDiscountAmount: Int
    ): Boolean {
        return freeShippingMinimumPurchase > freeShippingDiscountAmount
    }

    fun isValidQuota(quota: Int): QuotaState {
        return when {
            quota < MINIMUM_FREE_SHIPPING_QUOTA -> QuotaState.BelowAllowedQuotaAmount
            quota > MAXIMUM_FREE_SHIPPING_QUOTA -> QuotaState.ExceedAllowedQuotaAmount
            else -> QuotaState.ValidQuota
        }
    }

    fun couponTypeChanged(selectedCouponType: CouponType) {
        _areInputValid.value = false
        _couponType.value = selectedCouponType
    }

    fun getMinimalPurchaseErrorMessage(
        selectedCouponType: CouponType,
        selectedMinimumPurchaseType: MinimumPurchaseType
    ): String {
        return when {
            selectedCouponType == CouponType.CASHBACK && selectedMinimumPurchaseType == MinimumPurchaseType.NOMINAL -> resourceProvider.getInvalidMinimalPurchaseNominalErrorMessage()
            selectedCouponType == CouponType.CASHBACK && selectedMinimumPurchaseType == MinimumPurchaseType.QUANTITY -> resourceProvider.getInvalidMinimalQuantityQuantityErrorMessage()
            selectedCouponType == CouponType.FREE_SHIPPING -> resourceProvider.getInvalidMinimalPurchaseNominalErrorMessage()
            else -> resourceProvider.getInvalidMinimalPurchaseNominalErrorMessage()
        }
    }

    fun calculateMaxExpenseEstimation(
        selectedCouponType: CouponType,
        selectedDiscountType: DiscountType,
        cashbackDiscountAmount: Int,
        cashbackMaximumDiscountAmount : Int,
        freeShippingDiscountAmount : Int,
        cashbackQuota: Int,
        freeShippingQuota: Int
    ) {
        val discount = when {
            selectedCouponType == CouponType.CASHBACK && selectedDiscountType == DiscountType.NOMINAL -> cashbackDiscountAmount
            selectedCouponType == CouponType.CASHBACK && selectedDiscountType == DiscountType.PERCENTAGE -> cashbackMaximumDiscountAmount
            selectedCouponType == CouponType.FREE_SHIPPING -> freeShippingDiscountAmount
            else -> ZERO
        }

        val quota = if (selectedCouponType == CouponType.CASHBACK) {
            cashbackQuota
        } else {
            freeShippingQuota
        }
        _maxExpenseEstimation.value = (discount * quota).toLong()
    }

    fun saveCoupon(
        selectedCouponType: CouponType,
        selectedDiscountType: DiscountType,
        selectedMinimumPurchaseType: MinimumPurchaseType,
        cashbackPercentage: Int,
        cashbackMaximumAmount: Int,
        cashbackDiscountAmount: Int,
        cashbackMinimumPurchase: Int,
        cashbackQuota: Int,
        freeShippingDiscountAmount: Int,
        freeShippingMinimumPurchase: Int,
        freeShippingQuota: Int,
        estimatedMaxExpense: Long
    ) {

        val discountAmount = if (selectedCouponType == CouponType.CASHBACK) {
            cashbackDiscountAmount
        } else {
            freeShippingDiscountAmount
        }

        val quota = if (selectedCouponType == CouponType.CASHBACK) {
            cashbackQuota
        } else {
            freeShippingQuota
        }


        val minimumPurchase = when {
            selectedCouponType == CouponType.CASHBACK && selectedMinimumPurchaseType == MinimumPurchaseType.NOTHING -> ZERO
            selectedCouponType == CouponType.CASHBACK -> cashbackMinimumPurchase
            selectedCouponType == CouponType.FREE_SHIPPING -> freeShippingMinimumPurchase
            else -> cashbackMinimumPurchase
        }

        val cashbackMax = when (selectedDiscountType) {
            DiscountType.NONE -> discountAmount
            DiscountType.NOMINAL -> discountAmount
            DiscountType.PERCENTAGE -> cashbackMaximumAmount
        }

        val adjustedCashbackPercentage = when(selectedCouponType) {
            CouponType.NONE -> cashbackPercentage
            CouponType.CASHBACK -> MAXIMUM_CASHBACK_PERCENTAGE
            CouponType.FREE_SHIPPING -> cashbackPercentage
        }

        _saveCoupon.value = CouponSettings(
            selectedCouponType,
            selectedDiscountType,
            selectedMinimumPurchaseType,
            discountAmount,
            adjustedCashbackPercentage,
            cashbackMax,
            quota,
            minimumPurchase,
            estimatedMaxExpense
        )
    }

}