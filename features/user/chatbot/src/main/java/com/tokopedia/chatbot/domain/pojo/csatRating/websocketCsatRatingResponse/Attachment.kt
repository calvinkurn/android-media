package com.tokopedia.chatbot.domain.pojo.csatRating.websocketCsatRatingResponse

import com.google.gson.annotations.SerializedName

data class Attachment(

	@SerializedName("fallback_attachment")
	val fallbackAttachment: FallbackAttachment? = null,

	@SerializedName("attributes")
	val attributes: Attributes? = null,

	@SerializedName("id")
	val id: Int? = null,

	@SerializedName("type")
	val type: Int? = null
)