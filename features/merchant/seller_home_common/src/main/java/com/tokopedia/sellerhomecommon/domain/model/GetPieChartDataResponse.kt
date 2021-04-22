package com.tokopedia.sellerhomecommon.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetPieChartDataResponse(
        @Expose
        @SerializedName("fetchPieChartWidgetData")
        val fetchPieChartWidgetData: FetchPieChartWidgetDataModel = FetchPieChartWidgetDataModel()
)

data class FetchPieChartWidgetDataModel(
        @Expose
        @SerializedName("data")
        val `data`: List<PieChartWidgetDataModel> = emptyList()
)

data class PieChartWidgetDataModel(
        @Expose
        @SerializedName("data")
        val `data`: PieChartDataModel = PieChartDataModel(),
        @Expose
        @SerializedName("dataKey")
        val dataKey: String = "",
        @Expose
        @SerializedName("errorMsg")
        val errorMsg: String = "",
        @Expose
        @SerializedName("showWidget")
        val showWidget: Boolean = false
)

data class PieChartDataModel(
        @Expose
        @SerializedName("item")
        val item: List<PieChartItemModel> = emptyList(),
        @Expose
        @SerializedName("summary")
        val summary: ChartSummaryModel = ChartSummaryModel()
)

data class PieChartItemModel(
        @Expose
        @SerializedName("color")
        val color: String = "",
        @Expose
        @SerializedName("legend")
        val legend: String = "",
        @Expose
        @SerializedName("percentage")
        val percentage: Int = 0,
        @Expose
        @SerializedName("percentageFmt")
        val percentageFmt: String = "",
        @Expose
        @SerializedName("value")
        val value: Int = 0,
        @Expose
        @SerializedName("valueFmt")
        val valueFmt: String = ""
)