package com.tokopedia.tokopedianow.repurchase.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.base.viewmodel.BaseTokoNowViewModel
import com.tokopedia.tokopedianow.common.constant.ConstantKey.DEFAULT_QUANTITY
import com.tokopedia.tokopedianow.common.constant.ConstantValue.PAGE_NAME_RECOMMENDATION_NO_RESULT_PARAM
import com.tokopedia.tokopedianow.common.constant.ConstantValue.PAGE_NAME_RECOMMENDATION_OOC_PARAM
import com.tokopedia.tokopedianow.common.constant.ServiceType
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.domain.mapper.AddressMapper
import com.tokopedia.tokopedianow.common.domain.model.RepurchaseProduct
import com.tokopedia.tokopedianow.common.domain.model.SetUserPreference.SetUserPreferenceData
import com.tokopedia.tokopedianow.common.domain.usecase.GetCategoryListUseCase
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.domain.usecase.SetUserPreferenceUseCase
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAddToCartTracker
import com.tokopedia.tokopedianow.repurchase.constant.RepurchaseStaticLayoutId
import com.tokopedia.tokopedianow.repurchase.constant.RepurchaseStaticLayoutId.Companion.EMPTY_STATE_NO_HISTORY_FILTER
import com.tokopedia.tokopedianow.repurchase.constant.RepurchaseStaticLayoutId.Companion.EMPTY_STATE_NO_HISTORY_SEARCH
import com.tokopedia.tokopedianow.repurchase.constant.RepurchaseStaticLayoutId.Companion.EMPTY_STATE_NO_RESULT
import com.tokopedia.tokopedianow.repurchase.constant.RepurchaseStaticLayoutId.Companion.EMPTY_STATE_OOC
import com.tokopedia.tokopedianow.repurchase.constant.RepurchaseStaticLayoutId.Companion.ERROR_STATE_FAILED_TO_FETCH_DATA
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.PRODUCT_REPURCHASE
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.addCategoryMenu
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.addChooseAddress
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.addEmptyStateNoHistory
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.addEmptyStateNoResult
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.addEmptyStateOoc
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.addLayoutList
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.addLoading
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.addProduct
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.addProductRecommendation
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.addProductRecommendationOoc
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.addServerErrorState
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.addSortFilter
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.mapCategoryMenuData
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.removeAllProduct
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.removeChooseAddress
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.removeEmptyStateNoHistory
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.removeLoading
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.removeProductRecommendation
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.setCategoryFilter
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.setDateFilter
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.setSortFilter
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.updateDeletedATCQuantity
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.updateProductATCQuantity
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.updateProductQuantity
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.updateProductWishlist
import com.tokopedia.tokopedianow.repurchase.domain.param.GetRepurchaseProductListParam
import com.tokopedia.tokopedianow.repurchase.domain.usecase.GetRepurchaseProductListUseCase
import com.tokopedia.tokopedianow.repurchase.presentation.fragment.TokoNowRepurchaseFragment.Companion.CATEGORY_LEVEL_DEPTH
import com.tokopedia.tokopedianow.repurchase.presentation.model.RepurchaseProductListMeta
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseLayoutUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseProductUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseSortFilterUiModel.SelectedDateFilter
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseSortFilterUiModel.SelectedSortFilter
import com.tokopedia.tokopedianow.sortfilter.presentation.bottomsheet.TokoNowSortFilterBottomSheet.Companion.FREQUENTLY_BOUGHT
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class TokoNowRepurchaseViewModel @Inject constructor(
    private val getRepurchaseProductListUseCase: GetRepurchaseProductListUseCase,
    private val getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val getChooseAddressWarehouseLocUseCase: GetChosenAddressWarehouseLocUseCase,
    private val setUserPreferenceUseCase: SetUserPreferenceUseCase,
    private val userSession: UserSessionInterface,
    addToCartUseCase: AddToCartUseCase,
    updateCartUseCase: UpdateCartUseCase,
    deleteCartUseCase: DeleteCartUseCase,
    affiliateService: NowAffiliateService,
    getTargetedTickerUseCase: GetTargetedTickerUseCase,
    addressData: TokoNowLocalAddress,
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

    companion object {
        private const val INITIAL_PAGE = 1
    }

    val serviceType: String
        get() = localCacheModel?.service_type.orEmpty()

    val getLayout: LiveData<Result<RepurchaseLayoutUiModel>>
        get() = _getLayout
    val loadMore: LiveData<Result<RepurchaseLayoutUiModel>>
        get() = _loadMore
    val atcQuantity: LiveData<Result<RepurchaseLayoutUiModel>>
        get() = _atcQuantity
    val chooseAddress: LiveData<Result<GetStateChosenAddressResponse>>
        get() = _chooseAddress
    val repurchaseAddToCartTracker: LiveData<RepurchaseAddToCartTracker>
        get() = _repurchaseAddToCartTracker
    val openScreenTracker: LiveData<String>
        get() = _openScreenTracker
    val setUserPreference: LiveData<Result<SetUserPreferenceData>>
        get() = _setUserPreference
    val updateToolbarNotification: LiveData<Boolean>
        get() = _updateToolbarNotification

    private val _getLayout = MutableLiveData<Result<RepurchaseLayoutUiModel>>()
    private val _loadMore = MutableLiveData<Result<RepurchaseLayoutUiModel>>()
    private val _atcQuantity = MutableLiveData<Result<RepurchaseLayoutUiModel>>()
    private val _chooseAddress = MutableLiveData<Result<GetStateChosenAddressResponse>>()
    private val _repurchaseAddToCartTracker = MutableLiveData<RepurchaseAddToCartTracker>()
    private val _openScreenTracker = MutableLiveData<String>()
    private val _setUserPreference = MutableLiveData<Result<SetUserPreferenceData>>()
    private val _updateToolbarNotification = MutableLiveData<Boolean>()

    private var localCacheModel: LocalCacheModel? = null
    private var productListMeta: RepurchaseProductListMeta? = null
    private var selectedCategoryFilter: SelectedSortFilter? = null
    private var selectedDateFilter: SelectedDateFilter = SelectedDateFilter()
    private var selectedSortFilter: Int = FREQUENTLY_BOUGHT
    private var layoutList: MutableList<Visitable<*>> = mutableListOf()

    private var getMiniCartJob: Job? = null

    fun trackOpeningScreen(screenName: String) {
        _openScreenTracker.value = screenName
    }

    fun showLoading() {
        layoutList.clear()
        layoutList.addLoading()

        val layout = RepurchaseLayoutUiModel(
            layoutList = layoutList,
            state = TokoNowLayoutState.LOADING
        )

        _getLayout.postValue(Success(layout))
    }

    fun getLayoutList() {
        layoutList.addLayoutList()

        val layout = RepurchaseLayoutUiModel(
            layoutList = layoutList,
            state = TokoNowLayoutState.SHOW
        )

        _getLayout.postValue(Success(layout))
    }

    fun getLayoutData() {
        launchCatchError(block = {
            getProductListAsync().await()

            val layout = RepurchaseLayoutUiModel(
                layoutList = layoutList,
                state = TokoNowLayoutState.LOADED
            )

            _getLayout.postValue(Success(layout))
        }) {
        }
    }

    fun getMiniCart(shopId: List<String>, warehouseId: String?) {
        if (shopId.isNotEmpty() && warehouseId.toLongOrZero() != 0L && userSession.isLoggedIn) {
            getMiniCartJob?.cancel()
            launchCatchError(block = {
                getMiniCartUseCase.setParams(shopId, MiniCartSource.TokonowRepurchasePage)
                val data = getMiniCartUseCase.executeOnBackground()
                val isInitialLoad = _getLayout.value == null

                if (isInitialLoad) {
                    setMiniCartAndProductQuantity(data)
                } else {
                    setProductAddToCartQuantity(data)
                }

                _miniCart.postValue(Success(data))
            }) {
                _miniCart.postValue(Fail(it))
            }.let {
                getMiniCartJob = it
            }
        }
    }

    fun getChooseAddress(source: String) {
        getChooseAddressWarehouseLocUseCase.getStateChosenAddress({
            _chooseAddress.postValue(Success(it))
        }, {
            _chooseAddress.postValue(Fail(it))
        }, source)
    }

    fun showEmptyState(@RepurchaseStaticLayoutId id: String) {
        launchCatchError(block = {
            layoutList.removeLoading()
            addEmptyState(id)

            val layout = RepurchaseLayoutUiModel(
                layoutList = layoutList,
                state = TokoNowLayoutState.EMPTY
            )

            _getLayout.postValue(Success(layout))
        }) { /* nothing to do */ }
    }

    fun removeChooseAddressWidget() {
        layoutList.removeChooseAddress()

        val layout = RepurchaseLayoutUiModel(
            layoutList = layoutList,
            state = TokoNowLayoutState.UPDATE
        )

        _getLayout.postValue(Success(layout))
    }

    fun removeProductRecommendationWidget() {
        layoutList.removeProductRecommendation()

        val layout = RepurchaseLayoutUiModel(
            layoutList = layoutList,
            state = TokoNowLayoutState.UPDATE
        )

        _getLayout.postValue(Success(layout))
    }

    fun setProductAddToCartQuantity(miniCart: MiniCartSimplifiedData) {
        if (_getLayout.value == null) return

        launchCatchError(block = {
            setMiniCartAndProductQuantity(miniCart)

            val layout = RepurchaseLayoutUiModel(
                layoutList = layoutList,
                state = TokoNowLayoutState.UPDATE
            )

            _atcQuantity.postValue(Success(layout))
        }) {}
    }

    fun onCartQuantityChanged(
        productId: String,
        quantity: Int,
        type: String,
        shopId: String,
        stock: Int,
        isVariant: Boolean
    ) {
        onCartQuantityChanged(
            productId = productId,
            shopId = shopId,
            quantity = quantity,
            stock = stock,
            isVariant = isVariant,
            onSuccessAddToCart = {
                trackProductAddToCart(productId, quantity, type, it.data.cartId)
                updateAddToCartQuantity(productId, quantity)
                updateToolbarNotification()
            },
            onSuccessUpdateCart = { _, _ ->
                updateAddToCartQuantity(productId, quantity)
                updateToolbarNotification()
            },
            onSuccessDeleteCart = { _, _ ->
                updateAddToCartQuantity(productId, DEFAULT_QUANTITY)
                updateToolbarNotification()
            }
        )
    }

    fun updateToolbarNotification() {
        _updateToolbarNotification.postValue(true)
    }

    fun applyCategoryFilter(selectedFilter: SelectedSortFilter?) {
        launchCatchError(block = {
            setCategoryFilter(selectedFilter)
            val productList = getProductList()
            layoutList.removeLoading()

            if (productList.isEmpty()) {
                showEmptyState(EMPTY_STATE_NO_HISTORY_FILTER)
            } else {
                showProductList(productList)
            }
        }) {
        }
    }

    fun applySortFilter(sort: Int) {
        launchCatchError(block = {
            setSortFilter(sort)
            val productList = getProductList()
            layoutList.removeLoading()

            if (productList.isEmpty()) {
                showEmptyState(EMPTY_STATE_NO_HISTORY_FILTER)
            } else {
                showProductList(productList)
            }
        }) {
        }
    }

    fun applyDateFilter(selectedFilter: SelectedDateFilter) {
        launchCatchError(block = {
            setDateFilter(selectedFilter)
            val productList = getProductList()
            layoutList.removeLoading()

            if (productList.isEmpty()) {
                showEmptyState(EMPTY_STATE_NO_HISTORY_FILTER)
            } else {
                showProductList(productList)
            }
        }) {
        }
    }

    fun getAddToCartQuantity() {
        val shopId = localCacheModel?.shop_id.orEmpty()
        val warehouseId = localCacheModel?.warehouse_id.orEmpty()
        val isLoggedIn = userSession.isLoggedIn

        if (shopId.isNotEmpty() && warehouseId.toLongOrZero() != 0L && isLoggedIn) {
            launchCatchError(block = {
                getMiniCartUseCase.setParams(listOf(shopId), MiniCartSource.TokonowRepurchasePage)
                val data = getMiniCartUseCase.executeOnBackground()
                setProductAddToCartQuantity(data)
            }) {
                _atcQuantity.postValue(Fail(it))
            }
        }
    }

    fun switchService() {
        launchCatchError(block = {
            localCacheModel?.let {
                val currentServiceType = it.service_type

                val serviceType = if (
                    currentServiceType == ServiceType.NOW_15M ||
                    currentServiceType == ServiceType.NOW_OOC
                ) {
                    ServiceType.NOW_2H
                } else {
                    ServiceType.NOW_15M
                }

                val setUserPreference = setUserPreferenceUseCase.execute(it, serviceType)
                _setUserPreference.postValue(Success(setUserPreference))
            }
        }) {
            _setUserPreference.postValue(Fail(it))
        }
    }

    fun setLocalCacheModel(localCacheModel: LocalCacheModel?) {
        localCacheModel?.let { setAddressData(localCacheModel) }
        this.localCacheModel = localCacheModel
    }

    fun getSelectedCategoryFilter() = selectedCategoryFilter

    fun getSelectedSortFilter() = selectedSortFilter

    fun getSelectedDateFilter() = selectedDateFilter

    fun clearSelectedFilters() {
        selectedSortFilter = FREQUENTLY_BOUGHT
        selectedDateFilter = SelectedDateFilter()
        selectedCategoryFilter = null
    }

    private fun setCategoryFilter(selectedFilter: SelectedSortFilter?) {
        layoutList.setCategoryFilter(selectedFilter)
        layoutList.removeEmptyStateNoHistory()
        layoutList.removeAllProduct()
        layoutList.addLoading()

        val layout = RepurchaseLayoutUiModel(
            layoutList = layoutList,
            state = TokoNowLayoutState.UPDATE
        )

        selectedCategoryFilter = selectedFilter
        _getLayout.postValue(Success(layout))
    }

    private fun setSortFilter(sort: Int) {
        layoutList.setSortFilter(sort)
        layoutList.removeEmptyStateNoHistory()
        layoutList.removeAllProduct()
        layoutList.addLoading()

        val layout = RepurchaseLayoutUiModel(
            layoutList = layoutList,
            state = TokoNowLayoutState.UPDATE
        )

        selectedSortFilter = sort
        _getLayout.postValue(Success(layout))
    }

    private fun setDateFilter(selectedFilter: SelectedDateFilter) {
        layoutList.setDateFilter(selectedFilter)
        layoutList.removeEmptyStateNoHistory()
        layoutList.removeAllProduct()
        layoutList.addLoading()

        val layout = RepurchaseLayoutUiModel(
            layoutList = layoutList,
            state = TokoNowLayoutState.UPDATE
        )

        selectedDateFilter = selectedFilter
        _getLayout.postValue(Success(layout))
    }

    private fun showProductList(productList: List<RepurchaseProduct>) {
        layoutList.addProduct(productList)

        val layout = RepurchaseLayoutUiModel(
            layoutList = layoutList,
            state = TokoNowLayoutState.LOADED
        )

        _getLayout.postValue(Success(layout))
    }

    private suspend fun getProductList(page: Int = INITIAL_PAGE): List<RepurchaseProduct> {
        val requestParam = createProductListRequestParam(page)
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
            layoutList.removeLoading()

            if (productList.isEmpty()) {
                addEmptyState(id = EMPTY_STATE_NO_RESULT)
            } else {
                layoutList.addSortFilter()
                layoutList.addProduct(productList)
            }
        }) {
            addEmptyState(ERROR_STATE_FAILED_TO_FETCH_DATA)
        }
    }

    private fun getCategoryMenuAsync(): Deferred<Unit?> {
        return asyncCatchError(block = {
            layoutList.addCategoryMenu(
                state = TokoNowLayoutState.LOADING
            )

            val warehouses = AddressMapper.mapToWarehousesData(localCacheModel)
            val response = getCategoryListUseCase.execute(warehouses, CATEGORY_LEVEL_DEPTH).data
            layoutList.mapCategoryMenuData(response, getWarehouseId())

            val layout = RepurchaseLayoutUiModel(
                layoutList = layoutList,
                state = TokoNowLayoutState.UPDATE
            )

            _getLayout.postValue(Success(layout))
        }) {
            layoutList.mapCategoryMenuData(listOf())

            val layout = RepurchaseLayoutUiModel(
                layoutList = layoutList,
                state = TokoNowLayoutState.UPDATE
            )

            _getLayout.postValue(Success(layout))
        }
    }

    fun getCategoryMenu() {
        launchCatchError(block = {
            val warehouses = AddressMapper.mapToWarehousesData(localCacheModel)
            val response = getCategoryListUseCase.execute(warehouses, CATEGORY_LEVEL_DEPTH).data
            layoutList.mapCategoryMenuData(response, getWarehouseId())

            val layout = RepurchaseLayoutUiModel(
                layoutList = layoutList,
                state = TokoNowLayoutState.UPDATE
            )

            _getLayout.postValue(Success(layout))
        }) {
            layoutList.mapCategoryMenuData(listOf())

            val layout = RepurchaseLayoutUiModel(
                layoutList = layoutList,
                state = TokoNowLayoutState.UPDATE
            )

            _getLayout.postValue(Success(layout))
        }
    }

    fun onScrollProductList(index: Int?, itemCount: Int) {
        val lastItemIndex = itemCount - 1
        val isLastItemIndex = index == lastItemIndex
        val hasNextPage = productListMeta?.hasNext == true

        if (isLastItemIndex && hasNextPage) {
            loadMoreProduct()
        }
    }

    fun updateWishlistStatus(
        productId: String,
        hasBeenWishlist: Boolean
    ) {
        launch {
            layoutList.updateProductWishlist(
                productId = productId,
                hasBeenWishlist = hasBeenWishlist
            )

            val layout = RepurchaseLayoutUiModel(
                layoutList = layoutList,
                state = TokoNowLayoutState.UPDATE
            )

            _getLayout.postValue(Success(layout))
        }
    }

    private fun loadMoreProduct() {
        launchCatchError(block = {
            val page = productListMeta?.page.orZero() + 1
            val productList = getProductList(page)

            layoutList.addProduct(productList)

            miniCartData?.let {
                setMiniCartAndProductQuantity(it)
            }

            val layout = RepurchaseLayoutUiModel(
                layoutList = layoutList,
                state = TokoNowLayoutState.LOAD_MORE
            )

            _loadMore.postValue(Success(layout))
        }) {
        }
    }

    private fun trackProductAddToCart(
        productId: String,
        quantity: Int,
        type: String,
        cartId: String
    ) {
        if (type == PRODUCT_REPURCHASE) {
            trackRepurchaseAddToCart(productId, quantity, cartId)
        }
    }

    private fun trackRepurchaseAddToCart(productId: String, quantity: Int, cartId: String) {
        val items = layoutList.filterIsInstance(RepurchaseProductUiModel::class.java)
        val item = items.firstOrNull { it.productCardModel.productId == productId }

        item?.let {
            val data = RepurchaseAddToCartTracker(quantity, cartId, it)
            _repurchaseAddToCartTracker.postValue(data)
        }
    }

    private fun updateAddToCartQuantity(
        productId: String,
        quantity: Int
    ) {
        layoutList.updateProductQuantity(productId, quantity)

        val data = RepurchaseLayoutUiModel(
            layoutList = layoutList,
            state = TokoNowLayoutState.UPDATE
        )

        _atcQuantity.postValue(Success(data))
    }

    private fun createProductListRequestParam(page: Int): GetRepurchaseProductListParam {
        val warehouses = AddressMapper.mapToWarehousesData(localCacheModel)
        val totalScan = productListMeta?.totalScan.orZero()
        val categoryIds = selectedCategoryFilter?.id
        val sort = selectedSortFilter
        val dateStart = selectedDateFilter.startDate
        val dateEnd = selectedDateFilter.endDate

        return GetRepurchaseProductListParam(
            warehouses = warehouses,
            sort = sort,
            totalScan = totalScan,
            page = page,
            catIds = categoryIds,
            dateStart = dateStart,
            dateEnd = dateEnd
        )
    }

    private fun setMiniCartAndProductQuantity(miniCart: MiniCartSimplifiedData) {
        setMiniCartData(miniCart)
        layoutList.updateProductATCQuantity(miniCart)
        layoutList.updateDeletedATCQuantity(miniCart, PRODUCT_REPURCHASE)
    }

    private suspend fun addEmptyState(@RepurchaseStaticLayoutId id: String) {
        when (id) {
            EMPTY_STATE_NO_HISTORY_SEARCH -> {
                val title = R.string.tokopedianow_repurchase_empty_state_no_history_title_search
                val description = R.string.tokopedianow_repurchase_empty_state_no_history_desc_search
                layoutList.addEmptyStateNoHistory(title, description)
                layoutList.removeAllProduct()
            }
            EMPTY_STATE_NO_HISTORY_FILTER -> {
                val title = R.string.tokopedianow_repurchase_empty_state_no_history_title_filter
                val description = R.string.tokopedianow_repurchase_empty_state_no_history_desc_filter
                layoutList.addEmptyStateNoHistory(title, description)
                layoutList.removeAllProduct()
            }
            EMPTY_STATE_OOC -> {
                layoutList.clear()
                layoutList.addChooseAddress()
                layoutList.addEmptyStateOoc(serviceType)
                layoutList.addProductRecommendationOoc(PAGE_NAME_RECOMMENDATION_OOC_PARAM)
            }
            ERROR_STATE_FAILED_TO_FETCH_DATA -> {
                layoutList.clear()
                layoutList.addServerErrorState()
            }
            else -> {
                layoutList.clear()
                layoutList.addChooseAddress()
                layoutList.addEmptyStateNoResult(serviceType)
                getCategoryMenuAsync().await()
                layoutList.addProductRecommendation(PAGE_NAME_RECOMMENDATION_NO_RESULT_PARAM)
            }
        }
    }
}
