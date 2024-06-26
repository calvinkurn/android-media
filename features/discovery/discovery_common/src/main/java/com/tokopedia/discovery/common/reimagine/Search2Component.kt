package com.tokopedia.discovery.common.reimagine

import com.tokopedia.remoteconfig.RollenceKey

enum class Search2Component(val variant: String) {
    CONTROL("") {
        override fun isReimagineShopAds(): Boolean = true
        override fun isReimagineCarousel(): Boolean = true
    },
    PRODUCT_CARD_SRE_2024(RollenceKey.PRODUCT_CARD_SRE_2024) {
        override fun isReimagineShopAds(): Boolean = true
        override fun isReimagineCarousel(): Boolean = true
    },
    REVERSE_PRODUCT_CARD(RollenceKey.REVERSE_PRODUCT_CARD_V4) {
        override fun isReimagineShopAds(): Boolean = false
        override fun isReimagineCarousel(): Boolean = false
    };

    abstract fun isReimagineShopAds(): Boolean
    fun isReimagineQuickFilter(): Boolean = true
    abstract fun isReimagineCarousel(): Boolean

    companion object {
        fun fromVariant(variant: String): Search2Component =
            Search2Component.values().find { it.variant == variant } ?: CONTROL
    }
}
