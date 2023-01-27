package com.tokopedia.search.result.product.visitable

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.ProductDataView

data class VisitableFactoryFirstPageData(
    val productDataView: ProductDataView,
    val pageTitle: String,
    val isGlobalNavWidgetAvailable: Boolean,
    val isLocalSearch: Boolean,
    val isTickerHasDismissed: Boolean,
    val responseCode: String,
    val productList: List<Visitable<*>>,
    val searchProductModel: SearchProductModel,
    val externalReference: String,
    val globalSearchApplink: String,
)

data class VisitableFactorySecondPageData(
    val isLocalSearch: Boolean,
    val responseCode: String,
    val allProductList: List<Visitable<*>>,
    val searchProductModel: SearchProductModel,
    val externalReference: String,
    val globalSearchApplink: String,
    val loadMoreProductList: List<Visitable<*>>,
)
