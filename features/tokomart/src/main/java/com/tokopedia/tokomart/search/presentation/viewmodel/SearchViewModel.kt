package com.tokopedia.tokomart.search.presentation.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokomart.search.domain.model.SearchModel
import com.tokopedia.tokomart.search.utils.SEARCH_FIRST_PAGE_USE_CASE
import com.tokopedia.tokomart.search.utils.SEARCH_LOAD_MORE_PAGE_USE_CASE
import com.tokopedia.tokomart.searchcategory.presentation.BaseSearchCategoryViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class SearchViewModel @Inject constructor (
        baseDispatcher: CoroutineDispatchers,
        @param:Named(SEARCH_FIRST_PAGE_USE_CASE)
        private val getSearchFirstPageUseCase: UseCase<SearchModel>,
        @param:Named(SEARCH_LOAD_MORE_PAGE_USE_CASE)
        private val getSearchLoadMorePageUseCase: UseCase<SearchModel>,
): BaseSearchCategoryViewModel(baseDispatcher) {

    override fun onViewCreated() {
        getSearchFirstPageUseCase.cancelJobs()
        getSearchFirstPageUseCase.execute(
                this::onGetSearchFirstPageSuccess,
                this::onGetSearchFirstPageError,
                RequestParams.create()
        )
    }

    private fun onGetSearchFirstPageSuccess(searchModel: SearchModel) {
        val headerDataView = HeaderDataView(
                title = "",
                hasSeeAllCategoryButton = false,
                totalData = searchModel.searchProduct.header.totalData
        )

        val contentDataView = ContentDataView(
                productList = searchModel.searchProduct.data.productList,
        )

        onGetFirstPageSuccess(headerDataView, contentDataView)
    }

    private fun onGetSearchFirstPageError(throwable: Throwable) {

    }

    override fun onLoadMore() {
        if (hasLoadedAllData()) return

        getSearchLoadMorePageUseCase.cancelJobs()
        getSearchLoadMorePageUseCase.execute(
                this::onGetSearchLoadMorePageSuccess,
                this::onGetSearchLoadMorePageError,
                RequestParams.create()
        )
    }

    private fun hasLoadedAllData() = totalData == totalFetchedData

    private fun onGetSearchLoadMorePageSuccess(searchModel: SearchModel) {
        val contentDataView = ContentDataView(productList = searchModel.searchProduct.data.productList)
        onGetLoadMorePageSuccess(contentDataView)
    }

    private fun onGetSearchLoadMorePageError(throwable: Throwable) {

    }
}
