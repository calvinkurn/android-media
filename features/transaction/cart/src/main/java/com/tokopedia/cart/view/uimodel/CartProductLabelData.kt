package com.tokopedia.cart.view.uimodel

data class CartProductLabelData(
    val type: String = "",
    val localExpiredTimeMillis: Long = 0,
    val imageLogoUrl: String = "",
    val iconUrl: String = "",
    val text: String = "",
    val textColor: HexColor = HexColor(""),
    val backgroundStartColor: HexColor = HexColor(""),
    val backgroundEndColor: HexColor = HexColor(""),
    val lineColor: HexColor = HexColor(""),
    val alwaysShowTimer: Boolean = false
) {

    companion object {
        const val TYPE_DEFAULT = "default"
        const val TYPE_TIMER = "timer"
    }
}
