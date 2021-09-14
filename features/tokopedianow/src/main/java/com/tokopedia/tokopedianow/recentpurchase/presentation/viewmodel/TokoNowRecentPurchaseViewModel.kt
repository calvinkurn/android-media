package com.tokopedia.tokopedianow.recentpurchase.presentation.viewmodel

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
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.categorylist.domain.usecase.GetCategoryListUseCase
import com.tokopedia.tokopedianow.common.constant.ConstantValue
import com.tokopedia.tokopedianow.common.constant.ConstantValue.PAGE_NAME_RECOMMENDATION_NO_RESULT_PARAM
import com.tokopedia.tokopedianow.common.constant.ConstantValue.PAGE_NAME_RECOMMENDATION_OOC_PARAM
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.domain.model.RepurchaseProduct
import com.tokopedia.tokopedianow.recentpurchase.constant.RepurchaseStaticLayoutId.Companion.EMPTY_STATE_NO_HISTORY_FILTER
import com.tokopedia.tokopedianow.recentpurchase.constant.RepurchaseStaticLayoutId.Companion.EMPTY_STATE_NO_HISTORY_SEARCH
import com.tokopedia.tokopedianow.recentpurchase.constant.RepurchaseStaticLayoutId.Companion.EMPTY_STATE_NO_RESULT
import com.tokopedia.tokopedianow.recentpurchase.constant.RepurchaseStaticLayoutId.Companion.EMPTY_STATE_OOC
import com.tokopedia.tokopedianow.recentpurchase.domain.mapper.RepurchaseLayoutMapper.addCategoryGrid
import com.tokopedia.tokopedianow.recentpurchase.domain.mapper.RepurchaseLayoutMapper.addChooseAddress
import com.tokopedia.tokopedianow.recentpurchase.domain.mapper.RepurchaseLayoutMapper.addEmptyStateNoHistory
import com.tokopedia.tokopedianow.recentpurchase.domain.mapper.RepurchaseLayoutMapper.addEmptyStateNoResult
import com.tokopedia.tokopedianow.recentpurchase.domain.mapper.RepurchaseLayoutMapper.addEmptyStateOoc
import com.tokopedia.tokopedianow.recentpurchase.domain.mapper.RepurchaseLayoutMapper.addLayoutList
import com.tokopedia.tokopedianow.recentpurchase.domain.mapper.RepurchaseLayoutMapper.addLoading
import com.tokopedia.tokopedianow.recentpurchase.domain.mapper.RepurchaseLayoutMapper.addProduct
import com.tokopedia.tokopedianow.recentpurchase.domain.mapper.RepurchaseLayoutMapper.addProductRecom
import com.tokopedia.tokopedianow.recentpurchase.domain.mapper.RepurchaseLayoutMapper.addSortFilter
import com.tokopedia.tokopedianow.recentpurchase.domain.mapper.RepurchaseLayoutMapper.removeAllProduct
import com.tokopedia.tokopedianow.recentpurchase.domain.mapper.RepurchaseLayoutMapper.removeChooseAddress
import com.tokopedia.tokopedianow.recentpurchase.domain.mapper.RepurchaseLayoutMapper.removeEmptyStateNoHistory
import com.tokopedia.tokopedianow.recentpurchase.domain.mapper.RepurchaseLayoutMapper.removeLoading
import com.tokopedia.tokopedianow.recentpurchase.domain.mapper.RepurchaseLayoutMapper.setCategoryFilter
import com.tokopedia.tokopedianow.recentpurchase.domain.param.GetRepurchaseProductListParam
import com.tokopedia.tokopedianow.recentpurchase.domain.param.GetRepurchaseProductListParam.Companion.SORT_FREQUENTLY_BOUGHT
import com.tokopedia.tokopedianow.recentpurchase.domain.usecase.GetRepurchaseProductListUseCase
import com.tokopedia.tokopedianow.recentpurchase.presentation.fragment.TokoNowRecentPurchaseFragment.Companion.CATEGORY_LEVEL_DEPTH
import com.tokopedia.tokopedianow.recentpurchase.presentation.model.RepurchaseProductListMeta
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseLayoutUiModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseSortFilterUiModel.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Deferred
import javax.inject.Inject

