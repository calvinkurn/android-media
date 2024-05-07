package com.tokopedia.libra

sealed class LibraOwner(val type: String) {
    object Home : LibraOwner("home")
    object Discovery : LibraOwner("discovery")
}
