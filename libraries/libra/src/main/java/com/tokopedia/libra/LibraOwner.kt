package com.tokopedia.libra

/**
 * A ownership of Libra's experiment source.
 *
 * Each page owner have to register their own Owner's type (tribe name),
 * please contact representative your tribe to get the type's name.
 */
sealed class LibraOwner(val type: String) {
    object Home : LibraOwner("home")
    object Discovery : LibraOwner("discovery")
}
