package com.tokopedia.chatbot.domain.pojo.livechatdivider

import com.google.gson.annotations.SerializedName

data class NotificationsItem(

	@SerializedName("subject")
	val subject: String?,

	@SerializedName("description")
	val description: String?
)