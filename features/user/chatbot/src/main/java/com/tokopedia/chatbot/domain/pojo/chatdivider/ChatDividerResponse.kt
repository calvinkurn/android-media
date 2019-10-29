package com.tokopedia.chatbot.domain.pojo.chatdivider

import com.google.gson.annotations.SerializedName

data class ChatDividerResponse(

	@SerializedName("devider")
	val divider: Divider?
)