package com.tokopedia.createpost.producttag.view.uimodel.action

import com.tokopedia.createpost.producttag.view.uimodel.ProductTagSource
import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel
import com.tokopedia.createpost.producttag.view.uimodel.QuickFilterUiModel
import com.tokopedia.createpost.producttag.view.uimodel.ShopUiModel

/**
 * Created By : Jonathan Darwin on April 25, 2022
 */
sealed interface ProductTagAction {
    object BackPressed: ProductTagAction
    object ClickBreadcrumb: ProductTagAction
    object OpenAutoCompletePage: ProductTagAction

    data class SetDataFromAutoComplete(val source: ProductTagSource, val query: String, val shopId: String): ProductTagAction
    data class SelectProductTagSource(val source: ProductTagSource): ProductTagAction
    data class ProductSelected(val product: ProductUiModel): ProductTagAction

    /** Last Tagged Product */
    object LoadLastTaggedProduct: ProductTagAction

    /** Last Purchased Product */
    object LoadLastPurchasedProduct: ProductTagAction

    /** My Shop Product */
    object LoadMyShopProduct: ProductTagAction
    data class SearchMyShopProduct(val query: String): ProductTagAction
    object OpenMyShopSortBottomSheet: ProductTagAction

    /** Global Search Product */
    object LoadGlobalSearchProduct: ProductTagAction
    object TickerClicked: ProductTagAction
    object CloseTicker: ProductTagAction
    data class SelectProductQuickFilter(val quickFilter: QuickFilterUiModel): ProductTagAction
    object OpenProductSortFilterBottomSheet: ProductTagAction
    data class RequestProductFilterProductCount(val selectedSortFilter: Map<String, String>): ProductTagAction
    data class ApplyProductSortFilter(val selectedSortFilter: Map<String, String>): ProductTagAction

    /** Global Search Shop */
    object LoadGlobalSearchShop: ProductTagAction
    data class ShopSelected(val shop: ShopUiModel): ProductTagAction
    data class SelectShopQuickFilter(val quickFilter: QuickFilterUiModel): ProductTagAction
    object OpenShopSortFilterBottomSheet: ProductTagAction
    data class RequestShopFilterProductCount(val selectedSortFilter: Map<String, String>): ProductTagAction
    data class ApplyShopSortFilter(val selectedSortFilter: Map<String, String>): ProductTagAction

    /** Shop Product */
    object LoadShopProduct: ProductTagAction
    data class SearchShopProduct(val query: String): ProductTagAction
}