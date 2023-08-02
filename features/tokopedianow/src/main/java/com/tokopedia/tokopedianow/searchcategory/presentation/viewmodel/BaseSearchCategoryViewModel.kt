package com.tokopedia.tokopedianow.searchcategory.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.ApplinkConst
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
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.WAREHOUSES
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
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant.OOC_TOKONOW
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant.PAGE_NUMBER_RECOM_WIDGET
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant.RECOM_WIDGET
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant.TOKONOW_NO_RESULT
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_TOKONOW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_PHYSICAL_GOODS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.constant.ServiceType
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.domain.mapper.ProductAdsMapper.mapProductAdsCarousel
import com.tokopedia.tokopedianow.common.domain.mapper.TickerMapper
import com.tokopedia.tokopedianow.common.domain.model.GetProductAdsResponse.ProductAdsResponse
import com.tokopedia.tokopedianow.common.domain.model.GetTargetedTickerResponse
import com.tokopedia.tokopedianow.common.domain.mapper.AddressMapper.mapToWarehouses
import com.tokopedia.tokopedianow.common.domain.model.SetUserPreference
import com.tokopedia.tokopedianow.common.domain.param.GetProductAdsParam
import com.tokopedia.tokopedianow.common.domain.usecase.SetUserPreferenceUseCase
import com.tokopedia.tokopedianow.common.model.NowAffiliateAtcData
import com.tokopedia.tokopedianow.common.model.TokoNowAdsCarouselUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateNoResultUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateOocUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationOocUiModel
import com.tokopedia.tokopedianow.common.model.oldrepurchase.TokoNowRepurchaseUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowTickerUiModel
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.home.domain.mapper.oldrepurchase.HomeRepurchaseMapper
import com.tokopedia.tokopedianow.home.domain.model.GetRepurchaseResponse.RepurchaseData
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.GENERAL_SEARCH
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Category.TOP_NAV
import com.tokopedia.tokopedianow.search.presentation.model.BroadMatchDataView
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.LOCAL_SEARCH
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.NONE
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.PAGESOURCE
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.RELATEDKEYWORD
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.TOKOPEDIA_NOW
import com.tokopedia.tokopedianow.searchcategory.cartservice.CartProductItem
import com.tokopedia.tokopedianow.searchcategory.cartservice.CartService
import com.tokopedia.tokopedianow.searchcategory.data.model.QuerySafeModel
import com.tokopedia.tokopedianow.searchcategory.domain.mapper.ProductItemMapper.mapResponseToProductItem
import com.tokopedia.tokopedianow.searchcategory.domain.mapper.VisitableMapper.updateWishlistStatus
import com.tokopedia.tokopedianow.searchcategory.domain.mapper.mapChooseAddressToQuerySafeModel
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel.Product
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel.SearchProduct
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel.SearchProductData
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel.SearchProductHeader
import com.tokopedia.tokopedianow.searchcategory.presentation.model.BannerDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.CategoryFilterDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.CategoryFilterItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ChooseAddressDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductCountDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProgressBarDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.SortFilterItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.SwitcherWidgetDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.TitleDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.TokoNowFeedbackWidgetUiModel
import com.tokopedia.tokopedianow.searchcategory.utils.ChooseAddressWrapper
import com.tokopedia.tokopedianow.searchcategory.utils.PRODUCT_ADS_PARAMS
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseSearchCategoryViewModel(
    private val baseDispatcher: CoroutineDispatchers,
    queryParamMap: Map<String, String>,
    protected val getFilterUseCase: UseCase<DynamicFilterModel>,
    protected val getProductCountUseCase: UseCase<String>,
    protected val getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase,
    protected val cartService: CartService,
    private val getShopAndWarehouseUseCase: GetChosenAddressWarehouseLocUseCase,
    protected val setUserPreferenceUseCase: SetUserPreferenceUseCase,
    protected val chooseAddressWrapper: ChooseAddressWrapper,
    private val affiliateService: NowAffiliateService,
    protected val userSession: UserSessionInterface
) : BaseViewModel(baseDispatcher.io) {
    companion object {
        private const val MIN_PRODUCT_COUNT = 6
        private const val DEFAULT_HEADER_Y_COORDINATE = 0f
    }

    protected var chooseAddressDataView = ChooseAddressDataView()
    protected val loadingMoreModel = LoadingMoreModel()
    protected val visitableList = mutableListOf<Visitable<*>>()
    protected val queryParamMutable = queryParamMap.toMutableMap()
    protected var totalData = 0
    protected var chooseAddressData: LocalCacheModel? = null
    protected var hasBlockedAddToCart = false

    private var headerYCoordinate = 0f
    private val filterController = FilterController()
    private var totalFetchedData = 0
    private var nextPage = 1
    private var currentProductPosition: Int = 1
    protected var feedbackFieldToggle = false
    private var isFeedbackFieldVisible = false

    val queryParam: Map<String, String> = queryParamMutable
    val hasGlobalMenu: Boolean
    var warehouseId = ""
        private set
    var autoCompleteApplink = ""
        private set
    var serviceType = ""
        private set

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

    private val addToCartRepurchaseWidgetTrackingMutableLiveData =
        SingleLiveEvent<Triple<Int, String, TokoNowProductCardUiModel>>()

    val addToCartRepurchaseWidgetTrackingLiveData:
        LiveData<Triple<Int, String, TokoNowProductCardUiModel>> =
            addToCartRepurchaseWidgetTrackingMutableLiveData

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

    private val needToUpdateProductRecommendationMutableLiveData = MutableLiveData<Boolean>()
    val needToUpdateProductRecommendationLiveData: LiveData<Boolean> = needToUpdateProductRecommendationMutableLiveData

    private val blockAddToCartMutableLiveData = MutableLiveData<Unit>()
    val blockAddToCartLiveData: LiveData<Unit> = blockAddToCartMutableLiveData

    private val updateAdsCarouselMutableLiveData = MutableLiveData<Pair<Int, TokoNowAdsCarouselUiModel>>()
    val updateAdsCarouselLiveData: LiveData<Pair<Int, TokoNowAdsCarouselUiModel>> = updateAdsCarouselMutableLiveData

    var isEmptyResult: Boolean = false

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

    abstract val tickerPageSource: String

    private fun isABTestNavigationRevamp() = true

    open fun onViewCreated(source: MiniCartSource? = null) {
        initAffiliateCookie()
        processLoadDataPage(source)
    }

    private fun processLoadDataPage(source: MiniCartSource? = null) {
        val shopId = chooseAddressData?.shop_id.orEmpty()
        val warehouseId = chooseAddressData?.warehouse_id.orEmpty()
        val serviceType = chooseAddressData?.service_type.orEmpty()
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
        val serviceType = tokonowData.serviceType

        processLoadDataWithShopId(shopId, warehouseId, serviceType)

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

    private fun sendOutOfCoverageTrackingEvent() {
        oocOpenScreenTrackingMutableEvent.value = true
    }

    private fun createVisitableListWithOutOfCoverageView() {
        visitableList.clear()
        visitableList.add(chooseAddressDataView)
        visitableList.add(
            TokoNowEmptyStateOocUiModel(
                hostSource = DEFAULT_VALUE_SOURCE_SEARCH,
                serviceType = serviceType
            )
        )
        visitableList.add(
            TokoNowProductRecommendationOocUiModel(
                pageName = OOC_TOKONOW,
                isBindWithPageName = true
            )
        )
    }

    protected open fun onGetShopAndWarehouseFailed(throwable: Throwable) {
    }

    protected abstract fun loadFirstPage()

    protected open fun createRequestParams(): RequestParams {
        val tokonowQueryParam = createTokonowQueryParams()
        val productAdsParam = createProductAdsParam()
            .generateQueryParams()

        val requestParams = RequestParams.create()
        requestParams.putObject(TOKONOW_QUERY_PARAMS, tokonowQueryParam)
        requestParams.putObject(PRODUCT_ADS_PARAMS, productAdsParam)
        requestParams.putObject(USER_WAREHOUSE_ID, warehouseId)

        return requestParams
    }

    protected open fun createTokonowQueryParams(): MutableMap<String, Any> {
        val tokonowQueryParam = mutableMapOf<String, Any>()

        appendMandatoryParams(tokonowQueryParam)
        appendPaginationParam(tokonowQueryParam)
        appendQueryParam(tokonowQueryParam)

        return tokonowQueryParam
    }

    protected open fun createProductAdsParam(): GetProductAdsParam = GetProductAdsParam()

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

        if (chooseAddressData.city_id.isNotEmpty()) {
            tokonowQueryParam[USER_CITY_ID] = chooseAddressData.city_id
        }
        if (chooseAddressData.address_id.isNotEmpty()) {
            tokonowQueryParam[USER_ADDRESS_ID] = chooseAddressData.address_id
        }
        if (chooseAddressData.district_id.isNotEmpty()) {
            tokonowQueryParam[USER_DISTRICT_ID] = chooseAddressData.district_id
        }
        if (chooseAddressData.lat.isNotEmpty()) {
            tokonowQueryParam[USER_LAT] = chooseAddressData.lat
        }
        if (chooseAddressData.long.isNotEmpty()) {
            tokonowQueryParam[USER_LONG] = chooseAddressData.long
        }
        if (chooseAddressData.postal_code.isNotEmpty()) {
            tokonowQueryParam[USER_POST_CODE] = chooseAddressData.postal_code
        }
        if (chooseAddressData.warehouses.isNotEmpty()) {
            tokonowQueryParam[WAREHOUSES] = mapToWarehouses(chooseAddressData)
        }
    }

    protected open fun appendUniqueIdParam(tokonowQueryParam: MutableMap<String, Any>) {
        tokonowQueryParam[SearchApiConst.UNIQUE_ID] = getUniqueId()
    }

    protected open fun appendPaginationParam(tokonowQueryParam: MutableMap<String, Any>) {
        tokonowQueryParam[SearchApiConst.PAGE] = nextPage
        tokonowQueryParam[SearchApiConst.USE_PAGE] = true
        tokonowQueryParam[SearchApiConst.ROWS] = SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS_PROFILE
    }

    private fun appendQueryParam(tokonowQueryParam: MutableMap<String, Any>) {
        tokonowQueryParam.putAll(FilterHelper.createParamsWithoutExcludes(queryParam))
    }

    protected fun onGetFirstPageSuccess(
        headerDataView: HeaderDataView,
        contentDataView: ContentDataView,
        searchProduct: SearchProduct,
        feedbackFieldIsActive: Boolean = false
    ) {
        totalData = headerDataView.aceSearchProductHeader.totalData
        totalFetchedData += contentDataView.aceSearchProductData.productList.size
        autoCompleteApplink = contentDataView.aceSearchProductData.autocompleteApplink
        currentProductPosition = 1
        feedbackFieldToggle = feedbackFieldIsActive

        val isEmptyProductList = contentDataView.aceSearchProductData.productList.isEmpty()

        if (dynamicFilterModelLiveData.value == null) {
            initFilterController(headerDataView)
        }
        createVisitableListFirstPage(headerDataView, contentDataView, isEmptyProductList)
        processEmptyState(isEmptyProductList)
        if (getKeywordForGeneralSearchTracking().isNotEmpty()) {
            sendGeneralSearchTracking(searchProduct)
        }
        updateViewForFirstPage(isEmptyProductList)

        querySafeMutableLiveData.value = chooseAddressData.mapChooseAddressToQuerySafeModel(
            isQuerySafe = searchProduct.data.isQuerySafe
        )
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
        isEmptyProductList: Boolean
    ) {
        visitableList.clear()

        if (isEmptyProductList) {
            createVisitableListWithEmptyProduct()
        } else {
            createVisitableListWithProduct(headerDataView, contentDataView)
        }
    }

    protected open fun createVisitableListWithEmptyProduct() {
        val activeFilterList = filterController.getActiveFilterOptionList()
        isEmptyResult = true

        visitableList.add(chooseAddressDataView)
        visitableList.add(TokoNowEmptyStateNoResultUiModel(activeFilterList = activeFilterList))
        visitableList.add(
            TokoNowProductRecommendationUiModel(
                requestParam = createProductRecommendationRequestParam(
                    pageName = TOKONOW_NO_RESULT
                ),
                tickerPageSource = tickerPageSource
            )
        )
        if (feedbackFieldToggle) {
            _feedbackLoopTrackingMutableLivedata.value = Pair(
                first = chooseAddressData?.warehouse_id.orEmpty(),
                second = false
            )
            isFeedbackFieldVisible = true
            visitableList.add(TokoNowFeedbackWidgetUiModel())
        } else {
            isFeedbackFieldVisible = false
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
                first = chooseAddressData?.warehouse_id.orEmpty(),
                second = true
            )
            isFeedbackFieldVisible = true
            visitableList.add(TokoNowFeedbackWidgetUiModel(true))
        } else {
            isFeedbackFieldVisible = false
        }
        visitableList.addFooter()
    }

    protected open fun createHeaderVisitableList(headerDataView: HeaderDataView): List<Visitable<*>> {
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

    protected abstract fun createTitleDataView(headerDataView: HeaderDataView): TitleDataView

    protected open fun processCategoryFilter(
        headerList: MutableList<Visitable<*>>,
        categoryFilterDataValue: DataValue
    ) {
        val categoryFilter = categoryFilterDataValue.filter.getOrNull(0)
        categoryFilter ?: return

        if (isShowCategoryFilter(categoryFilter)) {
            headerList.add(CategoryFilterDataView(createCategoryFilterItemList(categoryFilter)))
        }
    }

    protected open fun isShowCategoryFilter(categoryFilter: Filter) =
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

        if (filter.options.size == 1) {
            val option = filter.options.firstOrNull() ?: Option()
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

    open fun onViewReloadPage(
        isDynamicFilterRemoved: Boolean = true
    ) {
        if (isDynamicFilterRemoved) {
            dynamicFilterModelMutableLiveData.value = null
        }
        totalData = 0
        totalFetchedData = 0
        nextPage = 1
        chooseAddressData = chooseAddressWrapper.getChooseAddressData()
        chooseAddressDataView = ChooseAddressDataView(chooseAddressData)
        isFeedbackFieldVisible = false
        showLoading()
        processLoadDataPage()
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

    protected open fun openL3FilterPage(selectedFilter: Filter) {
        if (isL3FilterPageOpenMutableLiveData.value != null) return

        isL3FilterPageOpenMutableLiveData.value = selectedFilter
    }

    protected open fun postProcessHeaderList(headerList: MutableList<Visitable<*>>) {
    }

    protected open fun createContentVisitableList(
        contentDataView: ContentDataView
    ): List<Visitable<*>> {
        val contentVisitableList = mutableListOf<Visitable<*>>()
        val productList = contentDataView
            .aceSearchProductData
            .productList
        val repurchaseWidget = contentDataView.repurchaseWidget
        val productAds = contentDataView.productAds

        addProductAds(contentVisitableList, productAds)
        addProductList(contentVisitableList, productList)
        addRepurchaseWidget(contentVisitableList, repurchaseWidget, productList)

        return contentVisitableList
    }

    private fun addProductAds(
        contentVisitableList: MutableList<Visitable<*>>,
        response: ProductAdsResponse
    ) {
        if(response.productList.isNotEmpty()) {
            contentVisitableList.add(mapProductAdsCarousel(
                response,
                miniCartWidgetLiveData.value,
                hasBlockedAddToCart
            ))
        }
    }

    protected open fun addProductList(
        contentVisitableList: MutableList<Visitable<*>>,
        productList: List<Product>
    ) {
        val productListDataView = productList.mapIndexed { index, product ->
            mapResponseToProductItem(
                index = index,
                product = product,
                cartService = cartService,
                hasBlockedAddToCart = hasBlockedAddToCart
            )
        }
        contentVisitableList.addAll(productListDataView)
    }

    protected open fun addRepurchaseWidget(
        contentVisitableList: MutableList<Visitable<*>>,
        repurchaseWidget: RepurchaseData,
        productList: List<Product>
    ) {
        val canShowRepurchaseWidget =
            repurchaseWidget.products.isNotEmpty() &&
                productList.size > REPURCHASE_WIDGET_POSITION

        if (canShowRepurchaseWidget) {
            contentVisitableList.add(
                REPURCHASE_WIDGET_POSITION,
                createRepurchaseWidgetUIModel(repurchaseWidget)
            )
        }
    }

    private fun createRepurchaseWidgetUIModel(repurchaseWidget: RepurchaseData) =
        HomeRepurchaseMapper.mapToRepurchaseUiModel(
            TokoNowRepurchaseUiModel(
                id = "",
                title = "",
                productList = listOf(),
                state = TokoNowLayoutState.IDLE
            ),
            repurchaseWidget
        ).also {
            updateRepurchaseWidgetQuantity(it)
        }

    private fun updateRepurchaseWidgetQuantity(
        repurchaseUiModel: TokoNowRepurchaseUiModel,
        index: Int = -1,
        updatedProductIndices: MutableList<Int>? = null
    ) {
        repurchaseUiModel.productList.forEach { productUiModel ->
            productUiModel.product = createUpdatedRepurchaseWidgetQuantity(productUiModel)
        }

        updatedProductIndices?.add(index)
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

    private fun createUpdatedRepurchaseWidgetQuantity(
        repurchaseProduct: TokoNowProductCardUiModel
    ): ProductCardModel {
        val quantity = cartService.getProductQuantity(
            repurchaseProduct.productId,
            repurchaseProduct.parentId
        )

        val nonVariant = repurchaseProduct.product.nonVariant?.copy(quantity = quantity)
        val variant = repurchaseProduct.product.variant?.copy(quantity = quantity)

        return repurchaseProduct.product.copy(
            nonVariant = nonVariant,
            variant = variant
        )
    }

    private fun createProductCardCompactModel(
        product: ProductCardCompactCarouselItemUiModel,
    ): ProductCardCompactUiModel {
        val quantity = cartService.getProductQuantity(
            product.getProductId(),
            product.parentId,
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
            PAGESOURCE to pageSource
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

        needToUpdateProductRecommendationMutableLiveData.value = true
    }

    private fun clearVisitableListLiveData() {
        visitableListMutableLiveData.value = listOf()
    }

    private fun updateVisitableListLiveData() {
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

    fun removeProductRecommendationWidget() {
        visitableList.removeFirst { it is TokoNowProductRecommendationUiModel }

        updateVisitableListLiveData()
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
        filterController.refreshMapParameter(queryParam)
        filter(option, isSelected)
    }

    private fun resetSortFilterIfExclude(option: Option) {
        val isOptionKeyHasExclude = option.key.startsWith(OptionHelper.EXCLUDE_PREFIX)

        if (!isOptionKeyHasExclude) return

        queryParamMutable.remove(option.key)
        queryParamMutable.entries.retainAll { it.isNotFilterAndSortKey() }
        queryParamMutable[SearchApiConst.OB] = DEFAULT_VALUE_OF_PARAMETER_SORT
    }

    private fun resetSortFilterIfPminPmax(option: Option) {
        val isOptionKeyHasPminPmax = option.key == SearchApiConst.PMIN || option.key == SearchApiConst.PMAX

        if (!isOptionKeyHasPminPmax) return

        queryParamMutable.remove(SearchApiConst.PMIN)
        queryParamMutable.remove(SearchApiConst.PMAX)
    }

    open fun onViewApplySortFilter(applySortFilterModel: ApplySortFilterModel) {
        filterController.refreshMapParameter(applySortFilterModel.mapParameter)
        refreshQueryParamFromFilterController()

        onViewReloadPage(
            isDynamicFilterRemoved = false
        )
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
        updateToolbarNotification()

        val isChooseAddressUpdated = getIsChooseAddressUpdated()
        if (isChooseAddressUpdated) {
            onViewReloadPage()
        }
    }

    open fun refreshMiniCart() {
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
            val isShowMiniCartWidget = showMiniCart && !outOfCoverage
            miniCartWidgetMutableLiveData.value = miniCartSimplifiedData.copy(isShowMiniCartWidget = isShowMiniCartWidget)
            isShowMiniCartMutableLiveData.value = isShowMiniCartWidget
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

            updatedVisitableIndicesMutableLiveData.postValue(updatedProductIndices)
        }
    }

    protected open fun updateQuantityInVisitable(
        visitable: Visitable<*>,
        index: Int,
        updatedProductIndices: MutableList<Int>
    ) {
        when (visitable) {
            is ProductItemDataView ->
                updateProductItemQuantity(index, visitable, updatedProductIndices)
            is TokoNowRepurchaseUiModel ->
                updateRepurchaseWidgetQuantity(visitable, index, updatedProductIndices)
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
        return chooseAddressData?.let {
            chooseAddressWrapper.isChooseAddressUpdated(it)
        } ?: false
    }

    open fun onViewATCProductNonVariant(
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

    protected fun addToCartMessageSuccess(successMessage: String) {
        successAddToCartMessageMutableLiveData.value = successMessage
    }

    protected fun removeFromCartMessageSuccess(successMessage: String) {
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

    protected fun onAddToCartFailed(throwable: Throwable) {
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

    protected open fun handleAddToCartEventNonLogin(updatedVisitableIndex: Int) {
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
    }

    fun needToShowOnBoardBottomSheet(has20mBottomSheetBeenShown: Boolean): Boolean {
        chooseAddressData?.apply {
            val is20mServiceType = service_type == ServiceType.NOW_15M
            return is20mServiceType && !has20mBottomSheetBeenShown && warehouse_id.isValidId()
        }
        return false
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
            }
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

    open fun createProductRecommendationRequestParam(
        pageName: String
    ) = GetRecommendationRequestParam(
        pageName = pageName,
        categoryIds = getRecomCategoryId(pageName),
        xSource = RECOM_WIDGET,
        isTokonow = true,
        pageNumber = PAGE_NUMBER_RECOM_WIDGET,
        keywords = getRecomKeywords(),
        xDevice = DEFAULT_VALUE_OF_PARAMETER_DEVICE
    )

    protected open fun getRecomCategoryId(
        pageName: String
    ) = listOf<String>()

    protected open fun getRecomKeywords() = listOf<String>()

    fun updateToolbarNotification() {
        updateToolbarNotificationLiveData.postValue(true)
    }

    open fun onViewATCRepurchaseWidget(
        repurchaseProduct: TokoNowProductCardUiModel,
        quantity: Int
    ) {
        val nonVariant = repurchaseProduct.product.nonVariant ?: return
        val productId = repurchaseProduct.productId
        val shopId = repurchaseProduct.shopId
        val currentQuantity = nonVariant.quantity

        if (hasBlockedAddToCart) {
            // this only blocks add to cart when using repurchase widget
            blockAddToCartMutableLiveData.value = Unit
        } else {
            cartService.handleCart(
                CartProductItem(productId, shopId, currentQuantity),
                quantity,
                onSuccessAddToCart = {
                    addToCartMessageSuccess(it.errorMessage.joinToString(separator = ", "))
                    onSuccessATCRepurchaseWidgetProduct(repurchaseProduct, quantity)
                    sendAddToCartRepurchaseProductTracking(quantity, it.data.cartId, repurchaseProduct)
                    updateToolbarNotification()
                },
                onSuccessUpdateCart = {
                    onSuccessATCRepurchaseWidgetProduct(repurchaseProduct, quantity)
                    updateToolbarNotification()
                },
                onSuccessDeleteCart = {
                    removeFromCartMessageSuccess(it.errorMessage.joinToString(separator = ", "))
                    onSuccessATCRepurchaseWidgetProduct(repurchaseProduct, 0)
                    updateToolbarNotification()
                },
                onError = ::onAddToCartFailed,
                handleCartEventNonLogin = {
                    handleAddToCartEventNonLogin(getRepurchaseWidgetIndex())
                }
            )
        }
    }

    private fun onSuccessATCRepurchaseWidgetProduct(
        repurchaseProduct: TokoNowProductCardUiModel,
        quantity: Int
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
        repurchaseProduct: TokoNowProductCardUiModel
    ) {
        addToCartRepurchaseWidgetTrackingMutableLiveData.value =
            Triple(quantity, cartId, repurchaseProduct)
    }

    private fun getRepurchaseWidgetIndex() =
        visitableList.indexOfFirst { it is TokoNowRepurchaseUiModel }

    private fun getUniqueId() =
        if (userSession.isLoggedIn) {
            AuthHelper.getMD5Hash(userSession.userId)
        } else {
            AuthHelper.getMD5Hash(userSession.deviceId)
        }

    protected class HeaderDataView(
            val title: String = "",
            val aceSearchProductHeader: SearchProductHeader = SearchProductHeader(),
            categoryFilterDataValue: DataValue = DataValue(),
            quickFilterDataValue: DataValue = DataValue(),
            val bannerChannel: Channels = Channels(),
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
        val aceSearchProductData: SearchProductData = SearchProductData(),
        val repurchaseWidget: RepurchaseData = RepurchaseData(),
        val productAds: ProductAdsResponse = ProductAdsResponse()
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
}
