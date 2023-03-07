package com.tokopedia.content.common.producttag.view.uimodel

import com.tokopedia.content.common.producttag.view.uimodel.ProductUiModel

/**
 * Created By : Jonathan Darwin on May 11, 2022
 */
data class ShopProductUiModel(
    val shop: ShopUiModel,
    val products: List<ProductUiModel>,
    val state: PagedState,
    val param: SearchParamUiModel,
) {

    companion object {
        val Empty: ShopProductUiModel
            get() = ShopProductUiModel(
                shop = ShopUiModel(),
                products = emptyList(),
                state = PagedState.Unknown,
                param = SearchParamUiModel.Empty,
            )
    }
}