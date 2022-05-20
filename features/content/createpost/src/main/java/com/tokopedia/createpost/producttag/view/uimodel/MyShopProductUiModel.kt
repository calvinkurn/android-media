package com.tokopedia.createpost.producttag.view.uimodel

import com.tokopedia.filter.common.data.DynamicFilterModel

/**
 * Created By : Jonathan Darwin on May 09, 2022
 */
data class MyShopProductUiModel(
    val products: List<ProductUiModel>,
    val sorts: DynamicFilterModel,
    val state: PagedState,
    val param: SearchParamUiModel,
) {

    companion object {
        val Empty: MyShopProductUiModel
            get() = MyShopProductUiModel(
                products = emptyList(),
                sorts = DynamicFilterModel(),
                state = PagedState.Unknown,
                param = SearchParamUiModel.Empty
            )
    }
}