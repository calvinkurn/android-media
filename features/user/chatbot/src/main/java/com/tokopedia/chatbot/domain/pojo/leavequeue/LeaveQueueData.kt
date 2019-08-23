package com.tokopedia.chatbot.domain.pojo.leavequeue

import com.google.gson.annotations.SerializedName

data class LeaveQueueData(

	@SerializedName("Message")
	val message: String?
)