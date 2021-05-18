package com.tokopedia.product.manage.data

import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccess

fun createShopOwnerAccess(): ProductManageAccess {
    return ProductManageAccess(
        addProduct = true,
        editProduct = true,
        etalaseList = true,
        multiSelect = true,
        editPrice = true,
        editStock = true,
        duplicateProduct = true,
        setStockReminder = true,
        deleteProduct = true,
        setTopAds = true,
        setCashBack = true,
        setFeatured = true,
        productList = true,
        broadcastChat = true
    )
}

fun createDefaultAccess(): ProductManageAccess {
    return ProductManageAccess(
        addProduct = false,
        editProduct = false,
        etalaseList = false,
        multiSelect = false,
        editPrice = false,
        editStock = false,
        duplicateProduct = false,
        setStockReminder = false,
        deleteProduct = false,
        setTopAds = false,
        setCashBack = false,
        setFeatured = false,
        productList = false,
        broadcastChat = false
    )
}