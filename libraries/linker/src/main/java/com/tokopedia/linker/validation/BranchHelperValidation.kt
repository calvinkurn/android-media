package com.tokopedia.linker.validation

import android.text.TextUtils
import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.PaymentData
import org.json.JSONArray
import org.json.JSONException
import timber.log.Timber

class BranchHelperValidation {
    val TAG = "P2#BRANCH_VALIDATION#"
    val VALUE_IDR = "IDR"
    private val CONTENT_TYPE = "product"
    private val ADD_TO_CART = "ADD_TO_CART"
    private val VIEW_ITEM = "VIEW_ITEM"
    private val PURCHASE = "PURCHASE"

    fun validatePurchaseEvent(branchIOPayment: PaymentData, revenuePrice: Double, shippingPrice: Double) {
        try {
            validatePaymentId(branchIOPayment.getPaymentId())
            validateOrderId(branchIOPayment.getOrderId())
            isFromNotNative(branchIOPayment.isFromNative, branchIOPayment.getPaymentId(), branchIOPayment.getOrderId())
            validateRevenue(revenuePrice)
            validateShipping(shippingPrice)
            validateNewBuyer(branchIOPayment.isNewBuyer, branchIOPayment.productType)
            validateMonthlyNewBuyer(branchIOPayment.monthlyNewBuyer, branchIOPayment.productType)
            validateProductType(branchIOPayment.productType)
            branchIOPayment.products.forEach {item ->
                validateProductPrice(PURCHASE,LinkerUtils.convertToDouble(item.get(LinkerConstants.PRICE_IDR_TO_DOUBLE), "Product_price-PURCHASE"))
                validateProductId(PURCHASE,item.get(LinkerConstants.ID))
                validateProductName(PURCHASE,item.get(LinkerConstants.NAME))
            }

        }catch (e: Exception){
            e.printStackTrace()
        }

    }

