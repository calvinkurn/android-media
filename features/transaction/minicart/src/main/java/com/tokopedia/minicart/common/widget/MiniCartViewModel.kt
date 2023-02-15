package com.tokopedia.minicart.common.widget

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.data.model.request.AddToCartBundleRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiCartParam
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartBundleModel
import com.tokopedia.atc_common.domain.model.response.AddToCartOccMultiDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartBundleUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.cartcommon.data.request.updatecart.BundleInfo
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.undodeletecart.UndoDeleteCartDataResponse
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.data.RemoveFromCartDomainModel
import com.tokopedia.cartcommon.domain.data.UndoDeleteCartDomainModel
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UndoDeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.minicart.cartlist.MiniCartListBottomSheet.Companion.STATE_PRODUCT_BUNDLE_RECOM_ATC
import com.tokopedia.minicart.cartlist.MiniCartListUiModelMapper
import com.tokopedia.minicart.cartlist.uimodel.MiniCartAccordionUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartListUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProductBundleRecomShimmeringUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProductUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartTickerErrorUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartTickerWarningUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartUnavailableHeaderUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartUnavailableReasonUiModel
import com.tokopedia.minicart.chatlist.MiniCartChatListUiModelMapper
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.data.response.minicartlist.BeliButtonConfig
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.data.tracker.ProductBundleRecomTracker
import com.tokopedia.minicart.common.domain.data.MiniCartABTestData
import com.tokopedia.minicart.common.domain.data.MiniCartCheckoutData
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.ProductBundleRecomResponse
import com.tokopedia.minicart.common.domain.data.getMiniCartItemBundleGroup
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListUseCase
import com.tokopedia.minicart.common.domain.usecase.GetProductBundleRecomUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeBundleProductUiModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleItemUiModel
import com.tokopedia.user.session.UserSessionInterface
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

