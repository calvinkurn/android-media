package com.tokopedia.product.manage.feature.list.view.model

data class EditPriceResult(
    val productId: String,
    val price: String,
    val error: Throwable? = null
): Throwable()