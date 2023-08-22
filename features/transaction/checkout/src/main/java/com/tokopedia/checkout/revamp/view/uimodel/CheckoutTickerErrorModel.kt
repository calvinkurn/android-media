package com.tokopedia.checkout.revamp.view.uimodel

data class CheckoutTickerErrorModel(
    override val cartStringGroup: String = "",
    val errorMessage: String
) : CheckoutItem {

    val isError: Boolean
        get() = errorMessage.isNotEmpty()
}
