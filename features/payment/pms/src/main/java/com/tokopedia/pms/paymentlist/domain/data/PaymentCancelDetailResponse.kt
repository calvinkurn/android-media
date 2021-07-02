package com.tokopedia.pms.paymentlist.domain.data

import com.google.gson.annotations.SerializedName

data class PaymentCancelDetailResponse(
    @SerializedName("cancelDetail")
    val cancelDetail: CancelDetail?
)

data class CancelDetail(
    @SerializedName("success")
    val success: Boolean?,
    @SerializedName("refundWalletAmount")
    val refundWalletAmount: Int?,
    @SerializedName("refundMessage")
    val refundMessage: String?,
    @SerializedName("combineMessage")
    val combineMessage: String?
)

data class CancelDetailWrapper(
    val transactionId: String,
    val merchantCode: String,
    val productName: String?,
    val cancelDetailData: CancelDetail?
)


