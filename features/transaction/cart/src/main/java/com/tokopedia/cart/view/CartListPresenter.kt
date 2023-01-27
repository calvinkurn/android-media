package com.tokopedia.cart.view

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.AtcFromExternalSource
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.data.model.request.AddCartToWishlistRequest
import com.tokopedia.cart.data.model.response.promo.CartPromoTicker
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.cart.data.model.request.CartShopGroupTickerAggregatorParam
import com.tokopedia.cart.domain.model.cartlist.SummaryTransactionUiModel
import com.tokopedia.cart.domain.model.updatecart.UpdateAndValidateUseData
import com.tokopedia.cart.domain.usecase.AddCartToWishlistUseCase
import com.tokopedia.cart.domain.usecase.CartShopGroupTickerAggregatorUseCase
import com.tokopedia.cart.domain.usecase.FollowShopUseCase
import com.tokopedia.cart.domain.usecase.GetCartRevampV3UseCase
import com.tokopedia.cart.domain.usecase.SetCartlistCheckboxStateUseCase
import com.tokopedia.cart.domain.usecase.UpdateAndReloadCartUseCase
import com.tokopedia.cart.domain.usecase.UpdateCartAndValidateUseUseCase
import com.tokopedia.cart.view.analytics.EnhancedECommerceActionFieldData
import com.tokopedia.cart.view.analytics.EnhancedECommerceClickData
import com.tokopedia.cart.view.analytics.EnhancedECommerceData
import com.tokopedia.cart.view.analytics.EnhancedECommerceProductData
import com.tokopedia.cart.view.mapper.CartUiModelMapper
import com.tokopedia.cart.view.subscriber.AddCartToWishlistSubscriber
import com.tokopedia.cart.view.subscriber.AddToCartExternalSubscriber
import com.tokopedia.cart.view.subscriber.AddToCartSubscriber
import com.tokopedia.cart.view.subscriber.CartSeamlessLoginSubscriber
import com.tokopedia.cart.view.subscriber.ClearRedPromosBeforeGoToCheckoutSubscriber
import com.tokopedia.cart.view.subscriber.FollowShopSubscriber
import com.tokopedia.cart.view.subscriber.GetRecentViewSubscriber
import com.tokopedia.cart.view.subscriber.GetRecommendationSubscriber
import com.tokopedia.cart.view.subscriber.UpdateAndReloadCartSubscriber
import com.tokopedia.cart.view.subscriber.UpdateCartAndValidateUseSubscriber
import com.tokopedia.cart.view.subscriber.UpdateCartCounterSubscriber
import com.tokopedia.cart.view.subscriber.UpdateCartSubscriber
import com.tokopedia.cart.view.subscriber.ValidateUseSubscriber
import com.tokopedia.cart.view.uimodel.CartBundlingBottomSheetData
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cart.view.uimodel.CartRecentViewItemHolderData
import com.tokopedia.cart.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.cart.view.uimodel.CartShopGroupTickerState
import com.tokopedia.cart.view.uimodel.CartShopHolderData
import com.tokopedia.cart.view.uimodel.CartWishlistItemHolderData
import com.tokopedia.cart.view.uimodel.PromoSummaryData
import com.tokopedia.cart.view.uimodel.PromoSummaryDetailData
import com.tokopedia.cartcommon.data.request.updatecart.BundleInfo
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UndoDeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.toZeroStringIfNullOrBlank
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.logisticcart.shipping.model.Product
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.ShippingParam
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceAdd
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCartMapData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCheckout
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceRecomProductCartMapData
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoOrder
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoOrderData
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
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
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rx.subscriptions.CompositeSubscription
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class CartListPresenter @Inject constructor(
    private val getCartRevampV3UseCase: GetCartRevampV3UseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val undoDeleteCartUseCase: UndoDeleteCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val compositeSubscription: CompositeSubscription,
    private val addToWishlistV2UseCase: AddToWishlistV2UseCase,
    private val addCartToWishlistUseCase: AddCartToWishlistUseCase,
    private val deleteWishlistV2UseCase: DeleteWishlistV2UseCase,
    private val updateAndReloadCartUseCase: UpdateAndReloadCartUseCase,
    private val userSessionInterface: UserSessionInterface,
    private val clearCacheAutoApplyStackUseCase: OldClearCacheAutoApplyStackUseCase,
    private val getRecentViewUseCase: GetRecommendationUseCase,
    private val getWishlistV2UseCase: GetWishlistV2UseCase,
    private val getRecommendationUseCase: GetRecommendationUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val addToCartExternalUseCase: AddToCartExternalUseCase,
    private val seamlessLoginUsecase: SeamlessLoginUsecase,
    private val updateCartCounterUseCase: UpdateCartCounterUseCase,
    private val updateCartAndValidateUseUseCase: UpdateCartAndValidateUseUseCase,
    private val validateUsePromoRevampUseCase: OldValidateUsePromoRevampUseCase,
    private val setCartlistCheckboxStateUseCase: SetCartlistCheckboxStateUseCase,
    private val followShopUseCase: FollowShopUseCase,
    private val cartShopGroupTickerAggregatorUseCase: CartShopGroupTickerAggregatorUseCase,
    private val schedulers: ExecutorSchedulers,
    private val dispatchers: CoroutineDispatchers
) : ICartListPresenter, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + dispatchers.immediate

    private var view: ICartListView? = null

    private var cartListData: CartData? = null
    private var summaryTransactionUiModel: SummaryTransactionUiModel? = null
    private var promoSummaryUiModel: PromoSummaryData? = null

    private var hasPerformChecklistChange: Boolean = false

    // Store last validate use response from promo page
    var lastValidateUseResponse: ValidateUsePromoRevampUiModel? = null

    // Store last validate use response from cart page
    var lastUpdateCartAndValidateUseResponse: UpdateAndValidateUseData? = null
    var isLastApplyResponseStillValid = true

    // Store last validate use request
    private var lastValidateUseRequest: ValidateUsePromoRequest? = null

    // Store promo ticker
    private var promoTicker: CartPromoTicker = CartPromoTicker()

    // Store flag show choose promo widget
    private var showChoosePromoWidget: Boolean = false

    // Store LCA data for bo affordability
    var lca: LocalCacheModel? = null

    // Store last cart shop group ticker cart string for debounce handling
    var lastCartShopGroupTickerCartString: String = ""

    // Bo affordability debounce job
    var cartShopGroupTickerJob: Job? = null

    companion object {
        private const val PERCENTAGE = 100.0f
        private const val CART_SHOP_GROUP_TICKER_DELAY = 500L
        private const val BO_AFFORDABILITY_WEIGHT_KILO = 1000

        const val ITEM_CHECKED_ALL_WITHOUT_CHANGES = 0
        const val ITEM_CHECKED_ALL_WITH_CHANGES = 1
        const val ITEM_CHECKED_PARTIAL_SHOP = 3
        const val ITEM_CHECKED_PARTIAL_ITEM = 4
        const val ITEM_CHECKED_PARTIAL_SHOP_AND_ITEM = 5
        private const val RECENT_VIEW_XSOURCE = "recentview"
        private const val PAGE_NAME_RECENT_VIEW = "cart_recent_view"

        private const val QUERY_APP_CLIENT_ID = "{app_client_id}"
        private val REGEX_NUMBER = "[^0-9]".toRegex()
        private val SOURCE_CART = "cart"
    }

    override fun attachView(view: ICartListView) {
        this.view = view
    }

    override fun detachView() {
        compositeSubscription.unsubscribe()
        cartShopGroupTickerJob?.cancel()
        coroutineContext.cancelChildren()
        view = null
    }

    override fun getCartListData(): CartData? {
        return cartListData
    }

    override fun setCartListData(cartListData: CartData) {
        this.cartListData = cartListData
        this.promoSummaryUiModel =
            CartUiModelMapper.mapPromoSummaryUiModel(cartListData.promoSummary)
    }

    override fun getSummaryTransactionUiModel(): SummaryTransactionUiModel? {
        return summaryTransactionUiModel
    }

    override fun getPromoSummaryUiModel(): PromoSummaryData? {
        return promoSummaryUiModel
    }

    override fun processInitialGetCartData(
        cartId: String,
        initialLoad: Boolean,
        isLoadingTypeRefresh: Boolean,
        getCartState: Int
    ) {
        view?.let {
            CartIdlingResource.increment()
            if (initialLoad) {
                it.renderLoadGetCartData()
            } else if (!isLoadingTypeRefresh) {
                it.showProgressLoading()
            }

            getCartRevampV3UseCase.setParams(cartId, getCartState)
            getCartRevampV3UseCase.execute(
                onSuccess = {
                    onSuccessGetCartList(it, initialLoad)
                },
                onError = {
                    onErrorGetCartList(it, initialLoad)
                }
            )
        }
    }

    private fun onErrorGetCartList(throwable: Throwable, initialLoad: Boolean) {
        Timber.e(throwable)
        view?.let {
            if (!initialLoad) {
                it.hideProgressLoading()
            }
            it.renderLoadGetCartDataFinish()
            it.renderErrorInitialGetCartListData(throwable)
            it.stopCartPerformanceTrace(false)
            CartLogger.logOnErrorLoadCartPage(throwable)
            CartIdlingResource.decrement()
        }
    }

    private fun onSuccessGetCartList(cartData: CartData, initialLoad: Boolean) {
        view?.let {
            setLastApplyValid()
            setValidateUseLastResponse(null)
            setUpdateCartAndValidateUseLastResponse(null)
            if (!initialLoad) {
                it.hideProgressLoading()
            }
            setCartListData(cartData)
            summaryTransactionUiModel = CartUiModelMapper.mapSummaryTransactionUiModel(cartData)
            showChoosePromoWidget = cartData.promo.showChoosePromoWidget
            promoTicker = cartData.promo.ticker
            it.renderLoadGetCartDataFinish()
            it.renderInitialGetCartListDataSuccess(cartData)
            it.stopCartPerformanceTrace(true)
            CartIdlingResource.decrement()
        }
    }

    override fun processDeleteCartItem(
        allCartItemData: List<CartItemHolderData>,
        removedCartItems: List<CartItemHolderData>,
        addWishList: Boolean,
        forceExpandCollapsedUnavailableItems: Boolean,
        isFromGlobalCheckbox: Boolean,
        isFromEditBundle: Boolean
    ) {
        view?.let {
            it.showProgressLoading()

            val removeAllItems = allCartItemData.size == removedCartItems.size
            val toBeDeletedCartIds = ArrayList<String>()
            for (cartItemData in removedCartItems) {
                toBeDeletedCartIds.add(cartItemData.cartId)
            }

            deleteCartUseCase.setParams(toBeDeletedCartIds, addWishList)
            deleteCartUseCase.execute(
                onSuccess = {
                    onSuccessDeleteCartItems(
                        toBeDeletedCartIds,
                        removeAllItems,
                        forceExpandCollapsedUnavailableItems,
                        addWishList,
                        isFromGlobalCheckbox,
                        isFromEditBundle
                    )
                },
                onError = {
                    onErrorDeleteCartItems(forceExpandCollapsedUnavailableItems, it)
                }
            )
        }
    }

    private fun onErrorDeleteCartItems(
        forceExpandCollapsedUnavailableItems: Boolean,
        throwable: Throwable
    ) {
        view?.let { view ->
            if (forceExpandCollapsedUnavailableItems) {
                view.reCollapseExpandedDeletedUnavailableItems()
            }
            view.hideProgressLoading()
            view.showToastMessageRed(throwable)
        }
    }

    private fun onSuccessDeleteCartItems(
        toBeDeletedCartIds: ArrayList<String>,
        removeAllItems: Boolean,
        forceExpandCollapsedUnavailableItems: Boolean,
        addWishList: Boolean,
        isFromGlobalCheckbox: Boolean,
        isFromEditBundle: Boolean
    ) {
        view?.let { view ->
            view.renderLoadGetCartDataFinish()
            view.onDeleteCartDataSuccess(
                toBeDeletedCartIds,
                removeAllItems,
                forceExpandCollapsedUnavailableItems,
                addWishList,
                isFromGlobalCheckbox,
                isFromEditBundle
            )

            val params = view.generateGeneralParamValidateUse()
            if (!removeAllItems && (view.checkHitValidateUseIsNeeded(params))) {
                view.showPromoCheckoutStickyButtonLoading()
                doUpdateCartAndValidateUse(params)
            }
            processUpdateCartCounter()
        }
    }

    override fun processUndoDeleteCartItem(cartIds: List<String>) {
        view?.let {
            it.showProgressLoading()
            undoDeleteCartUseCase.setParams(cartIds)
            undoDeleteCartUseCase.execute(
                onSuccess = {
                    onSuccessUndoDeleteCartItem()
                },
                onError = {
                    onErrorUndoDeleteCartItem(it)
                }
            )
        }
    }

    private fun onErrorUndoDeleteCartItem(throwable: Throwable) {
        view?.let { view ->
            Timber.e(throwable)
            view.hideProgressLoading()
            view.showToastMessageRed(throwable)
        }
    }

    private fun onSuccessUndoDeleteCartItem() {
        view?.let { view ->
            view.hideProgressLoading()
            view.onUndoDeleteCartDataSuccess()
        }
    }

    override fun processUpdateCartData(fireAndForget: Boolean, onlyTokoNowProducts: Boolean) {
        view?.let {
            if (!fireAndForget) {
                it.showProgressLoading()
                CartIdlingResource.increment()
            }

            val cartItemDataList: List<CartItemHolderData> = if (fireAndForget) {
                // Update cart to save state
                it.getAllAvailableCartDataList()
            } else {
                // Update cart to go to shipment
                it.getAllSelectedCartDataList() ?: emptyList()
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
                    return@let
                } else {
                    updateCartUseCase.setParams(updateCartRequestList)
                    updateCartUseCase.execute(
                        onSuccess = {
                            onSuccessUpdateCartForCheckout(it, cartItemDataList)
                        },
                        onError = {
                            onErrorUpdateCartForCheckout(it, cartItemDataList)
                        }
                    )
                }
            } else {
                if (!fireAndForget) {
                    it.hideProgressLoading()
                    CartLogger.logOnErrorUpdateCartForCheckout(
                        MessageErrorException("update cart empty product"),
                        cartItemDataList
                    )
                }
            }
        }
    }

    private fun onErrorUpdateCartForCheckout(
        throwable: Throwable,
        cartItemDataList: List<CartItemHolderData>
    ) {
        view?.let { view ->
            view.hideProgressLoading()
            view.renderErrorToShipmentForm(throwable)
            CartLogger.logOnErrorUpdateCartForCheckout(throwable, cartItemDataList)
        }
    }

    private fun onSuccessUpdateCartForCheckout(
        updateCartV2Data: UpdateCartV2Data,
        cartItemDataList: List<CartItemHolderData>
    ) {
        view?.let { view ->
            view.hideProgressLoading()
            if (updateCartV2Data.data.status) {
                val checklistCondition = getChecklistCondition()
                view.renderToShipmentFormSuccess(
                    generateCheckoutDataAnalytics(
                        cartItemDataList,
                        EnhancedECommerceActionField.STEP_1
                    ),
                    cartItemDataList,
                    isCheckoutProductEligibleForCashOnDelivery(cartItemDataList),
                    checklistCondition
                )
            } else {
                if (updateCartV2Data.data.outOfService.isOutOfService()) {
                    view.renderErrorToShipmentForm(updateCartV2Data.data.outOfService)
                } else {
                    view.renderErrorToShipmentForm(
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
    }

    private fun getChecklistCondition(): Int {
        var checklistCondition = ITEM_CHECKED_ALL_WITHOUT_CHANGES
        val cartShopHolderDataList = view?.getAllShopDataList()

        if (cartShopHolderDataList?.size ?: 0 == 1) {
            cartShopHolderDataList?.get(0)?.productUiModelList?.let {
                for (cartItemHolderData in it) {
                    if (!cartItemHolderData.isSelected) {
                        checklistCondition = ITEM_CHECKED_PARTIAL_ITEM
                        break
                    }
                }
            }
        } else if (cartShopHolderDataList?.size ?: 0 > 1) {
            var allSelectedItemShopCount = 0
            var selectPartialShopAndItem = false
            cartShopHolderDataList?.let {
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

        if (checklistCondition == ITEM_CHECKED_ALL_WITHOUT_CHANGES && getHasPerformChecklistChange()) {
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
        return totalAmount <= UpdateCartSubscriber.MAX_TOTAL_AMOUNT_ELIGIBLE_FOR_COD
    }

    override fun processToUpdateAndReloadCartData(cartId: String, getCartState: Int) {
        view?.let {
            val cartItemDataList = ArrayList<CartItemHolderData>()
            for (data in it.getAllAvailableCartDataList()) {
                if (!data.isError) {
                    cartItemDataList.add(data)
                }
            }

            val updateCartRequestList = getUpdateCartRequest(cartItemDataList)
            if (updateCartRequestList.isNotEmpty()) {
                val requestParams = RequestParams.create()
                requestParams.putObject(
                    UpdateCartAndValidateUseUseCase.PARAM_UPDATE_CART_REQUEST,
                    updateCartRequestList
                )
                requestParams.putString(
                    UpdateCartAndValidateUseUseCase.PARAM_KEY_SOURCE,
                    UpdateCartAndValidateUseUseCase.PARAM_VALUE_SOURCE_UPDATE_QTY_NOTES
                )
                requestParams.putString(GetCartRevampV3UseCase.PARAM_KEY_SELECTED_CART_ID, cartId)
                requestParams.putInt(GetCartRevampV3UseCase.PARAM_KEY_STATE, getCartState)

                compositeSubscription.add(
                    updateAndReloadCartUseCase.createObservable(requestParams)
                        .subscribe(UpdateAndReloadCartSubscriber(it, this))
                )
            } else {
                it.hideProgressLoading()
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

    override fun updatePromoSummaryData(lastApplyUiModel: LastApplyUiModel) {
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

    override fun reCalculateSubTotal(dataList: List<CartShopHolderData>) {
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
        view?.updateCashback(subtotalCashback)
        view?.renderDetailInfoSubTotal(totalItemQty.toString(), subtotalPrice, dataList.isEmpty())
    }

    private fun updateSummaryTransactionUiModel(
        subtotalBeforeSlashedPrice: Double,
        subtotalPrice: Double,
        totalItemQty: Int,
        subtotalCashback: Double
    ) {
        summaryTransactionUiModel?.qty = totalItemQty.toString()
        if (subtotalBeforeSlashedPrice == 0.0) {
            summaryTransactionUiModel?.totalValue = subtotalPrice.toLong()
        } else {
            summaryTransactionUiModel?.totalValue = subtotalBeforeSlashedPrice.toLong()
        }
        summaryTransactionUiModel?.discountValue =
            (subtotalBeforeSlashedPrice - subtotalPrice).toLong()
        summaryTransactionUiModel?.paymentTotal = subtotalPrice.toLong()
        summaryTransactionUiModel?.sellerCashbackValue = subtotalCashback.toLong()
    }

    fun getAvailableCartItemDataListAndShopTotalWeight(cartShopHolderData: CartShopHolderData): Pair<ArrayList<CartItemHolderData>, Double> {
        val allCartItemDataList = ArrayList<CartItemHolderData>()
        var shopWeight = 0.0
        if (!cartShopHolderData.isError && cartShopHolderData.hasSelectedProduct) {
            cartShopHolderData.productUiModelList.forEach { cartItemHolderData ->
                if (!cartItemHolderData.isError && cartItemHolderData.isSelected) {
                    allCartItemDataList.add(cartItemHolderData)
                    val quantity =
                        if (cartItemHolderData.isBundlingItem) cartItemHolderData.quantity * cartItemHolderData.bundleQuantity
                        else cartItemHolderData.quantity

                    val weight = cartItemHolderData.productWeight
                    shopWeight += quantity * weight
                }
            }
        }
        return allCartItemDataList to shopWeight
    }

    private fun getAvailableCartItemDataList(dataList: List<CartShopHolderData>): ArrayList<CartItemHolderData> {
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
                    wholesalePriceData.prdPrc, false
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
                if (cartItemHolderData.productOriginalPrice > 0) (itemQty * cartItemHolderData.productOriginalPrice)
                else (itemQty * cartItemHolderData.productPrice)
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
                if (cartItemHolderData.isBundlingItem) cartItemHolderData.bundleQuantity * cartItemHolderData.quantity
                else cartItemHolderData.quantity
            totalItemQty += itemQty
            if (cartItemHolderData.parentId.isNotBlank() && cartItemHolderData.parentId.isNotBlank() && cartItemHolderData.parentId != "0") {
                for (cartItemHolderDataTmp in allCartItemDataList) {
                    if (cartItemHolderData.productId != cartItemHolderDataTmp.productId &&
                        cartItemHolderData.parentId == cartItemHolderDataTmp.parentId &&
                        cartItemHolderData.productPrice == cartItemHolderDataTmp.productPrice
                    ) {
                        val tmpQty =
                            if (cartItemHolderDataTmp.isBundlingItem) cartItemHolderDataTmp.bundleQuantity * cartItemHolderDataTmp.quantity
                            else cartItemHolderDataTmp.quantity
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
            } else if (!cartItemHolderData.wholesalePriceData.isNullOrEmpty()) {
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

    override fun processAddToWishlistV2(
        productId: String,
        userId: String,
        wishListActionListener: WishlistV2ActionListener
    ) {
        launch(dispatchers.main) {
            addToWishlistV2UseCase.setParams(productId, userId)
            val result =
                withContext(dispatchers.io) { addToWishlistV2UseCase.executeOnBackground() }
            if (result is Success) {
                wishListActionListener.onSuccessAddWishlist(result.data, productId)
            } else {
                val error = (result as Fail).throwable
                wishListActionListener.onErrorAddWishList(error, productId)
            }
        }
    }

    override fun processRemoveFromWishlistV2(
        productId: String,
        userId: String,
        wishListActionListener: WishlistV2ActionListener
    ) {
        launch(dispatchers.main) {
            deleteWishlistV2UseCase.setParams(productId, userId)
            val result =
                withContext(dispatchers.io) { deleteWishlistV2UseCase.executeOnBackground() }
            if (result is Success) {
                wishListActionListener.onSuccessRemoveWishlist(result.data, productId)
            } else {
                val error = (result as Fail).throwable
                wishListActionListener.onErrorRemoveWishlist(error, productId)
            }
        }
    }

    override fun processAddCartToWishlist(
        productId: String,
        cartId: String,
        isLastItem: Boolean,
        source: String,
        forceExpandCollapsedUnavailableItems: Boolean
    ) {
        view?.let {
            val addCartToWishlistRequest = AddCartToWishlistRequest()
            addCartToWishlistRequest.cartIds = listOf(cartId)

            val requestParams = RequestParams.create()
            requestParams.putObject(
                AddCartToWishlistUseCase.PARAM_ADD_CART_TO_WISHLIST_REQUEST,
                addCartToWishlistRequest
            )

            compositeSubscription.add(
                addCartToWishlistUseCase.createObservable(requestParams)
                    .subscribe(
                        AddCartToWishlistSubscriber(
                            it,
                            this,
                            productId,
                            cartId,
                            isLastItem,
                            source,
                            forceExpandCollapsedUnavailableItems
                        )
                    )
            )
        }
    }

    // ANALYTICS COMMON
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

    // ANALYTICS DELETE ACTION
    override fun generateDeleteCartDataAnalytics(cartItemDataList: List<CartItemHolderData>): Map<String, Any> {
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
                if (cartItemHolderData.category.isBlank()) {
                    EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                } else {
                    cartItemHolderData.category
                }
            )
            setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setQty(cartItemHolderData.quantity)
            setShopId(cartItemHolderData.shopId)
            setShopType(cartItemHolderData.shopTypeInfoData.titleFmt)
            setShopName(cartItemHolderData.shopName)
            setCategoryId(cartItemHolderData.categoryId)
            setAttribution(
                if (cartItemHolderData.trackerAttribution.isBlank()) {
                    EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                } else {
                    cartItemHolderData.trackerAttribution
                }
            )
            setDimension38(
                if (cartItemHolderData.trackerAttribution.isBlank()) {
                    EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                } else {
                    cartItemHolderData.trackerAttribution
                }
            )
            setListName(
                if (cartItemHolderData.trackerListName.isBlank()) {
                    EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                } else {
                    cartItemHolderData.trackerListName
                }
            )
            setDimension40(
                if (cartItemHolderData.trackerListName.isBlank()) {
                    EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                } else {
                    cartItemHolderData.trackerListName
                }
            )
        }
    }

    // ANALYTICS IMPRESSION
    override fun generateRecommendationImpressionDataAnalytics(
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
                if (recommendationItem.categoryBreadcrumbs.isBlank())
                    EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                else
                    recommendationItem.categoryBreadcrumbs
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

    override fun generateWishlistDataImpressionAnalytics(
        cartWishlistItemHolderDataList: List<CartWishlistItemHolderData>,
        isEmptyCart: Boolean
    ): Map<String, Any> {
        val enhancedECommerceCartMapData = EnhancedECommerceCartMapData().apply {
            var position = 0
            for (cartWishlistItemHolderData in cartWishlistItemHolderDataList) {
                val enhancedECommerceProductCartMapData = getProductWishlistImpressionMapData(
                    cartWishlistItemHolderData,
                    isEmptyCart,
                    position
                )
                addImpression(enhancedECommerceProductCartMapData.getProduct())
                position++
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

    override fun generateRecentViewDataImpressionAnalytics(
        cartRecentViewItemHolderDataList: List<CartRecentViewItemHolderData>,
        isEmptyCart: Boolean
    ): Map<String, Any> {
        val enhancedECommerceCartMapData = EnhancedECommerceCartMapData().apply {
            var position = 0
            for (cartRecentViewItemHolderData in cartRecentViewItemHolderDataList) {
                val enhancedECommerceProductCartMapData = getProductRecentViewImpressionMapData(
                    cartRecentViewItemHolderData,
                    isEmptyCart,
                    position
                )
                addImpression(enhancedECommerceProductCartMapData.getProduct())
                position++
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

    // ANALYTICS CLICK ACTION
    override fun generateRecommendationDataOnClickAnalytics(
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
                if (recommendationItem.categoryBreadcrumbs.isBlank())
                    EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                else
                    recommendationItem.categoryBreadcrumbs
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

    override fun generateRecentViewProductClickDataLayer(
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

    override fun generateRecentViewProductClickEmptyCartDataLayer(
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
            productsData, EnhancedECommerceActionFieldData.VALUE_SECTION_NAME_RECENT_VIEW_EMPTY_CART
        )

        return enhancedECommerceEmptyCart.getData()
    }

    override fun generateWishlistProductClickDataLayer(
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

    override fun generateWishlistProductClickEmptyCartDataLayer(
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
            productsData, EnhancedECommerceActionFieldData.VALUE_SECTION_NAME_WISHLIST_EMPTY_CART
        )

        return enhancedECommerceEmptyCart.getData()
    }

    // ANALYTICS STEP 0 / STEP 1
    override fun generateCheckoutDataAnalytics(
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
                if (cartItemHolderData.trackerAttribution.isBlank()) {
                    EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                } else {
                    cartItemHolderData.trackerAttribution
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
                if (cartItemHolderData.category.isBlank()) {
                    EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                } else {
                    cartItemHolderData.category
                }
            )
            setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setQty(cartItemHolderData.quantity)
            setShopId(cartItemHolderData.shopId)
            setShopType(cartItemHolderData.shopTypeInfoData.titleFmt)
            setShopName(cartItemHolderData.shopName)
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

    // ANALYTICS ATC
    override fun generateAddToCartEnhanceEcommerceDataLayer(
        cartWishlistItemHolderData: CartWishlistItemHolderData,
        addToCartDataResponseModel: AddToCartDataModel, isCartEmpty: Boolean
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

    override fun generateAddToCartEnhanceEcommerceDataLayer(
        cartRecentViewItemHolderData: CartRecentViewItemHolderData,
        addToCartDataResponseModel: AddToCartDataModel, isCartEmpty: Boolean
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

    override fun generateAddToCartEnhanceEcommerceDataLayer(
        cartRecommendationItemHolderData: CartRecommendationItemHolderData,
        addToCartDataResponseModel: AddToCartDataModel, isCartEmpty: Boolean
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

    override fun setHasPerformChecklistChange(hasChangeState: Boolean) {
        hasPerformChecklistChange = hasChangeState
    }

    override fun getHasPerformChecklistChange(): Boolean {
        return hasPerformChecklistChange
    }

    override fun dataHasChanged(): Boolean {
        var hasChanges = false
        view?.getAllCartDataList()?.let {
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

    override fun processGetRecentViewData(allProductIds: List<String>) {
        view?.showItemLoading()
        val requestParam = getRecentViewUseCase.getRecomParams(
            1, RECENT_VIEW_XSOURCE, PAGE_NAME_RECENT_VIEW, allProductIds, ""
        )
        compositeSubscription.add(
            getRecentViewUseCase.createObservable(requestParam)
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)
                .subscribe(GetRecentViewSubscriber(view))
        )
    }

    override fun processGetWishlistV2Data() {
        val requestParams = WishlistV2Params().apply {
            source = SOURCE_CART
            lca?.let { address ->
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
                view?.let {
                    if (result.data.items.isNotEmpty()) {
                        it.renderWishlistV2(result.data.items, true)
                    }
                    it.setHasTriedToLoadWishList()
                    it.stopAllCartPerformanceTrace()
                }
            } else {
                val error = (result as Fail).throwable
                Timber.d(error)
                view?.setHasTriedToLoadWishList()
                view?.stopAllCartPerformanceTrace()
            }
        }
    }

    override fun processGetRecommendationData(page: Int, allProductIds: List<String>) {
        view?.showItemLoading()
        val requestParam = getRecommendationUseCase.getRecomParams(
            page, "recom_widget", "cart", allProductIds, ""
        )
        compositeSubscription.add(
            getRecommendationUseCase.createObservable(requestParam)
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)
                .subscribe(GetRecommendationSubscriber(view))
        )
    }

    override fun processAddToCart(productModel: Any) {
        view?.showProgressLoading()

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
            if (clickUrl.isNotEmpty() && productModel.isTopAds) view?.sendATCTrackingURLRecent(
                productModel
            )
        } else if (productModel is CartRecommendationItemHolderData) {
            val recommendationItem = productModel.recommendationItem
            productId = recommendationItem.productId.toLong()
            shopId = recommendationItem.shopId
            productName = recommendationItem.name
            productCategory = recommendationItem.categoryBreadcrumbs
            productPrice = recommendationItem.price
            quantity = productModel.recommendationItem.minOrder
            externalSource = AtcFromExternalSource.ATC_FROM_RECOMMENDATION

            val clickUrl = recommendationItem.clickUrl
            if (clickUrl.isNotEmpty()) view?.sendATCTrackingURL(recommendationItem)
        } else if (productModel is BannerShopProductUiModel) {
            productId = productModel.productId.toLongOrZero()
            shopId = productModel.shopId.toIntOrZero()
            productName = productModel.productName
            productCategory = productModel.productCategory
            productPrice = productModel.productPrice
            quantity = productModel.productMinOrder
            externalSource = AtcFromExternalSource.ATC_FROM_RECOMMENDATION

            val clickUrl = productModel.adsClickUrl
            if (clickUrl.isNotEmpty()) view?.sendATCTrackingURL(productModel)
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

        val requestParams = RequestParams.create()
        requestParams.putObject(
            AddToCartUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST,
            addToCartRequestParams
        )
        compositeSubscription.add(
            addToCartUseCase.createObservable(requestParams)
                .subscribeOn(schedulers.io)
                .unsubscribeOn(schedulers.io)
                .observeOn(schedulers.main)
                .subscribe(AddToCartSubscriber(view, this, productModel))
        )
    }

    override fun processAddToCartExternal(productId: Long) {
        view?.showProgressLoading()
        val requestParams = RequestParams.create()
        requestParams.putString(AddToCartExternalUseCase.PARAM_PRODUCT_ID, productId.toString())
        requestParams.putString(AddToCartExternalUseCase.PARAM_USER_ID, userSessionInterface.userId)
        compositeSubscription.add(
            addToCartExternalUseCase.createObservable(requestParams)
                .subscribeOn(schedulers.io)
                .unsubscribeOn(schedulers.io)
                .observeOn(schedulers.main)
                .subscribe(AddToCartExternalSubscriber(view))
        )
    }

    override fun redirectToLite(url: String) {
        view?.let {
            it.showProgressLoading()
            val adsId = it.getAdsId()
            if (adsId != null && !adsId.trim { it <= ' ' }.isEmpty()) {
                seamlessLoginUsecase.generateSeamlessUrl(
                    url.replace(QUERY_APP_CLIENT_ID, adsId),
                    CartSeamlessLoginSubscriber(view)
                )
            } else {
                it.hideProgressLoading()
                it.showToastMessageRed()
            }
        }
    }

    override fun processUpdateCartCounter() {
        compositeSubscription.add(
            updateCartCounterUseCase.createObservable(RequestParams.create())
                .subscribeOn(schedulers.io)
                .unsubscribeOn(schedulers.io)
                .observeOn(schedulers.main)
                .subscribe(UpdateCartCounterSubscriber(view))
        )
    }

    override fun doUpdateCartForPromo() {
        view?.let { view ->
            view.showProgressLoading()

            val updateCartRequestList = getUpdateCartRequest(view.getAllSelectedCartDataList())
            if (updateCartRequestList.isNotEmpty()) {
                updateCartUseCase.setParams(
                    updateCartRequestList,
                    UpdateCartUseCase.VALUE_SOURCE_UPDATE_QTY_NOTES
                )
                updateCartUseCase.execute(
                    onSuccess = {
                        onSuccessUpdateCartForPromo(it)
                    },
                    onError = {
                        onErrorUpdateCartForPromo(it)
                    }
                )
            } else {
                view.hideProgressLoading()
            }
        }

    }

    private fun onErrorUpdateCartForPromo(throwable: Throwable) {
        view?.let { view ->
            view.hideProgressLoading()
            view.showToastMessageRed(throwable)
        }
    }

    private fun onSuccessUpdateCartForPromo(updateCartV2Data: UpdateCartV2Data) {
        view?.let { view ->
            view.hideProgressLoading()
            view.navigateToPromoRecommendation()
        }
    }

    override fun doValidateUse(promoRequest: ValidateUsePromoRequest) {
        val requestParams = RequestParams.create()
        requestParams.putObject(OldValidateUsePromoRevampUseCase.PARAM_VALIDATE_USE, promoRequest)
        lastValidateUseRequest = promoRequest
        compositeSubscription.add(
            validateUsePromoRevampUseCase.createObservable(requestParams)
                .subscribeOn(schedulers.io)
                .unsubscribeOn(schedulers.io)
                .observeOn(schedulers.main)
                .subscribe(ValidateUseSubscriber(view, this))
        )
    }

    override fun doUpdateCartAndValidateUse(promoRequest: ValidateUsePromoRequest) {
        view?.let { cartListView ->
            val cartItemDataList = ArrayList<CartItemHolderData>()
            cartListView.getAllSelectedCartDataList().let { listCartItemData ->
                for (data in listCartItemData) {
                    if (!data.isError) {
                        cartItemDataList.add(data)
                    }
                }
            }

            val updateCartRequestList = getUpdateCartRequest(cartItemDataList)
            if (updateCartRequestList.isNotEmpty()) {
                val requestParams = RequestParams.create()
                requestParams.putObject(
                    UpdateCartAndValidateUseUseCase.PARAM_UPDATE_CART_REQUEST,
                    updateCartRequestList
                )
                requestParams.putString(
                    UpdateCartAndValidateUseUseCase.PARAM_KEY_SOURCE,
                    UpdateCartAndValidateUseUseCase.PARAM_VALUE_SOURCE_UPDATE_QTY_NOTES
                )
                requestParams.putObject(
                    OldValidateUsePromoRevampUseCase.PARAM_VALIDATE_USE,
                    promoRequest
                )
                lastValidateUseRequest = promoRequest

                compositeSubscription.add(
                    updateCartAndValidateUseUseCase.createObservable(requestParams)
                        .subscribe(
                            UpdateCartAndValidateUseSubscriber(
                                cartListView,
                                this,
                                promoTicker.enable
                            )
                        )
                )
            } else {
                cartListView.hideProgressLoading()
            }
        }
    }

    override fun doClearRedPromosBeforeGoToCheckout(clearPromoRequest: ClearPromoRequest) {
        view?.showItemLoading()
        clearCacheAutoApplyStackUseCase.setParams(clearPromoRequest)
        compositeSubscription.add(
            clearCacheAutoApplyStackUseCase.createObservable(RequestParams.create())
                .subscribe(ClearRedPromosBeforeGoToCheckoutSubscriber(view))
        )
    }

    override fun doClearAllPromo() {
        lastValidateUseRequest?.let {
            val param = ClearPromoRequest(
                OldClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE,
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
                        )
                    }
                )
            )
            clearCacheAutoApplyStackUseCase.setParams(param)
            compositeSubscription.add(
                // Do nothing on subscribe
                clearCacheAutoApplyStackUseCase.createObservable(RequestParams.create()).subscribe()
            )
            setLastApplyNotValid()
            setValidateUseLastResponse(ValidateUsePromoRevampUiModel())
        }
    }

    override fun setLastValidateUseRequest(validateUsePromoRequest: ValidateUsePromoRequest) {
        lastValidateUseRequest = validateUsePromoRequest
    }

    override fun getLastValidateUseRequest(): ValidateUsePromoRequest? {
        return lastValidateUseRequest
    }

    override fun getValidateUseLastResponse(): ValidateUsePromoRevampUiModel? {
        return lastValidateUseResponse
    }

    override fun setValidateUseLastResponse(response: ValidateUsePromoRevampUiModel?) {
        lastValidateUseResponse = response
    }

    override fun getUpdateCartAndValidateUseLastResponse(): UpdateAndValidateUseData? {
        return lastUpdateCartAndValidateUseResponse
    }

    override fun setUpdateCartAndValidateUseLastResponse(response: UpdateAndValidateUseData?) {
        lastUpdateCartAndValidateUseResponse = response
    }

    override fun isLastApplyValid(): Boolean {
        return isLastApplyResponseStillValid
    }

    override fun setLastApplyNotValid() {
        isLastApplyResponseStillValid = false
    }

    override fun setLastApplyValid() {
        isLastApplyResponseStillValid = true
    }

    override fun saveCheckboxState(cartItemDataList: List<CartItemHolderData>) {
        val requestParams = setCartlistCheckboxStateUseCase.buildRequestParams(cartItemDataList)
        compositeSubscription.add(
            setCartlistCheckboxStateUseCase.createObservable(requestParams)
                ?.subscribe({ /* No-op*/ }, { /* No-op*/ })
        )
    }

    override fun followShop(shopId: String) {
        view?.showProgressLoading()
        val requestParams = followShopUseCase.buildRequestParams(shopId)
        compositeSubscription.add(
            followShopUseCase.createObservable(requestParams)
                .subscribe(FollowShopSubscriber(view, this))
        )
    }

    override fun setLocalizingAddressData(lca: LocalCacheModel?) {
        this.lca = lca
    }

    override fun checkCartShopGroupTicker(cartShopHolderData: CartShopHolderData) {
        if (lastCartShopGroupTickerCartString == cartShopHolderData.cartString) {
            cartShopGroupTickerJob?.cancel()
        }
        lastCartShopGroupTickerCartString = cartShopHolderData.cartString
        cartShopGroupTickerJob = launch(dispatchers.io) {
            try {
                delay(CART_SHOP_GROUP_TICKER_DELAY)
                // kalo user ada centang salah satu product bundle berarti enableBundle=false,
                // cek juga kalo user hanya centang product2 yang ga ada bundleIds => enableBundle=false
                val enableBundleCrossSell = checkEnableBundleCrossSell(cartShopHolderData)
                if (!cartShopHolderData.cartShopGroupTicker.enableBoAffordability && !enableBundleCrossSell) {
                    cartShopHolderData.cartShopGroupTicker.state = CartShopGroupTickerState.EMPTY
                    withContext(dispatchers.main) {
                        view?.updateCartShopGroupTicker(cartShopHolderData)
                    }
                    return@launch
                }
                val shopShipments = cartShopHolderData.shopShipments
                // Recalculate total price and total weight, to prevent racing condition
                val (shopProductList, shopTotalWeight) =
                    getAvailableCartItemDataListAndShopTotalWeight(cartShopHolderData)
                if (cartShopHolderData.cartShopGroupTicker.enableBoAffordability
                    && cartShopHolderData.shouldValidateWeight
                    && shopTotalWeight > cartShopHolderData.maximumShippingWeight) {
                    // Check for overweight (only when BO Affordability is enabled)
                    cartShopHolderData.cartShopGroupTicker.state =
                        CartShopGroupTickerState.FAILED
                    withContext(dispatchers.main) {
                        view?.updateCartShopGroupTicker(cartShopHolderData)
                    }
                    return@launch
                }
                val calculatePriceMarketplaceProduct =
                    calculatePriceMarketplaceProduct(shopProductList)
                val subtotalPrice = calculatePriceMarketplaceProduct.second.second.toLong()
                val shipping = ShippingParam().apply {
                    destinationDistrictId = lca?.district_id
                    destinationLongitude = lca?.long
                    destinationLatitude = lca?.lat
                    destinationPostalCode = lca?.postal_code
                    originDistrictId = cartShopHolderData.districtId
                    originLongitude = cartShopHolderData.longitude
                    originLatitude = cartShopHolderData.latitude
                    originPostalCode = cartShopHolderData.postalCode
                    weightInKilograms = shopTotalWeight / BO_AFFORDABILITY_WEIGHT_KILO
                    weightActualInKilograms = shopTotalWeight / BO_AFFORDABILITY_WEIGHT_KILO
                    orderValue = subtotalPrice
                    shopId = cartShopHolderData.shopId
                    shopTier = cartShopHolderData.shopTypeInfo.shopTier
                    uniqueId = cartShopHolderData.cartString
                    isFulfillment = cartShopHolderData.isFulfillment
                    boMetadata = cartShopHolderData.boMetadata
                    products = shopProductList.map {
                        Product(
                            it.productId.toLong(),
                            it.isFreeShipping,
                            it.isFreeShippingExtra
                        )
                    }
                }
                val cartAggregatorParam = CartShopGroupTickerAggregatorParam(
                    ratesParam = RatesParam.Builder(shopShipments, shipping).build(),
                    enableBoAffordability = cartShopHolderData.cartShopGroupTicker.enableBoAffordability,
                    enableBundleCrossSell = enableBundleCrossSell
                )
                val response = cartShopGroupTickerAggregatorUseCase(cartAggregatorParam)
                cartShopHolderData.cartShopGroupTicker.cartIds =
                    shopProductList.joinToString(",") { it.cartId }
                cartShopHolderData.cartShopGroupTicker.enableBundleCrossSell = enableBundleCrossSell
                cartShopHolderData.cartShopGroupTicker.tickerText = response.data.ticker.text
                cartShopHolderData.cartShopGroupTicker.leftIcon = response.data.ticker.icon.leftIcon
                cartShopHolderData.cartShopGroupTicker.leftIconDark = response.data.ticker.icon.leftIconDark
                cartShopHolderData.cartShopGroupTicker.rightIcon = response.data.ticker.icon.rightIcon
                cartShopHolderData.cartShopGroupTicker.rightIconDark = response.data.ticker.icon.rightIconDark
                cartShopHolderData.cartShopGroupTicker.applink = response.data.ticker.applink
                cartShopHolderData.cartShopGroupTicker.action = response.data.ticker.action
                cartShopHolderData.cartShopGroupTicker.cartBundlingBottomSheetData = CartBundlingBottomSheetData(
                    title = response.data.bundleBottomSheet.title,
                    description = response.data.bundleBottomSheet.description,
                    bottomTicker = response.data.bundleBottomSheet.bottomTicker,
                    bundleIds = cartShopHolderData.productUiModelList
                        .filter { it.isSelected }.flatMap { it.bundleIds }
                )
                cartShopHolderData.cartShopGroupTicker.hasSeenTicker = false
                if (response.data.ticker.text.isBlank()) {
                    cartShopHolderData.cartShopGroupTicker.state =
                        CartShopGroupTickerState.EMPTY
                } else if (subtotalPrice >= response.data.minTransaction) {
                    cartShopHolderData.cartShopGroupTicker.state =
                        CartShopGroupTickerState.SUCCESS_AFFORD
                } else {
                    cartShopHolderData.cartShopGroupTicker.state =
                        CartShopGroupTickerState.SUCCESS_NOT_AFFORD
                }
                withContext(dispatchers.main) {
                    view?.updateCartShopGroupTicker(cartShopHolderData)
                }
            } catch (t: Throwable) {
                if (t !is CancellationException) {
                    cartShopHolderData.cartShopGroupTicker.tickerText = ""
                    cartShopHolderData.cartShopGroupTicker.state = CartShopGroupTickerState.FAILED
                    withContext(dispatchers.main) {
                        view?.updateCartShopGroupTicker(cartShopHolderData)
                    }
                }
            }
        }
    }

    override fun getPromoFlag(): Boolean {
        val cartListData = cartListData
        val lastValidateUseResponse = lastValidateUseResponse
        return if (isLastApplyResponseStillValid && cartListData != null) {
            cartListData.promo.lastApplyPromo.lastApplyPromoData.additionalInfo.pomlAutoApplied
        } else if (!isLastApplyResponseStillValid && lastValidateUseResponse != null) {
            lastValidateUseResponse.promoUiModel.additionalInfoUiModel.pomlAutoApplied
        } else {
            false
        }
    }

    override fun getTickerPromoData(): CartPromoTicker {
        return promoTicker
    }

    override fun getShowChoosePromoWidget(): Boolean {
        return showChoosePromoWidget
    }

    override fun clearAllBo(clearPromoOrderData: ClearPromoOrderData) {
        clearCacheAutoApplyStackUseCase.setParams(
            ClearPromoRequest(
                OldClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE,
                orderData = clearPromoOrderData
            )
        )
        compositeSubscription.add(
            // Do nothing on subscribe
            clearCacheAutoApplyStackUseCase.createObservable(RequestParams.create()).subscribe()
        )
    }

    override fun validateBoPromo(validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel) {
        val shopDataList = view?.getAllShopDataList()
        if (shopDataList != null) {
            val boUniqueIds = mutableSetOf<String>()
            for (voucherOrderUiModel in validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels) {
                if (voucherOrderUiModel.shippingId > 0 && voucherOrderUiModel.spId > 0 && voucherOrderUiModel.type == "logistic") {
                    if (voucherOrderUiModel.messageUiModel.state == "green") {
                        shopDataList.firstOrNull { it.cartString == voucherOrderUiModel.uniqueId }?.boCode =
                            voucherOrderUiModel.code
                        boUniqueIds.add(voucherOrderUiModel.uniqueId)
                    }
                }
            }
            for (shop in shopDataList) {
                if (shop.boCode.isNotEmpty() && !boUniqueIds.contains(shop.cartString)) {
                    clearBo(shop)
                }
            }
        }
    }

    private fun clearBo(shop: CartShopHolderData) {
        clearCacheAutoApplyStackUseCase.setParams(
            ClearPromoRequest(
                serviceId = ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE,
                orderData = ClearPromoOrderData(
                    orders = listOf(
                        ClearPromoOrder(
                            uniqueId = shop.cartString,
                            boType = shop.boMetadata.boType,
                            codes = mutableListOf(shop.boCode),
                            shopId = shop.shopId.toLongOrZero(),
                            isPo = shop.isPo,
                            poDuration = shop.poDuration,
                            warehouseId = shop.warehouseId
                        )
                    )
                )
            )
        )
        compositeSubscription.add(
            clearCacheAutoApplyStackUseCase.createObservable(RequestParams.EMPTY).subscribe()
        )
        shop.promoCodes = ArrayList(shop.promoCodes).apply { remove(shop.boCode) }
        shop.boCode = ""
    }

    private fun checkEnableBundleCrossSell(cartShopHolderData: CartShopHolderData): Boolean {
        val hasCheckedProductWithBundle = cartShopHolderData.productUiModelList
            .any { it.isSelected && !it.isBundlingItem  && it.bundleIds.isNotEmpty() }
        val hasCheckedBundleProduct = cartShopHolderData.productUiModelList
            .any { it.isSelected && it.isBundlingItem &&  it.bundleIds.isNotEmpty() }
        return cartShopHolderData.cartShopGroupTicker.enableBundleCrossSell
            && hasCheckedProductWithBundle && !hasCheckedBundleProduct
    }
}
