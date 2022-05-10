package com.tokopedia.createpost.producttag.view.uimodel.action

import com.tokopedia.createpost.producttag.view.uimodel.ProductTagSource
import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel

/**
 * Created By : Jonathan Darwin on April 25, 2022
 */
sealed interface ProductTagAction {
    data class SelectProductTagSource(val source: ProductTagSource): ProductTagAction
    data class ProductSelected(val product: ProductUiModel): ProductTagAction

    /** Last Tagged Product */
    object LoadLastTaggedProduct: ProductTagAction

    /** Last Purchased Product */
    object LoadLastPurchasedProduct: ProductTagAction

    /** My Shop Product */
    object LoadMyShopProduct: ProductTagAction
    data class SearchMyShopProduct(val query: String): ProductTagAction

    /** Global Search Product */
    object LoadGlobalSearchProduct: ProductTagAction
}