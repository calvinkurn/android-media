package com.tokopedia.product.estimasiongkir.data.model

import com.tokopedia.product.detail.data.util.KG

/**
 * Created by Yehezkiel on 25/01/21
 */
data class RatesEstimateRequest(
        val productWeight: Float = 0F,
        val shopDomain: String = "",
        val origin: String? = null,
        val shopId: String = "",
        val productId: String = "",
        val productWeightUnit: String = "",
        val isFreeOngkir: Boolean = false
) {
    fun getWeightRequest(): Float {
        return if (productWeightUnit.toLowerCase() == KG) productWeight else (productWeight / 1000)
    }
}