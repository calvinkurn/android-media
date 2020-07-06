package com.tokopedia.sellerhomecommon.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetPieChartDataResponse(
        @Expose
        @SerializedName("fetchPieChartWidgetData")
        val fetchPieChartWidgetData: FetchPieChartWidgetDataModel
)

data class FetchPieChartWidgetDataModel(
        @Expose
        @SerializedName("data")
        val `data`: List<PieChartWidgetDataModel>
)

data class PieChartWidgetDataModel(
        @Expose
        @SerializedName("data")
        val `data`: PieChartDataModel,
        @Expose
        @SerializedName("dataKey")
        val dataKey: String,
        @Expose
        @SerializedName("errorMsg")
        val errorMsg: String
)

data class PieChartDataModel(
        @Expose
        @SerializedName("item")
        val item: List<PieChartItemModel>,
        @Expose
        @SerializedName("summary")
        val summary: PieChartSummary
)

data class PieChartItemModel(
        @Expose
        @SerializedName("color")
        val color: String,
        @Expose
        @SerializedName("legend")
        val legend: String,
        @Expose
        @SerializedName("percentage")
        val percentage: Int,
        @Expose
        @SerializedName("percentageFmt")
        val percentageFmt: String,
        @Expose
        @SerializedName("value")
        val value: Int,
        @Expose
        @SerializedName("valueFmt")
        val valueFmt: String
)

data class PieChartSummary(
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