    fun validateATCEvent(linkerData: LinkerData){
        try{
            validateQuantity(ADD_TO_CART, linkerData.quantity,linkerData.sku,linkerData.userId)
            validateCurrency(ADD_TO_CART,linkerData.currency)
            validateProductPrice(ADD_TO_CART,LinkerUtils.convertToDouble(linkerData.getPrice(), "Product_price-ADD_TO_CART"))
            validateProductId(ADD_TO_CART,linkerData.sku)
            validateContentId(ADD_TO_CART,linkerData.contentId)
            validateProductName(ADD_TO_CART,linkerData.productName)
            validateProductCate3(ADD_TO_CART,linkerData.level3Id)
            validateUser(ADD_TO_CART,linkerData.userId)
            validateContentType(ADD_TO_CART,linkerData.contentType)
            validateContent(ADD_TO_CART,linkerData.content)

        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun validateItemViewEvent(linkerData: LinkerData){
        try{
            validateQuantity(VIEW_ITEM, linkerData.quantity,linkerData.sku,linkerData.userId)
            validateCurrency(VIEW_ITEM,linkerData.currency)
            validateProductPrice(VIEW_ITEM,LinkerUtils.convertToDouble(linkerData.getPrice(), "Product_price-VIEW_ITEM"))
            validateProductId(VIEW_ITEM,linkerData.sku)
            validateContentId(VIEW_ITEM,linkerData.contentId)
            validateProductName(VIEW_ITEM,linkerData.productName)
            validateProductCate3(VIEW_ITEM,linkerData.level3Id)
            validateUser(VIEW_ITEM,linkerData.userId)
            validateContentType(VIEW_ITEM,linkerData.contentType)
            validateContent(VIEW_ITEM,linkerData.content)
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
            logging("validation;reason=transaction_not_new_thankspage;id='$paymentId';order_id='$orderId'")
        }
    }

    private fun validateRevenue(revenuePrice: Double) {
        if (revenuePrice <= 0) {
            logging("validation;reason=revenue_blank;revenue='$revenuePrice'")
        }
    }


    private fun validateShipping(shippingPrice: Double) {
        if (shippingPrice <= 0) {
            logging("validation;reason=shippingPrice_blank;shipping_price='$shippingPrice'")
        }
    }


    fun exceptionStringToDouble(ex: String, type: String) {
        logging("error;reason=exceptionStringToDouble;err='$ex';type='$type'")
    }

    fun exceptionToSendEvent(ex: String, type: String) {
        logging("error;reason=exceptionToSendEvent;err='$ex';type='$type'")
    }

    private fun validateNewBuyer(isNewBuyer: Boolean, productType: String) {
        if (isNewBuyer && LinkerConstants.PRODUCTTYPE_DIGITAL.equals(productType, true)) {
            logging("validation;reason=validateNewBuyer;new_buyer='$isNewBuyer';product_type='$productType'")
        }
    }

    private fun validateMonthlyNewBuyer(isMonthlyNewBuyer: Boolean, productType: String) {
        if (isMonthlyNewBuyer && LinkerConstants.PRODUCTTYPE_DIGITAL.equals(productType, true)) {
            logging("validation;reason=validateMonthlyNewBuyer;monthly_new_buyer='$isMonthlyNewBuyer';product_type='$productType'")
        }
    }

   private fun validateCurrency(eventName: String ,currency: String){
        if (!VALUE_IDR.equals(currency)) {
            logging("validation;reason=currency_invalid;eventName='$eventName';data='$currency'")
        }
    }

    private fun validateProductPrice(eventName: String ,price: Double){
        if (price <= 0) {
            logging("validation;reason=productprice_blank;eventName='$eventName';data='$price'")
        }
    }

    private fun validateProductId(eventName: String ,productId: String?){
        if (productId.isNullOrBlank()) {
            logging("validation;reason=productId_blank;eventName='$eventName';data=''")
        }
    }

    fun validateQuantity(eventName: String, quantity: String,productId: String, userId:String) {
        if (LinkerUtils.convertToDouble(quantity,"quantity validation" ) <= 0) {
            logging("validation;reason=quantity_blank;eventName='$eventName';quantity='$quantity';productId='$productId';userId='$userId'")
        }
    }

    private fun validateContentId(eventName: String ,contentId: String) {
        if (contentId.isNullOrBlank()) {
            logging("validation;reason=contentId_blank;eventName='$eventName';data=''")
        } else {
            try {
                val productarray = JSONArray(contentId)
                if (productarray.length() < 1) {
                    logging("validation;reason=contentId_invalid;eventName='$eventName';data='$contentId'")
                }
            } catch (e: JSONException) {
                logging("error;reason=contentId_array_exception;eventName='$eventName';data='$contentId'");
            }
        }
    }

    private fun validateProductName(eventName: String ,productName: String?){
        if (productName.isNullOrBlank()) {
            logging("validation;reason=productName_blank;eventName='$eventName';data=''")
        }
    }

    private fun validateProductCate3(eventName: String ,ProductCate3: String){
        if (ProductCate3.isNullOrBlank()) {
            logging("validation;reason=ProductCate3_blank;eventName='$eventName';data=''")
        }
    }

    private fun validateUser(eventName: String ,userId: String){
        if (userId.isNullOrBlank()) {
            logging("validation;reason=userId_blank;eventName='$eventName';data=''")
        }else if(userId.trim() == "0"){
            logging("validation;reason=userId_blank;eventName='$eventName';data='$userId'")
        }
    }

    private fun validateContentType(eventName: String, contentType: String) {
        if (CONTENT_TYPE != contentType) {
            logging("validation;reason=contentType_invalid;eventName='$eventName';data='$contentType'")
        }
    }

    private fun validateContent(eventName: String, content: String) {
        if (TextUtils.isEmpty(content)) {
            logging("validation;reason=content_array_blank;eventName='$eventName';data=''")
        } else {
            try {
                val contentarray = JSONArray(content)
                if (contentarray.length() < 1) {
                    logging("validation;reason=content_array_invalid;eventName='$eventName';data='$content'")
                }
            } catch (e: JSONException) {
                logging("error;reason=content_array_exception;eventName='$eventName';data='$content'")
            }
        }
    }

    private fun validateProductType( productType: String) {
        if (LinkerConstants.PRODUCTTYPE_DIGITAL != productType && LinkerConstants.PRODUCTTYPE_MARKETPLACE != productType) {
            logging("validation;reason=validateProductType;eventName='';data='$productType'")
        }
    }

    private fun logging(log:String){
        Timber.w(TAG + log)
    }
}