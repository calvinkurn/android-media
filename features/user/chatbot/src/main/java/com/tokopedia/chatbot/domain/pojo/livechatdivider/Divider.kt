package com.tokopedia.chatbot.domain.pojo.livechatdivider

import com.google.gson.annotations.SerializedName

data class Divider(

	@SerializedName("quick_replies")
	val quickReplies: List<QuickRepliesItem?>?,

	@SerializedName("label")
	val label: String?
)