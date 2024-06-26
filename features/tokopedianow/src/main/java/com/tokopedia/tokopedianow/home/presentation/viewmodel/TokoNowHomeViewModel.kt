package com.tokopedia.tokopedianow.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.GetProductBundleRecomUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.tokopedianow.buyercomm.domain.usecase.GetBuyerCommunicationUseCase
import com.tokopedia.tokopedianow.common.base.viewmodel.BaseTokoNowViewModel
import com.tokopedia.tokopedianow.common.constant.ConstantValue.X_DEVICE_RECOMMENDATION_PARAM
import com.tokopedia.tokopedianow.common.constant.ConstantValue.X_SOURCE_RECOMMENDATION_PARAM
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.CHIP_CAROUSEL
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.MIX_LEFT_CAROUSEL_ATC
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.PRODUCT_RECOM
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.REPURCHASE_PRODUCT
import com.tokopedia.tokopedianow.common.domain.mapper.AddressMapper
import com.tokopedia.tokopedianow.common.domain.mapper.HomeBannerMapper.mapHomeBanner
import com.tokopedia.tokopedianow.common.domain.model.GetCategoryListResponse
import com.tokopedia.tokopedianow.common.domain.usecase.GetCategoryListUseCase
import com.tokopedia.tokopedianow.common.domain.usecase.GetHomeBannerUseCase
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.model.TokoNowBundleUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowChipUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowRepurchaseProductUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowRepurchaseUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.home.analytic.HomeAddToCartTracker
import com.tokopedia.tokopedianow.home.analytic.HomeRemoveFromCartTracker
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId
import com.tokopedia.tokopedianow.home.domain.mapper.CatalogCouponListMapper.COUPON_WIDGET_DOUBLE_SLUG_SIZE
import com.tokopedia.tokopedianow.home.domain.mapper.CatalogCouponListMapper.COUPON_WIDGET_SINGLE_SLUG_SIZE
import com.tokopedia.tokopedianow.home.domain.mapper.CatalogCouponListMapper.mapToClaimCouponDataModel
import com.tokopedia.tokopedianow.home.domain.mapper.HomeHeaderMapper.mapHomeHeaderErrorState
import com.tokopedia.tokopedianow.home.domain.mapper.HomeHeaderMapper.mapToHomeHeaderUiModel
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.addEmptyStateIntoList
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.addLoadingIntoList
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.addMoreHomeLayout
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.addProductRecomOoc
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.addProgressBar
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.getItem
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapHomeCatalogCouponList
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapHomeCategoryMenuData
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapHomeClaimCouponList
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapHomeLayoutList
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapPlayWidgetData
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapProductBundleRecomData
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapProductPurchaseData
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapQuestFinishedWidget
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapQuestReloadWidget
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapQuestWidget
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapSharingEducationData
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapSharingReferralData
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.removeItem
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.removeProgressBar
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.setStateToLoading
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.updateCarouselChipsQuantity
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.updateLeftCarouselProductQuantity
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.updatePlayWidget
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.updateProductQuantity
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.updateProductRecom
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.updateProductRecomQuantity
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.updateRepurchaseProductQuantity
import com.tokopedia.tokopedianow.home.domain.mapper.ProductCarouselChipsMapper.getCurrentSelectedChipId
import com.tokopedia.tokopedianow.home.domain.mapper.ProductCarouselChipsMapper.getProductCarouselChipByProductId
import com.tokopedia.tokopedianow.home.domain.mapper.ProductCarouselChipsMapper.getProductCarouselChipsItem
import com.tokopedia.tokopedianow.home.domain.mapper.ProductCarouselChipsMapper.mapProductCarouselChipsWidget
import com.tokopedia.tokopedianow.home.domain.mapper.ProductCarouselChipsMapper.setProductCarouselChipsLoading
import com.tokopedia.tokopedianow.home.domain.mapper.QuestMapper
import com.tokopedia.tokopedianow.home.domain.mapper.QuestMapper.updateQuestWidgetUiModel
import com.tokopedia.tokopedianow.home.domain.mapper.RealTimeRecomMapper.getRealTimeRecom
import com.tokopedia.tokopedianow.home.domain.mapper.RealTimeRecomMapper.mapLatestRealTimeRecommendation
import com.tokopedia.tokopedianow.home.domain.mapper.RealTimeRecomMapper.mapLoadingRealTimeRecommendation
import com.tokopedia.tokopedianow.home.domain.mapper.RealTimeRecomMapper.mapRealTimeRecomState
import com.tokopedia.tokopedianow.home.domain.mapper.RealTimeRecomMapper.mapRealTimeRecommendation
import com.tokopedia.tokopedianow.home.domain.mapper.RealTimeRecomMapper.mapRefreshRealTimeRecommendation
import com.tokopedia.tokopedianow.home.domain.mapper.RealTimeRecomMapper.removeRealTimeRecommendation
import com.tokopedia.tokopedianow.home.domain.mapper.RecomParamMapper.mapToRecomRequestParam
import com.tokopedia.tokopedianow.home.domain.mapper.VisitableMapper.getVisitableId
import com.tokopedia.tokopedianow.home.domain.model.HomeRemoveAbleWidget
import com.tokopedia.tokopedianow.home.domain.model.SearchPlaceholder
import com.tokopedia.tokopedianow.home.domain.model.StartQuestResponse
import com.tokopedia.tokopedianow.home.domain.usecase.GetCatalogCouponListUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.GetHomeLayoutDataUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.GetHomeReferralUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.GetKeywordSearchUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.GetQuestWidgetListUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.GetRepurchaseWidgetUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.RedeemCouponUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.ReferralEvaluateJoinUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.StartQuestUseCase
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment.Companion.CATEGORY_LEVEL_DEPTH
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment.Companion.DEFAULT_QUANTITY
import com.tokopedia.tokopedianow.home.presentation.model.HomeClaimCouponDataModel
import com.tokopedia.tokopedianow.home.presentation.model.HomeReferralDataModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeHeaderUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutListUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomePlayWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductCarouselChipsUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProgressBarUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomUiModel.RealTimeRecomWidgetState
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeReceiverReferralDialogUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSharingWidgetUiModel.HomeSharingEducationWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSharingWidgetUiModel.HomeSharingReferralWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.claimcoupon.HomeClaimCouponWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.quest.HomeQuestReloadWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.quest.HomeQuestShimmeringWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.viewholder.claimcoupon.HomeClaimCouponWidgetItemViewHolder.Companion.COUPON_STATUS_CLAIMED
import com.tokopedia.tokopedianow.home.presentation.viewholder.claimcoupon.HomeClaimCouponWidgetItemViewHolder.Companion.COUPON_STATUS_LOGIN
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class TokoNowHomeViewModel @Inject constructor(
    private val getHomeLayoutDataUseCase: GetHomeLayoutDataUseCase,
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val getKeywordSearchUseCase: GetKeywordSearchUseCase,
    private val getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    private val getRecommendationUseCase: GetRecommendationUseCase,
    private val getChooseAddressWarehouseLocUseCase: GetChosenAddressWarehouseLocUseCase,
    private val getRepurchaseWidgetUseCase: GetRepurchaseWidgetUseCase,
    private val getQuestWidgetListUseCase: GetQuestWidgetListUseCase,
    private val getHomeReferralUseCase: GetHomeReferralUseCase,
    private val referralEvaluateJoinUseCase: ReferralEvaluateJoinUseCase,
    private val getCatalogCouponListUseCase: GetCatalogCouponListUseCase,
    private val redeemCouponUseCase: RedeemCouponUseCase,
    private val getProductBundleRecomUseCase: GetProductBundleRecomUseCase,
    private val getBuyerCommunicationUseCase: GetBuyerCommunicationUseCase,
    private val getHomeBannerUseCase: GetHomeBannerUseCase,
    private val startQuestUseCase: StartQuestUseCase,
    private val playWidgetTools: PlayWidgetTools,
    private val addressData: TokoNowLocalAddress,
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers,
    addToCartUseCase: AddToCartUseCase,
    updateCartUseCase: UpdateCartUseCase,
    deleteCartUseCase: DeleteCartUseCase,
    affiliateService: NowAffiliateService,
    getTargetedTickerUseCase: GetTargetedTickerUseCase
) : BaseTokoNowViewModel(
    addToCartUseCase,
    updateCartUseCase,
    deleteCartUseCase,
    getMiniCartUseCase,
    affiliateService,
    getTargetedTickerUseCase,
    addressData,
    userSession,
    dispatchers
) {

    companion object {
        private const val DEFAULT_INDEX = 1
        private const val SUCCESS_CODE = "200"
        private const val QUERY_PARAM_PRODUCT_BUNDLE = "type=SINGLE,MULTIPLE&pageName=now_homepage"
        private const val SWITCH_PRODUCT_CAROUSEL_TAB_DELAY = 500L
    }

    val homeLayoutList: LiveData<Result<HomeLayoutListUiModel>>
        get() = _homeLayoutList
    val keywordSearch: LiveData<SearchPlaceholder>
        get() = _keywordSearch
    val chooseAddress: LiveData<Result<GetStateChosenAddressResponse>>
        get() = _chooseAddress
    val homeAddToCartTracker: LiveData<HomeAddToCartTracker>
        get() = _homeAddToCartTracker
    val homeRemoveFromCartTracker: LiveData<HomeRemoveFromCartTracker>
        get() = _homeRemoveFromCartTracker
    val atcQuantity: LiveData<Result<HomeLayoutListUiModel>>
        get() = _atcQuantity
    val openScreenTracker: LiveData<String>
        get() = _openScreenTracker
    val getReferralResult: LiveData<Result<HomeReferralDataModel>>
        get() = _getReferralResult
    val invalidatePlayImpression: LiveData<Boolean>
        get() = _invalidatePlayImpression
    val updateToolbarNotification: LiveData<Boolean>
        get() = _updateToolbarNotification
    val referralEvaluate: LiveData<Result<HomeReceiverReferralDialogUiModel>>
        get() = _referralEvaluate
    val couponClaimed: LiveData<Result<HomeClaimCouponDataModel>>
        get() = _couponClaimed
    val blockAddToCart: LiveData<Unit>
        get() = _blockAddToCart
    val startQuestResult: LiveData<Result<StartQuestResponse>>
        get() = _startQuestResult

    private val _homeLayoutList = MutableLiveData<Result<HomeLayoutListUiModel>>()
    private val _keywordSearch = MutableLiveData<SearchPlaceholder>()
    private val _chooseAddress = MutableLiveData<Result<GetStateChosenAddressResponse>>()
    private val _homeAddToCartTracker = MutableLiveData<HomeAddToCartTracker>()
    private val _homeRemoveFromCartTracker = MutableLiveData<HomeRemoveFromCartTracker>()
    private val _atcQuantity = MutableLiveData<Result<HomeLayoutListUiModel>>()
    private val _openScreenTracker = MutableLiveData<String>()
    private val _getReferralResult = MutableLiveData<Result<HomeReferralDataModel>>()
    private val _invalidatePlayImpression = MutableLiveData<Boolean>()
    private val _updateToolbarNotification = MutableLiveData<Boolean>()
    private val _referralEvaluate = MutableLiveData<Result<HomeReceiverReferralDialogUiModel>>()
    private val _couponClaimed = MutableLiveData<Result<HomeClaimCouponDataModel>>()
    private val _startQuestResult = MutableLiveData<Result<StartQuestResponse>>()

    private val homeLayoutItemList = mutableListOf<HomeLayoutItemUiModel?>()
    private var channelToken = ""
    private var getHomeLayoutJob: Job? = null
    private var getMiniCartJob: Job? = null
    private var switchProductCarouselChipJob: Job? = null

    var isEmptyState = false

    fun trackOpeningScreen(screenName: String) {
        _openScreenTracker.value = screenName
    }

    fun getLoadingState() {
        channelToken = ""
        isEmptyState = false

        homeLayoutItemList.addLoadingIntoList(addressData)
        val data = HomeLayoutListUiModel(
            items = getHomeVisitableList(),
            state = TokoNowLayoutState.LOADING
        )

        _homeLayoutList.postValue(Success(data))
    }

    fun getEmptyState(@HomeStaticLayoutId id: String, serviceType: String) {
        launchCatchError(block = {
            isEmptyState = true
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

    fun getHomeLayout(
        localCacheModel: LocalCacheModel,
        removeAbleWidgets: List<HomeRemoveAbleWidget>
    ) {
        getHomeLayoutJob?.cancel()
        launchCatchError(block = {
            val warehouseId = localCacheModel.warehouse_id
            val tickerPage = GetTargetedTickerUseCase.HOME_PAGE
            val tickerData = getTickerDataAsync(warehouseId, tickerPage).await()

            val homeLayoutResponse = getHomeLayoutDataUseCase.execute(
                localCacheModel = localCacheModel
            )

            channelToken = homeLayoutResponse.first().token
            hasBlockedAddToCart = tickerData?.blockAddToCart.orFalse()

            homeLayoutItemList.mapHomeLayoutList(
                response = homeLayoutResponse,
                removeAbleWidgets = removeAbleWidgets,
                miniCartData = miniCartData,
                localCacheModel = localCacheModel,
                hasBlockedAddToCart = hasBlockedAddToCart,
                tickerList = tickerData?.tickerList.orEmpty()
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
                    response = homeLayoutResponse,
                    removeAbleWidgets = removeAbleWidgets,
                    miniCartData = miniCartData,
                    localCacheModel = localCacheModel,
                    hasBlockedAddToCart = hasBlockedAddToCart
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
        if (shopId.isNotEmpty() && warehouseId.toLongOrZero() != 0L && userSession.isLoggedIn) {
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
        launchCatchError(block = {
            _chooseAddress.postValue(Success(getChooseAddressWarehouseLocUseCase(source)))
        }) {
            _chooseAddress.postValue(Fail(it))
        }
    }

    fun getCategoryMenu(addressData: LocalCacheModel) {
        launchCatchError(block = {
            val warehouseId = addressData.warehouse_id
            val response = getCategoryList(addressData)
            homeLayoutItemList.mapHomeCategoryMenuData(response, warehouseId)
            updateVisitableList()
        }) {
            homeLayoutItemList.mapHomeCategoryMenuData(null)
            updateVisitableList()
        }
    }

    fun getCatalogCouponList(
        widgetId: String,
        slugs: List<String>
    ) {
        launchCatchError(block = {
            val response = getCatalogCouponListUseCase.execute(
                catalogSlugs = slugs
            )
            homeLayoutItemList.mapHomeCatalogCouponList(
                response = response,
                widgetId = widgetId,
                slugs = slugs,
                state = TokoNowLayoutState.SHOW
            )
            updateVisitableList()
        }) {
            homeLayoutItemList.mapHomeCatalogCouponList(
                widgetId = widgetId,
                slugs = slugs,
                state = TokoNowLayoutState.HIDE
            )
            updateVisitableList()
        }
    }

    fun claimCoupon(
        widgetId: String,
        catalogId: String,
        couponStatus: String,
        position: Int,
        slugText: String,
        couponName: String,
        warehouseId: String
    ) {
        launchCatchError(block = {
            if (userSession.isLoggedIn) {
                val response = redeemCouponUseCase.execute(catalogId)
                val coupon = response.mapToClaimCouponDataModel(
                    couponStatus = couponStatus,
                    position = position,
                    slugText = slugText,
                    couponName = couponName,
                    warehouseId = warehouseId
                )
                _couponClaimed.postValue(Success(coupon))

                homeLayoutItemList.mapHomeClaimCouponList(
                    widgetId = widgetId,
                    catalogId = catalogId,
                    ctaText = COUPON_STATUS_CLAIMED
                )
                updateVisitableList()
            } else {
                _couponClaimed.postValue(Success(HomeClaimCouponDataModel(code = COUPON_STATUS_LOGIN)))
            }
        }) {
            _couponClaimed.postValue(Fail(it))
        }
    }

    fun onCartQuantityChanged(
        channelId: String = "",
        productId: String,
        quantity: Int,
        shopId: String,
        stock: Int,
        isVariant: Boolean,
        @TokoNowLayoutType type: String
    ) {
        onCartQuantityChanged(
            productId = productId,
            shopId = shopId,
            quantity = quantity,
            stock = stock,
            isVariant = isVariant,
            onSuccessAddToCart = {
                updateProductCartQuantity(productId, quantity, type)
                checkRealTimeRecommendation(channelId, productId, type)
                trackProductAddToCart(productId, quantity, type, it.data.cartId)
                updateToolbarNotification()
            },
            onSuccessUpdateCart = { miniCartItem, _ ->
                val cartId = miniCartItem.cartId
                updateProductCartQuantity(productId, quantity, type)
                trackProductUpdateCart(productId, quantity, type, cartId)
                updateToolbarNotification()
            },
            onSuccessDeleteCart = { miniCartItem, _ ->
                updateProductCartQuantity(productId, DEFAULT_QUANTITY, type)
                trackProductRemoveCart(productId, type, miniCartItem.cartId)
                updateToolbarNotification()
            },
            onError = {
                updateProductCartQuantity(productId, quantity, type)
            }
        )
    }

    fun setProductAddToCartQuantity(miniCart: MiniCartSimplifiedData) {
        launchCatchError(block = {
            setMiniCartAndProductQuantity(miniCart)
            val data = HomeLayoutListUiModel(
                items = getHomeVisitableList(),
                state = TokoNowLayoutState.UPDATE
            )
            updateAtcQuantity(data)
        }) {}
    }

    fun updateToolbarNotification() {
        _updateToolbarNotification.postValue(true)
    }

    fun removeLeftCarouselAtc(id: String) {
        launchCatchError(block = {
            homeLayoutItemList.removeItem(id)
            updateVisitableList()
        }) {}
    }

    fun removeWidget(id: String) {
        launchCatchError(block = {
            homeLayoutItemList.removeItem(id)
            updateVisitableList()
        }) {}
    }

    fun getRepurchaseProducts(): List<TokoNowRepurchaseProductUiModel> {
        val item = homeLayoutItemList.firstOrNull { it?.layout is TokoNowRepurchaseUiModel }
        val repurchase = item?.layout as? TokoNowRepurchaseUiModel
        return repurchase?.productList.orEmpty()
    }

    fun refreshQuestWidget() {
        homeLayoutItemList.getItem(HomeQuestReloadWidgetUiModel::class.java)?.let { uiModel ->
            launchCatchError(block = {
                getQuestListData(
                    id = uiModel.id,
                    mainTitle = uiModel.mainTitle,
                    finishedWidgetTitle = uiModel.finishedWidgetTitle,
                    finishedWidgetContentDescription = uiModel.finishedWidgetContentDescription
                )

                updateVisitableList()
            }) {
                removeWidget(uiModel.id)
            }
        }
    }

    fun autoRefreshPlayWidget(item: HomePlayWidgetUiModel) {
        launchCatchError(block = {
            val playWidgetUiModel = getPlayWidget(item, isAutoRefresh = true)
            homeLayoutItemList.mapPlayWidgetData(playWidgetUiModel)
            updateVisitableList()
        }, onError = {
                removeWidget(item.id)
            })
    }

    fun updatePlayWidget(channelId: String, totalView: String) {
        launchCatchError(block = {
            homeLayoutItemList.updatePlayWidget(channelId, totalView)
            updateVisitableList()
        }) {
            // do nothing
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
                    else -> getTokoNowGlobalComponent(item, localCacheModel) // TokoNow Common Component
                }

                updateVisitableList()
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

    fun switchProductCarouselChipTab(channelId: String, chipId: String) {
        val carouselModel = homeLayoutItemList.getProductCarouselChipsItem(channelId)
        val currentSelectedChipId = getCurrentSelectedChipId(carouselModel)
        if (carouselModel == null || currentSelectedChipId == chipId) return
        switchProductCarouselChipJob?.cancel()

        launchCatchError(block = {
            val selectedChip = carouselModel.chipList.first { it.id == chipId }
            setProductCarouselChipsLoading(carouselModel, selectedChip)
            delay(SWITCH_PRODUCT_CAROUSEL_TAB_DELAY)

            getCarouselChipsProductList(carouselModel, selectedChip)

            updateVisitableList()
        }) {
        }.let {
            switchProductCarouselChipJob = it
        }
    }

    fun startQuest(channelId: String, questId: Int) {
        launchCatchError(block = {
            homeLayoutItemList.updateQuestWidgetUiModel(
                channelId = channelId,
                questId = questId,
                isLoading = true
            )

            updateVisitableList()

            val response = startQuestUseCase.execute(questId)

            homeLayoutItemList.updateQuestWidgetUiModel(
                channelId = channelId,
                questId = questId,
                isLoading = false,
                showStartBtn = false,
                isIdle = false
            )

            _startQuestResult.postValue(Success(response))

            updateVisitableList()
        }) {
            _startQuestResult.postValue(Fail(it))

            homeLayoutItemList.updateQuestWidgetUiModel(
                channelId = channelId,
                questId = questId,
                isLoading = false
            )

            updateVisitableList()
        }
    }

    private fun setProductCarouselChipsLoading(
        carouselModel: HomeProductCarouselChipsUiModel,
        selectedChip: TokoNowChipUiModel
    ) {
        homeLayoutItemList.setProductCarouselChipsLoading(
            carouselModel,
            selectedChip
        )
        updateVisitableList()
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
            is HomeSharingEducationWidgetUiModel -> getSharingEducationAsync(item, localCacheModel).await()
            is HomeSharingReferralWidgetUiModel -> getSharingReferralAsync(item).await()
            is HomeQuestShimmeringWidgetUiModel -> getQuestListAsync(item).await()
            is HomePlayWidgetUiModel -> getPlayWidgetAsync(item).await()
            is HomeClaimCouponWidgetUiModel -> getCatalogCouponListAsync(item).await()
            is HomeProductCarouselChipsUiModel -> getCarouselChipsProductListAsync(item).await()
            is HomeHeaderUiModel -> getGetBuyerCommunicationAsync(item).await()
            else -> removeUnsupportedLayout(item)
        }
    }

    /**
     * Get data from additional query for TokopediaNOW Common Component.
     * Add use case and data mapping for TokopediaNOW Common Component here.
     * Example: Category Grid get its data from getCategoryListUseCase.
     *
     * @param item TokopediaNOW component item
     * @param addressData obtained from choose address widget
     */
    private suspend fun getTokoNowGlobalComponent(item: Visitable<*>?, addressData: LocalCacheModel) {
        when (item) {
            is TokoNowCategoryMenuUiModel -> getCategoryMenuDataAsync(addressData).await()
            is TokoNowRepurchaseUiModel -> getRepurchaseDataAsync(item, addressData).await()
            is TokoNowBundleUiModel -> getProductBundleRecomAsync(item).await()
            is BannerDataModel -> getBannerDataAsync(item).await()
            else -> removeUnsupportedLayout(item)
        }
    }

    private fun getBannerDataAsync(item: BannerDataModel): Deferred<Unit?> {
        return asyncCatchError(block = {
            val response = getHomeBannerUseCase.execute(addressData.getAddressData())
            homeLayoutItemList.mapHomeBanner(item, response)
        }) {
            homeLayoutItemList.removeItem(item.visitableId())
        }
    }

    private suspend fun getRepurchaseDataAsync(
        item: TokoNowRepurchaseUiModel,
        addressData: LocalCacheModel
    ): Deferred<Unit?> {
        return asyncCatchError(block = {
            val warehouses = AddressMapper.mapToWarehousesData(addressData)
            val response = getRepurchaseWidgetUseCase.execute(warehouses)
            if (response.products.isNotEmpty()) {
                homeLayoutItemList.mapProductPurchaseData(
                    item = item,
                    response = response,
                    miniCartData = miniCartData,
                    blockAddToCart = hasBlockedAddToCart
                )
            } else {
                homeLayoutItemList.removeItem(item.id)
            }
        }) {
            homeLayoutItemList.removeItem(item.id)
        }
    }

    private suspend fun getCategoryMenuDataAsync(
        addressData: LocalCacheModel
    ): Deferred<Unit?> {
        return asyncCatchError(block = {
            val warehouseId = addressData.warehouse_id
            val response = getCategoryList(addressData)
            homeLayoutItemList.mapHomeCategoryMenuData(response, warehouseId)
        }) {
            homeLayoutItemList.mapHomeCategoryMenuData(emptyList())
        }
    }

    private suspend fun getProductBundleRecomAsync(
        item: TokoNowBundleUiModel
    ): Deferred<Unit?> {
        return asyncCatchError(block = {
            val response = getProductBundleRecomUseCase.execute(
                queryParam = QUERY_PARAM_PRODUCT_BUNDLE
            )
            homeLayoutItemList.mapProductBundleRecomData(item, response)
        }) {
            homeLayoutItemList.removeItem(item.id)
        }
    }

    private suspend fun getCatalogCouponListAsync(item: HomeClaimCouponWidgetUiModel): Deferred<Unit?> {
        return asyncCatchError(block = {
            val response = getCatalogCouponListUseCase.execute(
                catalogSlugs = item.slugs
            )
            val isDouble = item.isDouble && item.slugs.size == COUPON_WIDGET_DOUBLE_SLUG_SIZE
            val isSingle = !item.isDouble && item.slugs.size == COUPON_WIDGET_SINGLE_SLUG_SIZE
            if ((isDouble || isSingle)) {
                homeLayoutItemList.mapHomeCatalogCouponList(
                    widgetId = item.id,
                    response = response,
                    state = TokoNowLayoutState.SHOW
                )
            } else {
                homeLayoutItemList.removeItem(item.id)
            }
        }) {
            homeLayoutItemList.mapHomeCatalogCouponList(
                widgetId = item.id,
                state = TokoNowLayoutState.HIDE
            )
        }
    }

    private suspend fun getCarouselChipsProductListAsync(
        carouselModel: HomeProductCarouselChipsUiModel
    ): Deferred<Unit?> {
        return asyncCatchError(block = {
            val chipList = carouselModel.chipList
            val selectedChip = chipList.first()
            getCarouselChipsProductList(carouselModel, selectedChip)
        }) {
        }
    }

    private suspend fun getQuestListAsync(item: HomeQuestShimmeringWidgetUiModel): Deferred<Unit?> {
        return asyncCatchError(block = {
            getQuestListData(
                id = item.id,
                mainTitle = item.mainTitle,
                finishedWidgetTitle = item.finishedWidgetTitle,
                finishedWidgetContentDescription = item.finishedWidgetContentDescription
            )
        }) {
            homeLayoutItemList.removeItem(item.visitableId)
        }
    }

    private suspend fun getQuestListData(
        id: String,
        mainTitle: String,
        finishedWidgetTitle: String,
        finishedWidgetContentDescription: String
    ) {
        val questListResponse = getQuestWidgetListUseCase.execute().questWidgetList
        if (questListResponse.questWidgetList.isEmpty() && questListResponse.resultStatus.code == SUCCESS_CODE) {
            homeLayoutItemList.removeItem(id)
        } else if (questListResponse.resultStatus.code != SUCCESS_CODE) {
            homeLayoutItemList.mapQuestReloadWidget(
                id = id,
                mainTitle = mainTitle,
                finishedWidgetTitle = finishedWidgetTitle,
                finishedWidgetContentDescription = finishedWidgetContentDescription
            )
        } else {
            val questList = QuestMapper.mapQuestCardData(id, questListResponse.questWidgetList)
            val currentQuestFinished = questList.filter { it.isFinished() }.size
            val totalQuestTarget = questList.size
            if (currentQuestFinished == totalQuestTarget) {
                homeLayoutItemList.mapQuestFinishedWidget(
                    id = id,
                    title = finishedWidgetTitle,
                    contentDescription = finishedWidgetContentDescription
                )
            } else {
                homeLayoutItemList.mapQuestWidget(
                    id = id,
                    title = mainTitle,
                    questList = questList
                )
            }
        }
    }

    private suspend fun getSharingEducationAsync(
        item: HomeSharingEducationWidgetUiModel,
        addressData: LocalCacheModel
    ): Deferred<Unit?> {
        return asyncCatchError(block = {
            val warehouses = AddressMapper.mapToWarehousesData(addressData)
            val response = getRepurchaseWidgetUseCase.execute(warehouses)
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

    private suspend fun getCategoryList(
        addressData: LocalCacheModel
    ): List<GetCategoryListResponse.CategoryListResponse.CategoryResponse> {
        val warehouses = AddressMapper.mapToWarehousesData(addressData)
        return getCategoryListUseCase.execute(warehouses, CATEGORY_LEVEL_DEPTH).data
    }

    private suspend fun getPlayWidgetAsync(item: HomePlayWidgetUiModel): Deferred<Unit?> {
        return asyncCatchError(block = {
            val playWidgetUiModel = getPlayWidget(item, isAutoRefresh = false)

            if (playWidgetUiModel.playWidgetState.model.items.isNotEmpty()) {
                homeLayoutItemList.mapPlayWidgetData(playWidgetUiModel)
            } else {
                homeLayoutItemList.removeItem(item.id)
            }

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

    private fun getGetBuyerCommunicationAsync(item: HomeHeaderUiModel): Deferred<Unit?> {
        return asyncCatchError(block = {
            val warehouses = addressData.getWarehousesData()
            val response = getBuyerCommunicationUseCase.execute(addressData)
            homeLayoutItemList.mapToHomeHeaderUiModel(item, warehouses, response)
        }) {
            homeLayoutItemList.mapHomeHeaderErrorState(item)
        }
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
        updateVisitableList()
    }

    fun removeRealTimeRecommendation(channelId: String, @TokoNowLayoutType type: String) {
        launchCatchError(block = {
            homeLayoutItemList.removeRealTimeRecommendation(channelId, type)
            updateVisitableList()
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
                    miniCartData,
                    hasBlockedAddToCart,
                    type
                )
            } else {
                homeLayoutItemList.mapLatestRealTimeRecommendation(channelId, type)
            }

            updateVisitableList()
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

        updateVisitableList()
    }

    private fun getRefreshRealTimeRecommendation(channelId: String, productId: String) {
        homeLayoutItemList.mapRefreshRealTimeRecommendation(
            channelId = channelId,
            productId = productId
        )

        updateVisitableList()
    }

    private suspend fun getCarouselChipsProductList(
        carouselModel: HomeProductCarouselChipsUiModel,
        selectedChip: TokoNowChipUiModel
    ) {
        val requestParam = mapToRecomRequestParam(selectedChip.param)
        val recommendationWidgets = getRecommendationUseCase.getData(requestParam)
        val recommendationWidget = recommendationWidgets.first()

        homeLayoutItemList.mapProductCarouselChipsWidget(
            carouselModel,
            recommendationWidget,
            miniCartData,
            selectedChip,
            hasBlockedAddToCart
        )
    }

    private fun updateProductCartQuantity(
        productId: String,
        quantity: Int,
        @TokoNowLayoutType type: String
    ) {
        homeLayoutItemList.updateProductQuantity(productId, quantity, type)

        val data = HomeLayoutListUiModel(
            items = getHomeVisitableList(),
            state = TokoNowLayoutState.UPDATE
        )

        updateAtcQuantity(data)
    }

    private fun setMiniCartAndProductQuantity(miniCart: MiniCartSimplifiedData) {
        setMiniCartData(miniCart)
        updateProductQuantity(miniCart)
    }

    private fun updateProductQuantity(miniCart: MiniCartSimplifiedData) {
        homeLayoutItemList.updateRepurchaseProductQuantity(miniCart)
        homeLayoutItemList.updateProductRecomQuantity(miniCart)
        homeLayoutItemList.updateLeftCarouselProductQuantity(miniCart)
        homeLayoutItemList.updateCarouselChipsQuantity(miniCart)
    }

    private fun trackProductAddToCart(
        productId: String,
        quantity: Int,
        @TokoNowLayoutType type: String,
        cartId: String
    ) {
        when (type) {
            REPURCHASE_PRODUCT -> {
                trackRepurchaseAddToCart(productId, quantity, cartId)
            }
            PRODUCT_RECOM -> trackRecentProductRecomAddToCart(productId, quantity, cartId)
            MIX_LEFT_CAROUSEL_ATC -> trackLeftCarouselAddToCart(productId, quantity, cartId)
            CHIP_CAROUSEL -> trackCarouselChipAddToCart(productId, quantity, cartId)
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

    private fun trackCarouselChipAddToCart(productId: String, quantity: Int, cartId: String) {
        homeLayoutItemList.getProductCarouselChipByProductId(productId)?.let { carousel ->
            val productList = carousel.carouselItemList
                .filterIsInstance<ProductCardCompactCarouselItemUiModel>()
            val product = productList.first { it.getProductId() == productId }
            val position = productList.indexOf(product)

            val data = HomeAddToCartTracker(
                position,
                quantity,
                cartId,
                carousel
            )

            _homeAddToCartTracker.postValue(data)
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
        updateVisitableList()
    }

    private fun getHomeVisitableList(): List<Visitable<*>> {
        val layoutItemsList = homeLayoutItemList.toMutableList()
        return layoutItemsList.filterNotNull().mapNotNull { it.layout }
    }

    private fun removeUnsupportedLayout(item: Visitable<*>?) {
        homeLayoutItemList.removeItem(item?.getVisitableId())
    }

    private fun updateAtcQuantity(data: HomeLayoutListUiModel) {
        if (getHomeLayoutJob?.isCompleted == true) {
            _atcQuantity.postValue(Success(data))
        }
    }

    private fun updateVisitableList() {
        val data = HomeLayoutListUiModel(
            getHomeVisitableList(),
            TokoNowLayoutState.UPDATE
        )

        _homeLayoutList.postValue(Success(data))
    }
}
