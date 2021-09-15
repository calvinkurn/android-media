package com.tokopedia.gopay_kyc.domain.data

import com.google.gson.annotations.SerializedName

data class SubmitKycGqlResponse(
    @SerializedName("walletappSubmitKYCResponse")
    val submitKycResponse : SubmitKycResponse
)

data class SubmitKycResponse(
    @SerializedName("code")
    val code: String
)
/*

data class SubmitKycData(
    @SerializedName("kyc_request_id")
)
*/
