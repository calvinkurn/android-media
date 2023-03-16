package com.tokopedia.kyc_centralized.ui.gotoKyc.data

import com.google.gson.annotations.SerializedName

data class SubmitChallengeResponse(
	@SerializedName("kycSubmitGoToChallenge")
	val submitKYCChallenge: SubmitKYCChallenge
)

data class SubmitKYCChallenge(
	@SerializedName("errorMessages")
	val errorMessages: List<String>,

	@SerializedName("isSuccess")
	val isSuccess: Boolean,

    //TODO: this is fake field, please change it when BE ready
    val submitStatus: String = "KYC_CHALLENGE_ATTEMPTS_EXHAUSTED"
)
