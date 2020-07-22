package com.tokopedia.smartbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common.payment.model.TopPayBaseModel
import com.tokopedia.common_digital.cart.data.entity.response.AttributesCheckout
import com.tokopedia.common_digital.cart.data.entity.response.Parameter

data class RechargeMultiCheckoutResponse(
    @SerializedName("type")
    @Expose
    val type: String = "",
    @SerializedName("id")
    @Expose
    val id: String = "",
    @SerializedName("attributes")
    @Expose
    val attributes: MultiCheckoutResponseAttributes = MultiCheckoutResponseAttributes()
): TopPayBaseModel {

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

    data class MultiCheckoutResponseAttributes(
            @SerializedName("all_success")
            @Expose
            val allSuccess: Boolean = false,
            @SerializedName("errors")
            @Expose
            val errors: List<Error> = listOf()
    ): AttributesCheckout() {
        constructor(allSuccess: Boolean = false, errors: List<Error> = listOf(), redirectUrl: String = "",
                    callbackUrlSuccess: String = "", callbackUrlFailed: String = "",
                    queryString: String = "", parameter: Parameter = Parameter(),
                    thanksUrl: String = ""): this(allSuccess, errors) {
            this.redirectUrl = redirectUrl
            this.callbackUrlSuccess = callbackUrlSuccess
            this.callbackUrlFailed = callbackUrlFailed
            this.queryString = queryString
            this.parameter = parameter
            this.thanksUrl = thanksUrl
        }
    }

    data class Error(
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