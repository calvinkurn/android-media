package com.tokopedia.kyc_centralized.ui.gotoKyc.data

import com.google.gson.annotations.SerializedName

data class SubmitChallengeResponse(
	@SerializedName("kycSubmitGoToChallenge")
	val submitKYCChallenge: SubmitKYCChallenge
)

data class SubmitKYCChallenge(
    @SerializedName("cooldownTimeInSeconds")
    val cooldownTimeInSeconds: String = "",

    @SerializedName("attemptsRemaining")
    val attemptsRemaining: String = "",

    @SerializedName("maximumAttemptsAllowed")
    val maximumAttemptsAllowed: String = "",

    @SerializedName("message")
    val message: String = "",

    @SerializedName("errorMessages")
	val errorMessages: List<String> = emptyList(),

    @SerializedName("errorCode")
    val errorCode: String = ""
)
