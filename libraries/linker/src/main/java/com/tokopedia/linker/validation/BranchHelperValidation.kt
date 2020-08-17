package com.tokopedia.linker.validation

import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.model.PaymentData
import timber.log.Timber

class BranchHelperValidation() {
    val TAG = "BRANCH_EVENTS_VALIDATIONS#"
    fun validatePurchaseEvent(branchIOPayment: PaymentData, revenuePrice: Double, shippingPrice: Double) {
        validatePaymentId(branchIOPayment.getPaymentId())
        validateOrderId(branchIOPayment.getOrderId())
        isFromNotNative(branchIOPayment.isFromNative)
        validateRevenue(revenuePrice)
        validateShipping(shippingPrice)
        validateNewBuyer(branchIOPayment.isNewBuyer, branchIOPayment.productType)
        validateMonthlyNewBuyer(branchIOPayment.monthlyNewBuyer, branchIOPayment.productType)

    }

    private fun validatePaymentId(paymentId: String) {
        if (paymentId.isNullOrBlank()) {
            Timber.d(TAG + "paymentId is blank =$paymentId")
        }
    }

    private fun validateOrderId(orderID: String) {
        if (orderID.isNullOrBlank()) {
            Timber.d(TAG + "orderID is blank =$orderID")
        }
    }

    private fun isFromNotNative(isFromNative: Boolean) {
        if (!isFromNative) {
            Timber.d(TAG + "isFromNative =$isFromNative")
        }
    }

    private fun validateRevenue(revenuePrice: Double) {
        if (revenuePrice <= 0) {
            Timber.d(TAG + "revenue <= 0 =$revenuePrice")
        }
    }


    private fun validateShipping(shippingPrice: Double) {
        if (shippingPrice <= 0) {
            Timber.d(TAG + "shippingPrice <= 0 =$shippingPrice")
        }
    }


    fun exceptionStringToDouble(ex: String, type: String) {
        Timber.d(TAG + "exceptionStringToDouble $ex$type")
    }

    fun exceptionToSendEvent(ex: String, type: String) {
        Timber.d(TAG + "exceptionToSendEvent $ex$type")
    }

    private fun validateNewBuyer(isNewBuyer: Boolean, productType: String) {
        if (isNewBuyer && LinkerConstants.PRODUCTTYPE_DIGITAL.equals(productType, true)) {
            Timber.d(TAG + "validateNewBuyer $isNewBuyer$productType")
        }
    }

    private fun validateMonthlyNewBuyer(isMonthlyNewBuyer: Boolean, productType: String) {
        if (isMonthlyNewBuyer && LinkerConstants.PRODUCTTYPE_DIGITAL.equals(productType, true)) {
            Timber.d(TAG + "validateMonthlyNewBuyer $isMonthlyNewBuyer$productType")
        }
    }

    fun validateCartQuantity(quantity: String) {
        if (LinkerUtils.convertToDouble(quantity,"cart quantity validation" ) <= 0) {
            Timber.d(TAG + "add to cart quantity <= 0 =$quantity")
        }
    }
}