package com.tokopedia.thankyou_native.recommendationdigital.model

import com.google.gson.annotations.SerializedName

data class RechargeFavoriteRecommendationList(

	@field:SerializedName("title")
	val title: String?,

	@field:SerializedName("recommendations")
	val recommendations: List<RecommendationsItem?>?
)