package com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.common.constant.ConstantValue.X_DEVICE_RECOMMENDATION_PARAM
import com.tokopedia.tokopedianow.common.constant.ConstantValue.X_SOURCE_RECOMMENDATION_PARAM
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState.Companion.SHOW
import com.tokopedia.tokopedianow.common.domain.mapper.AddressMapper.mapToWarehousesData
import com.tokopedia.tokopedianow.common.model.UiState
import com.tokopedia.tokopedianow.common.model.UiState.Success
import com.tokopedia.tokopedianow.common.model.UiState.Loading
import com.tokopedia.tokopedianow.common.model.UiState.Error
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType.AVAILABLE_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductState.COLLAPSE
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductState
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.CommonVisitableMapper.addErrorState
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.CommonVisitableMapper.addRecommendedProducts
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addLoadingState
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addDivider
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addEmptyShoppingList
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addExpandCollapse
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addHeader
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addLoadMore
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addProductCartItem
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addProductCartWidget
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addRetry
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addShoppingListProducts
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addTitle
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addTopCheckAllShoppingList
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.doIf
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.mapAvailableShoppingList
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.mapUnavailableShoppingList
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.modifyExpandCollapseProducts
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.modifyExpandCollapseState
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.modifyProduct
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.modifyTopCheckAll
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.modifyTopCheckAllState
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.removeLoadMore
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.removeRetry
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.updateProductSelections
import com.tokopedia.tokopedianow.shoppinglist.domain.model.GetShoppingListDataResponse
import com.tokopedia.tokopedianow.shoppinglist.domain.model.SaveShoppingListStateActionParam
import com.tokopedia.tokopedianow.shoppinglist.domain.usecase.GetShoppingListUseCase
import com.tokopedia.tokopedianow.shoppinglist.domain.usecase.SaveShoppingListStateUseCase
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.BottomBulkAtcModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.HeaderModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.LayoutModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListProductCartItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListTopCheckAllUiModel
import com.tokopedia.tokopedianow.shoppinglist.util.Constant.INVALID_INDEX
import com.tokopedia.tokopedianow.shoppinglist.util.Constant.MAX_TOTAL_PRODUCT_DISPLAYED
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class TokoNowShoppingListViewModel @Inject constructor(
    private val addressData: TokoNowLocalAddress,
    private val userSession: UserSessionInterface,
    private val productRecommendationUseCase: GetSingleRecommendationUseCase,
    private val getShoppingListUseCase: GetShoppingListUseCase,
    private val saveShoppingListStateUseCase: SaveShoppingListStateUseCase,
    private val getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.io) {

    private companion object {
        private const val OOC_WAREHOUSE_ID = 0L
        private const val INVALID_SHOP_ID = 0L
        private const val PRODUCT_RECOMMENDATION_PAGE_NAME = "tokonow_shopping_list"
        private const val PRODUCT_RECOMMENDATION_PAGE_NUMBER_COUNTER = 1
        private const val DEBOUNCE_TIMES_SHOPPING_LIST = 1000L
        private const val EMPTY_STOCK_WIDGET_TITLE = "Stok habis "
        private const val PRODUCT_CART_WIDGET_TITLE = "produk ada di keranjang"
    }

    /**
     * -- private variable section --
     */

    private val layout = mutableListOf<Visitable<*>> ()
    private val availableProducts = mutableListOf<ShoppingListHorizontalProductCardItemUiModel>()
    private val unavailableProducts = mutableListOf<ShoppingListHorizontalProductCardItemUiModel>()
    private val checkUncheckStateParams = mutableListOf<SaveShoppingListStateActionParam>()
    private val cartProducts = mutableListOf<ShoppingListProductCartItemUiModel>()

    private val _layoutState: MutableStateFlow<UiState<LayoutModel>> = MutableStateFlow(Loading(LayoutModel(layout.addLoadingState())))
    private val _miniCartState: MutableStateFlow<UiState<MiniCartSimplifiedData>> = MutableStateFlow(Loading())
    private val _isOnScrollNotNeeded: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _isNavToolbarScrollingBehaviourEnabled: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val _isTopCheckAllSelected: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _isProductAvailable: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _isLoaderDialogShown: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _bottomBulkAtcData: MutableStateFlow<BottomBulkAtcModel?> = MutableStateFlow(null)
    private val _updateToolbarNotification: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private var pageCounter: Int = PRODUCT_RECOMMENDATION_PAGE_NUMBER_COUNTER
    private var hasLoadedLayout: Boolean = false
    private var mMiniCartData: MiniCartSimplifiedData? = null
    private var loadLayoutJob: Job? = null
    private var saveShoppingListStateJob: Job? = null
    private var getMiniCartJob: Job? = null

    /**
     * -- public variable section --
     */

    val layoutState
        get() = _layoutState.asStateFlow()
    val miniCartState
        get() = _miniCartState.asStateFlow()
    val isOnScrollNotNeeded
        get() = _isOnScrollNotNeeded.asStateFlow()
    val isNavToolbarScrollingBehaviourEnabled
        get() = _isNavToolbarScrollingBehaviourEnabled.asStateFlow()
    val isTopCheckAllSelected
        get() = _isTopCheckAllSelected.asStateFlow()
    val isProductAvailable
        get() = _isProductAvailable.asStateFlow()
    val isLoaderDialogShown
        get() = _isLoaderDialogShown.asStateFlow()
    val bottomBulkAtcData
        get() = _bottomBulkAtcData.asStateFlow()
    val updateToolbarNotification
        get() = _updateToolbarNotification.asStateFlow()

    var headerModel: HeaderModel = HeaderModel()

    /**
     * -- private suspend function section --
     */

    private suspend fun getMiniCartDeferred(): Deferred<MiniCartSimplifiedData?> = async {
        if (isGettingMiniCartAllowed()) {
            getMiniCartUseCase.setParams(
                shopIds = listOf(addressData.getShopId().toString()),
                source = MiniCartSource.TokonowShoppingList
            )
            val miniCartData = getMiniCartUseCase.executeOnBackground()
            miniCartData.copy(isShowMiniCartWidget = miniCartData.isShowMiniCartWidget && !addressData.isOutOfCoverage())
        } else {
            null
        }
    }

    private suspend fun getShoppingListDeferred() = async {
        val warehouses = mapToWarehousesData(addressData.getAddressData())
        getShoppingListUseCase.execute(warehouses)
    }

    private suspend fun getProductRecommendationDeferred() = async {
        val param = GetRecommendationRequestParam(
            pageNumber = pageCounter,
            userId = userSession.userId.toIntSafely(),
            pageName = PRODUCT_RECOMMENDATION_PAGE_NAME,
            xDevice = X_DEVICE_RECOMMENDATION_PARAM,
            xSource = X_SOURCE_RECOMMENDATION_PARAM,
            isTokonow = true
        )
        productRecommendationUseCase.getData(param)
    }

    private suspend fun saveShoppingListState() {
        delay(DEBOUNCE_TIMES_SHOPPING_LIST)
        saveShoppingListStateUseCase.execute(checkUncheckStateParams)
        checkUncheckStateParams.clear()
    }

    private suspend fun saveAllAvailableProductsState() {
        checkUncheckStateParams.clear()
        checkUncheckStateParams.addAll(availableProducts.map { SaveShoppingListStateActionParam(it.id, it.isSelected) })

        saveShoppingListState()
    }

    private suspend fun saveAvailableProductState(
        productId: String,
        isSelected: Boolean
    ) {
        val index = checkUncheckStateParams.indexOfFirst { it.productId == productId }
        if (index != INVALID_INDEX) {
            checkUncheckStateParams[index] = SaveShoppingListStateActionParam(productId, isSelected)
        } else {
            checkUncheckStateParams.add(SaveShoppingListStateActionParam(productId, isSelected))
        }

        saveShoppingListState()
    }

    /**
     * -- private function section --
     */

    private fun addHeaderSection() {
        layout.addHeader(
            headerModel = headerModel,
            state = SHOW
        )
    }

    private fun addShoppingListSection(
        shoppingListData: GetShoppingListDataResponse.Data
    ) {
        availableProducts.clear()
        unavailableProducts.clear()

        availableProducts.addAll(mapAvailableShoppingList(shoppingListData.listAvailableItem))
        unavailableProducts.addAll(mapUnavailableShoppingList(shoppingListData.listUnavailableItem))

        val isShoppingListAvailable = availableProducts.isNotEmpty() || unavailableProducts.isNotEmpty()

        layout.doIf(
            predicate = isShoppingListAvailable,
            then = {
                val displayedAvailableItems = availableProducts.take(MAX_TOTAL_PRODUCT_DISPLAYED)
                val displayedUnavailableItems = unavailableProducts.take(MAX_TOTAL_PRODUCT_DISPLAYED)
                val isAvailableProductListNotEmpty = availableProducts.isNotEmpty()
                val isUnavailableProductListNotEmpty = unavailableProducts.isNotEmpty()

                _isProductAvailable.value = availableProducts.isNotEmpty()

                /**
                 * Add Available Products
                 */

                doIf(
                    predicate = isAvailableProductListNotEmpty,
                    then = layout@ {
                        calculateDataForBottomBulkAtc()

                        val areAllAvailableProductsSelected = shoppingListData.metadata.inStockSelectedTotalData == availableProducts.size
                        val areAvailableProductsMoreThanDefaultDisplayed = availableProducts.size > MAX_TOTAL_PRODUCT_DISPLAYED
                        val remainingTotalProduct = availableProducts.size - displayedAvailableItems.size

                        _isTopCheckAllSelected.value = areAllAvailableProductsSelected

                        this@layout
                            .addTopCheckAllShoppingList(
                                productState = COLLAPSE,
                                isSelected = areAllAvailableProductsSelected
                            )
                            .addShoppingListProducts(displayedAvailableItems)
                            .doIf(
                                predicate = areAvailableProductsMoreThanDefaultDisplayed,
                                then = {
                                    addExpandCollapse(
                                        productState = COLLAPSE,
                                        remainingTotalProduct = remainingTotalProduct,
                                        productLayoutType = AVAILABLE_SHOPPING_LIST
                                    )
                                }
                            )
                            doIf(
                                predicate = isUnavailableProductListNotEmpty,
                                then = {
                                    addDivider()
                                }
                            )
                    }
                )

                /**
                 * Add Unavailable Products
                 */

                doIf(
                    predicate = isUnavailableProductListNotEmpty,
                    then = layout@ {
                        val areUnavailableProductsMoreThanDefaultDisplayed = unavailableProducts.size > MAX_TOTAL_PRODUCT_DISPLAYED
                        val remainingTotalProduct = unavailableProducts.size - displayedUnavailableItems.size

                        this@layout
                            .addTitle("$EMPTY_STOCK_WIDGET_TITLE(${unavailableProducts.size})")
                            .addShoppingListProducts(displayedUnavailableItems)
                            .doIf(
                                predicate = areUnavailableProductsMoreThanDefaultDisplayed,
                                then = {
                                    addExpandCollapse(
                                        productState = COLLAPSE,
                                        remainingTotalProduct = remainingTotalProduct,
                                        productLayoutType = ShoppingListProductLayoutType.UNAVAILABLE_SHOPPING_LIST
                                    )
                                }
                            )
                    }
                )
            },
            ifNot = {
                addEmptyShoppingList()
            }
        )
    }

    private fun addProductCartWidgetSection(
        miniCartData: MiniCartSimplifiedData?
    ) {
        val isShoppingListAvailable = availableProducts.isNotEmpty() || unavailableProducts.isNotEmpty()

        filterShoppingListByProductInCart(
            miniCartData = miniCartData
        )

        layout.doIf(
            predicate = cartProducts.isNotEmpty(),
            then = layout@ {
                this@layout
                    .doIf(
                        predicate = isShoppingListAvailable,
                        then = {
                            addDivider()
                        }
                    )
                    .addTitle("${cartProducts.size} $PRODUCT_CART_WIDGET_TITLE")
                    .addProductCartWidget(cartProducts)
            }
        )
    }

    private fun addProductRecommendationSection(
        productRecommendation: RecommendationWidget,
    ) {
        layout
            .doIf(
                predicate = productRecommendation.recommendationItemList.isNotEmpty(),
                then = layout@ {
                    val isShoppingListAvailable = availableProducts.isNotEmpty() || unavailableProducts.isNotEmpty()

                    this@layout
                        .doIf(
                            predicate = isShoppingListAvailable,
                            then = {
                                addDivider()
                            }
                        )
                        .addTitle(productRecommendation.title)
                        .addRecommendedProducts(productRecommendation)
                        .doIf(
                            predicate = productRecommendation.hasNext,
                            then = {
                                addLoadMore()
                                pageCounter++
                            },
                            ifNot = {
                                _isOnScrollNotNeeded.value = true
                            }
                        )
                },
                ifNot = {
                    _isOnScrollNotNeeded.value = true
                }
            )
    }

    private fun filterShoppingListByProductInCart(
        miniCartData: MiniCartSimplifiedData?
    ) {
        miniCartData?.apply {
            val miniCartItems: List<MiniCartItem.MiniCartItemProduct> = miniCartItems.values.filterIsInstance<MiniCartItem.MiniCartItemProduct>()
            if (miniCartItems.isEmpty()) return

            cartProducts.clear()

            cartProducts.addProductCartItem(
                productList = availableProducts,
                miniCartItems = miniCartItems
            )

            cartProducts.addProductCartItem(
                productList = unavailableProducts,
                miniCartItems = miniCartItems
            )
        }
    }

    private fun calculateDataForBottomBulkAtc() {
        _bottomBulkAtcData.value = BottomBulkAtcModel(
            counter = availableProducts.count { it.isSelected },
            price = availableProducts.filter { it.isSelected }.sumOf { it.priceInt }
        )
    }

    private fun isGettingMiniCartAllowed(): Boolean {
        val isOutOfCoverage = addressData.getWarehouseId() == OOC_WAREHOUSE_ID
        val isShopValid = addressData.getShopId() != INVALID_SHOP_ID
        val isUserLoggedIn = userSession.isLoggedIn
        return isShopValid && !isOutOfCoverage && isUserLoggedIn
    }

    private fun getUpdatedLayout(
        isRequiredToScrollUp: Boolean = false
    ): LayoutModel = LayoutModel(
        layout = layout.toList(),
        isRequiredToScrollUp = isRequiredToScrollUp
    )

    private fun loadErrorState(
        throwable: Throwable
    ) {
        layout.clear()

        layout.addErrorState(
            isFullPage = true,
            throwable = throwable
        )

        _layoutState.value = Error(getUpdatedLayout(), throwable)

        _isNavToolbarScrollingBehaviourEnabled.value = false
    }

    private fun loadLoadingState() {
        layout.clear()

        layout.addLoadingState()

        _layoutState.value = Loading(getUpdatedLayout())

        _isNavToolbarScrollingBehaviourEnabled.value = true
    }

    private fun loadSuccessState() {

        /**
         * block thread until the coroutine inside runBlocking completes
         */

        val result = runBlocking {
            listOf(
                getMiniCartDeferred(),
                getShoppingListDeferred(),
                getProductRecommendationDeferred()
            ).awaitAll()
        }

        /**
         * cast the results to their respective types and create some variables to be helpers
         */

        val miniCartData = result.component1() as MiniCartSimplifiedData?
        val shoppingListData = result.component2() as GetShoppingListDataResponse.Data
        val productRecommendationData = result.component3() as RecommendationWidget

        /**
         * add each widget section
         */

        layout.clear()

        addHeaderSection()
        addShoppingListSection(shoppingListData)
        addProductCartWidgetSection(miniCartData)
        addProductRecommendationSection(productRecommendationData)

        _layoutState.value = Success(getUpdatedLayout(isRequiredToScrollUp = true))

        if (availableProducts.isNotEmpty()) {
            _isProductAvailable.value = true
        } else if (miniCartData != null) {
            _isProductAvailable.value = false
            _miniCartState.value = Success(miniCartData.copy())
        } else {
            _isProductAvailable.value = false
            _miniCartState.value = Error(throwable = Throwable())
        }

        hasLoadedLayout = true
    }

    /**
     * -- public function section --
     */

    fun loadLayout() {
        loadLayoutJob = launchCatchError(
            block = {
                loadSuccessState()
            },
            onError = { throwable ->
                loadErrorState(throwable)
            }
        )
    }

    fun refreshLayout() {
        loadLoadingState()
        loadLayout()
    }

    fun expandCollapseShoppingList(
        productState: ShoppingListProductState,
        productLayoutType: ShoppingListProductLayoutType
    ) {
        layout
            .modifyExpandCollapseProducts(
                state = productState,
                productLayoutType = productLayoutType,
                products = if (productLayoutType == AVAILABLE_SHOPPING_LIST) availableProducts else unavailableProducts
            )
            .modifyExpandCollapseState(
                productState = productState,
                productLayoutType = productLayoutType
            )
            .doIf(
                predicate = productLayoutType == AVAILABLE_SHOPPING_LIST,
                then = {
                    modifyTopCheckAllState(
                        productState = productState
                    )
                }
            )

        _layoutState.value = Success(getUpdatedLayout())
    }

    fun selectAllAvailableProducts(
        state: ShoppingListProductState,
        isSelected: Boolean
    ) {
        saveShoppingListStateJob?.cancel()
        saveShoppingListStateJob = launchCatchError(
            block = {
                availableProducts.updateProductSelections(
                    isSelected = isSelected
                )

                calculateDataForBottomBulkAtc()

                layout
                    .modifyTopCheckAll(
                        isSelected = isSelected
                    )
                    .modifyExpandCollapseProducts(
                        state = state,
                        productLayoutType = AVAILABLE_SHOPPING_LIST,
                        products = availableProducts
                    )

                _isTopCheckAllSelected.value = isSelected

                _layoutState.value = Success(getUpdatedLayout())

                saveAllAvailableProductsState()
            }, onError = { /* do nothing */ }
        )
    }

    fun selectAllAvailableProducts(
        isSelected: Boolean
    ) {
        val topCheckAllUiModel = layout.firstOrNull { it is ShoppingListTopCheckAllUiModel } as? ShoppingListTopCheckAllUiModel

        if (topCheckAllUiModel != null) {
            selectAllAvailableProducts(
                state = topCheckAllUiModel.productState,
                isSelected = isSelected
            )
        }
    }

    fun selectAvailableProduct(
        productId: String,
        isSelected: Boolean
    ) {
        saveShoppingListStateJob?.cancel()
        saveShoppingListStateJob = launchCatchError(
            block = {
                availableProducts.updateProductSelections(
                    productId = productId,
                    isSelected = isSelected
                )

                calculateDataForBottomBulkAtc()

                val areAllAvailableProductsSelected = availableProducts.all { it.isSelected }

                layout
                    .modifyTopCheckAll(
                        isSelected = areAllAvailableProductsSelected
                    )
                    .modifyProduct(
                        productId = productId,
                        isSelected = isSelected
                    )

                _isTopCheckAllSelected.value = areAllAvailableProductsSelected

                _layoutState.value = Success(getUpdatedLayout())

                saveAvailableProductState(
                    productId = productId,
                    isSelected = isSelected
                )
            }, onError = { /* do nothing */ }
        )
    }

    fun switchRetryToLoadMore() {
        layout
            .removeRetry()
            .addLoadMore()

        _layoutState.value = Success(getUpdatedLayout())
    }

    fun loadMoreProductRecommendation(
        isLastVisibleLoadingMore: Boolean
    ) {
        if (isLastVisibleLoadingMore && loadLayoutJob?.isCompleted.orFalse()) {
            loadLayoutJob = launchCatchError(
                block = {
                    val productRecommendation = getProductRecommendationDeferred().await()

                    layout
                        .removeLoadMore()
                        .doIf(
                            predicate = productRecommendation.recommendationItemList.isNotEmpty(),
                            then = layout@ {
                                this@layout
                                    .addRecommendedProducts(productRecommendation)
                                    .doIf(
                                        predicate = productRecommendation.hasNext,
                                        then = {
                                            addLoadMore()
                                            pageCounter++
                                        },
                                        ifNot = {
                                            _isOnScrollNotNeeded.value = true
                                        }
                                    )
                            },
                            ifNot = {
                                _isOnScrollNotNeeded.value = true
                            }
                        )

                    _layoutState.value = Success(getUpdatedLayout())
                },
                onError = {
                    layout
                        .removeLoadMore()
                        .addRetry()

                    _layoutState.value = Success(getUpdatedLayout())
                }
            )
        }
    }

    fun getMiniCart() {
        getMiniCartJob?.cancel()
        getMiniCartJob = launchCatchError(block = {
            val miniCartData = getMiniCartDeferred().await()
            _miniCartState.value = if (miniCartData != null) Success(miniCartData.copy()) else Error(throwable = Throwable())
            _isLoaderDialogShown.value = false
        }) {
            _miniCartState.value = Error(throwable = it)
            _isLoaderDialogShown.value = false
        }
    }

    fun setMiniCartData(
        miniCartData: MiniCartSimplifiedData
    ) {
        mMiniCartData = miniCartData
    }

    fun getShopId(): Long = addressData.getShopId()

    fun onResume() {
        if (addressData.isChoosenAddressUpdated()) {
            refreshLayout()
        } else {
            if (hasLoadedLayout) {
                _isLoaderDialogShown.value = true
                getMiniCart()
            }
        }
        _updateToolbarNotification.value = true
    }
}
