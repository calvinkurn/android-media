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
            override val redirectUrl: String = "",
            override val callbackUrlSuccess: String = "",
            override val callbackUrlFailed: String = "",
            override val queryString: String = "",
            override val parameter: Parameter = Parameter(),
            override val thanksUrl: String = "",
            @SerializedName("all_success")
            @Expose
            val allSuccess: Boolean = false,
            @SerializedName("errors")
            @Expose
            val errors: List<Error> = listOf()
    ): AttributesCheckout(redirectUrl, callbackUrlSuccess, callbackUrlFailed, queryString, parameter, thanksUrl)

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