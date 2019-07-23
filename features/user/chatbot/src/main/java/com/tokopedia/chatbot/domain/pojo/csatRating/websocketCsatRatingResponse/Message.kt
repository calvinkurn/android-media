package com.tokopedia.chatbot.domain.pojo.csatRating.websocketCsatRatingResponse

import com.google.gson.annotations.SerializedName

data class Message(

	@SerializedName("censored_reply")
	val censoredReply: String? = null,

	@SerializedName("timestamp_unix")
	val timestampUnix: Long? = null,

	@SerializedName("timestamp_fmt")
	val timestampFmt: String? = null,

	@SerializedName("original_reply")
	val originalReply: String? = null,

	@SerializedName("timestamp_unix_nano")
	val timestampUnixNano: Long? = null,

	@SerializedName("timestamp")
	val timestamp: String? = null
)