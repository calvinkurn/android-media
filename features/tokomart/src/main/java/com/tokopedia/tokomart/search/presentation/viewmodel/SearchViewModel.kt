package com.tokopedia.tokomart.search.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.UpdateCartUseCase
import com.tokopedia.tokomart.search.domain.model.SearchModel
import com.tokopedia.tokomart.search.presentation.model.SuggestionDataView
import com.tokopedia.tokomart.search.utils.SEARCH_FIRST_PAGE_USE_CASE
import com.tokopedia.tokomart.search.utils.SEARCH_LOAD_MORE_PAGE_USE_CASE
import com.tokopedia.tokomart.search.utils.SEARCH_QUERY_PARAM_MAP
import com.tokopedia.tokomart.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.tokomart.searchcategory.presentation.model.CategoryFilterDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.ChooseAddressDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductCountDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.TitleDataView
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
        addToCartUseCase: AddToCartUseCase,
        updateCartUseCase: UpdateCartUseCase,
        getWarehouseUseCase: GetChosenAddressWarehouseLocUseCase,
        chooseAddressWrapper: ChooseAddressWrapper,
        abTestPlatformWrapper: ABTestPlatformWrapper,
): BaseSearchCategoryViewModel(
        baseDispatcher,
        queryParamMap,
        getFilterUseCase,
        getProductCountUseCase,
        getMiniCartListSimplifiedUseCase,
        addToCartUseCase,
        updateCartUseCase,
        getWarehouseUseCase,
        chooseAddressWrapper,
        abTestPlatformWrapper,
) {

    val query = queryParamMap[SearchApiConst.Q] ?: ""

    private var suggestionModel: AceSearchProductModel.Suggestion? = null

    override fun loadFirstPage() {
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
        suggestionModel = searchModel.searchProduct.data.suggestion

        val headerDataView = HeaderDataView(
                title = "",
                hasSeeAllCategoryButton = false,
                aceSearchProductHeader = searchModel.searchProduct.header,
                categoryFilterDataValue = searchModel.categoryFilter,
                quickFilterDataValue = searchModel.quickFilter,
                bannerChannel = searchModel.bannerChannel,
        )

        val contentDataView = ContentDataView(
                aceSearchProductData = searchModel.searchProduct.data,
        )

        onGetFirstPageSuccess(headerDataView, contentDataView)
    }

    override fun postProcessHeaderList(headerList: MutableList<Visitable<*>>) {
        processSuggestionModel(headerList)
    }

    private fun processSuggestionModel(headerList: MutableList<Visitable<*>>) {
        val suggestionModel = suggestionModel ?: return

        if (suggestionModel.text.isNotEmpty()) {
            val suggestionDataViewIndex = determineSuggestionDataViewIndex(headerList)

            headerList.add(
                    suggestionDataViewIndex,
                    SuggestionDataView(
                            text = suggestionModel.text,
                            query = suggestionModel.query,
                            suggestion = suggestionModel.suggestion,
                    ),
            )
        }

        this.suggestionModel = null
    }

    private fun determineSuggestionDataViewIndex(headerList: List<Visitable<*>>): Int {
        val quickFilterIndex = headerList.indexOfFirst { it is QuickFilterDataView }

        return quickFilterIndex + 1
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
        val contentDataView = ContentDataView(aceSearchProductData = searchModel.searchProduct.data)
        onGetLoadMorePageSuccess(contentDataView)
    }

    private fun onGetSearchLoadMorePageError(throwable: Throwable) {

    }
}
