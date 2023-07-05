package com.tokopedia.kyc_centralized.ui.gotoKyc.data

import com.google.gson.annotations.SerializedName

data class CheckEligibilityResponse(
	@SerializedName("checkOneKYCEligibility")
	val checkOneKYCEligibility: CheckOneKYCEligibility
)

data class DataEligibility(

	@SerializedName("name")
	val name: String = "",

	@SerializedName("flow")
	val flow: String = ""
)

data class CheckOneKYCEligibility(

    @SerializedName("errorMessages")
	val errorMessages: List<String> = listOf(),

    @SerializedName("data")
	val data: DataEligibility = DataEligibility(),

    @SerializedName("isSuccess")
	val isSuccess: Boolean = false
)
