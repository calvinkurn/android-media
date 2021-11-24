package com.tokopedia.topads.dashboard.data.model.insightkey

import com.google.gson.annotations.SerializedName

data class TopadsHeadlineKeyword(

	@field:SerializedName("topadsHeadlineKeywordSuggestion")
	val suggestion: TopadsHeadlineKeywordSuggestion? = null
)

data class TopadsHeadlineKeywordSuggestion(

	@field:SerializedName("data")
	val recommendedKeyword: RecommendedKeyword? = null,

	@field:SerializedName("errors")
	val errors: List<ErrorsItem?>? = null
)

data class RecommendedKeyword(

	@field:SerializedName("groupCount")
	val groupCount: Int? = null,

	@field:SerializedName("recommendedKeywordDetails")
	val recommendedKeywordDetails: List<Any?>? = null,

	@field:SerializedName("totalImpressionCount")
	val totalImpressionCount: String? = null,

	@field:SerializedName("recommendedKeywordCount")
	val recommendedKeywordCount: Int? = null,

	@field:SerializedName("shopID")
	val shopID: String? = null
)

data class Object(

	@field:SerializedName("text")
	val text: List<Any?>? = null,

	@field:SerializedName("type")
	val type: Int? = null
)

data class ErrorsItem(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("detail")
	val detail: String? = null,

	@field:SerializedName("title")
	val title: String? = null,
)

data class RecommendedKeywordDetail(
	@SerializedName("keywordTag")
	var keywordTag: String = "",
	@SerializedName("groupID")
	var groupId: Int = 0,
	@SerializedName("groupName")
	var groupName: String = "",
	@SerializedName("totalHits")
	var totalHits: Int = 0,
	@SerializedName("recommendedBid")
	var recommendedBid: Double = 0.0,
	@SerializedName("minBid")
	var minBid: Int = 0,
	@SerializedName("minBid")
	var maxBid: Int = 0,
	@SerializedName("impressionCount")
	var impressionCount: Int = 0
)
