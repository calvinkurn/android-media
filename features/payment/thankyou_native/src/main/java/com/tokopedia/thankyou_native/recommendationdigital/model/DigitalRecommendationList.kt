package com.tokopedia.thankyou_native.recommendationdigital.model

import com.google.gson.annotations.SerializedName

data class DigitalRecommendationList(

	@SerializedName("title")
	val title: String?,

	@SerializedName("recommendations")
	val recommendations: List<RecommendationsItem?>?
)