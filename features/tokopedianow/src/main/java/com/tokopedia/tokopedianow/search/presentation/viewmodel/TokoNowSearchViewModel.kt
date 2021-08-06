package com.tokopedia.tokopedianow.search.presentation.viewmodel

import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_TOKONOW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_PHYSICAL_GOODS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.GENERAL_SEARCH
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Category.TOKONOW_TOP_NAV
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Misc.HASIL_PENCARIAN_DI_TOKONOW
import com.tokopedia.tokopedianow.search.domain.model.SearchCategoryJumperModel.JumperData
import com.tokopedia.tokopedianow.search.domain.model.SearchCategoryJumperModel.SearchCategoryJumperData
import com.tokopedia.tokopedianow.search.domain.model.SearchModel
import com.tokopedia.tokopedianow.search.presentation.model.BroadMatchDataView
import com.tokopedia.tokopedianow.search.presentation.model.BroadMatchItemDataView
import com.tokopedia.tokopedianow.search.presentation.model.CTATokopediaNowHomeDataView
import com.tokopedia.tokopedianow.search.presentation.model.CategoryJumperDataView
import com.tokopedia.tokopedianow.search.presentation.model.SuggestionDataView
import com.tokopedia.tokopedianow.search.presentation.typefactory.SearchTypeFactory
import com.tokopedia.tokopedianow.search.utils.SEARCH_FIRST_PAGE_USE_CASE
import com.tokopedia.tokopedianow.search.utils.SEARCH_LOAD_MORE_PAGE_USE_CASE
import com.tokopedia.tokopedianow.search.utils.SEARCH_QUERY_PARAM_MAP
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel.SearchProductHeader
import com.tokopedia.tokopedianow.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import com.tokopedia.tokopedianow.searchcategory.utils.ABTestPlatformWrapper
import com.tokopedia.tokopedianow.searchcategory.utils.ChooseAddressWrapper
import com.tokopedia.tokopedianow.searchcategory.utils.TOKONOW
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import javax.inject.Inject
import javax.inject.Named

