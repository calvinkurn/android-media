package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 06/04/21
 */

data class GetRecommendationDataResponse(
        @SerializedName("fetchRecommendationWidgetData")
        val recommendationWidgetData: RecommendationWidgetModel? = null
)

data class RecommendationWidgetModel(
        @SerializedName("data")
        val data: List<RecommendationWidgetDataModel> = emptyList()
)

data class RecommendationWidgetDataModel(
        @SerializedName("dataKey")
        val dataKey: String? = "",
        @SerializedName("errorMsg")
        val errorMsg: String? = "",
        @SerializedName("showWidget")
        val shouldShowWidget: Boolean? = true,
        @SerializedName("data")
        val data: RecommendationDataModel? = null
)

data class RecommendationDataModel(
        @SerializedName("ticker")
        val ticker: RecommendationTicker? = null,
        @SerializedName("progressBar1")
        val progressLevel: RecommendationProgressModel? = null,
        @SerializedName("progressBar2")
        val progressBar: RecommendationProgressModel? = null,
        @SerializedName("recommendation")
        val recommendation: RecommendationModel? = null
)

data class RecommendationModel(
        @SerializedName("title")
        val title: String? = "",
        @SerializedName("list")
        val list: List<RecommendationItemModel> = emptyList()
)

data class RecommendationItemModel(
        @SerializedName("text")
        val text: String? = "",
        @SerializedName("applink")
        val appLink: String? = "",
        @SerializedName("type")
        val type: Int? = 0
)

data class RecommendationTicker(
        @SerializedName("type")
        val type: Int? = 0,
        @SerializedName("text")
        val text: String? = ""
)

data class RecommendationProgressModel(
        @SerializedName("show")
        val isShown: Boolean? = false,
        @SerializedName("text")
        val text: String? = "",
        @SerializedName("bar")
        val bar: RecommendationBarModel? = null
)

data class RecommendationBarModel(
        @SerializedName("value")
        val value: Int? = 0,
        @SerializedName("maxValue")
        val maxValue: Int? = 0,
        @SerializedName("valueDisplay")
        val valueToDisplay: String? = ""
)
