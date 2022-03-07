package com.tokopedia.gopay.kyc.domain.data

import com.google.gson.annotations.SerializedName

data class CheckKycStatusGqlResponse(
    @SerializedName("walletappCheckStatusKYC")
    val kycStatusResponse: KycStatusResponse
)

data class KycStatusResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("data")
    val kycStatusData: KycStatusData
)

data class KycStatusData(
    @SerializedName("is_eligible_kyc")
    val isEligible: Boolean,
    @SerializedName("message")
    val kycStatusMessage: String?
)
