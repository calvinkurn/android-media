package com.tokopedia.logisticCommon.data.constant

enum class AddEditAddressSource(val source: String) {
    CHECKOUT("checkout"),
    CART("cart"),
    OCC("occ"),
    TOKOFOOD("tokofood"),
    ADDRESS_LOCALIZATION_WIDGET("addressLocalizationWidget")
}