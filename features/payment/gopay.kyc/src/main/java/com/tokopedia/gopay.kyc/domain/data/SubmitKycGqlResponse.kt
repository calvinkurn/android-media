package com.tokopedia.gopay.kyc.domain.data

import com.google.gson.annotations.SerializedName

data class SubmitKycGqlResponse(
    @SerializedName("walletappSubmitKYC")
    val submitKycResponse : SubmitKycResponse
)

data class SubmitKycResponse(
    @SerializedName("code")
    val code: String
)
