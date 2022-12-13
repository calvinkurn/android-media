package com.tokopedia.mvc.data.request

import com.google.gson.annotations.SerializedName

data class MvUpdateStatusRequest(
    @SerializedName("voucher_id")
    val voucherId: Long,
    @SerializedName("voucher_status")
    val voucherStatus: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("source")
    val source: String,
)
