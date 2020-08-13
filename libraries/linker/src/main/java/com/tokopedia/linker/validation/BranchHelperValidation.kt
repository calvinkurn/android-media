package com.tokopedia.linker.validation

import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.model.PaymentData

class BranchHelperValidation () {

    fun validatePurchaseEvent(branchIOPayment : PaymentData, revenuePrice : Double, shippingPrice: Double ){

    }

    fun isFromNotNative(isFromNative: Boolean){
        if(!isFromNative){
            //logs
        }
    }

    fun validateRevenue(revenuePrice : Double){
        if(revenuePrice <=0){
            //logs
        }
    }


    fun validateShipping(shippingPrice : Double){
        if(shippingPrice <=0){
            //logs
        }
    }


    fun exceptionStringToDouble(ex:String, type:String){
        //logs
    }

    fun exceptionToSendEvent(ex:String, type:String){
        //logs
    }

    fun validateNewBuyer(isNewBuyer: Boolean, productType:String){
        if (isNewBuyer && LinkerConstants.PRODUCTTYPE_DIGITAL.equals(productType,true)){
            //logs
        }
    }

    fun validateMonthlyNewBuyer(isMonthlyNewBuyer: Boolean, productType:String){
        if (isMonthlyNewBuyer && LinkerConstants.PRODUCTTYPE_DIGITAL.equals(productType,true)){
            //logs
        }
    }
}