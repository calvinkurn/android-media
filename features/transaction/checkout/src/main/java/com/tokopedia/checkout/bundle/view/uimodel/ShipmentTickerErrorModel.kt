package com.tokopedia.checkout.bundle.view.uimodel

class ShipmentTickerErrorModel(
        val errorMessage: String = ""
) {
    val isError: Boolean
        get() = errorMessage.isNotEmpty()
}