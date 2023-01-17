package com.tokopedia.content.common.producttag.view.uimodel.state

import com.tokopedia.content.common.producttag.view.uimodel.ProductUiModel
import com.tokopedia.content.common.producttag.view.uimodel.*
import com.tokopedia.filter.common.data.DynamicFilterModel

/**
 * Created By : Jonathan Darwin on April 25, 2022
 */
data class ProductTagUiState(
    val productTagSource: ProductTagSourceUiState,
    val lastTaggedProduct: LastTaggedProductUiState,
    val lastPurchasedProduct: LastPurchasedProductUiState,
    val myShopProduct: MyShopProductUiState,
    val globalSearchProduct: GlobalSearchProductUiState,
    val globalSearchShop: GlobalSearchShopUiState,
    val shopProduct: ShopProductUiState,
    val selectedProduct: List<SelectedProductUiModel>,
    val isSubmitting: Boolean,
)

data class ProductTagSourceUiState(
    val productTagSourceList: List<ProductTagSource>,
    val productTagSourceStack: Set<ProductTagSource>,
)

data class LastTaggedProductUiState(
    val products: List<ProductUiModel>,
    val nextCursor: String,
    val state: PagedState,
)

data class LastPurchasedProductUiState(
    val products: List<ProductUiModel>,
    val nextCursor: String,
    val state: PagedState,
    val coachmark: String,
    val isCoachmarkShown: Boolean,
)

data class MyShopProductUiState(
    val products: List<ProductUiModel>,
    val sorts: List<SortUiModel>,
    val state: PagedState,
    val param: SearchParamUiModel,
) {
    fun hasFilter(): Boolean {
        return param.query.isNotEmpty()
    }
}

data class GlobalSearchProductUiState(
    val products: List<ProductUiModel>,
    val quickFilters: List<QuickFilterUiModel>,
    val sortFilters: DynamicFilterModel,
    val state: PagedState,
    val param: SearchParamUiModel,
    val suggestion: SuggestionUiModel,
    val ticker: TickerUiModel,
)

data class GlobalSearchShopUiState(
    val shops: List<ShopUiModel>,
    val quickFilters: List<QuickFilterUiModel>,
    val sortFilters: DynamicFilterModel,
    val state: PagedState,
    val param: SearchParamUiModel,
)

data class ShopProductUiState(
    val shop: ShopUiModel,
    val products: List<ProductUiModel>,
    val state: PagedState,
    val param: SearchParamUiModel,
) {
    fun hasFilter(): Boolean {
        return param.query.isNotEmpty()
    }
}