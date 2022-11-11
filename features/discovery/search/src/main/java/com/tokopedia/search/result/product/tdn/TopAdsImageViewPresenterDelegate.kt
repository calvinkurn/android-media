package com.tokopedia.search.result.product.tdn

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.model.SearchProductTopAdsImageDataView
import com.tokopedia.search.result.product.cpm.CpmDataView
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import timber.log.Timber
import javax.inject.Inject

class TopAdsImageViewPresenterDelegate @Inject constructor() {
    private var topAdsImageViewModelList = mutableListOf<TopAdsImageViewModel>()

    fun setTopAdsImageViewModelList(topAdsImageViewModelList: List<TopAdsImageViewModel>) {
        this.topAdsImageViewModelList = topAdsImageViewModelList.toMutableList()
    }

    fun processTopAdsImageViewModel(
        list: List<Visitable<*>>,
        productList: List<Visitable<*>>,
        action: (Int, Visitable<*>) -> Unit,
        logAction: (Exception) -> Unit,
    ) {
        if (topAdsImageViewModelList.isEmpty()) return

        val topAdsImageViewModelIterator = topAdsImageViewModelList.iterator()

        while (topAdsImageViewModelIterator.hasNext()) {
            val data = topAdsImageViewModelIterator.next()

            if (data.position <= 0) {
                topAdsImageViewModelIterator.remove()
                continue
            }

            if (data.position <= productList.size) {
                try {
                    processTopAdsImageViewModelInPosition(list, productList, data, action)
                    topAdsImageViewModelIterator.remove()
                } catch (exception: java.lang.Exception) {
                    Timber.w(exception)
                    logAction(exception)
                }
            }
        }
    }

    private fun processTopAdsImageViewModelInPosition(
        list: List<Visitable<*>>,
        productList: List<Visitable<*>>,
        data: TopAdsImageViewModel,
        action: (Int, Visitable<*>) -> Unit,
    ) {
        val isTopPosition = data.position == 1
        val searchProductTopAdsImageDataView = SearchProductTopAdsImageDataView(data)
        if (isTopPosition) {
            val index = getIndexOfTopAdsImageViewModelAtTop(list)
            action(index, searchProductTopAdsImageDataView)
        } else {
            val product = productList[data.position - 1]
            action(list.indexOf(product) + 1, searchProductTopAdsImageDataView)
        }
    }

    private fun getIndexOfTopAdsImageViewModelAtTop(list: List<Visitable<*>>): Int {
        var index = 0
        while (shouldIncrementIndexForTopAdsImageViewModel(index, list)) index++
        return index
    }

    private fun shouldIncrementIndexForTopAdsImageViewModel(
        index: Int,
        list: List<Visitable<*>>,
    ): Boolean {
        if (index >= list.size) return false

        val visitable = list[index]
        val isCPMOrProductItem = visitable is CpmDataView || visitable is ProductItemDataView

        return !isCPMOrProductItem
    }
}
