package com.tokopedia.content.common.model

import com.tokopedia.content.common.producttag.view.uimodel.ShopUiModel

/**
 * Created By : Jonathan Darwin on May 30, 2022
 */
class ShopModelBuilder {

    fun buildUiModelList(
        size: Int = 5,
    ): List<ShopUiModel> {
        return List(size) {
            ShopUiModel(
                shopId = it.toString(),
                shopName = "Shop $it",
            )
        }
    }

    fun buildUiModel(
        id: String = "1",
    ): ShopUiModel {
        return ShopUiModel(
            shopId = id,
            shopName = "Shop $id",
        )
    }
}