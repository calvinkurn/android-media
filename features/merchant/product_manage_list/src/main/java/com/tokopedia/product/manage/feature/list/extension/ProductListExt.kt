package com.tokopedia.product.manage.feature.list.extension

import com.tokopedia.product.manage.feature.list.view.model.ProductViewModel

fun MutableList<ProductViewModel>.findIndex(productId: String): Int? {
    return find { it.id == productId }?.let { indexOf(it) }
}