package com.tokopedia.sellerorder.orderextension.presentation.util


fun Int.toColorString(): String {
    return "#${Integer.toHexString(
        this and 0x00ffffff
    )}"
}
