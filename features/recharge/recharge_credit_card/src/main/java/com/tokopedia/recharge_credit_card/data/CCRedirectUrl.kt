package com.tokopedia.recharge_credit_card.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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