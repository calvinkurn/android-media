package com.tokopedia.common_electronic_money.data

import com.google.gson.annotations.SerializedName

data class RechargeEmoneyInquiryLogResponse (
    @SerializedName("rechargeEmoneyInquiryLog")
    val emoneyInquiryLog: EmoneyInquiryLogResponse? = null
)

data class EmoneyInquiryLogResponse (
    @SerializedName("inquiry_id")
    val inquiryId: Long = 0,
    @SerializedName("status")
    val status: String = "",
    @SerializedName("message")
    val message: String = ""
)

