package com.tokopedia.logisticCommon.data.response

import com.google.gson.annotations.SerializedName

data class KeroAddrIsEligibleForAddressFeatureResponse(
	@SerializedName("KeroAddrIsEligibleForAddressFeature")
	val data: KeroAddrIsEligibleForAddressFeatureData = KeroAddrIsEligibleForAddressFeatureData()
)

data class KeroAddrIsEligibleForAddressFeatureData(

	@SerializedName("kero_addr_error")
	val keroAddrError: KeroAddrError = KeroAddrError(),

	@SerializedName("data")
	val eligibleForRevampAna: EligibleForAddressFeature = EligibleForAddressFeature(),

	@SerializedName("server_process_time")
	val serverProcessTime: String = "",

	@SerializedName("config")
	val config: String = "",

	@SerializedName("status")
	val status: String = ""
)

data class EligibleForAddressFeature(

	@SerializedName("eligible")
	val eligible: Boolean = false
)

data class KeroAddrError(

	@SerializedName("code")
	val code: Int = 0,

	@SerializedName("detail")
	val detail: String = ""
)
