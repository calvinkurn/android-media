package com.tokopedia.wishlist

import androidx.recyclerview.widget.RecyclerView

internal inline fun <reified T : RecyclerView.Adapter<*>> RecyclerView?.adapter(): T {
    val castedAdapter = this?.adapter as? T
    checkNotNull(castedAdapter) {
        "Adapter is not instance of ${T::class.java.simpleName}"
    }
    return castedAdapter
}
