package com.tokopedia.product.manage.data

import com.tokopedia.product.manage.feature.list.view.model.ProductViewModel
import com.tokopedia.shop.common.data.source.cloud.model.productlist.Picture
import com.tokopedia.shop.common.data.source.cloud.model.productlist.Price
import com.tokopedia.shop.common.data.source.cloud.model.productlist.Product
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

fun createProduct(
    id: String = "",
    name: String? = "Tolak Angin",
    price: Price? = Price(),
    stock: Int? = 1,
    status: ProductStatus? = ProductStatus.ACTIVE,
    cashback: Int = 0,
    featured: Int = 0,
    isVariant: Boolean? = false,
    url: String? = "productUrl",
    sku: String? = "sku",
    pictures: List<Picture>? = emptyList()
): Product {
    return Product(id, name, price, stock, status, cashback, featured, isVariant, url, sku, pictures)
}

fun createProductViewModel(
    id: String = "",
    name: String? = "Tolak Angin",
    imageUrl: String? = "imageUrl",
    price: String? = "100000",
    priceFormatted: String? = "Ro100.000",
    status: ProductStatus? = ProductStatus.ACTIVE,
    url: String? = "productUrl",
    cashback: Int = 0,
    stock: Int? = 1,
    featured: Boolean = false,
    isVariant: Boolean? = false,
    multiSelectActive: Boolean = false,
    isChecked: Boolean = false
): ProductViewModel {
    return ProductViewModel(
        id,
        name,
        imageUrl,
        price,
        priceFormatted,
        status,
        url,
        cashback,
        stock,
        featured,
        isVariant,
        multiSelectActive,
        isChecked
    )
}