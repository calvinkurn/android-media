package com.tokopedia.linker.validation

import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.model.PaymentData
import timber.log.Timber

class BranchHelperValidation {
    val TAG = "P2#BRANCH_VALIDATION#"
    fun validatePurchaseEvent(branchIOPayment: PaymentData, revenuePrice: Double, shippingPrice: Double) {
        try {
            validatePaymentId(branchIOPayment.getPaymentId())
            validateOrderId(branchIOPayment.getOrderId())
            isFromNotNative(branchIOPayment.isFromNative, branchIOPayment.getPaymentId(), branchIOPayment.getOrderId())
            validateRevenue(revenuePrice)
            validateShipping(shippingPrice)
            validateNewBuyer(branchIOPayment.isNewBuyer, branchIOPayment.productType)
            validateMonthlyNewBuyer(branchIOPayment.monthlyNewBuyer, branchIOPayment.productType)
        }catch (e: Exception){
            e.printStackTrace()
        }

    }

    private fun validatePaymentId(paymentId: String) {
        if (paymentId.isNullOrBlank()) {
            logging("validation;reason=paymentId_blank;data=''")
        }
    }

    private fun validateOrderId(orderID: String) {
        if (orderID.isNullOrBlank()) {
            logging("validation;reason=orderId_blank;data=''")
        }
    }

    private fun isFromNotNative(isFromNative: Boolean, paymentId: String, orderId:String) {
        if (!isFromNative) {
            logging("validation;reason=transaction_not_new_thankspage;id=$paymentId;order_id=$orderId")
        }
    }

    private fun validateRevenue(revenuePrice: Double) {
        if (revenuePrice <= 0) {
            logging("validation;reason=revenue_blank;revenue=$revenuePrice")
        }
    }


    private fun validateShipping(shippingPrice: Double) {
        if (shippingPrice <= 0) {
            logging("validation;reason=shippingPrice_blank;shipping_price=$shippingPrice")
        }
    }


    fun exceptionStringToDouble(ex: String, type: String) {
        logging("error;reason=exceptionStringToDouble;err=$ex;type=$type")
    }

    fun exceptionToSendEvent(ex: String, type: String) {
        logging("error;reason=exceptionToSendEvent;err=$ex;type=$type")
    }

    private fun validateNewBuyer(isNewBuyer: Boolean, productType: String) {
        if (isNewBuyer && LinkerConstants.PRODUCTTYPE_DIGITAL.equals(productType, true)) {
            logging("validation;reason=validateNewBuyer;new_buyer=$isNewBuyer;product_type=$productType")
        }
    }

    private fun validateMonthlyNewBuyer(isMonthlyNewBuyer: Boolean, productType: String) {
        if (isMonthlyNewBuyer && LinkerConstants.PRODUCTTYPE_DIGITAL.equals(productType, true)) {
            logging("validation;reason=validateMonthlyNewBuyer;monthly_new_buyer=$isMonthlyNewBuyer;product_type=$productType")
        }
    }

    fun validateCartQuantity(quantity: String) {
        if (LinkerUtils.convertToDouble(quantity,"cart quantity validation" ) <= 0) {
            logging("validation;reason=add_to_cart;quantity=$quantity")
        }
    }

    private fun logging(log:String){
        Timber.w(TAG + log)
    }
}