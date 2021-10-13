package com.tokopedia.tokopedianow.home.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryResponse
import com.tokopedia.tokopedianow.categorylist.domain.usecase.GetCategoryListUseCase
import com.tokopedia.tokopedianow.common.constant.ConstantValue.PAGE_NAME_RECOMMENDATION_OOC_PARAM
import com.tokopedia.tokopedianow.common.constant.ConstantValue.X_DEVICE_RECOMMENDATION_PARAM
import com.tokopedia.tokopedianow.common.constant.ConstantValue.X_SOURCE_RECOMMENDATION_PARAM
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryGridUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowLayoutUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowRecentPurchaseUiModel
import com.tokopedia.tokopedianow.home.analytic.HomeAddToCartTracker
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.PRODUCT_RECOM
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.RECENT_PURCHASE
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.addEmptyStateIntoList
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.addLoadingIntoList
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.addMoreHomeLayout
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.addProductRecomOoc
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.addProgressBar
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapEducationalInformationData
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapHomeCategoryGridData
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapHomeLayoutList
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapProductPurchaseData
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapSharingEducationData
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.mapTickerData
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.removeItem
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.removeProgressBar
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.setQuantityToZero
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.updateProductQuantity
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.updateProductRecomQuantity
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.updateRecentPurchaseQuantity
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.setStateToLoading
import com.tokopedia.tokopedianow.home.domain.mapper.TickerMapper
import com.tokopedia.tokopedianow.home.domain.model.SearchPlaceholder
import com.tokopedia.tokopedianow.home.domain.usecase.GetHomeLayoutDataUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.GetKeywordSearchUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.GetRecentPurchaseUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.GetTickerUseCase
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment.Companion.CATEGORY_LEVEL_DEPTH
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment.Companion.DEFAULT_QUANTITY
import com.tokopedia.tokopedianow.home.presentation.uimodel.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Deferred
import javax.inject.Inject

