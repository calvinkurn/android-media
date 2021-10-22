package com.tokopedia.tokopedianow.data

import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseProductUiModel

fun createRepurchaseProductUiModel(
    id: String = "",
    parentId: String = "",
    shopId: String = "",
    categoryId: String = "",
    isStockEmpty: Boolean = false,
    productCard: ProductCardModel = ProductCardModel()
): RepurchaseProductUiModel  {
    return RepurchaseProductUiModel(id, parentId, shopId, categoryId, isStockEmpty, productCard)
}