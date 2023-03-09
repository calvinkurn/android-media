package com.tokopedia.kyc_centralized.ui.gotoKyc.data

import com.google.gson.annotations.SerializedName

data class SubmitChallengeResponse(
	@SerializedName("submitKYCChallenge")
	val submitKYCChallenge: SubmitKYCChallenge
)

data class SubmitKYCChallenge(
	@SerializedName("errorMessages")
	val errorMessages: List<String>,

	@SerializedName("isSuccess")
	val isSuccess: Boolean
)
