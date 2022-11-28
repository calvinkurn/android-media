package com.tokopedia.affiliate.sse.model

import com.google.gson.annotations.SerializedName

data class AffiliateSSEAdpTotalClick(
    @SerializedName("event_name")
    val eventName: String? = null,
    @SerializedName("day_range")
    val dayRange: Int? = null,
    @SerializedName("data")
    val data: ClickData? = null
) {
    data class ClickData(
        @SerializedName("metric_type")
        val metricType: String? = null,
        @SerializedName("metric_value")
        val metricValue: Int? = null,
        @SerializedName("metric_difference_value")
        val metricDifferenceValue: Int? = null,
        @SerializedName("page_type")
        val pageType: Int? = null,
        @SerializedName("item_id")
        val itemId: Long? = null,
        @SerializedName("tooltip")
        val tooltip: ToolTip? = null
    ) {
        data class ToolTip(
            @SerializedName("metrics")
            val metrics: List<String?>? = null
        )
    }
}
