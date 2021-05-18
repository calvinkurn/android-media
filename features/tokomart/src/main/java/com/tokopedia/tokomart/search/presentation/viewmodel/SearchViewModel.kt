package com.tokopedia.tokomart.search.presentation.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.tokomart.search.domain.model.SearchModel
import com.tokopedia.tokomart.search.utils.SEARCH_FIRST_PAGE_USE_CASE
import com.tokopedia.tokomart.search.utils.SEARCH_LOAD_MORE_PAGE_USE_CASE
import com.tokopedia.tokomart.search.utils.SEARCH_QUERY_PARAM_MAP
import com.tokopedia.tokomart.searchcategory.domain.model.FilterModel
import com.tokopedia.tokomart.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import com.tokopedia.tokomart.searchcategory.utils.ChooseAddressWrapper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class SearchViewModel @Inject constructor (
        baseDispatcher: CoroutineDispatchers,
        @Named(SEARCH_QUERY_PARAM_MAP)
        queryParamMap: Map<String, String>,
        @param:Named(SEARCH_FIRST_PAGE_USE_CASE)
        private val getSearchFirstPageUseCase: UseCase<SearchModel>,
        @param:Named(SEARCH_LOAD_MORE_PAGE_USE_CASE)
        private val getSearchLoadMorePageUseCase: UseCase<SearchModel>,
        getFilterUseCase: UseCase<FilterModel>,
        chooseAddressWrapper: ChooseAddressWrapper,
): BaseSearchCategoryViewModel(baseDispatcher, queryParamMap, getFilterUseCase, chooseAddressWrapper) {

    val query = queryParamMap[SearchApiConst.Q] ?: ""

    override fun onViewCreated() {
        getSearchFirstPageUseCase.cancelJobs()
        getSearchFirstPageUseCase.execute(
                this::onGetSearchFirstPageSuccess,
                this::onGetSearchFirstPageError,
                createRequestParams()
        )
    }

    private fun createRequestParams() = RequestParams.create().also {
        it.putInt(SearchApiConst.PAGE, nextPage)
        it.putBoolean(SearchApiConst.USE_PAGE, true)
        it.putString(SearchApiConst.SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH)
        it.putString(SearchApiConst.DEVICE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE)
        it.putAll(queryParamMap as Map<String, String>)
    }

    private fun onGetSearchFirstPageSuccess(searchModel: SearchModel) {
        val headerDataView = HeaderDataView(
                title = "",
                hasSeeAllCategoryButton = false,
                aceSearchProductHeader = searchModel.searchProduct.header,
                quickFilterDataValue = searchModel.quickFilter,
        )

        val contentDataView = ContentDataView(
                productList = searchModel.searchProduct.data.productList,
        )

        onGetFirstPageSuccess(headerDataView, contentDataView)
    }

    private fun onGetSearchFirstPageError(throwable: Throwable) {

    }

    override fun executeLoadMore() {
        getSearchLoadMorePageUseCase.cancelJobs()
        getSearchLoadMorePageUseCase.execute(
                this::onGetSearchLoadMorePageSuccess,
                this::onGetSearchLoadMorePageError,
                createRequestParams(),
        )
    }

    private fun onGetSearchLoadMorePageSuccess(searchModel: SearchModel) {
        val contentDataView = ContentDataView(productList = searchModel.searchProduct.data.productList)
        onGetLoadMorePageSuccess(contentDataView)
    }

    private fun onGetSearchLoadMorePageError(throwable: Throwable) {

    }
}
