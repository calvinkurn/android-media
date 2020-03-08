package com.tokopedia.product.manage.feature.quickedit.stock.data.model

data class EditStockResult(
        val productName: String,
        val productId: String,
        val stock: Int,
        val error: Throwable? = null
): Throwable()