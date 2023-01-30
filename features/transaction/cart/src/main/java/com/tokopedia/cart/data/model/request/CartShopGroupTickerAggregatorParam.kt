package com.tokopedia.cart.data.model.request

import com.tokopedia.logisticcart.shipping.model.RatesParam

data class CartShopGroupTickerAggregatorParam(
    val ratesParam: RatesParam,
    val enableBoAffordability: Boolean = false,
    val enableBundleCrossSell: Boolean = false,
    val isTokoNow: Boolean = false
) {

    fun toMap(): Map<String, Any?> {
        return ratesParam.toCartShopGroupTickerAggregatorMap().toMutableMap().apply {
            "enable_bo_affordability" to enableBoAffordability
            "enable_bundle_cross_sell" to enableBundleCrossSell
            "is_tokonow" to isTokoNow
        }
    }
}
