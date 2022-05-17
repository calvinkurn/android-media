package com.tokopedia.createpost.producttag.view.uimodel

/**
 * Created By : Jonathan Darwin on May 09, 2022
 */
data class MyShopProductUiModel(
    val products: List<ProductUiModel>,
    val nextCursor: Int,
    val state: PagedState,
    val param: SearchParamUiModel,
) {

    companion object {
        val Empty = MyShopProductUiModel(
            products = emptyList(),
            nextCursor = 0,
            state = PagedState.Unknown,
            param = SearchParamUiModel.Empty
        )
    }
}