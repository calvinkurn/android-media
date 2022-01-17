package com.tokopedia.vouchercreation.product.create.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponType
import com.tokopedia.vouchercreation.product.create.domain.entity.DiscountType
import com.tokopedia.vouchercreation.product.create.domain.entity.MinimumPurchaseType
import javax.inject.Inject

class CouponSettingViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _areInputValid = MutableLiveData<ValidationResult>()
    val areInputValid: LiveData<ValidationResult>
        get() = _areInputValid

    companion object {
        private const val MINIMUM_CASHBACK_DISCOUNT_AMOUNT = 5_000
        private const val MINIMUM_FREE_SHIPPING_DISCOUNT_AMOUNT = 5_000
        private const val MINIMUM_CASHBACK_QUOTA = 1
        private const val MINIMUM_FREE_SHIPPING_QUOTA = 1
    }

    sealed class ValidationResult {
        object CouponTypeNotSelected : ValidationResult()

        sealed class Cashback : ValidationResult() {
            object DiscountTypeNotSelected : Cashback()
            object MinimumPurchaseTypeNotSelected : Cashback()
            object InvalidDiscountAmount : Cashback()
            object InvalidMinimumPurchase : Cashback()
            object InvalidQuota : Cashback()
            object Valid : Cashback()
        }

        sealed class FreeShipping : ValidationResult() {
            object InvalidFreeShippingAmount : FreeShipping()
            object InvalidMinimumPurchase : FreeShipping()
            object InvalidQuota : FreeShipping()
            object Valid : FreeShipping()
        }

        object Valid : ValidationResult()
    }

    fun validateInput(
        selectedCouponType: CouponType,
        selectedDiscountType: DiscountType,
        selectedMinimumPurchaseType: MinimumPurchaseType,
        cashbackDiscountAmount: Int,
        cashbackMinimumPurchase: Int,
        cashbackQuota: Int,
        freeShippingDiscountAmount: Int,
        freeShippingMinimumPurchase: Int,
        freeShippingQuota: Int
    ) {

        if (selectedCouponType == CouponType.NONE) {
            _areInputValid.value = ValidationResult.CouponTypeNotSelected
            return
        }

        if (selectedCouponType == CouponType.CASHBACK) {
            val validCashbackCoupon =
                isValidCashback(
                    selectedDiscountType,
                    selectedMinimumPurchaseType,
                    cashbackDiscountAmount,
                    cashbackMinimumPurchase,
                    cashbackQuota
                )

            _areInputValid.value = validCashbackCoupon

        } else {
            val validFreeShippingCoupon = isValidFreeShipping(
                freeShippingDiscountAmount,
                freeShippingMinimumPurchase,
                freeShippingQuota
            )
            _areInputValid.value = validFreeShippingCoupon
        }

    }

    private fun isValidCashback(
        selectedDiscountType: DiscountType,
        selectedMinimumPurchaseType: MinimumPurchaseType,
        cashbackDiscountAmount: Int,
        cashbackMinimumPurchase: Int,
        cashbackQuota: Int
    ): ValidationResult {

        if (selectedDiscountType == DiscountType.NONE) {
            return ValidationResult.Cashback.DiscountTypeNotSelected
        }

        if (selectedMinimumPurchaseType == MinimumPurchaseType.NONE) {
            return ValidationResult.Cashback.MinimumPurchaseTypeNotSelected
        }

        if (cashbackDiscountAmount < MINIMUM_CASHBACK_DISCOUNT_AMOUNT) {
            return ValidationResult.Cashback.InvalidDiscountAmount
        }

        if (cashbackMinimumPurchase < cashbackDiscountAmount) {
            return ValidationResult.Cashback.InvalidMinimumPurchase
        }

        if (cashbackQuota < MINIMUM_CASHBACK_QUOTA) {
            return ValidationResult.Cashback.InvalidQuota
        }

        return ValidationResult.Cashback.Valid
    }

    private fun isValidFreeShipping(
        freeShippingDiscountAmount: Int,
        freeShippingMinimumPurchase: Int,
        freeShippingQuota: Int
    ): ValidationResult {
        if (freeShippingDiscountAmount < MINIMUM_FREE_SHIPPING_DISCOUNT_AMOUNT) {
            return ValidationResult.FreeShipping.InvalidFreeShippingAmount
        }

        if (freeShippingMinimumPurchase < freeShippingDiscountAmount) {
            return ValidationResult.FreeShipping.InvalidMinimumPurchase
        }

        if (freeShippingQuota < MINIMUM_FREE_SHIPPING_QUOTA) {
            return ValidationResult.FreeShipping.InvalidQuota
        }

        return ValidationResult.FreeShipping.Valid
    }
}