package com.tokopedia.product.manage.data

import com.tokopedia.product.manage.common.feature.list.data.model.PriceUiModel
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccess
import com.tokopedia.product.manage.common.feature.list.data.model.ProductUiModel
import com.tokopedia.product.manage.common.feature.list.data.model.TopAdsInfo
import com.tokopedia.shop.common.data.source.cloud.model.productlist.Picture
import com.tokopedia.shop.common.data.source.cloud.model.productlist.Price
import com.tokopedia.shop.common.data.source.cloud.model.productlist.Product
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductTopAds

fun createProduct(
    id: String = "",
    name: String? = "Tolak Angin",
    price: Price? = Price(),
    stock: Int? = 1,
    hasStockReserved: Boolean = false,
    status: ProductStatus? = ProductStatus.ACTIVE,
    cashback: Int = 0,
    featured: Int = 0,
    isVariant: Boolean? = false,
    url: String? = "productUrl",
    sku: String? = "sku",
    pictures: List<Picture>? = emptyList(),
    topAds: ProductTopAds? = null,
    isCampaign: Boolean = false
): Product {
    return Product(id, name, price, stock, hasStockReserved, status, cashback, featured, isVariant, url, sku, pictures, topAds, isCampaign)
}

fun createProductUiModel(
    id: String = "",
    name: String? = "Tolak Angin",
    imageUrl: String? = "imageUrl",
    minPrice: PriceUiModel? = PriceUiModel("10000", "Rp10.000"),
    maxPrice: PriceUiModel? = PriceUiModel("100000", "Rp100.000"),
    status: ProductStatus? = ProductStatus.ACTIVE,
    url: String? = "productUrl",
    cashback: Int = 0,
    stock: Int? = 1,
    featured: Boolean = false,
    isVariant: Boolean? = false,
    multiSelectActive: Boolean = false,
    isChecked: Boolean = false,
    hasStockReserved: Boolean = false,
    topAds: TopAdsInfo? = null,
    access: ProductManageAccess? = createShopOwnerAccess(),
    isCampaign: Boolean = false
): ProductUiModel {
    return ProductUiModel(
        id,
        name,
        imageUrl,
        minPrice,
        maxPrice,
        status,
        url,
        cashback,
        stock,
        featured,
        isVariant,
        multiSelectActive,
        isChecked,
        hasStockReserved,
        topAds,
        access,
        isCampaign
    )
}