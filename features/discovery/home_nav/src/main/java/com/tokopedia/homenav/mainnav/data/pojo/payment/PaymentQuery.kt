package com.tokopedia.homenav.mainnav.data.pojo.payment

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PaymentQuery(
        @SerializedName("paymentList")
        @Expose
    val paymentList: List<PaymentX>? = listOf()
)