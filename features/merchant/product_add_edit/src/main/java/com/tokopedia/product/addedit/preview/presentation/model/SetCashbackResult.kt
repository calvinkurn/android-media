package com.tokopedia.product.addedit.preview.presentation.model

data class SetCashbackResult(
        val productId: String = "",
        val productName: String = "",
        val cashback : Int = 0,
        val limitExceeded: Boolean = false,
        val error: Throwable? = null
) : Throwable()