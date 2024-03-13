package com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.model.request.AddToCartMultiParam
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartMultiUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartMultiUseCase.Companion.SUCCESS_STATUS
import com.tokopedia.home_component.customview.pullrefresh.LayoutIconPullRefreshView
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
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
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState.Companion.LOADING
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
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.CommonVisitableMapper.modifyProduct
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addLoadingState
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addDivider
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addEmptyShoppingList
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addExpandCollapse
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addHeader
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addLoadMore
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addRetry
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addTitle
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addTopCheckAllShoppingList
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.doIf
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.mapUnavailableShoppingList
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.modifyExpandCollapseProducts
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.modifyExpandCollapseState
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.modifyTopCheckAll
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.modifyTopCheckAllState
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.removeLoadMore
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.removeProduct
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.removeRetry
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.updateProductSelections
import com.tokopedia.tokopedianow.shoppinglist.domain.model.GetShoppingListDataResponse
import com.tokopedia.tokopedianow.shoppinglist.domain.model.SaveShoppingListStateActionParam
import com.tokopedia.tokopedianow.shoppinglist.domain.usecase.GetShoppingListUseCase
import com.tokopedia.tokopedianow.shoppinglist.domain.usecase.SaveShoppingListStateUseCase
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.BottomBulkAtcModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.HeaderModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.LayoutModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.ToasterModel.Event.DELETE_WISHLIST
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListCartProductItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListTopCheckAllUiModel
import com.tokopedia.tokopedianow.shoppinglist.util.Constant.INVALID_INDEX
import com.tokopedia.tokopedianow.shoppinglist.util.Constant.MAX_TOTAL_PRODUCT_DISPLAYED
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType.UNAVAILABLE_SHOPPING_LIST
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.helper.ResourceProvider
import com.tokopedia.tokopedianow.common.model.TokoNowThematicHeaderUiModel
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.CommonVisitableMapper.mapRecommendedProducts
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addEmptyStateOoc
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addProduct
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addProductCartItem
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addProductCarts
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addProductRecommendationOoc
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addProducts
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.filteredBy
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.mapAvailableShoppingList
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.RecommendationModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.ToasterModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.ToasterModel.Event.ADD_MULTI_PRODUCTS_TO_CART
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.ToasterModel.Event.ADD_WISHLIST
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class TokoNowShoppingListViewModel @Inject constructor(
    private val addressData: TokoNowLocalAddress,
    private val userSession: UserSessionInterface,
    private val productRecommendationUseCase: GetSingleRecommendationUseCase,
    private val getShoppingListUseCase: GetShoppingListUseCase,
    private val saveShoppingListStateUseCase: SaveShoppingListStateUseCase,
    private val getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    private val addToWishlistUseCase: AddToWishlistV2UseCase,
    private val deleteFromWishlistUseCase: DeleteWishlistV2UseCase,
    private val addToCartMultiUseCase: AddToCartMultiUseCase,
    private val resourceProvider: ResourceProvider,
    dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.io) {

    private companion object {
        private const val OOC_WAREHOUSE_ID = 0L
        private const val INVALID_SHOP_ID = 0L
        private const val PRODUCT_RECOMMENDATION_PAGE_NAME = "tokonow_shopping_list"
        private const val DEBOUNCE_TIMES_SHOPPING_LIST = 1000L
        private const val EMPTY_STOCK_WIDGET_TITLE = "Stok habis "
        private const val PRODUCT_CART_WIDGET_TITLE = "produk ada di keranjang"
    }

    /**
     * -- private variable section --
     */

    private val mutableLayout = mutableListOf<Visitable<*>>()

    private val availableProducts = mutableListOf<ShoppingListHorizontalProductCardItemUiModel>()
    private val unavailableProducts = mutableListOf<ShoppingListHorizontalProductCardItemUiModel>()
    private val recommendedProducts = mutableListOf<ShoppingListHorizontalProductCardItemUiModel>()
    private val cartProducts = mutableListOf<ShoppingListCartProductItemUiModel>()

    private val filteredAvailableProducts: MutableList<ShoppingListHorizontalProductCardItemUiModel> = mutableListOf()
    private val filteredUnavailableProducts: MutableList<ShoppingListHorizontalProductCardItemUiModel> = mutableListOf()
    private val filteredRecommendedProducts: MutableList<ShoppingListHorizontalProductCardItemUiModel> = mutableListOf()

    private val checkUncheckStateParams = mutableListOf<SaveShoppingListStateActionParam>()

    private val _layoutState: MutableStateFlow<UiState<LayoutModel>> = MutableStateFlow(Loading(LayoutModel(mutableLayout.addLoadingState())))
    private val _miniCartState: MutableStateFlow<UiState<MiniCartSimplifiedData>> = MutableStateFlow(Loading())
    private val _isOnScrollNotNeeded: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _isNavToolbarScrollingBehaviourEnabled: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val _isTopCheckAllSelected: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _isProductAvailable: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _isLoaderDialogShown: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _toasterData: MutableStateFlow<ToasterModel?> = MutableStateFlow(null)
    private val _bottomBulkAtcData: MutableStateFlow<BottomBulkAtcModel?> = MutableStateFlow(null)
    private val _isCoachMarkShown: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private var hasLoadedLayout: Boolean = false
    private var mMiniCartData: MiniCartSimplifiedData? = null
    private var loadLayoutJob: Job? = null
    private var saveShoppingListStateJob: Job? = null
    private var getMiniCartJob: Job? = null
    private var recommendationModel: RecommendationModel = RecommendationModel()

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
    val toasterData
        get() = _toasterData.asStateFlow()
    val isCoachMarkShown
        get() = _isCoachMarkShown.asStateFlow()

    /**
     * -- private suspend function section --
     */

    private suspend fun getMiniCartDeferred(): Deferred<Unit> = async {
        val miniCartData = if (isGettingMiniCartAllowed()) {
            getMiniCartUseCase.setParams(
                shopIds = listOf(addressData.getShopId().toString()),
                source = MiniCartSource.TokonowShoppingList
            )
            val miniCartData = getMiniCartUseCase.executeOnBackground()
            miniCartData.copy(isShowMiniCartWidget = miniCartData.isShowMiniCartWidget && !addressData.isOutOfCoverage())
        } else {
            null
        }
        mMiniCartData = miniCartData
    }

    private suspend fun getShoppingListDeferred() = async {
        val warehouses = mapToWarehousesData(addressData.getAddressData())
        getShoppingListUseCase.execute(warehouses)
    }

    private suspend fun getProductRecommendationDeferred(
        pageNumber: Int
    ) = async {
        val param = GetRecommendationRequestParam(
            pageNumber = pageNumber,
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
        checkUncheckStateParams.addAll(availableProducts.map { SaveShoppingListStateActionParam(productId = it.id, isSelected = it.isSelected) })

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
        mutableLayout.addHeader(
            headerModel = HeaderModel(
                pageTitle = resourceProvider.getString(R.string.tokopedianow_shopping_list_page_title),
                pageTitleColor = resourceProvider.getColor(unifyprinciplesR.color.Unify_Static_White),
                ctaText = resourceProvider.getString(R.string.tokopedianow_shopping_list_repurchase),
                ctaTextColor = resourceProvider.getColor(unifyprinciplesR.color.Unify_Static_White),
                ctaChevronColor = resourceProvider.getColor(unifyprinciplesR.color.Unify_Static_White),
                backgroundGradientColor = TokoNowThematicHeaderUiModel.GradientColor(
                    startColor = resourceProvider.getColor(unifyprinciplesR.color.Unify_GN500),
                    endColor = resourceProvider.getColor(unifyprinciplesR.color.Unify_GN400)
                ),
                chooseAddressResIntColor = unifyprinciplesR.color.Unify_Static_White,
                isChooseAddressShown = true,
                isSuperGraphicImageShown = true
            ),
            state = SHOW
        )
    }

    private fun addShoppingListSection(
        isShoppingListAvailable: Boolean
    ) {
        mutableLayout.doIf(
            predicate = isShoppingListAvailable,
            then = {
                val displayedAvailableItems = filteredAvailableProducts.take(MAX_TOTAL_PRODUCT_DISPLAYED)
                val displayedUnavailableItems = filteredUnavailableProducts.take(MAX_TOTAL_PRODUCT_DISPLAYED)

                /**
                 * Add Available Products
                 */

                doIf(
                    predicate = filteredAvailableProducts.isNotEmpty(),
                    then = layout@ {
                        val areAllAvailableProductsSelected = filteredAvailableProducts.count { it.isSelected } == filteredAvailableProducts.size
                        val areAvailableProductsMoreThanDefaultDisplayed = filteredAvailableProducts.size > MAX_TOTAL_PRODUCT_DISPLAYED
                        val remainingTotalProduct = filteredAvailableProducts.size - displayedAvailableItems.size

                        _isTopCheckAllSelected.value = areAllAvailableProductsSelected

                        this@layout
                            .addTopCheckAllShoppingList(
                                productState = COLLAPSE,
                                isSelected = areAllAvailableProductsSelected
                            )
                            .addProducts(displayedAvailableItems)
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
                                predicate = filteredUnavailableProducts.isNotEmpty(),
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
                    predicate = filteredUnavailableProducts.isNotEmpty(),
                    then = layout@ {
                        val areUnavailableProductsMoreThanDefaultDisplayed = filteredUnavailableProducts.size > MAX_TOTAL_PRODUCT_DISPLAYED
                        val remainingTotalProduct = filteredUnavailableProducts.size - displayedUnavailableItems.size

                        this@layout
                            .addTitle("$EMPTY_STOCK_WIDGET_TITLE(${filteredUnavailableProducts.size})")
                            .addProducts(displayedUnavailableItems)
                            .doIf(
                                predicate = areUnavailableProductsMoreThanDefaultDisplayed,
                                then = {
                                    addExpandCollapse(
                                        productState = COLLAPSE,
                                        remainingTotalProduct = remainingTotalProduct,
                                        productLayoutType = UNAVAILABLE_SHOPPING_LIST
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
        isShoppingListAvailable: Boolean
    ) {
        mutableLayout
            .doIf(
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
                        .addProductCarts(cartProducts)
            }
        )
    }

    private fun addProductRecommendationSection(
        isShoppingListAvailable: Boolean
    ) {
        mutableLayout
            .doIf(
                predicate = filteredRecommendedProducts.isNotEmpty(),
                then = layout@ {
                    this@layout
                        .doIf(
                            predicate = isShoppingListAvailable,
                            then = {
                                addDivider()
                            }
                        )
                        .addTitle(recommendationModel.title)
                        .addProducts(filteredRecommendedProducts)
                        .doIf(
                            predicate = recommendationModel.hasNext,
                            then = {
                                addLoadMore()
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

    private fun filterShoppingListWithProductCart(
        miniCartData: MiniCartSimplifiedData?
    ) {
        filteredAvailableProducts.clear()
        filteredUnavailableProducts.clear()

        if (miniCartData == null) {
            filteredAvailableProducts.addAll(availableProducts)
            filteredUnavailableProducts.addAll(unavailableProducts)
        } else {
            val miniCartItems = miniCartData.miniCartItems.values.filterIsInstance<MiniCartItem.MiniCartItemProduct>()

            cartProducts.clear()

            val newAvailableProducts = cartProducts.addProductCartItem(
                productList = availableProducts.toMutableList(),
                miniCartItems = miniCartItems
            )

            val newUnavailableProducts = cartProducts.addProductCartItem(
                productList = unavailableProducts.toMutableList(),
                miniCartItems = miniCartItems
            )

            filteredAvailableProducts.addAll(newAvailableProducts)
            filteredUnavailableProducts.addAll(newUnavailableProducts)
        }
    }

    private fun filterProductRecommendationWithAvailableProduct() {
        val newRecommendedProducts = recommendedProducts.filteredBy(
            productList = availableProducts
        )

        filteredRecommendedProducts.clear()
        filteredRecommendedProducts.addAll(newRecommendedProducts)
    }

    private fun calculateDataForBottomBulkAtc() {
        _bottomBulkAtcData.value = BottomBulkAtcModel(
            counter = filteredAvailableProducts.count { it.isSelected },
            price = filteredAvailableProducts.filter { it.isSelected }.sumOf { it.priceInt }
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
        layout = mutableLayout.toList(),
        isRequiredToScrollUp = isRequiredToScrollUp
    )

    private fun loadErrorState(
        throwable: Throwable
    ) {
        mutableLayout.clear()

        mutableLayout.addErrorState(
            isFullPage = true,
            throwable = throwable
        )

        _layoutState.value = Error(getUpdatedLayout(), throwable)

        _isNavToolbarScrollingBehaviourEnabled.value = false
    }

    private fun loadLoadingState() {
        hideBottomWidget()

        mutableLayout.clear()

        mutableLayout.addLoadingState()

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
                getProductRecommendationDeferred(pageNumber = Int.ZERO)
            ).awaitAll()
        }

        /**
         * 1. Cast the results to their respective types.
         * 2. Set some variables as the source of truth
         * 2. Update layout
         */

        val shoppingListData = result.component2() as GetShoppingListDataResponse.Data
        val productRecommendationData = result.component3() as RecommendationWidget

        recommendationModel = RecommendationModel(
            pageCounter = if (productRecommendationData.hasNext) Int.ONE else Int.ZERO,
            hasNext = productRecommendationData.hasNext,
            title = productRecommendationData.title
        )

        availableProducts.clear()
        unavailableProducts.clear()
        recommendedProducts.clear()

        availableProducts.addAll(mapAvailableShoppingList(shoppingListData.listAvailableItem))
        unavailableProducts.addAll(mapUnavailableShoppingList(shoppingListData.listUnavailableItem))
        recommendedProducts.addAll(mapRecommendedProducts(productRecommendationData))

        updateLayout(
            isRequiredToScrollUp = true
        )

        /**
         * do others
         */

        hasLoadedLayout = true

        _isCoachMarkShown.value = true
    }

    private fun setDataToBottomWidget(
        miniCartData: MiniCartSimplifiedData?
    ) {
        if (filteredAvailableProducts.isNotEmpty()) {
            calculateDataForBottomBulkAtc()
            _isProductAvailable.value = true
            _miniCartState.value = Error(throwable = Throwable())
        } else if (miniCartData != null) {
            _isProductAvailable.value = false
            _miniCartState.value = Success(miniCartData.copy())
        } else {
            hideBottomWidget()
        }
    }

    private fun hideBottomWidget() {
        _isProductAvailable.value = false
        _miniCartState.value = Error(throwable = Throwable())
    }

    private fun updateLayout(
        isRequiredToScrollUp: Boolean = false
    ) {
        filterProductRecommendationWithAvailableProduct()
        filterShoppingListWithProductCart(mMiniCartData)

        mutableLayout.clear()

        val isShoppingListAvailable = availableProducts.isNotEmpty() || unavailableProducts.isNotEmpty()
        val isFilteredShoppingListAvailable =  filteredAvailableProducts.isNotEmpty() || filteredUnavailableProducts.isNotEmpty()

        addHeaderSection()
        addShoppingListSection(
            isShoppingListAvailable = isShoppingListAvailable
        )
        addProductCartWidgetSection(
            isShoppingListAvailable = isFilteredShoppingListAvailable
        )
        addProductRecommendationSection(
            isShoppingListAvailable = isShoppingListAvailable
        )

        _layoutState.value = Success(getUpdatedLayout(isRequiredToScrollUp))

        setDataToBottomWidget(mMiniCartData)
    }

    private fun onSuccessAddingToCart(
        productListSize: Int
    ) {
        getMiniCart {
            _toasterData.value = ToasterModel(
                text = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_success_to_add_product_to_cart, productListSize),
                actionText = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_success_for_cta),
                type = Toaster.TYPE_NORMAL,
                event = ADD_MULTI_PRODUCTS_TO_CART,
                any = mMiniCartData
            )
        }
    }

    private fun onErrorAddingToCart(
        messageError: String
    ) {
        _isLoaderDialogShown.value = false

        _toasterData.value = ToasterModel(
            text = messageError,
            actionText = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_for_cta),
            type = Toaster.TYPE_ERROR,
            event = ADD_MULTI_PRODUCTS_TO_CART
        )
    }

    private fun onSuccessAddingToWishlist(
        product: ShoppingListHorizontalProductCardItemUiModel
    ) {
        availableProducts
            .addProduct(
                product.copy(
                    productLayoutType = AVAILABLE_SHOPPING_LIST,
                    isSelected = true,
                    state = SHOW
                )
            )

        recommendedProducts
            .removeProduct(product.id)

        updateLayout()

        _toasterData.value = ToasterModel(
            text = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_success_to_add_product_to_shopping_list),
            actionText = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_success_for_cta),
            type = Toaster.TYPE_NORMAL,
            event = ADD_WISHLIST
        )
    }

    private fun onErrorAddingToWishlist(
        product: ShoppingListHorizontalProductCardItemUiModel
    ) {
        mutableLayout
            .modifyProduct(
                productId = product.id,
                state = SHOW
            )

        _layoutState.value = Success(getUpdatedLayout())

        _toasterData.value = ToasterModel(
            text = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_to_add_product_to_shopping_list),
            actionText = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_for_cta),
            type = Toaster.TYPE_ERROR,
            event = ADD_WISHLIST,
            any = product
        )
    }

    private fun onSuccessDeletingFromWishlist(
        product: ShoppingListHorizontalProductCardItemUiModel
    ) {
        if (product.productLayoutType == AVAILABLE_SHOPPING_LIST) availableProducts.removeProduct(product.id) else unavailableProducts.removeProduct(product.id)

        updateLayout()

        _toasterData.value = ToasterModel(
            text = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_success_to_delete_product_from_shopping_list),
            actionText = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_success_for_cta),
            type = Toaster.TYPE_NORMAL,
            event = DELETE_WISHLIST
        )
    }

    private fun onErrorDeletingFromWishlist(
        product: ShoppingListHorizontalProductCardItemUiModel
    ) {
        mutableLayout
            .modifyProduct(
                productId = product.id,
                state = SHOW
            )

        _layoutState.value = Success(getUpdatedLayout())

        _toasterData.value = ToasterModel(
            text = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_to_delete_product_from_shopping_list),
            actionText = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_for_cta),
            type = Toaster.TYPE_ERROR,
            event = DELETE_WISHLIST,
            any = product
        )
    }

    /**
     * -- public function section --
     */

    fun loadOutOfCoverageState() {
        mutableLayout.clear()

        mutableLayout
            .addHeader(
                headerModel = HeaderModel(
                    backgroundGradientColor = TokoNowThematicHeaderUiModel.GradientColor(
                        startColor = resourceProvider.getColor(unifyprinciplesR.color.Unify_Background),
                        endColor = resourceProvider.getColor(unifyprinciplesR.color.Unify_Background)
                    ),
                    isChooseAddressShown = true,
                    iconPullRefreshType = LayoutIconPullRefreshView.TYPE_GREEN
                ),
                state = SHOW
            )
            .addEmptyStateOoc()
            .addProductRecommendationOoc()

        _layoutState.value = Success(getUpdatedLayout())

        _isNavToolbarScrollingBehaviourEnabled.value = false
    }

    fun loadLayout() {
        loadLayoutJob?.cancel()
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
        mutableLayout
            .modifyExpandCollapseProducts(
                state = productState,
                productLayoutType = productLayoutType,
                products = if (productLayoutType == AVAILABLE_SHOPPING_LIST) filteredAvailableProducts else filteredUnavailableProducts
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
                availableProducts
                    .updateProductSelections(
                        isSelected = isSelected
                    )

                filteredAvailableProducts
                    .updateProductSelections(
                        isSelected = isSelected
                    )

                calculateDataForBottomBulkAtc()

                mutableLayout
                    .modifyTopCheckAll(
                        isSelected = isSelected
                    )
                    .modifyExpandCollapseProducts(
                        state = state,
                        productLayoutType = AVAILABLE_SHOPPING_LIST,
                        products = filteredAvailableProducts
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
        val topCheckAllUiModel = mutableLayout.firstOrNull { it is ShoppingListTopCheckAllUiModel } as? ShoppingListTopCheckAllUiModel

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
                availableProducts
                    .updateProductSelections(
                        productId = productId,
                        isSelected = isSelected
                    )

                filteredAvailableProducts
                    .updateProductSelections(
                        productId = productId,
                        isSelected = isSelected
                    )

                calculateDataForBottomBulkAtc()

                val areAllAvailableProductsSelected = filteredAvailableProducts.all { it.isSelected }

                mutableLayout
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
        mutableLayout
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
                    val productRecommendationData = getProductRecommendationDeferred(
                        pageNumber = recommendationModel.pageCounter
                    ).await()
                    recommendationModel.hasNext = productRecommendationData.hasNext

                    mutableLayout
                        .removeLoadMore()
                        .doIf(
                            predicate = productRecommendationData.recommendationItemList.isNotEmpty(),
                            then = layout@ {
                                recommendedProducts.addAll(mapRecommendedProducts(productRecommendationData))
                                this@layout
                                    .addProducts(recommendedProducts)
                                    .doIf(
                                        predicate = recommendationModel.hasNext,
                                        then = {
                                            addLoadMore()
                                            recommendationModel.pageCounter = recommendationModel.pageCounter.inc()
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
                    mutableLayout
                        .removeLoadMore()
                        .addRetry()

                    _layoutState.value = Success(getUpdatedLayout())
                }
            )
        }
    }

    fun getMiniCart(
        onGetMiniCartSuccess: () -> Unit = {}
    ) {
        getMiniCartJob?.cancel()
        getMiniCartJob = launchCatchError(block = {
            getMiniCartDeferred().await()

            updateLayout()

            _isLoaderDialogShown.value = false

            onGetMiniCartSuccess.invoke()
        }) {
            _miniCartState.value = Error(throwable = it)

            _isLoaderDialogShown.value = false
        }
    }

    fun setMiniCartData(
        miniCartData: MiniCartSimplifiedData
    ) {
        mMiniCartData = miniCartData

        updateLayout()
    }

    fun resumeLayout() {
        if (addressData.isChoosenAddressUpdated()) {
            refreshLayout()
        } else {
            if (hasLoadedLayout) {
                _isLoaderDialogShown.value = true
                getMiniCart()
            }
        }
    }

    fun addToWishlist(
        product: ShoppingListHorizontalProductCardItemUiModel
    ) {
        launchCatchError(
            block = {
                _toasterData.value = null

                mutableLayout
                    .modifyProduct(
                        productId = product.id,
                        state = LOADING
                    )

                _layoutState.value = Success(getUpdatedLayout())

                addToWishlistUseCase.setParams(
                    productId = product.id,
                    userId = userSession.userId
                )

                val response = addToWishlistUseCase.executeOnBackground()
                if (response is com.tokopedia.usecase.coroutines.Success) {
                    onSuccessAddingToWishlist(product)
                } else {
                    onErrorAddingToWishlist(product)
                }
            },
            onError = {
                onErrorAddingToWishlist(product)
            }
        )
    }

    fun deleteFromWishlist(
        product: ShoppingListHorizontalProductCardItemUiModel
    ) {
        launchCatchError(
            block = {
                _toasterData.value = null

                mutableLayout
                    .modifyProduct(
                        productId = product.id,
                        state = LOADING
                    )

                _layoutState.value = Success(getUpdatedLayout())

                deleteFromWishlistUseCase.setParams(
                    productId = product.id,
                    userId = userSession.userId
                )

                val response = deleteFromWishlistUseCase.executeOnBackground()
                if (response is com.tokopedia.usecase.coroutines.Success) {
                    onSuccessDeletingFromWishlist(product)
                } else {
                    onErrorDeletingFromWishlist(product)
                }
            },
            onError = {
                onErrorDeletingFromWishlist(product)
            }
        )
    }

    fun addMultiProductsToCart() {
        launchCatchError(
            block = {
                _toasterData.value = null
                _isLoaderDialogShown.value = true

                val addToCartMultiParams = filteredAvailableProducts
                    .filter { it.isSelected }
                    .map { product ->
                        AddToCartMultiParam(
                            productId = product.id,
                            productName = product.name,
                            productPrice = product.priceInt,
                            qty = product.minOrder,
                            shopId = product.shopId,
                            warehouseId = product.warehouseId
                        )
                    }

                val response = addToCartMultiUseCase.invoke(
                    params = ArrayList(addToCartMultiParams)
                )

                if (response is com.tokopedia.usecase.coroutines.Success && response.data.atcMulti.buyAgainData.success == SUCCESS_STATUS) {
                    onSuccessAddingToCart(addToCartMultiParams.size)
                } else if (response is com.tokopedia.usecase.coroutines.Success) {
                    onErrorAddingToCart(response.data.atcMulti.buyAgainData.message.firstOrNull().orEmpty())
                } else if (response is Fail) {
                    onErrorAddingToCart(resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_to_add_product_to_cart))
                }
            },
            onError = {
                onErrorAddingToCart(resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_to_add_product_to_cart))
            }
        )
    }

    fun getAvailableProducts(): List<ShoppingListHorizontalProductCardItemUiModel> = availableProducts

    fun isOutOfCoverage() = addressData.isOutOfCoverage()

    fun isLoggedIn() = userSession.isLoggedIn

    fun getShopId(): Long = addressData.getShopId()
}
