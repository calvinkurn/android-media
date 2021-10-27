package com.tokopedia.checkout.view.uimodel

class ShipmentTickerErrorModel(
        val errorMessage: String = ""
) {
    val isError: Boolean
        get() = errorMessage.isNotEmpty()
}