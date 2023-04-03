package com.tokopedia.kyc_centralized.ui.gotoKyc.data

import com.google.gson.annotations.SerializedName

data class RegisterProgressiveResponse(
	@SerializedName("kycRegisterProgressive")
	val registerProgressiveKYC: RegisterProgressiveKYC = RegisterProgressiveKYC()
)

data class RegisterProgressiveKYCData(
	@SerializedName("challengeID")
	val challengeID: String = "",

    @SerializedName("status")
    val status: Int = 0,

    @SerializedName("rejectionReasonCode")
    val rejectionReasonCode: String = "",

    @SerializedName("rejectionReasonMessage")
    val rejectionReasonMessage: String = ""
)

data class RegisterProgressiveKYC(
    @SerializedName("errorMessages")
	val errorMessages: List<String> = listOf(),

    @SerializedName("data")
	val data: RegisterProgressiveKYCData = RegisterProgressiveKYCData(),

    @SerializedName("isSuccess")
	val isSuccess: Boolean = false
)
