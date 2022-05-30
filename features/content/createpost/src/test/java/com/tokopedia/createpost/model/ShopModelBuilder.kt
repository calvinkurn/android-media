package com.tokopedia.createpost.model

import com.tokopedia.createpost.producttag.view.uimodel.ShopUiModel

/**
 * Created By : Jonathan Darwin on May 30, 2022
 */
class ShopModelBuilder {

    fun buildUiModel(
        size: Int = 5,
    ): List<ShopUiModel> {
        return List(size) {
            ShopUiModel(
                shopId = it.toString(),
                shopName = "Shop $it",
            )
        }
    }
}