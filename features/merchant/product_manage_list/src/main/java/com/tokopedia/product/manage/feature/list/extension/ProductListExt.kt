package com.tokopedia.product.manage.feature.list.extension

import com.tokopedia.product.manage.common.feature.list.data.model.ProductUiModel

fun MutableList<ProductUiModel>.findIndex(productId: String): Int? {
    return find { it.id == productId }?.let { indexOf(it) }
}
