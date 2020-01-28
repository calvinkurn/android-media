package com.tokopedia.sellerhome.domain.model


import com.google.gson.annotations.SerializedName

data class LineGraphDataResponse(
        @SerializedName("data")
        val responseData: Data?
)

data class Data(
        @SerializedName("getLineGraphData")
        val getLineGraphData: GetLineGraphDataModel?
)

data class GetLineGraphDataModel(
        @SerializedName("data")
        val widgetData: List<LineGraphDataModel>?
)

data class LineGraphDataModel(
        @SerializedName("dataKey")
        val dataKey: String?,
        @SerializedName("description")
        val description: String?,
        @SerializedName("error")
        val error: String?,
        @SerializedName("header")
        val header: String?,
        @SerializedName("list")
        val list: List<XYAxisModel>?,
        @SerializedName("yLabels")
        val yLabels: List<XYAxisModel>?
)

data class XYAxisModel(
        @SerializedName("xLabel")
        val xLabel: String?,
        @SerializedName("yLabel")
        val yLabel: String?,
        @SerializedName("yVal")
        val yVal: Long?
)