package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 06/04/21
 */

data class GetRecommendationDataResponse(
        @SerializedName("fetchRecommendationWidgetData")
        val recommendationWidgetData: RecommendationWidgetModel
)

data class RecommendationWidgetModel(
        @SerializedName("data")
        val data: List<RecommendationWidgetDataModel>
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
        val title: String,
        @SerializedName("list")
        val list: List<RecommendationItemModel>
)

data class RecommendationItemModel(
        @SerializedName("text")
        val text: String,
        @SerializedName("applink")
        val appLink: String,
        @SerializedName("type")
        val type: Int
)

data class RecommendationTicker(
        @SerializedName("type")
        val type: Int,
        @SerializedName("text")
        val text: String
)

data class RecommendationProgressModel(
        @SerializedName("show")
        val isShown: Boolean,
        @SerializedName("text")
        val text: String,
        @SerializedName("bar")
        val bar: RecommendationBarModel
)

data class RecommendationBarModel(
        @SerializedName("value")
        val value: Int,
        @SerializedName("maxValue")
        val maxValue: Int
)
