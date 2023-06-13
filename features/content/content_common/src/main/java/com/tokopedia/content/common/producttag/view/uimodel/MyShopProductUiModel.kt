package com.tokopedia.content.common.producttag.view.uimodel

import com.tokopedia.content.common.producttag.view.uimodel.ProductUiModel

/**
 * Created By : Jonathan Darwin on May 09, 2022
 */
data class MyShopProductUiModel(
    val products: List<ProductUiModel>,
    val state: PagedState,
    val param: SearchParamUiModel,
) {

    companion object {
        val Empty: MyShopProductUiModel
            get() = MyShopProductUiModel(
                products = emptyList(),
                state = PagedState.Unknown,
                param = SearchParamUiModel.Empty,
            )
    }
}