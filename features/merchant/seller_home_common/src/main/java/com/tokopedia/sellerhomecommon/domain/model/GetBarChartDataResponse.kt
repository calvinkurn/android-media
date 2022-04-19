package com.tokopedia.sellerhomecommon.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetBarChartDataResponse(
        @Expose
        @SerializedName("fetchBarChartWidgetData")
        val fetchBarChartWidgetData: FetchBarChartWidgetDataModel = FetchBarChartWidgetDataModel()
)

data class FetchBarChartWidgetDataModel(
        @Expose
        @SerializedName("data")
        val `data`: List<BarChartWidgetDataModel> = emptyList()
)

data class BarChartWidgetDataModel(
        @Expose
        @SerializedName("data")
        val `data`: BarChartDataModel = BarChartDataModel(),
        @Expose
        @SerializedName("dataKey")
        val dataKey: String = "",
        @Expose
        @SerializedName("showWidget")
        val showWidget: Boolean = false,
        @Expose
        @SerializedName("errorMsg")
        val errorMsg: String = ""
)

data class BarChartDataModel(
        @Expose
        @SerializedName("axes")
        val axes: BarChartAxesModel = BarChartAxesModel(),
        @Expose
        @SerializedName("metrics")
        val metrics: List<BarChartMetricModel> = emptyList(),
        @Expose
        @SerializedName("summary")
        val summary: ChartSummaryModel = ChartSummaryModel()
)

data class BarChartAxesModel(
        @Expose
        @SerializedName("xLabel")
        val xLabel: List<BarChartValueModel> = emptyList(),
        @Expose
        @SerializedName("yLabel")
        val yLabel: List<BarChartValueModel> = emptyList()
)

data class BarChartMetricModel(
        @Expose
        @SerializedName("name")
        val name: String = "",
        @Expose
        @SerializedName("value")
        val value: List<BarChartValueModel> = emptyList()
)

data class BarChartValueModel(
        @Expose
        @SerializedName("value")
        val value: Int = 0,
        @Expose
        @SerializedName("valueFmt")
        val valueFmt: String = "",
        @Expose
        @SerializedName("color")
        val hexColor: String = ""
)