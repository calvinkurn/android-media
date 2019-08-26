package com.tokopedia.chatbot.domain.pojo.livechatdivider

import com.google.gson.annotations.SerializedName

data class FallbackAttachment(

	@SerializedName("html")
	val html: String?
)