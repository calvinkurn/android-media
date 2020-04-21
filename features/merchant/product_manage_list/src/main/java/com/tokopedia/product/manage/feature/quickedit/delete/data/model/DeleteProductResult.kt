package com.tokopedia.product.manage.feature.quickedit.delete.data.model

data class DeleteProductResult (
        val productName: String,
        val productId: String,
        val error: Throwable? = null
): Throwable()