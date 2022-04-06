package com.tokopedia.tokomember_seller_dashboard.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TmEligibilityCheck(

    @Expose
    @SerializedName("data")
    val data: CheckEligibility? = null
)

data class CheckEligibility(
    @Expose
    @SerializedName("membershipCheckSellerEligibility")
    val eligibilityCheckData: CheckSellerEligibilityData? = null
)

data class ResultStatusEligible(
    @Expose
    @SerializedName("reason")
    val reason: String? = null,
    @Expose
    @SerializedName("code")
    val code: String? = null
)

data class Message(
    @Expose
    @SerializedName("title")
    val title: String? = null,
    @Expose
    @SerializedName("subtitle")
    val subtitle: String? = null
)

data class CheckSellerEligibilityData(
    @Expose
    @SerializedName("resultStatus")
    val resultStatus: ResultStatusEligible? = null,

    @Expose
    @SerializedName("isEligible")
    val isEligible: Boolean,

    @Expose
    @SerializedName("message")
    val message: Message
)