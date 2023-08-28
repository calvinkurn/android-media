package com.tokopedia.cartrevamp.view

import android.os.Bundle
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.addon.presentation.uimodel.AddOnUIModel
import com.tokopedia.atc_common.AtcFromExternalSource
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cart.data.model.request.CartShopGroupTickerAggregatorParam
import com.tokopedia.cart.data.model.request.UpdateCartWrapperRequest
import com.tokopedia.cart.data.model.response.promo.LastApplyPromo
import com.tokopedia.cart.data.model.response.promo.LastApplyPromoData
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.cart.domain.usecase.CartShopGroupTickerAggregatorUseCase
import com.tokopedia.cart.domain.usecase.FollowShopUseCase
import com.tokopedia.cart.domain.usecase.GetCartParam
import com.tokopedia.cart.domain.usecase.GetCartRevampV4UseCase
import com.tokopedia.cart.domain.usecase.UpdateAndReloadCartUseCase
import com.tokopedia.cart.domain.usecase.UpdateCartAndGetLastApplyUseCase
import com.tokopedia.cart.view.CartIdlingResource
import com.tokopedia.cart.view.analytics.EnhancedECommerceActionFieldData
import com.tokopedia.cart.view.analytics.EnhancedECommerceClickData
import com.tokopedia.cart.view.analytics.EnhancedECommerceData
import com.tokopedia.cart.view.analytics.EnhancedECommerceProductData
import com.tokopedia.cartcommon.data.request.updatecart.BundleInfo
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UndoDeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.cartrevamp.domain.usecase.SetCartlistCheckboxStateUseCase
import com.tokopedia.cartrevamp.view.helper.CartDataHelper
import com.tokopedia.cartrevamp.view.mapper.CartUiModelMapper
import com.tokopedia.cartrevamp.view.mapper.PromoRequestMapper
import com.tokopedia.cartrevamp.view.processor.CartCalculator
import com.tokopedia.cartrevamp.view.processor.CartPromoEntryPointProcessor
import com.tokopedia.cartrevamp.view.uimodel.AddCartToWishlistV2Event
import com.tokopedia.cartrevamp.view.uimodel.AddToCartEvent
import com.tokopedia.cartrevamp.view.uimodel.AddToCartExternalEvent
import com.tokopedia.cartrevamp.view.uimodel.CartAddOnProductData
import com.tokopedia.cartrevamp.view.uimodel.CartBundlingBottomSheetData
import com.tokopedia.cartrevamp.view.uimodel.CartCheckoutButtonState
import com.tokopedia.cartrevamp.view.uimodel.CartEmptyHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartGlobalEvent
import com.tokopedia.cartrevamp.view.uimodel.CartGroupHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartItemTickerErrorHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartLoadingHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartModel
import com.tokopedia.cartrevamp.view.uimodel.CartMutableLiveData
import com.tokopedia.cartrevamp.view.uimodel.CartRecentViewHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartRecentViewItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartSectionHeaderHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartSelectedAmountHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartShopBottomHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartShopGroupTickerState
import com.tokopedia.cartrevamp.view.uimodel.CartState
import com.tokopedia.cartrevamp.view.uimodel.CartTopAdsHeadlineData
import com.tokopedia.cartrevamp.view.uimodel.CartTrackerEvent
import com.tokopedia.cartrevamp.view.uimodel.CartWishlistHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartWishlistItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.DeleteCartEvent
import com.tokopedia.cartrevamp.view.uimodel.DisabledAccordionHolderData
import com.tokopedia.cartrevamp.view.uimodel.DisabledCollapsedHolderData
import com.tokopedia.cartrevamp.view.uimodel.DisabledItemHeaderHolderData
import com.tokopedia.cartrevamp.view.uimodel.DisabledReasonHolderData
import com.tokopedia.cartrevamp.view.uimodel.EntryPointInfoEvent
import com.tokopedia.cartrevamp.view.uimodel.FollowShopEvent
import com.tokopedia.cartrevamp.view.uimodel.LoadRecentReviewState
import com.tokopedia.cartrevamp.view.uimodel.LoadRecommendationState
import com.tokopedia.cartrevamp.view.uimodel.LoadWishlistV2State
import com.tokopedia.cartrevamp.view.uimodel.PromoSummaryDetailData
import com.tokopedia.cartrevamp.view.uimodel.RemoveFromWishlistEvent
import com.tokopedia.cartrevamp.view.uimodel.SeamlessLoginEvent
import com.tokopedia.cartrevamp.view.uimodel.UndoDeleteEvent
import com.tokopedia.cartrevamp.view.uimodel.UpdateCartAndGetLastApplyEvent
import com.tokopedia.cartrevamp.view.uimodel.UpdateCartCheckoutState
import com.tokopedia.cartrevamp.view.uimodel.UpdateCartPromoState
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.toZeroStringIfNullOrBlank
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.logisticcart.shipping.model.Product
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.ShippingParam
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.productbundlewidget.model.BundleDetailUiModel
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceAdd
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCartMapData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCheckout
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceRecomProductCartMapData
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoOrder
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoOrderData
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.UserGroupMetadata
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.sellercashback.ShipmentSellerCashbackModel
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.extension.hasLabelGroupFulfillment
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.seamless_login_common.subscriber.SeamlessLoginSubscriber
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopProductUiModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.wishlistcommon.data.WishlistV2Params
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.GetWishlistV2UseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rx.subscriptions.CompositeSubscription
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.math.min

