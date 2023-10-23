package com.tokopedia.search.result.product.banned

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.product.ViewUpdater
import com.tokopedia.search.result.product.globalnavwidget.GlobalNavDataView
import javax.inject.Inject

@SearchScope
class BannedProductsPresenterDelegate @Inject constructor(
    private val bannedProductsView: BannedProductsView,
    private val viewUpdater: ViewUpdater,
) {

    fun isBannedProducts(searchProduct: SearchProductModel): Boolean =
        searchProduct.errorMessage.isNotEmpty()

    fun processBannedProducts(
        searchProduct: SearchProductModel,
        globalNavDataView: GlobalNavDataView?,
    ) {
        val bannedProductsVisitableList = createBannedProductsVisitableList(
            searchProduct,
            globalNavDataView,
        )

        viewUpdater.removeLoading()
        viewUpdater.appendItems(bannedProductsVisitableList)
        bannedProductsView.trackEventImpressionBannedProducts()
    }

    private fun createBannedProductsVisitableList(
        searchProduct: SearchProductModel,
        globalNavDataView: GlobalNavDataView?,
    ): List<Visitable<*>> =
        mutableListOf<Visitable<*>>().apply {
            globalNavDataView?.let { add(it) }

            add(BannedProductsEmptySearchDataView(searchProduct.errorMessage))
        }
}
