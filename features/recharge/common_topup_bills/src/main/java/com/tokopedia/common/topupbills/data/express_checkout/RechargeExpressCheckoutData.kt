package com.tokopedia.common.topupbills.data.express_checkout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common.payment.model.TopPayBaseModel
import com.tokopedia.common_digital.cart.data.entity.response.AttributesCheckout

class RechargeExpressCheckoutData: AttributesCheckout(), TopPayBaseModel {
    @SerializedName("transaction_id")
    @Expose
    var transactionId: String = ""
    @SerializedName("need_otp")
    @Expose
    var needsOtp: Boolean = false

    override fun getCallbackSuccessUrlToPass(): String {
        return callbackUrlSuccess ?: ""
    }

    override fun getQueryStringToPass(): String {
        return queryString ?: ""
    }

    override fun getRedirectUrlToPass(): String {
        return redirectUrl ?: ""
    }

    override fun getCallbackFailedUrlToPass(): String {
        return callbackUrlFailed ?: ""
    }

    override fun getTransactionIdToPass(): String {
        return transactionId
    }
}