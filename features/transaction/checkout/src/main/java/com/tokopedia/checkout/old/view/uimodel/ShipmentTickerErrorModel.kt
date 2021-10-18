package com.tokopedia.checkout.old.view.uimodel

class ShipmentTickerErrorModel(
        val errorMessage: String = ""
) {
    val isError: Boolean
        get() = errorMessage.isNotEmpty()
}