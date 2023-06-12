package com.tokopedia.shop_nib.util.extension

fun Long.toMb() : Float {
    if (this == 0L) return 0.0f
    return (this.toFloat() / (1024 * 1024))
}
