package com.tokopedia.searchbar.navigation_component

enum class NavSource {
    DEFAULT,
    ACCOUNT,
    AFFILIATE,
    CART,
    CATALOG,
    CLP,
    DISCOVERY,
    DT,
    FEED,
    HOME,
    HOME_WISHLIST,
    HOME_UOH,
    MVC,
    NOTIFICATION,
    PDP,
    SHOP,
    SOS,
    SRP,
    SRP_UNIVERSAL,
    THANKYOU,
    TOKOFOOD,
    TOKONOW,
    UOH,
    USER_PROFILE,
    WISHLIST
}

fun String?.asNavSource(): NavSource {
    return NavSource.values().find {
        it.name == this
    } ?: NavSource.DEFAULT
}
