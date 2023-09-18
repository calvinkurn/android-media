package com.tokopedia.cart.view

import android.os.Bundle
import androidx.core.os.bundleOf
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.atc_common.AtcFromExternalSource
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cart.data.model.request.AddCartToWishlistRequest
import com.tokopedia.cart.data.model.request.CartShopGroupTickerAggregatorParam
import com.tokopedia.cart.data.model.request.UpdateCartWrapperRequest
import com.tokopedia.cart.data.model.response.promo.CartPromoTicker
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.cart.domain.model.cartlist.SummaryTransactionUiModel
import com.tokopedia.cart.domain.model.updatecart.UpdateAndGetLastApplyData
import com.tokopedia.cart.domain.usecase.AddCartToWishlistUseCase
import com.tokopedia.cart.domain.usecase.CartShopGroupTickerAggregatorUseCase
import com.tokopedia.cart.domain.usecase.FollowShopUseCase
import com.tokopedia.cart.domain.usecase.GetCartParam
import com.tokopedia.cart.domain.usecase.GetCartRevampV4UseCase
import com.tokopedia.cart.domain.usecase.SetCartlistCheckboxStateUseCase
import com.tokopedia.cart.domain.usecase.UpdateAndReloadCartUseCase
import com.tokopedia.cart.domain.usecase.UpdateCartAndGetLastApplyUseCase
import com.tokopedia.cart.view.analytics.EnhancedECommerceActionFieldData
import com.tokopedia.cart.view.analytics.EnhancedECommerceClickData
import com.tokopedia.cart.view.analytics.EnhancedECommerceData
import com.tokopedia.cart.view.analytics.EnhancedECommerceProductData
import com.tokopedia.cart.view.mapper.CartUiModelMapper
import com.tokopedia.cart.view.mapper.PromoRequestMapper
import com.tokopedia.cart.view.subscriber.CartSeamlessLoginSubscriber
import com.tokopedia.cart.view.subscriber.UpdateCartCounterSubscriber
import com.tokopedia.cart.view.uimodel.CartBundlingBottomSheetData
import com.tokopedia.cart.view.uimodel.CartGroupHolderData
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cart.view.uimodel.CartRecentViewItemHolderData
import com.tokopedia.cart.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.cart.view.uimodel.CartShopGroupTickerState
import com.tokopedia.cart.view.uimodel.CartWishlistItemHolderData
import com.tokopedia.cart.view.uimodel.PromoSummaryData
import com.tokopedia.cart.view.uimodel.PromoSummaryDetailData
import com.tokopedia.cartcommon.data.request.updatecart.BundleInfo
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UndoDeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.toZeroStringIfNullOrBlank
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.logisticcart.shipping.model.Product
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.ShippingParam
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.productbundlewidget.model.BundleDetailUiModel
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceAdd
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCartMapData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCheckout
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceRecomProductCartMapData
import com.tokopedia.purchase_platform.common.constant.CartConstant.QTY_ADDON_REPLACE
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoOrder
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoOrderData
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
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
) : ICartListPresenter, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + dispatchers.immediate

    private var view: ICartListView? = null

    private var cartListData: CartData? = null
    private var summaryTransactionUiModel: SummaryTransactionUiModel? = null
    private var summariesAddOnUiModel: HashMap<Int, String> = hashMapOf()
    private var promoSummaryUiModel: PromoSummaryData? = null

    private var hasPerformChecklistChange: Boolean = false

    // Store last validate use response from promo page
    private var lastValidateUseResponse: ValidateUsePromoRevampUiModel? = null

    // Store last validate use response from cart page
    private var lastUpdateCartAndGetLastApplyResponse: UpdateAndGetLastApplyData? = null
    var isLastApplyResponseStillValid = true

    // Store last validate use request
    private var lastValidateUseRequest: ValidateUsePromoRequest? = null

    // Store promo ticker
    private var promoTicker: CartPromoTicker = CartPromoTicker()

    // Store flag show choose promo widget
    private var showChoosePromoWidget: Boolean = false

    // Store LCA data for bo affordability
    private var lca: LocalCacheModel? = null

    // Store last cart shop group ticker cart string for debounce handling
    private var lastCartShopGroupTickerCartString: String = ""

    // Cart shop ticker debounce job
    private var cartShopGroupTickerJob: Job? = null

    private var totalQtyWithAddon: Int = 0

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
        private const val SOURCE_CART = "cart"

        private const val MAX_TOTAL_AMOUNT_ELIGIBLE_FOR_COD = 1000000.0

        private const val STATUS_OK = "OK"
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

    override fun getSummaryTransactionUiModel(selectedCartItemData: List<CartItemHolderData>): SummaryTransactionUiModel? {
        val updatedAddOnSummary = cartListData?.shoppingSummary?.summaryAddOnList?.let {
            CartUiModelMapper.mapSummariesAddOnsFromSelectedItems(it, selectedCartItemData)
        }
        if (updatedAddOnSummary != null) {
            var totalAddOnPrice = 0L
            for (entry in updatedAddOnSummary) {
                totalAddOnPrice += entry.priceValue.toLong()
            }
            summaryTransactionUiModel?.listSummaryAddOns = updatedAddOnSummary
        }
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

            launch {
                try {
                    val cartData = getCartRevampV4UseCase(GetCartParam(cartId, getCartState))
                    onSuccessGetCartList(cartData, initialLoad)
                } catch (t: Throwable) {
                    onErrorGetCartList(t, initialLoad)
                }
            }
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
            setUpdateCartAndGetLastApplyLastResponse(null)
            if (!initialLoad) {
                it.hideProgressLoading()
            }
            setCartListData(cartData)
            summaryTransactionUiModel = CartUiModelMapper.mapSummaryTransactionUiModel(cartData)
            summariesAddOnUiModel = CartUiModelMapper.getShoppingSummaryAddOns(cartData.shoppingSummary.summaryAddOnList)
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
                onError = { throwable ->
                    onErrorDeleteCartItems(forceExpandCollapsedUnavailableItems, throwable)
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

            val params = view.generateGeneralParamGetLastApply()
            if (!removeAllItems && (view.checkHitValidateUseIsNeeded(params))) {
                view.showPromoCheckoutStickyButtonLoading()
                doUpdateCartAndGetLastApply(params)
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
                onError = { throwable ->
                    onErrorUndoDeleteCartItem(throwable)
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
                it.getAllSelectedCartDataList()
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
        val cartShopHolderDataList = view?.getAllGroupDataList()

        if ((cartShopHolderDataList?.size ?: 0) == 1) {
            cartShopHolderDataList?.get(0)?.productUiModelList?.let {
                for (cartItemHolderData in it) {
                    if (!cartItemHolderData.isSelected) {
                        checklistCondition = ITEM_CHECKED_PARTIAL_ITEM
                        break
                    }
                }
            }
        } else if ((cartShopHolderDataList?.size ?: 0) > 1) {
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
        return totalAmount <= MAX_TOTAL_AMOUNT_ELIGIBLE_FOR_COD
    }

    override fun processToUpdateAndReloadCartData(cartId: String, getCartState: Int) {
        view?.let { cartListView ->
            launch(dispatchers.io) {
                val cartItemDataList = ArrayList<CartItemHolderData>()
                for (data in cartListView.getAllAvailableCartDataList()) {
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
                        val updateAndReloadCartListData = updateAndReloadCartUseCase(updateCartWrapperRequest)
                        view?.hideProgressLoading()
                        processInitialGetCartData(
                            updateAndReloadCartListData.cartId,
                            initialLoad = false,
                            isLoadingTypeRefresh = true,
                            updateAndReloadCartListData.getCartState
                        )
                    } catch (t: Throwable) {
                        view?.hideProgressLoading()
                        view?.showToastMessageRed(t)
                    }
                } else {
                    cartListView.hideProgressLoading()
                }
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

    override fun reCalculateSubTotal(dataList: List<CartGroupHolderData>) {
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
        // update summary addons
        var totalAddonPrice = 0.0
        for ((key, value) in summariesAddOnUiModel) {
            summaryTransactionUiModel?.listSummaryAddOns?.forEach {
                if (it.type == key) {
                    it.qty = totalQtyWithAddon
                    it.wording = value.replace(QTY_ADDON_REPLACE, totalQtyWithAddon.toString())

                    totalAddonPrice = totalQtyWithAddon * it.priceValue
                    it.priceLabel = CurrencyFormatUtil.convertPriceValueToIdrFormat(totalAddonPrice, false).removeDecimalSuffix()
                }
            }
        }

        val priceAfterAddon = subtotalPrice + totalAddonPrice
        val priceAfterAddonBeforeSlashedPrice = subtotalBeforeSlashedPrice + totalAddonPrice

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
    }

    fun getAvailableCartItemDataListAndShopTotalWeight(cartGroupHolderData: CartGroupHolderData): Pair<ArrayList<CartItemHolderData>, Double> {
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
                totalQtyWithAddon = itemQty
                cartItemHolderData.addOnsProduct.listData.forEach {
                    subtotalPrice += (totalQtyWithAddon * it.price)
                    subtotalBeforeSlashedPrice += (totalQtyWithAddon * it.price)
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
            launch(dispatchers.io) {
                try {
                    val addCartToWishlistRequest = AddCartToWishlistRequest()
                    addCartToWishlistRequest.cartIds = listOf(cartId)
                    val data = addCartToWishlistUseCase(addCartToWishlistRequest)
                    withContext(dispatchers.main) {
                        view?.let { cartListView ->
                            if (data.status == STATUS_OK) {
                                if (data.success == 1) {
                                    cartListView.onAddCartToWishlistSuccess(data.message, productId, cartId, isLastItem, source, forceExpandCollapsedUnavailableItems)
                                } else {
                                    cartListView.showToastMessageRed(data.message)
                                }
                            } else {
                                cartListView.showToastMessageRed(data.message)
                            }
                        }
                    }
                } catch (t: Throwable) {
                    withContext(dispatchers.main) {
                        view?.let { cartListView ->
                            Timber.e(t)
                            cartListView.showToastMessageRed(t)
                        }
                    }
                }
            }
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

    override fun generateWishlistDataImpressionAnalytics(
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

    override fun generateRecentViewDataImpressionAnalytics(
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

    override fun generateCartBundlingPromotionsAnalyticsData(
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
            productsData,
            EnhancedECommerceActionFieldData.VALUE_SECTION_NAME_RECENT_VIEW_EMPTY_CART
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
            productsData,
            EnhancedECommerceActionFieldData.VALUE_SECTION_NAME_WISHLIST_EMPTY_CART
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
                cartItemHolderData.trackerAttribution.ifBlank {
                    EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                }
            )
            setDimension45(cartItemHolderData.cartId)
            setDimension54(cartItemHolderData.isFulfillment)
            setDimension53(cartItemHolderData.productOriginalPrice > 0)
            setShopId(cartItemHolderData.shopHolderData.shopId)
            setShopName(cartItemHolderData.shopHolderData.shopName)
            setShopType(cartItemHolderData.shopHolderData.shopTypeInfo.titleFmt)
            setDimension82(cartItemHolderData.categoryId)
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
            setDimension136(cartItemHolderData.cartString)
        }
        return enhancedECommerceProductCartMapData
    }

    // ANALYTICS ATC
    override fun generateAddToCartEnhanceEcommerceDataLayer(
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

    override fun generateAddToCartEnhanceEcommerceDataLayer(
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

    override fun generateAddToCartEnhanceEcommerceDataLayer(
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
                view?.let {
                    it.hideItemLoading()
                    if (recommendationWidgets.firstOrNull()?.recommendationItemList?.isNotEmpty() == true) {
                        it.renderRecentView(recommendationWidgets[0])
                    }
                    it.setHasTriedToLoadRecentView()
                    it.stopAllCartPerformanceTrace()
                }
            } catch (t: Throwable) {
                Timber.d(t)
                view?.setHasTriedToLoadRecentView()
                view?.stopAllCartPerformanceTrace()
            }
        }
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
                view?.let {
                    it.hideItemLoading()
                    if (recommendationWidgets[0].recommendationItemList.isNotEmpty()) {
                        it.renderRecommendation(recommendationWidgets[0])
                    }
                    it.setHasTriedToLoadRecommendation()
                    it.stopAllCartPerformanceTrace()
                }
            } catch (t: Throwable) {
                Timber.d(t)
                view?.hideItemLoading()
                view?.setHasTriedToLoadRecommendation()
                view?.stopAllCartPerformanceTrace()
            }
        }
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
            if (clickUrl.isNotEmpty() && productModel.isTopAds) {
                view?.sendATCTrackingURLRecent(
                    productModel
                )
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

        launch {
            try {
                addToCartUseCase.setParams(addToCartRequestParams)
                val addToCartDataModel = addToCartUseCase.executeOnBackground()
                view?.let { v ->
                    v.hideProgressLoading()
                    if (addToCartDataModel.status.equals(
                            AddToCartDataModel.STATUS_OK,
                            true
                        ) && addToCartDataModel.data.success == 1
                    ) {
                        v.triggerSendEnhancedEcommerceAddToCartSuccess(
                            addToCartDataModel,
                            productModel
                        )
                        v.resetRecentViewList()
                        processInitialGetCartData("0", false, false)
                        if (addToCartDataModel.data.message.size > 0) {
                            v.showToastMessageGreen(addToCartDataModel.data.message[0])
                            v.notifyBottomCartParent()
                        }
                    } else {
                        if (addToCartDataModel.errorMessage.size > 0) {
                            v.showToastMessageRed(addToCartDataModel.errorMessage[0])
                        }
                    }
                }
            } catch (t: Throwable) {
                view?.hideProgressLoading()
                view?.showToastMessageRed(t)
            }
        }
    }

    override fun processAddToCartExternal(productId: Long) {
        view?.showProgressLoading()

        launch(dispatchers.io) {
            try {
                val model = addToCartExternalUseCase(Pair(productId.toString(), userSessionInterface.userId))
                withContext(dispatchers.main) {
                    view?.let { cartListView ->
                        cartListView.hideProgressLoading()
                        if (model.message.isNotEmpty()) {
                            cartListView.showToastMessageGreen(model.message[0])
                        }
                        cartListView.refreshCartWithSwipeToRefresh()
                    }
                }
            } catch (t: Throwable) {
                Timber.d(t)
                withContext(dispatchers.main) {
                    view?.let {
                        it.hideProgressLoading()
                        it.showToastMessageRed(t)
                        it.refreshCartWithSwipeToRefresh()
                    }
                }
            }
        }
    }

    override fun redirectToLite(url: String) {
        view?.let {
            it.showProgressLoading()
            val adsId = it.getAdsId()
            if (adsId.trim { c -> c <= ' ' }.isNotEmpty()) {
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

    override fun doUpdateCartAndGetLastApply(promoRequest: ValidateUsePromoRequest) {
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
                launch(dispatchers.io) {
                    try {
                        lastValidateUseRequest = promoRequest
                        val updateCartWrapperRequest = UpdateCartWrapperRequest(
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
                                        setLastApplyNotValid()
                                        setValidateUseLastResponse(
                                            ValidateUsePromoRevampUiModel(
                                                promoUiModel = promoUiModel
                                            )
                                        )
                                        setUpdateCartAndGetLastApplyLastResponse(
                                            updateCartDataResponse
                                        )
                                        view?.updatePromoCheckoutStickyButton(promoUiModel)
                                    }
                                }
                            }
                        }
                    } catch (t: Throwable) {
                        withContext(dispatchers.main) {
                            if (t is AkamaiErrorException) {
                                doClearAllPromo()
                                if (!promoTicker.enable) {
                                    view?.showToastMessageRed(t)
                                }
                            }
                            view?.renderPromoCheckoutButtonActiveDefault(emptyList())
                        }
                    }
                }
            } else {
                cartListView.hideProgressLoading()
            }
        }
    }

    override fun doClearRedPromosBeforeGoToCheckout(clearPromoRequest: ClearPromoRequest) {
        view?.showItemLoading()
        launch {
            try {
                clearCacheAutoApplyStackUseCase.setParams(clearPromoRequest).executeOnBackground()
                view?.hideProgressLoading()
                view?.onSuccessClearRedPromosThenGoToCheckout()
            } catch (t: Throwable) {
                Timber.d(t)
                view?.hideProgressLoading()
                view?.onSuccessClearRedPromosThenGoToCheckout()
            }
        }
    }

    override fun doClearRedPromosBeforeGoToPromo(clearPromoRequest: ClearPromoRequest) {
        view?.showItemLoading()
        launch {
            try {
                clearCacheAutoApplyStackUseCase.setParams(clearPromoRequest).executeOnBackground()
                view?.hideProgressLoading()
                view?.onSuccessClearRedPromosThenGoToPromo()
            } catch (t: Throwable) {
                Timber.d(t)
                view?.hideProgressLoading()
                view?.onSuccessClearRedPromosThenGoToPromo()
            }
        }
    }

    override fun doClearAllPromo() {
        lastValidateUseRequest?.let {
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

    override fun getUpdateCartAndGetLastApplyLastResponse(): UpdateAndGetLastApplyData? {
        return lastUpdateCartAndGetLastApplyResponse
    }

    override fun setUpdateCartAndGetLastApplyLastResponse(response: UpdateAndGetLastApplyData?) {
        lastUpdateCartAndGetLastApplyResponse = response
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
        launchCatchError(dispatchers.io, block = {
            setCartlistCheckboxStateUseCase(cartItemDataList)
        }, onError = {})
    }

    override fun followShop(shopId: String) {
        view?.showProgressLoading()
        launch(dispatchers.io) {
            try {
                val data = followShopUseCase(shopId)
                withContext(dispatchers.main) {
                    view?.let {
                        it.hideProgressLoading()
                        it.showToastMessageGreen(data.followShop?.message ?: "")
                        processInitialGetCartData("0", false, false)
                    }
                }
            } catch (t: Throwable) {
                withContext(dispatchers.main) {
                    view?.let {
                        Timber.e(t)
                        it.hideProgressLoading()
                        it.showToastMessageRed(t)
                    }
                }
            }
        }
    }

    override fun setLocalizingAddressData(lca: LocalCacheModel?) {
        this.lca = lca
    }

    override fun checkCartShopGroupTicker(cartGroupHolderData: CartGroupHolderData) {
        if (lastCartShopGroupTickerCartString == cartGroupHolderData.cartString) {
            cartShopGroupTickerJob?.cancel()
        }
        lastCartShopGroupTickerCartString = cartGroupHolderData.cartString
        cartShopGroupTickerJob = launch(dispatchers.io) {
            try {
                delay(CART_SHOP_GROUP_TICKER_DELAY)
                cartGroupHolderData.cartShopGroupTicker.enableBundleCrossSell = checkEnableBundleCrossSell(cartGroupHolderData)
                if (!cartGroupHolderData.cartShopGroupTicker.enableBoAffordability &&
                    !cartGroupHolderData.cartShopGroupTicker.enableBundleCrossSell
                ) {
                    cartGroupHolderData.cartShopGroupTicker.state = CartShopGroupTickerState.EMPTY
                    withContext(dispatchers.main) {
                        view?.updateCartShopGroupTicker(cartGroupHolderData)
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
                    cartGroupHolderData.cartShopGroupTicker.state =
                        CartShopGroupTickerState.FAILED
                    withContext(dispatchers.main) {
                        view?.updateCartShopGroupTicker(cartGroupHolderData)
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
                            it.isFreeShippingExtra,
                            it.shopHolderData.shopId.toLongOrZero()
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
                    view?.updateCartShopGroupTicker(cartGroupHolderData)
                }
            } catch (t: Throwable) {
                if (t !is CancellationException) {
                    cartGroupHolderData.cartShopGroupTicker.tickerText = ""
                    cartGroupHolderData.cartShopGroupTicker.state = CartShopGroupTickerState.FAILED
                    withContext(dispatchers.main) {
                        view?.updateCartShopGroupTicker(cartGroupHolderData)
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
        launch {
            try {
                clearCacheAutoApplyStackUseCase.setParams(
                    ClearPromoRequest(
                        ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE,
                        orderData = clearPromoOrderData
                    )
                ).executeOnBackground()
            } catch (t: Throwable) {
                Timber.d(t)
            }
        }
    }

    override fun validateBoPromo(validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel) {
        val groupDataList = view?.getAllGroupDataList()
        if (groupDataList != null) {
            val boGroupUniqueIds = mutableSetOf<String>()
            for (voucherOrderUiModel in validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels) {
                if (voucherOrderUiModel.shippingId > 0 && voucherOrderUiModel.spId > 0 && voucherOrderUiModel.type == "logistic") {
                    if (voucherOrderUiModel.messageUiModel.state == "green") {
                        groupDataList.firstOrNull { it.cartString == voucherOrderUiModel.cartStringGroup }?.apply {
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
    }

    private fun clearBo(group: CartGroupHolderData) {
        launch {
            try {
                val cartStringGroupSet = mutableSetOf<String>()
                val cartPromoHolderData = PromoRequestMapper.mapSelectedCartGroupToPromoData(listOf(group))
                clearCacheAutoApplyStackUseCase.setParams(
                    ClearPromoRequest(
                        serviceId = ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE,
                        orderData = ClearPromoOrderData(
                            orders = cartPromoHolderData.values.map {
                                val isNoCodeExistInCurrentGroup = !cartStringGroupSet.contains(it.cartStringGroup)
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
            } catch (t: Throwable) {
                Timber.d(t)
            }
        }

        group.promoCodes = ArrayList(group.promoCodes).apply { remove(group.boCode) }
        group.boCode = ""
    }

    override fun checkEnableBundleCrossSell(cartGroupHolderData: CartGroupHolderData): Boolean {
        val hasCheckedProductWithBundle = cartGroupHolderData.productUiModelList
            .any { it.isSelected && !it.isBundlingItem && it.bundleIds.isNotEmpty() }
        val hasCheckedBundleProduct = cartGroupHolderData.productUiModelList
            .any { it.isSelected && it.isBundlingItem && it.bundleIds.isNotEmpty() }
        return cartGroupHolderData.cartShopGroupTicker.enableCartAggregator &&
            hasCheckedProductWithBundle && !hasCheckedBundleProduct
    }

    private fun syncCartGroupShopBoCodeWithPromoUiModel(promoUiModel: PromoUiModel) {
        view?.let { cartListView ->
            val groupDataList = cartListView.getAllGroupDataList()
            promoUiModel.voucherOrderUiModels.forEach { voucherOrder ->
                if (
                    voucherOrder.shippingId > 0 && voucherOrder.spId > 0 && voucherOrder.type == "logistic" && voucherOrder.messageUiModel.state == "green"
                ) {
                    groupDataList.firstOrNull { it.cartString == voucherOrder.cartStringGroup }?.apply {
                        boCode = voucherOrder.code
                    }
                }
            }
        }
    }
}
