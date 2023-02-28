package com.tokopedia.common_electronic_money.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class RechargeEmoneyInquiryLogRequest(
    @SerializedName("issuer_id")
    val issueId: Long = 0,
    @SerializedName("inquiry_id")
    val inquiryId: Long = 0,
    @SerializedName("card_number")
    val cardNumber: String = "",
    @SerializedName("rc")
    val rc: String = "",
    @SerializedName("last_balance")
    val lastBalance: Double = 0.0,
): GqlParam
