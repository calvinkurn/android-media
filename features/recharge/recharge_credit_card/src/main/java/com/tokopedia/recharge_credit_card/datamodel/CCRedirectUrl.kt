package com.tokopedia.recharge_credit_card.datamodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CCRedirectUrlResponse(
        @SerializedName("success")
        @Expose
        val success: Boolean = false,
        @SerializedName("data")
        @Expose
        val data: CCRedirectUrl = CCRedirectUrl(),
)

class CCRedirectUrl(
        @SerializedName("message_error")
        @Expose
        val messageError: String = "",
        @SerializedName("redirect_url")
        @Expose
        val redirectUrl: String = "",
        var clientNumber: String = "",
        var operatorId: String = "",
        var productId: String = ""
)