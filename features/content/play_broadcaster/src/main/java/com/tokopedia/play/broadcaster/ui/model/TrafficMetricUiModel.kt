package com.tokopedia.play.broadcaster.ui.model

/**
 * @author by jessica on 04/06/20
 */

data class TrafficMetricUiModel(
        val trafficMetricEnum: TrafficMetricsEnum? = null,
        val liveTrafficMetricCount: String = ""
)