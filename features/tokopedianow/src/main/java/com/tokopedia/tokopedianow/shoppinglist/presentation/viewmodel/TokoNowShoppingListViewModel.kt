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
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.CommonVisitableMapper.addRecommendedProducts
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addLoadingState
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addAvailableShoppingList
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addDivider
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addEmptyShoppingList
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addErrorState
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addHeader
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addIf
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addLoadMore
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addProductInCartWidget
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addRetry
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addTitle
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addTopCheckAllShoppingList
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addUnavailableShoppingList
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.removeLoadMore
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.removeRetry
import com.tokopedia.tokopedianow.shoppinglist.domain.model.GetShoppingListDataResponse
import com.tokopedia.tokopedianow.shoppinglist.domain.usecase.GetShoppingListUseCase
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.HeaderModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.LayoutModel
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

    private val layout: MutableList<Visitable<*>> = arrayListOf()

    private val _isOnScrollNotNeeded: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _isNavToolbarScrollingBehaviourEnabled: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val _uiState: MutableStateFlow<UiState<LayoutModel>> = MutableStateFlow(Loading(LayoutModel(layout.addLoadingState())))

    private var pageCounter: Int = PRODUCT_RECOMMENDATION_PAGE_NUMBER_COUNTER
    private var job: Job? = null

    /**
     * -- public variable section --
     */

    val isOnScrollNotNeeded
        get() = _isOnScrollNotNeeded.asStateFlow()
    val isNavToolbarScrollingBehaviourEnabled
        get() = _isNavToolbarScrollingBehaviourEnabled.asStateFlow()
    val uiState
        get() = _uiState.asStateFlow()

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
        val availableItems = shoppingListData.listAvailableItem
        val unavailableItems = shoppingListData.listUnavailableItem

        if (availableItems.isNotEmpty() || unavailableItems.isNotEmpty()) {
            layout
                .addIf(availableItems.isNotEmpty()) {
                    layout
                        .addTopCheckAllShoppingList(shoppingListData.metadata)
                        .addAvailableShoppingList(shoppingListData.listAvailableItem)
                        .addIf(unavailableItems.isNotEmpty()) {
                            layout.addDivider()
                        }
                }
                .addIf(unavailableItems.isNotEmpty()) {
                    layout
                        .addTitle(headerModel.emptyStockTitle)
                        .addUnavailableShoppingList(shoppingListData.listUnavailableItem)
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

    private fun getUpdatedLayout(
        isRequiredToScrollUp: Boolean = false
    ): LayoutModel = LayoutModel(
        layout = layout.toList(),
        isRequiredToScrollUp = isRequiredToScrollUp
    )

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

    private fun loadErrorState(
        throwable: Throwable
    ) {
        layout.clear()
        layout.addErrorState(throwable)

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
