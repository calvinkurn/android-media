package com.tokopedia.checkout.backup.view.uimodel

data class CheckoutTickerErrorModel(
    override val cartStringGroup: String = "",
    val errorMessage: String
) : CheckoutItem {

    val isError: Boolean
        get() = errorMessage.isNotEmpty()
}
