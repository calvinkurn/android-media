package com.tokopedia.tokopedianow.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryResponse
import com.tokopedia.tokopedianow.categorylist.domain.usecase.GetCategoryListUseCase
import com.tokopedia.tokopedianow.common.constant.ConstantKey.NO_VARIANT_PARENT_PRODUCT_ID
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryGridUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowLayoutUiModel
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.addEmptyStateIntoList
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.addLoadingIntoList
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.findNextIndex
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.isNotStaticLayout
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapGlobalHomeLayoutData
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapHomeCategoryGridData
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapHomeLayoutList
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapProductRecomData
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapTickerData
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.removeItem
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.updateStateToLoading
import com.tokopedia.tokopedianow.home.domain.mapper.TickerMapper
import com.tokopedia.tokopedianow.home.domain.model.SearchPlaceholder
import com.tokopedia.tokopedianow.home.domain.usecase.GetHomeLayoutDataUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.GetHomeLayoutListUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.GetKeywordSearchUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.GetTickerUseCase
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment.Companion.CATEGORY_LEVEL_DEPTH
import com.tokopedia.tokopedianow.home.presentation.uimodel.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TokoNowHomeViewModel @Inject constructor(
    private val getHomeLayoutListUseCase: GetHomeLayoutListUseCase,
    private val getHomeLayoutDataUseCase: GetHomeLayoutDataUseCase,
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val getKeywordSearchUseCase: GetKeywordSearchUseCase,
    private val getTickerUseCase: GetTickerUseCase,
    private val getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val getChooseAddressWarehouseLocUseCase: GetChosenAddressWarehouseLocUseCase,
    private val userSession: UserSessionInterface,
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
    val miniCartAdd: LiveData<Result<AddToCartDataModel>>
        get() = _miniCartAdd
    val miniCartUpdate: LiveData<Result<UpdateCartV2Data>>
        get() = _miniCartUpdate
    val miniCartRemove: LiveData<Result<String>>
        get() = _miniCartRemove

    private val _homeLayoutList = MutableLiveData<Result<HomeLayoutListUiModel>>()
    private val _keywordSearch = MutableLiveData<SearchPlaceholder>()
    private val _miniCart = MutableLiveData<Result<MiniCartSimplifiedData>>()
    private val _chooseAddress = MutableLiveData<Result<GetStateChosenAddressResponse>>()
    private val _miniCartWidgetDataUpdated = MutableLiveData<MiniCartSimplifiedData>()
    private val _miniCartAdd = MutableLiveData<Result<AddToCartDataModel>>()
    private val _miniCartUpdate = MutableLiveData<Result<UpdateCartV2Data>>()
    private val _miniCartRemove = MutableLiveData<Result<String>>()

    private var hasTickerBeenRemoved = false
    private val homeLayoutItemList = mutableListOf<HomeLayoutItemUiModel>()
    private var miniCartSimplifiedData: MiniCartSimplifiedData? = null

    fun getLoadingState() {
        homeLayoutItemList.clear()
        homeLayoutItemList.addLoadingIntoList()
        val data = HomeLayoutListUiModel(
                result = homeLayoutItemList,
                state = TokoNowLayoutState.LOADING,
                isInitialLoad = true
        )
        _homeLayoutList.postValue(Success(data))
    }

    fun getEmptyState(id: String) {
        homeLayoutItemList.clear()
        homeLayoutItemList.addEmptyStateIntoList(id)
        val data = HomeLayoutListUiModel(
                result = homeLayoutItemList,
                state = TokoNowLayoutState.HIDE
        )
        _homeLayoutList.postValue(Success(data))
    }

    /**
     * Get home layout structure without its content data.
     * Content data requested lazily for each component.
     * @see getLayoutData for loading content data.
     */
    fun getHomeLayout() {
        launchCatchError(block = {
            homeLayoutItemList.clear()
            val homeLayoutResponse = getHomeLayoutListUseCase.execute()
            homeLayoutItemList.mapHomeLayoutList(homeLayoutResponse, hasTickerBeenRemoved)
            val data = HomeLayoutListUiModel(
                result = homeLayoutItemList,
                state = TokoNowLayoutState.SHOW,
                isInitialLoad = true
            )
            _homeLayoutList.postValue(Success(data))
        }) {
            _homeLayoutList.postValue(Fail(it))
        }
    }

    /**
     * Get content data for visible layout component. Request content data
     * only for non static layout, see HomeLayoutMapper.STATIC_LAYOUT_ID.
     *
     * @param index current home layout item index
     * @param warehouseId Id obtained from choose address widget
     * @param firstVisibleItemIndex first item index visible on user screen
     * @param lastVisibleItemIndex last item index visible on user screen
     */
    fun getLayoutData(index: Int?, warehouseId: String, firstVisibleItemIndex: Int, lastVisibleItemIndex: Int) {
        launchCatchError(block = {
            if(index != null) {
                val item = homeLayoutItemList.getOrNull(index)
                val lastItemIndex = homeLayoutItemList.count() - 1
                val lastItemLoaded = index >= lastItemIndex
                val isLayoutVisible = index in firstVisibleItemIndex..lastVisibleItemIndex

                if (item != null && isLayoutVisible && shouldLoadLayout(item)) {
                    val layout = item.layout
                    setItemStateToLoading(item)
                    getLayoutComponentData(layout, warehouseId)
                }

                val nextItemIndex = homeLayoutItemList.findNextIndex()
                val isLoadDataFinished = lastItemLoaded || !isLayoutVisible || allItemLoaded()

                val data = HomeLayoutListUiModel(
                    result = homeLayoutItemList,
                    state = TokoNowLayoutState.SHOW,
                    nextItemIndex = nextItemIndex,
                    isInitialLoad = index == 0,
                    isLoadDataFinished = isLoadDataFinished
                )

                _homeLayoutList.postValue(Success(data))
            }
        }) {
            _homeLayoutList.postValue(Fail(it))
        }
    }

    /**
     * Get more layout data when user scroll through TokopediaNOW Home page.
     *
     * @param warehouseId Id obtained from choose address widget
     * @param firstVisibleItemIndex first item index visible on user screen
     * @param lastVisibleItemIndex last item index visible on user screen
     */
    fun getMoreLayoutData(warehouseId: String, firstVisibleItemIndex: Int, lastVisibleItemIndex: Int) {
        launchCatchError(block = {
            for (i in firstVisibleItemIndex..lastVisibleItemIndex) {
                val index = homeLayoutItemList.findNextIndex() ?: i
                val item = homeLayoutItemList.getOrNull(index)

                if (item != null && shouldLoadLayout(item)) {
                    val layout = item.layout
                    setItemStateToLoading(item)
                    getLayoutComponentData(layout, warehouseId)

                    val data = HomeLayoutListUiModel(
                        result = homeLayoutItemList,
                        state = TokoNowLayoutState.LOAD_MORE
                    )

                    _homeLayoutList.postValue(Success(data))
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
        if(!shopId.isNullOrEmpty() && warehouseId.toLongOrZero() != 0L && userSession.isLoggedIn) {
            launchCatchError(block = {
                getMiniCartUseCase.setParams(shopId)
                getMiniCartUseCase.execute({
                    setMiniCartSimplifiedData(it)
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
            homeLayoutItemList.mapHomeCategoryGridData(item, response)
            val data = HomeLayoutListUiModel(
                    result = homeLayoutItemList,
                    state = TokoNowLayoutState.SHOW
            )
            _homeLayoutList.postValue(Success(data))
        }) {
            homeLayoutItemList.mapHomeCategoryGridData(item, null)
            val data = HomeLayoutListUiModel(
                    result = homeLayoutItemList,
                    state = TokoNowLayoutState.SHOW
            )
            _homeLayoutList.postValue(Success(data))
        }
    }

    fun addProductToCart(recomItem: RecommendationItem, quantity: Int) {
        if (recomItem.quantity == quantity) return
        if (recomItem.quantity.isZero()) {
            addItemToCart(recomItem, quantity)
        } else {
            if (quantity.isZero()) {
                removeItemCart(recomItem)
            } else {
                updateItemCart(recomItem, quantity)
            }
        }
    }

    private fun addItemToCart(recomItem: RecommendationItem, quantity: Int) {
        val addToCartRequestParams = AddToCartUseCase.getMinimumParams(
            productId = recomItem.productId.toString(),
            shopId = recomItem.shopId.toString(),
            quantity = quantity,
        )
        addToCartUseCase.setParams(addToCartRequestParams)
        addToCartUseCase.execute({
            _miniCartAdd.value = Success(it)
        }, {
            _miniCartAdd.value = Fail(it)
        })
    }

    private fun updateItemCart(recomItem: RecommendationItem, quantity: Int) {
        val miniCartItem = miniCartSimplifiedData?.miniCartItems?.find { it.productId == recomItem.productId.toString() } ?: return
        miniCartItem.quantity = quantity
        val updateCartRequest = UpdateCartRequest(
            cartId = miniCartItem.cartId,
            quantity = miniCartItem.quantity,
            notes = miniCartItem.notes
        )
        updateCartUseCase.setParams(
            updateCartRequestList = listOf(updateCartRequest),
            source = UpdateCartUseCase.VALUE_SOURCE_UPDATE_QTY_NOTES,
        )
        updateCartUseCase.execute({
            _miniCartWidgetDataUpdated.value = miniCartSimplifiedData
            _miniCartUpdate.value = Success(it)
        }, {
            _miniCartUpdate.value = Fail(it)
        })
    }

    private fun removeItemCart(recomItem: RecommendationItem) {
        val miniCartItem = miniCartSimplifiedData?.miniCartItems?.find { it.productId == recomItem.productId.toString() } ?: return
        deleteCartUseCase.setParams(
            cartIdList = listOf(miniCartItem.cartId)
        )
        deleteCartUseCase.execute({
            _miniCartRemove.value = Success(miniCartItem.productId)
        }, {
            _miniCartRemove.value = Fail(it)
        })
    }

    fun updateProductCard(item: MiniCartSimplifiedData, needToObserve: Boolean) {
        val productRecomUiModel = homeLayoutItemList.find { it.layout is HomeProductRecomUiModel }?.layout as HomeProductRecomUiModel
        val recom = productRecomUiModel.recomWidget.copy()

        val groupVariant = item.miniCartItems.groupBy { it.productParentId }
        item.miniCartItems.map { miniCartItem ->
            recom.recommendationItemList.forEach { recommendationItem ->
                if (miniCartItem.productParentId == recommendationItem.parentID.toString() && miniCartItem.productParentId != NO_VARIANT_PARENT_PRODUCT_ID) {
                    recommendationItem.quantity = groupVariant[miniCartItem.productParentId]?.sumBy { it.quantity }.orZero()
                } else {
                    if (recommendationItem.productId.toString() == miniCartItem.productId) {
                        recommendationItem.quantity = miniCartItem.quantity
                    }
                }
            }
        }
        homeLayoutItemList.mapProductRecomData(productRecomUiModel, recom)

        if (needToObserve) {
            val data = HomeLayoutListUiModel(
                result = homeLayoutItemList,
                state = TokoNowLayoutState.SHOW
            )
            _homeLayoutList.postValue(Success(data))
        }
    }

    fun setMiniCartSimplifiedData(data: MiniCartSimplifiedData) {
        miniCartSimplifiedData = data
    }

    fun removeTickerWidget(id: String) {
        launchCatchError(block = {
            hasTickerBeenRemoved = true
            homeLayoutItemList.removeItem(id)

            val data = HomeLayoutListUiModel(
                result = homeLayoutItemList,
                state = TokoNowLayoutState.SHOW
            )

            _homeLayoutList.postValue(Success(data))
        }) {}
    }

    /**
     * Add home component mapping here.
     *
     * @param item layout visitable item
     * @param warehouseId Id obtained from choose address widget
     */
    private suspend fun getLayoutComponentData(item: Visitable<*>, warehouseId: String) {
        when (item) {
            is HomeComponentVisitable -> getGlobalHomeComponent(item) // Tokopedia Home Common Component
            is HomeLayoutUiModel -> getTokoNowHomeComponent(item) // TokoNow Home Component
            is TokoNowLayoutUiModel -> getTokoNowGlobalComponent(item, warehouseId) // TokoNow Common Component
        }
    }

    /**
     * Get data from additional query for TokopediaNOW Home Component.
     * Add use case and data mapping for TokopediaNOW Home Component here.
     * Example: Category Grid get its data from getCategoryListUseCase.
     *
     * @param item TokopediaNOW Home component item
     */
    private suspend fun getTokoNowHomeComponent(item: HomeLayoutUiModel) {
        when (item) {
            is HomeTickerUiModel -> getTickerData(item)
            is HomeProductRecomUiModel -> getHomeLayoutData(item)
        }
    }

    /**
     * Get data from additional query for TokopediaNOW Common Component.
     * Add use case and data mapping for TokopediaNOW Common Component here.
     * Example: Category Grid get its data from getCategoryListUseCase.
     *
     * @param item TokopediaNOW component item
     * @param warehouseId Id obtained from choose address widget
     */
    private suspend fun getTokoNowGlobalComponent(item: TokoNowLayoutUiModel, warehouseId: String) {
        when (item) {
            is TokoNowCategoryGridUiModel -> getCategoryGridData(item, warehouseId)
        }
    }

    /**
     * Get data from dynamic home channel query for Tokopedia Home Common Component.
     * Add mapping to HomeLayoutMapper.mapGlobalHomeLayoutData -> mapToHomeUiModel
     * for each global home components.
     *
     * @param item Tokopedia Home component item
     */
    private suspend fun getGlobalHomeComponent(item: HomeComponentVisitable) {
        asyncCatchError(block = {
            val channelId = item.visitableId()
            val response = getHomeLayoutDataUseCase.execute(channelId)
            homeLayoutItemList.mapGlobalHomeLayoutData(item, response)
        }) {
            val id = item.visitableId().orEmpty()
            homeLayoutItemList.removeItem(id)
        }.await()
    }

    private suspend fun getHomeLayoutData(item: HomeLayoutUiModel) {
        asyncCatchError(block = {
            val channelId = item.visitableId
            val response = getHomeLayoutDataUseCase.execute(channelId)
            homeLayoutItemList.mapGlobalHomeLayoutData(item, response)
            miniCartSimplifiedData?.let {
                updateProductCard(it, false)
            }
        }) {
            val id = item.visitableId
            homeLayoutItemList.removeItem(id)
        }.await()
    }

    private suspend fun getCategoryGridData(item: TokoNowCategoryGridUiModel, warehouseId: String) {
        asyncCatchError(block = {
            val response = getCategoryList(warehouseId)
            homeLayoutItemList.mapHomeCategoryGridData(item, response)
        }) {
            homeLayoutItemList.mapHomeCategoryGridData(item, emptyList())
        }.await()
    }

    private suspend fun getTickerData(item: HomeTickerUiModel) {
        asyncCatchError(block = {
            val tickerList = getTickerUseCase.execute().ticker.tickerList
            val tickerData = TickerMapper.mapTickerData(tickerList)
            homeLayoutItemList.mapTickerData(item, tickerData)
        }) {
            homeLayoutItemList.removeItem(item.id)
        }.await()
    }

    private suspend fun getCategoryList(warehouseId: String): List<CategoryResponse> {
        return getCategoryListUseCase.execute(warehouseId, CATEGORY_LEVEL_DEPTH).data
    }

    private fun shouldLoadLayout(item: HomeLayoutItemUiModel): Boolean {
        return item.state != HomeLayoutItemState.LOADING &&
                item.state != HomeLayoutItemState.LOADED &&
                item.layout.isNotStaticLayout()
    }

    private fun setItemStateToLoading(item: HomeLayoutItemUiModel) {
        homeLayoutItemList.updateStateToLoading(item)
    }

    private fun allItemLoaded(): Boolean = homeLayoutItemList.all {
        it.state == HomeLayoutItemState.LOADED
    }
}