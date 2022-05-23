package com.tokopedia.createpost.producttag.analytic.product

import com.tokopedia.createpost.producttag.view.uimodel.ProductTagSource
import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel

/**
 * Created By : Jonathan Darwin on May 23, 2022
 */
interface ProductTagAnalytic {

    fun clickBreadcrumb()

    fun clickProductTagSource(source: ProductTagSource)

    fun impressProductCard(
        source: ProductTagSource,
        shopId: String,
        productId: String,
        products: List<Pair<ProductUiModel, Int>>,
        isGlobalSearch: Boolean
    ) /** TODO: later */

    fun clickProductCard(
        source: ProductTagSource,
        product: Pair<ProductUiModel, Int>,
        isGlobalSearch: Boolean
    ) /** TODO: later */

    fun clickSearchBar(source: ProductTagSource)

    fun clickLastSearch()

    fun clickShopSuggestion(shopId: String)

    fun impressShopSuggestion(shopId: String) /** TODO: dunno how */

    fun clickKeywordSuggestion()

    fun clickGlobalSearchTab(tabName: String)

    fun clickBackButton(source: ProductTagSource)

    fun impressShopCard() /** TODO: later */

    fun clickShopCard() /** TODO: later */
}