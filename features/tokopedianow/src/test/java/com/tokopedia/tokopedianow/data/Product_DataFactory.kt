package com.tokopedia.tokopedianow.data

import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseProductUiModel

fun createRepurchaseProductUiModel(
    parentId: String = "",
    shopId: String = "",
    categoryId: String = "",
    category: String = "",
    productCard: ProductCardCompactUiModel = ProductCardCompactUiModel(),
    position: Int
): RepurchaseProductUiModel  {
    return RepurchaseProductUiModel(parentId, shopId, categoryId, category, position, productCard)
}
