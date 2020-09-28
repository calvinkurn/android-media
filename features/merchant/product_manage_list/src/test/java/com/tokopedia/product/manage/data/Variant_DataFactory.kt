package com.tokopedia.product.manage.data

import com.tokopedia.product.manage.feature.quickedit.variant.adapter.model.ProductVariant
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.GetProductV3
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.Option
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.Picture
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.Product
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.Selection
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.Variant
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.response.GetProductVariantResponse
import com.tokopedia.product.manage.feature.quickedit.variant.presentation.data.EditVariantResult
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

fun createProductVariantResponse(
    productID: String = "1",
    status: ProductStatus = ProductStatus.ACTIVE,
    combination: List<Int> = emptyList(),
    isPrimary: Boolean = false,
    price: Int = 100,
    sku: String = "sku",
    stock: Int = 0,
    pictures: List<Picture> = emptyList()
): Product {
    return Product(productID, status, combination, isPrimary, price, sku, stock, pictures)
}

fun createSelectionResponse(
    variantID: String = "1",
    variantName: String = "Warna",
    unitName: String = "",
    unitID: String = "2",
    identifier: String = "id",
    options: List<Option> = emptyList()
): Selection {
    return Selection(variantID, variantName, unitName, unitID, identifier, options)
}

fun createOptionResponse(
    unitValueID: String = "1",
    value: String = "Biru",
    hexCode: String = ""
): Option {
    return Option(unitValueID, value, hexCode)
}

fun createProductVariant(
    id: String = "1",
    name: String = "",
    status: ProductStatus = ProductStatus.ACTIVE,
    combination: List<Int> = emptyList(),
    isPrimary: Boolean = false,
    price: Int = 100,
    sku: String = "sku",
    stock: Int = 0,
    pictures: List<Picture> = emptyList(),
    isAllStockEmpty: Boolean = true
): ProductVariant {
    return ProductVariant(id, name, status, combination, isPrimary, price, sku, stock, pictures, isAllStockEmpty)
}

fun createGetVariantResponse(
    productName: String = "",
    products: List<Product> = emptyList(),
    selections: List<Selection> = emptyList(),
    sizeCharts: List<Picture> = emptyList()
): GetProductVariantResponse {
    val variantResponse = Variant(products, selections, sizeCharts)
    val getProductV3Response = GetProductV3(productName, variantResponse)
    return GetProductVariantResponse(getProductV3Response)
}

fun createEditVariantResult(
    productId: String = "1",
    productName: String = "Produk",
    variants: List<ProductVariant> = emptyList(),
    selections: List<Selection> = emptyList(),
    sizeCharts: List<Picture> = emptyList()
): EditVariantResult {
    return EditVariantResult(productId, productName, variants, selections, sizeCharts)
}