package com.tokopedia.tokomart.search.presentation.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.tokomart.search.domain.model.SearchModel
import com.tokopedia.tokomart.search.utils.SEARCH_FIRST_PAGE_USE_CASE
import com.tokopedia.tokomart.search.utils.SEARCH_LOAD_MORE_PAGE_USE_CASE
import com.tokopedia.tokomart.search.utils.SEARCH_QUERY_PARAM_MAP
import com.tokopedia.tokomart.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import com.tokopedia.tokomart.searchcategory.utils.ABTestPlatformWrapper
import com.tokopedia.tokomart.searchcategory.utils.ChooseAddressWrapper
import com.tokopedia.tokomart.searchcategory.utils.TOKONOW
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
        getFilterUseCase: UseCase<DynamicFilterModel>,
        getProductCountUseCase: UseCase<String>,
        getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase,
        chooseAddressWrapper: ChooseAddressWrapper,
        abTestPlatformWrapper: ABTestPlatformWrapper,
): BaseSearchCategoryViewModel(
        baseDispatcher,
        queryParamMap,
        getFilterUseCase,
        getProductCountUseCase,
        getMiniCartListSimplifiedUseCase,
        chooseAddressWrapper,
        abTestPlatformWrapper,
) {

    val query = queryParamMap[SearchApiConst.Q] ?: ""

    override fun onViewCreated() {
        getSearchFirstPageUseCase.cancelJobs()
        getSearchFirstPageUseCase.execute(
                ::onGetSearchFirstPageSuccess,
                ::onGetSearchFirstPageError,
                createRequestParams()
        )
    }

    override fun appendMandatoryParams(tokonowQueryParam: MutableMap<String, Any>) {
        super.appendMandatoryParams(tokonowQueryParam)

        tokonowQueryParam[SearchApiConst.SOURCE] = TOKONOW
    }

    private fun onGetSearchFirstPageSuccess(searchModel: SearchModel) {
        val headerDataView = HeaderDataView(
                title = "",
                hasSeeAllCategoryButton = false,
                aceSearchProductHeader = searchModel.searchProduct.header,
                categoryFilterDataValue = searchModel.categoryFilter,
                quickFilterDataValue = searchModel.quickFilter,
                bannerChannel = searchModel.bannerChannel,
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
                ::onGetSearchLoadMorePageSuccess,
                ::onGetSearchLoadMorePageError,
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
