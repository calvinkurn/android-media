package com.tokopedia.chatbot.domain.pojo.livechatdivider

import com.google.gson.annotations.SerializedName

data class Attachment(

	@SerializedName("fallback_attachment")
	val fallbackAttachment: FallbackAttachment?,

	@SerializedName("attributes")
	val attributes: Attributes?,

	@SerializedName("id")
	val id: Int = 0,

	@SerializedName("type")
	val type: Int = 0
)