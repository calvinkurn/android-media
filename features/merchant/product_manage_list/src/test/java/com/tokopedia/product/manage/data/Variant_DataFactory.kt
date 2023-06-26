// ktlint-disable filename
package com.tokopedia.product.manage.data

import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccess
import com.tokopedia.product.manage.common.feature.variant.adapter.model.ProductVariant
import com.tokopedia.product.manage.common.feature.variant.data.model.CampaignType
import com.tokopedia.product.manage.common.feature.variant.data.model.GetProductV3
import com.tokopedia.product.manage.common.feature.variant.data.model.Option
import com.tokopedia.product.manage.common.feature.variant.data.model.Picture
import com.tokopedia.product.manage.common.feature.variant.data.model.Product
import com.tokopedia.product.manage.common.feature.variant.data.model.Selection
import com.tokopedia.product.manage.common.feature.variant.data.model.Variant
import com.tokopedia.product.manage.common.feature.variant.data.model.response.GetProductVariantResponse
import com.tokopedia.product.manage.common.feature.variant.presentation.data.EditVariantResult
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

fun createProductVariantResponse(
    productID: String = "1",
    status: ProductStatus = ProductStatus.ACTIVE,
    combination: List<Int> = emptyList(),
    isPrimary: Boolean = false,
    price: Double = 100.0,
    sku: String = "sku",
    stock: Int = 0,
    stockAlertStatus: Int = 1,
    stockAlertCount: String = "0",
    pictures: List<Picture> = emptyList(),
    isCampaign: Boolean = false,
    hasStockAlert: Boolean = false,
    notifyMeOOSCount: Int = 0,
    isBelowStockAlert: Boolean = false,
    isEmptyStock: Boolean = false,
    hasDTStock: Boolean = false,
    isTokoCabang: Boolean = false
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
        stockAlertCount,
        stockAlertStatus,
        pictures,
        hasStockAlert = hasStockAlert,
        isEmptyStock = isEmptyStock,
        isBelowStockAlert = isBelowStockAlert,
        notifymeCount = notifyMeOOSCount,
        hasDTStock = hasDTStock,
        isTokoCabang = isTokoCabang
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
    maxStock: Int? = null,
    notifyMeOOSCount: Int = 0,
    stockAlertStatus: Int = 1,
    stockAlertCount: Int = 0,
    isBelowStockAlert: Boolean = false,
    hasDTStock: Boolean = false,
    isTokoCabang: Boolean = false
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
        maxStock,
        notifyMeOOSCount,
        stockAlertStatus,
        stockAlertCount,
        isBelowStockAlert,
        hasDTStock,
        isTokoCabang
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
    sizeCharts: List<Picture> = emptyList(),
    notifyMeOOSCount: Int = 0
): GetProductVariantResponse {
    val variantResponse = Variant(products, selections, sizeCharts)
    val getProductV3Response = GetProductV3(
        productId,
        productName,
        stockAlertCount,
        stockAlertStatus,
        stock,
        notifyMeOOSCount,
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
