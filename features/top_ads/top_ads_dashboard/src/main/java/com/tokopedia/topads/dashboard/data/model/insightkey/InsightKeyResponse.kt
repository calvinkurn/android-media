package com.tokopedia.topads.dashboard.data.model.insightkey

import com.google.gson.annotations.SerializedName

data class InsightKeyResponse(
        @SerializedName("topAdsGetKeywordInsights")
        val topAdsGetKeywordInsights: TopAdsGetKeywordInsights = TopAdsGetKeywordInsights()
)

data class TopAdsGetKeywordInsights(
        @SerializedName("data")
        val `data`: InsightKeyData = InsightKeyData(),
        @SerializedName("errors")
        val errors: List<Any> = listOf()
)

data class InsightKeyData(
        @SerializedName("data")
        val data: HashMap<String, KeywordInsightDataMain> = HashMap(),
        @SerializedName("header")
        val header: Header = Header(),
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("name")
        val name: String = "",
        @SerializedName("text")
        val text: String = ""
)
