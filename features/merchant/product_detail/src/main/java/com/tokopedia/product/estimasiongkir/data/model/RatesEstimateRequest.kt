package com.tokopedia.product.estimasiongkir.data.model

import com.tokopedia.product.detail.data.util.KG
import com.tokopedia.product.detail.common.numberFormatted

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
        val isFulfillment: Boolean = false,
        val destination: String = "",
        val boType: Int = 0,
        val freeOngkirUrl: String = "",
        val poTime: Long = 0L,
        val uspImageUrl: String = "",
        val userId: String = "",
        val forceRefresh: Boolean = false,
        val shopTier: Int = 0,
        val isTokoNow: Boolean = false
) {
    companion object {
        const val KG_TEXT = "kilo"
        const val GRAM_TEXT = "gram"
    }

    fun getWeightRequest(): Float {
        return if (productWeightUnit.toLowerCase() == KG) productWeight else (productWeight / 1000)
    }

    fun getWeightTxt(): String = "${productWeight.numberFormatted()} ${
        if (productWeightUnit.toLowerCase() == KG)
            KG_TEXT else GRAM_TEXT
    }"
}