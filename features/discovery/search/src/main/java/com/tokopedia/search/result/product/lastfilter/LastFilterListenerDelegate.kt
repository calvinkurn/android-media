package com.tokopedia.search.result.product.lastfilter

import com.tokopedia.filter.common.data.SavedOption
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.iris.Iris
import com.tokopedia.search.result.presentation.view.fragment.RecyclerViewUpdater
import com.tokopedia.search.result.product.DynamicFilterModelProvider
import com.tokopedia.search.result.product.ProductListParameterListener
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.track.TrackApp
import javax.inject.Inject

class LastFilterListenerDelegate @Inject constructor(
    private val recyclerViewUpdater: RecyclerViewUpdater,
    private val iris: Iris,
    queryKeyProvider: QueryKeyProvider,
    productListParameterListener: ProductListParameterListener,
    searchParameterProvider: SearchParameterProvider,
    private val filterController: FilterController,
    private val lastFilterPresenter: LastFilterPresenter,
    private val dynamicFilterModelProvider: DynamicFilterModelProvider,
) : LastFilterListener,
    QueryKeyProvider by queryKeyProvider,
    ProductListParameterListener by productListParameterListener,
    SearchParameterProvider by searchParameterProvider {

    override fun onImpressedLastFilter(lastFilterDataView: LastFilterDataView) {
        lastFilterDataView.impress(iris)
    }

    override fun applyLastFilter(lastFilterDataView: LastFilterDataView) {
        lastFilterDataView.click(TrackApp.getInstance().gtm)

        filterController.setFilter(lastFilterDataView.filterOptions())

        val queryParams = filterController.getParameter() + lastFilterDataView.sortParameter()
        refreshSearchParameter(queryParams)

        reloadData()
    }

    override fun closeLastFilter(lastFilterDataView: LastFilterDataView) {
        LastFilterTracking.trackEventLastFilterClickClose(
            queryKey,
            lastFilterDataView.sortFilterParamsString(),
            lastFilterDataView.dimension90,
        )

        val searchParameterMap = getSearchParameter()?.getSearchParameterMap() ?: mapOf()

        recyclerViewUpdater.productListAdapter?.removeLastFilterWidget()

        lastFilterPresenter.closeLastFilter(searchParameterMap)
    }

    override fun updateLastFilter() {
        val mapParameter = getSearchParameter()?.getSearchParameterMap() ?: mapOf()
        val appliedSort = dynamicFilterModelProvider.dynamicFilterModel?.getAppliedSort(mapParameter)

        val savedOptionFromFilter = filterController.getActiveSavedOptionList()
        val savedOptionFromSort = appliedSort?.let { listOf(SavedOption.create(it)) } ?: listOf()
        val savedOptionList = savedOptionFromFilter + savedOptionFromSort

        lastFilterPresenter.updateLastFilter(
            mapParameter,
            savedOptionList,
        )
    }
}
