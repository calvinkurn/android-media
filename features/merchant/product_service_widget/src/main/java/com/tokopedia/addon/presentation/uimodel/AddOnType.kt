package com.tokopedia.addon.presentation.uimodel

enum class AddOnType(val value: String) {
    GREETING_CARD_TYPE("GREETING_CARD_TYPE"),
    GREETING_CARD_AND_PACKAGING_TYPE("GREETING_CARD_AND_PACKAGING_TYPE"),
    INSTALLATION_TYPE("INSTALLATION_TYPE"),
    PRODUCT_PROTECTION_INSURANCE_TYPE("PRODUCT_PROTECTION_INSURANCE_TYPE");

    fun toRequestAddonType(): Int {
        return ordinal.inc()
    }
}
