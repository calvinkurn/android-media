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
    WISHLIST
}

fun String?.asNavSource(): NavSource {
    if(this == null) return NavSource.DEFAULT
    NavSource.values().forEach {
        if (it.name == this) return it
    }
    return NavSource.DEFAULT
}
