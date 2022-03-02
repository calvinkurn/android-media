package com.tokopedia.tokopedianow.searchcategory.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_OF_PARAMETER_DEVICE
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_OF_PARAMETER_SORT
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_SOURCE_SEARCH
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.PREVIOUS_KEYWORD
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_ADDRESS_ID
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_CITY_ID
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_DISTRICT_ID
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_LAT
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_LONG
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_POST_CODE
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_WAREHOUSE_ID
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet.ApplySortFilterModel
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.helper.isNotFilterAndSortKey
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.filter.newdynamicfilter.helper.FilterHelper
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.home_component.data.DynamicHomeChannelCommon.Channels
import com.tokopedia.home_component.mapper.DynamicChannelComponentMapper
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant.OOC_TOKONOW
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant.PAGE_NUMBER_RECOM_WIDGET
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant.RECOM_WIDGET
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant.TOKONOW_NO_RESULT
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_TOKONOW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_PHYSICAL_GOODS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.constant.ServiceType
import com.tokopedia.tokopedianow.common.domain.model.SetUserPreference
import com.tokopedia.tokopedianow.common.domain.usecase.SetUserPreferenceUseCase
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateOocUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowRecommendationCarouselUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowRepurchaseUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateNoResultUiModel
import com.tokopedia.tokopedianow.home.domain.mapper.HomeRepurchaseMapper
import com.tokopedia.tokopedianow.home.domain.model.GetRepurchaseResponse.RepurchaseData
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.GENERAL_SEARCH
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Category.TOP_NAV
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.LOCAL_SEARCH
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.NONE
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.PAGESOURCE
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.RELATEDKEYWORD
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.TOKOPEDIA_NOW
import com.tokopedia.tokopedianow.searchcategory.cartservice.CartProductItem
import com.tokopedia.tokopedianow.searchcategory.cartservice.CartService
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel.Product
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel.ProductLabelGroup
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel.SearchProduct
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel.SearchProductData
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel.SearchProductHeader
import com.tokopedia.tokopedianow.searchcategory.presentation.model.BannerDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.CategoryFilterDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.CategoryFilterItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ChooseAddressDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.LabelGroupDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.LabelGroupVariantDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.NonVariantATCDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductCountDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProgressBarDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.SortFilterItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.TitleDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.VariantATCDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.SwitcherWidgetDataView
import com.tokopedia.tokopedianow.searchcategory.utils.ABTestPlatformWrapper
import com.tokopedia.tokopedianow.searchcategory.utils.ChooseAddressWrapper
import com.tokopedia.tokopedianow.searchcategory.utils.REPURCHASE_WIDGET_POSITION
import com.tokopedia.tokopedianow.searchcategory.utils.TOKONOW
import com.tokopedia.tokopedianow.searchcategory.utils.TOKONOW_QUERY_PARAMS
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.withContext

