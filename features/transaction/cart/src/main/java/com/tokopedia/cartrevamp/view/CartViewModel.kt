package com.tokopedia.cartrevamp.view

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.AtcFromExternalSource
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cart.data.model.response.promo.LastApplyPromo
import com.tokopedia.cart.view.analytics.EnhancedECommerceActionFieldData
import com.tokopedia.cartcommon.data.request.updatecart.BundleInfo
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UndoDeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.cartrevamp.data.model.request.UpdateCartWrapperRequest
import com.tokopedia.cartrevamp.data.model.response.promo.LastApplyPromoData
import com.tokopedia.cartrevamp.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.cartrevamp.domain.usecase.AddCartToWishlistUseCase
import com.tokopedia.cartrevamp.domain.usecase.CartShopGroupTickerAggregatorUseCase
import com.tokopedia.cartrevamp.domain.usecase.FollowShopUseCase
import com.tokopedia.cartrevamp.domain.usecase.GetCartParam
import com.tokopedia.cartrevamp.domain.usecase.GetCartRevampV4UseCase
import com.tokopedia.cartrevamp.domain.usecase.SetCartlistCheckboxStateUseCase
import com.tokopedia.cartrevamp.domain.usecase.UpdateAndReloadCartUseCase
import com.tokopedia.cartrevamp.domain.usecase.UpdateCartAndGetLastApplyUseCase
import com.tokopedia.cartrevamp.view.mapper.CartUiModelMapper
import com.tokopedia.cartrevamp.view.mapper.PromoRequestMapper
import com.tokopedia.cartrevamp.view.uimodel.AddToCartEvent
import com.tokopedia.cartrevamp.view.uimodel.CartCheckoutButtonState
import com.tokopedia.cartrevamp.view.uimodel.CartEmptyHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartGlobalEvent
import com.tokopedia.cartrevamp.view.uimodel.CartGroupHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartItemHolderData
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
import com.tokopedia.cartrevamp.view.uimodel.DisabledAccordionHolderData
import com.tokopedia.cartrevamp.view.uimodel.DisabledCollapsedHolderData
import com.tokopedia.cartrevamp.view.uimodel.DisabledItemHeaderHolderData
import com.tokopedia.cartrevamp.view.uimodel.DisabledReasonHolderData
import com.tokopedia.cartrevamp.view.uimodel.LoadRecentReviewState
import com.tokopedia.cartrevamp.view.uimodel.LoadRecommendationState
import com.tokopedia.cartrevamp.view.uimodel.LoadWishlistV2State
import com.tokopedia.cartrevamp.view.uimodel.PromoSummaryDetailData
import com.tokopedia.cartrevamp.view.uimodel.UpdateCartAndGetLastApplyState
import com.tokopedia.cartrevamp.view.uimodel.UpdateCartCheckoutState
import com.tokopedia.cartrevamp.view.uimodel.UpdateCartPromoState
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.toZeroStringIfNullOrBlank
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.network.exception.MessageErrorException
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
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
    private val addCartToWishlistUseCase: AddCartToWishlistUseCase,
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
    private val schedulers: ExecutorSchedulers,
    private val dispatchers: CoroutineDispatchers
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + dispatchers.immediate
    
    var cartModel: CartModel = CartModel()
        private set

    val cartDataList: CartMutableLiveData<ArrayList<Any>> =
        CartMutableLiveData(arrayListOf())

    val globalEvent: CartMutableLiveData<CartGlobalEvent> =
        CartMutableLiveData(CartGlobalEvent.Normal)
    val loadCartState: CartMutableLiveData<CartState<CartData>?> = CartMutableLiveData(null)

    val updateCartForCheckoutState: CartMutableLiveData<UpdateCartCheckoutState> =
        CartMutableLiveData(UpdateCartCheckoutState.None)
    val updateCartForPromoState: CartMutableLiveData<UpdateCartPromoState> =
        CartMutableLiveData(UpdateCartPromoState.None)

    val cartCheckoutButtonState: CartMutableLiveData<CartCheckoutButtonState?> =
        CartMutableLiveData(null)
    val recentViewState: CartMutableLiveData<LoadRecentReviewState?> = CartMutableLiveData(null)
    val wishlistV2State: CartMutableLiveData<LoadWishlistV2State?> = CartMutableLiveData(null)
    val recommendationState: CartMutableLiveData<LoadRecommendationState?> =
        CartMutableLiveData(null)
    val updateCartAndGetLastApplyState: CartMutableLiveData<UpdateCartAndGetLastApplyState?> = CartMutableLiveData(null)
    val selectedAmountState: CartMutableLiveData<Int> = CartMutableLiveData(0)
    val cartTrackerEvent: CartMutableLiveData<CartTrackerEvent?> = CartMutableLiveData(null)
    val addToCartEvent: CartMutableLiveData<AddToCartEvent?> = CartMutableLiveData(null)

    companion object {
        private const val PERCENTAGE = 100.0f
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

        private const val STATUS_OK = "OK"
    }

    fun dataHasChanged(): Boolean {
        var hasChanges = false
        getAllCartItemData().let {
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

    fun getAllShopGroupDataList(): List<CartGroupHolderData> {
        val shopGroupList = mutableListOf<CartGroupHolderData>()
        loop@ for (data in cartDataList.value) {
            when (data) {
                is CartGroupHolderData -> {
                    shopGroupList.add(data)
                }

                hasReachAllShopItems(data) -> break@loop
            }
        }
        return shopGroupList
    }

    fun getAllCartItemData(): List<CartItemHolderData> {
        val cartItemDataList = ArrayList<CartItemHolderData>()
        loop@ for (data in cartDataList.value) {
            when (data) {
                is CartGroupHolderData -> {
                    val cartItemHolderDataList = data.productUiModelList
                    for (cartItemHolderData in cartItemHolderDataList) {
                        cartItemHolderData.shopBoMetadata = data.boMetadata
                        cartItemHolderData.shopCartShopGroupTickerData = data.cartShopGroupTicker
                        cartItemDataList.add(cartItemHolderData)
                    }
                }

                hasReachAllShopItems(data) -> break@loop
            }
        }

        if (cartModel.tmpAllUnavailableShop?.isNotEmpty() == true) {
            cartItemDataList.addAll(getCollapsedUnavailableCartItemData())
        }

        return cartItemDataList
    }

    fun getAllAvailableCartItemData(): List<CartItemHolderData> {
        val cartItemDataList = ArrayList<CartItemHolderData>()
        loop@ for (data in cartDataList.value) {
            when (data) {
                is CartGroupHolderData -> {
                    if (!data.isError) {
                        val cartItemHolderDataList = data.productUiModelList
                        for (cartItemHolderData in cartItemHolderDataList) {
                            cartItemHolderData.shopBoMetadata = data.boMetadata
                            cartItemHolderData.shopCartShopGroupTickerData =
                                data.cartShopGroupTicker
                            cartItemDataList.add(cartItemHolderData)
                        }
                    }
                }

                hasReachAllShopItems(data) -> break@loop
            }
        }

        return cartItemDataList
    }

    fun getAllAvailableCartItemHolderData(): List<CartItemHolderData> {
        val cartItemDataList = ArrayList<CartItemHolderData>()
        loop@ for (data in cartDataList.value) {
            when (data) {
                is CartGroupHolderData -> {
                    data.productUiModelList.let {
                        cartItemDataList.addAll(it)
                    }
                }

                hasReachAllShopItems(data) -> break@loop
            }
        }

        return cartItemDataList
    }

    fun getSelectedAvailableCartItemData(): List<CartItemHolderData> {
        val cartItemDataList = ArrayList<CartItemHolderData>()
        loop@ for (data in cartDataList.value) {
            when (data) {
                is CartGroupHolderData -> {
                    if (!data.isError) {
                        for (cartItemHolderData in data.productUiModelList) {
                            if (cartItemHolderData.isSelected) {
                                cartItemDataList.add(cartItemHolderData)
                            }
                        }
                    }
                }

                hasReachAllShopItems(data) -> break@loop
            }
        }

        return cartItemDataList
    }

    fun getSelectedCartGroupHolderData(): List<CartGroupHolderData> {
        val cartGroupHolderDataList = ArrayList<CartGroupHolderData>()
        loop@ for (data in cartDataList.value) {
            when (data) {
                is CartGroupHolderData -> {
                    if (data.isPartialSelected || data.isAllSelected) {
                        cartGroupHolderDataList.add(data)
                    }
                }

                hasReachAllShopItems(data) -> break@loop
            }
        }

        return cartGroupHolderDataList
    }

    fun getDisabledAccordionHolderData(): DisabledAccordionHolderData? {
        cartDataList.value.forEach {
            if (it is DisabledAccordionHolderData) {
                return it
            }
        }

        return null
    }

    fun getSelectedCartItemData(): List<CartItemHolderData> {
        val cartItemDataList = ArrayList<CartItemHolderData>()
        loop@ for (data in cartDataList.value) {
            when (data) {
                is CartGroupHolderData -> {
                    if ((data.isPartialSelected || data.isAllSelected)) {
                        for (cartItemHolderData in data.productUiModelList) {
                            if (cartItemHolderData.isSelected && !cartItemHolderData.isError) {
                                cartItemHolderData.shopBoMetadata = data.boMetadata
                                cartItemHolderData.shopCartShopGroupTickerData =
                                    data.cartShopGroupTicker
                                cartItemDataList.add(cartItemHolderData)
                            }
                        }
                    }
                }

                hasReachAllShopItems(data) -> break@loop
            }
        }

        return cartItemDataList
    }

    fun getAllAvailableShopGroupDataList(): List<CartGroupHolderData> {
        val availableShopGroupList = mutableListOf<CartGroupHolderData>()
        loop@ for (data in cartDataList.value) {
            when (data) {
                is CartGroupHolderData -> {
                    if (!data.isError) {
                        availableShopGroupList.add(data)
                    }
                }

                hasReachAllShopItems(data) -> break@loop
            }
        }
        return availableShopGroupList
    }

    fun getAllCartItemProductId(): List<String> {
        val productIdList = ArrayList<String>()
        loop@ for (data in cartDataList.value) {
            when (data) {
                is CartGroupHolderData -> {
                    data.productUiModelList.let {
                        for (cartItemHolderData in it) {
                            productIdList.add(cartItemHolderData.productId)
                        }
                    }
                }

                hasReachAllShopItems(data) -> break@loop
            }
        }

        return productIdList
    }

    private fun getCollapsedUnavailableCartItemData(): List<CartItemHolderData> {
        val cartItemDataList = mutableListOf<CartItemHolderData>()
        cartModel.tmpAllUnavailableShop?.let {
            loop@ for (data in it) {
                when (data) {
                    is CartGroupHolderData -> {
                        cartItemDataList.addAll(data.productUiModelList)
                    }
                }
            }
        }
        return cartItemDataList
    }

    fun getCartWishlistHolderData(): CartWishlistHolderData {
        cartDataList.value.forEach {
            if (it is CartWishlistHolderData) {
                return it
            }
        }

        return CartWishlistHolderData()
    }

    fun getDisabledItemHeaderPosition(): Int {
        for (i in cartDataList.value.indices) {
            if (cartDataList.value[i] is DisabledItemHeaderHolderData) {
                return i
            }
        }

        return 0
    }

    fun hasSelectedCartItem(): Boolean {
        cartDataList.value.forEach {
            when (it) {
                is CartGroupHolderData -> {
                    it.productUiModelList.forEach {
                        if (it.isSelected) return true
                    }
                }

                is DisabledItemHeaderHolderData, is CartSectionHeaderHolderData -> {
                    return false
                }
            }
        }

        return false
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
                        globalEvent.value = CartGlobalEvent.AdapterItemChanged(index)
                    } else {
                        return@forEachIndexed
                    }
                }

                is CartItemHolderData -> {
                    if (!data.isError) {
                        if (data.isSelected != checked) {
                            data.isSelected = checked
                            globalEvent.value = CartGlobalEvent.AdapterItemChanged(index)
                        }
                    } else {
                        return@forEachIndexed
                    }
                }

                is CartShopBottomHolderData -> {
                    data.shopData.cartShopGroupTicker.state = CartShopGroupTickerState.FIRST_LOAD
                    globalEvent.value = CartGlobalEvent.AdapterItemChanged(index)
                }

                is DisabledItemHeaderHolderData, is CartSectionHeaderHolderData -> {
                    return@forEachIndexed
                }
            }
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
        launch(dispatchers.io) {
            val cartItemDataList = ArrayList<CartItemHolderData>()
            for (data in getAllAvailableCartItemData()) {
                if (!data.isError) {
                    cartItemDataList.add(data)
                }
            }

            val updateCartRequestList = getUpdateCartRequest(cartItemDataList)
            if (updateCartRequestList.isNotEmpty()) {
                try {
                    val updateCartWrapperRequest = UpdateCartWrapperRequest(
                        updateCartRequestList = updateCartRequestList,
                        source = UpdateCartAndGetLastApplyUseCase.PARAM_VALUE_SOURCE_UPDATE_QTY_NOTES,
                        cartId = cartId,
                        getCartState = getCartState
                    )
                    val updateAndReloadCartListData =
                        updateAndReloadCartUseCase(updateCartWrapperRequest)
                    globalEvent.value = CartGlobalEvent.ProgressLoading(false)
                    processInitialGetCartData(
                        updateAndReloadCartListData.cartId,
                        initialLoad = false,
                        isLoadingTypeRefresh = true,
                        updateAndReloadCartListData.getCartState
                    )
                } catch (t: Throwable) {
                    globalEvent.value = CartGlobalEvent.UpdateAndReloadCartFailed(t)
                }
            } else {
                globalEvent.value = CartGlobalEvent.ProgressLoading(false)
            }
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
                    if (cartItemHolderData.isBundlingItem) {
                        quantity = cartItemHolderData.quantity * cartItemHolderData.bundleQuantity
                    } else {
                        quantity = cartItemHolderData.quantity
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
            globalEvent.value = CartGlobalEvent.LoadGetCartData
        } else if (!isLoadingTypeRefresh) {
            globalEvent.value = CartGlobalEvent.ProgressLoading(true)
        }

        launch {
            try {
                val cartData = getCartRevampV4UseCase(GetCartParam(cartId, getCartState))
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
            showChoosePromoWidget = cartData.promo.showChoosePromoWidget,
            promoTicker = cartData.promo.ticker,
            recommendationPage = RECOMMENDATION_START_PAGE
        )
        if (!initialLoad) {
            globalEvent.value = CartGlobalEvent.ProgressLoading(false)
        }

        loadCartState.value = CartState.Success(cartData)
        CartIdlingResource.decrement()
    }

    private fun onErrorGetCartList(throwable: Throwable, initialLoad: Boolean) {
        Timber.e(throwable)
        if (!initialLoad) {
            globalEvent.value = CartGlobalEvent.ProgressLoading(false)
        }
        loadCartState.value = CartState.Failed(throwable)
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
                    globalEvent.value = CartGlobalEvent.CartCounterUpdated(it)
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

            cartDataList.value.removeAll(tmpAllUnavailableShop)
            cartDataList.value.add(startIdxDisabledReason, disabledCollapsedData)
            cartDataList.notifyObserver()
//            val firstUnavailableShop = tmpAllUnavailableShop.firstOrNull()
//            firstUnavailableShop?.let { shop ->
//                if (shop is CartGroupHolderData) {
//                    val tmpProducts = getNonCollapsibleUnavailableProduct(shop)
//                    val cartShopHolderData = shop.deepCopy()
//                    cartShopHolderData.productUiModelList.clear()
//                    cartShopHolderData.productUiModelList.addAll(tmpProducts)
//
//                    tmpCollapsedUnavailableShop.addAll(tmpProducts)
//                    cartModel.tmpCollapsedUnavailableShop = tmpCollapsedUnavailableShop
//
//                    cartDataList.value.removeAll(tmpAllUnavailableShop)
//                    cartDataList.value.addAll(firstIndex, tmpCollapsedUnavailableShop)
//                    cartDataList.notifyObserver()
//                    notifyItemRangeChanged(firstIndex, tmpCollapsedUnavailableShop.size)
//                    notifyItemRangeRemoved(
//                        firstIndex + tmpCollapsedUnavailableShop.size,
//                        tmpAllUnavailableShop.size - tmpCollapsedUnavailableShop.size
//                    )
//                }
//            }
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
//                        notifyItemRangeChanged(firstIndex, existingSize)
//                        notifyItemRangeInserted(firstIndex + existingSize, newSize - existingSize)
                }
            }
        }
    }

    private fun getNonCollapsibleUnavailableProduct(shop: CartGroupHolderData): List<CartItemHolderData> {
        val nonCollapsibleProducts = mutableListOf<CartItemHolderData>()
        var previousBundlingId = ""
        var previousBundlingGroupId = ""
        loop@ for (product in shop.productUiModelList) {
            if (product.isBundlingItem) {
                if (nonCollapsibleProducts.isNotEmpty() && nonCollapsibleProducts.firstOrNull()?.isBundlingItem == false) {
                    break@loop
                } else {
                    if (previousBundlingId.isBlank() && previousBundlingGroupId.isBlank()) {
                        previousBundlingId = product.bundleId
                        previousBundlingGroupId = product.bundleGroupId
                    }
                    if (product.bundleId == previousBundlingId && product.bundleGroupId == previousBundlingGroupId) {
                        nonCollapsibleProducts.add(product)
                    }
                }
            } else {
                if (nonCollapsibleProducts.isEmpty()) {
                    nonCollapsibleProducts.add(product)
                } else {
                    break@loop
                }
            }
        }

        return nonCollapsibleProducts
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

    private fun calculatePriceMarketplaceProduct(allCartItemDataList: ArrayList<CartItemHolderData>): Triple<Int, Pair<Double, Double>, Double> {
        var totalItemQty = 0
        var subtotalBeforeSlashedPrice = 0.0
        var subtotalPrice = 0.0
        var subtotalCashback = 0.0

        val subtotalWholesaleBeforeSlashedPriceMap = HashMap<String, Double>()
        val subtotalWholesalePriceMap = HashMap<String, Double>()
        val subtotalWholesaleCashbackMap = HashMap<String, Double>()
        val cartItemParentIdMap = HashMap<String, CartItemHolderData>()
        val calculatedBundlingGroupId = HashSet<String>()

        for (cartItemHolderData in allCartItemDataList) {
            var itemQty =
                if (cartItemHolderData.isBundlingItem) {
                    cartItemHolderData.bundleQuantity * cartItemHolderData.quantity
                } else {
                    cartItemHolderData.quantity
                }
            totalItemQty += itemQty
            if (cartItemHolderData.parentId.isNotBlank() && cartItemHolderData.parentId.isNotBlank() && cartItemHolderData.parentId != "0") {
                for (cartItemHolderDataTmp in allCartItemDataList) {
                    if (cartItemHolderData.productId != cartItemHolderDataTmp.productId &&
                        cartItemHolderData.parentId == cartItemHolderDataTmp.parentId &&
                        cartItemHolderData.productPrice == cartItemHolderDataTmp.productPrice
                    ) {
                        val tmpQty =
                            if (cartItemHolderDataTmp.isBundlingItem) {
                                cartItemHolderDataTmp.bundleQuantity * cartItemHolderDataTmp.quantity
                            } else {
                                cartItemHolderDataTmp.quantity
                            }
                        itemQty += tmpQty
                    }
                }
            }

            if (cartItemHolderData.isBundlingItem) {
                if (!calculatedBundlingGroupId.contains(cartItemHolderData.bundleGroupId)) {
                    subtotalPrice += cartItemHolderData.bundleQuantity * cartItemHolderData.bundlePrice
                    subtotalBeforeSlashedPrice += cartItemHolderData.bundleQuantity * cartItemHolderData.bundleOriginalPrice
                    calculatedBundlingGroupId.add(cartItemHolderData.bundleGroupId)
                }
            } else if (cartItemHolderData.wholesalePriceData.isNotEmpty()) {
                // Calculate price and cashback for wholesale marketplace product
                val returnValueWholesaleProduct =
                    calculatePriceWholesaleProduct(cartItemHolderData, itemQty)

                if (!subtotalWholesaleBeforeSlashedPriceMap.containsKey(cartItemHolderData.parentId)) {
                    subtotalWholesaleBeforeSlashedPriceMap[cartItemHolderData.parentId] =
                        returnValueWholesaleProduct.first
                }
                if (!subtotalWholesalePriceMap.containsKey(cartItemHolderData.parentId)) {
                    subtotalWholesalePriceMap[cartItemHolderData.parentId] =
                        returnValueWholesaleProduct.second
                }
                if (!subtotalWholesaleCashbackMap.containsKey(cartItemHolderData.parentId)) {
                    subtotalWholesaleCashbackMap[cartItemHolderData.parentId] =
                        returnValueWholesaleProduct.third
                }
            } else {
                // Calculate price and cashback for normal marketplace product
                val returnValueNormalProduct = calculatePriceNormalProduct(
                    cartItemHolderData,
                    itemQty,
                    cartItemParentIdMap,
                    subtotalBeforeSlashedPrice,
                    subtotalPrice,
                    subtotalCashback
                )
                subtotalBeforeSlashedPrice = returnValueNormalProduct.first
                subtotalPrice = returnValueNormalProduct.second
                subtotalCashback = returnValueNormalProduct.third
            }

            if (cartItemHolderData.addOnsProduct.listData.isNotEmpty()) {
                cartModel = cartModel.copy(totalQtyWithAddon = totalItemQty)
                cartItemHolderData.addOnsProduct.listData.forEach {
                    subtotalPrice += (cartModel.totalQtyWithAddon * it.price)
                }
            }
        }

        if (subtotalWholesaleBeforeSlashedPriceMap.isNotEmpty()) {
            for ((_, value) in subtotalWholesaleBeforeSlashedPriceMap) {
                subtotalBeforeSlashedPrice += value
            }
        }

        if (subtotalWholesalePriceMap.isNotEmpty()) {
            for ((_, value) in subtotalWholesalePriceMap) {
                subtotalPrice += value
            }
        }

        if (subtotalWholesaleCashbackMap.isNotEmpty()) {
            for ((_, value) in subtotalWholesaleCashbackMap) {
                subtotalCashback += value
            }
        }

        val pricePair = Pair(subtotalBeforeSlashedPrice, subtotalPrice)
        return Triple(totalItemQty, pricePair, subtotalCashback)
    }

    private fun calculatePriceNormalProduct(
        cartItemHolderData: CartItemHolderData,
        itemQty: Int,
        cartItemParentIdMap: HashMap<String, CartItemHolderData>,
        subtotalBeforeSlashedPrice: Double,
        subtotalPrice: Double,
        subtotalCashback: Double
    ): Triple<Double, Double, Double> {
        var tmpSubtotalBeforeSlashedPrice = subtotalBeforeSlashedPrice
        var tmpSubTotalPrice = subtotalPrice
        var tmpSubtotalCashback = subtotalCashback

        val parentIdPriceIndex =
            cartItemHolderData.parentId + cartItemHolderData.productPrice.toString()
        if (!cartItemParentIdMap.containsKey(parentIdPriceIndex)) {
            val itemPrice = itemQty * cartItemHolderData.productPrice
            if (cartItemHolderData.productCashBack.isNotBlank()) {
                val cashbackPercentageString = cartItemHolderData.productCashBack
                    .replace(" ", "")
                    .replace("%", "")
                val cashbackPercentage = cashbackPercentageString.toDouble()
                val itemCashback = cashbackPercentage / PERCENTAGE * itemPrice
                tmpSubtotalCashback += itemCashback
            }

            if (cartItemHolderData.productOriginalPrice > 0) {
                tmpSubtotalBeforeSlashedPrice += (itemQty * cartItemHolderData.productOriginalPrice)
            } else {
                tmpSubtotalBeforeSlashedPrice += itemPrice
            }

            tmpSubTotalPrice += itemPrice
            cartItemHolderData.wholesalePriceFormatted = null
            cartItemParentIdMap[parentIdPriceIndex] = cartItemHolderData
        }

        return Triple(tmpSubtotalBeforeSlashedPrice, tmpSubTotalPrice, tmpSubtotalCashback)
    }

    private fun calculatePriceWholesaleProduct(
        cartItemHolderData: CartItemHolderData,
        itemQty: Int
    ): Triple<Double, Double, Double> {
        var subtotalBeforeSlashedPrice = 0.0
        var subTotalWholesalePrice = 0.0
        var subtotalWholesaleCashback = 0.0

        var hasCalculateWholesalePrice = false
        val wholesalePriceDataList = cartItemHolderData.wholesalePriceData

        for (wholesalePriceData in wholesalePriceDataList) {
            if (itemQty >= wholesalePriceData.qtyMin) {
                subTotalWholesalePrice = (itemQty * wholesalePriceData.prdPrc)
                hasCalculateWholesalePrice = true
                val wholesalePriceFormatted = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    wholesalePriceData.prdPrc,
                    false
                ).removeDecimalSuffix()
                cartItemHolderData.wholesalePriceFormatted = wholesalePriceFormatted
                cartItemHolderData.wholesalePrice = wholesalePriceData.prdPrc
                subtotalBeforeSlashedPrice = itemQty * cartItemHolderData.wholesalePrice
                break
            }
        }

        if (!hasCalculateWholesalePrice) {
            subTotalWholesalePrice = (itemQty * cartItemHolderData.productPrice)
            cartItemHolderData.wholesalePriceFormatted = null
            cartItemHolderData.wholesalePrice = 0.0
            subtotalBeforeSlashedPrice =
                if (cartItemHolderData.productOriginalPrice > 0) {
                    (itemQty * cartItemHolderData.productOriginalPrice)
                } else {
                    (itemQty * cartItemHolderData.productPrice)
                }
        }

        if (cartItemHolderData.productCashBack.isNotBlank()) {
            val cashbackPercentageString = cartItemHolderData.productCashBack
                .replace(" ", "")
                .replace("%", "")
            val cashbackPercentage = cashbackPercentageString.toDouble()
            subtotalWholesaleCashback = cashbackPercentage / PERCENTAGE * subTotalWholesalePrice
        }

        return Triple(subtotalBeforeSlashedPrice, subTotalWholesalePrice, subtotalWholesaleCashback)
    }

    fun reCalculateSubTotal(dataList: List<CartGroupHolderData>) {
        var totalItemQty = 0
        var subtotalBeforeSlashedPrice = 0.0
        var subtotalPrice = 0.0
        var subtotalCashback = 0.0

        // Collect all Cart Item & also calculate total weight on each shop
        val cartItemDataList = getAvailableCartItemDataList(dataList)

        // Calculate total total item, price and cashback for marketplace product
        val returnValueMarketplaceProduct = calculatePriceMarketplaceProduct(cartItemDataList)
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

        globalEvent.value = CartGlobalEvent.SubTotalUpdated(
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
        globalEvent.value = CartGlobalEvent.ItemLoading(false)
        launch {
            try {
                clearCacheAutoApplyStackUseCase.setParams(clearPromoRequest).executeOnBackground()
                globalEvent.value = CartGlobalEvent.SuccessClearRedPromosThenGoToCheckout
            } catch (t: Throwable) {
                Timber.d(t)
                globalEvent.value = CartGlobalEvent.SuccessClearRedPromosThenGoToCheckout
            }
        }
    }

    fun doClearRedPromosBeforeGoToPromo(clearPromoRequest: ClearPromoRequest) {
        globalEvent.value = CartGlobalEvent.ItemLoading(true)
        launch {
            try {
                clearCacheAutoApplyStackUseCase.setParams(clearPromoRequest).executeOnBackground()
                globalEvent.value = CartGlobalEvent.SuccessClearRedPromosThenGoToPromo
            } catch (t: Throwable) {
                Timber.d(t)
                globalEvent.value = CartGlobalEvent.SuccessClearRedPromosThenGoToPromo
            }
        }
    }

    fun processUpdateCartData(fireAndForget: Boolean, onlyTokoNowProducts: Boolean = false) {
        if (!fireAndForget) {
            globalEvent.value = CartGlobalEvent.ProgressLoading(true)
            CartIdlingResource.increment()
        }

        val cartItemDataList: List<CartItemHolderData> = if (fireAndForget) {
            // Update cart to save state
            getAllAvailableCartItemData()
        } else {
            // Update cart to go to shipment
            getSelectedCartItemData()
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
                globalEvent.value = CartGlobalEvent.ProgressLoading(false)
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
        globalEvent.value = CartGlobalEvent.ProgressLoading(false)
        if (updateCartV2Data.data.status) {
            val checklistCondition = getChecklistCondition()
            updateCartForCheckoutState.value = UpdateCartCheckoutState.Success(
                generateCheckoutDataAnalytics(
                    cartItemDataList,
                    EnhancedECommerceActionField.STEP_1
                ),
                isCheckoutProductEligibleForCashOnDelivery(cartItemDataList),
                checklistCondition
            )
        } else {
            if (updateCartV2Data.data.outOfService.isOutOfService()) {
                updateCartForCheckoutState.value = UpdateCartCheckoutState.ErrorOutOfService(
                    updateCartV2Data.data.outOfService
                )
            } else {
                updateCartForCheckoutState.value = UpdateCartCheckoutState.UnknownError(
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
        val cartShopHolderDataList = getAllShopGroupDataList()

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
        updateCartForCheckoutState.value = UpdateCartCheckoutState.Failed(throwable)
        CartLogger.logOnErrorUpdateCartForCheckout(throwable, cartItemDataList)
    }

    fun doUpdateCartForPromo() {
        globalEvent.value = CartGlobalEvent.ProgressLoading(true)

        val updateCartRequestList = getUpdateCartRequest(getSelectedCartItemData())
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
            globalEvent.value = CartGlobalEvent.ProgressLoading(false)
        }
    }

    private fun onErrorUpdateCartForPromo(throwable: Throwable) {
        updateCartForPromoState.value = UpdateCartPromoState.Failed(throwable)
    }

    private fun onSuccessUpdateCartForPromo() {
        updateCartForPromoState.value = UpdateCartPromoState.Success
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
        // TODO: check assign by reference
    }

    fun checkForShipmentForm() {
        var hasCheckedAvailableItem = false
        loop@ for (any in cartDataList.value) {
            if (hasCheckedAvailableItem) break@loop
            if (any is CartGroupHolderData) {
                if (any.isAllSelected) {
                    hasCheckedAvailableItem = true
                } else if (any.isPartialSelected) {
                    any.productUiModelList.let {
                        innerLoop@ for (cartItemHolderData in it) {
                            if (cartItemHolderData.isSelected) {
                                hasCheckedAvailableItem = true
                                break@innerLoop
                            }
                        }
                    }
                }
            }
        }

        if (hasCheckedAvailableItem) {
            cartCheckoutButtonState.value = CartCheckoutButtonState.ENABLE
        } else {
            cartCheckoutButtonState.value = CartCheckoutButtonState.DISABLE
        }
    }

    fun processGetRecentViewData(allProductIds: List<String>) {
        globalEvent.value = CartGlobalEvent.ItemLoading(true)
        launch {
            try {
                val recommendationWidgets = getRecentViewUseCase.getData(
                    GetRecommendationRequestParam(
                        pageNumber = 1,
                        xSource = RECENT_VIEW_XSOURCE,
                        pageName = PAGE_NAME_RECENT_VIEW,
                        productIds = allProductIds,
                        queryParam = ""
                    )
                )
                recentViewState.value = LoadRecentReviewState.Success(recommendationWidgets)
            } catch (t: Throwable) {
                Timber.d(t)
                recentViewState.value = LoadRecentReviewState.Failed(t)
            }
        }
    }

    fun updateCartModel(newCartModel: CartModel) {
        this.cartModel = newCartModel
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
        // TODO: notify
//        notifyDataSetChanged()
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

        launch(dispatchers.main) {
            getWishlistV2UseCase.setParams(requestParams)
            val result = withContext(dispatchers.io) { getWishlistV2UseCase.executeOnBackground() }
            if (result is Success) {
                wishlistV2State.value = LoadWishlistV2State.Success(
                    result.data.items,
                    true
                )
            } else {
                val error = (result as Fail).throwable
                Timber.d(error)
                wishlistV2State.value = LoadWishlistV2State.Failed
            }
        }
    }

    fun setLocalizingAddressData(lca: LocalCacheModel?) {
        cartModel = cartModel.copy(
            lca = lca
        )
    }

    fun saveCheckboxState(cartItemDataList: List<CartItemHolderData>) {
        launchCatchError(dispatchers.io, block = {
            setCartlistCheckboxStateUseCase(cartItemDataList)
        }, onError = {})
    }

    fun processGetRecommendationData(page: Int, allProductIds: List<String>) {
        globalEvent.value = CartGlobalEvent.ItemLoading(true)
        launch {
            try {
                val recommendationWidgets = getRecommendationUseCase.getData(
                    GetRecommendationRequestParam(
                        pageNumber = page,
                        xSource = "recom_widget",
                        pageName = "cart",
                        productIds = allProductIds,
                        queryParam = ""
                    )
                )
                recommendationState.value = LoadRecommendationState.Success(recommendationWidgets)
            } catch (t: Throwable) {
                Timber.d(t)
                recommendationState.value = LoadRecommendationState.Failed
            }
        }
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
//            notifyItemRangeInserted(recommendationIndex, cartRecommendationItemHolderDataList.size)
        // TODO: notify
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
            // TODO: notify
        }
    }

    fun resetData() {
        cartModel = cartModel.copy(firstCartSectionHeaderPosition = -1)
        cartDataList.value = arrayListOf()
        cartDataList.notifyObserver()
        // TODO: notify
        checkForShipmentForm()
    }

    fun addCartLoadingData() {
        if (cartModel.cartLoadingHolderData == null) {
            cartModel.cartLoadingHolderData = CartLoadingHolderData()
        }
        cartModel.cartLoadingHolderData?.let {
            cartDataList.value.add(it)
            cartDataList.notifyObserver()
//            notifyItemInserted(cartDataList.indexOf(it))
            // TODO: notify
        }
    }

    fun removeCartLoadingData() {
        cartModel.cartLoadingHolderData?.let {
            val index = cartDataList.value.indexOf(it)
            if (index != -1) {
                cartDataList.value.remove(it)
                cartDataList.notifyObserver()
//                notifyItemRemoved(index)
                // TODO: notify
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
        var listName: String
        if (isCartEmpty) {
            listName = EnhancedECommerceActionField.LIST_CART_RECOMMENDATION_ON_EMPTY_CART
        } else {
            listName = EnhancedECommerceActionField.LIST_CART_RECOMMENDATION
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
                val lastApplyPromo = cartModel.cartListData?.promo?.lastApplyPromo ?: LastApplyPromo()
                PromoRequestMapper.generateGetLastApplyRequestParams(
                    lastApplyPromo,
                    getSelectedCartGroupHolderData(),
                    null
                )
            }

            cartModel.lastValidateUseResponse != null -> {
                val promoUiModel = cartModel.lastValidateUseResponse?.promoUiModel ?: PromoUiModel()
                PromoRequestMapper.generateGetLastApplyRequestParams(
                    promoUiModel,
                    getSelectedCartGroupHolderData(),
                    cartModel.lastValidateUseRequest
                )
            }

            else -> {
                PromoRequestMapper.generateGetLastApplyRequestParams(
                    null,
                    getSelectedCartGroupHolderData(),
                    null
                )
            }
        }
    }

    fun doUpdateCartAndGetLastApply(promoRequest: ValidateUsePromoRequest) {
        val cartItemDataList = ArrayList<CartItemHolderData>()
        getSelectedCartItemData().let { listCartItemData ->
            for (data in listCartItemData) {
                if (!data.isError) {
                    cartItemDataList.add(data)
                }
            }
        }

        val updateCartRequestList = getUpdateCartRequest(cartItemDataList)
        if (updateCartRequestList.isNotEmpty()) {
            launch(dispatchers.io) {
                try {
                    cartModel.lastValidateUseRequest = promoRequest
                    val updateCartWrapperRequest =
                        UpdateCartWrapperRequest(
                            updateCartRequestList = updateCartRequestList,
                            source = UpdateCartAndGetLastApplyUseCase.PARAM_VALUE_SOURCE_UPDATE_QTY_NOTES,
                            getLastApplyPromoRequest = promoRequest
                        )
                    val updateCartDataResponse = updateCartAndGetLastApplyUseCase(updateCartWrapperRequest)
                    withContext(dispatchers.main) {
                        updateCartDataResponse.updateCartData?.let { updateCartData ->
                            if (updateCartData.isSuccess) {
                                updateCartDataResponse.promoUiModel?.let { promoUiModel ->
                                    syncCartGroupShopBoCodeWithPromoUiModel(promoUiModel)
                                    cartModel.apply {
                                        isLastApplyResponseStillValid = false
                                        cartModel.lastValidateUseResponse = ValidateUsePromoRevampUiModel(
                                            promoUiModel = promoUiModel
                                        )
                                        lastUpdateCartAndGetLastApplyResponse = updateCartDataResponse
                                    }
                                    updateCartAndGetLastApplyState.value = UpdateCartAndGetLastApplyState.Success(promoUiModel)
                                }
                            }
                        }
                    }
                } catch (t: Throwable) {
                    updateCartAndGetLastApplyState.value = UpdateCartAndGetLastApplyState.Failed(t)
                }
            }
        } else {
            globalEvent.value = CartGlobalEvent.ProgressLoading(false)
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
            launch {
                try {
                    clearCacheAutoApplyStackUseCase.setParams(param).executeOnBackground()
                } catch (t: Throwable) {
                    Timber.d(t)
                }
            }
            cartModel.isLastApplyResponseStillValid = false
            cartModel.lastValidateUseResponse = ValidateUsePromoRevampUiModel()
        }
    }

    private fun syncCartGroupShopBoCodeWithPromoUiModel(promoUiModel: PromoUiModel) {
        val groupDataList = getAllShopGroupDataList()
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
        globalEvent.value = CartGlobalEvent.ProgressLoading(true)

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
                cartTrackerEvent.value = CartTrackerEvent.ATCTrackingURLRecent(productModel)
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
                cartTrackerEvent.value = CartTrackerEvent.ATCTrackingURLRecommendation(recommendationItem)
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
                cartTrackerEvent.value = CartTrackerEvent.ATCTrackingURLBanner(productModel)
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

        launch {
            try {
                addToCartUseCase.setParams(addToCartRequestParams)
                val addToCartDataModel = addToCartUseCase.executeOnBackground()
                globalEvent.value = CartGlobalEvent.ProgressLoading(false)
                addToCartEvent.value = AddToCartEvent.Success(addToCartDataModel, productModel)
            } catch (t: Throwable) {
                addToCartEvent.value = AddToCartEvent.Failed(t)
            }
        }
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
        val allSelectedAvailableCartItems = getSelectedAvailableCartItemData()
        val totalSelected = allSelectedAvailableCartItems.count { it.isSelected }
        val selectedAmountHolderData = cartDataList.value.first()
        if (selectedAmountHolderData is CartSelectedAmountHolderData) {
            selectedAmountHolderData.selectedAmount = totalSelected
        }
        selectedAmountState.value = totalSelected
    }
}
