package com.tokopedia.product.manage.feature.list.view.model

data class SetCashBackResult(
    val productId: String,
    val cashback: Int,
    val error: Throwable? = null
): Throwable()