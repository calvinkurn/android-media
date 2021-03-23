package com.tokopedia.sellerhomecommon.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetMultiLineGraphResponse(
        @Expose
        @SerializedName("fetchMultiTrendlineWidgetData")
        val fetchMultiTrendlineWidgetData: FetchMultiTrendlineWidgetDataModel? = FetchMultiTrendlineWidgetDataModel()
)

data class FetchMultiTrendlineWidgetDataModel(
        @Expose
        @SerializedName("data")
        val fetchMultiTrendlineData: List<MultiTrendlineWidgetDataModel>? = listOf()
)

data class MultiTrendlineWidgetDataModel(
        @Expose
        @SerializedName("data")
        val multiTrendlineData: MultiTrendlineModel? = MultiTrendlineModel(),
        @Expose
        @SerializedName("dataKey")
        val dataKey: String? = "",
        @Expose
        @SerializedName("error")
        val error: Boolean? = false,
        @Expose
        @SerializedName("errorMsg")
        val errorMsg: String? = "",
        @Expose
        @SerializedName("showWidget")
        val showWidget: Boolean? = false
)

data class MultiTrendlineModel(
        @Expose
        @SerializedName("metrics")
        val metrics: List<MultiTrendLineMetricModel>? = listOf()
)

data class MultiTrendLineMetricModel(
        @Expose
        @SerializedName("error")
        val isError: Boolean? = false,
        @Expose
        @SerializedName("errMsg")
        val errMsg: String? = "",
        @Expose
        @SerializedName("line")
        val line: LineModel? = LineModel(),
        @Expose
        @SerializedName("summary")
        val summary: MultiTrendlineSummaryModel? = MultiTrendlineSummaryModel(),
        @Expose
        @SerializedName("type")
        val type: String? = "",
        @Expose
        @SerializedName("yAxis")
        val yAxis: List<YAxisModel>? = listOf()
)

data class MultiTrendlineSummaryModel(
        @Expose
        @SerializedName("color")
        val color: String? = "",
        @Expose
        @SerializedName("description")
        val description: String? = "",
        @Expose
        @SerializedName("state")
        val state: String? = "",
        @Expose
        @SerializedName("title")
        val title: String? = "",
        @Expose
        @SerializedName("value")
        val value: String? = ""
)

data class YAxisModel(
        @Expose
        @SerializedName("YLabel")
        val yLabel: String? = "",
        @Expose
        @SerializedName("YVal")
        val yVal: Float? = 0f
)

data class LineModel(
        @Expose
        @SerializedName("currentPeriode")
        val currentPeriode: List<CurrentPeriodeModel>? = listOf(),
        @Expose
        @SerializedName("lastPeriode")
        val lastPeriode: List<CurrentPeriodeModel>? = listOf()
)

data class CurrentPeriodeModel(
        @Expose
        @SerializedName("XLabel")
        val xLabel: String? = "",
        @Expose
        @SerializedName("YLabel")
        val yLabel: String? = "",
        @Expose
        @SerializedName("YVal")
        val yVal: Float? = 0f
)