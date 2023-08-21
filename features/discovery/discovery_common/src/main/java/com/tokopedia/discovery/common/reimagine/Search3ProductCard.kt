package com.tokopedia.discovery.common.reimagine

import com.tokopedia.remoteconfig.RollenceKey.SEARCH_3_PRODUCT_CARD_CONTROL
import com.tokopedia.remoteconfig.RollenceKey.SEARCH_3_PRODUCT_CARD_PC_NEWLABEL_VAR
import com.tokopedia.remoteconfig.RollenceKey.SEARCH_3_PRODUCT_CARD_PC_VAR

enum class Search3ProductCard(val variant: String) {
    CONTROL(SEARCH_3_PRODUCT_CARD_CONTROL),
    PC_VAR(SEARCH_3_PRODUCT_CARD_PC_VAR),
    PC_NEW_LABEL_VAR(SEARCH_3_PRODUCT_CARD_PC_NEWLABEL_VAR);

    companion object {
        fun fromVariant(variant: String): Search3ProductCard =
            Search3ProductCard.values().find { it.variant == variant } ?: CONTROL
    }
}
