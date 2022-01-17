package com.tokopedia.vouchercreation.product.create.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.vouchercreation.common.utils.ResourceProvider
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

    
    companion object {
        private const val MINIMUM_CASHBACK_DISCOUNT_AMOUNT = 5_000
        private const val MINIMUM_FREE_SHIPPING_DISCOUNT_AMOUNT = 5_000
        private const val MINIMUM_CASHBACK_QUOTA = 1
        private const val MINIMUM_FREE_SHIPPING_QUOTA = 1
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
            _areInputValid.value = false
            return
        }

        if (selectedCouponType == CouponType.CASHBACK) {
            val isValidCashback =
                isDiscountTypeSelected(selectedDiscountType) && isMinimumPurchaseSelected(
                    selectedMinimumPurchaseType
                ) && isValidCashbackDiscountAmount(cashbackDiscountAmount) && isValidCashbackMinimumPurchase(
                    cashbackMinimumPurchase,
                    cashbackDiscountAmount
                ) && isValidCashbackQuota(cashbackQuota)

            _areInputValid.value = isValidCashback
        } else {

            val isValidFreeShipping = isValidFreeShippingDiscountAmount(freeShippingDiscountAmount)
                    && isValidFreeShippingMinimumPurchase(freeShippingMinimumPurchase, freeShippingDiscountAmount)
                    && isValidFreeShippingQuota(freeShippingQuota)

            _areInputValid.value = isValidFreeShipping

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
        cashbackMinimumPurchase: Int,
        cashbackDiscountAmount: Int
    ): Boolean {
        return cashbackMinimumPurchase > cashbackDiscountAmount
    }

    fun isValidCashbackQuota(cashbackQuota: Int): Boolean {
        return cashbackQuota >= MINIMUM_CASHBACK_QUOTA
    }


    fun isValidFreeShippingDiscountAmount(freeShippingDiscountAmount: Int): Boolean {
        return freeShippingDiscountAmount >= MINIMUM_FREE_SHIPPING_DISCOUNT_AMOUNT
    }

    fun isValidFreeShippingMinimumPurchase(
        freeShippingMinimumPurchase: Int,
        freeShippingDiscountAmount: Int
    ): Boolean {
        return freeShippingMinimumPurchase > freeShippingDiscountAmount
    }

    fun isValidFreeShippingQuota(freeShippingQuota: Int): Boolean {
        return freeShippingQuota >= MINIMUM_FREE_SHIPPING_QUOTA
    }

    fun couponTypeChanged(selectedCouponType: CouponType) {
        _areInputValid.value = false
        _couponType.value = selectedCouponType
    }

    fun getMinimalPurchaseErrorMessage(selectedMinimumPurchaseType: MinimumPurchaseType) : String {
        return if (selectedMinimumPurchaseType == MinimumPurchaseType.NOMINAL) {
            resourceProvider.getInvalidMinimalPurchaseNominalErrorMessage()
        } else {
            resourceProvider.getInvalidMinimalQuantityQuantityErrorMessage()
        }
    }
    
    fun calculateMaxExpenseEstimation(discountAmount : Int, voucherQuotaCount : Int) {
        _maxExpenseEstimation.value = (discountAmount * voucherQuotaCount).toLong()
    }

}