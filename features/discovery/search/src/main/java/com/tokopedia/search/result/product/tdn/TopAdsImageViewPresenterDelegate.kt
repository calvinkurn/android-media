package com.tokopedia.search.result.product.tdn

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.presentation.model.SearchProductTopAdsImageDataView
import com.tokopedia.topads.sdk.domain.model.TopAdsImageUiModel
import timber.log.Timber
import javax.inject.Inject

@SearchScope
class TopAdsImageViewPresenterDelegate @Inject constructor() {
    private var topAdsImageUiModelList = mutableListOf<TopAdsImageUiModel>()

    fun setTopAdsImageViewModelList(topAdsImageUiModelList: List<TopAdsImageUiModel>) {
        this.topAdsImageUiModelList = topAdsImageUiModelList.toMutableList()
    }

    fun processTopAdsImageViewModel(
        totalProductItem: Int,
        action: (Int, Visitable<*>) -> Unit,
        logAction: (Exception) -> Unit,
    ) {
        if (topAdsImageUiModelList.isEmpty()) return

        val topAdsImageViewModelIterator = topAdsImageUiModelList.iterator()

        while (topAdsImageViewModelIterator.hasNext()) {
            val data = topAdsImageViewModelIterator.next()

            if (data.position <= 0) {
                topAdsImageViewModelIterator.remove()
                continue
            }

            if (data.position <= totalProductItem) {
                try {
                    val searchProductTopAdsImageDataView = SearchProductTopAdsImageDataView(data)
                    action(data.position, searchProductTopAdsImageDataView)

                    topAdsImageViewModelIterator.remove()
                } catch (exception: java.lang.Exception) {
                    Timber.w(exception)
                    logAction(exception)
                }
            }
        }
    }

    companion object {
        const val TOP_POSITION = 1
    }
}
