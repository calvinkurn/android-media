package com.tokopedia.profilecompletion.data

import com.google.gson.annotations.SerializedName

data class SeamlessResponse(

	@SerializedName("data")
	val data: SeamlessData = SeamlessData(),

	@SerializedName("success")
	val success: Boolean = false,

	@SerializedName("errors")
	val errors: List<SeamlessErrorsItem> = emptyList()
)

data class SeamlessData(

	@SerializedName("back_url")
	val backUrl: String = "",

	@SerializedName("expired_at")
	val expiredAt: Long = 0,

	@SerializedName("token")
	val token: String = ""
)

data class SeamlessErrorsItem(

    @SerializedName("code")
    val code: String = "",

    @SerializedName("message_title")
    val messageTitle: String = "",

    @SerializedName("message")
    val message: String = ""
)
