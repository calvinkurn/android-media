package com.tokopedia.chatbot.domain.pojo.livechatdivider

import com.google.gson.annotations.SerializedName

data class QuickRepliesItem(

	@SerializedName("action")
	val action: String?,

	@SerializedName("text")
	val text: String?,

	@SerializedName("value")
	val value: String?
)