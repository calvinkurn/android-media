package com.tokopedia.tokopedianow.data

import com.tokopedia.tokopedianow.common.model.TokoNowProductCardViewUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseProductUiModel

fun createRepurchaseProductUiModel(
    parentId: String = "",
    shopId: String = "",
    categoryId: String = "",
    category: String = "",
    productCard: TokoNowProductCardViewUiModel = TokoNowProductCardViewUiModel(),
    position: Int
): RepurchaseProductUiModel  {
    return RepurchaseProductUiModel(parentId, shopId, categoryId, category, position, productCard)
}
