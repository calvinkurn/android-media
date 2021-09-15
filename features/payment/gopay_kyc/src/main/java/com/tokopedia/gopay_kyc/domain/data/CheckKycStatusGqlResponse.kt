package com.tokopedia.gopay_kyc.domain.data

import com.google.gson.annotations.SerializedName

data class CheckKycStatusGqlResponse(
    @SerializedName("walletappCheckStatusKYCResponse")
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
    @SerializedName("wallet_level_str")
    val walletLevelString: String
)
