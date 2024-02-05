package com.tokopedia.discovery.common.reimagine

import com.tokopedia.remoteconfig.RollenceKey

enum class Search2Component(val variant: String) {
    CONTROL("") {
        override fun isReimagineCarousel(): Boolean = false
    },

    PRODUCT_CARD_SRE_2024(RollenceKey.PRODUCT_CARD_SRE_2024) {
        override fun isReimagineCarousel(): Boolean = true
    };

    fun isReimagineShopAds(): Boolean = false
    fun isReimagineQuickFilter(): Boolean = true
    abstract fun isReimagineCarousel(): Boolean

    companion object {
        fun fromVariant(variant: String): Search2Component =
            Search2Component.values().find { it.variant == variant } ?: CONTROL
    }
}
