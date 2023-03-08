package com.tokopedia.kyc_centralized.ui.gotoKyc.data

import com.google.gson.annotations.SerializedName

data class GetChallengeResponse(
	@SerializedName("getOneKYCChallenge")
	val getOneKYCChallenge: GetOneKYCChallenge = GetOneKYCChallenge()
)

data class GetOneKYCChallenge(
    @SerializedName("errorMessages")
	val errorMessages: List<String> = listOf(),

    @SerializedName("data")
	val data: GetChallengeData = GetChallengeData(),

    @SerializedName("isSuccess")
	val isSuccess: Boolean = false
)

data class QuestionsItem(
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

data class GetChallengeData(
	@SerializedName("questions")
	val questions: List<QuestionsItem> = listOf()
)
