package com.tokopedia.tokofood.purchase.promopage.presentation

data class UiEvent(
        var state: Int = 0,
        var data: Any? = null,
        var throwable: Throwable? = null
) {
    companion object {

    }
}