abstract class BaseSearchCategoryViewModel(
        protected val baseDispatcher: CoroutineDispatchers,
        queryParamMap: Map<String, String>,
        protected val getFilterUseCase: UseCase<DynamicFilterModel>,
        protected val getProductCountUseCase: UseCase<String>,
        protected val getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase,
        protected val cartService: CartService,
        protected val getShopAndWarehouseUseCase: GetChosenAddressWarehouseLocUseCase,
        protected val getRecommendationUseCase: GetRecommendationUseCase,
        protected val setUserPreferenceUseCase: SetUserPreferenceUseCase,
        protected val chooseAddressWrapper: ChooseAddressWrapper,
        protected val abTestPlatformWrapper: ABTestPlatformWrapper,
        protected val userSession: UserSessionInterface,
): BaseViewModel(baseDispatcher.io) {

    protected val filterController = FilterController()
    protected var chooseAddressDataView = ChooseAddressDataView()
    protected val loadingMoreModel = LoadingMoreModel()
    protected val visitableList = mutableListOf<Visitable<*>>()
    protected val queryParamMutable = queryParamMap.toMutableMap()
    protected var totalData = 0
    protected var totalFetchedData = 0
    protected var nextPage = 1
    protected var chooseAddressData: LocalCacheModel? = null
    protected var currentProductPosition: Int = 1
    protected var recommendationPositionInVisitableList = -1
    protected val recommendationList = mutableListOf<RecommendationWidget>()

    val queryParam: Map<String, String> = queryParamMutable
    val hasGlobalMenu: Boolean
    var warehouseId = ""
        private set
    var autoCompleteApplink = ""
        private set

    protected val visitableListMutableLiveData = MutableLiveData<List<Visitable<*>>>(visitableList)
    val visitableListLiveData: LiveData<List<Visitable<*>>> = visitableListMutableLiveData

    protected val hasNextPageMutableLiveData = MutableLiveData(false)
    val hasNextPageLiveData: LiveData<Boolean> = hasNextPageMutableLiveData

    protected val isFilterPageOpenMutableLiveData = MutableLiveData(false)
    val isFilterPageOpenLiveData: LiveData<Boolean> = isFilterPageOpenMutableLiveData

    protected val dynamicFilterModelMutableLiveData = MutableLiveData<DynamicFilterModel?>(null)
    val dynamicFilterModelLiveData: LiveData<DynamicFilterModel?> = dynamicFilterModelMutableLiveData

    protected val productCountAfterFilterMutableLiveData = MutableLiveData("")
    val productCountAfterFilterLiveData: LiveData<String> = productCountAfterFilterMutableLiveData

    protected val isL3FilterPageOpenMutableLiveData = MutableLiveData<Filter?>(null)
    val isL3FilterPageOpenLiveData: LiveData<Filter?> = isL3FilterPageOpenMutableLiveData

    protected val isShowMiniCartMutableLiveData = MutableLiveData<Boolean?>(null)
    val isShowMiniCartLiveData: LiveData<Boolean?> = isShowMiniCartMutableLiveData

    protected val miniCartWidgetMutableLiveData = MutableLiveData<MiniCartSimplifiedData?>(null)
    val miniCartWidgetLiveData: LiveData<MiniCartSimplifiedData?> = miniCartWidgetMutableLiveData

    protected val updatedVisitableIndicesMutableLiveData = SingleLiveEvent<List<Int>>()
    val updatedVisitableIndicesLiveData: LiveData<List<Int>> =
            updatedVisitableIndicesMutableLiveData

    protected val successATCMessageMutableLiveData = SingleLiveEvent<String>()
    val successATCMessageLiveData: LiveData<String> = successATCMessageMutableLiveData

    protected val errorATCMessageMutableLiveData = SingleLiveEvent<String>()
    val errorATCMessageLiveData: LiveData<String> = errorATCMessageMutableLiveData

    protected val isHeaderBackgroundVisibleMutableLiveData = MutableLiveData(true)
    val isHeaderBackgroundVisibleLiveData: LiveData<Boolean> = isHeaderBackgroundVisibleMutableLiveData

    protected val isContentLoadingMutableLiveData = MutableLiveData(true)
    val isContentLoadingLiveData: LiveData<Boolean> = isContentLoadingMutableLiveData

    protected val shopIdMutableLiveData = MutableLiveData("")
    val shopIdLiveData: LiveData<String> = shopIdMutableLiveData

    protected val addToCartTrackingMutableLiveData =
            SingleLiveEvent<Triple<Int, String, ProductItemDataView>>()
    val addToCartTrackingLiveData: LiveData<Triple<Int, String, ProductItemDataView>> =
            addToCartTrackingMutableLiveData

    protected val decreaseQtyTrackingMutableLiveData = SingleLiveEvent<String>()
    val decreaseQtyTrackingLiveData: LiveData<String> = decreaseQtyTrackingMutableLiveData

    protected val increaseQtyTrackingMutableLiveData = SingleLiveEvent<String>()
    val increaseQtyTrackingLiveData: LiveData<String> = increaseQtyTrackingMutableLiveData

    protected val quickFilterTrackingMutableLiveData = SingleLiveEvent<Pair<Option, Boolean>>()
    val quickFilterTrackingLiveData: LiveData<Pair<Option, Boolean>> = quickFilterTrackingMutableLiveData

    protected val isShowErrorMutableLiveData = SingleLiveEvent<Throwable?>()
    val isShowErrorLiveData: LiveData<Throwable?> = isShowErrorMutableLiveData

    protected val routeApplinkMutableLiveData = SingleLiveEvent<String>()
    val routeApplinkLiveData: LiveData<String> = routeApplinkMutableLiveData

    protected val deleteCartTrackingMutableLiveData = SingleLiveEvent<String>()
    val deleteCartTrackingLiveData: LiveData<String> = deleteCartTrackingMutableLiveData

    protected val addToCartRecommendationTrackingMutableLiveData =
            SingleLiveEvent<Triple<Int, String, RecommendationItem>>()
    val addToCartRecommendationItemTrackingLiveData: LiveData<Triple<Int, String, RecommendationItem>> =
            addToCartRecommendationTrackingMutableLiveData

    protected val generalSearchEventMutableLiveData = SingleLiveEvent<Map<String, Any>>()
    val generalSearchEventLiveData: LiveData<Map<String, Any>> = generalSearchEventMutableLiveData

    protected val addToCartRepurchaseWidgetTrackingMutableLiveData =
        SingleLiveEvent<Triple<Int, String, TokoNowProductCardUiModel>>()

    val addToCartRepurchaseWidgetTrackingLiveData:
        LiveData<Triple<Int, String, TokoNowProductCardUiModel>> =
        addToCartRepurchaseWidgetTrackingMutableLiveData

    protected val oocOpenScreenTrackingMutableEvent = SingleLiveEvent<Boolean>()
    val oocOpenScreenTrackingEvent: LiveData<Boolean> = oocOpenScreenTrackingMutableEvent

    protected val setUserPreferenceMutableLiveData = SingleLiveEvent<Result<SetUserPreference.SetUserPreferenceData>>()
    val setUserPreferenceLiveData: LiveData<Result<SetUserPreference.SetUserPreferenceData>> = setUserPreferenceMutableLiveData

    init {
        updateQueryParams()

        hasGlobalMenu = isABTestNavigationRevamp()
        chooseAddressData = chooseAddressWrapper.getChooseAddressData()
        chooseAddressDataView = ChooseAddressDataView(chooseAddressData)
    }

    private fun showLoading() {
        isContentLoadingMutableLiveData.value = true
    }

    private fun updateQueryParams() {
        queryParamMutable[SearchApiConst.OB] = DEFAULT_VALUE_OF_PARAMETER_SORT
        queryParamMutable[SearchApiConst.NAVSOURCE] = tokonowSource
        queryParamMutable[SearchApiConst.SOURCE] = tokonowSource
    }

    abstract val tokonowSource: String

    private fun isABTestNavigationRevamp() = true

    open fun onViewCreated() {
        val shopId = chooseAddressData?.shop_id ?: ""
        val warehouseId = chooseAddressData?.warehouse_id ?: ""

        if (shopId.isValidId())
            processLoadDataWithShopId(shopId, warehouseId)
        else
            getShopIdBeforeLoadData()
    }

    protected open fun processLoadDataWithShopId(shopId: String, warehouseId: String) {
        this.shopIdMutableLiveData.value = shopId
        this.warehouseId = warehouseId

        if (warehouseId.isValidId())
            processLoadDataInCoverage()
        else
            showOutOfCoverage()
    }

    protected open fun processLoadDataInCoverage() {
        loadFirstPage()
    }

    private fun getShopIdBeforeLoadData() {
        getShopAndWarehouseUseCase.getStateChosenAddress(
                ::onGetShopAndWarehouseSuccess,
                ::onGetShopAndWarehouseFailed,
                DEFAULT_VALUE_SOURCE_SEARCH
        )
    }

    private fun onGetShopAndWarehouseSuccess(state: GetStateChosenAddressResponse) {
        val tokonowData = state.tokonow
        val shopId = tokonowData.shopId.toString()
        val warehouseId = tokonowData.warehouseId.toString()

        processLoadDataWithShopId(shopId, warehouseId)

        refreshMiniCart()
    }

    private fun showOutOfCoverage() {
        updateHeaderBackgroundVisibility(false)
        updateMiniCartVisibility(false)

        sendOutOfCoverageTrackingEvent()
        createVisitableListWithOutOfCoverageView()
        clearVisitableListLiveData()
        updateVisitableListLiveData()
        showPageContent()
    }

    protected fun sendOutOfCoverageTrackingEvent() {
        oocOpenScreenTrackingMutableEvent.value = true
    }

    private fun createVisitableListWithOutOfCoverageView() {
        visitableList.clear()
        visitableList.add(chooseAddressDataView)
        visitableList.add(TokoNowEmptyStateOocUiModel(
            hostSource = DEFAULT_VALUE_SOURCE_SEARCH,
            serviceType = chooseAddressData?.service_type.orEmpty()
        ))
        visitableList.add(TokoNowRecommendationCarouselUiModel(pageName = OOC_TOKONOW))
    }

    protected open fun onGetShopAndWarehouseFailed(throwable: Throwable) {

    }

    protected abstract fun loadFirstPage()

    protected open fun createRequestParams(): RequestParams {
        val tokonowQueryParam = createTokonowQueryParams()

        val requestParams = RequestParams.create()
        requestParams.putObject(TOKONOW_QUERY_PARAMS, tokonowQueryParam)

        return requestParams
    }

    protected open fun createTokonowQueryParams(): MutableMap<String, Any> {
        val tokonowQueryParam = mutableMapOf<String, Any>()

        appendMandatoryParams(tokonowQueryParam)
        appendPaginationParam(tokonowQueryParam)
        appendQueryParam(tokonowQueryParam)

        return tokonowQueryParam
    }

    protected open fun appendMandatoryParams(tokonowQueryParam: MutableMap<String, Any>) {
        appendDeviceParam(tokonowQueryParam)
        appendChooseAddressParams(tokonowQueryParam)
        appendUniqueIdParam(tokonowQueryParam)
    }

    private fun appendDeviceParam(tokonowQueryParam: MutableMap<String, Any>) {
        tokonowQueryParam[SearchApiConst.DEVICE] = DEFAULT_VALUE_OF_PARAMETER_DEVICE
    }

    private fun appendChooseAddressParams(tokonowQueryParam: MutableMap<String, Any>) {
        val chooseAddressData = this.chooseAddressData ?: return

        if (chooseAddressData.city_id.isNotEmpty())
            tokonowQueryParam[USER_CITY_ID] = chooseAddressData.city_id
        if (chooseAddressData.address_id.isNotEmpty())
            tokonowQueryParam[USER_ADDRESS_ID] = chooseAddressData.address_id
        if (chooseAddressData.district_id.isNotEmpty())
            tokonowQueryParam[USER_DISTRICT_ID] = chooseAddressData.district_id
        if (chooseAddressData.lat.isNotEmpty())
            tokonowQueryParam[USER_LAT] = chooseAddressData.lat
        if (chooseAddressData.long.isNotEmpty())
            tokonowQueryParam[USER_LONG] = chooseAddressData.long
        if (chooseAddressData.postal_code.isNotEmpty())
            tokonowQueryParam[USER_POST_CODE] = chooseAddressData.postal_code
        if (chooseAddressData.warehouse_id.isNotEmpty())
            tokonowQueryParam[USER_WAREHOUSE_ID] = chooseAddressData.warehouse_id
    }

    protected open fun appendUniqueIdParam(tokonowQueryParam: MutableMap<String, Any>) {
        tokonowQueryParam[SearchApiConst.UNIQUE_ID] = getUniqueId()
    }

    protected open fun appendPaginationParam(tokonowQueryParam: MutableMap<String, Any>) {
        tokonowQueryParam[SearchApiConst.PAGE] = nextPage
        tokonowQueryParam[SearchApiConst.USE_PAGE] = true
    }

    private fun appendQueryParam(tokonowQueryParam: MutableMap<String, Any>) {
        tokonowQueryParam.putAll(FilterHelper.createParamsWithoutExcludes(queryParam))
    }

    protected fun onGetFirstPageSuccess(
            headerDataView: HeaderDataView,
            contentDataView: ContentDataView,
            searchProduct: SearchProduct,
    ) {
        totalData = headerDataView.aceSearchProductHeader.totalData
        totalFetchedData += contentDataView.aceSearchProductData.productList.size
        autoCompleteApplink = contentDataView.aceSearchProductData.autocompleteApplink
        currentProductPosition = 1

        val isEmptyProductList = contentDataView.aceSearchProductData.productList.isEmpty()

        initFilterController(headerDataView)
        createVisitableListFirstPage(headerDataView, contentDataView, isEmptyProductList)
        processEmptyState(isEmptyProductList)
        if (getKeywordForGeneralSearchTracking().isNotEmpty()) {
            sendGeneralSearchTracking(searchProduct)
        }
        updateViewForFirstPage(isEmptyProductList)
    }

    private fun initFilterController(headerDataView: HeaderDataView) {
        val filterList =
                headerDataView.quickFilterDataValue.filter +
                        headerDataView.categoryFilterDataValue.filter

        filterController.initFilterController(queryParamMutable, filterList)
    }

    private fun createVisitableListFirstPage(
            headerDataView: HeaderDataView,
            contentDataView: ContentDataView,
            isEmptyProductList: Boolean,
    ) {
        visitableList.clear()

        if (isEmptyProductList)
            createVisitableListWithEmptyProduct()
        else
            createVisitableListWithProduct(headerDataView, contentDataView)
    }

    protected open fun createVisitableListWithEmptyProduct() {
        val activeFilterList = filterController.getActiveFilterOptionList()

        visitableList.add(chooseAddressDataView)
        visitableList.add(TokoNowEmptyStateNoResultUiModel(activeFilterList = activeFilterList))
        visitableList.add(
            TokoNowRecommendationCarouselUiModel(
                pageName = TOKONOW_NO_RESULT,
                keywords = getKeywordForGeneralSearchTracking()
            )
        )
    }

    private fun createVisitableListWithProduct(
            headerDataView: HeaderDataView,
            contentDataView: ContentDataView,
    ) {
        visitableList.addAll(createHeaderVisitableList(headerDataView))
        visitableList.addAll(createContentVisitableList(contentDataView))
        visitableList.addFooter()
    }

    protected open fun createHeaderVisitableList(headerDataView: HeaderDataView): List<Visitable<*>> {
        val headerList = mutableListOf<Visitable<*>>()

        headerList.add(chooseAddressDataView)
        headerList.add(createBannerDataView(headerDataView))
        headerList.add(createTitleDataView(headerDataView))

        processCategoryFilter(headerList, headerDataView.categoryFilterDataValue)

        headerList.add(QuickFilterDataView(createQuickFilterItemList(headerDataView), queryParam))
        headerList.add(ProductCountDataView(headerDataView.aceSearchProductHeader.totalDataText))

        postProcessHeaderList(headerList)

        return headerList
    }

    protected abstract fun createTitleDataView(headerDataView: HeaderDataView): TitleDataView

    protected open fun processCategoryFilter(
            headerList: MutableList<Visitable<*>>,
            categoryFilterDataValue: DataValue,
    ) {
        val categoryFilter = categoryFilterDataValue.filter.getOrNull(0)
        categoryFilter ?: return

        if (isShowCategoryFilter(categoryFilter))
            headerList.add(CategoryFilterDataView(createCategoryFilterItemList(categoryFilter)))
    }

    protected open fun isShowCategoryFilter(categoryFilter: Filter) =
            categoryFilter.options.size > 1

    protected fun createBannerDataView(headerDataView: HeaderDataView): BannerDataView {
        val channel = headerDataView.bannerChannel
        val position = 1
        val channelModel = DynamicChannelComponentMapper.mapChannelToComponent(channel, position)

        return BannerDataView(channelModel)
    }

    protected fun createCategoryFilterItemList(categoryFilter: Filter) =
            categoryFilter.options.map {
                CategoryFilterItemDataView(it, filterController.getFilterViewState(it))
            }

    protected fun createQuickFilterItemList(headerDataView: HeaderDataView) =
            headerDataView.quickFilterDataValue.filter.map {
                SortFilterItemDataView(
                        filter = it,
                        sortFilterItem = createSortFilterItem(it),
                )
            }

    private fun createSortFilterItem(filter: Filter): SortFilterItem {
        val isSelected = getQuickFilterIsSelected(filter)
        val chipType = getSortFilterItemType(isSelected)

        val sortFilterItem = SortFilterItem(filter.title, chipType)
        sortFilterItem.typeUpdated = false

        if (filter.options.size == 1) {
            val option = filter.options.firstOrNull() ?: Option()
            sortFilterItem.listener = {
                sendQuickFilterTrackingEvent(option, isSelected)
                filter(option, !isSelected)
            }
        }
        else {
            val listener = {
                openL3FilterPage(filter)
            }
            sortFilterItem.chevronListener = listener
            sortFilterItem.listener = listener
        }

        return sortFilterItem
    }

    private fun getQuickFilterIsSelected(filter: Filter) =
            filter.options.any {
                if (it.key.contains(OptionHelper.EXCLUDE_PREFIX)) false
                else filterController.getFilterViewState(it)
            }

    private fun getSortFilterItemType(isSelected: Boolean) =
            if (isSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL

    private fun sendQuickFilterTrackingEvent(option: Option, isSelected: Boolean) {
        quickFilterTrackingMutableLiveData.value = Pair(option, !isSelected)
    }

    private fun filter(option: Option, isApplied: Boolean) {
        filterController.setFilter(
                option = option,
                isFilterApplied = isApplied,
                isCleanUpExistingFilterWithSameKey = option.isCategoryOption,
        )

        refreshQueryParamFromFilterController()

        applyFilter()
    }

    private fun refreshQueryParamFromFilterController() {
        queryParamMutable.clear()
        queryParamMutable.putAll(filterController.getParameter())
    }

    open fun onViewReloadPage() {
        totalData = 0
        totalFetchedData = 0
        nextPage = 1
        chooseAddressData = chooseAddressWrapper.getChooseAddressData()
        chooseAddressDataView = ChooseAddressDataView(chooseAddressData)
        dynamicFilterModelMutableLiveData.value = null

        showLoading()
        onViewCreated()
    }

    private fun applyFilter() {
        totalData = 0
        totalFetchedData = 0
        nextPage = 1
        chooseAddressData = chooseAddressWrapper.getChooseAddressData()
        chooseAddressDataView = ChooseAddressDataView(chooseAddressData)
        dynamicFilterModelMutableLiveData.value = null

        showProgressBar()
        onViewCreated()
    }

    private fun showProgressBar() {
        addProgressBarDataView()
        updateVisitableListLiveData()
    }

    private fun addProgressBarDataView() {
        visitableList.firstOrNull { it is ProductCountDataView }?.let {
            val index = visitableList.indexOf(it)
            visitableList.add(index, ProgressBarDataView)
        }
    }

    protected open fun openL3FilterPage(selectedFilter: Filter) {
        if (isL3FilterPageOpenMutableLiveData.value != null) return

        isL3FilterPageOpenMutableLiveData.value = selectedFilter
    }

    protected open fun postProcessHeaderList(headerList: MutableList<Visitable<*>>) {

    }

    protected open fun createContentVisitableList(
        contentDataView: ContentDataView,
    ): List<Visitable<*>> {
        val contentVisitableList = mutableListOf<Visitable<*>>()

        val productList = contentDataView
            .aceSearchProductData
            .productList

        addProductList(contentVisitableList, productList)

        val repurchaseWidget = contentDataView.repurchaseWidget
        addRepurchaseWidget(contentVisitableList, repurchaseWidget, productList)

        return contentVisitableList
    }

    protected open fun addProductList(
        contentVisitableList: MutableList<Visitable<*>>,
        productList: List<Product>,
    ) {
        val productListDataView = productList.mapIndexed(::mapToProductItemDataView)

        contentVisitableList.addAll(productListDataView)
    }

    protected open fun mapToProductItemDataView(index: Int, product: Product): ProductItemDataView {
        return ProductItemDataView(
                id = product.id,
                imageUrl300 = product.imageUrl300,
                name = product.name,
                price = product.price,
                priceInt = product.priceInt,
                discountPercentage = product.discountPercentage,
                originalPrice = product.originalPrice,
                parentId = product.parentId,
                shop = ProductItemDataView.Shop(
                        id = product.shop.id,
                        name = product.shop.name,
                ),
                ratingAverage = product.ratingAverage,
                variantATC = createVariantATCDataView(product),
                nonVariantATC = createNonVariantATCDataView(product),
                labelGroupDataViewList = product.labelGroupList.map(::mapToLabelGroupDataView),
                labelGroupVariantDataViewList = product.labelGroupVariantList.map { labelGroupVariant ->
                    LabelGroupVariantDataView(
                            title = labelGroupVariant.title,
                            type = labelGroupVariant.type,
                            typeVariant = labelGroupVariant.typeVariant,
                            hexColor = labelGroupVariant.hexColor,
                    )
                },
                sourceEngine = product.sourceEngine,
                boosterList = product.boosterList,
                position = currentProductPosition++,
        )
    }

    protected open fun createVariantATCDataView(product: Product) =
            if (product.childs.isNotEmpty())
                VariantATCDataView(
                        quantity = cartService.getProductQuantity(product.id, product.parentId)
                )
            else null

    protected open fun createNonVariantATCDataView(product: Product) =
            if (product.childs.isEmpty())
                NonVariantATCDataView(
                        minQuantity = product.minOrder,
                        maxQuantity = product.maxOrder,
                        quantity = cartService.getProductQuantity(product.id, product.parentId)
                )
            else null

    protected open fun mapToLabelGroupDataView(labelGroup: ProductLabelGroup) =
        LabelGroupDataView(
            url = labelGroup.url,
            title = labelGroup.title,
            position = labelGroup.position,
            type = labelGroup.type,
        )

    protected open fun addRepurchaseWidget(
        contentVisitableList: MutableList<Visitable<*>>,
        repurchaseWidget: RepurchaseData,
        productList: List<Product>,
    ) {
        val canShowRepurchaseWidget =
            repurchaseWidget.products.isNotEmpty()
                && productList.size > REPURCHASE_WIDGET_POSITION

        if (canShowRepurchaseWidget)
            contentVisitableList.add(
                REPURCHASE_WIDGET_POSITION,
                createRepurchaseWidgetUIModel(repurchaseWidget)
            )
    }

    private fun createRepurchaseWidgetUIModel(repurchaseWidget: RepurchaseData) =
        HomeRepurchaseMapper.mapToRepurchaseUiModel(
            TokoNowRepurchaseUiModel(
                id = "",
                title = "",
                productList = listOf(),
                state = -1,
            ),
            repurchaseWidget,
        ).also {
            updateRepurchaseWidgetQuantity(it)
        }

    private fun updateRepurchaseWidgetQuantity(
        repurchaseUiModel: TokoNowRepurchaseUiModel,
        index: Int = -1,
        updatedProductIndices: MutableList<Int>? = null,
    ) {
        repurchaseUiModel.productList.forEach { productUiModel ->
            productUiModel.product = createUpdatedRepurchaseWidgetQuantity(productUiModel)
        }

        updatedProductIndices?.add(index)
    }

    private fun createUpdatedRepurchaseWidgetQuantity(
        repurchaseProduct: TokoNowProductCardUiModel,
    ): ProductCardModel {
        val quantity = cartService.getProductQuantity(
            repurchaseProduct.productId,
            repurchaseProduct.parentId,
        )

        val nonVariant = repurchaseProduct.product.nonVariant?.copy(quantity = quantity)
        val variant = repurchaseProduct.product.variant?.copy(quantity = quantity)

        return repurchaseProduct.product.copy(
            nonVariant = nonVariant,
            variant = variant,
        )
    }

    private fun MutableList<Visitable<*>>.addFooter() {
        if (isLastPage()) {
            // show switcher only if service type is 15m
            if (chooseAddressData?.service_type == ServiceType.NOW_15M) {
                add(SwitcherWidgetDataView())
            }
            addAll(createFooterVisitableList())
        } else {
            add(loadingMoreModel)
        }
    }

    protected open fun isLastPage() = totalFetchedData >= totalData

    protected open fun createFooterVisitableList() = listOf<Visitable<*>>()

    protected open fun processEmptyState(isEmptyProductList: Boolean) {

    }

    protected open fun sendGeneralSearchTracking(searchProduct: SearchProduct) {
        val searchProductHeader = searchProduct.header
        val eventLabel = getKeywordForGeneralSearchTracking() +
            "|${searchProductHeader.keywordProcess}" +
            "|${searchProductHeader.responseCode}" +
            "|$BUSINESS_UNIT_PHYSICAL_GOODS" +
            "|$TOKONOW" +
            "|$TOKOPEDIA_NOW" +
            "|${searchProductHeader.totalData}"
        val previousKeyword = getPreviousKeywordForGeneralSearchTracking()
        val pageSource = getPageSourceForGeneralSearchTracking()

        val generalSearchDataLayer = mapOf(
            EVENT to EVENT_CLICK_TOKONOW,
            EVENT_ACTION to GENERAL_SEARCH,
            EVENT_CATEGORY to TOP_NAV,
            EVENT_LABEL to eventLabel,
            KEY_BUSINESS_UNIT to BUSINESS_UNIT_PHYSICAL_GOODS,
            KEY_CURRENT_SITE to CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
            RELATEDKEYWORD to "$previousKeyword - ${searchProduct.getAlternativeKeyword()}",
            PAGESOURCE to pageSource,
        )

        generalSearchEventMutableLiveData.value = generalSearchDataLayer
    }

    protected open fun getKeywordForGeneralSearchTracking() = ""

    private fun getPreviousKeywordForGeneralSearchTracking(): String {
        val previousKeyword = queryParam[PREVIOUS_KEYWORD] ?: ""

        return if (previousKeyword.isBlank()) NONE else previousKeyword
    }

    private fun getPageSourceForGeneralSearchTracking(): String {
        return "$TOKOPEDIA_NOW.$TOKONOW.$LOCAL_SEARCH.$warehouseId"
    }

    private fun updateViewForFirstPage(isEmptyProductList: Boolean) {
        updateVisitableListLiveData()

        updateNextPageData()
        updateHeaderBackgroundVisibility(!isEmptyProductList)

        showPageContent()
    }

    protected fun clearVisitableListLiveData() {
        visitableListMutableLiveData.value = listOf()
    }

    protected fun updateVisitableListLiveData() {
        visitableListMutableLiveData.value = visitableList
    }

    protected open suspend fun suspendUpdateVisitableListLiveData() {
        withContext(baseDispatcher.main) {
            updateVisitableListLiveData()
        }
    }

    protected open fun updateNextPageData() {
        val hasNextPage = totalData > totalFetchedData

        hasNextPageMutableLiveData.value = hasNextPage

        if (hasNextPage) nextPage++
    }

    private fun updateHeaderBackgroundVisibility(isVisible: Boolean) {
        isHeaderBackgroundVisibleMutableLiveData.value = isVisible
    }

    private fun showPageContent() {
        isContentLoadingMutableLiveData.value = false
    }

    protected open fun onGetFirstPageError(throwable: Throwable) {
        isShowErrorMutableLiveData.value = throwable
    }

    open fun onLoadMore() {
        if (hasLoadedAllData()) return

        executeLoadMore()
    }

    protected open fun hasLoadedAllData() = totalData <= totalFetchedData

    abstract fun executeLoadMore()

    protected open fun onGetLoadMorePageSuccess(contentDataView: ContentDataView) {
        totalFetchedData += contentDataView.aceSearchProductData.productList.size

        updateVisitableListForNextPage(contentDataView)
        updateVisitableListLiveData()
        updateNextPageData()
    }

    protected open fun updateVisitableListForNextPage(contentDataView: ContentDataView) {
        visitableList.remove(loadingMoreModel)
        visitableList.addAll(createContentVisitableList(contentDataView))
        visitableList.addFooter()
    }

    open fun onViewOpenFilterPage() {
        if (isFilterPageOpenLiveData.value == true) return

        isFilterPageOpenMutableLiveData.value = true

        if (dynamicFilterModelLiveData.value != null) return

        val getFilterRequestParams = RequestParams.create()
        getFilterRequestParams.putAll(createTokonowQueryParams())

        getFilterUseCase.cancelJobs()
        getFilterUseCase.execute(
                ::onGetFilterSuccess,
                ::onGetFilterFailed,
                getFilterRequestParams
        )
    }

    protected open fun onGetFilterSuccess(dynamicFilterModel: DynamicFilterModel) {
        filterController.appendFilterList(queryParam, dynamicFilterModel.data.filter)
        dynamicFilterModelMutableLiveData.value = dynamicFilterModel
    }

    protected open fun onGetFilterFailed(throwable: Throwable) {

    }

    open fun onViewDismissFilterPage() {
        isFilterPageOpenMutableLiveData.value = false
    }

    open fun onViewClickCategoryFilterChip(option: Option, isSelected: Boolean) {
        resetSortFilterIfExclude(option)
        filter(option, isSelected)
    }

    private fun resetSortFilterIfExclude(option: Option) {
        val isOptionKeyHasExclude = option.key.startsWith(OptionHelper.EXCLUDE_PREFIX)

        if (!isOptionKeyHasExclude) return

        queryParamMutable.remove(option.key)
        queryParamMutable.entries.retainAll { it.isNotFilterAndSortKey() }
        queryParamMutable[SearchApiConst.OB] = DEFAULT_VALUE_OF_PARAMETER_SORT
        filterController.refreshMapParameter(queryParam)
    }

    open fun onViewApplySortFilter(applySortFilterModel: ApplySortFilterModel) {
        filterController.refreshMapParameter(applySortFilterModel.mapParameter)
        refreshQueryParamFromFilterController()

        onViewReloadPage()
    }

    open fun onViewGetProductCount(mapParameter: Map<String, String>) {
        getProductCountUseCase.cancelJobs()
        getProductCountUseCase.execute(
                ::onGetProductCountSuccess,
                ::onGetProductCountFailed,
                createGetProductCountRequestParams(mapParameter)
        )
    }

    protected open fun createGetProductCountRequestParams(
            mapParameter: Map<String, String>
    ): RequestParams {
        val getProductCountParams = mutableMapOf<String, Any>()
        appendMandatoryParams(getProductCountParams)
        getProductCountParams[SearchApiConst.ROWS] = 0
        getProductCountParams.putAll(FilterHelper.createParamsWithoutExcludes(mapParameter))

        val getProductCountRequestParams = RequestParams.create()
        getProductCountRequestParams.putAll(getProductCountParams)

        return getProductCountRequestParams
    }

    protected open fun onGetProductCountSuccess(countText: String) {
        productCountAfterFilterMutableLiveData.value = countText
    }

    protected open fun onGetProductCountFailed(throwable: Throwable) {
        onGetProductCountSuccess("0")
    }

    open fun onViewGetProductCount(option: Option) {
        val queryParamWithoutOption = queryParam.toMutableMap().apply { removeOption(option) }
        val mapParameter = queryParamWithoutOption + mapOf(option.key to option.value)
        onViewGetProductCount(mapParameter)
    }

    private fun MutableMap<String, String>.removeOption(option: Option) {
        remove(option.key)
        remove(OptionHelper.getKeyRemoveExclude(option))
    }

    open fun onViewApplyFilterFromCategoryChooser(chosenCategoryFilter: Option) {
        onViewDismissL3FilterPage()
        removeAllCategoryFilter(chosenCategoryFilter)
        filter(chosenCategoryFilter, true)
    }

    private fun removeAllCategoryFilter(chosenCategoryFilter: Option) {
        queryParamMutable.removeOption(chosenCategoryFilter)

        filterController.refreshMapParameter(queryParam)
    }

    open fun onViewDismissL3FilterPage() {
        isL3FilterPageOpenMutableLiveData.value = null
    }

    open fun onViewResumed() {
        refreshMiniCart()

        val isChooseAddressUpdated = getIsChooseAddressUpdated()
        if (isChooseAddressUpdated)
            onViewReloadPage()
    }

    open fun refreshMiniCart() {
        val shopId = shopIdLiveData.value ?: ""
        if (!shopId.isValidId()) return

        getMiniCartListSimplifiedUseCase.cancelJobs()
        getMiniCartListSimplifiedUseCase.setParams(listOf(shopId))
        getMiniCartListSimplifiedUseCase.execute(
            ::onViewUpdateCartItems,
            ::onGetMiniCartDataFailed,
        )
    }

    private fun String.isValidId() = this.isNotEmpty() && this != "0"

    open fun onViewUpdateCartItems(miniCartSimplifiedData: MiniCartSimplifiedData) {
        updateMiniCartWidgetData(miniCartSimplifiedData)

        viewModelScope.launchCatchError(
            block = { updateMiniCartInBackground(miniCartSimplifiedData) },
            onError = { }
        )
    }

    private fun updateMiniCartWidgetData(miniCartSimplifiedData: MiniCartSimplifiedData) {
        chooseAddressData?.let {
            val outOfCoverage = it.isOutOfCoverage()
            val showMiniCart = miniCartSimplifiedData.isShowMiniCartWidget
            miniCartWidgetMutableLiveData.value = miniCartSimplifiedData
            isShowMiniCartMutableLiveData.value = showMiniCart && !outOfCoverage
        }
    }

    private suspend fun updateMiniCartInBackground(
            miniCartSimplifiedData: MiniCartSimplifiedData
    ) {
        withContext(baseDispatcher.io) {
            cartService.updateMiniCartItems(miniCartSimplifiedData)

            if (visitableList.isEmpty()) return@withContext

            val updatedProductIndices = mutableListOf<Int>()

            visitableList.forEachIndexed { index, visitable ->
                updateQuantityInVisitable(visitable, index, updatedProductIndices)
            }

            updateRecommendationListQuantity(recommendationList)
            if (recommendationPositionInVisitableList != -1)
                updatedProductIndices.add(recommendationPositionInVisitableList)

            updateVisitableWithIndex(updatedProductIndices)
        }
    }

    protected open fun updateQuantityInVisitable(
        visitable: Visitable<*>,
        index: Int,
        updatedProductIndices: MutableList<Int>,
    ) {
        when (visitable) {
            is ProductItemDataView ->
                updateProductItemQuantity(index, visitable, updatedProductIndices)
            is TokoNowRepurchaseUiModel ->
                updateRepurchaseWidgetQuantity(visitable, index, updatedProductIndices)
        }
    }

    private fun updateProductItemQuantity(
            index: Int,
            productItem: ProductItemDataView,
            updatedProductIndices: MutableList<Int>,
    ) {
        val productId = productItem.id
        val parentProductId = productItem.parentId
        val nonVariantATC = productItem.nonVariantATC
        val variantATC = productItem.variantATC

        val quantity = cartService.getProductQuantity(productId, parentProductId)

        if (nonVariantATC != null && nonVariantATC.quantity != quantity) {
            nonVariantATC.quantity = quantity
            updatedProductIndices.add(index)
        } else if (variantATC != null && variantATC.quantity != quantity) {
            variantATC.quantity = quantity
            updatedProductIndices.add(index)
        }
    }

    private suspend fun updateVisitableWithIndex(updatedProductIndices: List<Int>) {
        withContext(baseDispatcher.main) {
            updatedVisitableIndicesMutableLiveData.value = updatedProductIndices
        }
    }

    private fun onGetMiniCartDataFailed(throwable: Throwable) {
        updateMiniCartVisibility(false)
    }

    private fun updateMiniCartVisibility(isVisible: Boolean) {
        isShowMiniCartMutableLiveData.value = isVisible
    }

    private fun getIsChooseAddressUpdated(): Boolean {
        return chooseAddressData?.let {
            chooseAddressWrapper.isChooseAddressUpdated(it)
        } ?: false
    }

    open fun onViewATCProductNonVariant(productItem: ProductItemDataView, quantity: Int) {
        val nonVariantATC = productItem.nonVariantATC ?: return

        val productId = productItem.id
        val shopId = productItem.shop.id
        val currentQuantity = nonVariantATC.quantity

        cartService.handleCart(
            cartProductItem = CartProductItem(productId, shopId, currentQuantity),
            quantity = quantity,
            onSuccessAddToCart = {
                sendAddToCartTracking(quantity, it.data.cartId, productItem)
                onAddToCartSuccess(productItem, it.data.quantity)
                updateCartMessageSuccess(it.errorMessage.joinToString(separator = ", "))
            },
            onSuccessUpdateCart = {
                sendTrackingUpdateQuantity(quantity, productItem)
                onAddToCartSuccess(productItem, quantity)
            },
            onSuccessDeleteCart = {
                sendDeleteCartTracking(productItem)
                onAddToCartSuccess(productItem, 0)
                updateCartMessageSuccess(it.errorMessage.joinToString(separator = ", "))
            },
            onError = ::onAddToCartFailed,
            handleCartEventNonLogin = {
                handleAddToCartEventNonLogin(visitableList.indexOf(productItem))
            },
        )
    }

    private fun sendAddToCartTracking(quantity: Int, cartId: String, productItem: ProductItemDataView) {
        addToCartTrackingMutableLiveData.value = Triple(quantity, cartId, productItem)
    }

    protected fun updateCartMessageSuccess(successMessage: String) {
        successATCMessageMutableLiveData.value = successMessage
    }

    private fun onAddToCartSuccess(productItem: ProductItemDataView, quantity: Int) {
        updateProductNonVariantQuantity(productItem, quantity)
        refreshMiniCart()
    }

    private fun updateProductNonVariantQuantity(
            productItem: ProductItemDataView,
            quantity: Int,
    ) {
        productItem.nonVariantATC?.quantity = quantity
    }

    protected fun onAddToCartFailed(throwable: Throwable) {
        errorATCMessageMutableLiveData.value = throwable.message ?: ""
    }

    private fun sendTrackingUpdateQuantity(newQuantity: Int, productItem: ProductItemDataView) {
        val nonVariantATC = productItem.nonVariantATC ?: return

        if (nonVariantATC.quantity > newQuantity)
            decreaseQtyTrackingMutableLiveData.value = productItem.id
        else if (nonVariantATC.quantity < newQuantity)
            increaseQtyTrackingMutableLiveData.value = productItem.id
    }

    private fun sendDeleteCartTracking(productItem: ProductItemDataView) {
        deleteCartTrackingMutableLiveData.value = productItem.id
    }

    protected open fun handleAddToCartEventNonLogin(updatedVisitableIndex: Int) {
        routeApplinkMutableLiveData.value = ApplinkConst.LOGIN
        updatedVisitableIndicesMutableLiveData.value = listOf(updatedVisitableIndex)
    }

    fun onLocalizingAddressSelected() {
        refreshMiniCart()
        onViewReloadPage()
    }

    fun onViewRemoveFilter(option: Option) {
        resetSortFilterIfExclude(option)
        filter(option, false)
    }

    open fun setUserPreference(serviceType: String) {
        launchCatchError(
            block = {
                chooseAddressData?.let {
                    val setUserPreference = setUserPreferenceUseCase.execute(it, serviceType)
                    setUserPreferenceMutableLiveData.postValue(Success(setUserPreference))
                }
            },
            onError = {
                setUserPreferenceMutableLiveData.postValue(Fail(it))
            },
        )
    }

    open fun switchService() {
        chooseAddressData?.let {
            val currentServiceType = it.service_type

            val serviceType = if (
                currentServiceType == ServiceType.NOW_15M ||
                currentServiceType == ServiceType.NOW_OOC
            ) {
                ServiceType.NOW_2H
            } else {
                ServiceType.NOW_15M
            }

            setUserPreference(serviceType)
        }
    }

    open fun onBindRecommendationCarousel(
            element: TokoNowRecommendationCarouselUiModel,
            adapterPosition: Int,
    ) {
        launchCatchError(
                block = { getRecommendationCarousel(element, adapterPosition) },
                onError = { getRecommendationCarouselError(element, adapterPosition) },
        )
    }

    protected open suspend fun getRecommendationCarousel(
            element: TokoNowRecommendationCarouselUiModel,
            adapterPosition: Int,
    ) {
        if (element.carouselData.state == RecommendationCarouselData.STATE_READY) return

        recommendationPositionInVisitableList = adapterPosition

        val getRecommendationRequestParam = createRecommendationRequestParam(element)
        val recommendationListData =
                getRecommendationUseCase.getData(getRecommendationRequestParam)

        updateRecommendationList(recommendationListData)
        updateVisitableListForRecommendationCarousel(element, adapterPosition)
    }

    protected open fun createRecommendationRequestParam(
            recommendationCarouselDataView: TokoNowRecommendationCarouselUiModel
    ) = GetRecommendationRequestParam(
            pageName = recommendationCarouselDataView.pageName,
            categoryIds = getRecomCategoryId(recommendationCarouselDataView),
            xSource = RECOM_WIDGET,
            isTokonow = true,
            pageNumber = PAGE_NUMBER_RECOM_WIDGET,
            keywords = getRecomKeywords(),
            xDevice = DEFAULT_VALUE_OF_PARAMETER_DEVICE,
    )

    protected open fun getRecomCategoryId(
            recommendationCarouselDataView: TokoNowRecommendationCarouselUiModel
    ) = listOf<String>()

    protected open fun getRecomKeywords() = listOf<String>()

    private fun updateRecommendationList(recommendationListData: List<RecommendationWidget>) {
        recommendationList.clear()
        recommendationList.addAll(recommendationListData)

        updateRecommendationListQuantity(recommendationList)
    }

    private fun updateRecommendationListQuantity(recommendationList: List<RecommendationWidget>?) {
        recommendationList
                ?.flatMap(RecommendationWidget::recommendationItemList)
                ?.forEach(::setRecommendationItemQuantity)
    }

    private fun setRecommendationItemQuantity(recommendationItem: RecommendationItem) {
        val productId = recommendationItem.productId.toString()
        val parentProductId = recommendationItem.parentID.toString()
        val quantity = cartService.getProductQuantity(productId, parentProductId)

        recommendationItem.quantity = quantity
    }

    protected open suspend fun updateVisitableListForRecommendationCarousel(
        element: TokoNowRecommendationCarouselUiModel,
        adapterPosition: Int,
    ) {
        val recommendationData = recommendationList.firstOrNull() ?: RecommendationWidget()

        if (recommendationData.recommendationItemList.isEmpty()) {
            visitableList.remove(element)
            suspendUpdateVisitableListLiveData()
        } else {
            element.carouselData = RecommendationCarouselData(
                state = RecommendationCarouselData.STATE_READY,
                recommendationData = recommendationData
            )

            updateVisitableWithIndex(listOf(adapterPosition))
        }
    }

    protected open suspend fun getRecommendationCarouselError(
        element: TokoNowRecommendationCarouselUiModel,
        adapterPosition: Int,
    ) {
        element.carouselData = RecommendationCarouselData(
                state = RecommendationCarouselData.STATE_FAILED
        )

        updateVisitableWithIndex(listOf(adapterPosition))
    }

    open fun onViewATCRecommendationItemNonVariant(
            recommendationItem: RecommendationItem,
            adapterPosition: Int,
            quantity: Int,
    ) {
        val productId = recommendationItem.productId.toString()
        val shopId = recommendationItem.shopId.toString()
        val currentQuantity = recommendationItem.quantity

        cartService.handleCart(
            cartProductItem = CartProductItem(productId, shopId, currentQuantity),
            quantity = quantity,
            onSuccessAddToCart = {
                addToCartRecommendationTrackingMutableLiveData.value = Triple(
                    quantity,
                    it.data.cartId,
                    recommendationItem,
                )
                updateCartMessageSuccess(it.errorMessage.joinToString(separator = ", "))
                onAddToCartSuccessRecommendationItem(recommendationItem, it.data.quantity)
            },
            onSuccessUpdateCart = {
                onAddToCartSuccessRecommendationItem(recommendationItem, quantity)
            },
            onSuccessDeleteCart = {
                updateCartMessageSuccess(it.errorMessage.joinToString(separator = ", "))
                onAddToCartSuccessRecommendationItem(recommendationItem, 0)
            },
            onError = ::onAddToCartFailed,
            handleCartEventNonLogin = {
                handleAddToCartEventNonLogin(adapterPosition)
            },
        )
    }

    private fun onAddToCartSuccessRecommendationItem(
        recommendationItem: RecommendationItem,
        quantity: Int,
    ) {
        recommendationItem.quantity = quantity
        refreshMiniCart()
    }

    open fun onViewATCRepurchaseWidget(
        repurchaseProduct: TokoNowProductCardUiModel,
        quantity: Int,
    ) {
        val nonVariant = repurchaseProduct.product.nonVariant ?: return
        val productId = repurchaseProduct.productId
        val shopId = repurchaseProduct.shopId
        val currentQuantity = nonVariant.quantity

        cartService.handleCart(
            CartProductItem(productId, shopId, currentQuantity),
            quantity,
            onSuccessAddToCart = {
                updateCartMessageSuccess(it.errorMessage.joinToString(separator = ", "))
                onSuccessATCRepurchaseWidgetProduct(repurchaseProduct, quantity)
                sendAddToCartRepurchaseProductTracking(quantity, it.data.cartId, repurchaseProduct)
            },
            onSuccessUpdateCart = {
                onSuccessATCRepurchaseWidgetProduct(repurchaseProduct, quantity)
            },
            onSuccessDeleteCart = {
                updateCartMessageSuccess(it.errorMessage.joinToString(separator = ", "))
                onSuccessATCRepurchaseWidgetProduct(repurchaseProduct, 0)
            },
            onError = ::onAddToCartFailed,
            handleCartEventNonLogin = {
                handleAddToCartEventNonLogin(getRepurchaseWidgetIndex())
            }
        )
    }

    private fun onSuccessATCRepurchaseWidgetProduct(
        repurchaseProduct: TokoNowProductCardUiModel,
        quantity: Int,
    ) {
        val nonVariant = repurchaseProduct.product.nonVariant ?: return

        repurchaseProduct.product = repurchaseProduct.product.copy(
            nonVariant = nonVariant.copy(quantity = quantity)
        )

        refreshMiniCart()
    }

    private fun sendAddToCartRepurchaseProductTracking(
        quantity: Int,
        cartId: String,
        repurchaseProduct: TokoNowProductCardUiModel,
    ) {
        addToCartRepurchaseWidgetTrackingMutableLiveData.value =
            Triple(quantity, cartId, repurchaseProduct)
    }

    private fun getRepurchaseWidgetIndex() =
        visitableList.indexOfFirst { it is TokoNowRepurchaseUiModel }

    private fun getUniqueId() =
        if (userSession.isLoggedIn) AuthHelper.getMD5Hash(userSession.userId)
        else AuthHelper.getMD5Hash(userSession.deviceId)

    protected class HeaderDataView(
            val title: String = "",
            val aceSearchProductHeader: SearchProductHeader = SearchProductHeader(),
            categoryFilterDataValue: DataValue = DataValue(),
            quickFilterDataValue: DataValue = DataValue(),
            val bannerChannel: Channels = Channels(),
    ) {
        val categoryFilterDataValue = DataValue(
                filter = FilterHelper.copyFilterWithOptionAsExclude(categoryFilterDataValue.filter)
        )

        val quickFilterDataValue = DataValue(
                filter = quickFilterDataValue.filter.map { filter ->
                    filter.clone(options = createOptionListWithExclude(filter))
                }
        )

        private fun createOptionListWithExclude(filter: Filter) =
                filter.options.map { option ->
                    option.clone().also { copyOption ->
                        modifyOptionKeyInCategoryFilter(copyOption)
                    }
                }

        private fun modifyOptionKeyInCategoryFilter(option: Option) {
            val isCategoryFilter = isInCategoryFilter(option)

            if (isCategoryFilter)
                option.key = OptionHelper.EXCLUDE_PREFIX + option.key
        }

        private fun isInCategoryFilter(optionToCheck: Option): Boolean {
            val categoryOptionList = categoryFilterDataValue.filter.map { it.options }.flatten()

            return categoryOptionList.any {
                it.key.removePrefix(OptionHelper.EXCLUDE_PREFIX) == optionToCheck.key
                        && it.value == optionToCheck.value
            }
        }
    }

    protected data class ContentDataView(
            val aceSearchProductData: SearchProductData = SearchProductData(),
            val repurchaseWidget: RepurchaseData = RepurchaseData()
    )
}