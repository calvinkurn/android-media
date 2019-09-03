package com.tokopedia.chatbot.domain.pojo.livechatdivider

import com.google.gson.annotations.SerializedName

data class Message(

	@SerializedName("censored_reply")
	val censoredReply: String?,

	@SerializedName("timestamp_unix")
	val timestampUnix: Long?,

	@SerializedName("timestamp_fmt")
	val timestampFmt: String?,

	@SerializedName("original_reply")
	val originalReply: String?,

	@SerializedName("timestamp_unix_nano")
	val timestampUnixNano: Long?,

	@SerializedName("timestamp")
	val timestamp: String? 
)