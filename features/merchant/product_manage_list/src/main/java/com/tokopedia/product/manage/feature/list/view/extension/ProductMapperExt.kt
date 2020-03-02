package com.tokopedia.product.manage.feature.list.view.extension

import com.tokopedia.product.manage.feature.list.view.model.ProductViewModel
import com.tokopedia.shop.common.data.source.cloud.model.productlist.Product

fun List<Product>?.mapToViewModels(): List<ProductViewModel> {
    return this?.map {
        ProductViewModel(
            id = it.id,
            title = it.name,
            imageUrl = it.pictures?.firstOrNull()?.urlThumbnail,
            price = it.price?.min?.toString(),
            status = it.status,
            stock = it.stock,
            isVariant = it.isVariant,
            url = it.url,
            cashBack = 100
        )
    } ?: emptyList()
}