class TokoNowSearchViewModel @Inject constructor (
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
        deleteCartUseCase: DeleteCartUseCase,
        getWarehouseUseCase: GetChosenAddressWarehouseLocUseCase,
        chooseAddressWrapper: ChooseAddressWrapper,
        abTestPlatformWrapper: ABTestPlatformWrapper,
        userSession: UserSessionInterface,
): BaseSearchCategoryViewModel(
        baseDispatcher,
        queryParamMap,
        getFilterUseCase,
        getProductCountUseCase,
        getMiniCartListSimplifiedUseCase,
        addToCartUseCase,
        updateCartUseCase,
        deleteCartUseCase,
        getWarehouseUseCase,
        chooseAddressWrapper,
        abTestPlatformWrapper,
        userSession,
) {

    private val generalSearchEventMutableLiveData = SingleLiveEvent<Map<String, Any>>()
    val generalSearchEventLiveData: LiveData<Map<String, Any>> = generalSearchEventMutableLiveData

    val query = queryParamMap[SearchApiConst.Q] ?: ""

    private var responseCode = ""
    private var suggestionModel: AceSearchProductModel.Suggestion? = null
    private var searchCategoryJumper: SearchCategoryJumperData? = null
    private var related: AceSearchProductModel.Related? = null

    override val tokonowSource: String
        get() = TOKONOW

    override fun loadFirstPage() {
        getSearchFirstPageUseCase.cancelJobs()
        getSearchFirstPageUseCase.execute(
                ::onGetSearchFirstPageSuccess,
                ::onGetFirstPageError,
                createRequestParams()
        )
    }

    private fun onGetSearchFirstPageSuccess(searchModel: SearchModel) {
        responseCode = searchModel.getResponseCode()
        suggestionModel = searchModel.getSuggestion()
        searchCategoryJumper = searchModel.searchCategoryJumper
        related = searchModel.getRelated()

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
        if (!shouldShowSuggestion()) return

        processSuggestionModel { suggestionDataView ->
            val suggestionDataViewIndex = determineSuggestionDataViewIndex(headerList)

            headerList.add(suggestionDataViewIndex, suggestionDataView)
        }
    }

    private fun shouldShowSuggestion() = showSuggestionResponseCodeList.contains(responseCode)

    private fun processSuggestionModel(action: (SuggestionDataView) -> Unit) {
        val suggestionModel = suggestionModel ?: return

        if (suggestionModel.text.isNotEmpty()) {
            val suggestionDataView = createSuggestionDataView(suggestionModel)
            action(suggestionDataView)
        }

        this.suggestionModel = null
    }

    private fun createSuggestionDataView(suggestionModel: AceSearchProductModel.Suggestion) =
        SuggestionDataView(
            text = suggestionModel.text,
            query = suggestionModel.query,
            suggestion = suggestionModel.suggestion,
        )

    private fun determineSuggestionDataViewIndex(headerList: List<Visitable<*>>): Int {
        val quickFilterIndex = headerList.indexOfFirst { it is QuickFilterDataView }

        return quickFilterIndex + 1
    }

    override fun createFooterVisitableList(): List<Visitable<SearchTypeFactory>> {
        val broadMatchVisitableList = createBroadMatchVisitableList()

        return broadMatchVisitableList + listOf(
            createCategoryJumperDataView(),
            CTATokopediaNowHomeDataView(),
        )
    }

    private fun createBroadMatchVisitableList(): List<Visitable<SearchTypeFactory>> {
        val broadMatchVisitableList = mutableListOf<Visitable<SearchTypeFactory>>()

        if (!isShowBroadMatch()) return broadMatchVisitableList

        processSuggestionModel { suggestionDataView ->
            broadMatchVisitableList.add(suggestionDataView)
        }

        processBroadMatch { broadMatchDataView ->
            broadMatchVisitableList.add(broadMatchDataView)
        }

        return broadMatchVisitableList
    }

    private fun isShowBroadMatch() =
        showBroadMatchResponseCodeList.contains(responseCode)

    private fun processBroadMatch(action: (BroadMatchDataView) -> Unit) {
        related?.otherRelatedList?.forEach { otherRelated ->
            val broadMatchDataView = createBroadMatchDataView(otherRelated)
            action(broadMatchDataView)
        }

        related = null
    }

    private fun createBroadMatchDataView(otherRelated: AceSearchProductModel.OtherRelated) =
        BroadMatchDataView(
            keyword = otherRelated.keyword,
            applink = otherRelated.applink,
            broadMatchItemDataViewList = otherRelated.productList
                .mapIndexed { index, otherRelatedProduct ->
                    BroadMatchItemDataView(
                        id = otherRelatedProduct.id,
                        name = otherRelatedProduct.name,
                        price = otherRelatedProduct.price,
                        imageUrl = otherRelatedProduct.imageUrl,
                        applink = otherRelatedProduct.applink,
                        priceString = otherRelatedProduct.priceString,
                        position = index + 1,
                        alternativeKeyword = otherRelated.keyword,
                        ratingAverage = otherRelatedProduct.ratingAverage,
                        labelGroupDataList = otherRelatedProduct.labelGroupList
                            .map(::mapToLabelGroupDataView),
                    )
                },
        )


    private fun createCategoryJumperDataView(): CategoryJumperDataView {
        val categoryJumperItemList =
                searchCategoryJumper
                        ?.getJumperItemList()
                        ?.map(this::mapToCategoryJumperItem)
                        ?: listOf()

        return CategoryJumperDataView(
                title = searchCategoryJumper?.getTitle() ?: "",
                itemList = categoryJumperItemList
        )
    }

    private fun mapToCategoryJumperItem(jumperData: JumperData) =
            CategoryJumperDataView.Item(
                    title = jumperData.title,
                    applink = jumperData.applink,
            )

    override fun createVisitableListWithEmptyProduct() {
        if (isShowBroadMatch())
            createVisitableListWithEmptyProductBroadmatch()
        else
            super.createVisitableListWithEmptyProduct()
    }

    private fun createVisitableListWithEmptyProductBroadmatch() {
        visitableList.add(chooseAddressDataView)
        visitableList.addAll(createBroadMatchVisitableList())
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

    companion object {
        private val showBroadMatchResponseCodeList = listOf("4", "5")
        private val showSuggestionResponseCodeList = listOf("3", "6", "7")
    }
}
