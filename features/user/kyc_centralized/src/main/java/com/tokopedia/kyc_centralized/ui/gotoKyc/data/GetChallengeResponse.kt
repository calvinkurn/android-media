package com.tokopedia.kyc_centralized.ui.gotoKyc.data

import com.google.gson.annotations.SerializedName

data class GetChallengeResponse(
	@SerializedName("kycGetGoToChallenge")
	val getOneKYCChallenge: GetOneKYCChallenge = GetOneKYCChallenge()
)

data class GetOneKYCChallenge(
    @SerializedName("errorMessages")
	val errorMessages: List<String> = listOf(),

    @SerializedName("errorCode")
    val errorCode: String = "",

    @SerializedName("data")
	val data: List<QuestionsData> = listOf(),

    @SerializedName("isSuccess")
	val isSuccess: Boolean = false
)

data class QuestionsData(
	@SerializedName("displayText")
	val displayText: String = "",

	@SerializedName("hint")
	val hint: String = "",

	@SerializedName("id")
	val id: String = "",

	@SerializedName("type")
	val type: String = "",

	@SerializedName("questionType")
	val questionType: String = ""
)
