package com.tokopedia.discovery.common.reimagine

import com.tokopedia.remoteconfig.RollenceKey

enum class Search3ProductCard(val variant: String) {
    CONTROL("") {
        override fun isUseAceSearchProductV5(): Boolean = true
        override fun isReimagineProductCard(): Boolean = true
    },
    PRODUCT_CARD_SRE_2024(RollenceKey.PRODUCT_CARD_SRE_2024) {
        override fun isUseAceSearchProductV5(): Boolean = true
        override fun isReimagineProductCard(): Boolean = true
    },
    REVERSE_PRODUCT_CARD(RollenceKey.REVERSE_PRODUCT_CARD_V4) {
        override fun isUseAceSearchProductV5(): Boolean = false
        override fun isReimagineProductCard(): Boolean = false
    };

    abstract fun isUseAceSearchProductV5(): Boolean
    abstract fun isReimagineProductCard(): Boolean

    companion object {
        fun fromVariant(variant: String): Search3ProductCard =
            Search3ProductCard.values().find { it.variant == variant } ?: CONTROL
    }
}
