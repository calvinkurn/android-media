package com.tokopedia.logisticCommon.data.constant

enum class ManageAddressSource(val value: String) {
    ACCOUNT("account"),
    CART("cart"),
    CHECKOUT("checkout"),
    DISTRICT_NOT_MATCH("districtNotMatch"),
    LOCALIZED_ADDRESS_WIDGET("localizedAddressWidget"),
    NOTIFICATION("notification"),
    TOKOFOOD("tokofood")
}