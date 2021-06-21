package com.tokopedia.tokomart.search.presentation.viewmodel

import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.UpdateCartUseCase
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.EVENT.EVENT_CLICK_TOKONOW
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_PHYSICAL_GOODS
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokomart.search.domain.model.SearchModel
import com.tokopedia.tokomart.search.presentation.model.SuggestionDataView
import com.tokopedia.tokomart.search.utils.SEARCH_FIRST_PAGE_USE_CASE
import com.tokopedia.tokomart.search.utils.SEARCH_LOAD_MORE_PAGE_USE_CASE
import com.tokopedia.tokomart.search.utils.SEARCH_QUERY_PARAM_MAP
import com.tokopedia.tokomart.search.analytics.SearchTracking.Action.GENERAL_SEARCH
import com.tokopedia.tokomart.search.analytics.SearchTracking.Category.TOKONOW_TOP_NAV
import com.tokopedia.tokomart.search.analytics.SearchTracking.Misc.HASIL_PENCARIAN_DI_TOKONOW
import com.tokopedia.tokomart.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.tokomart.searchcategory.domain.model.AceSearchProductModel.SearchProductHeader
import com.tokopedia.tokomart.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokomart.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import com.tokopedia.tokomart.searchcategory.utils.ABTestPlatformWrapper
import com.tokopedia.tokomart.searchcategory.utils.ChooseAddressWrapper
import com.tokopedia.tokomart.searchcategory.utils.TOKONOW
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.utils.lifecycle.SingleLiveEvent
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

    private val generalSearchEventMutableLiveData = SingleLiveEvent<Map<String, Any>>()
    val generalSearchEventLiveData: LiveData<Map<String, Any>> = generalSearchEventMutableLiveData

    val query = queryParamMap[SearchApiConst.Q] ?: ""

    private var suggestionModel: AceSearchProductModel.Suggestion? = null

    override val tokonowSource: String
        get() = TOKONOW

    override fun loadFirstPage() {
        getSearchFirstPageUseCase.cancelJobs()
        getSearchFirstPageUseCase.execute(
                ::onGetSearchFirstPageSuccess,
                ::onGetSearchFirstPageError,
                createRequestParams()
        )
    }

    private fun onGetSearchFirstPageSuccess(searchModel: SearchModel) {
        suggestionModel = searchModel.searchProduct.data.suggestion

        val searchProductHeader = searchModel.searchProduct.header

        val headerDataView = HeaderDataView(
                title = "",
                hasSeeAllCategoryButton = false,
                aceSearchProductHeader = searchProductHeader,
                categoryFilterDataValue = searchModel.categoryFilter,
                quickFilterDataValue = searchModel.quickFilter,
                bannerChannel = searchModel.bannerChannel,
        )

        val contentDataView = ContentDataView(
                aceSearchProductData = searchModel.searchProduct.data,
        )

        onGetFirstPageSuccess(headerDataView, contentDataView)

        sendGeneralSearchTracking(searchProductHeader)
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

    private fun sendGeneralSearchTracking(searchProductHeader: SearchProductHeader) {
        val eventLabel = query +
                "|${searchProductHeader.keywordProcess}" +
                "|${searchProductHeader.responseCode}" +
                "|$BUSINESS_UNIT_PHYSICAL_GOODS" +
                "|$TOKONOW" +
                "|$HASIL_PENCARIAN_DI_TOKONOW" +
                "|${searchProductHeader.totalData}"

        val generalSearchDataLayer = mapOf(
                EVENT to EVENT_CLICK_TOKONOW,
                EVENT_ACTION to GENERAL_SEARCH,
                EVENT_CATEGORY to TOKONOW_TOP_NAV,
                EVENT_LABEL to eventLabel,
                KEY_BUSINESS_UNIT to BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE to CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
        )

        generalSearchEventMutableLiveData.value = generalSearchDataLayer
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
