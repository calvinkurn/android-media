package com.tokopedia.discovery.common.reimagine

import com.tokopedia.remoteconfig.RollenceKey

enum class Search1InstAuto(val variant: String) {
    CONTROL(RollenceKey.SEARCH_1_INST_AUTO_CONTROL),
    VARIANT_1(RollenceKey.SEARCH_1_INST_AUTO_VARIANT_1),
    VARIANT_2(RollenceKey.SEARCH_1_INST_AUTO_VARIANT_2),
    VARIANT_3(RollenceKey.SEARCH_1_INST_AUTO_VARIANT_3),
    VARIANT_4(RollenceKey.SEARCH_1_INST_AUTO_VARIANT_4);

    companion object {
        fun fromVariant(variant: String): Search1InstAuto =
            Search1InstAuto.values().find { it.variant == variant } ?: CONTROL
    }
}

