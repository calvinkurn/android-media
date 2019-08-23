package com.tokopedia.chatbot.domain.pojo.leavequeue

import com.google.gson.annotations.SerializedName

data class PostLeaveQueue(

	@SerializedName("LeaveQueueData")
	val leaveQueueData: LeaveQueueData?,

	@SerializedName("LeaveQueueHeader")
	val leaveQueueHeader: LeaveQueueHeader?
)