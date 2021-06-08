package com.tokopedia.checkout.view.uimodel

class ShipmentTickerErrorModel(
        internal val errorMessage: String = "",
        internal var hasShown: Boolean = false
) {
    val isError: Boolean
        get() = errorMessage.isNotEmpty()
}