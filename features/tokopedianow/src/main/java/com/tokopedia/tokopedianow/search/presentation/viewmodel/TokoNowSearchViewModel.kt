package com.tokopedia.tokopedianow.search.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.helper.isNotFilterAndSortKey
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.filter.newdynamicfilter.helper.FilterHelper
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.home_component.data.DynamicHomeChannelCommon
import com.tokopedia.home_component.mapper.DynamicChannelComponentMapper
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants
import com.tokopedia.tokopedianow.common.constant.ConstantKey
import com.tokopedia.tokopedianow.common.constant.ServiceType
import com.tokopedia.tokopedianow.common.constant.ServiceType.NOW_15M
import com.tokopedia.tokopedianow.common.constant.TokoNowStaticLayoutType
import com.tokopedia.tokopedianow.common.domain.mapper.AddressMapper
import com.tokopedia.tokopedianow.common.domain.mapper.ProductAdsMapper
import com.tokopedia.tokopedianow.common.domain.mapper.TickerMapper
import com.tokopedia.tokopedianow.common.domain.model.GetProductAdsResponse
import com.tokopedia.tokopedianow.common.domain.model.GetTargetedTickerResponse
import com.tokopedia.tokopedianow.common.domain.model.SetUserPreference
import com.tokopedia.tokopedianow.common.domain.param.GetProductAdsParam
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.domain.usecase.SetUserPreferenceUseCase
import com.tokopedia.tokopedianow.common.model.NowAffiliateAtcData
import com.tokopedia.tokopedianow.common.model.TokoNowAdsCarouselUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateNoResultUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateOocUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationOocUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowTickerUiModel
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.search.analytics.SearchTracking
import com.tokopedia.tokopedianow.search.domain.mapper.VisitableMapper.addBroadMatchDataView
import com.tokopedia.tokopedianow.search.domain.mapper.VisitableMapper.addSuggestionDataView
import com.tokopedia.tokopedianow.search.domain.mapper.VisitableMapper.updateSuggestionDataView
import com.tokopedia.tokopedianow.search.domain.model.SearchModel
import com.tokopedia.tokopedianow.search.presentation.model.BroadMatchDataView
import com.tokopedia.tokopedianow.search.presentation.model.CTATokopediaNowHomeDataView
import com.tokopedia.tokopedianow.search.presentation.typefactory.SearchTypeFactory
import com.tokopedia.tokopedianow.search.utils.SEARCH_FIRST_PAGE_USE_CASE
import com.tokopedia.tokopedianow.search.utils.SEARCH_LOAD_MORE_PAGE_USE_CASE
import com.tokopedia.tokopedianow.search.utils.SEARCH_QUERY_PARAM_MAP
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst
import com.tokopedia.tokopedianow.searchcategory.cartservice.CartProductItem
import com.tokopedia.tokopedianow.searchcategory.cartservice.CartService
import com.tokopedia.tokopedianow.searchcategory.data.model.QuerySafeModel
import com.tokopedia.tokopedianow.searchcategory.domain.mapper.ProductItemMapper
import com.tokopedia.tokopedianow.searchcategory.domain.mapper.VisitableMapper.updateWishlistStatus
import com.tokopedia.tokopedianow.searchcategory.domain.mapper.mapChooseAddressToQuerySafeModel
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.tokopedianow.searchcategory.domain.usecase.GetFilterUseCase
import com.tokopedia.tokopedianow.searchcategory.presentation.model.AllProductTitle
import com.tokopedia.tokopedianow.searchcategory.presentation.model.BannerDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.CategoryFilterDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.CategoryFilterItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ChooseAddressDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductCountDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProgressBarDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.SearchTitle
import com.tokopedia.tokopedianow.searchcategory.presentation.model.SortFilterItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.SwitcherWidgetDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.TitleDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.TokoNowFeedbackWidgetUiModel
import com.tokopedia.tokopedianow.searchcategory.utils.ChooseAddressWrapper
import com.tokopedia.tokopedianow.searchcategory.utils.PRODUCT_ADS_PARAMS
import com.tokopedia.tokopedianow.searchcategory.utils.TOKONOW
import com.tokopedia.tokopedianow.searchcategory.utils.TOKONOW_QUERY_PARAMS
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class TokoNowSearchViewModel @Inject constructor(
    private val baseDispatcher: CoroutineDispatchers,
    @Named(SEARCH_QUERY_PARAM_MAP)
    private val queryParamMap: Map<String, String>,
    @param:Named(SEARCH_FIRST_PAGE_USE_CASE)
    private val getSearchFirstPageUseCase: UseCase<SearchModel>,
    @param:Named(SEARCH_LOAD_MORE_PAGE_USE_CASE)
    private val getSearchLoadMorePageUseCase: UseCase<SearchModel>,
    private val getFilterUseCase: GetFilterUseCase,
    private val getProductCountUseCase: UseCase<String>,
    private val getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase,
    private val cartService: CartService,
    private val getWarehouseUseCase: GetChosenAddressWarehouseLocUseCase,
    private val setUserPreferenceUseCase: SetUserPreferenceUseCase,
    private val remoteConfig: RemoteConfig,
    private val chooseAddressWrapper: ChooseAddressWrapper,
    private val affiliateService: NowAffiliateService,
    private val userSession: UserSessionInterface
) : BaseViewModel(baseDispatcher.io) {

    companion object {
        private const val MIN_PRODUCT_COUNT = 6
        private const val DEFAULT_HEADER_Y_COORDINATE = 0f

        private val showBroadMatchResponseCodeList = listOf("4", "5")
        private val showSuggestionResponseCodeList = listOf("3", "6", "7")
    }

    private var chooseAddressDataView = ChooseAddressDataView()
    private val loadingMoreModel = LoadingMoreModel()
    private val visitableList = mutableListOf<Visitable<*>>()
    private val queryParamMutable = queryParamMap.toMutableMap()
    private var totalData = 0
    private var chooseAddressData = chooseAddressWrapper.getChooseAddressData()
    private var hasBlockedAddToCart = false

    private var headerYCoordinate = 0f
    private val filterController = FilterController()
    private var totalFetchedData = 0
    private var nextPage = 1
    private var currentProductPosition: Int = 1
    private var feedbackFieldToggle = false
    private var isFeedbackFieldVisible = false
    private var getFilterJob: Job? = null
    private var excludeFilter: Option? = null

    private val addToCartBroadMatchTrackingMutableLiveData: SingleLiveEvent<Triple<Int, String, ProductCardCompactCarouselItemUiModel>> = SingleLiveEvent()
    private var responseCode: String = ""
    private var suggestionModel: AceSearchProductModel.Suggestion? = null
    private var related: AceSearchProductModel.Related? = null
    private var recommendationCategoryId: String = ""

    val addToCartBroadMatchTrackingLiveData: LiveData<Triple<Int, String, ProductCardCompactCarouselItemUiModel>> = addToCartBroadMatchTrackingMutableLiveData
    val query = queryParamMap[SearchApiConst.Q].orEmpty()

    private val tokonowSource: String
        get() = TOKONOW
    private val tickerPageSource: String
        get() = GetTargetedTickerUseCase.SEARCH_PAGE

    val queryParam: Map<String, String> = queryParamMutable
    val hasGlobalMenu: Boolean
    var warehouseId = ""
        private set
    var autoCompleteApplink = ""
        private set
    var serviceType = ""
        private set

    private val firstPageSuccessTriggerMutableLiveData = MutableLiveData<Unit>()
    val firstPageSuccessTriggerLiveData: LiveData<Unit> = firstPageSuccessTriggerMutableLiveData

    private val loadMoreSuccessTriggerMutableLiveData = MutableLiveData<Unit>()
    val loadMoreSuccessTriggerLiveData: LiveData<Unit> = loadMoreSuccessTriggerMutableLiveData

    private val stopPerformanceMonitoringMutableLiveData = MutableLiveData<Unit>()
    val stopPerformanceMonitoringLiveData: LiveData<Unit> = stopPerformanceMonitoringMutableLiveData

    private val visitableListMutableLiveData = MutableLiveData<List<Visitable<*>>>(visitableList)
    val visitableListLiveData: LiveData<List<Visitable<*>>> = visitableListMutableLiveData

    private val hasNextPageMutableLiveData = MutableLiveData(false)
    val hasNextPageLiveData: LiveData<Boolean> = hasNextPageMutableLiveData

    private val isFilterPageOpenMutableLiveData = MutableLiveData(false)
    val isFilterPageOpenLiveData: LiveData<Boolean> = isFilterPageOpenMutableLiveData

    private val dynamicFilterModelMutableLiveData = MutableLiveData<DynamicFilterModel?>(null)
    val dynamicFilterModelLiveData: LiveData<DynamicFilterModel?> = dynamicFilterModelMutableLiveData

    private val productCountAfterFilterMutableLiveData = MutableLiveData("")
    val productCountAfterFilterLiveData: LiveData<String> = productCountAfterFilterMutableLiveData

    private val isL3FilterPageOpenMutableLiveData = MutableLiveData<Filter?>(null)
    val isL3FilterPageOpenLiveData: LiveData<Filter?> = isL3FilterPageOpenMutableLiveData

    private val isShowMiniCartMutableLiveData = MutableLiveData<Boolean?>(null)
    val isShowMiniCartLiveData: LiveData<Boolean?> = isShowMiniCartMutableLiveData

    private val miniCartWidgetMutableLiveData = MutableLiveData<MiniCartSimplifiedData?>(null)
    val miniCartWidgetLiveData: LiveData<MiniCartSimplifiedData?> = miniCartWidgetMutableLiveData

    private val updatedVisitableIndicesMutableLiveData = SingleLiveEvent<List<Int>>()
    val updatedVisitableIndicesLiveData: LiveData<List<Int>> = updatedVisitableIndicesMutableLiveData

    private val successAddToCartMessageMutableLiveData = SingleLiveEvent<String>()
    val successAddToCartMessageLiveData: LiveData<String> = successAddToCartMessageMutableLiveData

    private val successRemoveFromCartMessageMutableLiveData = SingleLiveEvent<String>()
    val successRemoveFromCartMessageLiveData: LiveData<String> = successRemoveFromCartMessageMutableLiveData

    private val errorATCMessageMutableLiveData = SingleLiveEvent<String>()
    val errorATCMessageLiveData: LiveData<String> = errorATCMessageMutableLiveData

    private val isHeaderBackgroundVisibleMutableLiveData = MutableLiveData(true)
    val isHeaderBackgroundVisibleLiveData: LiveData<Boolean> = isHeaderBackgroundVisibleMutableLiveData

    private val isContentLoadingMutableLiveData = MutableLiveData(true)
    val isContentLoadingLiveData: LiveData<Boolean> = isContentLoadingMutableLiveData

    private val shopIdMutableLiveData = MutableLiveData("")
    val shopIdLiveData: LiveData<String> = shopIdMutableLiveData

    var miniCartSource: MiniCartSource = MiniCartSource.TokonowSRP

    private val addToCartTrackingMutableLiveData =
        SingleLiveEvent<Triple<Int, String, ProductItemDataView>>()
    val addToCartTrackingLiveData: LiveData<Triple<Int, String, ProductItemDataView>> =
        addToCartTrackingMutableLiveData

    private val decreaseQtyTrackingMutableLiveData = SingleLiveEvent<String>()
    val decreaseQtyTrackingLiveData: LiveData<String> = decreaseQtyTrackingMutableLiveData

    private val increaseQtyTrackingMutableLiveData = SingleLiveEvent<String>()
    val increaseQtyTrackingLiveData: LiveData<String> = increaseQtyTrackingMutableLiveData

    private val quickFilterTrackingMutableLiveData = SingleLiveEvent<Pair<Option, Boolean>>()
    val quickFilterTrackingLiveData: LiveData<Pair<Option, Boolean>> = quickFilterTrackingMutableLiveData

    private val isShowErrorMutableLiveData = SingleLiveEvent<Throwable?>()
    val isShowErrorLiveData: LiveData<Throwable?> = isShowErrorMutableLiveData

    private val routeApplinkMutableLiveData = SingleLiveEvent<String>()
    val routeApplinkLiveData: LiveData<String> = routeApplinkMutableLiveData

    private val deleteCartTrackingMutableLiveData = SingleLiveEvent<String>()
    val deleteCartTrackingLiveData: LiveData<String> = deleteCartTrackingMutableLiveData

    private val generalSearchEventMutableLiveData = SingleLiveEvent<Map<String, Any>>()
    val generalSearchEventLiveData: LiveData<Map<String, Any>> = generalSearchEventMutableLiveData

    private val oocOpenScreenTrackingMutableEvent = SingleLiveEvent<Boolean>()
    val oocOpenScreenTrackingEvent: LiveData<Boolean> = oocOpenScreenTrackingMutableEvent

    private val setUserPreferenceMutableLiveData = SingleLiveEvent<Result<SetUserPreference.SetUserPreferenceData>>()
    val setUserPreferenceLiveData: LiveData<Result<SetUserPreference.SetUserPreferenceData>> = setUserPreferenceMutableLiveData

    private val querySafeMutableLiveData = SingleLiveEvent<QuerySafeModel>()
    val querySafeLiveData: LiveData<QuerySafeModel> = querySafeMutableLiveData

    private val updateToolbarNotificationLiveData = MutableLiveData<Boolean>()
    val updateToolbarNotification: LiveData<Boolean> = updateToolbarNotificationLiveData

    private val _feedbackLoopTrackingMutableLivedata: MutableLiveData<Pair<String, Boolean>> = MutableLiveData(null)
    val feedbackLoopTrackingMutableLivedata: LiveData<Pair<String, Boolean>> = _feedbackLoopTrackingMutableLivedata

    private val updateAdsCarouselMutableLiveData = MutableLiveData<Pair<Int, TokoNowAdsCarouselUiModel>>()
    val updateAdsCarouselLiveData: LiveData<Pair<Int, TokoNowAdsCarouselUiModel>> = updateAdsCarouselMutableLiveData

    var isEmptyResult: Boolean = false

    init {
        updateQueryParams()

        hasGlobalMenu = isABTestNavigationRevamp()
        chooseAddressDataView = ChooseAddressDataView(chooseAddressData)
    }

    private fun showLoading() {
        isContentLoadingMutableLiveData.value = true
    }

    private fun updateQueryParams() {
        queryParamMutable[SearchApiConst.OB] = SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT
        queryParamMutable[SearchApiConst.NAVSOURCE] = tokonowSource
        queryParamMutable[SearchApiConst.SOURCE] = tokonowSource
    }

    private fun isABTestNavigationRevamp() = true

    fun onViewCreated(source: MiniCartSource? = null) {
        initAffiliateCookie()
        processLoadDataPage(source)
    }

    private fun processLoadDataPage(source: MiniCartSource? = null) {
        val shopId = chooseAddressData.shop_id
        val warehouseId = chooseAddressData.warehouse_id
        val serviceType = chooseAddressData.service_type
        if (source != null) {
            miniCartSource = source
        }

        if (shopId.isValidId()) {
            processLoadDataWithShopId(shopId, warehouseId, serviceType)
        } else {
            getShopIdBeforeLoadData()
        }
    }

    private fun processLoadDataWithShopId(shopId: String, warehouseId: String, serviceType: String) {
        this.shopIdMutableLiveData.value = shopId
        this.warehouseId = warehouseId
        this.serviceType = serviceType

        if (warehouseId.isValidId()) {
            loadFirstPage()
        } else {
            showOutOfCoverage()
        }
    }

    private fun getShopIdBeforeLoadData() {
        launch {
            try {
                onGetShopAndWarehouseSuccess(getWarehouseUseCase(SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH))
            } catch (e: Exception) {
                onGetShopAndWarehouseFailed(e)
            }
        }
    }

    private fun onGetShopAndWarehouseSuccess(state: GetStateChosenAddressResponse) {
        val tokonowData = state.tokonow
        val shopId = tokonowData.shopId.toString()
        val warehouseId = tokonowData.warehouseId.toString()
        val serviceType = tokonowData.serviceType

        processLoadDataWithShopId(shopId, warehouseId, serviceType)

        refreshMiniCart()
    }

    private fun showOutOfCoverage() {
        stopPerformanceMonitoringMutableLiveData.value = Unit

        updateHeaderBackgroundVisibility(false)
        updateMiniCartVisibility(false)

        sendOutOfCoverageTrackingEvent()
        createVisitableListWithOutOfCoverageView()
        clearVisitableListLiveData()
        updateVisitableListLiveData()
        showPageContent()
    }

    private fun sendOutOfCoverageTrackingEvent() {
        oocOpenScreenTrackingMutableEvent.value = true
    }

    private fun createVisitableListWithOutOfCoverageView() {
        visitableList.clear()
        visitableList.add(chooseAddressDataView)
        visitableList.add(
            TokoNowEmptyStateOocUiModel(
                hostSource = SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH,
                serviceType = serviceType
            )
        )
        visitableList.add(
            TokoNowProductRecommendationOocUiModel(
                pageName = RecomPageConstant.OOC_TOKONOW,
                isBindWithPageName = true
            )
        )
    }

    private fun onGetShopAndWarehouseFailed(throwable: Throwable) {
        stopPerformanceMonitoringMutableLiveData.value = Unit
    }

    private fun createRequestParams(): RequestParams {
        val tokonowQueryParam = createTokonowQueryParams()
        val productAdsParam = createGetProductAdsParam()

        val requestParams = RequestParams.create()
        requestParams.putObject(TOKONOW_QUERY_PARAMS, tokonowQueryParam)
        requestParams.putObject(PRODUCT_ADS_PARAMS, productAdsParam)
        requestParams.putObject(SearchApiConst.USER_WAREHOUSE_ID, warehouseId)

        return requestParams
    }

    private fun createTokonowQueryParams(): MutableMap<String, Any> {
        val tokonowQueryParam = mutableMapOf<String, Any>()

        appendMandatoryParams(tokonowQueryParam)
        appendPaginationParam(tokonowQueryParam)
        appendQueryParam(tokonowQueryParam)

        return tokonowQueryParam
    }

    private fun appendMandatoryParams(tokonowQueryParam: MutableMap<String, Any>) {
        appendDeviceParam(tokonowQueryParam)
        appendChooseAddressParams(tokonowQueryParam)
        appendUniqueIdParam(tokonowQueryParam)
    }

    private fun appendDeviceParam(tokonowQueryParam: MutableMap<String, Any>) {
        tokonowQueryParam[SearchApiConst.DEVICE] = SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE
    }

    private fun appendChooseAddressParams(tokonowQueryParam: MutableMap<String, Any>) {
        if (chooseAddressData.city_id.isNotEmpty()) {
            tokonowQueryParam[SearchApiConst.USER_CITY_ID] = chooseAddressData.city_id
        }
        if (chooseAddressData.address_id.isNotEmpty()) {
            tokonowQueryParam[SearchApiConst.USER_ADDRESS_ID] = chooseAddressData.address_id
        }
        if (chooseAddressData.district_id.isNotEmpty()) {
            tokonowQueryParam[SearchApiConst.USER_DISTRICT_ID] = chooseAddressData.district_id
        }
        if (chooseAddressData.lat.isNotEmpty()) {
            tokonowQueryParam[SearchApiConst.USER_LAT] = chooseAddressData.lat
        }
        if (chooseAddressData.long.isNotEmpty()) {
            tokonowQueryParam[SearchApiConst.USER_LONG] = chooseAddressData.long
        }
        if (chooseAddressData.postal_code.isNotEmpty()) {
            tokonowQueryParam[SearchApiConst.USER_POST_CODE] = chooseAddressData.postal_code
        }
        if (chooseAddressData.warehouses.isNotEmpty()) {
            tokonowQueryParam[SearchApiConst.WAREHOUSES] =
                AddressMapper.mapToWarehouses(chooseAddressData)
        }
    }

    private fun appendUniqueIdParam(tokonowQueryParam: MutableMap<String, Any>) {
        tokonowQueryParam[SearchApiConst.UNIQUE_ID] = getUniqueId()
    }

    private fun appendPaginationParam(tokonowQueryParam: MutableMap<String, Any>) {
        tokonowQueryParam[SearchApiConst.PAGE] = nextPage
        tokonowQueryParam[SearchApiConst.USE_PAGE] = true
        tokonowQueryParam[SearchApiConst.ROWS] = getRows()
    }

    private fun appendQueryParam(tokonowQueryParam: MutableMap<String, Any>) {
        tokonowQueryParam.putAll(FilterHelper.createParamsWithoutExcludes(queryParam))
    }

    private fun createGetProductAdsParam(): Map<String?, Any> {
        val params = createProductAdsParam().also {
            it.putAll(FilterHelper.createParamsWithoutExcludes(queryParam))
        }
        return params
    }

    private fun onGetFirstPageSuccess(
        headerDataView: HeaderDataView,
        contentDataView: ContentDataView,
        searchProduct: AceSearchProductModel.SearchProduct,
        feedbackFieldIsActive: Boolean
    ) {
        firstPageSuccessTriggerMutableLiveData.value = Unit

        totalData = headerDataView.aceSearchProductHeader.totalData
        totalFetchedData += contentDataView.aceSearchProductData.productList.size
        autoCompleteApplink = contentDataView.aceSearchProductData.autocompleteApplink
        currentProductPosition = 1
        feedbackFieldToggle = feedbackFieldIsActive

        val isEmptyProductList = contentDataView.aceSearchProductData.productList.isEmpty()

        initFilterController(headerDataView)
        createVisitableListFirstPage(headerDataView, contentDataView, isEmptyProductList, searchProduct.data.violation)
        if (getKeywordForGeneralSearchTracking().isNotEmpty()) {
            sendGeneralSearchTracking(searchProduct)
        }
        updateViewForFirstPage(isEmptyProductList)

        querySafeMutableLiveData.value = chooseAddressData.mapChooseAddressToQuerySafeModel(
            isQuerySafe = searchProduct.data.isQuerySafe
        )
    }

    private fun initFilterController(headerDataView: HeaderDataView) {
        if (dynamicFilterModelLiveData.value != null) return

        val filterList = headerDataView.quickFilterDataValue.filter + headerDataView.categoryFilterDataValue.filter

        filterController.initFilterController(queryParamMutable, filterList)
    }

    private fun createVisitableListFirstPage(
        headerDataView: HeaderDataView,
        contentDataView: ContentDataView,
        isEmptyProductList: Boolean,
        violation: AceSearchProductModel.Violation
    ) {
        visitableList.clear()

        if (isEmptyProductList) {
            createVisitableListWithEmptyProduct(violation)
        } else {
            createVisitableListWithProduct(headerDataView, contentDataView)
        }
    }

    private fun createVisitableListWithProduct(
        headerDataView: HeaderDataView,
        contentDataView: ContentDataView
    ) {
        isEmptyResult = false
        visitableList.addAll(createHeaderVisitableList(headerDataView))
        visitableList.addAll(createContentVisitableList(contentDataView))
        if (isLastPage() && feedbackFieldToggle && headerDataView.aceSearchProductHeader.totalData <= MIN_PRODUCT_COUNT) {
            _feedbackLoopTrackingMutableLivedata.value = Pair(
                first = chooseAddressData.warehouse_id,
                second = true
            )
            isFeedbackFieldVisible = true
            visitableList.add(TokoNowFeedbackWidgetUiModel(true))
        } else {
            isFeedbackFieldVisible = false
        }
        visitableList.addFooter()
    }

    private fun createHeaderVisitableList(headerDataView: HeaderDataView): List<Visitable<*>> {
        val headerList = mutableListOf<Visitable<*>>()

        headerList.add(chooseAddressDataView)
        headerList.addTicker(headerDataView)
        headerList.add(createBannerDataView(headerDataView))
        headerList.add(createTitleDataView(headerDataView))

        processCategoryFilter(headerList, headerDataView.categoryFilterDataValue)

        headerList.add(QuickFilterDataView(createQuickFilterItemList(headerDataView), queryParam))
        headerList.add(ProductCountDataView(headerDataView.aceSearchProductHeader.totalDataText))

        postProcessHeaderList(headerList)

        return headerList
    }

    private fun MutableList<Visitable<*>>.addTicker(headerDataView: HeaderDataView) {
        val (needToBlockAtc, tickerData) = TickerMapper.mapTickerData(headerDataView.targetedTicker)
        hasBlockedAddToCart = needToBlockAtc
        if (tickerData.isNotEmpty()) {
            add(
                TokoNowTickerUiModel(
                    id = String.EMPTY,
                    tickers = tickerData
                )
            )
        }
    }

    private fun processCategoryFilter(
        headerList: MutableList<Visitable<*>>,
        categoryFilterDataValue: DataValue
    ) {
        val categoryFilter = categoryFilterDataValue.filter.getOrNull(0)
        categoryFilter ?: return

        if (isShowCategoryFilter(categoryFilter)) {
            headerList.add(CategoryFilterDataView(createCategoryFilterItemList(categoryFilter)))
        }
    }

    private fun isShowCategoryFilter(categoryFilter: Filter) =
        categoryFilter.options.size > 1

    private fun createBannerDataView(headerDataView: HeaderDataView): BannerDataView {
        val channel = headerDataView.bannerChannel
        val position = 1
        val channelModel = DynamicChannelComponentMapper.mapChannelToComponent(channel, position)

        return BannerDataView(channelModel)
    }

    private fun createCategoryFilterItemList(categoryFilter: Filter) =
        categoryFilter.options.map {
            CategoryFilterItemDataView(it, filterController.getFilterViewState(it))
        }

    private fun createQuickFilterItemList(headerDataView: HeaderDataView) =
        headerDataView.quickFilterDataValue.filter.map {
            SortFilterItemDataView(
                filter = it,
                sortFilterItem = createSortFilterItem(it)
            )
        }

    private fun createSortFilterItem(filter: Filter): SortFilterItem {
        val isSelected = getQuickFilterIsSelected(filter)
        val chipType = getSortFilterItemType(isSelected)

        val sortFilterItem = SortFilterItem(filter.title, chipType)
        sortFilterItem.typeUpdated = false

        val option = filter.options.firstOrNull() ?: Option()
        if (filter.options.size == 1) {
            sortFilterItem.listener = {
                sendQuickFilterTrackingEvent(option, isSelected)
                filter(option, !isSelected)
            }
        } else {
            val listener = {
                openL3FilterPage(filter)
            }
            sortFilterItem.chevronListener = listener
            sortFilterItem.listener = listener
            if (option.key.startsWith(OptionHelper.EXCLUDE_PREFIX)) {
                excludeFilter = option
            }
        }

        return sortFilterItem
    }

    private fun getQuickFilterIsSelected(filter: Filter) =
        filter.options.any {
            if (it.key.contains(OptionHelper.EXCLUDE_PREFIX)) {
                false
            } else {
                filterController.getFilterViewState(it)
            }
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
            isCleanUpExistingFilterWithSameKey = option.isCategoryOption
        )

        refreshQueryParamFromFilterController()

        applyFilter()
    }

    private fun refreshQueryParamFromFilterController() {
        queryParamMutable.clear()
        queryParamMutable.putAll(filterController.getParameter())
    }

    fun onViewReloadPage(
        needToResetQueryParams: Boolean = true,
        updateMoreQueryParams: () -> Unit = {}
    ) {
        resetQueryParam(
            needToResetQueryParams = needToResetQueryParams,
            updateMoreQueryParams = updateMoreQueryParams
        )

        totalData = 0
        totalFetchedData = 0
        nextPage = 1
        chooseAddressData = chooseAddressWrapper.getChooseAddressData()
        chooseAddressDataView = ChooseAddressDataView(chooseAddressData)
        isFeedbackFieldVisible = false
        showLoading()
        processLoadDataPage()
    }

    /**
     * Reset the query param is needed to ensure the query param will be used is the previous query param,
     * this reset mechanism will set the dynamic filter to null.
     *
     * @param needToResetQueryParams there will be a some cases where it needs to reset to query param,
     * example case is when trying to pull and refresh the page. Otherwise there will be a few cases reset is not needed,
     * example case is when reload page after the filter is selected.
     * @param updateMoreQueryParams is used for child of BaseViewModel to update the query param needed on the page.
     */
    private fun resetQueryParam(
        needToResetQueryParams: Boolean,
        updateMoreQueryParams: () -> Unit
    ) {
        if (!needToResetQueryParams) return

        dynamicFilterModelMutableLiveData.value = null
        queryParamMutable.clear()
        queryParamMutable.putAll(queryParamMap)
        updateQueryParams()
        updateMoreQueryParams.invoke()
    }

    private fun applyFilter() {
        totalData = 0
        totalFetchedData = 0
        nextPage = 1
        chooseAddressData = chooseAddressWrapper.getChooseAddressData()
        chooseAddressDataView = ChooseAddressDataView(chooseAddressData)

        showProgressBar()
        processLoadDataPage()
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

    private fun openL3FilterPage(selectedFilter: Filter) {
        if (isL3FilterPageOpenMutableLiveData.value != null) return

        isL3FilterPageOpenMutableLiveData.value = selectedFilter
    }

    private fun createContentVisitableList(
        contentDataView: ContentDataView
    ): List<Visitable<*>> {
        val contentVisitableList = mutableListOf<Visitable<*>>()
        val productList = contentDataView
            .aceSearchProductData
            .productList
        val productAds = contentDataView.productAds

        addProductAds(contentVisitableList, productAds)
        addProductList(contentVisitableList, productList)

        return contentVisitableList
    }

    private fun addProductAds(
        contentVisitableList: MutableList<Visitable<*>>,
        response: GetProductAdsResponse.ProductAdsResponse
    ) {
        if (response.productList.isNotEmpty()) {
            contentVisitableList.add(
                ProductAdsMapper.mapProductAdsCarousel(
                    id = TokoNowStaticLayoutType.PRODUCT_ADS_CAROUSEL,
                    response = response,
                    miniCartData = miniCartWidgetLiveData.value,
                    hasBlockedAddToCart = hasBlockedAddToCart
                )
            )
        }
    }

    private fun addProductList(
        contentVisitableList: MutableList<Visitable<*>>,
        productList: List<AceSearchProductModel.Product>
    ) {
        val productListDataView = productList.mapIndexed { index, product ->
            ProductItemMapper.mapResponseToProductItem(
                index = index,
                product = product,
                cartService = cartService,
                hasBlockedAddToCart = hasBlockedAddToCart
            )
        }
        contentVisitableList.addAll(productListDataView)
    }

    private fun updateAdsCarouselQuantity(
        adsCarouselUiModel: TokoNowAdsCarouselUiModel,
        index: Int
    ) {
        val newItems = adsCarouselUiModel.items.map { productUiModel ->
            productUiModel.copy(productCardModel = createProductCardCompactModel(productUiModel))
        }
        val newAdsCarousel = adsCarouselUiModel.copy(items = newItems)
        updateAdsCarouselMutableLiveData.postValue(Pair(index, newAdsCarousel))
    }

    private fun createProductCardCompactModel(
        product: ProductCardCompactCarouselItemUiModel
    ): ProductCardCompactUiModel {
        val quantity = cartService.getProductQuantity(
            product.getProductId(),
            product.parentId
        )
        return product.productCardModel.copy(orderQuantity = quantity)
    }

    private fun MutableList<Visitable<*>>.addFooter() {
        if (isLastPage()) {
            // show switcher only if service type is 15m
            if (serviceType == ServiceType.NOW_15M) {
                add(SwitcherWidgetDataView())
            }
            addAll(createFooterVisitableList())
        } else {
            add(loadingMoreModel)
        }
    }

    private fun isLastPage() = totalFetchedData >= totalData

    private fun sendGeneralSearchTracking(searchProduct: AceSearchProductModel.SearchProduct) {
        val searchProductHeader = searchProduct.header
        val eventLabel = getKeywordForGeneralSearchTracking() +
            "|${searchProductHeader.keywordProcess}" +
            "|${searchProductHeader.responseCode}" +
            "|${TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_PHYSICAL_GOODS}" +
            "|$TOKONOW" +
            "|${SearchCategoryTrackingConst.Misc.TOKOPEDIA_NOW}" +
            "|${searchProductHeader.totalData}"
        val previousKeyword = getPreviousKeywordForGeneralSearchTracking()
        val pageSource = getPageSourceForGeneralSearchTracking()

        val generalSearchDataLayer = mapOf(
            TrackAppUtils.EVENT to TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_TOKONOW,
            TrackAppUtils.EVENT_ACTION to SearchTracking.Action.GENERAL_SEARCH,
            TrackAppUtils.EVENT_CATEGORY to SearchTracking.Category.TOP_NAV,
            TrackAppUtils.EVENT_LABEL to eventLabel,
            TokoNowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT to TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_PHYSICAL_GOODS,
            TokoNowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE to TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
            SearchCategoryTrackingConst.Misc.RELATEDKEYWORD to "$previousKeyword - ${searchProduct.getAlternativeKeyword()}",
            SearchCategoryTrackingConst.Misc.PAGESOURCE to pageSource
        )

        generalSearchEventMutableLiveData.value = generalSearchDataLayer
    }

    private fun getPreviousKeywordForGeneralSearchTracking(): String {
        val previousKeyword = queryParam[SearchApiConst.PREVIOUS_KEYWORD] ?: ""

        return if (previousKeyword.isBlank()) SearchCategoryTrackingConst.Misc.NONE else previousKeyword
    }

    private fun getPageSourceForGeneralSearchTracking(): String {
        return "${SearchCategoryTrackingConst.Misc.TOKOPEDIA_NOW}.$TOKONOW.${SearchCategoryTrackingConst.Misc.LOCAL_SEARCH}.$warehouseId"
    }

    private fun updateViewForFirstPage(isEmptyProductList: Boolean) {
        updateVisitableListLiveData()

        updateNextPageData()
        updateHeaderBackgroundVisibility(!isEmptyProductList)

        showPageContent()
    }

    private fun clearVisitableListLiveData() {
        visitableListMutableLiveData.value = listOf()
    }

    private fun updateVisitableListLiveData() {
        visitableListMutableLiveData.value = visitableList
    }

    private fun updateNextPageData() {
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

    private fun onGetFirstPageError(throwable: Throwable) {
        stopPerformanceMonitoringMutableLiveData.value = Unit
        isShowErrorMutableLiveData.value = throwable
    }

    fun onLoadMore() {
        if (hasLoadedAllData()) return

        executeLoadMore()
    }

    private fun hasLoadedAllData() = totalData <= totalFetchedData

    private fun onGetLoadMorePageSuccess(contentDataView: ContentDataView) {
        loadMoreSuccessTriggerMutableLiveData.value = Unit
        totalFetchedData += contentDataView.aceSearchProductData.productList.size

        updateVisitableListForNextPage(contentDataView)
        updateVisitableListLiveData()
        updateNextPageData()
    }

    private fun updateVisitableListForNextPage(contentDataView: ContentDataView) {
        visitableList.remove(loadingMoreModel)
        visitableList.addAll(createContentVisitableList(contentDataView))
        visitableList.addFooter()
    }

    fun removeProductRecommendationWidget() {
        visitableList.removeFirst { it is TokoNowProductRecommendationUiModel }

        updateVisitableListLiveData()
    }

    /**
     * Dynamic filter will have null value when first time user clicks filter to open main bottomsheet.
     * in that case need to fetch the data, so filter in bottomsheet will be up to date.
     */
    fun onViewOpenFilterPage() {
        if (isFilterPageOpenLiveData.value == true) return

        if (dynamicFilterModelLiveData.value == null) {
            getFilter(
                needToOpenBottomSheet = true
            )
        } else {
            isFilterPageOpenMutableLiveData.value = true
        }
    }

    /**
     * Update filter in main bottomsheet to be up to date
     *
     * @param needToOpenBottomSheet is used only when clicking filter chip
     */
    private fun getFilter(
        needToOpenBottomSheet: Boolean
    ) {
        getFilterJob?.cancel()
        getFilterJob = launchCatchError(
            block = {
                val dynamicFilterModel = getFilterUseCase.execute(createTokonowQueryParams())
                filterController.appendFilterList(queryParam, dynamicFilterModel.data.filter)
                dynamicFilterModelMutableLiveData.postValue(dynamicFilterModel)

                if (needToOpenBottomSheet) {
                    isFilterPageOpenMutableLiveData.postValue(true)
                }
            },
            onError = { /* do nothing */ }
        )
    }

    fun onViewDismissFilterPage() {
        isFilterPageOpenMutableLiveData.value = false
    }

    fun onViewClickCategoryFilterChip(option: Option, isSelected: Boolean) {
        resetSortFilterIfExclude(option)
        filterController.refreshMapParameter(queryParam)
        filter(option, isSelected)

        getFilter(
            needToOpenBottomSheet = false
        )
    }

    private fun resetSortFilterIfExclude(option: Option) {
        val isOptionKeyHasExclude = option.key.startsWith(OptionHelper.EXCLUDE_PREFIX)

        if (!isOptionKeyHasExclude) return

        queryParamMutable.remove(option.key)
        queryParamMutable.entries.retainAll { it.isNotFilterAndSortKey() }
        queryParamMutable[SearchApiConst.OB] = SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT
    }

    private fun resetSortFilterIfPminPmax(option: Option) {
        val isOptionKeyHasPminPmax = option.key == SearchApiConst.PMIN || option.key == SearchApiConst.PMAX

        if (!isOptionKeyHasPminPmax) return

        queryParamMutable.remove(SearchApiConst.PMIN)
        queryParamMutable.remove(SearchApiConst.PMAX)
    }

    fun onViewApplySortFilter(applySortFilterModel: SortFilterBottomSheet.ApplySortFilterModel) {
        filterController.refreshMapParameter(applySortFilterModel.mapParameter)
        refreshQueryParamFromFilterController()

        onViewReloadPage(
            needToResetQueryParams = false
        )
    }

    fun onViewGetProductCount(mapParameter: Map<String, String>) {
        getProductCountUseCase.cancelJobs()
        getProductCountUseCase.execute(
            ::onGetProductCountSuccess,
            ::onGetProductCountFailed,
            createGetProductCountRequestParams(mapParameter)
        )
    }

    private fun createGetProductCountRequestParams(
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

    private fun onGetProductCountSuccess(countText: String) {
        productCountAfterFilterMutableLiveData.value = countText
    }

    private fun onGetProductCountFailed(throwable: Throwable) {
        onGetProductCountSuccess("0")
    }

    fun onViewGetProductCount(option: Option) {
        val queryParamWithoutOption = queryParam.toMutableMap().apply { removeOption(option) }
        val mapParameter = queryParamWithoutOption + mapOf(option.key to option.value)
        onViewGetProductCount(mapParameter)
    }

    private fun MutableMap<String, String>.removeOption(option: Option) {
        remove(option.key)
        remove(OptionHelper.getKeyRemoveExclude(option))
    }

    fun onViewApplyFilterFromCategoryChooser(chosenCategoryFilter: Option) {
        onViewDismissL3FilterPage()
        removeAllCategoryFilter(chosenCategoryFilter)
        filter(chosenCategoryFilter, true)

        getFilter(
            needToOpenBottomSheet = false
        )
    }

    private fun removeAllCategoryFilter(chosenCategoryFilter: Option) {
        queryParamMutable.removeOption(chosenCategoryFilter)

        filterController.refreshMapParameter(queryParam)
    }

    fun onViewDismissL3FilterPage() {
        isL3FilterPageOpenMutableLiveData.value = null
    }

    fun onViewResumed() {
        refreshMiniCart()
        updateToolbarNotification()

        val isChooseAddressUpdated = getIsChooseAddressUpdated()
        if (isChooseAddressUpdated) {
            onViewReloadPage()
        }
    }

    fun refreshMiniCart() {
        val shopId = shopIdLiveData.value ?: ""
        if (!shopId.isValidId()) return

        getMiniCartListSimplifiedUseCase.cancelJobs()
        getMiniCartListSimplifiedUseCase.setParams(listOf(shopId), miniCartSource)
        getMiniCartListSimplifiedUseCase.execute(
            ::onViewUpdateCartItems,
            ::onGetMiniCartDataFailed
        )
    }

    private fun String.isValidId() = this.isNotEmpty() && this != "0"

    fun onViewUpdateCartItems(miniCartSimplifiedData: MiniCartSimplifiedData) {
        updateMiniCartWidgetData(miniCartSimplifiedData)

        viewModelScope.launchCatchError(
            block = { updateMiniCartInBackground(miniCartSimplifiedData) },
            onError = { }
        )
    }

    private fun updateMiniCartWidgetData(miniCartSimplifiedData: MiniCartSimplifiedData) {
        val outOfCoverage = chooseAddressData.isOutOfCoverage()
        val showMiniCart = miniCartSimplifiedData.isShowMiniCartWidget
        val isShowMiniCartWidget = showMiniCart && !outOfCoverage
        miniCartWidgetMutableLiveData.value = miniCartSimplifiedData.copy(isShowMiniCartWidget = isShowMiniCartWidget)
        isShowMiniCartMutableLiveData.value = isShowMiniCartWidget
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

            updatedVisitableIndicesMutableLiveData.postValue(updatedProductIndices)
        }
    }

    private fun updateQuantityInVisitable(
        visitable: Visitable<*>,
        index: Int,
        updatedProductIndices: MutableList<Int>
    ) {
        when (visitable) {
            is ProductItemDataView ->
                updateProductItemQuantity(index, visitable, updatedProductIndices)
            is TokoNowAdsCarouselUiModel ->
                updateAdsCarouselQuantity(visitable, index)
            is BroadMatchDataView ->
                updateBroadMatchQuantities(visitable, index, updatedProductIndices)
        }
    }

    private fun updateBroadMatchQuantities(
        broadMatchDataView: BroadMatchDataView,
        index: Int,
        updatedProductIndices: MutableList<Int>
    ) {
        broadMatchDataView.broadMatchItemModelList.forEach { broadMatchItemDataView ->
            val productCardQuantity = broadMatchItemDataView.productCardModel.orderQuantity
            val miniCartQuantity = cartService.getProductQuantity(broadMatchItemDataView.productCardModel.productId)

            if (productCardQuantity != miniCartQuantity) {
                broadMatchItemDataView.productCardModel = broadMatchItemDataView.productCardModel.copy(orderQuantity = miniCartQuantity)

                if (!updatedProductIndices.contains(index)) {
                    updatedProductIndices.add(index)
                }
            }
        }
    }

    private fun updateProductItemQuantity(
        index: Int,
        productItem: ProductItemDataView,
        updatedProductIndices: MutableList<Int>
    ) {
        val productId = productItem.productCardModel.productId
        val parentProductId = productItem.parentId
        val quantity = cartService.getProductQuantity(productId, parentProductId)

        productItem.productCardModel = productItem.productCardModel.copy(orderQuantity = quantity)
        updatedProductIndices.add(index)
    }

    private fun onGetMiniCartDataFailed(throwable: Throwable) {
        updateMiniCartVisibility(false)
    }

    private fun updateMiniCartVisibility(isVisible: Boolean) {
        isShowMiniCartMutableLiveData.value = isVisible
    }

    private fun getIsChooseAddressUpdated(): Boolean {
        return chooseAddressWrapper.isChooseAddressUpdated(chooseAddressData)
    }

    fun onViewATCProductNonVariant(
        productItem: ProductItemDataView,
        quantity: Int
    ) {
        val productId = productItem.productCardModel.productId
        val shopId = productItem.shop.id
        val currentQuantity = productItem.productCardModel.orderQuantity
        val stock = productItem.productCardModel.availableStock
        val isVariant = productItem.productCardModel.isVariant

        cartService.handleCart(
            cartProductItem = CartProductItem(productId, shopId, currentQuantity),
            quantity = quantity,
            onSuccessAddToCart = {
                checkAtcAffiliateCookie(productId, shopId, quantity, stock, isVariant)
                addToCartMessageSuccess(it.errorMessage.joinToString(separator = ", "))
                sendAddToCartTracking(quantity, it.data.cartId, productItem)
                onAddToCartSuccess(productItem, it.data.quantity)
                updateToolbarNotification()
            },
            onSuccessUpdateCart = {
                checkAtcAffiliateCookie(productId, shopId, quantity, stock, isVariant)
                sendTrackingUpdateQuantity(quantity, productItem)
                onAddToCartSuccess(productItem, quantity)
                updateToolbarNotification()
            },
            onSuccessDeleteCart = {
                removeFromCartMessageSuccess(it.errorMessage.joinToString(separator = ", "))
                sendDeleteCartTracking(productItem)
                onAddToCartSuccess(productItem, 0)
                updateToolbarNotification()
            },
            onError = ::onAddToCartFailed,
            handleCartEventNonLogin = {
                handleAddToCartEventNonLogin(visitableList.indexOf(productItem))
            }
        )
    }

    private fun sendAddToCartTracking(quantity: Int, cartId: String, productItem: ProductItemDataView) {
        addToCartTrackingMutableLiveData.value = Triple(quantity, cartId, productItem)
    }

    private fun addToCartMessageSuccess(successMessage: String) {
        successAddToCartMessageMutableLiveData.value = successMessage
    }

    private fun removeFromCartMessageSuccess(successMessage: String) {
        successRemoveFromCartMessageMutableLiveData.value = successMessage
    }

    private fun onAddToCartSuccess(productItem: ProductItemDataView, quantity: Int) {
        updateProductNonVariantQuantity(productItem, quantity)
        refreshMiniCart()
    }

    private fun updateProductNonVariantQuantity(
        productItem: ProductItemDataView,
        quantity: Int
    ) {
        productItem.productCardModel = productItem.productCardModel.copy(orderQuantity = quantity)
    }

    private fun onAddToCartFailed(throwable: Throwable) {
        errorATCMessageMutableLiveData.value = throwable.message ?: ""
    }

    private fun sendTrackingUpdateQuantity(newQuantity: Int, productItem: ProductItemDataView) {
        if (productItem.productCardModel.orderQuantity > newQuantity) {
            decreaseQtyTrackingMutableLiveData.value = productItem.productCardModel.productId
        } else if (productItem.productCardModel.orderQuantity < newQuantity) {
            increaseQtyTrackingMutableLiveData.value = productItem.productCardModel.productId
        }
    }

    private fun sendDeleteCartTracking(productItem: ProductItemDataView) {
        deleteCartTrackingMutableLiveData.value = productItem.productCardModel.productId
    }

    private fun handleAddToCartEventNonLogin(updatedVisitableIndex: Int) {
        routeApplinkMutableLiveData.postValue(ApplinkConst.LOGIN)
        updatedVisitableIndicesMutableLiveData.postValue(listOf(updatedVisitableIndex))
    }

    fun onLocalizingAddressSelected() {
        refreshMiniCart()
        onViewReloadPage()
    }

    fun onViewRemoveFilter(option: Option) {
        resetSortFilterIfExclude(option)
        resetSortFilterIfPminPmax(option)
        filterController.refreshMapParameter(queryParam)
        filter(option, false)

        getFilter(
            needToOpenBottomSheet = false
        )
    }

    fun needToShowOnBoardBottomSheet(has20mBottomSheetBeenShown: Boolean): Boolean {
        return chooseAddressData.run {
            val is20mServiceType = service_type == ServiceType.NOW_15M
            is20mServiceType && !has20mBottomSheetBeenShown && warehouse_id.isValidId()
        }
    }

    fun setUserPreference(serviceType: String) {
        launchCatchError(
            block = {
                chooseAddressData.let {
                    val setUserPreference = setUserPreferenceUseCase.execute(it, serviceType)
                    setUserPreferenceMutableLiveData.postValue(Success(setUserPreference))
                }
            },
            onError = {
                setUserPreferenceMutableLiveData.postValue(Fail(it))
            }
        )
    }

    fun switchService() {
        chooseAddressData.let {
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

    fun createProductRecommendationRequestParam(
        pageName: String
    ) = GetRecommendationRequestParam(
        pageName = pageName,
        categoryIds = getRecomCategoryId(pageName),
        xSource = RecomPageConstant.RECOM_WIDGET,
        isTokonow = true,
        pageNumber = RecomPageConstant.PAGE_NUMBER_RECOM_WIDGET,
        keywords = getRecomKeywords(),
        xDevice = SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE
    )

    fun updateToolbarNotification() {
        updateToolbarNotificationLiveData.postValue(true)
    }

    private fun getUniqueId() =
        if (userSession.isLoggedIn) {
            AuthHelper.getMD5Hash(userSession.userId)
        } else {
            AuthHelper.getMD5Hash(userSession.deviceId)
        }

    protected class HeaderDataView(
        val title: String = "",
        val aceSearchProductHeader: AceSearchProductModel.SearchProductHeader = AceSearchProductModel.SearchProductHeader(),
        categoryFilterDataValue: DataValue = DataValue(),
        quickFilterDataValue: DataValue = DataValue(),
        val bannerChannel: DynamicHomeChannelCommon.Channels = DynamicHomeChannelCommon.Channels(),
        val targetedTicker: GetTargetedTickerResponse = GetTargetedTickerResponse()
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

            if (isCategoryFilter) {
                option.key = OptionHelper.EXCLUDE_PREFIX + option.key
            }
        }

        private fun isInCategoryFilter(optionToCheck: Option): Boolean {
            val categoryOptionList = categoryFilterDataValue.filter.map { it.options }.flatten()

            return categoryOptionList.any {
                it.key.removePrefix(OptionHelper.EXCLUDE_PREFIX) == optionToCheck.key &&
                    it.value == optionToCheck.value
            }
        }
    }

    fun isProductFeedbackLoopVisible(): Boolean {
        return feedbackFieldToggle && isFeedbackFieldVisible
    }

    fun updateWishlistStatus(
        productId: String,
        hasBeenWishlist: Boolean
    ) {
        launch {
            val index = visitableList.updateWishlistStatus(
                productId = productId,
                hasBeenWishlist = hasBeenWishlist
            )
            if (index != null) {
                updatedVisitableIndicesMutableLiveData.postValue(listOf(index))
            }
        }
    }

    fun getTranslationYHeaderBackground(dy: Int): Float {
        headerYCoordinate += dy
        return if (-headerYCoordinate > DEFAULT_HEADER_Y_COORDINATE) {
            headerYCoordinate = DEFAULT_HEADER_Y_COORDINATE
            headerYCoordinate
        } else {
            -headerYCoordinate
        }
    }

    fun createAffiliateLink(url: String): String {
        return affiliateService.createAffiliateLink(url)
    }

    protected data class ContentDataView(
        val aceSearchProductData: AceSearchProductModel.SearchProductData = AceSearchProductModel.SearchProductData(),
        val productAds: GetProductAdsResponse.ProductAdsResponse = GetProductAdsResponse.ProductAdsResponse()
    )

    private fun initAffiliateCookie(affiliateUuid: String = "", affiliateChannel: String = "") {
        launchCatchError(block = {
            affiliateService.initAffiliateCookie(
                affiliateUuid,
                affiliateChannel
            )
        }) {
        }
    }

    private fun checkAtcAffiliateCookie(
        productId: String,
        shopId: String,
        quantity: Int,
        stock: Int,
        isVariant: Boolean
    ) {
        val miniCartItem = cartService.getMiniCartItem(productId)
        val currentQuantity = miniCartItem?.quantity.orZero()
        val data = NowAffiliateAtcData(productId, shopId, stock, isVariant, quantity, currentQuantity)

        launchCatchError(block = {
            affiliateService.checkAtcAffiliateCookie(data)
        }) {
        }
    }

    fun getRows(): String = if (getPaginationExperiment()) ConstantKey.EXPERIMENT_ROWS else ConstantKey.DEFAULT_ROWS

    private fun getPaginationExperiment(): Boolean {
        val experiment = remoteConfig.getString(
            RollenceKey.TOKOPEDIA_NOW_PAGINATION,
            ConstantKey.EXPERIMENT_DISABLED
        )
        return experiment == ConstantKey.EXPERIMENT_ENABLED
    }

    private fun loadFirstPage() {
        getSearchFirstPageUseCase.cancelJobs()
        getSearchFirstPageUseCase.execute(
            ::onGetSearchFirstPageSuccess,
            ::onGetFirstPageError,
            createRequestParams()
        )
    }

    private fun createTitleDataView(headerDataView: HeaderDataView): TitleDataView {
        val titleType = if (query.isEmpty()) AllProductTitle else SearchTitle
        val hasSeeAllCategoryButton = query.isEmpty()

        return TitleDataView(
            titleType = titleType,
            hasSeeAllCategoryButton = hasSeeAllCategoryButton,
            chooseAddressData = chooseAddressData
        )
    }

    private fun postProcessHeaderList(headerList: MutableList<Visitable<*>>) {
        if (!shouldShowSuggestion()) return

        val suggestionDataViewIndex = determineSuggestionDataViewIndex(headerList)

        headerList.updateSuggestionDataView(suggestionModel, suggestionDataViewIndex)
    }

    private fun createVisitableListWithEmptyProduct(
        violation: AceSearchProductModel.Violation
    ) {
        if (isShowBroadMatch()) {
            createVisitableListWithEmptyProductBroadmatch()
        } else {
            val activeFilterList = filterController.getActiveFilterOptionList()
            val newActiveFilterList = activeFilterList.filter { queryParamMutable.containsValue(it.value) }
            isEmptyResult = true

            visitableList.add(chooseAddressDataView)
            visitableList.add(
                TokoNowEmptyStateNoResultUiModel(
                    activeFilterList = newActiveFilterList,
                    excludeFilter = excludeFilter,
                    defaultTitle = violation.headerText,
                    defaultDescription = violation.descriptionText,
                    defaultImage = violation.imageUrl,
                    defaultTextPrimaryButton = violation.buttonText,
                    defaultUrlPrimaryButton = violation.ctaUrl
                )
            )
            visitableList.add(
                TokoNowProductRecommendationUiModel(
                    requestParam = createProductRecommendationRequestParam(
                        pageName = RecomPageConstant.TOKONOW_NO_RESULT
                    ),
                    tickerPageSource = tickerPageSource
                )
            )
            if (feedbackFieldToggle) {
                _feedbackLoopTrackingMutableLivedata.value = Pair(
                    first = chooseAddressData.warehouse_id,
                    second = false
                )
                isFeedbackFieldVisible = true
                visitableList.add(TokoNowFeedbackWidgetUiModel())
            } else {
                isFeedbackFieldVisible = false
            }
        }
    }

    private fun getKeywordForGeneralSearchTracking() = query

    private fun executeLoadMore() {
        getSearchLoadMorePageUseCase.cancelJobs()
        getSearchLoadMorePageUseCase.execute(
            ::onGetSearchLoadMorePageSuccess,
            ::onGetSearchLoadMorePageError,
            createRequestParams()
        )
    }

    private fun createFooterVisitableList(): List<Visitable<SearchTypeFactory>> {
        val broadMatchVisitableList = createBroadMatchVisitableList()
        return broadMatchVisitableList + if (serviceType == NOW_15M) {
            listOf()
        } else {
            listOf(CTATokopediaNowHomeDataView())
        }
    }

    private fun getRecomKeywords() = listOf(query)

    private fun getRecomCategoryId(pageName: String): List<String> = listOf(recommendationCategoryId)

    private fun createProductAdsParam(): MutableMap<String?, Any> {
        val query = queryParamMutable[SearchApiConst.Q].orEmpty()

        return GetProductAdsParam(
            query = query,
            src = GetProductAdsParam.SRC_SEARCH_TOKONOW,
            userId = userSession.userId,
            addressData = chooseAddressData
        ).generateQueryParams()
    }

    private fun onGetSearchFirstPageSuccess(searchModel: SearchModel) {
        val searchProduct = searchModel.searchProduct
        responseCode = searchModel.getResponseCode()
        suggestionModel = searchModel.getSuggestion()
        related = searchModel.getRelated()

        val searchProductHeader = searchProduct.header
        recommendationCategoryId = searchProductHeader.meta.categoryId

        val headerDataView = HeaderDataView(
            title = "",
            aceSearchProductHeader = searchProductHeader,
            categoryFilterDataValue = searchModel.categoryFilter,
            quickFilterDataValue = searchModel.quickFilter,
            bannerChannel = searchModel.bannerChannel,
            targetedTicker = searchModel.targetedTicker
        )

        val contentDataView = ContentDataView(
            aceSearchProductData = searchProduct.data,
            productAds = searchModel.productAds
        )

        val isActive = searchModel.feedbackFieldToggle.tokonowFeedbackFieldToggle.data.isActive

        onGetFirstPageSuccess(headerDataView, contentDataView, searchProduct, isActive)
    }

    private fun shouldShowSuggestion() = showSuggestionResponseCodeList.contains(responseCode)

    private fun determineSuggestionDataViewIndex(headerList: List<Visitable<*>>): Int {
        val quickFilterIndex = headerList.indexOfFirst { it is QuickFilterDataView }

        return quickFilterIndex + 1
    }

    private fun createBroadMatchVisitableList(): List<Visitable<SearchTypeFactory>> {
        val broadMatchVisitableList = mutableListOf<Visitable<SearchTypeFactory>>()

        if (!isShowBroadMatch()) return broadMatchVisitableList

        broadMatchVisitableList.addSuggestionDataView(
            suggestionModel = suggestionModel
        )
        broadMatchVisitableList.addBroadMatchDataView(
            related = related,
            cartService = cartService,
            hasBlockedAddToCart = hasBlockedAddToCart
        )

        return broadMatchVisitableList
    }

    private fun isShowBroadMatch() =
        showBroadMatchResponseCodeList.contains(responseCode)

    private fun createVisitableListWithEmptyProductBroadmatch() {
        visitableList.add(chooseAddressDataView)
        visitableList.addAll(createBroadMatchVisitableList())
    }

    private fun onGetSearchLoadMorePageSuccess(searchModel: SearchModel) {
        val contentDataView = ContentDataView(
            aceSearchProductData = searchModel.searchProduct.data,
            productAds = searchModel.productAds
        )
        onGetLoadMorePageSuccess(contentDataView)
    }

    private fun onGetSearchLoadMorePageError(throwable: Throwable) { /* nothing to do */ }

    private fun sendAddToCartBroadMatchItemTracking(
        quantity: Int,
        addToCartDataModel: AddToCartDataModel,
        broadMatchItem: ProductCardCompactCarouselItemUiModel
    ) {
        addToCartBroadMatchTrackingMutableLiveData.value = Triple(quantity, addToCartDataModel.data.cartId, broadMatchItem)
    }

    fun onViewATCBroadMatchItem(
        broadMatchItem: ProductCardCompactCarouselItemUiModel,
        quantity: Int,
        broadMatchIndex: Int
    ) {
        val productId = broadMatchItem.productCardModel.productId
        val shopId = broadMatchItem.shopId
        val currentQuantity = broadMatchItem.productCardModel.orderQuantity

        cartService.handleCart(
            cartProductItem = CartProductItem(productId, shopId, currentQuantity),
            quantity = quantity,
            onSuccessAddToCart = {
                sendAddToCartBroadMatchItemTracking(quantity, it, broadMatchItem)
                addToCartMessageSuccess(it.errorMessage.joinToString(separator = ", "))
                updateToolbarNotification()
                refreshMiniCart()
            },
            onSuccessUpdateCart = {
                updateToolbarNotification()
                refreshMiniCart()
            },
            onSuccessDeleteCart = {
                removeFromCartMessageSuccess(it.errorMessage.joinToString(separator = ", "))
                updateToolbarNotification()
                refreshMiniCart()
            },
            onError = ::onAddToCartFailed,
            handleCartEventNonLogin = {
                handleAddToCartEventNonLogin(broadMatchIndex)
            }
        )
    }
}
