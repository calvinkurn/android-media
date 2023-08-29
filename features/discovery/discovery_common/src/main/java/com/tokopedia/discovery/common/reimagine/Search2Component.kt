package com.tokopedia.discovery.common.reimagine

import com.tokopedia.remoteconfig.RollenceKey.SEARCH_2_COMPONENT_CAROUSEL_VAR
import com.tokopedia.remoteconfig.RollenceKey.SEARCH_2_COMPONENT_CONTROL
import com.tokopedia.remoteconfig.RollenceKey.SEARCH_2_COMPONENT_QF_VAR

enum class Search2Component(val variant: String) {
    CONTROL(SEARCH_2_COMPONENT_CONTROL) {
        override fun hasMultilineProductName(): Boolean = false
        override fun isReimagineShopAds(): Boolean = false
        override fun isReimagineQuickFilter(): Boolean = false
        override fun isReimagineCarousel(): Boolean = false
    },

    CAROUSEL_VAR(SEARCH_2_COMPONENT_CAROUSEL_VAR) {
        override fun hasMultilineProductName(): Boolean = true
        override fun isReimagineShopAds(): Boolean = true
        override fun isReimagineQuickFilter(): Boolean = false
        override fun isReimagineCarousel(): Boolean = true
    },

    QF_VAR(SEARCH_2_COMPONENT_QF_VAR) {
        override fun hasMultilineProductName(): Boolean = false
        override fun isReimagineShopAds(): Boolean = true
        override fun isReimagineQuickFilter(): Boolean = true
        override fun isReimagineCarousel(): Boolean = true
    };

    abstract fun hasMultilineProductName(): Boolean
    abstract fun isReimagineShopAds(): Boolean
    abstract fun isReimagineQuickFilter(): Boolean
    abstract fun isReimagineCarousel(): Boolean

    companion object {
        fun fromVariant(variant: String): Search2Component =
            Search2Component.values().find { it.variant == variant } ?: CONTROL
    }
}
