package com.tokopedia.kyc_centralized.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class KycData(
	@SerializedName("is_success_register")
	@Expose
	var isSuccessRegister: Boolean = false,

	@SerializedName("list_retake")
	@Expose
	var listRetake: ArrayList<Int> = ArrayList(),

	@SerializedName("list_message")
	@Expose
	var listMessage: ArrayList<String> = ArrayList(),

	@SerializedName("apps")
	@Expose
	var app: KycAppModel = KycAppModel()
)
