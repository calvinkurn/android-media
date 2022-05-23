package com.tokopedia.createpost.producttag.analytic.product

import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel

/**
 * Created By : Jonathan Darwin on May 23, 2022
 */
interface ProductTagAnalytic {

    fun clickBreadcrumb()

    fun clickProductTagSource(source: String)

    fun impressProductCard(
        source: String,
        shopId: String,
        productId: String,
        products: List<ProductUiModel>,
        isGlobalSearch: Boolean
    ) /** TODO: later */

    fun clickProductCard(
        source: String,
        shopId: String,
        productId: String,
        products: List<ProductUiModel>,
        isGlobalSearch: Boolean
    ) /** TODO: later */

    fun clickSearchBar(source: String)

    fun clickLastSearch()

    fun clickShopSuggestion(shopId: String)

    fun impressShopSuggestion(shopId: String) /** TODO: dunno how */

    fun clickKeywordSuggestion()

    fun clickGlobalSearchTab(tabName: String)

    fun clickBackButton(source: String)

    fun impressShopCard() /** TODO: later */

    fun clickShopCard() /** TODO: later */

    fun clickBackOnShop()
}