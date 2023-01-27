package com.tokopedia.cart.data.model.request

import com.tokopedia.logisticcart.shipping.model.RatesParam

data class CartShopGroupTickerAggregatorParam(
    val ratesParam: RatesParam,
    val enableBoAffordability: Boolean = false,
    val enableBundleCrossSell: Boolean = false,
    val isTokoNow: Boolean = false,
) {

    fun toMap(): Map<String, Any?> {
        val mappedParam = ratesParam.toCartShopGroupTickerAggregatorMap().toMutableMap()
        mappedParam["enable_bo_affordability"] = enableBoAffordability
        mappedParam["enable_bundle_cross_sell"] = enableBundleCrossSell
        mappedParam["is_tokonow"] = isTokoNow
        return mappedParam
    }
}
