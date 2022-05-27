package com.tokopedia.createpost.producttag.analytic.product

import com.tokopedia.createpost.producttag.view.uimodel.*

/**
 * Created By : Jonathan Darwin on May 23, 2022
 */
interface ProductTagAnalytic {

    fun trackGlobalSearchProduct(
        header: SearchHeaderUiModel,
        param: SearchParamUiModel,
    )

    fun trackGlobalSearchShop(
        header: SearchHeaderUiModel,
        param: SearchParamUiModel,
    )

    fun clickBreadcrumb(isOnShop: Boolean)

    fun clickProductTagSource(source: ProductTagSource)

    fun impressProductCard(
        source: ProductTagSource,
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

    fun clickGlobalSearchTab(tabName: String)

    fun clickBackButton(source: ProductTagSource)

    fun impressShopCard(
        source: ProductTagSource,
        shops: List<Pair<ShopUiModel, Int>>,
    )

    fun clickShopCard(
        shop: ShopUiModel,
        position: Int,
    )

    fun clickSearchBarOnShop()

    fun impressProductCardOnShop(
        products: List<Pair<ProductUiModel, Int>>,
    )

    fun clickProductCardOnShop(
        product: ProductUiModel,
        position: Int,
    )

    fun sendAll()
}