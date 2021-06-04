package com.tokopedia.pms.paymentlist.domain.data

import com.google.gson.annotations.SerializedName

data class CancelPaymentDetailResponse(
    @SerializedName("cancelPayment")
    val cancelPayment: CancelPayment?
)

data class CancelPayment(
    @SerializedName("success")
    val isSuccess: Boolean?,
    @SerializedName("message")
    val message: String?
)