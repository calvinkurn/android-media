package com.tokopedia.chatbot.domain.pojo.csatRating.websocketCsatRatingResponse

import com.google.gson.annotations.SerializedName

data class WebSocketCsatResponse(

	@SerializedName("is_opposite")
	val isOpposite: Boolean? = null,

	@SerializedName("thumbnail")
	val thumbnail: String? = null,

	@SerializedName("from_role")
	val fromRole: String? = null,

	@SerializedName("message")
	val message: Message? = null,

	@SerializedName("from_uid")
	val fromUid: Int? = null,

	@SerializedName("show_rating")
	val showRating: Boolean? = null,

	@SerializedName("start_time")
	val startTime: String? = null,

	@SerializedName("from_user_name")
	val fromUserName: String? = null,

	@SerializedName("rating_status")
	val ratingStatus: Int? = null,

	@SerializedName("attachment")
	val attachment: Attachment? = null,

	@SerializedName("to_uid")
	val toUid: Int? = null,

	@SerializedName("attachment_id")
	val attachmentId: Int? = null,

	@SerializedName("from")
	val from: String? = null,

	@SerializedName("to_buyer")
	val toBuyer: Boolean? = null,

	@SerializedName("msg_id")
	val msgId: Int? = null,

	@SerializedName("is_bot")
	val isBot: Boolean? = null
)