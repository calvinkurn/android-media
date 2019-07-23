package com.tokopedia.chatbot.domain.pojo.csatRating.websocketCsatRatingResponse

import com.google.gson.annotations.SerializedName

data class Attributes(

	@SerializedName("show_other_reason")
	val showOtherReason: Boolean? = null,

	@SerializedName("reasons")
	val reasons: List<String?>? = null,

	@SerializedName("reason_title")
	val reasonTitle: String? = null,

	@SerializedName("fallback_attachment")
	val fallbackAttachment: FallbackAttachment? = null,

	@SerializedName("chatbot_session_id")
	val chatbotSessionId: String? = null,

	@SerializedName("livechat_session_id")
	val livechatSessionId: String? = null,

	@SerializedName("trigger_rule_type")
	val triggerRuleType: String? = null,

	@SerializedName("title")
	val title: String? = null,

	@SerializedName("points")
	val points: List<PointsItem?>? = null
)