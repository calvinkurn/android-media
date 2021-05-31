package com.tokopedia.homenav.mainnav.data.pojo.payment

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Payment(
    @SerializedName("paymentQuery")
    @Expose
    val paymentQuery: PaymentQuery? = PaymentQuery()
)