class CartViewModel @Inject constructor(
    private val getCartRevampV4UseCase: GetCartRevampV4UseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val undoDeleteCartUseCase: UndoDeleteCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val compositeSubscription: CompositeSubscription,
    private val addToWishlistV2UseCase: AddToWishlistV2UseCase,
    private val deleteWishlistV2UseCase: DeleteWishlistV2UseCase,
    private val updateAndReloadCartUseCase: UpdateAndReloadCartUseCase,
    private val userSessionInterface: UserSessionInterface,
    private val clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase,
    private val getRecentViewUseCase: GetRecommendationUseCase,
    private val getWishlistV2UseCase: GetWishlistV2UseCase,
    private val getRecommendationUseCase: GetRecommendationUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val addToCartExternalUseCase: AddToCartExternalUseCase,
    private val seamlessLoginUsecase: SeamlessLoginUsecase,
    private val updateCartCounterUseCase: UpdateCartCounterUseCase,
    private val updateCartAndGetLastApplyUseCase: UpdateCartAndGetLastApplyUseCase,
    private val setCartlistCheckboxStateUseCase: SetCartlistCheckboxStateUseCase,
    private val followShopUseCase: FollowShopUseCase,
    private val cartShopGroupTickerAggregatorUseCase: CartShopGroupTickerAggregatorUseCase,
    private val cartPromoEntryPointProcessor: CartPromoEntryPointProcessor,
    private val schedulers: ExecutorSchedulers,
    private val dispatchers: CoroutineDispatchers,
    private val cartCalculator: CartCalculator
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + dispatchers.immediate

    var cartModel: CartModel = CartModel()
        private set

    val cartDataList: CartMutableLiveData<ArrayList<Any>> =
        CartMutableLiveData(arrayListOf())

    private val _globalEvent: MutableLiveData<CartGlobalEvent> = MutableLiveData()
    val globalEvent: LiveData<CartGlobalEvent> = _globalEvent

    private val _loadCartState: MutableLiveData<CartState<CartData>> = MutableLiveData()
    val loadCartState: LiveData<CartState<CartData>> = _loadCartState

    private val _updateCartForCheckoutState: MutableLiveData<UpdateCartCheckoutState> =
        MutableLiveData()
    val updateCartForCheckoutState: LiveData<UpdateCartCheckoutState> = _updateCartForCheckoutState

    private val _updateCartForPromoState: MutableLiveData<UpdateCartPromoState> = MutableLiveData()
    val updateCartForPromoState: LiveData<UpdateCartPromoState> = _updateCartForPromoState

    private val _cartCheckoutButtonState: MutableLiveData<CartCheckoutButtonState> =
        MutableLiveData()
    val cartCheckoutButtonState: LiveData<CartCheckoutButtonState> = _cartCheckoutButtonState

    private val _recentViewState: MutableLiveData<LoadRecentReviewState> = MutableLiveData()
    val recentViewState: LiveData<LoadRecentReviewState> = _recentViewState

    private val _wishlistV2State: MutableLiveData<LoadWishlistV2State> = MutableLiveData()
    val wishlistV2State: LiveData<LoadWishlistV2State> = _wishlistV2State

    private val _recommendationState: MutableLiveData<LoadRecommendationState> = MutableLiveData()
    val recommendationState: LiveData<LoadRecommendationState> = _recommendationState

    private val _updateCartAndGetLastApplyEvent: MutableLiveData<UpdateCartAndGetLastApplyEvent> =
        MutableLiveData()
    val updateCartAndGetLastApplyEvent: LiveData<UpdateCartAndGetLastApplyEvent> =
        _updateCartAndGetLastApplyEvent

    private val _selectedAmountState: CartMutableLiveData<Int> = CartMutableLiveData(0)
    val selectedAmountState: CartMutableLiveData<Int> = _selectedAmountState

    private val _cartTrackerEvent: MutableLiveData<CartTrackerEvent> = MutableLiveData()
    val cartTrackerEvent: LiveData<CartTrackerEvent> = _cartTrackerEvent

    private val _addToCartEvent: MutableLiveData<AddToCartEvent> = MutableLiveData()
    val addToCartEvent: LiveData<AddToCartEvent> = _addToCartEvent

    private val _addCartToWishlistV2Event: MutableLiveData<AddCartToWishlistV2Event> =
        MutableLiveData()
    val addCartToWishlistV2Event: LiveData<AddCartToWishlistV2Event> = _addCartToWishlistV2Event

    private val _deleteCartEvent: MutableLiveData<DeleteCartEvent> = MutableLiveData()
    val deleteCartEvent: LiveData<DeleteCartEvent> = _deleteCartEvent

    private val _undoDeleteEvent: MutableLiveData<UndoDeleteEvent> = MutableLiveData()
    val undoDeleteEvent: LiveData<UndoDeleteEvent> = _undoDeleteEvent

    private val _removeFromWishlistEvent: MutableLiveData<RemoveFromWishlistEvent> =
        MutableLiveData()
    val removeFromWishlistEvent: LiveData<RemoveFromWishlistEvent> = _removeFromWishlistEvent

    private val _seamlessLoginEvent: MutableLiveData<SeamlessLoginEvent> = MutableLiveData()
    val seamlessLoginEvent: LiveData<SeamlessLoginEvent> = _seamlessLoginEvent

    private val _addToCartExternalEvent: MutableLiveData<AddToCartExternalEvent> = MutableLiveData()
    val addToCartExternalEvent: LiveData<AddToCartExternalEvent> = _addToCartExternalEvent

    private val _followShopEvent: MutableLiveData<FollowShopEvent> = MutableLiveData()
    val followShopEvent: LiveData<FollowShopEvent> = _followShopEvent

    private val _tokoNowProductUpdater =
        MutableSharedFlow<Boolean>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val tokoNowProductUpdater: SharedFlow<Boolean> = _tokoNowProductUpdater

    private val _entryPointInfoEvent = MutableLiveData<EntryPointInfoEvent>()
    val entryPointInfoEvent: LiveData<EntryPointInfoEvent>
        get() = _entryPointInfoEvent

    private var cartShopGroupTickerJob: Job? = null

    companion object {
        private const val CART_SHOP_GROUP_TICKER_DELAY = 500L
        private const val BO_AFFORDABILITY_WEIGHT_KILO = 1000

        const val GET_CART_STATE_DEFAULT = 0

        const val RECOMMENDATION_START_PAGE = 1
        const val ITEM_CHECKED_ALL_WITHOUT_CHANGES = 0
        const val ITEM_CHECKED_ALL_WITH_CHANGES = 1
        const val ITEM_CHECKED_PARTIAL_SHOP = 3
        const val ITEM_CHECKED_PARTIAL_ITEM = 4
        const val ITEM_CHECKED_PARTIAL_SHOP_AND_ITEM = 5
        private const val RECENT_VIEW_XSOURCE = "recentview"
        private const val PAGE_NAME_RECENT_VIEW = "cart_recent_view"

        private const val QUERY_APP_CLIENT_ID = "{app_client_id}"
        private val REGEX_NUMBER = "[^0-9]".toRegex()
        private const val SOURCE_CART = "cart"

        private const val MAX_TOTAL_AMOUNT_ELIGIBLE_FOR_COD = 1000000.0
    }

    fun dataHasChanged(): Boolean {
        var hasChanges = false
        CartDataHelper.getAllCartItemData(
            cartDataList.value,
            cartModel
        ).let {
            for (cartItemHolderData in it) {
                if (cartItemHolderData.quantity != cartItemHolderData.originalQty || cartItemHolderData.notes != cartItemHolderData.originalNotes ||
                    (cartItemHolderData.isBundlingItem && cartItemHolderData.bundleQuantity != cartItemHolderData.originalBundleQuantity)
                ) {
                    hasChanges = true
                    break
                }
            }
            if (hasChanges) {
                for (cartItemHolderData in it) {
                    cartItemHolderData.originalQty = cartItemHolderData.quantity
                    cartItemHolderData.originalNotes = cartItemHolderData.notes
                }
            }
        }

        return hasChanges
    }

    fun setAllAvailableItemCheck(checked: Boolean) {
        cartDataList.value.forEachIndexed { index, data ->
            when (data) {
                is CartGroupHolderData -> {
                    if (!data.isError && data.isAllSelected != checked) {
                        data.isAllSelected = checked
                        data.isPartialSelected = false
                        if (data.isCollapsed) {
                            data.productUiModelList.forEach { product ->
                                if (product.isSelected != checked) {
                                    product.isSelected = checked
                                }
                            }
                        }
                        data.cartShopGroupTicker.state = CartShopGroupTickerState.FIRST_LOAD
                        _globalEvent.value = CartGlobalEvent.AdapterItemChanged(index)
                    } else {
                        return@forEachIndexed
                    }
                }

                is CartItemHolderData -> {
                    if (!data.isError) {
                        if (data.isSelected != checked) {
                            data.isSelected = checked
                            _globalEvent.value = CartGlobalEvent.AdapterItemChanged(index)
                        }
                    } else {
                        return@forEachIndexed
                    }
                }

                is DisabledItemHeaderHolderData, is CartSectionHeaderHolderData -> {
                    return@forEachIndexed
                }
            }
        }
    }

    fun setLastItemAlwaysSelected(): Boolean {
        var cartItemCount = 0
        cartDataList.value.forEach outer@{ any ->
            when (any) {
                is CartGroupHolderData -> {
                    any.productUiModelList.forEach { _ ->
                        cartItemCount++

                        if (cartItemCount > 1) {
                            return@outer
                        }
                    }
                }
            }
        }

        if (cartItemCount == 1) {
            var tmpIndex = 0
            cartDataList.value.forEachIndexed { index, any ->
                when (any) {
                    is CartGroupHolderData -> {
                        tmpIndex = index
                        any.isAllSelected = true
                        any.productUiModelList.forEach {
                            it.isSelected = true
                        }
                    }

                    is DisabledItemHeaderHolderData, is CartSectionHeaderHolderData -> {
                        return@forEachIndexed
                    }
                }
            }

            _globalEvent.value = CartGlobalEvent.AdapterItemChanged(tmpIndex)

            return true
        }

        return false
    }

    fun removeAccordionDisabledItem() {
        var item: DisabledAccordionHolderData? = null
        cartDataList.value.forEach {
            if (it is DisabledAccordionHolderData) {
                item = it
            }
        }

        item?.let {
            cartDataList.value.remove(it)
        }
    }

    fun updateCartWishlistData(cartWishlistHolderData: CartWishlistHolderData): Int {
        var wishlistIndex = 0
        for ((index, item) in cartDataList.value.withIndex()) {
            if (item is CartWishlistHolderData) {
                wishlistIndex = index
                break
            }
        }

        if (wishlistIndex != 0) {
            cartDataList.value[wishlistIndex] = cartWishlistHolderData
        }

        return wishlistIndex
    }

    fun addCartWishlistData(
        cartSectionHeaderHolderData: CartSectionHeaderHolderData,
        cartWishlistHolderData: CartWishlistHolderData
    ) {
        var wishlistIndex = 0
        for ((index, item) in cartDataList.value.withIndex()) {
            if (item is CartEmptyHolderData ||
                item is CartGroupHolderData ||
                item is CartItemHolderData ||
                item is CartShopBottomHolderData ||
                item is ShipmentSellerCashbackModel ||
                item is DisabledItemHeaderHolderData ||
                item is DisabledReasonHolderData ||
                item is DisabledAccordionHolderData
            ) {
                wishlistIndex = index
            }
        }
        cartDataList.value.add(++wishlistIndex, cartSectionHeaderHolderData)
        cartModel.firstCartSectionHeaderPosition = when (cartModel.firstCartSectionHeaderPosition) {
            -1 -> wishlistIndex
            else -> min(cartModel.firstCartSectionHeaderPosition, wishlistIndex)
        }
        cartDataList.value.add(++wishlistIndex, cartWishlistHolderData)
        cartDataList.notifyObserver()
    }

    private fun hasReachAllShopItems(data: Any): Boolean {
        return data is CartRecentViewHolderData ||
            data is CartWishlistHolderData ||
            data is CartTopAdsHeadlineData ||
            data is CartRecommendationItemHolderData
    }

    fun processToUpdateAndReloadCartData(
        cartId: String,
        getCartState: Int = GET_CART_STATE_DEFAULT
    ) {
        val cartItemDataList = ArrayList<CartItemHolderData>()
        val allAvailableCartItemData = CartDataHelper.getAllAvailableCartItemData(
            cartDataList.value
        )
        for (data in allAvailableCartItemData) {
            if (!data.isError) {
                cartItemDataList.add(data)
            }
        }

        val updateCartRequestList = getUpdateCartRequest(cartItemDataList)
        if (updateCartRequestList.isNotEmpty()) {
            viewModelScope.launchCatchError(
                context = dispatchers.io,
                block = {
                    val updateCartWrapperRequest = UpdateCartWrapperRequest(
                        updateCartRequestList = updateCartRequestList,
                        source = UpdateCartAndGetLastApplyUseCase.PARAM_VALUE_SOURCE_UPDATE_QTY_NOTES,
                        cartId = cartId,
                        getCartState = getCartState
                    )
                    val updateAndReloadCartListData = updateAndReloadCartUseCase(updateCartWrapperRequest)
                    withContext(dispatchers.main) {
                        _globalEvent.value = CartGlobalEvent.ProgressLoading(false)
                        processInitialGetCartData(
                            updateAndReloadCartListData.cartId,
                            initialLoad = false,
                            isLoadingTypeRefresh = true,
                            updateAndReloadCartListData.getCartState
                        )
                    }
                },
                onError = { throwable ->
                    withContext(dispatchers.main) {
                        _globalEvent.value = CartGlobalEvent.UpdateAndReloadCartFailed(throwable)
                    }
                }
            )
        } else {
            _globalEvent.value = CartGlobalEvent.ProgressLoading(false)
        }
    }

    private fun getUpdateCartRequest(
        cartItemHolderDataList: List<CartItemHolderData>,
        onlyTokoNowProducts: Boolean = false
    ): List<UpdateCartRequest> {
        val updateCartRequestList = ArrayList<UpdateCartRequest>()
        for (cartItemHolderData in cartItemHolderDataList) {
            if (!onlyTokoNowProducts || (onlyTokoNowProducts && cartItemHolderData.isTokoNow)) {
                val updateCartRequest = UpdateCartRequest().apply {
                    productId = cartItemHolderData.productId
                    cartId = cartItemHolderData.cartId
                    notes = cartItemHolderData.notes
                    quantity = if (cartItemHolderData.isBundlingItem) {
                        cartItemHolderData.quantity * cartItemHolderData.bundleQuantity
                    } else {
                        cartItemHolderData.quantity
                    }
                    bundleInfo = BundleInfo().apply {
                        bundleId = cartItemHolderData.bundleId.toZeroStringIfNullOrBlank()
                        bundleGroupId = cartItemHolderData.bundleGroupId.toZeroStringIfNullOrBlank()
                        bundleQty = cartItemHolderData.bundleQuantity
                    }
                }
                updateCartRequestList.add(updateCartRequest)
            }
        }
        return updateCartRequestList
    }

    fun processInitialGetCartData(
        cartId: String,
        initialLoad: Boolean,
        isLoadingTypeRefresh: Boolean,
        getCartState: Int = GET_CART_STATE_DEFAULT
    ) {
        CartIdlingResource.increment()
        if (initialLoad) {
            _globalEvent.value = CartGlobalEvent.LoadGetCartData
        } else if (!isLoadingTypeRefresh) {
            _globalEvent.value = CartGlobalEvent.ProgressLoading(true)
        }

        viewModelScope.launch {
            try {
                val cartData = getCartRevampV4UseCase(
                    GetCartParam(
                        cartId = cartId,
                        state = getCartState,
                        isCartReimagine = true
                    )
                )
                onSuccessGetCartList(cartData, initialLoad)
            } catch (t: Throwable) {
                onErrorGetCartList(t, initialLoad)
            }
        }
    }

    private fun onSuccessGetCartList(cartData: CartData, initialLoad: Boolean) {
        cartModel = cartModel.copy(
            isLastApplyResponseStillValid = true,
            lastValidateUseResponse = null,
            lastUpdateCartAndGetLastApplyResponse = null,
            cartListData = cartData,
            summaryTransactionUiModel = CartUiModelMapper.mapSummaryTransactionUiModel(cartData),
            summariesAddOnUiModel = CartUiModelMapper.getShoppingSummaryAddOns(cartData.shoppingSummary.summaryAddOnList),
            promoSummaryUiModel = CartUiModelMapper.mapPromoSummaryUiModel(cartData.promoSummary),
            showChoosePromoWidget = cartData.promo.showChoosePromoWidget,
            promoTicker = cartData.promo.ticker,
            recommendationPage = RECOMMENDATION_START_PAGE
        )
        if (!initialLoad) {
            _globalEvent.value = CartGlobalEvent.ProgressLoading(false)
        }

        _loadCartState.value = CartState.Success(cartData)
        CartIdlingResource.decrement()
    }

    private fun onErrorGetCartList(throwable: Throwable, initialLoad: Boolean) {
        Timber.e(throwable)
        if (!initialLoad) {
            _globalEvent.value = CartGlobalEvent.ProgressLoading(false)
        }
        _loadCartState.value = CartState.Failed(throwable)
        CartLogger.logOnErrorLoadCartPage(throwable)
        CartIdlingResource.decrement()
    }

    fun processUpdateCartCounter() {
        compositeSubscription.add(
            updateCartCounterUseCase.createObservable(RequestParams.create())
                .subscribeOn(schedulers.io)
                .unsubscribeOn(schedulers.io)
                .observeOn(schedulers.main)
                .subscribe {
                    _globalEvent.value = CartGlobalEvent.CartCounterUpdated(it)
                }
        )
    }

    fun getAllPromosApplied(lastApplyPromoData: LastApplyPromoData): List<String> {
        val listPromos = arrayListOf<String>()
        lastApplyPromoData.codes.forEach {
            listPromos.add(it)
        }
        lastApplyPromoData.listVoucherOrders.forEach {
            listPromos.add(it.code)
        }
        return listPromos
    }

    fun getAllPromosApplied(lastApplyData: LastApplyUiModel): List<String> {
        val listPromos = arrayListOf<String>()
        lastApplyData.codes.forEach {
            listPromos.add(it)
        }
        lastApplyData.voucherOrders.forEach {
            listPromos.add(it.code)
        }
        return listPromos
    }

    fun collapseDisabledItems() {
        var firstIndex = RecyclerView.NO_POSITION
        var lastIndex = 0
        for ((index, item) in cartDataList.value.withIndex()) {
            if (firstIndex == RecyclerView.NO_POSITION && item is DisabledItemHeaderHolderData) {
                firstIndex = index
            } else if (item is DisabledAccordionHolderData) {
                lastIndex = index
                break
            }
        }
        if (firstIndex > RecyclerView.NO_POSITION && lastIndex >= firstIndex && lastIndex <= cartDataList.value.size) {
            val startIdxDisabledReason = firstIndex + 1
            val tmpAllUnavailableShop =
                cartDataList.value.subList(startIdxDisabledReason, lastIndex).toMutableList()
            cartModel.tmpAllUnavailableShop = tmpAllUnavailableShop

            val tmpCollapsedUnavailableProducts = mutableListOf<CartItemHolderData>()
            tmpAllUnavailableShop.forEach {
                if (it is CartGroupHolderData) {
                    tmpCollapsedUnavailableProducts += it.productUiModelList
                }
            }
            val disabledCollapsedData = DisabledCollapsedHolderData(
                productUiModelList = tmpCollapsedUnavailableProducts
            )
            cartModel.tmpCollapsedUnavailableShop = disabledCollapsedData

            cartDataList.value.removeAll(tmpAllUnavailableShop.toSet())
            cartDataList.value.add(startIdxDisabledReason, disabledCollapsedData)
            cartDataList.notifyObserver()
        }
    }

    fun expandDisabledItems() {
        cartModel.tmpCollapsedUnavailableShop?.let { collapsedUnavailableItems ->
            val firstIndex = cartDataList.value.indexOf(collapsedUnavailableItems)
            if (firstIndex != -1) {
                cartDataList.value.remove(collapsedUnavailableItems)
                cartModel.tmpCollapsedUnavailableShop = null

                cartModel.tmpAllUnavailableShop?.let { unavailableItems ->
                    cartDataList.value.addAll(firstIndex, unavailableItems)
                    unavailableItems.clear()
                    cartDataList.notifyObserver()
                }
            }
        }
    }

    fun generateCheckoutDataAnalytics(
        cartItemDataList: List<CartItemHolderData>,
        step: String
    ): Map<String, Any> {
        val checkoutMapData = HashMap<String, Any>()
        val enhancedECommerceActionField = EnhancedECommerceActionField().apply {
            setStep(step)
            if (step == EnhancedECommerceActionField.STEP_0) {
                setOption(EnhancedECommerceActionField.STEP_0_OPTION_VIEW_CART_PAGE)
            } else if (step == EnhancedECommerceActionField.STEP_1) {
                setOption(EnhancedECommerceActionField.STEP_1_OPTION_CART_PAGE_LOADED)
            }
        }
        val enhancedECommerceCheckout = EnhancedECommerceCheckout().apply {
            for (cartItemData in cartItemDataList) {
                val enhancedECommerceProductCartMapData =
                    getCheckoutEnhancedECommerceProductCartMapData(cartItemData)
                addProduct(enhancedECommerceProductCartMapData.getProduct())
            }
            setCurrencyCode(EnhancedECommerceCartMapData.VALUE_CURRENCY_IDR)
            setActionField(enhancedECommerceActionField.getActionFieldMap())
        }
        checkoutMapData[EnhancedECommerceCheckout.KEY_CHECKOUT] =
            enhancedECommerceCheckout.getCheckoutMap()
        return checkoutMapData
    }

    private fun getCheckoutEnhancedECommerceProductCartMapData(cartItemHolderData: CartItemHolderData): EnhancedECommerceProductCartMapData {
        val enhancedECommerceProductCartMapData = EnhancedECommerceProductCartMapData().apply {
            setDimension38(
                cartItemHolderData.trackerAttribution.ifBlank {
                    EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                }
            )
            setDimension45(cartItemHolderData.cartId)
            setDimension54(cartItemHolderData.isFulfillment)
            setDimension53(cartItemHolderData.productOriginalPrice > 0)
            setProductName(cartItemHolderData.productName)
            setProductID(cartItemHolderData.productId)
            setPrice(cartItemHolderData.productPrice.toString())
            setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setCategory(
                cartItemHolderData.category.ifBlank {
                    EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                }
            )
            setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setQty(cartItemHolderData.quantity)
            setShopId(cartItemHolderData.shopHolderData.shopId)
            setShopType(cartItemHolderData.shopHolderData.shopTypeInfo.titleFmt)
            setShopName(cartItemHolderData.shopHolderData.shopName)
            setCategoryId(cartItemHolderData.categoryId)
            setWarehouseId(cartItemHolderData.warehouseId)
            setProductWeight(cartItemHolderData.productWeight.toString())
            setCartId(cartItemHolderData.cartId)
            setPromoCode(cartItemHolderData.promoCodes)
            setPromoDetails(cartItemHolderData.promoDetails)
            setDimension83(cartItemHolderData.freeShippingName)
            setDimension117(cartItemHolderData.bundleType)
            setDimension118(cartItemHolderData.bundleId)
            setCampaignId(cartItemHolderData.campaignId)
            if (cartItemHolderData.shopCartShopGroupTickerData.tickerText.isNotBlank()) {
                val fulfillText =
                    if (cartItemHolderData.shopCartShopGroupTickerData.state == CartShopGroupTickerState.SUCCESS_AFFORD) {
                        ConstantTransactionAnalytics.EventLabel.BO_FULFILL
                    } else {
                        ConstantTransactionAnalytics.EventLabel.BO_UNFULFILL
                    }
                setBoAffordability("${fulfillText}_${cartItemHolderData.shopBoMetadata.boType}")
            } else {
                setBoAffordability("")
            }
        }
        return enhancedECommerceProductCartMapData
    }

    fun getPromoFlag(): Boolean {
        val cartListData = cartModel.cartListData
        val lastValidateUseResponse = cartModel.lastValidateUseResponse
        return if (cartModel.isLastApplyResponseStillValid && cartListData != null) {
            cartListData.promo.lastApplyPromo.lastApplyPromoData.additionalInfo.pomlAutoApplied
        } else if (!cartModel.isLastApplyResponseStillValid && lastValidateUseResponse != null) {
            lastValidateUseResponse.promoUiModel.additionalInfoUiModel.pomlAutoApplied
        } else {
            false
        }
    }

    fun reCalculateSubTotal() {
        val dataList = CartDataHelper.getAllAvailableShopGroupDataList(cartDataList.value)
        var totalItemQty = 0
        var subtotalBeforeSlashedPrice = 0.0
        var subtotalPrice = 0.0
        var subtotalCashback = 0.0

        // Collect all Cart Item & also calculate total weight on each shop
        val cartItemDataList = getAvailableCartItemDataList(dataList)

        // Calculate total total item, price and cashback for marketplace product
        val returnValueMarketplaceProduct = cartCalculator.calculatePriceMarketplaceProduct(
            allCartItemDataList = cartItemDataList,
            cartModel = cartModel,
            updateCartModel = { newCartModel ->
                cartModel = newCartModel
            }
        )
        totalItemQty += returnValueMarketplaceProduct.first
        subtotalBeforeSlashedPrice += returnValueMarketplaceProduct.second.first
        subtotalPrice += returnValueMarketplaceProduct.second.second
        subtotalCashback += returnValueMarketplaceProduct.third

        updateSummaryTransactionUiModel(
            subtotalBeforeSlashedPrice,
            subtotalPrice,
            totalItemQty,
            subtotalCashback
        )

        _globalEvent.value = CartGlobalEvent.SubTotalUpdated(
            subtotalCashback,
            totalItemQty.toString(),
            subtotalPrice,
            dataList.isEmpty()
        )
    }

    private fun updateSummaryTransactionUiModel(
        subtotalBeforeSlashedPrice: Double,
        subtotalPrice: Double,
        totalItemQty: Int,
        subtotalCashback: Double
    ) {
        // update summary addons
        var totalAddonPrice = 0.0
        for ((key, value) in cartModel.summariesAddOnUiModel) {
            cartModel.summaryTransactionUiModel?.listSummaryAddOns?.forEach {
                if (it.type == key) {
                    it.qty = cartModel.totalQtyWithAddon
                    it.wording = value.replace(
                        CartConstant.QTY_ADDON_REPLACE,
                        cartModel.totalQtyWithAddon.toString()
                    )

                    totalAddonPrice = cartModel.totalQtyWithAddon * it.priceValue
                    it.priceLabel =
                        CurrencyFormatUtil.convertPriceValueToIdrFormat(totalAddonPrice, false)
                            .removeDecimalSuffix()
                }
            }
        }

        val priceAfterAddon = subtotalPrice + totalAddonPrice
        val priceAfterAddonBeforeSlashedPrice = subtotalBeforeSlashedPrice + totalAddonPrice

        val summaryTransactionUiModel = cartModel.summaryTransactionUiModel

        summaryTransactionUiModel?.qty = totalItemQty.toString()
        if (priceAfterAddonBeforeSlashedPrice == 0.0) {
            summaryTransactionUiModel?.totalValue = subtotalPrice.toLong()
        } else {
            summaryTransactionUiModel?.totalValue = subtotalBeforeSlashedPrice.toLong()
        }
        summaryTransactionUiModel?.discountValue =
            (priceAfterAddonBeforeSlashedPrice - priceAfterAddon).toLong()
        summaryTransactionUiModel?.paymentTotal = priceAfterAddon.toLong()
        summaryTransactionUiModel?.sellerCashbackValue = subtotalCashback.toLong()

        cartModel = cartModel.copy(summaryTransactionUiModel = summaryTransactionUiModel)
    }

    private fun getAvailableCartItemDataList(dataList: List<CartGroupHolderData>): ArrayList<CartItemHolderData> {
        // Collect all Cart Item, if has no error and selected
        // Also calculate total weight on each shop
        val allCartItemDataList = ArrayList<CartItemHolderData>()
        for (cartShopHolderData in dataList) {
            val (shopProductList, shopTotalWeight) = getAvailableCartItemDataListAndShopTotalWeight(
                cartShopHolderData
            )
            allCartItemDataList.addAll(shopProductList)
            cartShopHolderData.totalWeight = shopTotalWeight
        }

        return allCartItemDataList
    }

    private fun getAvailableCartItemDataListAndShopTotalWeight(cartGroupHolderData: CartGroupHolderData): Pair<ArrayList<CartItemHolderData>, Double> {
        val allCartItemDataList = ArrayList<CartItemHolderData>()
        var shopWeight = 0.0
        if (!cartGroupHolderData.isError && cartGroupHolderData.hasSelectedProduct) {
            cartGroupHolderData.productUiModelList.forEach { cartItemHolderData ->
                if (!cartItemHolderData.isError && cartItemHolderData.isSelected) {
                    allCartItemDataList.add(cartItemHolderData)
                    val quantity =
                        if (cartItemHolderData.isBundlingItem) {
                            cartItemHolderData.quantity * cartItemHolderData.bundleQuantity
                        } else {
                            cartItemHolderData.quantity
                        }

                    val weight = cartItemHolderData.productWeight
                    shopWeight += quantity * weight
                }
            }
        }
        return allCartItemDataList to shopWeight
    }

    fun doClearRedPromosBeforeGoToCheckout(clearPromoRequest: ClearPromoRequest) {
        _globalEvent.value = CartGlobalEvent.ItemLoading(false)
        viewModelScope.launchCatchError(
            context = dispatchers.io,
            block = {
                clearCacheAutoApplyStackUseCase.setParams(clearPromoRequest).executeOnBackground()
                withContext(dispatchers.main) {
                    _globalEvent.value = CartGlobalEvent.SuccessClearRedPromosThenGoToCheckout
                }
            },
            onError = { throwable ->
                Timber.d(throwable)
                withContext(dispatchers.main) {
                    _globalEvent.value = CartGlobalEvent.SuccessClearRedPromosThenGoToCheckout
                }
            }
        )
    }

    fun doClearRedPromosBeforeGoToPromo(clearPromoRequest: ClearPromoRequest) {
        _globalEvent.value = CartGlobalEvent.ItemLoading(true)
        viewModelScope.launchCatchError(
            context = dispatchers.io,
            block = {
                clearCacheAutoApplyStackUseCase.setParams(clearPromoRequest).executeOnBackground()
                withContext(dispatchers.main) {
                    _globalEvent.value = CartGlobalEvent.SuccessClearRedPromosThenGoToPromo
                }
            },
            onError = { throwable ->
                Timber.d(throwable)
                withContext(dispatchers.main) {
                    _globalEvent.value = CartGlobalEvent.SuccessClearRedPromosThenGoToPromo
                }
            }
        )
    }

    fun processUpdateCartData(fireAndForget: Boolean, onlyTokoNowProducts: Boolean = false) {
        if (!fireAndForget) {
            _globalEvent.value = CartGlobalEvent.ProgressLoading(true)
            CartIdlingResource.increment()
        }

        val cartItemDataList: List<CartItemHolderData> = if (fireAndForget) {
            // Update cart to save state
            CartDataHelper.getAllAvailableCartItemData(cartDataList.value)
        } else {
            // Update cart to go to shipment
            CartDataHelper.getSelectedCartItemData(cartDataList.value)
        }

        val updateCartRequestList = getUpdateCartRequest(cartItemDataList, onlyTokoNowProducts)
        if (updateCartRequestList.isNotEmpty()) {
            if (fireAndForget) {
                // Trigger use case without composite subscription, because this should continue even after view destroyed
                updateCartUseCase.setParams(
                    updateCartRequestList,
                    UpdateCartUseCase.VALUE_SOURCE_UPDATE_QTY_NOTES
                )
                updateCartUseCase.execute(onSuccess = {}, onError = {})
                return
            } else {
                updateCartUseCase.setParams(updateCartRequestList)
                updateCartUseCase.execute(
                    onSuccess = { updateCartV2Data ->
                        onSuccessUpdateCartForCheckout(
                            updateCartV2Data,
                            cartItemDataList
                        )
                    },
                    onError = { throwable ->
                        onErrorUpdateCartForCheckout(throwable, cartItemDataList)
                    }
                )
            }
        } else {
            if (!fireAndForget) {
                _globalEvent.value = CartGlobalEvent.ProgressLoading(false)
                CartLogger.logOnErrorUpdateCartForCheckout(
                    MessageErrorException("update cart empty product"),
                    cartItemDataList
                )
            }
        }
    }

    private fun onSuccessUpdateCartForCheckout(
        updateCartV2Data: UpdateCartV2Data,
        cartItemDataList: List<CartItemHolderData>
    ) {
        _globalEvent.value = CartGlobalEvent.ProgressLoading(false)
        if (updateCartV2Data.data.status) {
            val checklistCondition = getChecklistCondition()
            _updateCartForCheckoutState.value = UpdateCartCheckoutState.Success(
                generateCheckoutDataAnalytics(
                    cartItemDataList,
                    EnhancedECommerceActionField.STEP_1
                ),
                isCheckoutProductEligibleForCashOnDelivery(cartItemDataList),
                checklistCondition
            )
        } else {
            if (updateCartV2Data.data.outOfService.isOutOfService()) {
                _updateCartForCheckoutState.value = UpdateCartCheckoutState.ErrorOutOfService(
                    updateCartV2Data.data.outOfService
                )
            } else {
                _updateCartForCheckoutState.value = UpdateCartCheckoutState.UnknownError(
                    updateCartV2Data.data.error,
                    if (updateCartV2Data.data.toasterAction.showCta) updateCartV2Data.data.toasterAction.text else ""
                )
            }
            CartLogger.logOnErrorUpdateCartForCheckout(
                MessageErrorException(updateCartV2Data.data.error),
                cartItemDataList
            )
        }
        CartIdlingResource.decrement()
    }

    private fun getChecklistCondition(): Int {
        var checklistCondition = ITEM_CHECKED_ALL_WITHOUT_CHANGES
        val cartShopHolderDataList = CartDataHelper.getAllShopGroupDataList(cartDataList.value)

        if (cartShopHolderDataList.size == 1) {
            cartShopHolderDataList[0].productUiModelList.let {
                for (cartItemHolderData in it) {
                    if (!cartItemHolderData.isSelected) {
                        checklistCondition = ITEM_CHECKED_PARTIAL_ITEM
                        break
                    }
                }
            }
        } else if ((cartShopHolderDataList.size) > 1) {
            var allSelectedItemShopCount = 0
            var selectPartialShopAndItem = false
            cartShopHolderDataList.let {
                for (cartShopHolderData in it) {
                    if (cartShopHolderData.isAllSelected) {
                        allSelectedItemShopCount++
                    } else {
                        var selectedItem = 0
                        cartShopHolderData.productUiModelList.let { cartItemHolderDataList ->
                            for (cartItemHolderData in cartItemHolderDataList) {
                                if (!cartItemHolderData.isSelected) {
                                    selectedItem++
                                }
                            }
                            if (!selectPartialShopAndItem && selectedItem != cartItemHolderDataList.size) {
                                selectPartialShopAndItem = true
                            }
                        }
                    }
                }
                if (selectPartialShopAndItem) {
                    checklistCondition = ITEM_CHECKED_PARTIAL_SHOP_AND_ITEM
                } else if (allSelectedItemShopCount < it.size) {
                    checklistCondition = ITEM_CHECKED_PARTIAL_SHOP
                }
            }
        }

        if (checklistCondition == ITEM_CHECKED_ALL_WITHOUT_CHANGES && cartModel.hasPerformChecklistChange) {
            checklistCondition = ITEM_CHECKED_ALL_WITH_CHANGES
        }
        return checklistCondition
    }

    private fun isCheckoutProductEligibleForCashOnDelivery(cartItemHolderDataList: List<CartItemHolderData>): Boolean {
        var totalAmount = 0.0
        for (cartItemHolderData in cartItemHolderDataList) {
            val itemPriceAmount = cartItemHolderData.productPrice * cartItemHolderData.quantity
            totalAmount += itemPriceAmount
            if (!cartItemHolderData.isCod) return false
        }
        return totalAmount <= MAX_TOTAL_AMOUNT_ELIGIBLE_FOR_COD
    }

    private fun onErrorUpdateCartForCheckout(
        throwable: Throwable,
        cartItemDataList: List<CartItemHolderData>
    ) {
        _updateCartForCheckoutState.value = UpdateCartCheckoutState.Failed(throwable)
        CartLogger.logOnErrorUpdateCartForCheckout(throwable, cartItemDataList)
    }

    fun doUpdateCartForPromo() {
        _globalEvent.value = CartGlobalEvent.ProgressLoading(true)

        val updateCartRequestList =
            getUpdateCartRequest(CartDataHelper.getSelectedCartItemData(cartDataList.value))
        if (updateCartRequestList.isNotEmpty()) {
            updateCartUseCase.setParams(
                updateCartRequestList,
                UpdateCartUseCase.VALUE_SOURCE_UPDATE_QTY_NOTES
            )
            updateCartUseCase.execute(
                onSuccess = {
                    onSuccessUpdateCartForPromo()
                },
                onError = {
                    onErrorUpdateCartForPromo(it)
                }
            )
        } else {
            _globalEvent.value = CartGlobalEvent.ProgressLoading(false)
        }
    }

    private fun onErrorUpdateCartForPromo(throwable: Throwable) {
        _updateCartForPromoState.value = UpdateCartPromoState.Failed(throwable)
    }

    private fun onSuccessUpdateCartForPromo() {
        _updateCartForPromoState.value = UpdateCartPromoState.Success
    }

    fun updatePromoSummaryData(lastApplyUiModel: LastApplyUiModel) {
        val promoSummaryUiModel = cartModel.promoSummaryUiModel
        val summaryTransactionUiModel = cartModel.summaryTransactionUiModel
        promoSummaryUiModel?.details?.clear()
        promoSummaryUiModel?.details?.addAll(
            lastApplyUiModel.additionalInfo.usageSummaries.map {
                PromoSummaryDetailData(
                    description = it.description,
                    type = it.type,
                    amountStr = it.amountStr,
                    amount = it.amount.toDouble(),
                    currencyDetailStr = it.currencyDetailsStr
                )
            }.toList()
        )
        summaryTransactionUiModel?.promoValue =
            lastApplyUiModel.benefitSummaryInfo.finalBenefitAmount.toLong()
    }

    fun checkForShipmentForm() {
//        var hasCheckedAvailableItem = false
//        loop@ for (any in cartDataList.value) {
//            if (hasCheckedAvailableItem) break@loop
//            if (any is CartGroupHolderData) {
//                if (any.isAllSelected) {
//                    hasCheckedAvailableItem = true
//                } else if (any.isPartialSelected) {
//                    any.productUiModelList.let {
//                        innerLoop@ for (cartItemHolderData in it) {
//                            if (cartItemHolderData.isSelected) {
//                                hasCheckedAvailableItem = true
//                                break@innerLoop
//                            }
//                        }
//                    }
//                }
//            }
//        }

        if (_selectedAmountState.value > 0) {
            _cartCheckoutButtonState.value = CartCheckoutButtonState.ENABLE
        } else {
            _cartCheckoutButtonState.value = CartCheckoutButtonState.DISABLE
        }
    }

    fun processGetRecentViewData() {
        _globalEvent.value = CartGlobalEvent.ItemLoading(true)
        viewModelScope.launchCatchError(
            context = dispatchers.io,
            block = {
                val recommendationWidgets = getRecentViewUseCase.getData(
                    GetRecommendationRequestParam(
                        pageNumber = 1,
                        xSource = RECENT_VIEW_XSOURCE,
                        pageName = PAGE_NAME_RECENT_VIEW,
                        productIds = CartDataHelper.getAllCartItemProductId(cartDataList.value),
                        queryParam = ""
                    )
                )
                withContext(dispatchers.main) {
                    _recentViewState.value = LoadRecentReviewState.Success(recommendationWidgets)
                }
            },
            onError = { throwable ->
                Timber.d(throwable)
                withContext(dispatchers.main) {
                    _recentViewState.value = LoadRecentReviewState.Failed(throwable)
                }
            }
        )
    }

    fun addCartRecentViewData(
        cartSectionHeaderHolderData: CartSectionHeaderHolderData,
        cartRecentViewHolderData: CartRecentViewHolderData
    ) {
        var recentViewIndex = 0
        for ((index, item) in cartDataList.value.withIndex()) {
            if (item is CartEmptyHolderData ||
                item is CartGroupHolderData ||
                item is CartItemHolderData ||
                item is CartShopBottomHolderData ||
                item is ShipmentSellerCashbackModel ||
                item is CartWishlistHolderData ||
                item is DisabledItemHeaderHolderData ||
                item is DisabledReasonHolderData ||
                item is DisabledAccordionHolderData
            ) {
                recentViewIndex = index
            }
        }
        cartDataList.value.add(++recentViewIndex, cartSectionHeaderHolderData)
        cartModel.firstCartSectionHeaderPosition = when (cartModel.firstCartSectionHeaderPosition) {
            -1 -> recentViewIndex
            else -> min(cartModel.firstCartSectionHeaderPosition, recentViewIndex)
        }
        cartDataList.value.add(++recentViewIndex, cartRecentViewHolderData)
        cartDataList.notifyObserver()
    }

    fun processGetWishlistV2Data() {
        val requestParams = WishlistV2Params().apply {
            source = SOURCE_CART
            cartModel.lca?.let { address ->
                wishlistChosenAddress = WishlistV2Params.WishlistChosenAddress(
                    districtId = address.district_id,
                    cityId = address.city_id,
                    latitude = address.lat,
                    longitude = address.long,
                    postalCode = address.postal_code,
                    addressId = address.address_id
                )
            }
        }

        viewModelScope.launch(dispatchers.io) {
            getWishlistV2UseCase.setParams(requestParams)
            val result = getWishlistV2UseCase.executeOnBackground()
            withContext(dispatchers.main) {
                if (result is Success) {
                    _wishlistV2State.value = LoadWishlistV2State.Success(
                        result.data.items,
                        true
                    )
                } else {
                    val error = (result as Fail).throwable
                    Timber.d(error)
                    _wishlistV2State.value = LoadWishlistV2State.Failed
                }
            }
        }
    }

    fun setLocalizingAddressData(lca: LocalCacheModel?) {
        cartModel = cartModel.copy(
            lca = lca
        )
    }

    fun saveCheckboxState() {
        val cartItemDataList = CartDataHelper.getAllAvailableCartItemHolderData(cartDataList.value)
        viewModelScope.launchCatchError(dispatchers.io, block = {
            setCartlistCheckboxStateUseCase(cartItemDataList)
        }, onError = {})
    }

    fun processGetRecommendationData() {
        _globalEvent.value = CartGlobalEvent.ItemLoading(true)
        viewModelScope.launchCatchError(
            context = dispatchers.io,
            block = {
                val recommendationWidgets = getRecommendationUseCase.getData(
                    GetRecommendationRequestParam(
                        pageNumber = cartModel.recommendationPage,
                        xSource = "recom_widget",
                        pageName = "cart",
                        productIds = CartDataHelper.getAllCartItemProductId(cartDataList.value),
                        queryParam = ""
                    )
                )
                withContext(dispatchers.main) {
                    _recommendationState.value =
                        LoadRecommendationState.Success(recommendationWidgets)
                }
            },
            onError = { throwable ->
                Timber.d(throwable)
                withContext(dispatchers.main) {
                    _recommendationState.value = LoadRecommendationState.Failed
                }
            }
        )
    }

    fun addCartRecommendationData(
        cartSectionHeaderHolderData: CartSectionHeaderHolderData?,
        cartRecommendationItemHolderDataList: List<CartRecommendationItemHolderData>,
        recommendationPage: Int
    ) {
        var recommendationIndex = 0
        for ((index, item) in cartDataList.value.withIndex()) {
            if (item is CartEmptyHolderData ||
                item is CartGroupHolderData ||
                item is CartItemHolderData ||
                item is CartShopBottomHolderData ||
                item is ShipmentSellerCashbackModel ||
                item is DisabledItemHeaderHolderData ||
                item is DisabledReasonHolderData ||
                item is DisabledAccordionHolderData ||
                item is CartRecentViewHolderData ||
                item is CartWishlistHolderData ||
                item is CartTopAdsHeadlineData ||
                item is CartRecommendationItemHolderData
            ) {
                recommendationIndex = index
            }
        }
        cartSectionHeaderHolderData?.let {
            cartDataList.value.add(++recommendationIndex, cartSectionHeaderHolderData)
            cartModel.firstCartSectionHeaderPosition =
                when (cartModel.firstCartSectionHeaderPosition) {
                    -1 -> recommendationIndex
                    else -> min(cartModel.firstCartSectionHeaderPosition, recommendationIndex)
                }
        }

        if (recommendationPage == 1) {
            addCartTopAdsHeadlineData(++recommendationIndex)
        }
        cartDataList.value.addAll(++recommendationIndex, cartRecommendationItemHolderDataList)
        cartDataList.notifyObserver()
    }

    private fun addCartTopAdsHeadlineData(index: Int) {
        val cartProductIds = mutableListOf<String>()
        loop@ for (item in cartDataList.value) {
            when (item) {
                is CartGroupHolderData -> {
                    item.productUiModelList.forEach { cartItem ->
                        cartProductIds.add(cartItem.productId)
                    }
                }

                is CartRecentViewHolderData, is CartWishlistHolderData, is CartRecommendationItemHolderData -> {
                    break@loop
                }
            }
        }
        val cartTopAdsHeadlineData = CartTopAdsHeadlineData(cartProductIds = cartProductIds)
        cartModel = cartModel.copy(cartTopAdsHeadlineData = cartTopAdsHeadlineData)
        cartDataList.value.add(index, cartTopAdsHeadlineData)
    }

    fun addCartTopAdsHeadlineData(
        cartSectionHeaderHolderData: CartSectionHeaderHolderData?,
        recommendationPage: Int
    ) {
        if (recommendationPage == 1) {
            var recommendationIndex = 0
            for ((index, item) in cartDataList.value.withIndex()) {
                if (item is CartEmptyHolderData ||
                    item is CartGroupHolderData ||
                    item is CartItemHolderData ||
                    item is CartShopBottomHolderData ||
                    item is ShipmentSellerCashbackModel ||
                    item is DisabledItemHeaderHolderData ||
                    item is DisabledReasonHolderData ||
                    item is DisabledAccordionHolderData ||
                    item is CartRecentViewHolderData ||
                    item is CartWishlistHolderData
                ) {
                    recommendationIndex = index
                }
            }
            cartSectionHeaderHolderData?.let {
                cartDataList.value.add(++recommendationIndex, cartSectionHeaderHolderData)
                cartModel.firstCartSectionHeaderPosition =
                    when (cartModel.firstCartSectionHeaderPosition) {
                        -1 -> recommendationIndex
                        else -> min(cartModel.firstCartSectionHeaderPosition, recommendationIndex)
                    }
            }

            addCartTopAdsHeadlineData(++recommendationIndex)
            cartDataList.notifyObserver()
        }
    }

    fun resetData() {
        cartModel = cartModel.copy(firstCartSectionHeaderPosition = -1)
        cartDataList.value = arrayListOf()
        cartDataList.notifyObserver()
        checkForShipmentForm()
    }

    fun addCartLoadingData() {
        if (cartModel.cartLoadingHolderData == null) {
            cartModel.cartLoadingHolderData = CartLoadingHolderData()
        }
        cartModel.cartLoadingHolderData?.let {
            cartDataList.value.add(it)
            cartDataList.notifyObserver()
        }
    }

    fun removeCartLoadingData() {
        cartModel.cartLoadingHolderData?.let {
            val index = cartDataList.value.indexOf(it)
            if (index != -1) {
                cartDataList.value.remove(it)
                cartDataList.notifyObserver()
            }
        }
    }

    fun addItem(any: Any) {
        cartDataList.value.add(any)
    }

    fun addItems(anyList: List<Any>) {
        cartDataList.value.addAll(anyList)
    }

    fun addItems(index: Int = -1, anyList: List<Any>) {
        cartDataList.value.addAll(index, anyList)
    }

    fun generateWishlistDataImpressionAnalytics(
        cartWishlistItemHolderDataList: List<CartWishlistItemHolderData>,
        isEmptyCart: Boolean
    ): Map<String, Any> {
        val enhancedECommerceCartMapData = EnhancedECommerceCartMapData().apply {
            for ((position, cartWishlistItemHolderData) in cartWishlistItemHolderDataList.withIndex()) {
                val enhancedECommerceProductCartMapData = getProductWishlistImpressionMapData(
                    cartWishlistItemHolderData,
                    isEmptyCart,
                    position
                )
                addImpression(enhancedECommerceProductCartMapData.getProduct())
            }

            setCurrencyCode(EnhancedECommerceCartMapData.VALUE_CURRENCY_IDR)
        }
        return enhancedECommerceCartMapData.cartMap
    }

    private fun getProductWishlistImpressionMapData(
        wishlistItemHolderData: CartWishlistItemHolderData,
        isEmptyCart: Boolean,
        position: Int
    ): EnhancedECommerceProductCartMapData {
        return EnhancedECommerceProductCartMapData().apply {
            setProductID(wishlistItemHolderData.id)
            setProductName(wishlistItemHolderData.name)
            setPrice(wishlistItemHolderData.price.replace(REGEX_NUMBER, ""))
            setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setCategory(wishlistItemHolderData.category)
            setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)

            if (isEmptyCart) {
                setListName(EnhancedECommerceActionFieldData.VALUE_SECTION_NAME_WISHLIST_EMPTY_CART)
            } else {
                setListName(EnhancedECommerceActionFieldData.VALUE_SECTION_NAME_WISHLIST)
            }

            setPosition(position.toString())
        }
    }

    fun generateRecommendationImpressionDataAnalytics(
        position: Int,
        cartRecommendationItemHolderDataList: List<CartRecommendationItemHolderData>,
        isEmptyCart: Boolean
    ): Map<String, Any> {
        val enhancedECommerceCartMapData = EnhancedECommerceCartMapData().apply {
            var tmpPosition = position
            if (cartRecommendationItemHolderDataList.size == 1) tmpPosition += 1
            for (cartRecommendationItemHolderData in cartRecommendationItemHolderDataList) {
                val enhancedECommerceProductCartMapData =
                    getEnhancedECommerceProductRecommendationMapData(
                        cartRecommendationItemHolderData.recommendationItem,
                        isEmptyCart,
                        tmpPosition
                    )
                addImpression(enhancedECommerceProductCartMapData.getProduct())
                tmpPosition++
            }

            setCurrencyCode(EnhancedECommerceCartMapData.VALUE_CURRENCY_IDR)
        }
        return enhancedECommerceCartMapData.cartMap
    }

    private fun getEnhancedECommerceProductRecommendationMapData(
        recommendationItem: RecommendationItem,
        isEmptyCart: Boolean,
        position: Int
    ): EnhancedECommerceProductCartMapData {
        val enhancedECommerceProductCartMapData = EnhancedECommerceProductCartMapData().apply {
            setProductID(recommendationItem.productId.toString())
            setProductName(recommendationItem.name)
            setPrice(recommendationItem.price.replace(REGEX_NUMBER, ""))
            setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setCategory(
                recommendationItem.categoryBreadcrumbs.ifBlank {
                    EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                }
            )
            setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setListName(getActionFieldListStr(isEmptyCart, recommendationItem))
            setPosition(position.toString())
            if (recommendationItem.isFreeOngkirActive && recommendationItem.labelGroupList.hasLabelGroupFulfillment()) {
                setDimension83(EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR_EXTRA)
            } else if (recommendationItem.isFreeOngkirActive && !recommendationItem.labelGroupList.hasLabelGroupFulfillment()) {
                setDimension83(EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR)
            } else {
                setDimension83(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            }
        }
        return enhancedECommerceProductCartMapData
    }

    private fun getActionFieldListStr(
        isCartEmpty: Boolean,
        recommendationItem: RecommendationItem
    ): String {
        var listName: String = if (isCartEmpty) {
            EnhancedECommerceActionField.LIST_CART_RECOMMENDATION_ON_EMPTY_CART
        } else {
            EnhancedECommerceActionField.LIST_CART_RECOMMENDATION
        }
        listName += recommendationItem.recommendationType
        if (recommendationItem.isTopAds) {
            listName += EnhancedECommerceActionField.LIST_CART_RECOMMENDATION_TOPADS_TYPE
        }
        return listName
    }

    fun generateRecentViewDataImpressionAnalytics(
        cartRecentViewItemHolderDataList: List<CartRecentViewItemHolderData>,
        isEmptyCart: Boolean
    ): Map<String, Any> {
        val enhancedECommerceCartMapData = EnhancedECommerceCartMapData().apply {
            for ((position, cartRecentViewItemHolderData) in cartRecentViewItemHolderDataList.withIndex()) {
                val enhancedECommerceProductCartMapData = getProductRecentViewImpressionMapData(
                    cartRecentViewItemHolderData,
                    isEmptyCart,
                    position
                )
                addImpression(enhancedECommerceProductCartMapData.getProduct())
            }

            setCurrencyCode(EnhancedECommerceCartMapData.VALUE_CURRENCY_IDR)
        }
        return enhancedECommerceCartMapData.cartMap
    }

    private fun getProductRecentViewImpressionMapData(
        recentViewItemHolderData: CartRecentViewItemHolderData,
        isEmptyCart: Boolean,
        position: Int
    ): EnhancedECommerceProductCartMapData {
        return EnhancedECommerceProductCartMapData().apply {
            setProductID(recentViewItemHolderData.id)
            setProductName(recentViewItemHolderData.name)
            setPrice(recentViewItemHolderData.price.replace(REGEX_NUMBER, ""))
            setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setCategory(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)

            if (isEmptyCart) {
                setListName(EnhancedECommerceActionFieldData.VALUE_SECTION_NAME_RECENT_VIEW_EMPTY_CART)
            } else {
                setListName(EnhancedECommerceActionFieldData.VALUE_SECTION_NAME_RECENT_VIEW)
            }

            setPosition(position.toString())
        }
    }

    fun setShopSelected(position: Int, selected: Boolean) {
        val any = cartDataList.value[position]
        if (any is CartGroupHolderData) {
            any.isAllSelected = selected
            any.productUiModelList.let {
                for (cartItemHolderData in it) {
                    cartItemHolderData.isSelected = selected
                }
            }
            updateSelectedAmount()
        }
    }

    fun generateParamGetLastApplyPromo(): ValidateUsePromoRequest {
        return when {
            cartModel.isLastApplyResponseStillValid -> {
                val lastApplyPromo =
                    cartModel.cartListData?.promo?.lastApplyPromo ?: LastApplyPromo()
                PromoRequestMapper.generateGetLastApplyRequestParams(
                    lastApplyPromo,
                    CartDataHelper.getSelectedCartGroupHolderData(cartDataList.value),
                    null
                )
            }

            cartModel.lastValidateUseResponse != null -> {
                val promoUiModel = cartModel.lastValidateUseResponse?.promoUiModel ?: PromoUiModel()
                PromoRequestMapper.generateGetLastApplyRequestParams(
                    promoUiModel,
                    CartDataHelper.getSelectedCartGroupHolderData(cartDataList.value),
                    cartModel.lastValidateUseRequest
                )
            }

            else -> {
                PromoRequestMapper.generateGetLastApplyRequestParams(
                    null,
                    CartDataHelper.getSelectedCartGroupHolderData(cartDataList.value),
                    null
                )
            }
        }
    }

    fun doUpdateCartAndGetLastApply(promoRequest: ValidateUsePromoRequest) {
        val cartItemDataList = ArrayList<CartItemHolderData>()
        CartDataHelper.getSelectedCartItemData(cartDataList.value).let { listCartItemData ->
            for (data in listCartItemData) {
                if (!data.isError) {
                    cartItemDataList.add(data)
                }
            }
        }

        val updateCartRequestList = getUpdateCartRequest(cartItemDataList)
        if (updateCartRequestList.isNotEmpty()) {
            viewModelScope.launchCatchError(
                context = dispatchers.io,
                block = {
                    cartModel.lastValidateUseRequest = promoRequest
                    val updateCartWrapperRequest =
                        UpdateCartWrapperRequest(
                            updateCartRequestList = updateCartRequestList,
                            source = UpdateCartAndGetLastApplyUseCase.PARAM_VALUE_SOURCE_UPDATE_QTY_NOTES,
                            getLastApplyPromoRequest = promoRequest
                        )
                    val updateCartDataResponse =
                        updateCartAndGetLastApplyUseCase(updateCartWrapperRequest)
                    withContext(dispatchers.main) {
                        updateCartDataResponse.updateCartData?.let { updateCartData ->
                            if (updateCartData.isSuccess) {
                                updateCartDataResponse.promoUiModel?.let { promoUiModel ->
                                    syncCartGroupShopBoCodeWithPromoUiModel(promoUiModel)
                                    cartModel.apply {
                                        isLastApplyResponseStillValid = false
                                        cartModel.lastValidateUseResponse =
                                            ValidateUsePromoRevampUiModel(
                                                promoUiModel = promoUiModel
                                            )
                                        lastUpdateCartAndGetLastApplyResponse =
                                            updateCartDataResponse
                                    }
                                    _updateCartAndGetLastApplyEvent.value =
                                        UpdateCartAndGetLastApplyEvent.Success(promoUiModel)
                                }
                            }
                        }
                    }
                },
                onError = { throwable ->
                    withContext(dispatchers.main) {
                        _updateCartAndGetLastApplyEvent.value =
                            UpdateCartAndGetLastApplyEvent.Failed(throwable)
                    }
                }
            )
        } else {
            _globalEvent.value = CartGlobalEvent.ProgressLoading(false)
        }
    }

    fun doClearAllPromo() {
        cartModel.lastValidateUseRequest?.let {
            val param = ClearPromoRequest(
                ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE,
                orderData = ClearPromoOrderData(
                    codes = it.codes,
                    orders = it.orders.map { order ->
                        ClearPromoOrder(
                            uniqueId = order.uniqueId,
                            boType = order.boType,
                            codes = order.codes,
                            shopId = order.shopId,
                            warehouseId = order.warehouseId,
                            isPo = order.isPo,
                            poDuration = order.poDuration.toString(),
                            cartStringGroup = order.cartStringGroup
                        )
                    }
                )
            )
            viewModelScope.launchCatchError(
                context = dispatchers.io,
                block = {
                    clearCacheAutoApplyStackUseCase.setParams(param).executeOnBackground()
                },
                onError = { throwable ->
                    Timber.d(throwable)
                }
            )
            cartModel.isLastApplyResponseStillValid = false
            cartModel.lastValidateUseResponse = ValidateUsePromoRevampUiModel()
        }
    }

    private fun syncCartGroupShopBoCodeWithPromoUiModel(promoUiModel: PromoUiModel) {
        val groupDataList = CartDataHelper.getAllShopGroupDataList(cartDataList.value)
        promoUiModel.voucherOrderUiModels.forEach { voucherOrder ->
            if (
                voucherOrder.shippingId > 0 && voucherOrder.spId > 0 && voucherOrder.isTypeLogistic() && voucherOrder.messageUiModel.state == "green"
            ) {
                groupDataList.firstOrNull { it.cartString == voucherOrder.cartStringGroup }?.apply {
                    boCode = voucherOrder.code
                }
            }
        }
    }

    fun setItemSelected(position: Int, cartItemHolderData: CartItemHolderData, selected: Boolean) {
        var updatedShopData: CartGroupHolderData? = null
        var shopBottomIndex: Int? = null
        for ((id, data) in cartDataList.value.withIndex()) {
            if (data is CartGroupHolderData && data.cartString == cartItemHolderData.cartString && data.isError == cartItemHolderData.isError) {
                data.productUiModelList.forEachIndexed { index, item ->
                    if ((id + 1 + index) == position) {
                        item.isSelected = selected
                    }
                }

                var selectedCount = 0
                data.productUiModelList.forEach {
                    if (it.isSelected) {
                        selectedCount++
                    }
                }

                if (selectedCount == 0) {
                    data.isAllSelected = false
                    data.isPartialSelected = false
                } else if (selectedCount > 0 && selectedCount < data.productUiModelList.size) {
                    data.isAllSelected = false
                    data.isPartialSelected = true
                } else {
                    data.isAllSelected = true
                    data.isPartialSelected = false
                }
                updateSelectedAmount()
                updatedShopData = data
            } else if (data is CartShopBottomHolderData && data.shopData.cartString == cartItemHolderData.cartString && updatedShopData != null) {
                shopBottomIndex = id
                break
            }
        }
        if (shopBottomIndex != null && updatedShopData != null) {
            cartDataList.value[shopBottomIndex] = CartShopBottomHolderData(updatedShopData)
        }
    }

    fun processAddToCart(productModel: Any) {
        _globalEvent.value = CartGlobalEvent.ProgressLoading(true)

        var productId = 0L
        var shopId = 0
        var productName = ""
        var productCategory = ""
        var productPrice = ""
        var externalSource = ""
        var quantity = 0

        if (productModel is CartWishlistItemHolderData) {
            productId = productModel.id.toLongOrZero()
            shopId = productModel.shopId.toIntOrZero()
            productName = productModel.name
            productCategory = productModel.category
            productPrice = productModel.price
            quantity = productModel.minOrder
            externalSource = AtcFromExternalSource.ATC_FROM_WISHLIST
        } else if (productModel is CartRecentViewItemHolderData) {
            productId = productModel.id.toLongOrZero()
            shopId = productModel.shopId.toIntOrZero()
            productName = productModel.name
            productPrice = productModel.price
            quantity = productModel.minOrder
            externalSource = AtcFromExternalSource.ATC_FROM_RECENT_VIEW
            val clickUrl = productModel.clickUrl
            if (clickUrl.isNotEmpty() && productModel.isTopAds) {
                _cartTrackerEvent.value = CartTrackerEvent.ATCTrackingURLRecent(productModel)
            }
        } else if (productModel is CartRecommendationItemHolderData) {
            val recommendationItem = productModel.recommendationItem
            productId = recommendationItem.productId
            shopId = recommendationItem.shopId
            productName = recommendationItem.name
            productCategory = recommendationItem.categoryBreadcrumbs
            productPrice = recommendationItem.price
            quantity = productModel.recommendationItem.minOrder
            externalSource = AtcFromExternalSource.ATC_FROM_RECOMMENDATION

            val clickUrl = recommendationItem.clickUrl
            if (clickUrl.isNotEmpty()) {
                _cartTrackerEvent.value =
                    CartTrackerEvent.ATCTrackingURLRecommendation(recommendationItem)
            }
        } else if (productModel is BannerShopProductUiModel) {
            productId = productModel.productId.toLongOrZero()
            shopId = productModel.shopId.toIntOrZero()
            productName = productModel.productName
            productCategory = productModel.productCategory
            productPrice = productModel.productPrice
            quantity = productModel.productMinOrder
            externalSource = AtcFromExternalSource.ATC_FROM_RECOMMENDATION

            val clickUrl = productModel.adsClickUrl
            if (clickUrl.isNotEmpty()) {
                _cartTrackerEvent.value = CartTrackerEvent.ATCTrackingURLBanner(productModel)
            }
        }

        val addToCartRequestParams = AddToCartRequestParams().apply {
            this.productId = productId.toString()
            this.shopId = shopId.toString()
            this.quantity = quantity
            this.notes = ""
            this.warehouseId = "0"
            this.atcFromExternalSource = externalSource
            this.productName = productName
            this.category = productCategory
            this.price = productPrice
            this.userId = userSessionInterface.userId
        }

        viewModelScope.launchCatchError(
            context = dispatchers.io,
            block = {
                addToCartUseCase.setParams(addToCartRequestParams)
                val addToCartDataModel = addToCartUseCase.executeOnBackground()
                if (addToCartDataModel.status.equals(
                        AddToCartDataModel.STATUS_OK,
                        true
                    ) && addToCartDataModel.data.success == 1
                ) {
                    withContext(dispatchers.main) {
                        _addToCartEvent.value =
                            AddToCartEvent.Success(addToCartDataModel, productModel)
                    }
                } else {
                    if (addToCartDataModel.errorMessage.size > 0) {
                        throw ResponseErrorException(addToCartDataModel.errorMessage[0])
                    }
                }
            },
            onError = { throwable ->
                withContext(dispatchers.main) {
                    _addToCartEvent.value = AddToCartEvent.Failed(throwable)
                }
            }
        )
    }

    // ANALYTICS ATC
    fun generateAddToCartEnhanceEcommerceDataLayer(
        cartWishlistItemHolderData: CartWishlistItemHolderData,
        addToCartDataResponseModel: AddToCartDataModel,
        isCartEmpty: Boolean
    ): Map<String, Any> {
        val stringObjectMap = HashMap<String, Any>()
        val enhancedECommerceActionField = EnhancedECommerceActionField().apply {
            setList(if (isCartEmpty) EnhancedECommerceActionField.LIST_WISHLIST_ON_EMPTY_CART else EnhancedECommerceActionField.LIST_WISHLIST)
        }
        val enhancedECommerceProductCartMapData = EnhancedECommerceProductCartMapData().apply {
            setProductName(cartWishlistItemHolderData.name)
            setProductID(cartWishlistItemHolderData.id)
            setPrice(cartWishlistItemHolderData.rawPrice)
            setCategory(cartWishlistItemHolderData.category)
            setQty(cartWishlistItemHolderData.minOrder)
            setShopId(cartWishlistItemHolderData.shopId)
            setShopType(cartWishlistItemHolderData.shopType)
            setShopName(cartWishlistItemHolderData.shopName)
            setPicture(cartWishlistItemHolderData.imageUrl)
            setUrl(cartWishlistItemHolderData.url)
            setDimension45(addToCartDataResponseModel.data.cartId)
            setBrand("")
            setCategoryId("")
            setVariant("")
        }
        val enhancedECommerceAdd = EnhancedECommerceAdd().apply {
            setActionField(enhancedECommerceActionField.getActionFieldMap())
            addProduct(enhancedECommerceProductCartMapData.getProduct())
        }
        stringObjectMap["currencyCode"] = "IDR"
        stringObjectMap[EnhancedECommerceAdd.KEY_ADD] = enhancedECommerceAdd.getAddMap()
        return stringObjectMap
    }

    fun generateAddToCartEnhanceEcommerceDataLayer(
        cartRecentViewItemHolderData: CartRecentViewItemHolderData,
        addToCartDataResponseModel: AddToCartDataModel,
        isCartEmpty: Boolean
    ): Map<String, Any> {
        val stringObjectMap = HashMap<String, Any>()
        val enhancedECommerceActionField = EnhancedECommerceActionField().apply {
            setList(if (isCartEmpty) EnhancedECommerceActionField.LIST_RECENT_VIEW_ON_EMPTY_CART else EnhancedECommerceActionField.LIST_RECENT_VIEW)
        }
        val enhancedECommerceProductCartMapData = EnhancedECommerceProductCartMapData().apply {
            setProductName(cartRecentViewItemHolderData.name)
            setProductID(cartRecentViewItemHolderData.id)
            setPrice(cartRecentViewItemHolderData.price)
            setQty(cartRecentViewItemHolderData.minOrder)
            setDimension52(cartRecentViewItemHolderData.shopId)
            setDimension57(cartRecentViewItemHolderData.shopName)
            setDimension59(cartRecentViewItemHolderData.shopType)
            setDimension77(addToCartDataResponseModel.data.cartId)
            setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setCategoryId("")
            setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
        }
        val enhancedECommerceAdd = EnhancedECommerceAdd().apply {
            setActionField(enhancedECommerceActionField.getActionFieldMap())
            addProduct(enhancedECommerceProductCartMapData.getProduct())
        }
        stringObjectMap["currencyCode"] = "IDR"
        stringObjectMap[EnhancedECommerceAdd.KEY_ADD] = enhancedECommerceAdd.getAddMap()
        return stringObjectMap
    }

    fun generateAddToCartEnhanceEcommerceDataLayer(
        cartRecommendationItemHolderData: CartRecommendationItemHolderData,
        addToCartDataResponseModel: AddToCartDataModel,
        isCartEmpty: Boolean
    ): Map<String, Any> {
        val stringObjectMap = HashMap<String, Any>()
        val enhancedECommerceActionField = EnhancedECommerceActionField().apply {
            setList(
                getActionFieldListStr(
                    isCartEmpty,
                    cartRecommendationItemHolderData.recommendationItem
                )
            )
        }
        val enhancedECommerceProductCartMapData = EnhancedECommerceRecomProductCartMapData().apply {
            setProductName(cartRecommendationItemHolderData.recommendationItem.name)
            setProductID(cartRecommendationItemHolderData.recommendationItem.productId.toString())
            setPrice(
                cartRecommendationItemHolderData.recommendationItem.price.replace(
                    REGEX_NUMBER,
                    ""
                )
            )
            setCategory(cartRecommendationItemHolderData.recommendationItem.categoryBreadcrumbs)
            setQty(cartRecommendationItemHolderData.recommendationItem.minOrder)
            setShopId(cartRecommendationItemHolderData.recommendationItem.shopId.toString())
            setShopType(cartRecommendationItemHolderData.recommendationItem.shopType)
            setShopName(cartRecommendationItemHolderData.recommendationItem.shopName)
            setDimension45(addToCartDataResponseModel.data.cartId)
            setDimension53(cartRecommendationItemHolderData.recommendationItem.discountPercentageInt > 0)
            setDimension40(addToCartDataResponseModel.data.trackerListName)
            setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setCategoryId("")
            setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)

            val recommendationItem = cartRecommendationItemHolderData.recommendationItem
            if (recommendationItem.isFreeOngkirActive && recommendationItem.labelGroupList.hasLabelGroupFulfillment()) {
                setDimension83(EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR_EXTRA)
            } else if (recommendationItem.isFreeOngkirActive && !recommendationItem.labelGroupList.hasLabelGroupFulfillment()) {
                setDimension83(EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR)
            } else {
                setDimension83(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            }
        }

        val enhancedECommerceAdd = EnhancedECommerceAdd().apply {
            setActionField(enhancedECommerceActionField.getActionFieldMap())
            addProduct(enhancedECommerceProductCartMapData.getProduct())
        }
        stringObjectMap["currencyCode"] = "IDR"
        stringObjectMap[EnhancedECommerceAdd.KEY_ADD] = enhancedECommerceAdd.getAddMap()
        return stringObjectMap
    }

    fun updateSelectedAmount() {
        val allSelectedAvailableCartItems =
            CartDataHelper.getSelectedAvailableCartItemData(cartDataList.value)
        val totalSelected = allSelectedAvailableCartItems.count { it.isSelected }
        val selectedAmountHolderData = cartDataList.value.first()
        if (selectedAmountHolderData is CartSelectedAmountHolderData) {
            selectedAmountHolderData.selectedAmount = totalSelected
        }
        _selectedAmountState.value = totalSelected
    }

    fun generateRecentViewProductClickEmptyCartDataLayer(
        cartRecentViewItemHolderData: CartRecentViewItemHolderData,
        position: Int
    ): Map<String, Any> {
        val enhancedECommerceProductData = EnhancedECommerceProductData().apply {
            setProductID(cartRecentViewItemHolderData.id)
            setProductName(cartRecentViewItemHolderData.name)
            setPrice(cartRecentViewItemHolderData.price.replace(REGEX_NUMBER, ""))
            setBrand(EnhancedECommerceProductData.DEFAULT_VALUE_NONE_OTHER)
            setCategory(EnhancedECommerceProductData.DEFAULT_VALUE_NONE_OTHER)
            setPosition(position.toString())
            setVariant(EnhancedECommerceProductData.DEFAULT_VALUE_NONE_OTHER)
        }

        val productsData = ArrayList<Map<String, Any>>().apply {
            add(enhancedECommerceProductData.getProduct())
        }

        val enhancedECommerceEmptyCart = getEnhancedECommerceOnClickData(
            productsData,
            EnhancedECommerceActionFieldData.VALUE_SECTION_NAME_RECENT_VIEW_EMPTY_CART
        )

        return enhancedECommerceEmptyCart.getData()
    }

    private fun getEnhancedECommerceOnClickData(
        productsData: List<Map<String, Any>>,
        valueSectionName: String
    ): EnhancedECommerceData {
        val enhancedECommerceActionFieldData = EnhancedECommerceActionFieldData().apply {
            setList(valueSectionName)
        }

        val enhancedECommerceClickData = EnhancedECommerceClickData().apply {
            setActionField(enhancedECommerceActionFieldData.getData())
            setProducts(productsData)
        }

        val enhancedECommerce = EnhancedECommerceData().apply {
            setClickData(enhancedECommerceClickData.getData())
        }
        return enhancedECommerce
    }

    fun generateRecentViewProductClickDataLayer(
        cartRecentViewItemHolderData: CartRecentViewItemHolderData,
        position: Int
    ): Map<String, Any> {
        val stringObjectMap = HashMap<String, Any>()
        val enhancedECommerceActionField = EnhancedECommerceActionField().apply {
            setList(EnhancedECommerceActionFieldData.VALUE_SECTION_NAME_RECENT_VIEW)
        }
        val enhancedECommerceProductCartMapData = EnhancedECommerceProductCartMapData().apply {
            setProductName(cartRecentViewItemHolderData.name)
            setProductID(cartRecentViewItemHolderData.id)
            setPrice(cartRecentViewItemHolderData.price.replace(REGEX_NUMBER, ""))
            setCategory(EnhancedECommerceProductData.DEFAULT_VALUE_NONE_OTHER)
            setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setPosition(position.toString())
        }
        val enhancedECommerceAdd = EnhancedECommerceAdd().apply {
            setActionField(enhancedECommerceActionField.getActionFieldMap())
            addProduct(enhancedECommerceProductCartMapData.getProduct())
        }
        stringObjectMap["currencyCode"] = "IDR"
        stringObjectMap[EnhancedECommerceAdd.KEY_ADD] = enhancedECommerceAdd.getAddMap()
        return stringObjectMap
    }

    fun generateWishlistProductClickEmptyCartDataLayer(
        cartWishlistItemHolderData: CartWishlistItemHolderData,
        position: Int
    ): Map<String, Any> {
        val enhancedECommerceEmptyCartProductData = EnhancedECommerceProductData().apply {
            setProductID(cartWishlistItemHolderData.id)
            setProductName(cartWishlistItemHolderData.name)
            setPrice(cartWishlistItemHolderData.price.replace(REGEX_NUMBER, ""))
            setBrand(EnhancedECommerceProductData.DEFAULT_VALUE_NONE_OTHER)
            setCategory(cartWishlistItemHolderData.category)
            setPosition(position.toString())
            setVariant(EnhancedECommerceProductData.DEFAULT_VALUE_NONE_OTHER)
        }

        val productsData = ArrayList<Map<String, Any>>().apply {
            add(enhancedECommerceEmptyCartProductData.getProduct())
        }

        val enhancedECommerceEmptyCart = getEnhancedECommerceOnClickData(
            productsData,
            EnhancedECommerceActionFieldData.VALUE_SECTION_NAME_WISHLIST_EMPTY_CART
        )

        return enhancedECommerceEmptyCart.getData()
    }

    fun generateWishlistProductClickDataLayer(
        cartWishlistItemHolderData: CartWishlistItemHolderData,
        position: Int
    ): Map<String, Any> {
        val stringObjectMap = HashMap<String, Any>()
        val enhancedECommerceActionField = EnhancedECommerceActionField().apply {
            setList(EnhancedECommerceActionFieldData.VALUE_SECTION_NAME_WISHLIST)
        }
        val enhancedECommerceProductCartMapData = EnhancedECommerceProductCartMapData().apply {
            setProductName(cartWishlistItemHolderData.name)
            setProductID(cartWishlistItemHolderData.id)
            setPrice(cartWishlistItemHolderData.price.replace(REGEX_NUMBER, ""))
            setCategory(cartWishlistItemHolderData.category)
            setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setPosition(position.toString())
        }
        val enhancedECommerceAdd = EnhancedECommerceAdd().apply {
            setActionField(enhancedECommerceActionField.getActionFieldMap())
            addProduct(enhancedECommerceProductCartMapData.getProduct())
        }
        stringObjectMap["currencyCode"] = "IDR"
        stringObjectMap[EnhancedECommerceAdd.KEY_ADD] = enhancedECommerceAdd.getAddMap()
        return stringObjectMap
    }

    fun processAddCartToWishlist(
        productId: String,
        userId: String,
        isLastItem: Boolean,
        source: String,
        wishlistIcon: IconUnify,
        animatedWishlistImage: ImageView
    ) {
        viewModelScope.launch(dispatchers.io) {
            addToWishlistV2UseCase.setParams(productId, userId)
            val result = addToWishlistV2UseCase.executeOnBackground()
            withContext(dispatchers.main) {
                if (result is Success) {
                    _addCartToWishlistV2Event.value = AddCartToWishlistV2Event.Success(
                        result.data,
                        productId,
                        isLastItem,
                        source,
                        wishlistIcon,
                        animatedWishlistImage
                    )
                } else {
                    val error = (result as Fail).throwable
                    _addCartToWishlistV2Event.value = AddCartToWishlistV2Event.Failed(error)
                }
            }
        }
    }

    fun processRemoveFromWishlistV2(
        productId: String,
        userId: String,
        isFromCart: Boolean,
        wishlistIcon: IconUnify? = null,
        position: Int = 0
    ) {
        viewModelScope.launch(dispatchers.io) {
            deleteWishlistV2UseCase.setParams(productId, userId)
            val result = deleteWishlistV2UseCase.executeOnBackground()
            withContext(dispatchers.main) {
                if (result is Success) {
                    if (isFromCart) {
                        _removeFromWishlistEvent.value =
                            RemoveFromWishlistEvent.RemoveWishlistFromCartSuccess(
                                wishlistIcon,
                                position
                            )
                    } else {
                        _removeFromWishlistEvent.value = RemoveFromWishlistEvent.Success(
                            result.data,
                            productId
                        )
                    }
                } else {
                    val error = (result as Fail).throwable
                    if (isFromCart) {
                        _removeFromWishlistEvent.value =
                            RemoveFromWishlistEvent.RemoveWishlistFromCartFailed(
                                error
                            )
                    } else {
                        _removeFromWishlistEvent.value = RemoveFromWishlistEvent.Failed(
                            error,
                            productId
                        )
                    }
                }
            }
        }
    }

    fun removeProductByCartId(
        cartIds: List<String>,
        needRefresh: Boolean,
        isFromGlobalCheckbox: Boolean
    ): Triple<List<Int>, List<Int>, ArrayList<Any>> {
        val toBeRemovedItems = mutableListOf<Any>()
        val toBeRemovedIndices = mutableListOf<Int>()
        val toBeUpdatedIndices = mutableListOf<Int>()

        val newCartDataList = ArrayList(cartDataList.value.toMutableList())

        var cartSelectedAmountHolderDataIndexPair: Pair<CartSelectedAmountHolderData, Int>? = null
        var cartItemTickerErrorHolderDataIndexPair: Pair<CartItemTickerErrorHolderData, Int>? = null
        var disabledItemHeaderHolderDataIndexPair: Pair<DisabledItemHeaderHolderData, Int>? = null
        var disabledAccordionHolderDataIndexPair: Pair<DisabledAccordionHolderData, Int>? = null
        loop@ for ((index, data) in newCartDataList.withIndex()) {
            when {
                data is CartSelectedAmountHolderData ->
                    cartSelectedAmountHolderDataIndexPair =
                        Pair(data, index)

                data is CartItemTickerErrorHolderData ->
                    cartItemTickerErrorHolderDataIndexPair =
                        Pair(data, index)

                data is DisabledItemHeaderHolderData ->
                    disabledItemHeaderHolderDataIndexPair =
                        Pair(data, index)

                data is DisabledAccordionHolderData ->
                    disabledAccordionHolderDataIndexPair =
                        Pair(data, index)

                data is CartGroupHolderData -> {
                    val toBeDeletedProducts = mutableListOf<CartItemHolderData>()
                    var hasSelectDeletedProducts = false
                    var selectedNonDeletedProducts = 0
                    data.productUiModelList.forEach { cartItemHolderData ->
                        if (cartIds.contains(cartItemHolderData.cartId)) {
                            toBeDeletedProducts.add(cartItemHolderData)
                            if (cartItemHolderData.isSelected) {
                                hasSelectDeletedProducts = true
                            }
                        } else if (cartItemHolderData.isSelected) {
                            selectedNonDeletedProducts += 1
                        }
                    }
                    if (toBeDeletedProducts.isNotEmpty()) {
                        data.productUiModelList.removeAll(toBeDeletedProducts)
                        if (data.productUiModelList.isEmpty()) {
                            val previousIndex = index - 1
                            if (data.isError && previousIndex < newCartDataList.size) {
                                val previousData = newCartDataList[previousIndex]
                                if (previousData is DisabledReasonHolderData) {
                                    toBeRemovedItems.add(previousData)
                                    toBeRemovedIndices.add(previousIndex)
                                }
                            }
                            toBeRemovedItems.add(data)
                            toBeRemovedIndices.add(index)
                        } else {
                            // update selection
                            data.productUiModelList.last().isFinalItem = true
                            updateShopShownByCartGroup(data)
                            data.isAllSelected =
                                selectedNonDeletedProducts > 0 && data.productUiModelList.size == selectedNonDeletedProducts
                            data.isPartialSelected =
                                selectedNonDeletedProducts > 0 && data.productUiModelList.size > selectedNonDeletedProducts
                            if (!needRefresh && (isFromGlobalCheckbox || hasSelectDeletedProducts) && selectedNonDeletedProducts > 0) {
                                _globalEvent.value = CartGlobalEvent.CheckGroupShopCartTicker(data)
                            }
                            toBeUpdatedIndices.add(index)
                        }
                    }
                }

                data is CartItemHolderData -> {
                    if (cartIds.contains(data.cartId)) {
                        toBeRemovedItems.add(data)
                        toBeRemovedIndices.add(index)
                    }
                }

                hasReachAllShopItems(data) -> break@loop
            }
        }

        newCartDataList.removeAll(toBeRemovedItems.toSet())

        if (CartDataHelper.getAllAvailableCartItemData(newCartDataList).isEmpty()) {
            cartSelectedAmountHolderDataIndexPair?.let {
                newCartDataList.remove(it.first)
                toBeRemovedItems.add(it.second)
            }
        }

        val disabledCartItems = CartDataHelper.getAllDisabledCartItemData(
            newCartDataList,
            cartModel
        )
        if (disabledCartItems.isEmpty()) {
            cartItemTickerErrorHolderDataIndexPair?.let {
                newCartDataList.remove(it.first)
                toBeRemovedItems.add(it.second)
                toBeRemovedIndices.add(it.second)
            }
            disabledItemHeaderHolderDataIndexPair?.let {
                newCartDataList.remove(it.first)
                toBeRemovedItems.add(it.second)
                toBeRemovedIndices.add(it.second)
            }
            disabledAccordionHolderDataIndexPair?.let {
                newCartDataList.remove(it.first)
                toBeRemovedItems.add(it.second)
                toBeRemovedIndices.add(it.second)
            }
        } else {
            val errorItemCount = disabledCartItems.size
            cartItemTickerErrorHolderDataIndexPair?.let {
                it.first.errorProductCount = errorItemCount
                toBeUpdatedIndices.add(it.second)
            }
            disabledItemHeaderHolderDataIndexPair?.let {
                it.first.disabledItemCount = errorItemCount
                toBeUpdatedIndices.add(it.second)
            }

            var removeAccordion = false
            if (errorItemCount == 1) {
                removeAccordion = true
            } else {
                val bundleIdCartIdSet = mutableSetOf<String>()
                disabledCartItems.forEach {
                    if (it.isBundlingItem) {
                        bundleIdCartIdSet.add(it.bundleId)
                    } else {
                        bundleIdCartIdSet.add(it.cartId)
                    }
                }
                if (bundleIdCartIdSet.size <= 1) {
                    removeAccordion = true
                }
            }

            if (removeAccordion) {
                disabledAccordionHolderDataIndexPair?.let {
                    newCartDataList.remove(it.first)
                    toBeRemovedItems.add(it.second)
                    toBeRemovedIndices.add(it.second)
                }
            }
        }
        return Triple(toBeRemovedIndices, toBeUpdatedIndices, newCartDataList)
    }

    private fun updateShopShownByCartGroup(cartGroupHolderData: CartGroupHolderData) {
        if (cartGroupHolderData.isUsingOWOCDesign()) {
            val groupPromoHolderDataMap = hashMapOf<String, MutableList<CartItemHolderData>>()
            cartGroupHolderData.productUiModelList.forEach {
                it.isShopShown = false
                val cartStringOrder = it.cartStringOrder
                if (!groupPromoHolderDataMap.containsKey(cartStringOrder)) {
                    groupPromoHolderDataMap[cartStringOrder] = arrayListOf()
                }
                groupPromoHolderDataMap[cartStringOrder]?.add(it)
            }
            groupPromoHolderDataMap.forEach { (_, value) ->
                value.firstOrNull()?.isShopShown = true
            }
        }
    }

    fun checkCartShopGroupTicker(cartGroupHolderData: CartGroupHolderData) {
        if (cartModel.lastCartShopGroupTickerCartString == cartGroupHolderData.cartString) {
            cartShopGroupTickerJob?.cancel()
        }
        cartModel.lastCartShopGroupTickerCartString = cartGroupHolderData.cartString
        cartShopGroupTickerJob = viewModelScope.launch(dispatchers.io) {
            try {
                delay(CART_SHOP_GROUP_TICKER_DELAY)
                cartGroupHolderData.cartShopGroupTicker.enableBundleCrossSell =
                    checkEnableBundleCrossSell(cartGroupHolderData)
                if (!cartGroupHolderData.cartShopGroupTicker.enableBoAffordability &&
                    !cartGroupHolderData.cartShopGroupTicker.enableBundleCrossSell
                ) {
                    cartGroupHolderData.cartShopGroupTicker.state = CartShopGroupTickerState.EMPTY
                    withContext(dispatchers.main) {
                        _globalEvent.value =
                            CartGlobalEvent.UpdateCartShopGroupTicker(cartGroupHolderData)
                    }
                    return@launch
                }
                val shopShipments = cartGroupHolderData.shopShipments
                // Recalculate total price and total weight, to prevent racing condition
                val (shopProductList, shopTotalWeight) =
                    getAvailableCartItemDataListAndShopTotalWeight(cartGroupHolderData)
                if (cartGroupHolderData.cartShopGroupTicker.enableBoAffordability &&
                    cartGroupHolderData.shouldValidateWeight &&
                    shopTotalWeight > cartGroupHolderData.maximumShippingWeight
                ) {
                    // Check for overweight (only when BO Affordability is enabled)
                    cartGroupHolderData.cartShopGroupTicker.state = CartShopGroupTickerState.FAILED
                    withContext(dispatchers.main) {
                        _globalEvent.value =
                            CartGlobalEvent.UpdateCartShopGroupTicker(cartGroupHolderData)
                    }
                    return@launch
                }
                val calculatePriceMarketplaceProduct =
                    cartCalculator.calculatePriceMarketplaceProduct(
                        allCartItemDataList = shopProductList,
                        cartModel = cartModel,
                        updateCartModel = { newCartModel ->
                            cartModel = newCartModel
                        }
                    )
                val subtotalPrice = calculatePriceMarketplaceProduct.second.second.toLong()
                val shipping = ShippingParam().apply {
                    destinationDistrictId = cartModel.lca?.district_id
                    destinationLongitude = cartModel.lca?.long
                    destinationLatitude = cartModel.lca?.lat
                    destinationPostalCode = cartModel.lca?.postal_code
                    originDistrictId = cartGroupHolderData.districtId
                    originLongitude = cartGroupHolderData.longitude
                    originLatitude = cartGroupHolderData.latitude
                    originPostalCode = cartGroupHolderData.postalCode
                    weightInKilograms = shopTotalWeight / BO_AFFORDABILITY_WEIGHT_KILO
                    weightActualInKilograms = shopTotalWeight / BO_AFFORDABILITY_WEIGHT_KILO
                    orderValue = subtotalPrice
                    shopId = cartGroupHolderData.shop.shopId
                    shopTier = cartGroupHolderData.shop.shopTypeInfo.shopTier
                    uniqueId = cartGroupHolderData.cartString
                    isFulfillment = cartGroupHolderData.isFulfillment
                    boMetadata = cartGroupHolderData.boMetadata
                    products = shopProductList.map {
                        Product(
                            it.productId.toLong(),
                            it.isFreeShipping,
                            it.isFreeShippingExtra
                        )
                    }
                }
                val cartAggregatorParam = CartShopGroupTickerAggregatorParam(
                    ratesParam = RatesParam.Builder(shopShipments, shipping)
                        .warehouseId(cartGroupHolderData.warehouseId.toString())
                        .build(),
                    enableBoAffordability = cartGroupHolderData.cartShopGroupTicker.enableBoAffordability,
                    enableBundleCrossSell = cartGroupHolderData.cartShopGroupTicker.enableBundleCrossSell,
                    isTokoNow = cartGroupHolderData.isTokoNow
                )
                val response = cartShopGroupTickerAggregatorUseCase(cartAggregatorParam)
                    .cartShopGroupTickerAggregator.data
                cartGroupHolderData.cartShopGroupTicker.cartIds =
                    shopProductList.joinToString(",") { it.cartId }
                cartGroupHolderData.cartShopGroupTicker.tickerText = response.ticker.text
                cartGroupHolderData.cartShopGroupTicker.leftIcon = response.ticker.icon.leftIcon
                cartGroupHolderData.cartShopGroupTicker.leftIconDark =
                    response.ticker.icon.leftIconDark
                cartGroupHolderData.cartShopGroupTicker.rightIcon =
                    response.ticker.icon.rightIcon
                cartGroupHolderData.cartShopGroupTicker.rightIconDark =
                    response.ticker.icon.rightIconDark
                cartGroupHolderData.cartShopGroupTicker.applink = response.ticker.applink
                cartGroupHolderData.cartShopGroupTicker.action = response.ticker.action
                cartGroupHolderData.cartShopGroupTicker.cartBundlingBottomSheetData =
                    CartBundlingBottomSheetData(
                        title = response.bundleBottomSheet.title,
                        description = response.bundleBottomSheet.description,
                        bottomTicker = response.bundleBottomSheet.bottomTicker,
                        bundleIds = response.bundleBottomSheet.bundleIds
                    )
                cartGroupHolderData.cartShopGroupTicker.hasSeenTicker = false
                if (response.ticker.text.isBlank()) {
                    cartGroupHolderData.cartShopGroupTicker.state =
                        CartShopGroupTickerState.EMPTY
                } else if (subtotalPrice >= response.minTransaction) {
                    cartGroupHolderData.cartShopGroupTicker.state =
                        CartShopGroupTickerState.SUCCESS_AFFORD
                } else {
                    cartGroupHolderData.cartShopGroupTicker.state =
                        CartShopGroupTickerState.SUCCESS_NOT_AFFORD
                }
                withContext(dispatchers.main) {
                    _globalEvent.value =
                        CartGlobalEvent.UpdateCartShopGroupTicker(cartGroupHolderData)
                }
            } catch (t: Throwable) {
                if (t !is CancellationException) {
                    cartGroupHolderData.cartShopGroupTicker.tickerText = ""
                    cartGroupHolderData.cartShopGroupTicker.state = CartShopGroupTickerState.FAILED
                    withContext(dispatchers.main) {
                        _globalEvent.value =
                            CartGlobalEvent.UpdateCartShopGroupTicker(cartGroupHolderData)
                    }
                }
            }
        }
    }

    fun checkEnableBundleCrossSell(cartGroupHolderData: CartGroupHolderData): Boolean {
        val hasCheckedProductWithBundle = cartGroupHolderData.productUiModelList
            .any { it.isSelected && !it.isBundlingItem && it.bundleIds.isNotEmpty() }
        val hasCheckedBundleProduct = cartGroupHolderData.productUiModelList
            .any { it.isSelected && it.isBundlingItem && it.bundleIds.isNotEmpty() }
        return cartGroupHolderData.cartShopGroupTicker.enableCartAggregator &&
            hasCheckedProductWithBundle && !hasCheckedBundleProduct
    }

    fun validateBoPromo(validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel) {
        val groupDataList = CartDataHelper.getAllShopGroupDataList(cartDataList.value)
        val boGroupUniqueIds = mutableSetOf<String>()
        for (voucherOrderUiModel in validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels) {
            if (voucherOrderUiModel.shippingId > 0 && voucherOrderUiModel.spId > 0 && voucherOrderUiModel.type == "logistic") {
                if (voucherOrderUiModel.messageUiModel.state == "green") {
                    groupDataList.firstOrNull { it.cartString == voucherOrderUiModel.cartStringGroup }
                        ?.apply {
                            boCode = voucherOrderUiModel.code
                        }
                    boGroupUniqueIds.add(voucherOrderUiModel.cartStringGroup)
                }
            }
        }
        for (group in groupDataList) {
            if (group.boCode.isNotEmpty() && !boGroupUniqueIds.contains(group.cartString)) {
                clearBo(group)
            }
        }
    }

    private fun clearBo(group: CartGroupHolderData) {
        viewModelScope.launchCatchError(
            context = dispatchers.io,
            block = {
                val cartStringGroupSet = mutableSetOf<String>()
                val cartPromoHolderData =
                    PromoRequestMapper.mapSelectedCartGroupToPromoData(listOf(group))
                clearCacheAutoApplyStackUseCase.setParams(
                    ClearPromoRequest(
                        serviceId = ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE,
                        orderData = ClearPromoOrderData(
                            orders = cartPromoHolderData.values.map {
                                val isNoCodeExistInCurrentGroup =
                                    !cartStringGroupSet.contains(it.cartStringGroup)
                                if (isNoCodeExistInCurrentGroup) {
                                    cartStringGroupSet.add(it.cartStringGroup)
                                }
                                ClearPromoOrder(
                                    uniqueId = it.cartStringOrder,
                                    boType = group.boMetadata.boType,
                                    codes = if (isNoCodeExistInCurrentGroup) {
                                        mutableListOf(group.boCode)
                                    } else {
                                        mutableListOf()
                                    },
                                    shopId = it.shopId.toLongOrZero(),
                                    isPo = group.isPo,
                                    poDuration = it.poDuration,
                                    warehouseId = group.warehouseId,
                                    cartStringGroup = group.cartString
                                )
                            }
                        )
                    )
                ).executeOnBackground()
            },
            onError = {
                Timber.d(it)
            }
        )

        group.promoCodes = ArrayList(group.promoCodes).apply { remove(group.boCode) }
        group.boCode = ""
    }

    fun processDeleteCartItem(
        removedCartItems: List<CartItemHolderData>,
        addWishList: Boolean,
        forceExpandCollapsedUnavailableItems: Boolean = false,
        isFromGlobalCheckbox: Boolean = false,
        isFromEditBundle: Boolean = false
    ) {
        _globalEvent.value = CartGlobalEvent.ProgressLoading(true)
        val allCartItemData = CartDataHelper.getAllCartItemData(
            cartDataList.value,
            cartModel
        )

        val removeAllItems = allCartItemData.size == removedCartItems.size
        val toBeDeletedCartIds = ArrayList<String>()
        for (cartItemData in removedCartItems) {
            toBeDeletedCartIds.add(cartItemData.cartId)
        }

        deleteCartUseCase.setParams(toBeDeletedCartIds, addWishList)
        deleteCartUseCase.execute(
            onSuccess = {
                _deleteCartEvent.value = DeleteCartEvent.Success(
                    toBeDeletedCartIds,
                    removeAllItems,
                    forceExpandCollapsedUnavailableItems,
                    addWishList,
                    isFromGlobalCheckbox,
                    isFromEditBundle
                )
            },
            onError = { throwable ->
                _deleteCartEvent.value = DeleteCartEvent.Failed(
                    forceExpandCollapsedUnavailableItems,
                    throwable
                )
            }
        )
    }

    fun processUndoDeleteCartItem(cartIds: List<String>) {
        _globalEvent.value = CartGlobalEvent.ProgressLoading(true)
        undoDeleteCartUseCase.setParams(cartIds)
        undoDeleteCartUseCase.execute(
            onSuccess = {
                _undoDeleteEvent.value = UndoDeleteEvent.Success
            },
            onError = { throwable ->
                Timber.e(throwable)
                _undoDeleteEvent.value = UndoDeleteEvent.Failed(throwable)
            }
        )
    }

    fun generateDeleteCartDataAnalytics(cartItemDataList: List<CartItemHolderData>): Map<String, Any> {
        val enhancedECommerceCartMapData = EnhancedECommerceCartMapData().apply {
            for (cartItemData in cartItemDataList) {
                val enhancedECommerceProductCartMapData =
                    getEnhancedECommerceProductCartMapData(cartItemData)
                addProduct(enhancedECommerceProductCartMapData.getProduct())
            }

            setCurrencyCode(EnhancedECommerceCartMapData.VALUE_CURRENCY_IDR)
            setAction(EnhancedECommerceCartMapData.REMOVE_ACTION)
        }
        return enhancedECommerceCartMapData.cartMap
    }

    private fun getEnhancedECommerceProductCartMapData(cartItemHolderData: CartItemHolderData): EnhancedECommerceProductCartMapData {
        return EnhancedECommerceProductCartMapData().apply {
            setCartId(cartItemHolderData.cartId)
            setDimension45(cartItemHolderData.cartId)
            setProductName(cartItemHolderData.productName)
            setProductID(cartItemHolderData.productId)
            setPrice(cartItemHolderData.productPrice.toString())
            setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setCategory(
                cartItemHolderData.category.ifBlank {
                    EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                }
            )
            setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setQty(cartItemHolderData.quantity)
            setShopId(cartItemHolderData.shopHolderData.shopId)
            setShopType(cartItemHolderData.shopHolderData.shopTypeInfo.titleFmt)
            setShopName(cartItemHolderData.shopHolderData.shopName)
            setCategoryId(cartItemHolderData.categoryId)
            setAttribution(
                cartItemHolderData.trackerAttribution.ifBlank {
                    EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                }
            )
            setDimension38(
                cartItemHolderData.trackerAttribution.ifBlank {
                    EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                }
            )
            setListName(
                cartItemHolderData.trackerListName.ifBlank {
                    EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                }
            )
            setDimension40(
                cartItemHolderData.trackerListName.ifBlank {
                    EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                }
            )
        }
    }

    suspend fun emitTokonowUpdated(value: Boolean) {
        _tokoNowProductUpdater.emit(value)
    }

    fun generateCartBundlingPromotionsAnalyticsData(
        bundleDetail: BundleDetailUiModel
    ): List<Bundle> {
        return bundleDetail.products.map {
            bundleOf(
                ConstantTransactionAnalytics.Key.CREATIVE_NAME to "",
                ConstantTransactionAnalytics.Key.CREATIVE_SLOT to "",
                ConstantTransactionAnalytics.Key.DIMENSION40 to
                    ConstantTransactionAnalytics.EventLabel.CART_BUNDLING_BOTTOM_SHEET_BUNDLE_LIST_NAME,
                ConstantTransactionAnalytics.Key.ITEM_ID to it.productId,
                ConstantTransactionAnalytics.Key.ITEM_NAME to it.productName
            )
        }
    }

    fun generateRecommendationDataOnClickAnalytics(
        recommendationItem: RecommendationItem,
        isEmptyCart: Boolean,
        position: Int
    ): Map<String, Any> {
        val enhancedECommerceProductCartMapData = EnhancedECommerceProductCartMapData().apply {
            setProductID(recommendationItem.productId.toString())
            setProductName(recommendationItem.name)
            setPrice(recommendationItem.price.replace(REGEX_NUMBER, ""))
            setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setCategory(
                recommendationItem.categoryBreadcrumbs.ifBlank {
                    EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                }
            )
            setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setPosition(position.toString())
            setAttribution(EnhancedECommerceProductCartMapData.RECOMMENDATION_ATTRIBUTION)
            if (recommendationItem.isFreeOngkirActive && recommendationItem.labelGroupList.hasLabelGroupFulfillment()) {
                setDimension83(EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR_EXTRA)
            } else if (recommendationItem.isFreeOngkirActive && !recommendationItem.labelGroupList.hasLabelGroupFulfillment()) {
                setDimension83(EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR)
            } else {
                setDimension83(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            }
        }
        val productsData = ArrayList<Map<String, Any>>().apply {
            add(enhancedECommerceProductCartMapData.getProduct())
        }
        return getEnhancedECommerceOnClickData(
            productsData,
            getActionFieldListStr(isEmptyCart, recommendationItem)
        ).getData()
    }

    fun redirectToLite(url: String, adsId: String) {
        _globalEvent.value = CartGlobalEvent.ProgressLoading(true)
        if (adsId.trim { c -> c <= ' ' }.isNotEmpty()) {
            seamlessLoginUsecase.generateSeamlessUrl(
                url.replace(QUERY_APP_CLIENT_ID, adsId),
                object : SeamlessLoginSubscriber {
                    override fun onUrlGenerated(url: String) {
                        _seamlessLoginEvent.value = SeamlessLoginEvent.Success(url)
                    }

                    override fun onError(msg: String) {
                        _seamlessLoginEvent.value = SeamlessLoginEvent.Failed(msg)
                    }
                }
            )
        } else {
            _seamlessLoginEvent.value = SeamlessLoginEvent.Failed("")
        }
    }

    fun updateAddOnByCartId(
        cartId: String,
        newAddOnWording: String,
        selectedAddons: List<AddOnUIModel>
    ) {
        val position: Int
        loop@ for ((index, item) in cartDataList.value.withIndex()) {
            if (item is CartItemHolderData) {
                if (item.cartId == cartId) {
                    position = index
                    item.addOnsProduct.widget.wording = newAddOnWording
                    item.addOnsProduct.listData.clear()
                    selectedAddons.forEach {
                        item.addOnsProduct.listData.add(
                            CartAddOnProductData(
                                id = it.id,
                                uniqueId = it.uniqueId,
                                status = it.getSelectedStatus().value,
                                type = it.addOnType,
                                price = it.price.toDouble()
                            )
                        )
                    }
                    _globalEvent.value = CartGlobalEvent.AdapterItemChanged(position)
                    break@loop
                }
            }
        }
    }

    fun updateWishlistDataByProductId(productId: String, isWishlisted: Boolean) {
        outerloop@ for (i in cartDataList.value.indices) {
            val obj = cartDataList.value[i]
            if (obj is CartGroupHolderData) {
                val cartGroupHolderData = cartDataList.value[i] as CartGroupHolderData
                innerloop@ for (cartItemHolderData in cartGroupHolderData.productUiModelList) {
                    if (cartItemHolderData.productId == productId) {
                        cartItemHolderData.isWishlisted = isWishlisted
                        if (cartGroupHolderData.isCollapsed) {
                            _globalEvent.value = CartGlobalEvent.AdapterItemChanged(i)
                            break@outerloop
                        }
                        break@innerloop
                    }
                }
            } else if (obj is CartItemHolderData && obj.productId == productId) {
                obj.isWishlisted = isWishlisted
                _globalEvent.value = CartGlobalEvent.AdapterItemChanged(i)
                break@outerloop
            }
        }
    }

    fun updateWishlistHolderData(productId: String, isWishlist: Boolean) {
        outerloop@ for (any in cartDataList.value) {
            if (any is CartWishlistHolderData) {
                val wishlist = any.wishList
                for (data in wishlist) {
                    if (data.id == productId) {
                        data.isWishlist = isWishlist
                        _globalEvent.value = CartGlobalEvent.AdapterItemChanged(
                            wishlist.indexOf(data)
                        )
                        break@outerloop
                    }
                }
                break@outerloop
            }
        }
    }

    fun updateRecentViewData(productId: String, isWishlist: Boolean) {
        outerloop@ for (any in cartDataList.value) {
            if (any is CartRecentViewHolderData) {
                val recentViews = any.recentViewList
                for (data in recentViews) {
                    if (data.id == productId) {
                        data.isWishlist = isWishlist
                        _globalEvent.value = CartGlobalEvent.AdapterItemChanged(
                            recentViews.indexOf(data)
                        )
                        break@outerloop
                    }
                }
                break@outerloop
            }
        }
    }

    fun removeWishlist(productId: String) {
        var wishlistItemIndex = 0
        var cartWishlistHolderData: CartWishlistHolderData? = null
        for (any in cartDataList.value) {
            if (any is CartWishlistHolderData) {
                val wishlist = any.wishList
                for ((j, data) in wishlist.withIndex()) {
                    if (data.id == productId) {
                        wishlistItemIndex = j
                        cartWishlistHolderData = any
                        break
                    }
                }
                break
            }
        }

        if (cartWishlistHolderData != null) {
            if (cartWishlistHolderData.wishList.size > 1) {
                _globalEvent.value = CartGlobalEvent.OnNeedUpdateWishlistAdapterData(
                    cartWishlistHolderData,
                    wishlistItemIndex
                )
            } else {
                cartDataList.notifyObserver()
            }
        }
    }

    fun processAddToCartExternal(productId: Long) {
        _globalEvent.value = CartGlobalEvent.ProgressLoading(true)
        viewModelScope.launchCatchError(
            context = dispatchers.io,
            block = {
                val model = addToCartExternalUseCase(
                    Pair(
                        productId.toString(),
                        userSessionInterface.userId
                    )
                )
                withContext(dispatchers.main) {
                    _addToCartExternalEvent.value = AddToCartExternalEvent.Success(model)
                }
            },
            onError = { t ->
                Timber.d(t)
                withContext(dispatchers.main) {
                    _addToCartExternalEvent.value = AddToCartExternalEvent.Failed(t)
                }
            }
        )
    }

    fun getCartItemByBundleGroupId(
        bundleId: String,
        bundleGroupId: String
    ): List<CartItemHolderData> {
        val cartItemHolderDataList = mutableListOf<CartItemHolderData>()
        loop@ for (data in cartDataList.value) {
            if (cartItemHolderDataList.isNotEmpty()) {
                break@loop
            }
            when (data) {
                is CartGroupHolderData -> {
                    data.productUiModelList.forEach { cartItemHolderData ->
                        if (cartItemHolderData.isBundlingItem && cartItemHolderData.bundleId == bundleId && cartItemHolderData.bundleGroupId == bundleGroupId) {
                            cartItemHolderDataList.add(cartItemHolderData)
                        }
                    }
                }

                hasReachAllShopItems(data) -> break@loop
            }
        }

        return cartItemHolderDataList
    }

    fun updateCartDataList(newList: ArrayList<Any>) {
        cartDataList.value = newList
    }

    fun followShop(shopId: String) {
        _globalEvent.value = CartGlobalEvent.ProgressLoading(true)
        viewModelScope.launchCatchError(
            context = dispatchers.io,
            block = {
                val data = followShopUseCase(shopId)
                withContext(dispatchers.main) {
                    _followShopEvent.value = FollowShopEvent.Success(data)
                }
            },
            onError = { throwable ->
                withContext(dispatchers.main) {
                    _followShopEvent.value = FollowShopEvent.Failed(throwable)
                }
            }
        )
    }

    override fun onCleared() {
        cartShopGroupTickerJob?.cancel()
        super.onCleared()
    }

    fun useNewPromoPage(): Boolean {
        cartModel.cartListData?.let { data ->
            val lastApply = CartUiModelMapper.mapLastApplySimplified(data.promo.lastApplyPromo.lastApplyPromoData)
            return true
            return lastApply.userGroupPromoAbTest == UserGroupMetadata.PROMO_USER_GROUP_A
                || lastApply.userGroupPromoAbTest == UserGroupMetadata.PROMO_USER_GROUP_B
        }
        return false
    }

    fun getEntryPointInfoFromLastApply(lastApply: LastApplyUiModel) {
        launchCatchError(
            context = dispatchers.main,
            block = {
                _entryPointInfoEvent.postValue(EntryPointInfoEvent.Loading)
                val entryPointEvent = cartPromoEntryPointProcessor
                    .getEntryPointInfoFromLastApply(lastApply, cartModel, cartDataList.value)
                _entryPointInfoEvent.postValue(entryPointEvent)
            },
            onError = {
                Timber.e(it)
                _entryPointInfoEvent.postValue(EntryPointInfoEvent.Error(lastApply))
            }
        )
    }

    fun getEntryPointInfoDefault() {
        launchCatchError(
            context = dispatchers.main,
            block = {
                _entryPointInfoEvent.postValue(EntryPointInfoEvent.Loading)
                cartModel.cartListData?.let { data ->
                    val lastApply = CartUiModelMapper.mapLastApplySimplified(data.promo.lastApplyPromo.lastApplyPromoData)
                    val entryPointEvent = cartPromoEntryPointProcessor
                        .getEntryPointInfoFromLastApply(lastApply, cartModel, cartDataList.value)
                    _entryPointInfoEvent.postValue(entryPointEvent)
                }
            },
            onError = {
                cartModel.cartListData?.let { data ->
                    val lastApply = CartUiModelMapper.mapLastApplySimplified(data.promo.lastApplyPromo.lastApplyPromoData)
                    _entryPointInfoEvent.postValue(EntryPointInfoEvent.Error(lastApply))
                }
            }
        )
    }

    fun getEntryPointInfoNoItemSelected() {
        _entryPointInfoEvent.postValue(
            cartPromoEntryPointProcessor.getEntryPointInfoNoItemSelected()
        )
    }
}
