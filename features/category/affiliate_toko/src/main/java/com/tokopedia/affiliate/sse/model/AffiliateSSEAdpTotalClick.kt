package com.tokopedia.affiliate.sse.model

import com.google.gson.annotations.SerializedName

data class AffiliateSSEAdpTotalClick(
    @SerializedName("event_name")
    val eventName: String? = null,
    @SerializedName("day_range")
    val dayRange: Int? = null,
    @SerializedName("data")
    val data: Data? = null
) {
    data class Data(
        @SerializedName("metric_type")
        val metricType: String? = null,
        @SerializedName("metric_value")
        val metricValue: Int? = null,
        @SerializedName("metric_value_fmt")
        val metricValueFmt: String? = null,
        @SerializedName("metric_difference_value")
        val metricDifferenceValue: Int? = null,
        @SerializedName("metric_difference_value_fmt")
        val metricDifferenceValueFmt: String? = null,
        @SerializedName("tooltip")
        val tooltip: ToolTip? = null
    ) {
        data class ToolTip(
            @SerializedName("metrics")
            val metrics: List<Metric?>? = null
        ) {
            data class Metric(
                @SerializedName("metric_type")
                val metricType: String? = null,
                @SerializedName("metric_value")
                val metricValue: String? = null,
                @SerializedName("metric_value_fmt")
                val metricValueFmt: String? = null,
                @SerializedName("metric_difference_fmt")
                val metricDifferenceFmt: String? = null,
                @SerializedName("metric_difference_value")
                val metricDifferenceValue: String? = null
            )
        }
    }
}
