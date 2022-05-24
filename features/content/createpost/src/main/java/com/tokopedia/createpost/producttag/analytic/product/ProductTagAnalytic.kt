package com.tokopedia.createpost.producttag.analytic.product

import com.tokopedia.createpost.producttag.view.uimodel.ProductTagSource
import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel
import com.tokopedia.createpost.producttag.view.uimodel.ShopUiModel

/**
 * Created By : Jonathan Darwin on May 23, 2022
 */
interface ProductTagAnalytic {

    fun clickBreadcrumb(isOnShop: Boolean)

    fun clickProductTagSource(source: ProductTagSource)

    fun impressProductCard(
        source: ProductTagSource,
        shopId: String,
        productId: String,
        products: List<Pair<ProductUiModel, Int>>,
        isGlobalSearch: Boolean
    )

    fun clickProductCard(
        source: ProductTagSource,
        product: ProductUiModel,
        position: Int,
        isGlobalSearch: Boolean
    )

    fun clickSearchBar(source: ProductTagSource)

    fun clickLastSearch()

    fun clickShopSuggestion(shopId: String)

    fun impressShopSuggestion(shopId: String) /** TODO: dunno how */

    fun clickKeywordSuggestion()

    fun clickGlobalSearchTab(tabName: String)

    fun clickBackButton(source: ProductTagSource)

    fun impressShopCard(
        source: ProductTagSource,
        shopId: String,
        shops: List<Pair<ShopUiModel, Int>>,
    )

    fun clickShopCard(
        shop: ShopUiModel,
        position: Int,
    )

    fun clickSearchBarOnShop()

    fun impressProductCardOnShop(
        shopId: String,
        productId: String,
        products: List<Pair<ProductUiModel, Int>>,
    )

    fun clickProductCardOnShop(
        product: ProductUiModel,
        position: Int,
    )

    fun sendAll()
}