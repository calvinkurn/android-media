package com.tokopedia.play.broadcaster.ui.mapper

import com.tokopedia.play.broadcaster.data.model.Metrics
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricUiModel
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricsEnum

/**
 * @author by jessica on 04/06/20
 */

object PlaySummaryUiMapper {
    fun mapToLiveTrafficUiMetrics(metrics: Metrics): List<TrafficMetricUiModel> {
        val liveTrafficMetrics = mutableListOf<TrafficMetricUiModel>()
        with(liveTrafficMetrics) {
            add(TrafficMetricUiModel(TrafficMetricsEnum.TOTAL_VIEWS, metrics.visitChannel))
            add(TrafficMetricUiModel(TrafficMetricsEnum.VIDEO_LIKES, metrics.likeChannel))
            add(TrafficMetricUiModel(TrafficMetricsEnum.SHOP_VISIT, metrics.visitShop))
            add(TrafficMetricUiModel(TrafficMetricsEnum.PRODUCT_VISIT, metrics.visitPDP))
            add(TrafficMetricUiModel(TrafficMetricsEnum.NUMBER_OF_ATC, metrics.addToCart))
            add(TrafficMetricUiModel(TrafficMetricsEnum.NUMBER_OF_PAID_ORDER, metrics.paymentVerified))
            add(TrafficMetricUiModel(TrafficMetricsEnum.NEW_FOLLOWERS, metrics.followShop))
        }
        return liveTrafficMetrics
    }
}