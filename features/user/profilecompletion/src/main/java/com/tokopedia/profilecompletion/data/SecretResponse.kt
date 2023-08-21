package com.tokopedia.profilecompletion.data

import com.google.gson.annotations.SerializedName

data class SecretResponse(

	@SerializedName("data")
	val data: SecretData = SecretData(),

	@SerializedName("success")
	val success: Boolean = false,

	@SerializedName("errors")
	val errors: List<ErrorsItem> = emptyList()
)

data class ErrorsItem(

	@SerializedName("code")
	val code: String = "",

	@SerializedName("message_title")
	val messageTitle: String = "",

	@SerializedName("message")
	val message: String = ""
)

data class SecretData(

	@SerializedName("secret_key")
	val secretKey: String = "",

	@SerializedName("secret_key_id")
	val secretKeyId: String = "",

	@SerializedName("token")
	val token: String = ""
)
