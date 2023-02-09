package com.tokopedia.tokopedianow.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.tokopedianow.common.constant.ConstantValue.X_DEVICE_RECOMMENDATION_PARAM
import com.tokopedia.tokopedianow.common.constant.ConstantValue.X_SOURCE_RECOMMENDATION_PARAM
import com.tokopedia.tokopedianow.common.constant.ServiceType
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.MIX_LEFT_CAROUSEL_ATC
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.PRODUCT_RECOM
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.REPURCHASE_PRODUCT
import com.tokopedia.tokopedianow.common.domain.model.GetCategoryListResponse
import com.tokopedia.tokopedianow.common.domain.model.SetUserPreference.SetUserPreferenceData
import com.tokopedia.tokopedianow.common.domain.usecase.GetCategoryListUseCase
import com.tokopedia.tokopedianow.common.domain.usecase.SetUserPreferenceUseCase
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowRepurchaseUiModel
import com.tokopedia.tokopedianow.home.analytic.HomeAddToCartTracker
import com.tokopedia.tokopedianow.home.analytic.HomeRemoveFromCartTracker
import com.tokopedia.tokopedianow.home.analytic.HomeSwitchServiceTracker
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.addEmptyStateIntoList
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.addLoadingIntoList
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.addMoreHomeLayout
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.addProductRecomOoc
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.addProgressBar
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.getItem
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapHomeCategoryMenuData
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapHomeLayoutList
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapPlayWidgetData
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapProductPurchaseData
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapQuestData
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapSharingEducationData
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapSharingReferralData
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapTickerData
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.removeItem
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.removeProgressBar
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.setStateToLoading
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.updateLeftCarouselProductQuantity
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.updatePlayWidget
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.updateProductQuantity
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.updateProductRecom
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.updateProductRecomQuantity
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.updateRepurchaseProductQuantity
import com.tokopedia.tokopedianow.home.domain.mapper.QuestMapper
import com.tokopedia.tokopedianow.home.domain.mapper.RealTimeRecomMapper.getRealTimeRecom
import com.tokopedia.tokopedianow.home.domain.mapper.RealTimeRecomMapper.mapLatestRealTimeRecommendation
import com.tokopedia.tokopedianow.home.domain.mapper.RealTimeRecomMapper.mapLoadingRealTimeRecommendation
import com.tokopedia.tokopedianow.home.domain.mapper.RealTimeRecomMapper.mapRealTimeRecomState
import com.tokopedia.tokopedianow.home.domain.mapper.RealTimeRecomMapper.mapRealTimeRecommendation
import com.tokopedia.tokopedianow.home.domain.mapper.RealTimeRecomMapper.mapRefreshRealTimeRecommendation
import com.tokopedia.tokopedianow.home.domain.mapper.RealTimeRecomMapper.removeRealTimeRecommendation
import com.tokopedia.tokopedianow.home.domain.mapper.TickerMapper
import com.tokopedia.tokopedianow.home.domain.mapper.VisitableMapper.getVisitableId
import com.tokopedia.tokopedianow.home.domain.model.HomeRemoveAbleWidget
import com.tokopedia.tokopedianow.home.domain.model.SearchPlaceholder
import com.tokopedia.tokopedianow.home.domain.usecase.GetHomeLayoutDataUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.GetHomeReferralUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.GetKeywordSearchUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.GetQuestWidgetListUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.GetRepurchaseWidgetUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.GetTickerUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.ReferralEvaluateJoinUseCase
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment.Companion.CATEGORY_LEVEL_DEPTH
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment.Companion.DEFAULT_QUANTITY
import com.tokopedia.tokopedianow.home.presentation.model.HomeReferralDataModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutListUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomePlayWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProgressBarUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestSequenceWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomUiModel.RealTimeRecomWidgetState
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeReceiverReferralDialogUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSharingWidgetUiModel.HomeSharingEducationWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSharingWidgetUiModel.HomeSharingReferralWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeTickerUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class TokoNowHomeViewModel @Inject constructor(
    private val getHomeLayoutDataUseCase: GetHomeLayoutDataUseCase,
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val getKeywordSearchUseCase: GetKeywordSearchUseCase,
    private val getTickerUseCase: GetTickerUseCase,
    private val getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val getRecommendationUseCase: GetRecommendationUseCase,
    private val getChooseAddressWarehouseLocUseCase: GetChosenAddressWarehouseLocUseCase,
    private val getRepurchaseWidgetUseCase: GetRepurchaseWidgetUseCase,
    private val getQuestWidgetListUseCase: GetQuestWidgetListUseCase,
    private val setUserPreferenceUseCase: SetUserPreferenceUseCase,
    private val getHomeReferralUseCase: GetHomeReferralUseCase,
    private val referralEvaluateJoinUseCase: ReferralEvaluateJoinUseCase,
    private val playWidgetTools: PlayWidgetTools,
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    companion object {
        private const val DEFAULT_INDEX = 1
        private const val SUCCESS_CODE = "200"
        private const val DEFAULT_HEADER_Y_COORDINATE = 0f
    }

    val homeLayoutList: LiveData<com.tokopedia.usecase.coroutines.Result<HomeLayoutListUiModel>>
        get() = _homeLayoutList
    val keywordSearch: LiveData<SearchPlaceholder>
        get() = _keywordSearch
    val miniCart: LiveData<Result<MiniCartSimplifiedData>>
        get() = _miniCart
    val chooseAddress: LiveData<Result<GetStateChosenAddressResponse>>
        get() = _chooseAddress
    val miniCartAdd: LiveData<Result<AddToCartDataModel>>
        get() = _miniCartAdd
    val miniCartUpdate: LiveData<Result<UpdateCartV2Data>>
        get() = _miniCartUpdate
    val miniCartRemove: LiveData<Result<Pair<String, String>>>
        get() = _miniCartRemove
    val homeAddToCartTracker: LiveData<HomeAddToCartTracker>
        get() = _homeAddToCartTracker
    val homeRemoveFromCartTracker: LiveData<HomeRemoveFromCartTracker>
        get() = _homeRemoveFromCartTracker
    val atcQuantity: LiveData<Result<HomeLayoutListUiModel>>
        get() = _atcQuantity
    val openScreenTracker: LiveData<String>
        get() = _openScreenTracker
    val setUserPreference: LiveData<Result<SetUserPreferenceData>>
        get() = _setUserPreference
    val getReferralResult: LiveData<Result<HomeReferralDataModel>>
        get() = _getReferralResult
    val homeSwitchServiceTracker: LiveData<HomeSwitchServiceTracker>
        get() = _homeSwitchServiceTracker
    val invalidatePlayImpression: LiveData<Boolean>
        get() = _invalidatePlayImpression
    val updateToolbarNotification: LiveData<Boolean>
        get() = _updateToolbarNotification
    val referralEvaluate: LiveData<Result<HomeReceiverReferralDialogUiModel>>
        get() = _referralEvaluate

    private val _homeLayoutList = MutableLiveData<Result<HomeLayoutListUiModel>>()
    private val _keywordSearch = MutableLiveData<SearchPlaceholder>()
    private val _miniCart = MutableLiveData<Result<MiniCartSimplifiedData>>()
    private val _chooseAddress = MutableLiveData<Result<GetStateChosenAddressResponse>>()
    private val _miniCartAdd = MutableLiveData<Result<AddToCartDataModel>>()
    private val _miniCartUpdate = MutableLiveData<Result<UpdateCartV2Data>>()
    private val _miniCartRemove = MutableLiveData<Result<Pair<String, String>>>()
    private val _homeAddToCartTracker = MutableLiveData<HomeAddToCartTracker>()
    private val _homeRemoveFromCartTracker = MutableLiveData<HomeRemoveFromCartTracker>()
    private val _atcQuantity = MutableLiveData<Result<HomeLayoutListUiModel>>()
    private val _openScreenTracker = MutableLiveData<String>()
    private val _setUserPreference = MutableLiveData<Result<SetUserPreferenceData>>()
    private val _getReferralResult = MutableLiveData<Result<HomeReferralDataModel>>()
    private val _homeSwitchServiceTracker = MutableLiveData<HomeSwitchServiceTracker>()
    private val _invalidatePlayImpression = MutableLiveData<Boolean>()
    private val _updateToolbarNotification = MutableLiveData<Boolean>()
    private val _referralEvaluate = MutableLiveData<Result<HomeReceiverReferralDialogUiModel>>()

    private val homeLayoutItemList = mutableListOf<HomeLayoutItemUiModel?>()
    private var miniCartSimplifiedData: MiniCartSimplifiedData? = null
    private var hasTickerBeenRemoved = false
    private var channelToken = ""
    private var headerYCoordinate = 0f
    private var getHomeLayoutJob: Job? = null
    private var getMiniCartJob: Job? = null

    fun trackOpeningScreen(screenName: String) {
        _openScreenTracker.value = screenName
    }

    fun getLoadingState() {
        channelToken = ""
        homeLayoutItemList.clear()
        homeLayoutItemList.addLoadingIntoList()
        val data = HomeLayoutListUiModel(
            items = getHomeVisitableList(),
            state = TokoNowLayoutState.LOADING
        )
        _homeLayoutList.postValue(Success(data))
    }

    fun getEmptyState(@HomeStaticLayoutId id: String, serviceType: String) {
        launchCatchError(block = {
            homeLayoutItemList.clear()
            homeLayoutItemList.addEmptyStateIntoList(id, serviceType)
            val data = HomeLayoutListUiModel(
                items = getHomeVisitableList(),
                state = TokoNowLayoutState.HIDE
            )
            _homeLayoutList.postValue(Success(data))
        }) {
            _homeLayoutList.postValue(Fail(it))
        }
    }

    fun getProductRecomOoc() {
        launch {
            homeLayoutItemList.addProductRecomOoc()

            val data = HomeLayoutListUiModel(
                items = getHomeVisitableList(),
                state = TokoNowLayoutState.HIDE
            )

            _homeLayoutList.postValue(Success(data))
        }
    }

    fun getHomeLayout(localCacheModel: LocalCacheModel, removeAbleWidgets: List<HomeRemoveAbleWidget>) {
        getHomeLayoutJob?.cancel()
        launchCatchError(block = {
            homeLayoutItemList.clear()

            val homeLayoutResponse = getHomeLayoutDataUseCase.execute(
                localCacheModel = localCacheModel
            )
            channelToken = homeLayoutResponse.first().token

            homeLayoutItemList.mapHomeLayoutList(
                homeLayoutResponse,
                hasTickerBeenRemoved,
                removeAbleWidgets,
                miniCartSimplifiedData,
                localCacheModel,
                userSession.isLoggedIn
            )

            val data = HomeLayoutListUiModel(
                items = getHomeVisitableList(),
                state = TokoNowLayoutState.SHOW
            )
            _homeLayoutList.postValue(Success(data))
        }) {
            _homeLayoutList.postValue(Fail(it))
        }.let {
            getHomeLayoutJob = it
        }
    }

    /**
     * Handle on scroll event, load more if reached bottom of the homepage.
     * All items loaded when token returned from dynamic channel is empty.
     *
     * @param lastVisibleItemIndex last item index visible on user screen
     * @param localCacheModel address data cache from choose address widget
     * @param removeAbleWidgets list of widgets that can be dismissed by user
     */
    fun onScroll(
        lastVisibleItemIndex: Int,
        localCacheModel: LocalCacheModel,
        removeAbleWidgets: List<HomeRemoveAbleWidget>
    ) {
        if (shouldLoadMore(lastVisibleItemIndex)) {
            showProgressBar()

            launchCatchError(block = {
                val homeLayoutResponse = getHomeLayoutDataUseCase.execute(
                    token = channelToken,
                    localCacheModel = localCacheModel
                )
                channelToken = homeLayoutResponse.first().token

                homeLayoutItemList.removeProgressBar()
                homeLayoutItemList.addMoreHomeLayout(
                    homeLayoutResponse,
                    removeAbleWidgets,
                    miniCartSimplifiedData,
                    localCacheModel
                )

                getLayoutComponentData(localCacheModel)

                val data = HomeLayoutListUiModel(
                    items = getHomeVisitableList(),
                    state = TokoNowLayoutState.LOAD_MORE
                )

                _homeLayoutList.postValue(Success(data))
            }) {
                /* nothing to do */
            }
        }
    }

    fun getKeywordSearch(isFirstInstall: Boolean, deviceId: String, userId: String) {
        launchCatchError(coroutineContext, block = {
            val response = getKeywordSearchUseCase.execute(isFirstInstall, deviceId, userId)
            _keywordSearch.postValue(response.searchData)
        }) {}
    }

    fun getMiniCart(shopId: List<String>, warehouseId: String?) {
        if (!shopId.isNullOrEmpty() && warehouseId.toLongOrZero() != 0L && userSession.isLoggedIn) {
            getMiniCartJob?.cancel()
            launchCatchError(block = {
                getMiniCartUseCase.setParams(shopId, MiniCartSource.TokonowHome)
                getMiniCartUseCase.execute({
                    setProductAddToCartQuantity(it)
                    _miniCart.postValue(Success(it))
                }, {
                    _miniCart.postValue(Fail(it))
                })
            }) {
                _miniCart.postValue(Fail(it))
            }.let {
                getMiniCartJob = it
            }
        }
    }

    fun getChooseAddress(source: String) {
        getChooseAddressWarehouseLocUseCase.getStateChosenAddress({
            _chooseAddress.postValue(Success(it))
        }, {
            _chooseAddress.postValue(Fail(it))
        }, source)
    }

    fun getCategoryMenu(warehouseId: String) {
        launchCatchError(block = {
            val response = getCategoryList(warehouseId)
            homeLayoutItemList.mapHomeCategoryMenuData(response, warehouseId)
            val data = HomeLayoutListUiModel(
                items = getHomeVisitableList(),
                state = TokoNowLayoutState.UPDATE
            )
            _homeLayoutList.postValue(Success(data))
        }) {
            homeLayoutItemList.mapHomeCategoryMenuData(null)
            val data = HomeLayoutListUiModel(
                items = getHomeVisitableList(),
                state = TokoNowLayoutState.UPDATE
            )
            _homeLayoutList.postValue(Success(data))
        }
    }

    fun addProductToCart(
        channelId: String = "",
        productId: String,
        quantity: Int,
        shopId: String,
        @TokoNowLayoutType type: String
    ) {
        val miniCartItem = getMiniCartItem(productId)
        when {
            miniCartItem == null && quantity.isZero() -> { /* do nothing */ }
            miniCartItem == null -> addItemToCart(channelId, productId, shopId, quantity, type)
            quantity.isZero() -> removeItemCart(miniCartItem, type)
            else -> updateItemCart(miniCartItem, quantity, type)
        }
    }

    fun getMiniCartItem(productId: String): MiniCartItem.MiniCartItemProduct? {
        val items = miniCartSimplifiedData?.miniCartItems.orEmpty()
        return items.getMiniCartItemProduct(productId)
    }

    fun setProductAddToCartQuantity(miniCart: MiniCartSimplifiedData) {
        launchCatchError(block = {
            setMiniCartAndProductQuantity(miniCart)
            val data = HomeLayoutListUiModel(
                items = getHomeVisitableList(),
                state = TokoNowLayoutState.UPDATE
            )
            _atcQuantity.postValue(Success(data))
        }) {}
    }

    fun updateToolbarNotification() {
        _updateToolbarNotification.postValue(true)
    }

    fun removeTickerWidget(id: String) {
        launchCatchError(block = {
            hasTickerBeenRemoved = true
            homeLayoutItemList.removeItem(id)

            val data = HomeLayoutListUiModel(
                items = getHomeVisitableList(),
                state = TokoNowLayoutState.UPDATE
            )

            _homeLayoutList.postValue(Success(data))
        }) {}
    }

    fun removeLeftCarouselAtc(id: String) {
        launchCatchError(block = {
            homeLayoutItemList.removeItem(id)

            val data = HomeLayoutListUiModel(
                items = getHomeVisitableList(),
                state = TokoNowLayoutState.UPDATE
            )

            _homeLayoutList.postValue(Success(data))
        }) {}
    }

    fun removeWidget(id: String) {
        launchCatchError(block = {
            homeLayoutItemList.removeItem(id)

            val data = HomeLayoutListUiModel(
                items = getHomeVisitableList(),
                state = TokoNowLayoutState.UPDATE
            )

            _homeLayoutList.postValue(Success(data))
        }) {}
    }

    fun getRepurchaseProducts(): List<TokoNowProductCardUiModel> {
        val item = homeLayoutItemList.firstOrNull { it?.layout is TokoNowRepurchaseUiModel }
        val repurchase = item?.layout as? TokoNowRepurchaseUiModel
        return repurchase?.productList.orEmpty()
    }

    fun needToShowOnBoardToaster(serviceType: String, has20mCoachMarkBeenShown: Boolean, has2hCoachMarkBeenShown: Boolean, isWarehouseIdZero: Boolean): Boolean {
        /*
         * SWITCHER TOASTER
         * - toaster will be shown if coach mark has been shown
         */

        val is2hServiceType = serviceType == ServiceType.NOW_2H
        val is20mServiceType = serviceType == ServiceType.NOW_15M

        return when {
            isWarehouseIdZero -> false
            is2hServiceType && has20mCoachMarkBeenShown -> true
            is20mServiceType && has2hCoachMarkBeenShown -> true
            else -> false
        }
    }

    fun refreshQuestList() {
        getQuestUiModel()?.let { item ->
            launchCatchError(block = {
                getQuestListData(item)

                val data = HomeLayoutListUiModel(
                    items = getHomeVisitableList(),
                    state = TokoNowLayoutState.UPDATE
                )

                _homeLayoutList.postValue(Success(data))
            }) {
                removeWidget(item.id)
            }
        }
    }

    fun autoRefreshPlayWidget(item: HomePlayWidgetUiModel) {
        launchCatchError(block = {
            val playWidgetUiModel = getPlayWidget(item, isAutoRefresh = true)
            homeLayoutItemList.mapPlayWidgetData(playWidgetUiModel)

            val data = HomeLayoutListUiModel(
                items = getHomeVisitableList(),
                state = TokoNowLayoutState.UPDATE
            )

            _homeLayoutList.postValue(Success(data))
        }, onError = {
                removeWidget(item.id)
            })
    }

    fun updatePlayWidget(channelId: String, totalView: String) {
        launchCatchError(block = {
            homeLayoutItemList.updatePlayWidget(channelId, totalView)

            val data = HomeLayoutListUiModel(
                getHomeVisitableList(),
                TokoNowLayoutState.UPDATE
            )

            _homeLayoutList.postValue(Success(data))
        }) {
            // do nothing
        }
    }

    fun switchServiceOrLoadLayout(externalServiceType: String, localCacheModel: LocalCacheModel) {
        /*
           Note :
            - There are 2 types of external service we'll receive, those are 20m and 2h.
            - 20m will be changed to 15m temporarily.
            - 15m still used in LCA and BE as 20m service type.
         */
        val currentServiceType = localCacheModel.getServiceType()

        if (externalServiceType != currentServiceType && externalServiceType.isNotBlank()) {
            switchService(localCacheModel, externalServiceType)
        } else {
            getLoadingState()
        }
    }

    /***
     * Switch between NOW 20 minutes/2 hours
     * @param localCacheModel local data from choose address
     */
    fun switchService(localCacheModel: LocalCacheModel) {
        launchCatchError(block = {
            trackSwitchService(
                localCacheModel = localCacheModel
            )

            val currentServiceType = localCacheModel.service_type

            val serviceType = if (
                currentServiceType == ServiceType.NOW_15M ||
                currentServiceType == ServiceType.NOW_OOC
            ) {
                ServiceType.NOW_2H
            } else {
                ServiceType.NOW_15M
            }

            val userPreference = setUserPreferenceUseCase.execute(localCacheModel, serviceType)
            _setUserPreference.postValue(Success(userPreference))
        }) {
            _setUserPreference.postValue(Fail(it))
        }
    }

    /***
     * Switch between NOW 20 minutes/2 hours
     * @param localCacheModel local data from choose address
     * @param serviceType which is the intended type of service
     */
    fun switchService(localCacheModel: LocalCacheModel, serviceType: String) {
        launchCatchError(block = {
            val userPreference = setUserPreferenceUseCase.execute(localCacheModel, serviceType)
            _setUserPreference.postValue(Success(userPreference))
        }) {
            _setUserPreference.postValue(Fail(it))
        }
    }

    /**
     * Get layout content data from external query.
     * Example: Category Grid get its data from TokonowCategoryTree.
     * @param localCacheModel local data from choose address
     */
    fun getLayoutComponentData(localCacheModel: LocalCacheModel) {
        launchCatchError(block = {
            val layoutItems = mutableListOf<HomeLayoutItemUiModel>()
            layoutItems.addAll(homeLayoutItemList.filterNotNull())

            layoutItems.filter { it.state == HomeLayoutItemState.NOT_LOADED }.forEach {
                homeLayoutItemList.setStateToLoading(it)

                when (val item = it.layout) {
                    is HomeLayoutUiModel -> getTokoNowHomeComponent(item, localCacheModel) // TokoNow Home Component
                    else -> getTokoNowGlobalComponent(item, localCacheModel.warehouse_id) // TokoNow Common Component
                }

                val data = HomeLayoutListUiModel(
                    items = getHomeVisitableList(),
                    state = TokoNowLayoutState.UPDATE
                )

                _homeLayoutList.postValue(Success(data))
            }
        }) {
            _homeLayoutList.postValue(Fail(it))
        }
    }

    /**
     * get dialog data after user click link referral
     */
    fun getReceiverHomeDialog(referralData: String) {
        launchCatchError(block = {
            val data = referralEvaluateJoinUseCase.execute(referralData)
            _referralEvaluate.postValue(Success(data.gamiReferralEvaluteJoinResponse.toHomeReceiverDialogUiModel()))
        }, onError = {
                _referralEvaluate.postValue(Fail(it))
            })
    }

    /**
     * Get data from additional query for TokopediaNOW Home Component.
     * Add use case and data mapping for TokopediaNOW Home Component here.
     * Example: Category Grid get its data from getCategoryListUseCase.
     *
     * @param item TokopediaNOW Home component item
     */
    private suspend fun getTokoNowHomeComponent(item: HomeLayoutUiModel, localCacheModel: LocalCacheModel) {
        when (item) {
            is HomeTickerUiModel -> getTickerDataAsync(item, localCacheModel).await()
            is HomeSharingEducationWidgetUiModel -> getSharingEducationAsync(item, localCacheModel.warehouse_id).await()
            is HomeSharingReferralWidgetUiModel -> getSharingReferralAsync(item).await()
            is HomeQuestSequenceWidgetUiModel -> getQuestListAsync(item).await()
            is HomePlayWidgetUiModel -> getPlayWidgetAsync(item).await()
            else -> removeUnsupportedLayout(item)
        }
    }

    /**
     * Get data from additional query for TokopediaNOW Common Component.
     * Add use case and data mapping for TokopediaNOW Common Component here.
     * Example: Category Grid get its data from getCategoryListUseCase.
     *
     * @param item TokopediaNOW component item
     * @param warehouseId Id obtained from choose address widget
     */
    private suspend fun getTokoNowGlobalComponent(item: Visitable<*>?, warehouseId: String) {
        when (item) {
            is TokoNowCategoryMenuUiModel -> getCategoryMenuDataAsync(warehouseId).await()
            is TokoNowRepurchaseUiModel -> getRepurchaseDataAsync(item, warehouseId).await()
            else -> removeUnsupportedLayout(item)
        }
    }

    private suspend fun getRepurchaseDataAsync(
        item: TokoNowRepurchaseUiModel,
        warehouseId: String
    ): Deferred<Unit?> {
        return asyncCatchError(block = {
            val response = getRepurchaseWidgetUseCase.execute(warehouseId)
            if (response.products.isNotEmpty()) {
                homeLayoutItemList.mapProductPurchaseData(item, response, miniCartSimplifiedData)
            } else {
                homeLayoutItemList.removeItem(item.id)
            }
        }) {
            homeLayoutItemList.removeItem(item.id)
        }
    }

    private suspend fun getCategoryMenuDataAsync(
        warehouseId: String
    ): Deferred<Unit?> {
        return asyncCatchError(block = {
            val response = getCategoryList(warehouseId)
            homeLayoutItemList.mapHomeCategoryMenuData(response, warehouseId)
        }) {
            homeLayoutItemList.mapHomeCategoryMenuData(emptyList())
        }
    }

    private suspend fun getTickerDataAsync(item: HomeTickerUiModel, localCacheModel: LocalCacheModel?): Deferred<Unit?> {
        return asyncCatchError(block = {
            val tickerList = getTickerUseCase.execute(localCacheModel = localCacheModel).ticker.tickerList
            val tickerData = TickerMapper.mapTickerData(tickerList)
            homeLayoutItemList.mapTickerData(item, tickerData)
        }) {
            homeLayoutItemList.removeItem(item.id)
        }
    }

    private suspend fun getQuestListAsync(item: HomeQuestSequenceWidgetUiModel): Deferred<Unit?> {
        return asyncCatchError(block = {
            getQuestListData(item)
        }) {
            homeLayoutItemList.removeItem(item.id)
        }
    }

    private suspend fun getQuestListData(item: HomeQuestSequenceWidgetUiModel) {
        val questListResponse = getQuestWidgetListUseCase.execute().questWidgetList
        val questData = QuestMapper.mapQuestData(questListResponse.questWidgetList)
        if (questListResponse.questWidgetList.isEmpty() && questListResponse.resultStatus.code == SUCCESS_CODE) {
            homeLayoutItemList.removeItem(item.id)
        } else if (questListResponse.resultStatus.code != SUCCESS_CODE) {
            homeLayoutItemList.mapQuestData(
                item = item,
                questList = emptyList(),
                state = HomeLayoutItemState.NOT_LOADED
            )
        } else {
            homeLayoutItemList.mapQuestData(
                item = item,
                questList = questData,
                state = HomeLayoutItemState.LOADED
            )
        }
    }

    private suspend fun getSharingEducationAsync(item: HomeSharingEducationWidgetUiModel, warehouseId: String): Deferred<Unit?> {
        return asyncCatchError(block = {
            val response = getRepurchaseWidgetUseCase.execute(warehouseId)
            if (response.products.isNotEmpty()) {
                homeLayoutItemList.mapSharingEducationData(item)
            } else {
                homeLayoutItemList.removeItem(item.id)
            }
        }) {
            homeLayoutItemList.removeItem(item.id)
        }
    }

    private suspend fun getSharingReferralAsync(item: HomeSharingReferralWidgetUiModel): Deferred<Unit?> {
        return asyncCatchError(block = {
            val referral = getHomeReferralUseCase.execute(item.slug)

            if (referral.isEligible) {
                homeLayoutItemList.mapSharingReferralData(item, referral)
                _getReferralResult.postValue(Success(referral))
            } else {
                homeLayoutItemList.removeItem(item.id)
            }
        }) {
            homeLayoutItemList.removeItem(item.id)
            _getReferralResult.postValue(Fail(it))
        }
    }

    private suspend fun getCategoryList(warehouseId: String): List<GetCategoryListResponse.CategoryListResponse.CategoryResponse> {
        return getCategoryListUseCase.execute(warehouseId, CATEGORY_LEVEL_DEPTH).data
    }

    private suspend fun getPlayWidgetAsync(item: HomePlayWidgetUiModel): Deferred<Unit?> {
        return asyncCatchError(block = {
            val playWidgetUiModel = getPlayWidget(item, isAutoRefresh = false)
            homeLayoutItemList.mapPlayWidgetData(playWidgetUiModel)

            val data = HomeLayoutListUiModel(
                items = getHomeVisitableList(),
                state = TokoNowLayoutState.UPDATE
            )

            _invalidatePlayImpression.postValue(true)
            _homeLayoutList.postValue(Success(data))
        }) {
            removeWidget(item.id)
        }
    }

    private suspend fun getPlayWidget(
        item: HomePlayWidgetUiModel,
        isAutoRefresh: Boolean
    ): HomePlayWidgetUiModel {
        val playWidgetState = item.playWidgetState
        val title = playWidgetState.model.title
        val appLink = playWidgetState.model.actionAppLink

        val widgetType = item.widgetType
        val response = playWidgetTools.getWidgetFromNetwork(
            widgetType = widgetType,
            coroutineContext = dispatchers.io
        )
        val state = playWidgetTools.mapWidgetToModel(response)
        val model = state.model.copy(title = title, actionAppLink = appLink)
        val widgetState = state.copy(model = model)
        return HomePlayWidgetUiModel(item.id, widgetType, widgetState, isAutoRefresh)
    }

    private fun addItemToCart(
        channelId: String,
        productId: String,
        shopId: String,
        quantity: Int,
        @TokoNowLayoutType type: String
    ) {
        val addToCartRequestParams = AddToCartUseCase.getMinimumParams(
            productId = productId,
            shopId = shopId,
            quantity = quantity
        )
        addToCartUseCase.setParams(addToCartRequestParams)
        addToCartUseCase.execute({
            trackProductAddToCart(productId, quantity, type, it.data.cartId)
            updateAddToCartQuantity(productId, quantity, type)
            checkRealTimeRecommendation(channelId, productId, type)
            updateToolbarNotification()
            _miniCartAdd.postValue(Success(it))
        }, {
            updateAddToCartQuantity(productId, quantity, type)
            _miniCartAdd.postValue(Fail(it))
        })
    }

    fun refreshRealTimeRecommendation(
        channelId: String,
        productId: String,
        @TokoNowLayoutType type: String
    ) {
        homeLayoutItemList.getRealTimeRecom(channelId)?.let { recomItem ->
            showRealTimeRecommendationLoading(channelId, type)
            getRealTimeRecommendation(channelId, productId, recomItem.pageName, type)
        }
    }

    private fun showRealTimeRecommendationLoading(channelId: String, type: String) {
        homeLayoutItemList.mapLoadingRealTimeRecommendation(channelId, type)

        val data = HomeLayoutListUiModel(
            getHomeVisitableList(),
            TokoNowLayoutState.UPDATE
        )

        _homeLayoutList.postValue(Success(data))
    }

    fun removeRealTimeRecommendation(channelId: String, @TokoNowLayoutType type: String) {
        launchCatchError(block = {
            homeLayoutItemList.removeRealTimeRecommendation(channelId, type)

            val data = HomeLayoutListUiModel(
                getHomeVisitableList(),
                TokoNowLayoutState.UPDATE
            )

            _homeLayoutList.postValue(Success(data))
        }) {
        }
    }

    private fun checkRealTimeRecommendation(
        channelId: String,
        productId: String,
        @TokoNowLayoutType type: String
    ) {
        homeLayoutItemList.getRealTimeRecom(channelId)?.run {
            when {
                shouldFetch() -> {
                    showRealTimeRecommendationProgressBar(channelId, productId, type)
                    getRealTimeRecommendation(channelId, productId, pageName, type)
                }
                shouldRefresh(channelId, productId) -> {
                    getRefreshRealTimeRecommendation(channelId, productId)
                }
            }
        }
    }

    private fun getRealTimeRecommendation(
        channelId: String,
        productId: String,
        pageName: String,
        @TokoNowLayoutType type: String
    ) {
        launchCatchError(block = {
            val recommendationWidgets = getRecommendationUseCase.getData(
                GetRecommendationRequestParam(
                    pageName = pageName,
                    productIds = listOf(productId),
                    xSource = X_SOURCE_RECOMMENDATION_PARAM,
                    xDevice = X_DEVICE_RECOMMENDATION_PARAM
                )
            )

            if (recommendationWidgets.first().recommendationItemList.isNotEmpty()) {
                homeLayoutItemList.mapRealTimeRecommendation(
                    channelId,
                    recommendationWidgets.first(),
                    miniCartSimplifiedData,
                    type
                )
            } else {
                homeLayoutItemList.mapLatestRealTimeRecommendation(channelId, type)
            }

            val data = HomeLayoutListUiModel(
                getHomeVisitableList(),
                TokoNowLayoutState.UPDATE
            )

            _homeLayoutList.postValue(Success(data))
        }) {
        }
    }

    private fun showRealTimeRecommendationProgressBar(channelId: String, productId: String, type: String) {
        homeLayoutItemList.mapRealTimeRecomState(
            channelId = channelId,
            productId = productId,
            widgetState = RealTimeRecomWidgetState.LOADING,
            type = type
        )

        val data = HomeLayoutListUiModel(
            getHomeVisitableList(),
            TokoNowLayoutState.UPDATE
        )

        _homeLayoutList.postValue(Success(data))
    }

    private fun getRefreshRealTimeRecommendation(channelId: String, productId: String) {
        homeLayoutItemList.mapRefreshRealTimeRecommendation(
            channelId = channelId,
            productId = productId
        )

        val data = HomeLayoutListUiModel(
            getHomeVisitableList(),
            TokoNowLayoutState.UPDATE
        )

        _homeLayoutList.postValue(Success(data))
    }

    private fun updateAddToCartQuantity(
        productId: String,
        quantity: Int,
        @TokoNowLayoutType type: String
    ) {
        homeLayoutItemList.updateProductQuantity(productId, quantity, type)

        val data = HomeLayoutListUiModel(
            items = getHomeVisitableList(),
            state = TokoNowLayoutState.UPDATE
        )

        _atcQuantity.postValue(Success(data))
    }

    private fun updateItemCart(
        miniCartItem: MiniCartItem.MiniCartItemProduct,
        quantity: Int,
        @TokoNowLayoutType type: String
    ) {
        miniCartItem.quantity = quantity
        val cartId = miniCartItem.cartId
        val productId = miniCartItem.productId

        val updateCartRequest = UpdateCartRequest(
            cartId = cartId,
            quantity = miniCartItem.quantity,
            notes = miniCartItem.notes
        )
        updateCartUseCase.setParams(
            updateCartRequestList = listOf(updateCartRequest),
            source = UpdateCartUseCase.VALUE_SOURCE_UPDATE_QTY_NOTES
        )
        updateCartUseCase.execute({
            trackProductUpdateCart(productId, quantity, type, cartId)
            updateAddToCartQuantity(productId, quantity, type)
            updateToolbarNotification()
            _miniCartUpdate.value = Success(it)
        }, {
            updateAddToCartQuantity(productId, quantity, type)
            _miniCartUpdate.postValue(Fail(it))
        })
    }

    private fun removeItemCart(miniCartItem: MiniCartItem.MiniCartItemProduct, @TokoNowLayoutType type: String) {
        deleteCartUseCase.setParams(
            cartIdList = listOf(miniCartItem.cartId)
        )
        val productId = miniCartItem.productId
        deleteCartUseCase.execute({
            val data = Pair(productId, it.data.message.joinToString(separator = ", "))
            trackProductRemoveCart(productId, type, miniCartItem.cartId)
            updateAddToCartQuantity(productId, DEFAULT_QUANTITY, type)
            updateToolbarNotification()
            _miniCartRemove.postValue(Success(data))
        }, {
            updateAddToCartQuantity(productId, DEFAULT_QUANTITY, type)
            _miniCartRemove.postValue(Fail(it))
        })
    }

    private fun setMiniCartAndProductQuantity(miniCart: MiniCartSimplifiedData) {
        setMiniCartSimplifiedData(miniCart)
        updateProductQuantity(miniCart)
    }

    private fun updateProductQuantity(miniCart: MiniCartSimplifiedData) {
        homeLayoutItemList.updateRepurchaseProductQuantity(miniCart)
        homeLayoutItemList.updateProductRecomQuantity(miniCart)
        homeLayoutItemList.updateLeftCarouselProductQuantity(miniCart)
    }

    private fun setMiniCartSimplifiedData(miniCart: MiniCartSimplifiedData) {
        miniCartSimplifiedData = miniCart
    }

    private fun trackProductAddToCart(
        productId: String,
        quantity: Int,
        @TokoNowLayoutType type: String,
        cartId: String
    ) {
        when (type) {
            REPURCHASE_PRODUCT -> trackRepurchaseAddToCart(productId, quantity, cartId)
            PRODUCT_RECOM -> trackRecentProductRecomAddToCart(productId, quantity, cartId)
            MIX_LEFT_CAROUSEL_ATC -> trackLeftCarouselAddToCart(productId, quantity, cartId)
        }
    }

    private fun trackProductUpdateCart(
        productId: String,
        quantity: Int,
        @TokoNowLayoutType type: String,
        cartId: String
    ) {
        if (type == PRODUCT_RECOM) {
            trackRecentProductRecomAddToCart(productId, quantity, cartId)
        }
    }

    private fun trackProductRemoveCart(
        productId: String,
        @TokoNowLayoutType type: String,
        cartId: String
    ) {
        if (type == PRODUCT_RECOM) {
            trackRecentProductRecomRemoveCart(productId, cartId)
        }
    }

    private fun trackRepurchaseAddToCart(productId: String, quantity: Int, cartId: String) {
        val homeItem = homeLayoutItemList.filterNotNull()
            .firstOrNull { it.layout is TokoNowRepurchaseUiModel }
        val repurchase = homeItem?.layout as? TokoNowRepurchaseUiModel
        val productList = repurchase?.productList.orEmpty()
        val product = productList.firstOrNull { it.productId == productId }

        product?.let {
            val data = HomeAddToCartTracker(product.position, quantity, cartId, it)
            _homeAddToCartTracker.postValue(data)
        }
    }

    private fun trackRecentProductRecomAddToCart(productId: String, quantity: Int, cartId: String) {
        homeLayoutItemList.updateProductRecom(productId, quantity)?.let { productRecom ->
            val recomItemList = productRecom.productList
            val position = getPositionProductRecom(recomItemList, productId)
            val data = HomeAddToCartTracker(position, quantity, cartId, productRecom)
            _homeAddToCartTracker.postValue(data)
        }
    }

    private fun trackRecentProductRecomRemoveCart(productId: String, cartId: String) {
        homeLayoutItemList.updateProductRecom(productId, DEFAULT_QUANTITY)?.let { productRecom ->
            val recomItemList = productRecom.productList
            val position = getPositionProductRecom(recomItemList, productId)
            val data = HomeRemoveFromCartTracker(position, DEFAULT_QUANTITY, cartId, productRecom)
            _homeRemoveFromCartTracker.postValue(data)
        }
    }

    private fun getPositionProductRecom(
        recomItemList: List<ProductCardCompactCarouselItemUiModel>,
        productId: String
    ): Int {
        val product = recomItemList.first { it.productCardModel.productId == productId }
        return recomItemList.indexOf(product)
    }

    private fun trackLeftCarouselAddToCart(productId: String, quantity: Int, cartId: String) {
        homeLayoutItemList.filterNotNull().firstOrNull { it.layout is HomeLeftCarouselAtcUiModel }?.apply {
            val repurchase = layout as HomeLeftCarouselAtcUiModel
            val productList = repurchase.productList
            val product = productList.firstOrNull { it is HomeLeftCarouselAtcProductCardUiModel && it.productCardModel.productId == productId }

            product?.let {
                val position = productList.indexOf(it)
                val data = HomeAddToCartTracker(position, quantity, cartId, it)
                _homeAddToCartTracker.postValue(data)
            }
        }
    }

    fun trackSwitchService(localCacheModel: LocalCacheModel, isImpressionTracker: Boolean = false) {
        launchCatchError(block = {
            val whIdOrigin = localCacheModel.warehouse_id
            val whIdDestination = localCacheModel.warehouses.findLast { it.service_type != localCacheModel.service_type }?.warehouse_id.orZero().toString()

            val serviceType = if (localCacheModel.service_type == ServiceType.NOW_2H) {
                ServiceType.NOW_2H
            } else {
                ServiceType.NOW_15M
            }

            _homeSwitchServiceTracker.postValue(
                HomeSwitchServiceTracker(
                    userId = userSession.userId,
                    whIdOrigin = whIdOrigin,
                    whIdDestination = whIdDestination,
                    isNow15 = serviceType == ServiceType.NOW_15M,
                    isImpressionTracker = isImpressionTracker
                )
            )
        }) { /* nothing to do */ }
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

    private fun shouldLoadMore(lastVisibleItemIndex: Int): Boolean {
        val allItemsLoaded = channelToken.isEmpty()
        val scrolledToBottom = scrolledToBottom(lastVisibleItemIndex)
        return if (allItemsLoaded || !scrolledToBottom) false else !isLoading()
    }

    private fun scrolledToBottom(lastVisibleItemIndex: Int): Boolean {
        return lastVisibleItemIndex == homeLayoutItemList.count() - DEFAULT_INDEX
    }

    private fun isLoading(): Boolean {
        return getHomeVisitableList().firstOrNull { it == HomeProgressBarUiModel } != null
    }

    private fun showProgressBar() {
        homeLayoutItemList.addProgressBar()
        val data = HomeLayoutListUiModel(
            getHomeVisitableList(),
            TokoNowLayoutState.UPDATE
        )
        _homeLayoutList.postValue(Success(data))
    }

    private fun getHomeVisitableList(): List<Visitable<*>> {
        val layoutItemsList = homeLayoutItemList.toMutableList()
        return layoutItemsList.filterNotNull().mapNotNull { it.layout }
    }

    private fun getQuestUiModel(): HomeQuestSequenceWidgetUiModel? {
        return homeLayoutItemList.getItem(HomeQuestSequenceWidgetUiModel::class.java)
    }

    private fun removeUnsupportedLayout(item: Visitable<*>?) {
        homeLayoutItemList.removeItem(item?.getVisitableId())
    }
}