class MiniCartViewModel @Inject constructor(
    private val executorDispatchers: CoroutineDispatchers,
    private val getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase,
    private val getMiniCartListUseCase: GetMiniCartListUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val undoDeleteCartUseCase: UndoDeleteCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val getProductBundleRecomUseCase: GetProductBundleRecomUseCase,
    private val addToCartBundleUseCase: AddToCartBundleUseCase,
    private val addToCartOccMultiUseCase: AddToCartOccMultiUseCase,
    private val miniCartListUiModelMapper: MiniCartListUiModelMapper,
    private val miniCartChatListUiModelMapper: MiniCartChatListUiModelMapper,
    private val userSession: UserSessionInterface
) :
    BaseViewModel(executorDispatchers.main) {

    companion object {
        const val TEMPORARY_PARENT_ID_PREFIX = "tmp_"
        const val DEFAULT_PERCENTAGE = 100.0
        const val DEFAULT_WEIGHT = 1000.0f
    }

    // Global Data
    private val _currentShopIds = MutableLiveData<List<String>>()
    val currentShopIds: LiveData<List<String>>
        get() = _currentShopIds
    var currentSource: MiniCartSource = MiniCartSource.TokonowHome

    private val _currentPage = MutableLiveData<MiniCartAnalytics.Page>()
    val currentPage: LiveData<MiniCartAnalytics.Page>
        get() = _currentPage

    private val _miniCartABTestData = MutableLiveData<MiniCartABTestData>()
    val miniCartABTestData: LiveData<MiniCartABTestData>
        get() = _miniCartABTestData

    // Widget DATA
    private val _globalEvent = MutableLiveData<GlobalEvent>()
    val globalEvent: LiveData<GlobalEvent>
        get() = _globalEvent

    private val _miniCartSimplifiedData = MutableLiveData<MiniCartSimplifiedData>()
    val miniCartSimplifiedData: LiveData<MiniCartSimplifiedData>
        get() = _miniCartSimplifiedData

    // Bottom Sheet Data
    private val _miniCartListBottomSheetUiModel = MutableLiveData<MiniCartListUiModel>()
    val miniCartListBottomSheetUiModel: LiveData<MiniCartListUiModel>
        get() = _miniCartListBottomSheetUiModel

    // Bottom Sheet Chat Data
    private val _miniCartChatListBottomSheetUiModel = MutableLiveData<MiniCartListUiModel>()
    val miniCartChatListBottomSheetUiModel: LiveData<MiniCartListUiModel>
        get() = _miniCartChatListBottomSheetUiModel

    // Product Bundle Recommendation Data for Tracking Purpose Only
    private val _productBundleRecomTracker = MutableLiveData<ProductBundleRecomTracker>()
    val productBundleRecomTracker: LiveData<ProductBundleRecomTracker>
        get() = _productBundleRecomTracker

    val tmpHiddenUnavailableItems = mutableListOf<Visitable<*>>()

    var lastDeletedProductItems: List<MiniCartProductUiModel>? = null
        private set

    // Used for mocking _miniCartListBottomSheetUiModel value.
    // Should only be called from unit test.
    fun setMiniCartListUiModel(miniCartListUiModel: MiniCartListUiModel) {
        _miniCartListBottomSheetUiModel.value = miniCartListUiModel
    }

    // Used for mocking _miniCartSimplifiedData value.
    // Should only be called from unit test.
    fun setMiniCartSimplifiedData(miniCartSimplifiedData: MiniCartSimplifiedData) {
        _miniCartSimplifiedData.value = miniCartSimplifiedData
    }

    // Used for mocking lastDeletedProductItem value.
    // Should only be called from unit test.
    fun setLastDeleteProductItems(miniCartProductUiModels: List<MiniCartProductUiModel>) {
        lastDeletedProductItems = miniCartProductUiModels
    }

    // Setter & Getter
    // Some of setter & getter here added to reduce complexity (due to nullability) so we can increase unit test coverage

    fun initializeCurrentPage(currentPage: MiniCartAnalytics.Page) {
        _currentPage.value = currentPage
    }

    fun initializeShopIds(shopIds: List<String>) {
        _currentShopIds.value = shopIds
    }

    fun initializeGlobalState() {
        _globalEvent.value = GlobalEvent()
    }

    fun updateMiniCartSimplifiedData(miniCartSimplifiedData: MiniCartSimplifiedData) {
        _miniCartSimplifiedData.value = miniCartSimplifiedData
    }

    fun getLatestMiniCartData(): MiniCartSimplifiedData {
        return miniCartListUiModelMapper.reverseMapUiModel(
            miniCartListBottomSheetUiModel.value,
            tmpHiddenUnavailableItems
        )
    }

    fun resetTemporaryHiddenUnavailableItems() {
        tmpHiddenUnavailableItems.clear()
    }

    private fun getVisitables(): MutableList<Visitable<*>> {
        return miniCartListBottomSheetUiModel.value?.visitables ?: mutableListOf()
    }

    private fun getShopIds(): List<String> {
        return currentShopIds.value ?: emptyList()
    }

    private fun getMiniCartItems(): List<MiniCartItem> {
        return miniCartSimplifiedData.value?.miniCartItems?.values?.toList() ?: emptyList()
    }

    private fun updateVisitables(visitables: MutableList<Visitable<*>>) {
        miniCartListBottomSheetUiModel.value?.visitables = visitables
        _miniCartListBottomSheetUiModel.value = miniCartListBottomSheetUiModel.value
    }

    private fun updateVisitablesBackgroundState(visitables: MutableList<Visitable<*>>) {
        miniCartListBottomSheetUiModel.value?.visitables = visitables
        _miniCartListBottomSheetUiModel.postValue(miniCartListBottomSheetUiModel.value)
    }

    fun setMiniCartABTestData(isOCCFlow: Boolean, buttonBuyWording: String) {
        _miniCartABTestData.value = MiniCartABTestData(
            isOCCFlow = isOCCFlow,
            buttonBuyWording = buttonBuyWording
        )
    }

    // API Call & Callback

    fun getLatestWidgetState(shopIds: List<String>? = null) {
        if (shopIds != null) {
            initializeShopIds(shopIds)
            getMiniCartListSimplifiedUseCase.setParams(shopIds, currentSource)
        } else {
            val tmpShopIds = getShopIds()
            getMiniCartListSimplifiedUseCase.setParams(tmpShopIds, currentSource)
        }
        getMiniCartListSimplifiedUseCase.execute(
            onSuccess = {
                setMiniCartABTestData(
                    isOCCFlow = it.miniCartWidgetData.isOCCFlow,
                    buttonBuyWording = it.miniCartWidgetData.buttonBuyWording
                )
                _miniCartSimplifiedData.value = it
            },
            onError = {
                if (miniCartSimplifiedData.value != null) {
                    _miniCartSimplifiedData.value = miniCartSimplifiedData.value
                } else {
                    _miniCartSimplifiedData.value = MiniCartSimplifiedData()
                }
            }
        )
    }

    fun getCartList(isFirstLoad: Boolean = false) {
        val shopIds = getShopIds()
        getMiniCartListUseCase.setParams(shopIds)
        getMiniCartListUseCase.execute(
            onSuccess = {
                setMiniCartABTestData(
                    isOCCFlow = it.data.beliButtonConfig.buttonType == BeliButtonConfig.BUTTON_TYPE_OCC,
                    buttonBuyWording = it.data.beliButtonConfig.buttonWording
                )
                onSuccessGetCartList(it, isFirstLoad)
            },
            onError = {
                onErrorGetCartList(isFirstLoad, it)
            }
        )
    }

    private fun getProductBundleRecommendation(
        miniCartListUiModel: MiniCartListUiModel
    ) {
        launchCatchError(context = executorDispatchers.io, block = {
            showProductBundleRecomShimmering(miniCartListUiModel)

            val response = getProductBundleRecomUseCase.execute(
                productIds = miniCartListUiModel.availableProductIds,
                excludeBundleIds = miniCartListUiModel.availableBundleIds
            )

            showProductBundleRecom(
                miniCartListUiModel,
                response.tokonowBundleWidget.data.widgetData,
                response
            )
        }, onError = {
                hideProductBundleRecomShimmering(miniCartListUiModel)
            })
    }

    private fun showProductBundleRecomShimmering(miniCartListUiModel: MiniCartListUiModel) {
        miniCartListUiModel.visitables.add(MiniCartProductBundleRecomShimmeringUiModel())
        updateVisitablesBackgroundState(miniCartListUiModel.visitables)
    }

    private fun showProductBundleRecom(
        miniCartListUiModel: MiniCartListUiModel,
        widgetData: List<ProductBundleRecomResponse.TokonowBundleWidget.Data.WidgetData>,
        response: ProductBundleRecomResponse
    ) {
        if (widgetData.isNotEmpty()) {
            val productBundleRecom = miniCartListUiModelMapper.mapToProductBundleUiModel(response)
            miniCartListUiModel.visitables.removeFirst { it is MiniCartProductBundleRecomShimmeringUiModel }
            miniCartListUiModel.visitables.add(productBundleRecom)
            updateVisitablesBackgroundState(miniCartListUiModel.visitables)
        } else {
            hideProductBundleRecomShimmering(miniCartListUiModel)
        }
    }

    private fun hideProductBundleRecomShimmering(miniCartListUiModel: MiniCartListUiModel) {
        miniCartListUiModel.visitables.removeFirst { it is MiniCartProductBundleRecomShimmeringUiModel }
        updateVisitablesBackgroundState(miniCartListUiModel.visitables)
    }

    fun trackProductBundleRecom(
        shopId: String,
        warehouseId: String,
        bundleId: String,
        bundleName: String,
        bundleType: String,
        bundlePosition: Int,
        priceCut: String,
        state: String
    ) {
        _productBundleRecomTracker.value = ProductBundleRecomTracker(
            shopId = shopId,
            warehouseId = warehouseId,
            bundleId = bundleId,
            bundleName = bundleName,
            bundleType = bundleType,
            bundlePosition = bundlePosition,
            priceCut = priceCut,
            state = state
        )
    }

    fun addBundleToCart(
        shopId: String,
        warehouseId: String,
        bundleId: String,
        bundleName: String,
        bundleType: String,
        bundlePosition: Int,
        priceCut: String,
        productDetails: List<ShopHomeBundleProductUiModel>,
        productQuantity: Int
    ) {
        launchCatchError(context = executorDispatchers.io, block = {
            val productDetailsParam =
                miniCartListUiModelMapper.mapToAddToCartBundleProductDetailParam(
                    productDetails = productDetails,
                    quantity = productQuantity,
                    shopId = shopId,
                    userId = userSession.userId
                )

            val atcBundleParams = AddToCartBundleRequestParams(
                shopId = shopId,
                bundleId = bundleId,
                bundleQty = ShopHomeProductBundleItemUiModel.DEFAULT_BUNDLE_QUANTITY,
                selectedProductPdp = ShopHomeProductBundleItemUiModel.DEFAULT_BUNDLE_PRODUCT_PARENT_ID,
                productDetails = productDetailsParam
            )

            addToCartBundleUseCase.setParams(atcBundleParams)
            val response = addToCartBundleUseCase.executeOnBackground()

            validateBundleResponse(
                shopId = shopId,
                warehouseId = warehouseId,
                response = response,
                bundleId = bundleId,
                bundleName = bundleName,
                bundleType = bundleType,
                bundlePosition = bundlePosition,
                priceCut = priceCut,
                productDetails = productDetails
            )
        }) { throwable ->
            _globalEvent.postValue(
                GlobalEvent(
                    state = GlobalEvent.STATE_FAILED_ADD_TO_CART_BUNDLE_RECOM_ITEM,
                    throwable = throwable
                )
            )
        }
    }

    private fun validateBundleResponse(
        shopId: String,
        warehouseId: String,
        response: AddToCartBundleModel,
        bundleId: String,
        bundleName: String,
        bundleType: String,
        bundlePosition: Int,
        priceCut: String,
        productDetails: List<ShopHomeBundleProductUiModel>
    ) {
        response.validateResponse(
            onSuccess = {
                _globalEvent.postValue(
                    GlobalEvent(
                        state = GlobalEvent.STATE_SUCCESS_ADD_TO_CART_BUNDLE_RECOM_ITEM
                    )
                )

                val products = response.addToCartBundleDataModel.data
                _productBundleRecomTracker.postValue(
                    ProductBundleRecomTracker(
                        shopId = shopId,
                        warehouseId = warehouseId,
                        bundleId = bundleId,
                        bundleName = bundleName,
                        bundleType = bundleType,
                        bundlePosition = bundlePosition,
                        priceCut = priceCut,
                        atcItems = miniCartListUiModelMapper.mapToProductBundlRecomAtcItemTracker(
                            productList = products,
                            productDetails = productDetails
                        ),
                        state = STATE_PRODUCT_BUNDLE_RECOM_ATC
                    )
                )
            },
            onFailedWithMessages = { errorMessages ->
                _globalEvent.postValue(
                    GlobalEvent(
                        state = GlobalEvent.STATE_FAILED_ADD_TO_CART_BUNDLE_RECOM_ITEM,
                        data = errorMessages.joinToString(separator = ", ")
                    )
                )
            },
            onFailedWithException = { throwable ->
                _globalEvent.postValue(
                    GlobalEvent(
                        state = GlobalEvent.STATE_FAILED_ADD_TO_CART_BUNDLE_RECOM_ITEM,
                        throwable = throwable
                    )
                )
            }
        )
    }

    private fun onSuccessGetCartList(miniCartData: MiniCartData, isFirstLoad: Boolean) {
        if (isFirstLoad && miniCartData.data.outOfService.id.isNotBlank() && miniCartData.data.outOfService.id != "0") {
            _globalEvent.value = GlobalEvent(
                state = GlobalEvent.STATE_FAILED_LOAD_MINI_CART_LIST_BOTTOM_SHEET,
                data = miniCartData
            )
        } else {
            val tmpMiniCartListUiModel = miniCartListUiModelMapper.mapUiModel(miniCartData)
            val tmpMiniCartChatListUiModel = miniCartChatListUiModelMapper.mapUiModel(miniCartData)

            getProductBundleRecommendation(tmpMiniCartListUiModel)

            tmpMiniCartListUiModel.isFirstLoad = isFirstLoad
            tmpMiniCartChatListUiModel.isFirstLoad = isFirstLoad
            tmpMiniCartListUiModel.needToCalculateAfterLoad = true

            _miniCartListBottomSheetUiModel.value = tmpMiniCartListUiModel
            _miniCartChatListBottomSheetUiModel.value = tmpMiniCartChatListUiModel
        }
    }

    private fun onErrorGetCartList(isFirstLoad: Boolean, throwable: Throwable) {
        if (isFirstLoad) {
            _globalEvent.value = GlobalEvent(
                state = GlobalEvent.STATE_FAILED_LOAD_MINI_CART_LIST_BOTTOM_SHEET,
                throwable = throwable
            )
        }
    }

    fun deleteSingleCartItem(product: MiniCartProductUiModel) {
        deleteCartUseCase.setParams(listOf(product.cartId))
        deleteCartUseCase.execute(
            onSuccess = {
                onSuccessDeleteSingleCartItem(product, it)
            },
            onError = {
                onErrorDeleteSingleCartItem(it)
            }
        )
    }

    fun deleteMultipleCartItems(
        products: List<MiniCartProductUiModel>,
        isFromEditBundle: Boolean = false
    ) {
        deleteCartUseCase.setParams(products.map { it.cartId })
        deleteCartUseCase.execute(
            onSuccess = {
                onSuccessDeleteMultipleCartItems(products, it, isFromEditBundle)
            },
            onError = {
                onErrorDeleteMultipleCartItems(it, isFromEditBundle)
            }
        )
    }

    private fun onSuccessDeleteSingleCartItem(
        product: MiniCartProductUiModel,
        removeFromCartData: RemoveFromCartData
    ) {
        val visitables = getVisitables()
        val tmpVisitables = getVisitables()
        loop@ for ((index, visitable) in visitables.withIndex()) {
            if (visitable is MiniCartProductUiModel && visitable.cartId == product.cartId) {
                val deletedItem = visitables[index] as MiniCartProductUiModel
                lastDeletedProductItems = listOf(deletedItem)

                tmpVisitables.removeAt(index)

                updateVisitables(tmpVisitables)

                var isLastItem = true
                innerLoop@ for (item in tmpVisitables) {
                    if (item is MiniCartProductUiModel) {
                        isLastItem = false
                        break@innerLoop
                    }
                }

                _globalEvent.value = GlobalEvent(
                    state = GlobalEvent.STATE_SUCCESS_DELETE_CART_ITEM,
                    data = RemoveFromCartDomainModel(
                        removeFromCartData = removeFromCartData,
                        isLastItem = isLastItem,
                        isBulkDelete = false
                    )
                )
                break@loop
            }
        }
    }

    private fun onErrorDeleteSingleCartItem(throwable: Throwable) {
        _globalEvent.value = GlobalEvent(
            state = GlobalEvent.STATE_FAILED_DELETE_CART_ITEM,
            throwable = throwable
        )
    }

    private fun onSuccessDeleteMultipleCartItems(
        products: List<MiniCartProductUiModel>,
        removeFromCartData: RemoveFromCartData,
        isFromEditBundle: Boolean
    ) {
        val visitables = getVisitables()
        val tmpVisitables = getVisitables().toMutableList()
        val deletedItems = arrayListOf<MiniCartProductUiModel>()
        tmpVisitables.removeAll { visitable ->
            if (visitable is MiniCartProductUiModel && products.find { it.cartId == visitable.cartId } != null) {
                deletedItems.add(visitable)
                return@removeAll true
            }
            return@removeAll false
        }
        if (tmpVisitables.size < visitables.size) {
            val isLastItem = tmpVisitables.find { it is MiniCartProductUiModel } == null
            updateVisitables(tmpVisitables)
            lastDeletedProductItems = deletedItems
            _globalEvent.value = GlobalEvent(
                state = GlobalEvent.STATE_SUCCESS_DELETE_CART_ITEM,
                data = RemoveFromCartDomainModel(
                    removeFromCartData = removeFromCartData,
                    isLastItem = isLastItem && !isFromEditBundle,
                    isBulkDelete = false,
                    isFromEditBundle = isFromEditBundle
                )
            )
        }
    }

    private fun onErrorDeleteMultipleCartItems(throwable: Throwable, isFromEditBundle: Boolean) {
        _globalEvent.value = GlobalEvent(
            state = GlobalEvent.STATE_FAILED_DELETE_CART_ITEM,
            throwable = throwable
        )
    }

    fun bulkDeleteUnavailableCartItems() {
        val unavailableItemsCartId = mutableListOf<String>()
        val visitables = getVisitables()
        visitables.forEach {
            if (it is MiniCartProductUiModel && it.isProductDisabled) {
                unavailableItemsCartId.add(it.cartId)
            }
        }
        tmpHiddenUnavailableItems.forEach {
            if (it is MiniCartProductUiModel && it.isProductDisabled) {
                unavailableItemsCartId.add(it.cartId)
            }
        }

        val tmpVisitables = getVisitables()
        var isLastItem = true
        loop@ for (item in tmpVisitables) {
            if (item is MiniCartProductUiModel && !item.isProductDisabled) {
                isLastItem = false
                break@loop
            }
        }

        deleteCartUseCase.setParams(unavailableItemsCartId)
        deleteCartUseCase.execute(
            onSuccess = {
                onSuccessBulkDeleteUnavailableCartItems(it, isLastItem)
            },
            onError = {
                onErrorBulkDeleteUnavailableCartItems(it)
            }
        )
    }

    private fun onSuccessBulkDeleteUnavailableCartItems(
        removeFromCartData: RemoveFromCartData,
        isLastItem: Boolean
    ) {
        if (isLastItem) {
            // manual delete items when delete last item to directly update widget
            val visitables = getVisitables()
            val tmpVisitables = mutableListOf<Visitable<*>>()
            tmpVisitables.addAll(visitables)
            tmpVisitables.removeAll {
                it is MiniCartUnavailableHeaderUiModel || it is MiniCartUnavailableReasonUiModel ||
                    it is MiniCartProductUiModel
            }
            updateVisitables(tmpVisitables)
            tmpHiddenUnavailableItems.clear()
        }
        _globalEvent.value = GlobalEvent(
            state = GlobalEvent.STATE_SUCCESS_DELETE_CART_ITEM,
            data = RemoveFromCartDomainModel(
                removeFromCartData = removeFromCartData,
                isLastItem = isLastItem,
                isBulkDelete = true
            )
        )
    }

    private fun onErrorBulkDeleteUnavailableCartItems(throwable: Throwable) {
        _globalEvent.value = GlobalEvent(
            state = GlobalEvent.STATE_FAILED_DELETE_CART_ITEM,
            throwable = throwable
        )
    }

    fun undoDeleteCartItem(isLastItem: Boolean) {
        lastDeletedProductItems?.let { deletedItems ->
            undoDeleteCartUseCase.setParams(deletedItems.map { it.cartId })
            undoDeleteCartUseCase.execute(
                onSuccess = {
                    onSuccessUndoDeleteCartItem(it, isLastItem)
                },
                onError = {
                    onErrorUndoDeleteCartItem(it)
                }
            )
        }
    }

    private fun onSuccessUndoDeleteCartItem(
        undoDeleteCartDataResponse: UndoDeleteCartDataResponse,
        isLastItem: Boolean
    ) {
        lastDeletedProductItems = null
        _globalEvent.value = GlobalEvent(
            state = GlobalEvent.STATE_SUCCESS_UNDO_DELETE_CART_ITEM,
            data = UndoDeleteCartDomainModel(undoDeleteCartDataResponse, isLastItem)
        )
    }

    private fun onErrorUndoDeleteCartItem(throwable: Throwable) {
        _globalEvent.value = GlobalEvent(
            state = GlobalEvent.STATE_FAILED_UNDO_DELETE_CART_ITEM,
            throwable = throwable
        )
    }

    fun updateCart() {
        val source = UpdateCartUseCase.VALUE_SOURCE_UPDATE_QTY_NOTES
        val miniCartProductUiModels = mutableListOf<UpdateCartRequest>()
        val visitables = getVisitables()

        visitables.forEach {
            if (it is MiniCartProductUiModel && !it.isProductDisabled) {
                if (it.isBundlingItem) {
                    miniCartProductUiModels.add(
                        UpdateCartRequest(
                            cartId = it.cartId,
                            quantity = it.productQty,
                            notes = it.productNotes,
                            bundleInfo = BundleInfo(
                                bundleId = it.bundleId,
                                bundleGroupId = it.bundleGroupId,
                                bundleQty = it.bundleQty
                            )
                        )
                    )
                } else {
                    miniCartProductUiModels.add(
                        UpdateCartRequest(
                            cartId = it.cartId,
                            quantity = it.productQty,
                            notes = it.productNotes
                        )
                    )
                }
            }
        }
        updateCartUseCase.setParams(miniCartProductUiModels, source)
        // No-op for booth onSuccess & onError
        updateCartUseCase.execute(onSuccess = {}, onError = {})
    }

    fun goToCheckout(observer: Int) {
        if (miniCartABTestData.value?.isOCCFlow == true) {
            addToCartForCheckout(observer)
        } else {
            updateCartForCheckout(observer)
        }
    }

    private fun updateCartForCheckout(observer: Int) {
        if (observer == GlobalEvent.OBSERVER_MINI_CART_WIDGET) {
            val updateCartRequests = mutableListOf<UpdateCartRequest>()
            val allMiniCartItem = getMiniCartItems()
            allMiniCartItem.forEach {
                if (it is MiniCartItem.MiniCartItemProduct && !it.isError) {
                    updateCartRequests.add(
                        UpdateCartRequest(
                            cartId = it.cartId,
                            quantity = it.quantity,
                            notes = it.notes
                        )
                    )
                } else if (it is MiniCartItem.MiniCartItemBundleGroup && !it.isError) {
                    it.products.values.forEach { product ->
                        updateCartRequests.add(
                            UpdateCartRequest(
                                cartId = product.cartId,
                                quantity = product.quantity,
                                notes = product.notes,
                                bundleInfo = BundleInfo(
                                    bundleId = it.bundleId,
                                    bundleGroupId = it.bundleGroupId,
                                    bundleQty = it.bundleQuantity
                                )
                            )
                        )
                    }
                }
            }
            updateCartUseCase.setParams(updateCartRequests)
        } else if (observer == GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET) {
            val updateCartRequests = mutableListOf<UpdateCartRequest>()
            val visitables = getVisitables()
            visitables.forEach {
                if (it is MiniCartProductUiModel && !it.isProductDisabled) {
                    if (it.isBundlingItem) {
                        updateCartRequests.add(
                            UpdateCartRequest(
                                cartId = it.cartId,
                                quantity = it.productQty,
                                notes = it.productNotes,
                                bundleInfo = BundleInfo(
                                    bundleId = it.bundleId,
                                    bundleGroupId = it.bundleGroupId,
                                    bundleQty = it.bundleQty
                                )
                            )
                        )
                    } else {
                        updateCartRequests.add(
                            UpdateCartRequest(
                                cartId = it.cartId,
                                quantity = it.productQty,
                                notes = it.productNotes
                            )
                        )
                    }
                }
            }
            updateCartUseCase.setParams(updateCartRequests)
        }
        updateCartUseCase.execute(
            onSuccess = {
                onSuccessUpdateCartForCheckout(it, observer)
            },
            onError = {
                onErrorUpdateCartForCheckout(observer, it)
            }
        )
    }

    private fun onSuccessUpdateCartForCheckout(updateCartV2Data: UpdateCartV2Data, observer: Int) {
        if (updateCartV2Data.data.status) {
            _globalEvent.value = GlobalEvent(
                observer = observer,
                state = GlobalEvent.STATE_SUCCESS_TO_CHECKOUT
            )
        } else {
            _globalEvent.value = GlobalEvent(
                observer = observer,
                state = GlobalEvent.STATE_FAILED_TO_CHECKOUT,
                data = MiniCartCheckoutData(
                    errorMessage = updateCartV2Data.data.error,
                    outOfService = updateCartV2Data.data.outOfService,
                    toasterAction = updateCartV2Data.data.toasterAction
                )
            )
        }
    }

    private fun onErrorUpdateCartForCheckout(observer: Int, throwable: Throwable) {
        _globalEvent.value = GlobalEvent(
            observer = observer,
            state = GlobalEvent.STATE_FAILED_TO_CHECKOUT,
            throwable = throwable
        )
    }

    private fun addToCartForCheckout(observer: Int) {
        if (observer == GlobalEvent.OBSERVER_MINI_CART_WIDGET) {
            val addToCartParams = mutableListOf<AddToCartOccMultiCartParam>()
            getMiniCartItems().forEach { miniCartItem ->
                if (miniCartItem is MiniCartItem.MiniCartItemProduct && !miniCartItem.isError) {
                    addToCartParams.add(
                        AddToCartOccMultiCartParam(
                            cartId = miniCartItem.cartId,
                            productId = miniCartItem.productId,
                            shopId = miniCartItem.shopId,
                            quantity = miniCartItem.quantity.toString(),
                            notes = miniCartItem.notes,
                            warehouseId = miniCartItem.warehouseId,
                            attribution = miniCartItem.attribution
                        )
                    )
                }
            }
            val params = AddToCartOccMultiRequestParams(
                carts = addToCartParams,
                source = AddToCartOccMultiRequestParams.SOURCE_MINICART
            )
            addToCartOccMultiUseCase.setParams(params)
        } else if (observer == GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET) {
            val addToCartParams = mutableListOf<AddToCartOccMultiCartParam>()
            val visitables = getVisitables()
            visitables.forEach { visitable ->
                if (visitable is MiniCartProductUiModel && !visitable.isProductDisabled) {
                    addToCartParams.add(
                        AddToCartOccMultiCartParam(
                            cartId = visitable.cartId,
                            productId = visitable.productId,
                            shopId = visitable.shopId,
                            quantity = visitable.productQty.toString(),
                            notes = visitable.productNotes,
                            warehouseId = visitable.warehouseId,
                            attribution = visitable.attribution
                        )
                    )
                }
            }
            val params = AddToCartOccMultiRequestParams(
                carts = addToCartParams,
                source = AddToCartOccMultiRequestParams.SOURCE_MINICART
            )
            addToCartOccMultiUseCase.setParams(params)
        }

        addToCartOccMultiUseCase.execute(
            onSuccess = {
                onSuccessAddToCartForCheckout(it, observer)
            },
            onError = {
                onErrorAddToCartForCheckout(it, observer)
            }
        )
    }

    private fun onSuccessAddToCartForCheckout(
        addToCartOccMultiDataModel: AddToCartOccMultiDataModel,
        observer: Int
    ) {
        if (!addToCartOccMultiDataModel.isStatusError()) {
            _globalEvent.value = GlobalEvent(
                observer = observer,
                state = GlobalEvent.STATE_SUCCESS_TO_CHECKOUT
            )
        } else {
            _globalEvent.value = GlobalEvent(
                observer = observer,
                state = GlobalEvent.STATE_FAILED_TO_CHECKOUT,
                data = MiniCartCheckoutData(
                    errorMessage = addToCartOccMultiDataModel.getAtcErrorMessage() ?: "",
                    outOfService = addToCartOccMultiDataModel.data.outOfService,
                    toasterAction = addToCartOccMultiDataModel.data.toasterAction
                )
            )
        }
    }

    private fun onErrorAddToCartForCheckout(throwable: Throwable, observer: Int) {
        _globalEvent.value = GlobalEvent(
            observer = observer,
            state = GlobalEvent.STATE_FAILED_TO_CHECKOUT,
            throwable = throwable
        )
    }

    // User Interaction

    fun updateProductQty(element: MiniCartProductUiModel, newQty: Int) {
        val visitables = getVisitables()
        val productId = element.productId
        val bundleId = element.bundleId
        val bundleGroupId = element.bundleGroupId
        val isBundling = element.isBundlingItem
        // flag to identify cart group
        var isInBundle = false
        loop@ for (visitable in visitables) {
            if (visitable is MiniCartProductUiModel && !visitable.isProductDisabled) {
                val updateNonBundle = !isBundling && visitable.productId == productId
                val updateBundle =
                    isBundling && visitable.bundleId == bundleId && visitable.bundleGroupId == bundleGroupId
                if (updateNonBundle) {
                    visitable.setQuantity(newQty)
                    break@loop
                }
                if (updateBundle) {
                    isInBundle = true
                    visitable.setQuantity(newQty)
                }
                if (isInBundle && !updateBundle) {
                    // break loop when already in another cart group
                    break@loop
                }
            }
        }

        if (!isBundling) {
            miniCartSimplifiedData.value?.miniCartItems?.getMiniCartItemProduct(productId)?.apply {
                if (!isError) {
                    quantity = newQty
                }
            }
        } else {
            miniCartSimplifiedData.value?.miniCartItems?.getMiniCartItemBundleGroup(bundleGroupId)
                ?.apply {
                    if (!isError) {
                        bundleQuantity = newQty
                        products.forEach {
                            it.value.quantity = newQty * bundleMultiplier
                        }
                    }
                }
        }
    }

    fun updateProductNotes(
        productId: String,
        isBundlingItem: Boolean,
        bundleId: String,
        bundleGroupId: String,
        newNotes: String
    ) {
        val visitables = getVisitables()
        loop@ for (visitable in visitables) {
            if (visitable is MiniCartProductUiModel && visitable.productId == productId && !visitable.isProductDisabled) {
                visitable.productNotes = newNotes
                break@loop
            }
        }

        if (!isBundlingItem) {
            miniCartSimplifiedData.value?.miniCartItems?.getMiniCartItemProduct(productId)?.apply {
                if (!isError) {
                    notes = newNotes
                }
            }
        } else {
            miniCartSimplifiedData.value?.miniCartItems?.getMiniCartItemBundleGroup(bundleGroupId)
                ?.apply {
                    if (!isError) {
                        products[MiniCartItemKey(productId)]?.notes = newNotes
                    }
                }
        }
    }

    fun toggleUnavailableItemsAccordion() {
        val visitables = getVisitables()
        var miniCartAccordionUiModel: MiniCartAccordionUiModel? = null
        var indexAccordionUiModel: Int = -1
        loop@ for ((index, visitable) in visitables.withIndex()) {
            if (visitable is MiniCartAccordionUiModel) {
                miniCartAccordionUiModel = visitable
                indexAccordionUiModel = index
                break@loop
            }
        }

        miniCartAccordionUiModel?.let { accordionUiModel ->
            if (accordionUiModel.isCollapsed) {
                expandUnavailableItems(visitables, accordionUiModel, indexAccordionUiModel)
            } else {
                collapseUnavailableItems(visitables, accordionUiModel, indexAccordionUiModel)
            }
        }
    }

    private fun collapseUnavailableItems(
        visitables: MutableList<Visitable<*>>,
        accordionUiModel: MiniCartAccordionUiModel,
        indexAccordionUiModel: Int
    ) {
        val tmpUnavailableProducts = mutableListOf<Visitable<*>>()
        visitables.forEach { visitable ->
            if (visitable is MiniCartUnavailableReasonUiModel || (visitable is MiniCartProductUiModel && visitable.isProductDisabled)) {
                tmpUnavailableProducts.add(visitable)
            }
        }

        if (tmpUnavailableProducts.size > 2) {
            val updatedAccordionUiModel = accordionUiModel.deepCopy().apply {
                isCollapsed = true
            }
            visitables[indexAccordionUiModel] = updatedAccordionUiModel

            tmpUnavailableProducts.removeFirst() // exclude first reason
            tmpUnavailableProducts.removeFirst() // exclude first unavailable item
            tmpHiddenUnavailableItems.clear()
            tmpHiddenUnavailableItems.addAll(tmpUnavailableProducts)
            visitables.removeAll(tmpUnavailableProducts)

            updateVisitables(visitables)
        }
    }

    private fun expandUnavailableItems(
        visitables: MutableList<Visitable<*>>,
        accordionUiModel: MiniCartAccordionUiModel,
        indexAccordionUiModel: Int
    ) {
        val updatedAccordionUiModel = accordionUiModel.deepCopy().apply {
            isCollapsed = false
        }
        visitables[indexAccordionUiModel] = updatedAccordionUiModel

        visitables.addAll(indexAccordionUiModel - 1, tmpHiddenUnavailableItems)
        tmpHiddenUnavailableItems.clear()

        updateVisitables(visitables)
    }

    // Calculation

    fun calculateProduct() {
        val visitables = getVisitables()

        val miniCartProductList = mutableListOf<MiniCartProductUiModel>()
        visitables.forEach { visitable ->
            if (visitable is MiniCartProductUiModel && !visitable.isProductDisabled) {
                miniCartProductList.add(visitable)
            }
        }

        // Set temporary parent id for non variant product
        var tmpParentId = 0
        miniCartProductList.forEach { visitable ->
            if (visitable.parentId == "0") {
                visitable.parentId = TEMPORARY_PARENT_ID_PREFIX + ++tmpParentId
            }
        }

        // Map of parent id - qty
        val productParentQtyMap = mutableMapOf<String, Int>()
        miniCartProductList.forEach { visitable ->
            val productQty = visitable.getQuantity()
            if (productParentQtyMap.containsKey(visitable.parentId)) {
                val newQty = (productParentQtyMap[visitable.parentId] ?: 0) + productQty
                productParentQtyMap[visitable.parentId] = newQty
            } else {
                productParentQtyMap[visitable.parentId] = productQty
            }
        }

        // Set wholesale price
        setWholesalePrice(miniCartProductList, productParentQtyMap, visitables)

        // Calculate total price
        var totalQty = 0
        var sellerCashbackValue = 0.0
        var totalPrice = 0.0
        var totalValue = 0.0
        var totalDiscount = 0.0
        var totalWeight = 0

        val allProductList = visitables.filterIsInstance<MiniCartProductUiModel>()
        val nonBundleProductList = allProductList.filter { !it.isBundlingItem }
        val bundleProductList = allProductList.filter { it.isBundlingItem }
            .distinctBy { it.bundleGroupId }
        val productList = nonBundleProductList + bundleProductList

        productList.forEach { visitable ->
            if (!visitable.isProductDisabled) {
                val productQty = visitable.getQuantity()
                val productPrice = visitable.getPrice()
                val productOriginalPrice = visitable.getOriginalPrice()

                if (visitable.parentId.contains(TEMPORARY_PARENT_ID_PREFIX)) {
                    visitable.parentId =
                        "0"
                }
                val price = when {
                    visitable.productWholeSalePrice > 0 && !visitable.isBundlingItem -> {
                        visitable.productWholeSalePrice
                    }

                    else -> productPrice
                }
                totalPrice += productQty * price
                sellerCashbackValue += (productQty * visitable.productCashbackPercentage / DEFAULT_PERCENTAGE * price)
                val originalPrice = when {
                    productOriginalPrice > 0 -> productOriginalPrice
                    visitable.productWholeSalePrice > 0 && !visitable.isBundlingItem -> {
                        visitable.productWholeSalePrice
                    }

                    else -> productPrice
                }
                totalValue += productQty * originalPrice
                val discountValue = when {
                    productOriginalPrice > 0 -> productOriginalPrice - productPrice
                    else -> 0.0
                }
                totalDiscount += productQty * discountValue
            }
        }

        allProductList.forEach { visitable ->
            if (!visitable.isProductDisabled) {
                totalWeight += visitable.productWeight * visitable.productQty
                totalQty += visitable.productQty
            }
        }

        miniCartListBottomSheetUiModel.value?.let {
            it.miniCartWidgetUiModel.totalProductPrice = totalPrice
            it.miniCartWidgetUiModel.totalProductCount = totalQty
            it.miniCartSummaryTransactionUiModel.qty = totalQty
            it.miniCartSummaryTransactionUiModel.totalValue = totalValue
            it.miniCartSummaryTransactionUiModel.discountValue = totalDiscount
            it.miniCartSummaryTransactionUiModel.paymentTotal = totalPrice
            it.miniCartSummaryTransactionUiModel.sellerCashbackValue = sellerCashbackValue
            it.needToCalculateAfterLoad = false
        }

        validateOverWeight(totalWeight, visitables)

        updateVisitables(visitables)
    }

    private fun setWholesalePrice(
        miniCartProductList: MutableList<MiniCartProductUiModel>,
        productParentQtyMap: MutableMap<String, Int>,
        visitables: MutableList<Visitable<*>>
    ) {
        val updatedProductForWholesalePriceItems = mutableListOf<MiniCartProductUiModel>()
        val updatedProductForWholesalePriceItemsProductId = mutableListOf<String>()
        miniCartProductList.forEach { visitable ->
            val updatedProduct = visitable.copy()
            var isUpdatedWholeSalePrice = false // flag to update ui
            var isEligibleForWholesalePrice = false
            loop@ for (wholesalePrice in visitable.wholesalePriceGroup) {
                val qty = productParentQtyMap[visitable.parentId] ?: 0

                // Set wholesale price if eligible
                if (qty >= wholesalePrice.qtyMin) {
                    if (updatedProduct.productWholeSalePrice != wholesalePrice.prdPrc) {
                        updatedProduct.productWholeSalePrice = wholesalePrice.prdPrc
                        isUpdatedWholeSalePrice = true
                    }
                    isEligibleForWholesalePrice = true
                    break@loop
                }
            }

            // Reset wholesale price not eligible and previously has wholesale price
            if (!isEligibleForWholesalePrice && visitable.productWholeSalePrice > 0L) {
                updatedProduct.productWholeSalePrice = 0.0
                isUpdatedWholeSalePrice = true
            }

            if (isUpdatedWholeSalePrice) {
                updatedProductForWholesalePriceItems.add(updatedProduct)
                updatedProductForWholesalePriceItemsProductId.add(updatedProduct.productId)
            }
        }

        val updatedProductIndex = mutableListOf<Int>()
        visitables.forEachIndexed { index, visitable ->
            if (visitable is MiniCartProductUiModel && !visitable.isProductDisabled && updatedProductForWholesalePriceItemsProductId.contains(
                    visitable.productId
                )
            ) {
                updatedProductIndex.add(index)
            }
        }

        var tmpIndex = 0
        updatedProductIndex.forEach { index ->
            val tmpVisitable = updatedProductForWholesalePriceItems[tmpIndex++]
            visitables[index] = tmpVisitable
        }
    }

    private fun validateOverWeight(totalWeight: Int, visitables: MutableList<Visitable<*>>) {
        miniCartListBottomSheetUiModel.value?.let { miniCartListUiModel ->
            val maxWeight = miniCartListUiModel.maximumShippingWeight

            if (totalWeight > maxWeight) {
                var tickerWarning: MiniCartTickerWarningUiModel? = null
                var tickerWarningIndex = -1
                loop@ for ((index, visitable) in visitables.withIndex()) {
                    if (visitable is MiniCartTickerWarningUiModel) {
                        tickerWarning = visitable
                        tickerWarningIndex = index
                        break@loop
                    }
                }

                val warningWording = miniCartListUiModel.maximumShippingWeightErrorMessage
                val overWeight = (totalWeight - maxWeight) / DEFAULT_WEIGHT
                if (tickerWarning == null) {
                    tickerWarning = miniCartListUiModelMapper.mapTickerWarningUiModel(
                        overWeight,
                        warningWording
                    )
                    tickerWarning.let {
                        val firstItem = visitables.firstOrNull()
                        if (firstItem != null && firstItem is MiniCartTickerErrorUiModel) {
                            visitables.add(1, it)
                        } else {
                            visitables.add(0, it)
                        }
                    }
                } else {
                    val updatedTickerWarning = tickerWarning.deepCopy()
                    val formattedOverWeight =
                        NumberFormat.getNumberInstance(Locale("in", "id")).format(overWeight)
                    updatedTickerWarning.warningMessage = warningWording.replace(
                        MiniCartListUiModelMapper.PLACEHOLDER_OVERWEIGHT_VALUE,
                        "$formattedOverWeight "
                    )
                    visitables[tickerWarningIndex] = updatedTickerWarning
                }
            } else {
                removeTickerWarning(visitables)
            }
        }
    }

    private fun removeTickerWarning(visitables: MutableList<Visitable<*>>) {
        var tmpIndex = -1
        loop@ for ((index, visitable) in visitables.withIndex()) {
            if (visitable is MiniCartTickerWarningUiModel) {
                tmpIndex = index
                break@loop
            }
        }

        if (tmpIndex != -1) {
            visitables.removeAt(tmpIndex)
        }
    }
}
