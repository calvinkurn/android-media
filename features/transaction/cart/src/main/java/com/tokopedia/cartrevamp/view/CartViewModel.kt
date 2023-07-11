package com.tokopedia.cartrevamp.view

import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cart.domain.usecase.AddCartToWishlistUseCase
import com.tokopedia.cart.domain.usecase.CartShopGroupTickerAggregatorUseCase
import com.tokopedia.cart.domain.usecase.FollowShopUseCase
import com.tokopedia.cart.domain.usecase.GetCartRevampV4UseCase
import com.tokopedia.cart.domain.usecase.SetCartlistCheckboxStateUseCase
import com.tokopedia.cart.domain.usecase.UpdateAndReloadCartUseCase
import com.tokopedia.cart.domain.usecase.UpdateCartAndGetLastApplyUseCase
import com.tokopedia.cartcommon.data.request.updatecart.BundleInfo
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UndoDeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.cartrevamp.view.uimodel.CartGroupHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartModel
import com.tokopedia.cartrevamp.view.uimodel.CartMutableLiveData
import com.tokopedia.cartrevamp.view.uimodel.CartRecentViewHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartTopAdsHeadlineData
import com.tokopedia.cartrevamp.view.uimodel.CartWishlistHolderData
import com.tokopedia.cartrevamp.view.uimodel.ICartHolder
import com.tokopedia.kotlin.extensions.view.toZeroStringIfNullOrBlank
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.GetWishlistV2UseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import rx.subscriptions.CompositeSubscription
import kotlin.coroutines.CoroutineContext

class CartViewModel(
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

    private var tmpAllUnavailableShop: MutableList<Any>? = null
    var cartModel: CartModel = CartModel()
        private set

    val cartDataList: CartMutableLiveData<ArrayList<ICartHolder>> =
        CartMutableLiveData(arrayListOf())

    fun dataHasChanged(): Boolean {
        var hasChanges = false
        getAllCartDataList().let {
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

    private fun getAllCartDataList(): List<CartItemHolderData> {
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
                is CartRecentViewHolderData,
                is CartWishlistHolderData,
                is CartTopAdsHeadlineData,
                is CartRecommendationItemHolderData -> break@loop
            }
        }

        if (tmpAllUnavailableShop?.isNotEmpty() == true) {
            cartItemDataList.addAll(getCollapsedUnavailableCartItemData())
        }

        return cartItemDataList
    }

    private fun getAllAvailableCartDataList(): List<CartItemHolderData> {
        val cartItemDataList = ArrayList<CartItemHolderData>()
        loop@ for (data in cartDataList.value) {
            when (data) {
                is CartGroupHolderData -> {
                    if (!data.isError) {
                        val cartItemHolderDataList = data.productUiModelList
                        for (cartItemHolderData in cartItemHolderDataList) {
                            cartItemHolderData.shopBoMetadata = data.boMetadata
                            cartItemHolderData.shopCartShopGroupTickerData = data.cartShopGroupTicker
                            cartItemDataList.add(cartItemHolderData)
                        }
                    }
                }
                is CartRecentViewHolderData,
                is CartWishlistHolderData,
                is CartTopAdsHeadlineData,
                is CartRecommendationItemHolderData -> break@loop
            }
        }

        return cartItemDataList
    }

    private fun getCollapsedUnavailableCartItemData(): List<CartItemHolderData> {
        val cartItemDataList = mutableListOf<CartItemHolderData>()
        tmpAllUnavailableShop?.let {
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

    fun processToUpdateAndReloadCartData(cartId: String, getCartState: Int) {
//        launch(dispatchers.io) {
//            val cartItemDataList = ArrayList<CartItemHolderData>()
//            for (data in getAllAvailableCartDataList()) {
//                if (!data.isError) {
//                    cartItemDataList.add(data)
//                }
//            }
//
//            val updateCartRequestList = getUpdateCartRequest(cartItemDataList)
//            if (updateCartRequestList.isNotEmpty()) {
//                try {
//                    val updateCartWrapperRequest = UpdateCartWrapperRequest(
//                        updateCartRequestList = updateCartRequestList,
//                        source = UpdateCartAndGetLastApplyUseCase.PARAM_VALUE_SOURCE_UPDATE_QTY_NOTES,
//                        cartId = cartId,
//                        getCartState = getCartState
//                    )
//                    val updateAndReloadCartListData = updateAndReloadCartUseCase(updateCartWrapperRequest)
//                    view?.hideProgressLoading()
//                    processInitialGetCartData(
//                        updateAndReloadCartListData.cartId,
//                        initialLoad = false,
//                        isLoadingTypeRefresh = true,
//                        updateAndReloadCartListData.getCartState
//                    )
//                } catch (t: Throwable) {
//                    view?.hideProgressLoading()
//                    view?.showToastMessageRed(t)
//                }
//            } else {
//                cartListView.hideProgressLoading()
//            }
//        }
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
        getCartState: Int
    ) {
//        CartIdlingResource.increment()
//        if (initialLoad) {
//            it.renderLoadGetCartData()
//        } else if (!isLoadingTypeRefresh) {
//            it.showProgressLoading()
//        }
//
//        launch {
//            try {
//                val cartData = getCartRevampV4UseCase(GetCartParam(cartId, getCartState))
//                onSuccessGetCartList(cartData, initialLoad)
//            } catch (t: Throwable) {
//                onErrorGetCartList(t, initialLoad)
//            }
//        }
    }

    fun processGetRecommendationData(page: Int, allProductIds: List<String>) {
//        view?.showItemLoading()
//        launch {
//            try {
//                val recommendationWidgets = getRecommendationUseCase.getData(
//                    GetRecommendationRequestParam(
//                        pageNumber = page,
//                        xSource = "recom_widget",
//                        pageName = "cart",
//                        productIds = allProductIds,
//                        queryParam = ""
//                    )
//                )
//                view?.let {
//                    it.hideItemLoading()
//                    if (recommendationWidgets[0].recommendationItemList.isNotEmpty()) {
//                        it.renderRecommendation(recommendationWidgets[0])
//                    }
//                    it.setHasTriedToLoadRecommendation()
//                    it.stopAllCartPerformanceTrace()
//                }
//            } catch (t: Throwable) {
//                Timber.d(t)
//                view?.hideItemLoading()
//                view?.setHasTriedToLoadRecommendation()
//                view?.stopAllCartPerformanceTrace()
//            }
//        }
    }
}
