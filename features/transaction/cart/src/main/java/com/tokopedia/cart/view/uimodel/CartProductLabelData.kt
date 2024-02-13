package com.tokopedia.cart.view.uimodel

import com.tokopedia.cart.view.customview.HexColor

data class CartProductLabelData(
    val type: String = "",
    val remainingTimeMillis: Long = 0,
    val imageLogoUrl: String = "",
    val iconUrl: String = "",
    val text: String = "",
    val textColor: HexColor = HexColor(""),
    val backgroundStartColor: HexColor = HexColor(""),
    val backgroundEndColor: HexColor = HexColor(""),
    val lineColor: HexColor = HexColor("")
) {

    companion object {
        const val TYPE_DEFAULT = "default"
        const val TYPE_TIMER = "timer"
    }
}