class TokoNowRecentPurchaseViewModel @Inject constructor(
    private val getRepurchaseProductListUseCase: GetRepurchaseProductListUseCase,
    private val getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val getRecommendationUseCase: GetRecommendationUseCase,
    private val getChooseAddressWarehouseLocUseCase: GetChosenAddressWarehouseLocUseCase,
    private val userSession: UserSessionInterface,
    dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.io) {

    companion object {
        private const val INITIAL_PAGE = 1
    }

    val getLayout: LiveData<Result<RepurchaseLayoutUiModel>>
        get() = _getLayout
    val loadMore: LiveData<Result<RepurchaseLayoutUiModel>>
        get() = _loadMore
    val miniCart: LiveData<Result<MiniCartSimplifiedData>>
        get() = _miniCart
    val miniCartAdd: LiveData<Result<AddToCartDataModel>>
        get() = _miniCartAdd
    val miniCartUpdate: LiveData<Result<UpdateCartV2Data>>
        get() = _miniCartUpdate
    val miniCartRemove: LiveData<Result<Pair<String, String>>>
        get() = _miniCartRemove
    val productAddToCartQuantity: LiveData<Result<MiniCartSimplifiedData>>
        get() = _productAddToCartQuantity
    val chooseAddress: LiveData<Result<GetStateChosenAddressResponse>>
        get() = _chooseAddress

    private val _getLayout = MutableLiveData<Result<RepurchaseLayoutUiModel>>()
    private val _loadMore = MutableLiveData<Result<RepurchaseLayoutUiModel>>()
    private val _miniCart = MutableLiveData<Result<MiniCartSimplifiedData>>()
    private val _miniCartAdd = MutableLiveData<Result<AddToCartDataModel>>()
    private val _miniCartUpdate = MutableLiveData<Result<UpdateCartV2Data>>()
    private val _miniCartRemove = MutableLiveData<Result<Pair<String,String>>>()
    private val _productAddToCartQuantity = MutableLiveData<Result<MiniCartSimplifiedData>>()
    private val _chooseAddress = MutableLiveData<Result<GetStateChosenAddressResponse>>()

    private var localCacheModel: LocalCacheModel? = null
    private var productListMeta: RepurchaseProductListMeta? = null
    private var selectedCategoryFilter: SelectedSortFilter? = null
    private var miniCartSimplifiedData: MiniCartSimplifiedData? = null
    private var layoutList: MutableList<Visitable<*>> = mutableListOf()

    fun showLoading() {
        layoutList.clear()
        layoutList.addLoading()

        val layout = RepurchaseLayoutUiModel(
            layoutList = layoutList,
            nextPage = INITIAL_PAGE,
            state = TokoNowLayoutState.LOADING
        )

        _getLayout.postValue(Success(layout))
    }

    fun getLayoutList() {
        layoutList.removeLoading()
        layoutList.addLayoutList()

        val layout = RepurchaseLayoutUiModel(
            layoutList = layoutList,
            nextPage = INITIAL_PAGE,
            state = TokoNowLayoutState.SHOW
        )

        _getLayout.postValue(Success(layout))
    }

    fun getLayoutData() {
        launchCatchError(block = {
            getProductListAsync().await()

            val layout = RepurchaseLayoutUiModel(
                layoutList = layoutList,
                nextPage = INITIAL_PAGE,
                state = TokoNowLayoutState.LOADED
            )

            _getLayout.postValue(Success(layout))
        }) {

        }
    }

    fun getMiniCart(shopId: List<String>, warehouseId: String?) {
        if(!shopId.isNullOrEmpty() && warehouseId.toLongOrZero() != 0L && userSession.isLoggedIn) {
            launchCatchError(block = {
                getMiniCartUseCase.setParams(shopId)
                getMiniCartUseCase.execute({
                    val isInitialLoad = true

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

    fun getEmptyState(id: String) {
        launchCatchError(block = {
            layoutList.removeLoading()
            when(id) {
                EMPTY_STATE_NO_HISTORY_SEARCH -> {
                    val description = R.string
                        .tokopedianow_repurchase_empty_state_no_history_desc_search
                    layoutList.addEmptyStateNoHistory(description)
                    layoutList.removeAllProduct()
                }
                EMPTY_STATE_NO_HISTORY_FILTER -> {
                    val description = R.string
                        .tokopedianow_repurchase_empty_state_no_history_desc_filter
                    layoutList.addEmptyStateNoHistory(description)
                    layoutList.removeAllProduct()
                }
                EMPTY_STATE_OOC -> {
                    layoutList.clear()
                    layoutList.addChooseAddress()
                    layoutList.addEmptyStateOoc()
                    getProductRecomAsync(PAGE_NAME_RECOMMENDATION_OOC_PARAM).await()
                }
                else -> {
                    layoutList.clear()
                    layoutList.addChooseAddress()
                    layoutList.addEmptyStateNoResult()
                    getCategoryGridAsync().await()
                    getProductRecomAsync(PAGE_NAME_RECOMMENDATION_NO_RESULT_PARAM).await()
                }
            }
            val layout = RepurchaseLayoutUiModel(
                layoutList = layoutList,
                nextPage = INITIAL_PAGE,
                state = TokoNowLayoutState.LOADED
            )

            _getLayout.postValue(Success(layout))
        }) { /* nothing to do */ }
    }

    fun removeChooseAddressWidget() {
        layoutList.removeChooseAddress()

        val layout = RepurchaseLayoutUiModel(
            layoutList = layoutList,
            nextPage = INITIAL_PAGE,
            state = TokoNowLayoutState.SHOW
        )

        _getLayout.postValue(Success(layout))
    }

    fun setProductAddToCartQuantity(miniCart: MiniCartSimplifiedData) {
        launchCatchError(block = {
            setMiniCartAndProductQuantity(miniCart)
            _productAddToCartQuantity.postValue(Success(miniCart))
        }) {}
    }

    fun onCartItemUpdated(productId: String, quantity: Int, shopId: String) {
        val miniCartItem = getMiniCartItem(productId)

        when {
            miniCartItem == null -> addItemToCart(productId, shopId, quantity)
            quantity.isZero() -> removeItemFromCart(miniCartItem)
            else -> updateItemCart(miniCartItem, quantity)
        }
    }

    fun applyCategoryFilter(selectedFilter: SelectedSortFilter?) {
        launchCatchError(block = {
            setCategoryFilter(selectedFilter)
            val productList = getProductList()
            layoutList.removeLoading()

            if(productList.isEmpty()) {
                getEmptyState(EMPTY_STATE_NO_HISTORY_FILTER)
            } else {
                layoutList.addProduct(productList)
            }

            val layout = RepurchaseLayoutUiModel(
                layoutList = layoutList,
                nextPage = INITIAL_PAGE,
                state = TokoNowLayoutState.LOADED
            )

            _getLayout.postValue(Success(layout))
        }) {

        }
    }

    fun setLocalCacheModel(localCacheModel: LocalCacheModel?) {
        this.localCacheModel = localCacheModel
    }

    fun getSelectedCategoryFilter() = selectedCategoryFilter

    fun clearSelectedFilters() {
        selectedCategoryFilter = null
    }

    private fun getMiniCartItem(productId: String): MiniCartItem? {
        val items = miniCartSimplifiedData?.miniCartItems.orEmpty()
        return items.firstOrNull { it.productId == productId }
    }

    private fun setCategoryFilter(selectedFilter: SelectedSortFilter?) {
        layoutList.setCategoryFilter(selectedFilter)
        layoutList.removeEmptyStateNoHistory()
        layoutList.removeAllProduct()
        layoutList.addLoading()

        val layout = RepurchaseLayoutUiModel(
            layoutList = layoutList,
            nextPage = INITIAL_PAGE,
            state = TokoNowLayoutState.LOADED
        )

        selectedCategoryFilter = selectedFilter
        _getLayout.postValue(Success(layout))
    }

    private suspend fun getProductList(): List<RepurchaseProduct> {
        val requestParam = createProductListRequestParam()
        val response = getRepurchaseProductListUseCase.execute(requestParam)

        val productList = response.products
        val productMeta = response.meta

        productListMeta = RepurchaseProductListMeta(
            productMeta.page,
            productMeta.hasNext,
            productMeta.totalScan
        )
        return productList
    }

    private fun getProductListAsync(): Deferred<Unit?> {
        return asyncCatchError(block = {
            val productList = getProductList()

            if (productList.isNullOrEmpty()) {
                getEmptyState(id = EMPTY_STATE_NO_RESULT)
            } else {
                layoutList.addSortFilter()
                layoutList.addProduct(productList)
            }
        }) {

        }
    }

    private fun getProductRecomAsync(pageName: String): Deferred<Unit?> {
        return asyncCatchError(block = {
            val recommendationWidgets = getRecommendationUseCase.getData(
                GetRecommendationRequestParam(
                    pageName = pageName,
                    xSource = ConstantValue.X_SOURCE_RECOMMENDATION_PARAM,
                    xDevice = ConstantValue.X_DEVICE_RECOMMENDATION_PARAM
                )
            )

            if (!recommendationWidgets.first().recommendationItemList.isNullOrEmpty()) {
                layoutList.addProductRecom(pageName, recommendationWidgets.first())

                val layout = RepurchaseLayoutUiModel(
                    layoutList = layoutList,
                    nextPage = INITIAL_PAGE,
                    state = TokoNowLayoutState.LOADED
                )

                _getLayout.postValue(Success(layout))
            }
        }) { /* nothing to do */ }
    }

    private fun getCategoryGridAsync(): Deferred<Unit?> {
        return asyncCatchError(block = {
            val warehouseId = localCacheModel?.warehouse_id.orEmpty()
            val response = getCategoryListUseCase.execute(warehouseId, CATEGORY_LEVEL_DEPTH).data
            layoutList.addCategoryGrid(response)

            val layout = RepurchaseLayoutUiModel(
                layoutList = layoutList,
                nextPage = INITIAL_PAGE,
                state = TokoNowLayoutState.LOADED
            )

            _getLayout.postValue(Success(layout))
        }) {
            /* nothing to do */
        }
    }

    fun onScrollProductList(index: IntArray?, itemCount: Int) {
        val lastItemIndex = itemCount - 1
        val containsLastItemIndex = index?.contains(lastItemIndex)
        val scrolledToLastItem = containsLastItemIndex == true
        val hasNextPage = productListMeta?.hasNext == true

        if(scrolledToLastItem && hasNextPage) {
            loadMoreProduct()
        }
    }

    private fun loadMoreProduct() {
        launchCatchError(block = {
            val page = productListMeta?.page.orZero() + 1
            val requestParam = createProductListRequestParam(page)
            val response = getRepurchaseProductListUseCase.execute(requestParam)

            val productList = response.products
            val productMeta = response.meta

            productListMeta = RepurchaseProductListMeta(
                productMeta.page,
                productMeta.hasNext,
                productMeta.totalScan
            )

            layoutList.addProduct(productList)

            val layout = RepurchaseLayoutUiModel(
                layoutList = layoutList,
                nextPage = page,
                state = TokoNowLayoutState.LOADED
            )

            _getLayout.postValue(Success(layout))
        }) {

        }
    }

    private fun addItemToCart(productId: String, shopId: String, quantity: Int) {
        val addToCartRequestParams = AddToCartUseCase.getMinimumParams(
            productId = productId,
            shopId = shopId,
            quantity = quantity
        )
        addToCartUseCase.setParams(addToCartRequestParams)
        addToCartUseCase.execute({
            _miniCartAdd.postValue(Success(it))
        }, {
            _miniCartAdd.postValue(Fail(it))
        })
    }

    private fun removeItemFromCart(miniCartItem: MiniCartItem) {
        deleteCartUseCase.setParams(
            cartIdList = listOf(miniCartItem.cartId)
        )
        deleteCartUseCase.execute({
            val productId = miniCartItem.productId
            val data = Pair(productId, it.data.message.joinToString(separator = ", "))
            _miniCartRemove.postValue(Success(data))
        }, {
            _miniCartRemove.postValue(Fail(it))
        })
    }

    private fun updateItemCart(miniCartItem: MiniCartItem, quantity: Int) {
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
            _miniCartUpdate.value = Success(it)
        }, {
            _miniCartUpdate.postValue(Fail(it))
        })
    }

    private fun createProductListRequestParam(page: Int = INITIAL_PAGE): GetRepurchaseProductListParam {
        val warehouseID = localCacheModel?.warehouse_id.orEmpty()
        val totalScan = productListMeta?.totalScan.orZero()
        val categoryIds = selectedCategoryFilter?.id

        return GetRepurchaseProductListParam(
            warehouseID = warehouseID,
            sort = SORT_FREQUENTLY_BOUGHT,
            totalScan = totalScan,
            page = page,
            catIds = categoryIds
        )
    }

    private fun setMiniCartAndProductQuantity(miniCart: MiniCartSimplifiedData) {
        setMiniCartSimplifiedData(miniCart)
        updateProductQuantity(miniCart)
    }

    private fun setMiniCartSimplifiedData(miniCart: MiniCartSimplifiedData) {
        miniCartSimplifiedData = miniCart
    }

    private fun updateProductQuantity(miniCart: MiniCartSimplifiedData) {
        //TO-DO: Update product quantity here
    }
}