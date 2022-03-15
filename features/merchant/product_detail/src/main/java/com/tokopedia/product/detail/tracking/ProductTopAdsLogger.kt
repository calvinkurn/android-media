package com.tokopedia.product.detail.tracking

import android.util.Log
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

object ProductTopAdsLogger {
    private const val TOPADS_PDP = "TOPADS_PDP"

    const val MAX_LIMIT = 1000
    const val TOPADS_PDP_TIMEOUT_EXCEEDED = "topads_pdp_timeout"
    const val TOPADS_PDP_HIT_DYNAMIC_SLOTTING = "topads_pdp_hit_dynamic_slotting"
    const val TOPADS_PDP_HIT_ADS_TRACKER = "topads_pdp_hit_ads_tracker"
    const val TOPADS_PDP_IS_NOT_ADS = "topads_pdp_is_not_ads"
    const val TOPADS_PDP_BE_ERROR = "topads_recom_page_be_error"
    const val TOPADS_PDP_GENERAL_ERROR = "topads_recom_page_general_error"

    fun logServer(
        tag: String,
        reason: String = "",
        productId: String = "-1",
        queryParam: String = "-1",
        data: String = "",
        throwable: Throwable? = null
    ) {
        var reasonValue = reason
        var dataValue = data

        throwable?.let {
            reasonValue = (it.message ?: "").take(MAX_LIMIT)
            dataValue = Log.getStackTraceString(it).take(MAX_LIMIT)
        }
        ServerLogger.log(
            Priority.P2,
            TOPADS_PDP_GENERAL_ERROR,
            mapOf(
                "action" to tag,
                "productId" to productId,
                "queryParam" to queryParam,
                "reason" to reasonValue,
                "data" to dataValue
            )
        )
    }
}