package com.tokopedia.discovery.common.reimagine

import com.tokopedia.remoteconfig.RollenceKey.SEARCH_2_COMPONENT_CAROUSEL_VAR
import com.tokopedia.remoteconfig.RollenceKey.SEARCH_2_COMPONENT_CONTROL
import com.tokopedia.remoteconfig.RollenceKey.SEARCH_2_COMPONENT_QF_VAR

enum class Search2Component(val variant: String) {
    CONTROL(SEARCH_2_COMPONENT_CONTROL),
    CAROUSEL_VAR(SEARCH_2_COMPONENT_CAROUSEL_VAR),
    QF_VAR(SEARCH_2_COMPONENT_QF_VAR);

    companion object {
        fun fromVariant(variant: String): Search2Component =
            Search2Component.values().find { it.variant == variant } ?: CONTROL
    }
}
