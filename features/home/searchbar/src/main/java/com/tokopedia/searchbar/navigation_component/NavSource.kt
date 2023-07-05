package com.tokopedia.searchbar.navigation_component

import android.content.Intent
import android.os.Build
import java.io.Serializable

enum class NavSource {
    DEFAULT,
    ACCOUNT,
    AFFILIATE,
    CART,
    CATALOG,
    CLP,
    DISCOVERY,
    DISCOVERY_DEALS,
    DISCOVERY_SOS,
    DT,
    FEED,
    HOME,
    HOME_WISHLIST,
    HOME_UOH,
    MVC,
    NOTIFICATION,
    PDP,
    SHOP,
    SRP,
    SRP_UNIVERSAL,
    THANKYOU,
    TOKOFOOD,
    TOKONOW,
    UOH,
    WISHLIST;
}

fun String?.asNavSource(): NavSource {
    if(this == null) return NavSource.DEFAULT
    NavSource.values().forEach {
        if (it.name == this) return it
    }
    return NavSource.DEFAULT
}

inline fun <reified T : Serializable> Intent.getSerializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
}
