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
        val isFreeOngkir: Boolean = false,
        val isFullfillment: Boolean = false,
        val forceRefresh: Boolean = false
//TODO new free ongkir url
) {
    companion object {
        const val KG_TEXT = "kilo"
        const val GRAM_TEXT = "gram"
    }

    fun getWeightRequest(): Float {
        return if (productWeightUnit.toLowerCase() == KG) productWeight else (productWeight / 1000)
    }

    fun getWeightTxt(): String {
        return if (productWeightUnit.toLowerCase() == KG) {
            "$productWeight $KG_TEXT"
        } else {
            val resultGram = productWeight / 1000
            if (resultGram >= 1) {
                "${productWeight / 1000} $KG_TEXT"
            } else {
                "${productWeight.toLong()} $GRAM_TEXT"
            }
        }
    }
}