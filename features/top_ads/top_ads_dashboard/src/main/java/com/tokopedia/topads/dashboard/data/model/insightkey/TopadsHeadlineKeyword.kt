package com.tokopedia.topads.dashboard.data.model.insightkey

import com.google.gson.annotations.SerializedName

data class TopadsHeadlineKeyword(

    @SerializedName("topadsHeadlineKeywordSuggestion")
    val suggestion: TopadsHeadlineKeywordSuggestion? = null
)

data class TopadsHeadlineKeywordSuggestion(

    @SerializedName("data")
    val recommendedKeyword: RecommendedKeyword? = null,

    @SerializedName("errors")
    val errors: List<ErrorsItem?>? = null
)

data class RecommendedKeyword(
    val shopID: String? = null,
    val recommendedKeywordCount: Int = 0,
    val groupCount: Int = 0,
    val totalImpressionCount: String? = null,
    val recommendedKeywordDetails: List<RecommendedKeywordDetail> = listOf(),
    val topadsHeadlineKeywordSuggestion: TopadsHeadlineKeywordSuggestion? = null
)

data class ErrorsItem(
    val code: String? = null,
    val detail: String? = null,
    val title: String? = null,
)

data class RecommendedKeywordDetail(
    val keywordTag: String = "",
    val groupId: Int = 0,
    val groupName: String = "",
    val totalHits: Int = 0,
    val recommendedBid: Double = 0.0,
    val minBid: Int = 0,
    val maxBid: Int = 0,
    val impressionCount: Int = 0
)
