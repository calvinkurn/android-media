package com.tokopedia.product.estimasiongkir.util

import com.tokopedia.product.detail.view.util.ProductDetailLogger
import com.tokopedia.product.estimasiongkir.data.model.RatesEstimateRequest

object ProductDetailShippingLogger {
    private const val ERROR_TYPE_RATE_ESTIMATE = "error_rate_estimate"
    private const val LOCALIZATION_DATA_KEY = "localization_data"

    fun logRateEstimate(throwable: Throwable,
                                rateRequest: RatesEstimateRequest?,
                                deviceId: String) {
        val extras = mapOf(Pair(LOCALIZATION_DATA_KEY, rateRequest?.destination ?: "")).toString()
        ProductDetailLogger.logThrowable(
                throwable,
                ERROR_TYPE_RATE_ESTIMATE,
                rateRequest?.productId ?: "",
                deviceId,
                extras)
    }

}