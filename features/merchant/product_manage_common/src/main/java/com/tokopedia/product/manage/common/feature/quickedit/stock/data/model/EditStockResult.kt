package com.tokopedia.product.manage.common.feature.quickedit.stock.data.model

import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

data class EditStockResult(
        val productName: String,
        val productId: String,
        val stock: Int?,
        val status: ProductStatus?,
        val error: Throwable? = null
): Throwable()