package com.tokopedia.chatbot.domain.pojo.csatRating.websocketCsatRatingResponse

import com.google.gson.annotations.SerializedName

data class PointsItem(

	@SerializedName("score")
	val score: Int? = null,

	@SerializedName("caption")
	val caption: String? = null,

	@SerializedName("description")
	val description: String? = null
)