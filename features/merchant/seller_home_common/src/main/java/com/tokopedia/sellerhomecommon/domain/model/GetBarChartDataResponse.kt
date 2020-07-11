package com.tokopedia.sellerhomecommon.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetBarChartDataResponse(
        @Expose
        @SerializedName("fetchBarChartWidgetData")
        val fetchBarChartWidgetData: FetchBarChartWidgetDataModel
)

data class FetchBarChartWidgetDataModel(
        @Expose
        @SerializedName("data")
        val `data`: List<BarChartWidgetDataModel>
)

data class BarChartWidgetDataModel(
        @Expose
        @SerializedName("data")
        val `data`: BarChartDataModel,
        @Expose
        @SerializedName("dataKey")
        val dataKey: String,
        @Expose
        @SerializedName("errorMsg")
        val errorMsg: String
)

data class BarChartDataModel(
        @Expose
        @SerializedName("axes")
        val axes: BarChartAxesModel,
        @Expose
        @SerializedName("metrics")
        val metrics: List<BarChartMetricModel>,
        @Expose
        @SerializedName("summary")
        val summary: BarChartSummary
)

data class BarChartAxesModel(
        @Expose
        @SerializedName("xLabel")
        val xLabel: List<BarChartValueModel>,
        @Expose
        @SerializedName("yLabel")
        val yLabel: List<BarChartValueModel>
)

data class BarChartMetricModel(
        @Expose
        @SerializedName("name")
        val name: String,
        @Expose
        @SerializedName("value")
        val value: List<BarChartValueModel>
)

data class BarChartValueModel(
        @Expose
        @SerializedName("value")
        val value: Int,
        @Expose
        @SerializedName("valueFmt")
        val valueFmt: String
)

data class BarChartSummary(
        @Expose
        @SerializedName("diffPercentage")
        val diffPercentage: Int,
        @Expose
        @SerializedName("diffPercentageFmt")
        val diffPercentageFmt: String,
        @Expose
        @SerializedName("value")
        val value: Int,
        @Expose
        @SerializedName("valueFmt")
        val valueFmt: String
)