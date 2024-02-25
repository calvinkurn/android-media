package com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.common.base.viewmodel.BaseTokoNowViewModel
import com.tokopedia.tokopedianow.common.constant.ConstantValue.X_DEVICE_RECOMMENDATION_PARAM
import com.tokopedia.tokopedianow.common.constant.ConstantValue.X_SOURCE_RECOMMENDATION_PARAM
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState.Companion.SHOW
import com.tokopedia.tokopedianow.common.domain.mapper.AddressMapper.mapToWarehousesData
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.model.UiState
import com.tokopedia.tokopedianow.common.model.UiState.Success
import com.tokopedia.tokopedianow.common.model.UiState.Loading
import com.tokopedia.tokopedianow.common.model.UiState.Error
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
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
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addProductInCartWidget
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
import com.tokopedia.tokopedianow.shoppinglist.domain.model.GetShoppingListDataResponse
import com.tokopedia.tokopedianow.shoppinglist.domain.usecase.GetShoppingListUseCase
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.HeaderModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.LayoutModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListTopCheckAllUiModel
import com.tokopedia.tokopedianow.shoppinglist.util.Constant.MAX_TOTAL_PRODUCT_DISPLAYED
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class TokoNowShoppingListViewModel @Inject constructor(
    private val addressData: TokoNowLocalAddress,
    private val productRecommendationUseCase: GetSingleRecommendationUseCase,
    private val userSession: UserSessionInterface,
    private val getShoppingListUseCase: GetShoppingListUseCase,
    getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    addToCartUseCase: AddToCartUseCase,
    updateCartUseCase: UpdateCartUseCase,
    deleteCartUseCase: DeleteCartUseCase,
    affiliateService: NowAffiliateService,
    getTargetedTickerUseCase: GetTargetedTickerUseCase,
    dispatchers: CoroutineDispatchers
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
    private companion object {
        const val PRODUCT_RECOMMENDATION_PAGE_NAME = "tokonow_shopping_list"
        const val PRODUCT_RECOMMENDATION_PAGE_NUMBER_COUNTER = 1
    }

    /**
     * -- private variable section --
     */

    private val layout = mutableListOf<Visitable<*>> ()
    private val availableProducts = mutableListOf<ShoppingListHorizontalProductCardItemUiModel>()
    private val unavailableProducts = mutableListOf<ShoppingListHorizontalProductCardItemUiModel>()

    private val _uiState: MutableStateFlow<UiState<LayoutModel>> = MutableStateFlow(Loading(LayoutModel(layout.addLoadingState())))
    private val _isOnScrollNotNeeded: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _isNavToolbarScrollingBehaviourEnabled: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val _isTopCheckAllSelected: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _isStickyTopCheckAllScrollingBehaviourEnabled: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private var pageCounter: Int = PRODUCT_RECOMMENDATION_PAGE_NUMBER_COUNTER
    private var job: Job? = null

    /**
     * -- public variable section --
     */

    val uiState
        get() = _uiState.asStateFlow()
    val isOnScrollNotNeeded
        get() = _isOnScrollNotNeeded.asStateFlow()
    val isNavToolbarScrollingBehaviourEnabled
        get() = _isNavToolbarScrollingBehaviourEnabled.asStateFlow()
    val isTopCheckAllSelected
        get() = _isTopCheckAllSelected.asStateFlow()
    val isStickyTopCheckAllScrollingBehaviourEnabled
        get() = _isStickyTopCheckAllScrollingBehaviourEnabled.asStateFlow()

    var headerModel: HeaderModel = HeaderModel()

    /**
     * -- private suspend function section --
     */

    private suspend fun getShoppingListDeffered() = async {
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

    /**
     * -- private function section --
     */

    private fun addHeaderSection() {
        layout.addHeader(
            headerModel = headerModel,
            state = SHOW
        )
    }

    private fun addShoppingListSection(shoppingListData: GetShoppingListDataResponse.Data) {
        availableProducts.clear()
        unavailableProducts.clear()

        availableProducts.addAll(mapAvailableShoppingList(shoppingListData.listAvailableItem))
        unavailableProducts.addAll(mapUnavailableShoppingList(shoppingListData.listUnavailableItem))

        if (availableProducts.isNotEmpty() || unavailableProducts.isNotEmpty()) {
            val displayedAvailableItems = availableProducts.take(MAX_TOTAL_PRODUCT_DISPLAYED)
            val displayedUnavailableItems = unavailableProducts.take(MAX_TOTAL_PRODUCT_DISPLAYED)
            _isStickyTopCheckAllScrollingBehaviourEnabled.value = availableProducts.isNotEmpty()

            layout
                .doIf(availableProducts.isNotEmpty()) {
                    val isTopCheckAllSelected = availableProducts.count { it.isSelected } == availableProducts.size
                    _isTopCheckAllSelected.value = isTopCheckAllSelected

                    layout
                        .addTopCheckAllShoppingList(
                            productState = COLLAPSE,
                            isSelected = isTopCheckAllSelected
                        )
                        .addShoppingListProducts(displayedAvailableItems)
                        .doIf(availableProducts.size > MAX_TOTAL_PRODUCT_DISPLAYED) {
                            layout.addExpandCollapse(
                                productState = COLLAPSE,
                                remainingTotalProduct = availableProducts.size - displayedAvailableItems.size,
                                productLayoutType = AVAILABLE_SHOPPING_LIST
                            )
                        }
                        .doIf(unavailableProducts.isNotEmpty()) {
                            layout.addDivider()
                        }
                }
                .doIf(unavailableProducts.isNotEmpty()) {
                    layout
                        .addTitle(headerModel.emptyStockTitle)
                        .addShoppingListProducts(displayedUnavailableItems)
                        .doIf(unavailableProducts.size > MAX_TOTAL_PRODUCT_DISPLAYED) {
                            layout.addExpandCollapse(
                                productState = COLLAPSE,
                                remainingTotalProduct = unavailableProducts.size - displayedUnavailableItems.size,
                                productLayoutType = ShoppingListProductLayoutType.UNAVAILABLE_SHOPPING_LIST
                            )
                        }
                }
        } else {
            layout.addEmptyShoppingList()
        }
    }

    private fun addProductInCartSection() {
        // do some logic
        layout.addDivider()
        layout.addTitle(
            title = "5 produk ada di keranjang"
        )
        layout.addProductInCartWidget()
    }

    private fun addProductRecommendationSection(productRecommendation: RecommendationWidget) {
        if (productRecommendation.recommendationItemList.isNotEmpty()) {
            layout
                .addDivider()
                .addTitle(title = productRecommendation.title)
                .addRecommendedProducts(productRecommendation)

            if (productRecommendation.hasNext) {
                layout.addLoadMore()
                pageCounter++
            } else {
                _isOnScrollNotNeeded.value = true
            }
        } else {
            _isOnScrollNotNeeded.value = true
        }
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

        _uiState.value = Error(getUpdatedLayout(), throwable)

        _isNavToolbarScrollingBehaviourEnabled.value = false
    }

    private fun loadLoadingState() {
        layout.clear()
        layout.addLoadingState()

        _uiState.value = Loading(getUpdatedLayout())

        _isNavToolbarScrollingBehaviourEnabled.value = true
    }

    private fun loadSuccessState() {
        /**
         * block thread until the coroutine inside runBlocking completes
         */
        val result = runBlocking {
            listOf(
                getShoppingListDeffered(),
                getProductRecommendationDeferred()
            ).awaitAll()
        }

        /**
         * cast the results to their respective types
         */
        val shoppingList = result.first() as GetShoppingListDataResponse.Data
        val productRecommendation = result.last() as RecommendationWidget

        layout.clear()

        addHeaderSection()
        addShoppingListSection(shoppingList)
        addProductRecommendationSection(productRecommendation)

        _uiState.value = Success(getUpdatedLayout(isRequiredToScrollUp = true))
    }

    /**
     * -- public function section --
     */

    fun loadLayout() {
        job = launchCatchError(
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
            ).modifyExpandCollapseState(
                productState = productState,
                productLayoutType = productLayoutType
            ).doIf(productLayoutType == AVAILABLE_SHOPPING_LIST) {
                layout.modifyTopCheckAllState(
                    productState = productState
                )
            }

        _uiState.value = Success(getUpdatedLayout())
    }

    fun selectAllAvailableProducts(
        state: ShoppingListProductState,
        isSelected: Boolean
    ) {
        val tempAvailableProducts = availableProducts.map { it.copy(isSelected = isSelected) }.toList()
        availableProducts.clear()
        availableProducts.addAll(tempAvailableProducts)

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

        _uiState.value = Success(getUpdatedLayout())
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
        val tempAvailableProducts = availableProducts.map { product -> if (product.id == productId) product.copy(isSelected = isSelected) else product }.toList()
        availableProducts.clear()
        availableProducts.addAll(tempAvailableProducts)

        val isTopCheckAllSelected = availableProducts.all { it.isSelected }

        layout
            .modifyTopCheckAll(
                isSelected = isTopCheckAllSelected
            )
            .modifyProduct(
                productId = productId,
                isSelected = isSelected
            )

        _isTopCheckAllSelected.value = isTopCheckAllSelected

        _uiState.value = Success(getUpdatedLayout())
    }

    fun switchRetryToLoadMore() {
        layout
            .removeRetry()
            .addLoadMore()

        _uiState.value = Success(getUpdatedLayout())
    }

    fun loadMoreProductRecommendation(
        isLastVisibleLoadingMore: Boolean
    ) {
        if (isLastVisibleLoadingMore && job?.isCompleted.orFalse()) {
            job = launchCatchError(
                block = {
                    val productRecommendation = getProductRecommendationDeferred().await()

                    layout.removeLoadMore()

                    if (productRecommendation.recommendationItemList.isNotEmpty()) {
                        layout.addRecommendedProducts(productRecommendation)

                        if (productRecommendation.hasNext) {
                            layout.addLoadMore()
                            pageCounter++
                        } else {
                            _isOnScrollNotNeeded.value = true
                        }
                    } else {
                        _isOnScrollNotNeeded.value = true
                    }

                    _uiState.value = Success(getUpdatedLayout())
                },
                onError = {
                    layout
                        .removeLoadMore()
                        .addRetry()

                    _uiState.value = Success(getUpdatedLayout())
                }
            )
        }
    }
}
