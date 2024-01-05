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
    val rejectionReasonMessage: String = "",

    @SerializedName("cooldownTimeInSeconds")
    val cooldownTimeInSeconds: String = "",

    @SerializedName("maximumAttemptsAllowed")
    val maximumAttemptsAllowed: String = "",

    @SerializedName("message")
    val message: String = ""
)

data class RegisterProgressiveKYC(
    @SerializedName("errorMessages")
	val errorMessages: List<String> = listOf(),

    @SerializedName("errorCode")
    val errorCode: String = "",

    @SerializedName("data")
	val data: RegisterProgressiveKYCData = RegisterProgressiveKYCData()
)
