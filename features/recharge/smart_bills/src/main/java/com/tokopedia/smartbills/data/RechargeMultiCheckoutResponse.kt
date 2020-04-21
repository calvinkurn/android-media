package com.tokopedia.smartbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common.payment.model.TopPayBaseModel
import com.tokopedia.common_digital.cart.data.entity.response.AttributesCheckout

class RechargeMultiCheckoutResponse: TopPayBaseModel {
    @SerializedName("type")
    @Expose
    val type: String = ""
    @SerializedName("id")
    @Expose
    val id: String = ""
    @SerializedName("attributes")
    @Expose
    val attributes: MultiCheckoutReponseAttributes = MultiCheckoutReponseAttributes()

    override fun getRedirectUrlToPass(): String {
        return attributes.redirectUrl ?: ""
    }

    override fun getQueryStringToPass(): String {
        return attributes.queryString ?: ""
    }

    override fun getCallbackSuccessUrlToPass(): String {
        return attributes.callbackUrlSuccess ?: ""
    }

    override fun getCallbackFailedUrlToPass(): String {
        return attributes.callbackUrlFailed ?: ""
    }

    override fun getTransactionIdToPass(): String {
        return id
    }

    class MultiCheckoutReponseAttributes: AttributesCheckout() {
            @SerializedName("all_success")
            @Expose
            val allSuccess: Boolean = false
            @SerializedName("errors")
            @Expose
            val errors: List<Error> = listOf()
    }

    class Error(
            @SerializedName("index")
            @Expose
            val index: Int = -1,
            @SerializedName("product_id")
            @Expose
            val productID: Int = 0,
            @SerializedName("error_message")
            @Expose
            val errorMessage: String = ""
    )
}