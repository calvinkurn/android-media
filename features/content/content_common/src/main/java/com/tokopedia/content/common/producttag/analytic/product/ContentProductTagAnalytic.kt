package com.tokopedia.content.common.producttag.analytic.product

import com.tokopedia.content.common.producttag.view.uimodel.ProductUiModel
import com.tokopedia.content.common.producttag.view.uimodel.*
import com.tokopedia.content.common.ui.model.ContentAccountUiModel

/**
 * Created By : Jonathan Darwin on May 23, 2022
 */
interface ContentProductTagAnalytic {

    fun trackGlobalSearchProduct(
        header: SearchHeaderUiModel,
        param: SearchParamUiModel,
    )

    fun trackGlobalSearchShop(
        header: SearchHeaderUiModel,
        param: SearchParamUiModel,
    )

    fun clickBreadcrumb(isOnShop: Boolean)

    fun clickProductTagSource(source: ProductTagSource, authorId: String, authorType: String)

    fun impressProductCard(
        source: ProductTagSource,
        products: List<Pair<ProductUiModel, Int>>,
        isEntryPoint: Boolean
    )

    fun clickProductCard(
        source: ProductTagSource,
        product: ProductUiModel,
        position: Int,
        isEntryPoint: Boolean
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

    fun clickSaveProduct(source: ProductTagSource)

    fun clickAdvancedProductFilter()

    fun clickSaveAdvancedProductFilter()

    fun clickProductFilterChips()

    fun viewProductTagSourceBottomSheet(authorId: String, authorType: String)

    fun clickCloseOnProductTagSourceBottomSheet(authorId: String, authorType: String)

    fun sendAll()
}
