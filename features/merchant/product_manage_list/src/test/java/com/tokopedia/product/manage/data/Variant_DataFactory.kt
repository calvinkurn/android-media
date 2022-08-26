package com.tokopedia.product.manage.data

import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccess
import com.tokopedia.product.manage.common.feature.variant.adapter.model.ProductVariant
import com.tokopedia.product.manage.common.feature.variant.data.model.*
import com.tokopedia.product.manage.common.feature.variant.data.model.response.GetProductVariantResponse
import com.tokopedia.product.manage.common.feature.variant.presentation.data.EditVariantResult
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductCampaignType
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

fun createProductVariantResponse(
    productID: String = "1",
    status: ProductStatus = ProductStatus.ACTIVE,
    combination: List<Int> = emptyList(),
    isPrimary: Boolean = false,
    price: Double = 100.0,
    sku: String = "sku",
    stock: Int = 0,
    stockAlertStatus: String = "2",
    stockAlertCount: Int = 5,
    pictures: List<Picture> = emptyList(),
    isCampaign: Boolean = false
): Product {
    return Product(
        productID,
        status,
        combination,
        isPrimary,
        isCampaign,
        price,
        sku,
        stock,
        stockAlertStatus,
        stockAlertCount,
        pictures
    )
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
    isCampaign: Boolean = false,
    price: Double = 100.0,
    sku: String = "sku",
    stock: Int = 0,
    pictures: List<Picture> = emptyList(),
    isAllStockEmpty: Boolean = true,
    access: ProductManageAccess = createShopOwnerAccess(),
    campaignTypeList: List<CampaignType>? = emptyList(),
    maxStock: Int? = null
): ProductVariant {
    return ProductVariant(
        id,
        name,
        status,
        combination,
        isPrimary,
        isCampaign,
        price,
        sku,
        stock,
        pictures,
        isAllStockEmpty,
        access,
        campaignTypeList,
        maxStock
    )
}

fun createGetVariantResponse(
    productId: String = "",
    productName: String = "",
    stockAlertStatus: Int = 1,
    stockAlertCount: String = "5",
    stock: Int = 10,
    products: List<Product> = emptyList(),
    selections: List<Selection> = emptyList(),
    sizeCharts: List<Picture> = emptyList()
): GetProductVariantResponse {
    val variantResponse = Variant(products, selections, sizeCharts)
    val getProductV3Response = GetProductV3(
        productId,
        productName,
        stockAlertCount,
        stockAlertStatus,
        stock,
        variantResponse
    )
    return GetProductVariantResponse(getProductV3Response)
}

fun createEditVariantResult(
    productId: String = "1",
    productName: String = "Produk",
    variants: List<ProductVariant> = emptyList(),
    selections: List<Selection> = emptyList(),
    sizeCharts: List<Picture> = emptyList(),
    editStock: Boolean = false,
    editStatus: Boolean = false
): EditVariantResult {
    return EditVariantResult(
        productId,
        productName,
        variants,
        selections,
        sizeCharts,
        editStock,
        editStatus
    )
}