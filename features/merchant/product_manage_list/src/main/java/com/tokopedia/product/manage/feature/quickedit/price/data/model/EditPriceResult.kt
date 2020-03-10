package com.tokopedia.product.manage.feature.quickedit.price.data.model

data class EditPriceResult(
        val productName: String,
        val productId: String,
        val price: String,
        val error: Throwable? = null
): Throwable()