class TokoNowHomeViewModel @Inject constructor(
    private val getHomeLayoutDataUseCase: GetHomeLayoutDataUseCase,
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val getKeywordSearchUseCase: GetKeywordSearchUseCase,
    private val getTickerUseCase: GetTickerUseCase,
    private val getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val getRecommendationUseCase: GetRecommendationUseCase,
    private val getChooseAddressWarehouseLocUseCase: GetChosenAddressWarehouseLocUseCase,
    private val getRecentPurchaseUseCase: GetRecentPurchaseUseCase,
    private val userSession: UserSessionInterface,
    dispatchers: CoroutineDispatchers,
) : BaseViewModel(dispatchers.io) {

    val homeLayoutList: LiveData<Result<HomeLayoutListUiModel>>
        get() = _homeLayoutList
    val keywordSearch: LiveData<SearchPlaceholder>
        get() = _keywordSearch
    val miniCart: LiveData<Result<MiniCartSimplifiedData>>
        get() = _miniCart
    val chooseAddress: LiveData<Result<GetStateChosenAddressResponse>>
        get() = _chooseAddress
    val miniCartAdd: LiveData<Result<AddToCartDataModel>>
        get() = _miniCartAdd
    val miniCartUpdate: LiveData<Result<UpdateCartV2Data>>
        get() = _miniCartUpdate
    val miniCartRemove: LiveData<Result<Pair<String,String>>>
        get() = _miniCartRemove
    val homeAddToCartTracker: LiveData<HomeAddToCartTracker>
        get() = _homeAddToCartTracker
    val atcQuantity: LiveData<Result<HomeLayoutListUiModel>>
        get() = _productAddToCartQuantity

    private val _homeLayoutList = MutableLiveData<Result<HomeLayoutListUiModel>>()
    private val _keywordSearch = MutableLiveData<SearchPlaceholder>()
    private val _miniCart = MutableLiveData<Result<MiniCartSimplifiedData>>()
    private val _chooseAddress = MutableLiveData<Result<GetStateChosenAddressResponse>>()
    private val _miniCartAdd = MutableLiveData<Result<AddToCartDataModel>>()
    private val _miniCartUpdate = MutableLiveData<Result<UpdateCartV2Data>>()
    private val _miniCartRemove = MutableLiveData<Result<Pair<String,String>>>()
    private val _homeAddToCartTracker = MutableLiveData<HomeAddToCartTracker>()
    private val _productAddToCartQuantity = MutableLiveData<Result<HomeLayoutListUiModel>>()

    private val homeLayoutItemList = mutableListOf<HomeLayoutItemUiModel>()
    private var miniCartSimplifiedData: MiniCartSimplifiedData? = null
    private var hasTickerBeenRemoved = false
    private var channelToken = ""

    fun getLoadingState() {
        channelToken = ""
        homeLayoutItemList.clear()
        homeLayoutItemList.addLoadingIntoList()
        val data = HomeLayoutListUiModel(
                items = homeLayoutItemList,
                state = TokoNowLayoutState.LOADING
        )
        _homeLayoutList.postValue(Success(data))
    }

    fun getEmptyState(id: String) {
        homeLayoutItemList.clear()
        homeLayoutItemList.addEmptyStateIntoList(id)
        val data = HomeLayoutListUiModel(
                items = homeLayoutItemList,
                state = TokoNowLayoutState.HIDE
        )
        _homeLayoutList.postValue(Success(data))
    }

    fun getProductRecomOoc() {
        launchCatchError(block = {
            val recommendationWidgets = getRecommendationUseCase.getData(
                GetRecommendationRequestParam(
                    pageName = PAGE_NAME_RECOMMENDATION_OOC_PARAM,
                    xSource = X_SOURCE_RECOMMENDATION_PARAM,
                    xDevice = X_DEVICE_RECOMMENDATION_PARAM
                )
            )
            if (!recommendationWidgets.first().recommendationItemList.isNullOrEmpty()) {
                homeLayoutItemList.addProductRecomOoc(recommendationWidgets.first())
                val data = HomeLayoutListUiModel(
                    items = homeLayoutItemList,
                    state = TokoNowLayoutState.HIDE
                )
                _homeLayoutList.postValue(Success(data))
            }
        }) { /* nothing to do */ }
    }

    fun getHomeLayout(localCacheModel: LocalCacheModel?, hasSharingEducationBeenRemoved: Boolean) {
        launchCatchError(block = {
            homeLayoutItemList.clear()

            val warehouseId = localCacheModel?.warehouse_id.orEmpty()
            val homeLayoutResponse = getHomeLayoutDataUseCase.execute(
                localCacheModel = localCacheModel
            )
            channelToken = homeLayoutResponse.firstOrNull()?.token.orEmpty()

            homeLayoutItemList.mapHomeLayoutList(
                homeLayoutResponse,
                hasTickerBeenRemoved,
                hasSharingEducationBeenRemoved
            )

            getLayoutComponentData(warehouseId)

            val data = HomeLayoutListUiModel(
                items = homeLayoutItemList,
                state = TokoNowLayoutState.SHOW
            )
            _homeLayoutList.postValue(Success(data))
        }) {
            _homeLayoutList.postValue(Fail(it))
        }
    }

    /**
     * Handle on scroll event, load more if reached bottom of TokoMart Home.
     * All items loaded when token returned from dynamic channel is empty.
     *
     * @param lastVisibleItemIndex last item index visible on user screen
     * @param localCacheModel address data cache from choose address widget
     * @param hasSharingEducationBeenRemoved has sharing education widget dismissed by user
     */
    fun onScrollTokoMartHome(
        lastVisibleItemIndex: Int,
        localCacheModel: LocalCacheModel?,
        hasSharingEducationBeenRemoved: Boolean
    ) {
        if(shouldLoadMore(lastVisibleItemIndex)) {
            launchCatchError(block = {
                showProgressBar()

                Log.e("tokonowLog", "start load more")
                val warehouseId = localCacheModel?.warehouse_id.orEmpty()
                val homeLayoutResponse = getHomeLayoutDataUseCase.execute(
                    token = channelToken,
                    localCacheModel = LocalCacheModel()
                )
                channelToken = homeLayoutResponse.firstOrNull()?.token.orEmpty()

                homeLayoutItemList.addMoreHomeLayout(
                    homeLayoutResponse,
                    hasSharingEducationBeenRemoved
                )

                getLayoutComponentData(warehouseId)

                homeLayoutItemList.removeProgressBar()

                val data = HomeLayoutListUiModel(
                    items = homeLayoutItemList,
                    state = TokoNowLayoutState.LOAD_MORE
                )

                _homeLayoutList.postValue(Success(data))
            }) {
                _homeLayoutList.postValue(Fail(it))
            }
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
                    val isInitialLoad = _homeLayoutList.value == null

                    if(isInitialLoad) {
                        setMiniCartAndProductQuantity(it)
                    } else {
                        setProductAddToCartQuantity(it)
                    }

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
                    items = homeLayoutItemList,
                    state = TokoNowLayoutState.SHOW
            )
            _homeLayoutList.postValue(Success(data))
        }) {
            homeLayoutItemList.mapHomeCategoryGridData(item, null)
            val data = HomeLayoutListUiModel(
                    items = homeLayoutItemList,
                    state = TokoNowLayoutState.SHOW
            )
            _homeLayoutList.postValue(Success(data))
        }
    }

    fun addProductToCart(
        productId: String,
        quantity: Int,
        shopId: String,
        @TokoNowLayoutType type: String
    ) {
        val miniCartItem = getMiniCartItem(productId)

        when {
            miniCartItem == null -> addItemToCart(productId, shopId, quantity, type)
            quantity.isZero() -> removeItemCart(miniCartItem, type)
            else -> updateItemCart(miniCartItem, quantity, type)
        }
    }

    /**
     * Get product add to cart quantity based on mini cart data.
     * The quantity will be shown in product list quantity editor.
     *
     * @param shopId id obtained from choose address widget
     * @param warehouseId id obtained from choose address widget
     */
    fun getProductAddToCartQuantity(shopId: List<String>, warehouseId: String?) {
        if(!shopId.isNullOrEmpty() && warehouseId.toLongOrZero() != 0L && userSession.isLoggedIn) {
            launchCatchError(block = {
                getMiniCartUseCase.setParams(shopId)
                getMiniCartUseCase.execute({
                    setProductAddToCartQuantity(it)
                }, {
                    _productAddToCartQuantity.postValue(Fail(it))
                })
            }) {
                _productAddToCartQuantity.postValue(Fail(it))
            }
        }
    }

    fun getMiniCartItem(productId: String): MiniCartItem? {
        val items = miniCartSimplifiedData?.miniCartItems.orEmpty()
        return items.firstOrNull { it.productId == productId }
    }

    fun setProductAddToCartQuantity(miniCart: MiniCartSimplifiedData) {
        launchCatchError(block = {
            setMiniCartAndProductQuantity(miniCart)
            val data = HomeLayoutListUiModel(
                items = homeLayoutItemList,
                state = TokoNowLayoutState.SHOW
            )
            _productAddToCartQuantity.postValue(Success(data))
        }) {}
    }

    fun removeTickerWidget(id: String) {
        launchCatchError(block = {
            hasTickerBeenRemoved = true
            homeLayoutItemList.removeItem(id)

            val data = HomeLayoutListUiModel(
                items = homeLayoutItemList,
                state = TokoNowLayoutState.SHOW
            )

            _homeLayoutList.postValue(Success(data))
        }) {}
    }

    fun removeSharingEducationWidget(id: String) {
        launchCatchError(block = {
            homeLayoutItemList.removeItem(id)

            val data = HomeLayoutListUiModel(
                items = homeLayoutItemList,
                state = TokoNowLayoutState.UPDATE
            )

            _homeLayoutList.postValue(Success(data))
        }) {}
    }

    fun getRecentPurchaseProducts(): List<TokoNowProductCardUiModel> {
        val item = homeLayoutItemList.firstOrNull { it.layout is TokoNowRecentPurchaseUiModel }
        val recentPurchase = item?.layout as? TokoNowRecentPurchaseUiModel
        return recentPurchase?.productList.orEmpty()
    }

    /**
     * Get layout content data from external query.
     * Example: Category Grid get its data from TokonowCategoryTree.
     * @param warehouseId Id obtained from choose address widget
     */
    private suspend fun getLayoutComponentData(warehouseId: String) {
        homeLayoutItemList.filter { it.state == HomeLayoutItemState.NOT_LOADED }.forEach {
            homeLayoutItemList.setStateToLoading(it)

            when (val item = it.layout) {
                is HomeLayoutUiModel -> getTokoNowHomeComponent(item, warehouseId) // TokoNow Home Component
                is TokoNowLayoutUiModel -> getTokoNowGlobalComponent(item, warehouseId) // TokoNow Common Component
            }
        }
    }

    /**
     * Get data from additional query for TokopediaNOW Home Component.
     * Add use case and data mapping for TokopediaNOW Home Component here.
     * Example: Category Grid get its data from getCategoryListUseCase.
     *
     * @param item TokopediaNOW Home component item
     */
    private suspend fun getTokoNowHomeComponent(item: HomeLayoutUiModel, warehouseId: String) {
        when (item) {
            is HomeTickerUiModel -> getTickerDataAsync(item).await()
            is HomeEducationalInformationWidgetUiModel -> getEducationalInformationAsync(item).await()
            is HomeSharingEducationWidgetUiModel -> getSharingEducationAsync(item, warehouseId).await()
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
        when(item) {
            is TokoNowCategoryGridUiModel -> getCategoryGridDataAsync(item, warehouseId).await()
            is TokoNowRecentPurchaseUiModel -> getRecentPurchaseDataAsync(item, warehouseId).await()
        }
    }

    private suspend fun getRecentPurchaseDataAsync(
        item: TokoNowRecentPurchaseUiModel,
        warehouseId: String
    ): Deferred<Unit?> {
        return asyncCatchError(block = {
            val response = getRecentPurchaseUseCase.execute(warehouseId)
            if(response.products.isNotEmpty()) {
                homeLayoutItemList.mapProductPurchaseData(item, response, miniCartSimplifiedData)
            } else {
                homeLayoutItemList.removeItem(item.id)
            }
        }) {
            homeLayoutItemList.removeItem(item.id)
        }
    }

    private suspend fun getCategoryGridDataAsync(
        item: TokoNowCategoryGridUiModel,
        warehouseId: String
    ): Deferred<Unit?> {
        return asyncCatchError(block = {
            val response = getCategoryList(warehouseId)
            homeLayoutItemList.mapHomeCategoryGridData(item, response)
        }) {
            homeLayoutItemList.mapHomeCategoryGridData(item, emptyList())
        }
    }

    private suspend fun getTickerDataAsync(item: HomeTickerUiModel): Deferred<Unit?> {
        return asyncCatchError(block = {
            val tickerList = getTickerUseCase.execute().ticker.tickerList
            val tickerData = TickerMapper.mapTickerData(tickerList)
            homeLayoutItemList.mapTickerData(item, tickerData)
        }) {
            homeLayoutItemList.removeItem(item.id)
        }
    }

    private fun getEducationalInformationAsync(item: HomeEducationalInformationWidgetUiModel): Deferred<Unit?> {
        return asyncCatchError(block = {
            homeLayoutItemList.mapEducationalInformationData(item)
        }) {
            homeLayoutItemList.removeItem(item.id)
        }
    }

    private suspend fun getSharingEducationAsync(item: HomeSharingEducationWidgetUiModel, warehouseId: String): Deferred<Unit?> {
        return asyncCatchError(block = {
            val response = getRecentPurchaseUseCase.execute(warehouseId)
            if(response.products.isNotEmpty()) {
                homeLayoutItemList.mapSharingEducationData(item)
            } else {
                homeLayoutItemList.removeItem(item.id)
            }
        }) {
            homeLayoutItemList.removeItem(item.id)
        }
    }

    private suspend fun getCategoryList(warehouseId: String): List<CategoryResponse> {
        return getCategoryListUseCase.execute(warehouseId, CATEGORY_LEVEL_DEPTH).data
    }

    private fun addItemToCart(
        productId: String,
        shopId: String,
        quantity: Int,
        @TokoNowLayoutType type: String
    ) {
        val addToCartRequestParams = AddToCartUseCase.getMinimumParams(
            productId = productId,
            shopId = shopId,
            quantity = quantity
        )
        addToCartUseCase.setParams(addToCartRequestParams)
        addToCartUseCase.execute({
            trackProductAddToCart(productId, quantity, type, it.data.cartId)
            homeLayoutItemList.updateProductQuantity(
                productId,
                quantity,
                type
            )
            _miniCartAdd.postValue(Success(it))
        }, {
            _miniCartAdd.postValue(Fail(it))
        })
    }

    private fun updateItemCart(
        miniCartItem: MiniCartItem,
        quantity: Int,
        @TokoNowLayoutType type: String
    ) {
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
            trackProductUpdateCart(miniCartItem.productId, quantity, type, miniCartItem.cartId)
            _miniCartUpdate.value = Success(it)
        }, {
            _miniCartUpdate.postValue(Fail(it))
        })
    }

    private fun removeItemCart(miniCartItem: MiniCartItem, @TokoNowLayoutType type: String) {
        deleteCartUseCase.setParams(
            cartIdList = listOf(miniCartItem.cartId)
        )
        deleteCartUseCase.execute({
            val productId = miniCartItem.productId
            val data = Pair(productId, it.data.message.joinToString(separator = ", "))
            trackProductRemoveCart(productId, type, miniCartItem.cartId)
            homeLayoutItemList.setQuantityToZero(productId, type)
            _miniCartRemove.postValue(Success(data))
        }, {
            _miniCartRemove.postValue(Fail(it))
        })
    }

    private fun setMiniCartAndProductQuantity(miniCart: MiniCartSimplifiedData) {
        setMiniCartSimplifiedData(miniCart)
        updateProductQuantity(miniCart)
    }

    private fun updateProductQuantity(miniCart: MiniCartSimplifiedData) {
        homeLayoutItemList.updateRecentPurchaseQuantity(miniCart)
        homeLayoutItemList.updateProductRecomQuantity(miniCart)
    }

    private fun setMiniCartSimplifiedData(miniCart: MiniCartSimplifiedData) {
        miniCartSimplifiedData = miniCart
    }

    private fun trackProductAddToCart(
        productId: String,
        quantity: Int,
        @TokoNowLayoutType type: String,
        cartId: String
    ) {
        if(type == RECENT_PURCHASE) {
            trackRecentPurchaseAddToCart(productId, quantity, cartId)
        } else if (type ==  PRODUCT_RECOM) {
            trackRecentProductRecomAddToCart(productId, quantity, cartId)
        }
    }

    private fun trackProductUpdateCart(
        productId: String,
        quantity: Int,
        @TokoNowLayoutType type: String,
        cartId: String
    ) {
        if(type == PRODUCT_RECOM) {
            trackRecentProductRecomAddToCart(productId, quantity, cartId)
        }
    }

    private fun trackProductRemoveCart(
        productId: String,
        @TokoNowLayoutType type: String,
        cartId: String
    ) {
        if(type == PRODUCT_RECOM) {
            trackRecentProductRecomAddToCart(productId, DEFAULT_QUANTITY, cartId)
        }
    }

    private fun trackRecentPurchaseAddToCart(productId: String, quantity: Int, cartId: String) {
        val homeItem = homeLayoutItemList.firstOrNull { it.layout is TokoNowRecentPurchaseUiModel }
        val recentPurchase = homeItem?.layout as? TokoNowRecentPurchaseUiModel
        val productList = recentPurchase?.productList.orEmpty()
        val product = productList.firstOrNull { it.productId == productId }

        product?.let {
            val position = productList.indexOf(it)
            val data = HomeAddToCartTracker(position, quantity,cartId, it)
            _homeAddToCartTracker.postValue(data)
        }
    }

    private fun trackRecentProductRecomAddToCart(productId: String, quantity: Int, cartId: String) {
        val homeItem = homeLayoutItemList.firstOrNull { it.layout is HomeProductRecomUiModel }
        val productRecom = homeItem?.layout as? HomeProductRecomUiModel
        val recomWidget = productRecom?.recomWidget
        val recommendationItemList = recomWidget?.recommendationItemList.orEmpty()
        val product = recommendationItemList.firstOrNull {
            it.productId.toString() == productId
        }
        product?.quantity = quantity

        product?.let { item ->
            val position = recommendationItemList.indexOf(item)
            val data = HomeAddToCartTracker(position, quantity, cartId, productRecom)
            _homeAddToCartTracker.postValue(data)
        }
    }

    private fun shouldLoadMore(lastVisibleItemIndex: Int): Boolean {
        val allItemsLoaded = channelToken.isEmpty()
        val isLoading = homeLayoutItemList.map { it.layout }.contains(HomeProgressBarUiModel)
        val scrolledToBottom = lastVisibleItemIndex == homeLayoutItemList.count() - 1
        return scrolledToBottom && !isLoading && !allItemsLoaded
    }

    private fun showProgressBar() {
        homeLayoutItemList.addProgressBar()
        val data = HomeLayoutListUiModel(
            homeLayoutItemList,
            TokoNowLayoutState.UPDATE
        )
        _homeLayoutList.postValue(Success(data))
    }
}