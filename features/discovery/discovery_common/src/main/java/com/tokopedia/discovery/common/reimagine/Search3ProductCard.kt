package com.tokopedia.discovery.common.reimagine

import com.tokopedia.remoteconfig.RollenceKey.SEARCH_3_PRODUCT_CARD_CONTROL
import com.tokopedia.remoteconfig.RollenceKey.SEARCH_3_PRODUCT_CARD_VAR_1A
import com.tokopedia.remoteconfig.RollenceKey.SEARCH_3_PRODUCT_CARD_VAR_1B
import com.tokopedia.remoteconfig.RollenceKey.SEARCH_3_PRODUCT_CARD_VAR_2A
import com.tokopedia.remoteconfig.RollenceKey.SEARCH_3_PRODUCT_CARD_VAR_2B

enum class Search3ProductCard(val variant: String) {
    CONTROL(SEARCH_3_PRODUCT_CARD_CONTROL) {
        override fun isUseAceSearchProductV5(): Boolean = false
    },
    VAR_1A(SEARCH_3_PRODUCT_CARD_VAR_1A),
    VAR_1B(SEARCH_3_PRODUCT_CARD_VAR_1B),
    VAR_2A(SEARCH_3_PRODUCT_CARD_VAR_2A),
    VAR_2B(SEARCH_3_PRODUCT_CARD_VAR_2B);

    open fun isUseAceSearchProductV5(): Boolean = true

    companion object {
        fun fromVariant(variant: String): Search3ProductCard =
            Search3ProductCard.values().find { it.variant == variant } ?: CONTROL
    }
}
