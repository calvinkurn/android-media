package com.tokopedia.affiliate.sse.model

import com.google.gson.annotations.SerializedName

data class AffiliateSSEAdpTotalClickItem(
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

        @SerializedName("metric_difference_value")
        val metricDifferenceValue: Int? = null,

        @SerializedName("page_type")
        val pageType: Int? = null,

        @SerializedName("item_id")
        val itemId: Long? = null
    )
}
