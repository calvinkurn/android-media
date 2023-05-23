package com.tokopedia.linker.validation

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.PaymentData
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.track.TrackApp
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class BranchHelperValidation {
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
            branchIOPayment.products.forEach { item ->
                validateProductPrice(PURCHASE, LinkerUtils.convertToDouble(item.get(LinkerConstants.PRICE_IDR_TO_DOUBLE), "Product_price-PURCHASE"))
                validateProductId(PURCHASE, item.get(LinkerConstants.ID))
                validateProductName(PURCHASE, item.get(LinkerConstants.NAME))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            val messageMap = mapOf("type" to "error", "reason" to "exception_validatePURCHASE", "data" to PURCHASE, "ex" to Log.getStackTraceString(e))
            logging(messageMap)
        }
    }

    fun validateATCEvent(linkerData: LinkerData) {
        try {
            validateQuantity(ADD_TO_CART, linkerData.quantity, linkerData.sku, linkerData.userId)
            validateCurrency(ADD_TO_CART, linkerData.currency)
            validateProductPrice(ADD_TO_CART, LinkerUtils.convertToDouble(linkerData.getPrice(), "Product_price-ADD_TO_CART"))
            validateProductId(ADD_TO_CART, linkerData.sku)
            validateContentId(ADD_TO_CART, linkerData.contentId)
            validateProductName(ADD_TO_CART, linkerData.productName)
            validateProductCate3(ADD_TO_CART, linkerData.level3Id)
            validateUser(ADD_TO_CART, linkerData.userId)
            validateContentType(ADD_TO_CART, linkerData.contentType)
            validateContent(ADD_TO_CART, linkerData.content)
        } catch (e: Exception) {
            e.printStackTrace()
            val messageMap = mapOf("type" to "error", "reason" to "exception_validateADD_TO_CART", "data" to ADD_TO_CART, "ex" to Log.getStackTraceString(e))
            logging(messageMap)
        }
    }

    fun validateItemViewEvent(linkerData: LinkerData) {
        try {
            validateQuantity(VIEW_ITEM, linkerData.quantity, linkerData.sku, linkerData.userId)
            validateCurrency(VIEW_ITEM, linkerData.currency)
            validateProductPrice(VIEW_ITEM, LinkerUtils.convertToDouble(linkerData.getPrice(), "Product_price-VIEW_ITEM"))
            validateProductId(VIEW_ITEM, linkerData.sku)
            validateContentId(VIEW_ITEM, linkerData.contentId)
            validateProductName(VIEW_ITEM, linkerData.productName)
            validateProductCate3(VIEW_ITEM, linkerData.level3Id)
            validateUser(VIEW_ITEM, linkerData.userId)
            validateContentType(VIEW_ITEM, linkerData.contentType)
            validateContent(VIEW_ITEM, linkerData.content)
        } catch (e: Exception) {
            e.printStackTrace()
            val messageMap = mapOf("type" to "error", "reason" to "exception_validateVIEW_ITEM", "data" to VIEW_ITEM, "ex" to Log.getStackTraceString(e))
            logging(messageMap)
        }
    }

    fun logSkipDeeplinkNonBranchLink(referringParams: JSONObject, isFirstOpen: Boolean) {
        try {
            val clickTime = referringParams.optString("+click_timestamp")
            val utm_medium = referringParams.optString("utm_medium")
            val utm_source = referringParams.optString("utm_source")
            val campaign = referringParams.optString("~campaign")
            val android_deeplink_path = referringParams.optString(LinkerConstants.KEY_ANDROID_DEEPLINK_PATH)
            val clicked_branch_link = referringParams.optString("+clicked_branch_link")
            val is_first_session = referringParams.optString("+is_first_session").toString()
            val feature = referringParams.optString("~feature")
            val channel = referringParams.optString("~channel")
            val clientId = TrackApp.getInstance().gtm.clientIDString

            val messageMap = mapOf(
                "type" to "validation", "reason" to "SkipDeeplinkNonBranchLink", "click_time" to clickTime, "utm_medium" to utm_medium,
                "utm_source" to utm_source, "campaign" to campaign, "android_deeplink_path" to android_deeplink_path, "clicked_branch_link" to clicked_branch_link,
                "is_first_session" to is_first_session, "client_id" to clientId, "is_first_open" to (isFirstOpen ?: false).toString(), "feature" to feature, "channel" to channel
            )
            logging(messageMap)
        } catch (e: Exception) {
            e.printStackTrace()
            val messageMap = mapOf("type" to "error", "reason" to "exception_skipDeeplink", "data" to "logSkipDeeplinkNonBranchLink", "ex" to Log.getStackTraceString(e))
            logging(messageMap)
        }
    }

    fun logValidCampaignData(utmSource: String, utmMedium: String, utmCampaign: String, clickTime: String, isFirstOpen: Boolean, isBranchLink: Boolean) {
        try {
            val clientId = TrackApp.getInstance().gtm.clientIDString
            val messageMap = mapOf(
                "type" to "validation", "reason" to "BranchCallBackValidUTM", "utm_medium" to utmMedium,
                "utm_source" to utmSource, "campaign" to utmCampaign, "click_time" to clickTime, "client_id" to clientId, "is_first_open" to (isFirstOpen).toString(),
                "is_branch_link" to (isBranchLink).toString()
            )
            logging(messageMap)
        } catch (e: Exception) {
            e.printStackTrace()
            val messageMap = mapOf("type" to "error", "reason" to "exception_branchCallBackValidUtm", "data" to "logValidBranchCallBackUtm", "ex" to Log.getStackTraceString(e))
            logging(messageMap)
        }
    }

    private fun validatePaymentId(paymentId: String) {
        if (paymentId.isNullOrBlank()) {
            val messageMap = mapOf("type" to "validation", "reason" to "paymentId_blank", "data" to "")
            logging(messageMap)
        }
    }

    private fun validateOrderId(orderID: String) {
        if (orderID.isNullOrBlank()) {
            val messageMap = mapOf("type" to "validation", "reason" to "orderId_blank", "data" to "")
            logging(messageMap)
        }
    }

    private fun isFromNotNative(isFromNative: Boolean, paymentId: String, orderId: String) {
        if (!isFromNative) {
            val messageMap = mapOf("type" to "validation", "reason" to "transaction_not_new_thankspage", "id" to paymentId, "order_id" to orderId)
            logging(messageMap)
        }
    }

    private fun validateRevenue(revenuePrice: Double) {
        if (revenuePrice <= 0) {
            val messageMap = mapOf("type" to "validation", "reason" to "revenue_blank", "revenue" to revenuePrice.toString())
            logging(messageMap)
        }
    }

    private fun validateShipping(shippingPrice: Double) {
        if (shippingPrice <= 0) {
            val messageMap = mapOf("type" to "validation", "reason" to "shippingPrice_blank", "shipping_price" to shippingPrice.toString())
            logging(messageMap)
        }
    }

    fun exceptionStringToDouble(ex: String, type: String) {
        val messageMap = mapOf("type" to "error", "reason" to "exceptionStringToDouble", "err" to ex, "type" to type)
        logging(messageMap)
    }

    fun exceptionToSendEvent(ex: String, type: String) {
        val messageMap = mapOf("type" to "error", "reason" to "exceptionToSendEvent", "err" to ex, "type" to type)
        logging(messageMap)
    }

    private fun validateNewBuyer(isNewBuyer: Boolean, productType: String) {
        if (isNewBuyer && LinkerConstants.PRODUCTTYPE_DIGITAL.equals(productType, true)) {
            val messageMap = mapOf("type" to "validation", "reason" to "validateNewBuyer", "new_buyer" to isNewBuyer.toString(), "product_type" to productType)
            logging(messageMap)
        }
    }

    private fun validateMonthlyNewBuyer(isMonthlyNewBuyer: Boolean, productType: String) {
        if (isMonthlyNewBuyer && LinkerConstants.PRODUCTTYPE_DIGITAL.equals(productType, true)) {
            val messageMap = mapOf("type" to "validation", "reason" to "validateMonthlyNewBuyer", "monthly_new_buyer" to isMonthlyNewBuyer.toString(), "product_type" to productType)
            logging(messageMap)
        }
    }

    private fun validateCurrency(eventName: String, currency: String) {
        if (!VALUE_IDR.equals(currency)) {
            val messageMap = mapOf("type" to "validation", "reason" to "currency_invalid", "eventName" to eventName, "data" to currency)
            logging(messageMap)
        }
    }

    private fun validateProductPrice(eventName: String, price: Double) {
        if (price <= 0) {
            val messageMap = mapOf("type" to "validation", "reason" to "productprice_blank", "eventName" to eventName, "data" to price.toString())
            logging(messageMap)
        }
    }

    private fun validateProductId(eventName: String, productId: String?) {
        if (productId.isNullOrBlank()) {
            val messageMap = mapOf("type" to "validation", "reason" to "productId_blank", "eventName" to eventName, "data" to "")
            logging(messageMap)
        }
    }

    fun validateQuantity(eventName: String, quantity: String, productId: String, userId: String) {
        if (LinkerUtils.convertToDouble(quantity, "quantity validation") <= 0) {
            val messageMap = mapOf("type" to "validation", "reason" to "quantity_blank", "eventName" to eventName, "quantity" to quantity, "productId" to productId, "userId" to userId)
            logging(messageMap)
        }
    }

    private fun validateContentId(eventName: String, contentId: String) {
        if (contentId.isNullOrBlank()) {
            val messageMap = mapOf("type" to "validation", "reason" to "contentId_blank", "eventName" to eventName, "data" to "")
            logging(messageMap)
        }
    }

    private fun validateProductName(eventName: String, productName: String?) {
        if (productName.isNullOrBlank()) {
            val messageMap = mapOf("type" to "validation", "reason" to "productName_blank", "eventName" to eventName, "data" to "")
            logging(messageMap)
        }
    }

    private fun validateProductCate3(eventName: String, ProductCate3: String) {
        if (ProductCate3.isNullOrBlank()) {
            val messageMap = mapOf("type" to "validation", "reason" to "ProductCate3_blank", "eventName" to eventName, "data" to "")
            logging(messageMap)
        }
    }

    private fun validateUser(eventName: String, userId: String) {
        if (userId.isNullOrBlank()) {
            val messageMap = mapOf("type" to "validation", "reason" to "userId_blank", "eventName" to eventName, "data" to "")
            logging(messageMap)
        } else if (userId.trim() == "0") {
            val messageMap = mapOf("type" to "validation", "reason" to "userId_blank", "eventName" to eventName, "data" to "")
            logging(messageMap)
        }
    }

    private fun validateContentType(eventName: String, contentType: String) {
        if (CONTENT_TYPE != contentType) {
            val messageMap = mapOf("type" to "validation", "reason" to "contentType_invalid", "eventName" to eventName, "data" to contentType)
            logging(messageMap)
        }
    }

    private fun validateContent(eventName: String, content: String) {
        if (TextUtils.isEmpty(content)) {
            val messageMap = mapOf("type" to "validation", "reason" to "content_array_blank", "eventName" to eventName, "data" to "")
            logging(messageMap)
        } else {
            try {
                val contentarray = JSONArray(content)
                if (contentarray.length() < 1) {
                    val messageMap = mapOf("type" to "validation", "reason" to "content_array_invalid", "eventName" to eventName, "data" to content)
                    logging(messageMap)
                }
            } catch (e: JSONException) {
                val messageMap = mapOf("type" to "error", "reason" to "content_array_exception", "eventName" to eventName, "data" to content)
                logging(messageMap)
            }
        }
    }

    private fun validateProductType(productType: String) {
        if (!(LinkerConstants.PRODUCTTYPE_DIGITAL == productType || LinkerConstants.PRODUCTTYPE_MARKETPLACE == productType)) {
            val messageMap = mapOf("type" to "validation", "reason" to "validateProductType", "eventName" to "", "data" to productType)
            logging(messageMap)
        }
    }

    private fun logging(messageMap: Map<String, String>) {
        ServerLogger.log(Priority.P2, "BRANCH_VALIDATION", messageMap)
    }

    /** function to log when user click the branch link
     * @param errorCode if null then the error is not coming from branch listener
     */
    fun sendBranchErrorDataLogs(errorCode: Int?, errorMsg: String?, branchUrl: String?) {
        val messageMap: MutableMap<String, String> = HashMap()
        messageMap[BRANCH_LOG_TYPE] = BRANCH_FLOW_ON_CLICK_LINK
        messageMap[BRANCH_ERROR_DATA_MESSAGE] = errorMsg ?: "Empty error response"
        messageMap[BRANCH_ERROR_DATA_CODE] = errorCode.toString()
        messageMap[BRANCH_URL] = branchUrl.toString()
        logging(messageMap)
    }

    fun sendBranchSuccessDataLogs(context: Context, referringParams: JSONObject?, branchUrl: String) {
        val remoteConfig: RemoteConfig = FirebaseRemoteConfigImpl(context)
        if (!remoteConfig.getBoolean(RemoteConfigKey.ENABLE_SEND_SUCCESS_LOG_BRANCH, true)) {
            return
        }

        val messageMap = HashMap<String, String>()
        messageMap[BRANCH_URL] = branchUrl
        messageMap[BRANCH_LOG_TYPE] = BRANCH_FLOW_ON_CLICK_LINK
        if (referringParams != null) {
            messageMap[BRANCH_SUCCESS_DATA] = referringParams.toString()
        } else {
            messageMap[BRANCH_SUCCESS_DATA] = "Empty Success Response"
        }
        logging(messageMap)
    }

    fun sendGenerateBranchErrorLogs(errorMsg: String?) {
        val messageMap = HashMap<String, String>()
        messageMap[BRANCH_LOG_TYPE] = BRANCH_GENERATE_LINK
        messageMap[BRANCH_ERROR_DATA_MESSAGE] = errorMsg ?: "Empty error message generate branch error"
        logging(messageMap)
    }

    companion object {
        private val BRANCH_SUCCESS_DATA = "branch_success_data"
        private val BRANCH_ERROR_DATA_MESSAGE = "branch_error_message"
        private val BRANCH_ERROR_DATA_CODE = "branch_error_code"
        private val BRANCH_URL = "branch_url"
        private val BRANCH_FLOW_ON_CLICK_LINK = "on_click_link"
        private val BRANCH_LOG_TYPE = "branch_log_type"
        private val BRANCH_GENERATE_LINK = "branch_generate_link"
    }
}
