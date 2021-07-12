package com.tokopedia.sellerhomecommon.domain.model

/**
 * Created By @ilhamsuaib on 21/05/20
 */

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetLineGraphDataResponse(
        @Expose
        @SerializedName("fetchLineGraphWidgetData")
        val getLineGraphData: GetLineGraphDataModel?
)

data class GetLineGraphDataModel(
        @Expose
        @SerializedName("data")
        val widgetData: List<LineGraphDataModel>?
)

data class LineGraphDataModel(
        @Expose
        @SerializedName("dataKey")
        val dataKey: String?,
        @Expose
        @SerializedName("description")
        val description: String?,
        @Expose
        @SerializedName("error")
        val error: String?,
        @Expose
        @SerializedName("showWidget")
        val showWidget: Boolean?,
        @Expose
        @SerializedName("header")
        val header: String?,
        @Expose
        @SerializedName("list")
        val list: List<XYAxisModel>?,
        @Expose
        @SerializedName("yLabels")
        val yLabels: List<XYAxisModel>?
)

data class XYAxisModel(
        @Expose
        @SerializedName("xLabel")
        val xLabel: String?,
        @Expose
        @SerializedName("yLabel")
        val yLabel: String?,
        @Expose
        @SerializedName("yValPrecise")
        val yVal: Float?
)