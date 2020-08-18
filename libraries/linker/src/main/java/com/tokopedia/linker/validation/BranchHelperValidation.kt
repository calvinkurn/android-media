package com.tokopedia.linker.validation

import com.tokopedia.config.GlobalConfig
import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.model.PaymentData
import timber.log.Timber

class BranchHelperValidation() {
    val TAG = "P1#BRANCH_EVENT_VALIDATIONS#"
    fun validatePurchaseEvent(branchIOPayment: PaymentData, revenuePrice: Double, shippingPrice: Double) {
        validatePaymentId(branchIOPayment.getPaymentId())
        validateOrderId(branchIOPayment.getOrderId())
        isFromNotNative(branchIOPayment.isFromNative,branchIOPayment.getPaymentId(),branchIOPayment.getOrderId())
        validateRevenue(revenuePrice)
        validateShipping(shippingPrice)
        validateNewBuyer(branchIOPayment.isNewBuyer, branchIOPayment.productType)
        validateMonthlyNewBuyer(branchIOPayment.monthlyNewBuyer, branchIOPayment.productType)

    }

    private fun validatePaymentId(paymentId: String) {
        if (paymentId.isNullOrBlank()) {
            logging( "paymentId is blank =$paymentId")
        }
    }

    private fun validateOrderId(orderID: String) {
        if (orderID.isNullOrBlank()) {
            logging( "orderID is blank =$orderID")
        }
    }

    private fun isFromNotNative(isFromNative: Boolean, paymentId: String, orderId:String) {
        if (!isFromNative) {
            logging( "transaction is not from new thanks page =$paymentId$orderId")
        }
    }

    private fun validateRevenue(revenuePrice: Double) {
        if (revenuePrice <= 0) {
            logging( "revenue <= 0 =$revenuePrice")
        }
    }


    private fun validateShipping(shippingPrice: Double) {
        if (shippingPrice <= 0) {
            logging( "shippingPrice <= 0 =$shippingPrice")
        }
    }


    fun exceptionStringToDouble(ex: String, type: String) {
        logging( "exceptionStringToDouble $ex$type")
    }

    fun exceptionToSendEvent(ex: String, type: String) {
        logging( "exceptionToSendEvent $ex$type")
    }

    private fun validateNewBuyer(isNewBuyer: Boolean, productType: String) {
        if (isNewBuyer && LinkerConstants.PRODUCTTYPE_DIGITAL.equals(productType, true)) {
            logging( "validateNewBuyer $isNewBuyer$productType")
        }
    }

    private fun validateMonthlyNewBuyer(isMonthlyNewBuyer: Boolean, productType: String) {
        if (isMonthlyNewBuyer && LinkerConstants.PRODUCTTYPE_DIGITAL.equals(productType, true)) {
            logging( "validateMonthlyNewBuyer $isMonthlyNewBuyer$productType")
        }
    }

    fun validateCartQuantity(quantity: String) {
        if (LinkerUtils.convertToDouble(quantity,"cart quantity validation" ) <= 0) {
            logging("add to cart quantity <= 0 =$quantity")
        }
    }

    private fun logging(log:String){
        if(GlobalConfig.DEBUG){
            //add alert dialog
        }
        Timber.w(TAG + log)
    }
}