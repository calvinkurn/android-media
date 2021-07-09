package com.tokopedia.tokopedianow.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryResponse
import com.tokopedia.tokopedianow.categorylist.domain.usecase.GetCategoryListUseCase
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.constant.ConstantKey.NO_VARIANT_PARENT_PRODUCT_ID
import com.tokopedia.tokopedianow.home.domain.mapper.*
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.addEmptyStateIntoList
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.addLoadingIntoList
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.isNotStaticLayout
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapGlobalHomeLayoutData
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapHomeCategoryGridData
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapHomeLayoutList
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.updateStateToLoading
import com.tokopedia.tokopedianow.home.domain.mapper.TickerMapper.mapTickerData
import com.tokopedia.tokopedianow.home.domain.model.SearchPlaceholder
import com.tokopedia.tokopedianow.home.domain.model.Ticker
import com.tokopedia.tokopedianow.home.domain.usecase.GetHomeLayoutDataUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.GetHomeLayoutListUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.GetKeywordSearchUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.GetTickerUseCase
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment.Companion.CATEGORY_LEVEL_DEPTH
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryGridUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutListUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TokoNowHomeViewModel @Inject constructor(
    private val getHomeLayoutListUseCase: GetHomeLayoutListUseCase,
    private val getHomeLayoutDataUseCase: GetHomeLayoutDataUseCase,
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val getKeywordSearchUseCase: GetKeywordSearchUseCase,
    private val getTickerUseCase: GetTickerUseCase,
    private val getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    private val getChooseAddressWarehouseLocUseCase: GetChosenAddressWarehouseLocUseCase,
    private val dispatchers: CoroutineDispatchers,
) : BaseViewModel(dispatchers.io) {

    val homeLayoutList: LiveData<Result<HomeLayoutListUiModel>>
        get() = _homeLayoutList
    val keywordSearch: LiveData<SearchPlaceholder>
        get() = _keywordSearch
    val miniCart: LiveData<Result<MiniCartSimplifiedData>>
        get() = _miniCart
    val chooseAddress: LiveData<Result<GetStateChosenAddressResponse>>
        get() = _chooseAddress
    val miniCartWidgetDataUpdated: LiveData<MiniCartSimplifiedData>
        get() = _miniCartWidgetDataUpdated
    val updatedVisitableIndicesLiveData: LiveData<List<Int>>
        get() = updatedVisitableIndicesMutableLiveData

    private val _homeLayoutList = MutableLiveData<Result<HomeLayoutListUiModel>>()
    private val _keywordSearch = MutableLiveData<SearchPlaceholder>()
    private val _miniCart = MutableLiveData<Result<MiniCartSimplifiedData>>()
    private val _chooseAddress = MutableLiveData<Result<GetStateChosenAddressResponse>>()
    private val _miniCartWidgetDataUpdated = MutableLiveData<MiniCartSimplifiedData>()
    private val updatedVisitableIndicesMutableLiveData = SingleLiveEvent<List<Int>>()

    private var homeLayoutItemList = listOf<HomeLayoutItemUiModel>()
    private var cartItemsNonVariant: List<MiniCartItem>? = null
    private var cartItemsVariantGrouped: Map<String, List<MiniCartItem>>? = null

    fun getLoadingState() {
        homeLayoutItemList = addLoadingIntoList()
        val data = HomeLayoutListUiModel(
                result = homeLayoutItemList,
                state = TokoNowLayoutState.LOADING
        )
        _homeLayoutList.value = Success(data)
    }

    fun getEmptyState(id: String) {
        homeLayoutItemList = addEmptyStateIntoList(id)
        val data = HomeLayoutListUiModel(
                result = homeLayoutItemList,
                state = TokoNowLayoutState.HIDE
        )
        _homeLayoutList.value = Success(data)
    }

    fun getHomeLayout(hasTickerBeenRemoved: Boolean) {
        launchCatchError(block = {
            val homeLayoutResponse = getHomeLayoutListUseCase.execute()
            val tickerList = getTicker(hasTickerBeenRemoved)
            homeLayoutItemList = mapHomeLayoutList(
                homeLayoutResponse,
                mapTickerData(tickerList)
            )
            val data = HomeLayoutListUiModel(
                result = homeLayoutItemList,
                state = TokoNowLayoutState.SHOW
            )
            _homeLayoutList.postValue(Success(data))
        }) {
            _homeLayoutList.postValue(Fail(it))
        }
    }

    fun getInitialLayoutData(index: Int, warehouseId: String, isLayoutVisible: Boolean) {
        launchCatchError(block = {
            val lastItemIndex = homeLayoutItemList.count() - 1
            val lastItemLoaded = index > lastItemIndex
            val isInitialLoadFinished = lastItemLoaded || !isLayoutVisible
            val item = homeLayoutItemList.getOrNull(index)

            if (item != null && isLayoutVisible && shouldLoadLayout(item)) {
                setItemStateToLoading(item)
                when (val layout = item.layout) {
                    is TokoNowCategoryGridUiModel -> {
                        homeLayoutItemList = getCategoryGridData(layout, warehouseId)
                    }
                    is HomeComponentVisitable -> {
                        homeLayoutItemList = getGlobalHomeComponent(layout)
                    }
                    is HomeLayoutUiModel -> {
                        homeLayoutItemList = getGlobalHomeComponent(layout)
                    }
                }
            }

            val data = HomeLayoutListUiModel(
                result = homeLayoutItemList,
                state = TokoNowLayoutState.SHOW,
                nextItemIndex = index + 1,
                isInitialLoad = index == 0,
                isInitialLoadFinished = isInitialLoadFinished
            )

            withContext(dispatchers.main) {
                _homeLayoutList.value = Success(data)
            }
        }) {
            _homeLayoutList.postValue(Fail(it))
        }
    }

    fun getMoreLayoutData(warehouseId: String, firstVisibleItemIndex: Int, lastVisibleItemIndex: Int) {
        launchCatchError(block = {
            for (index in firstVisibleItemIndex..lastVisibleItemIndex) {
                val item = homeLayoutItemList.getOrNull(index)

                if (item != null && shouldLoadLayout(item)) {
                    setItemStateToLoading(item)
                    when (val layout = item.layout) {
                        is TokoNowCategoryGridUiModel -> {
                            homeLayoutItemList = getCategoryGridData(layout, warehouseId)
                        }
                        is HomeComponentVisitable -> {
                            homeLayoutItemList = getGlobalHomeComponent(layout)
                        }
                        is HomeLayoutUiModel -> {
                            homeLayoutItemList = getGlobalHomeComponent(layout)
                        }
                    }

                    val data = HomeLayoutListUiModel(
                        result = homeLayoutItemList,
                        state = TokoNowLayoutState.LOAD_MORE
                    )

                    withContext(dispatchers.main) {
                        _homeLayoutList.value = Success(data)
                    }
                }
            }
        }) {
            _homeLayoutList.postValue(Fail(it))
        }
    }

    fun getKeywordSearch(isFirstInstall: Boolean, deviceId: String, userId: String) {
        launchCatchError(coroutineContext, block = {
            val response = getKeywordSearchUseCase.execute(isFirstInstall, deviceId, userId)
            _keywordSearch.postValue(response.searchData)
        }) {}
    }

    fun getMiniCart(shopId: List<String>, warehouseId: String?) {
        if(!shopId.isNullOrEmpty() && warehouseId.toLongOrZero() != 0L) {
            launchCatchError(block = {
                getMiniCartUseCase.setParams(shopId)
                getMiniCartUseCase.execute({
                    _miniCart.postValue(Success(it))
                }, {
                    _miniCart.postValue(Fail(it))
                })
            }) {
                _miniCart.postValue(Fail(it))
            }
        }
    }

    fun getChooseAddress(source: String){
        getChooseAddressWarehouseLocUseCase.getStateChosenAddress( {
            _chooseAddress.postValue(Success(it))
        },{
            _chooseAddress.postValue(Fail(it))
        }, source)
    }

    fun getCategoryGrid(item: TokoNowCategoryGridUiModel, warehouseId: String) {
        launchCatchError(block = {
            val response = getCategoryList(warehouseId)
            val homeLayoutItemList = homeLayoutItemList
                .mapHomeCategoryGridData(item, response)
            val data = HomeLayoutListUiModel(
                    result = homeLayoutItemList,
                    state = TokoNowLayoutState.SHOW
            )
            _homeLayoutList.postValue(Success(data))
        }) {
            val homeLayoutItemList = homeLayoutItemList
                .mapHomeCategoryGridData(item, null)
            val data = HomeLayoutListUiModel(
                    result = homeLayoutItemList,
                    state = TokoNowLayoutState.SHOW
            )
            _homeLayoutList.postValue(Success(data))
        }
    }

    fun updateCartItems(miniCartSimplifiedData: MiniCartSimplifiedData, item: HomeProductRecomUiModel) {
        updateMiniCartWidgetData(miniCartSimplifiedData)

        viewModelScope.launch {
            updateMiniCartInBackground(miniCartSimplifiedData, item)
        }
    }

    private fun updateMiniCartWidgetData(miniCartSimplifiedData: MiniCartSimplifiedData) {
        _miniCartWidgetDataUpdated.value = miniCartSimplifiedData
    }

    private suspend fun updateMiniCartInBackground(miniCartSimplifiedData: MiniCartSimplifiedData, item: HomeProductRecomUiModel) {
        withContext(dispatchers.io) {
            updateMiniCartProperties(miniCartSimplifiedData)

            if (item.productCards.isNullOrEmpty()) return@withContext

            val updatedProductIndices = mutableListOf<Int>()

            item.productCards.forEachIndexed { position, product ->
                updateProductItemQuantity(position, product, updatedProductIndices)
            }
            withContext(dispatchers.main) {
                updatedVisitableIndicesMutableLiveData.value = updatedProductIndices
            }
        }
    }

    private fun updateMiniCartProperties(miniCartSimplifiedData: MiniCartSimplifiedData) {
        val cartItemsPartition = splitCartItemsVariantAndNonVariant(miniCartSimplifiedData.miniCartItems)
        cartItemsNonVariant = cartItemsPartition.first
        cartItemsVariantGrouped = cartItemsPartition.second.groupBy { it.productParentId }
    }

    private fun splitCartItemsVariantAndNonVariant(miniCartItems: List<MiniCartItem>) = miniCartItems.partition {
        it.productParentId == NO_VARIANT_PARENT_PRODUCT_ID
    }

    private fun updateProductItemQuantity(
        position: Int,
        productItem: HomeProductCardUiModel,
        updatedProductIndices: MutableList<Int>,
    ) {
        val productId = productItem.id
        val parentProductId = productItem.parentProductId
        val nonVariantATC = productItem.productCardModel.nonVariant
        val variantATC = productItem.productCardModel.variant

        if (nonVariantATC != null) {
            val quantity = getProductNonVariantQuantity(productId)
            if (nonVariantATC.quantity != quantity) {
                productItem.productCardModel = productItem.productCardModel.copy(
                    nonVariant = productItem.productCardModel.nonVariant?.copy(
                        quantity = quantity
                    )
                )
                updatedProductIndices.add(position)
            }
        } else if (variantATC != null) {
            val totalQuantity = getProductVariantTotalQuantity(parentProductId)
            if (variantATC.quantity != totalQuantity) {
                productItem.productCardModel = productItem.productCardModel.copy(
                    variant = productItem.productCardModel.variant?.copy(
                        quantity = totalQuantity
                    )
                )
                updatedProductIndices.add(position)
            }
        }
    }

    private fun getProductNonVariantQuantity(productId: String): Int {
        val cartItem = cartItemsNonVariant?.find { it.productId == productId }
        return cartItem?.quantity.orZero()
    }

    private fun getProductVariantTotalQuantity(parentProductId: String): Int {
        val cartItemsVariantGrouped = cartItemsVariantGrouped
        val miniCartItemsWithSameParentId = cartItemsVariantGrouped?.get(parentProductId)
        val totalQuantity = miniCartItemsWithSameParentId?.sumBy { it.quantity }
        return totalQuantity.orZero()
    }

    private suspend fun getCategoryList(warehouseId: String): List<CategoryResponse> {
        return getCategoryListUseCase.execute(warehouseId, CATEGORY_LEVEL_DEPTH).data
    }

    private suspend fun getTicker(hasTickerBeenRemoved: Boolean): List<Ticker> {
        return if (!hasTickerBeenRemoved) {
            getTickerUseCase.execute()
                .ticker
                .tickerList
        } else {
            emptyList()
        }
    }

    private suspend fun getCategoryGridData(
        item: TokoNowCategoryGridUiModel,
        warehouseId: String
    ): List<HomeLayoutItemUiModel> {
        val response = getCategoryList(warehouseId)
        return homeLayoutItemList.mapHomeCategoryGridData(item, response)
    }

    private suspend fun getGlobalHomeComponent(
        item: HomeComponentVisitable
    ): List<HomeLayoutItemUiModel> {
        val channelId = item.visitableId()
        val response = getHomeLayoutDataUseCase.execute(channelId)
        return homeLayoutItemList.mapGlobalHomeLayoutData(item, response)
    }

    private suspend fun getGlobalHomeComponent(
        item: HomeLayoutUiModel
    ): List<HomeLayoutItemUiModel> {
        val channelId = item.visitableId
        val response = getHomeLayoutDataUseCase.execute(channelId)
        return homeLayoutItemList.mapGlobalHomeLayoutData(item, response)
    }

    private fun shouldLoadLayout(item: HomeLayoutItemUiModel): Boolean {
        return item.state != HomeLayoutItemState.LOADING &&
                item.state != HomeLayoutItemState.LOADED &&
                item.layout.isNotStaticLayout()
    }

    private fun setItemStateToLoading(item: HomeLayoutItemUiModel) {
        homeLayoutItemList = homeLayoutItemList.updateStateToLoading(item)
    }
}