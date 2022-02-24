package com.tokopedia.product.detail.tracking

import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

object ProductTopAdsLogger {
    private const val TOPADS_PDP = "TOPADS_PDP"

    const val TOPADS_PDP_TIMEOUT_EXCEEDED = "topads_pdp_timeout"
    const val TOPADS_PDP_HIT_DYNAMIC_SLOTTING = "topads_pdp_hit_dynamic_slotting"
    const val TOPADS_PDP_HIT_ADS_TRACKER = "topads_pdp_hit_ads_tracker"
    const val TOPADS_PDP_IS_NOT_ADS = "topads_pdp_is_not_ads"
    fun logServer(tag: String, reason: String = "", productId: String = "-1", queryParam: String = "-1") {
        ServerLogger.log(
            Priority.P1,
            TOPADS_PDP,
            mapOf(
                "action" to tag,
                "productId" to productId,
                "queryParam" to queryParam,
                "reason" to reason
            )
        )
    }
}