package com.tokopedia.content.common.producttag.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.producttag.view.uimodel.ProductUiModel
import com.tokopedia.content.common.producttag.domain.repository.ProductTagRepository
import com.tokopedia.content.common.producttag.util.*
import com.tokopedia.content.common.producttag.util.AUTHOR_ID
import com.tokopedia.content.common.producttag.util.AUTHOR_TYPE
import com.tokopedia.content.common.producttag.util.AUTHOR_USER
import com.tokopedia.content.common.producttag.util.PRODUCT_TAG_SOURCE_RAW
import com.tokopedia.content.common.producttag.util.SHOP_BADGE
import com.tokopedia.content.common.producttag.util.extension.combine
import com.tokopedia.content.common.producttag.util.extension.isProductFound
import com.tokopedia.content.common.producttag.util.extension.setValue
import com.tokopedia.content.common.producttag.view.uimodel.*
import com.tokopedia.content.common.producttag.util.extension.removeLast
import com.tokopedia.content.common.producttag.util.preference.ProductTagPreference
import com.tokopedia.content.common.producttag.view.uimodel.ProductTagSource
import com.tokopedia.content.common.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.content.common.producttag.view.uimodel.config.ContentProductTagConfig
import com.tokopedia.content.common.producttag.view.uimodel.event.ProductTagUiEvent
import com.tokopedia.content.common.producttag.view.uimodel.state.*
import com.tokopedia.filter.common.helper.toMapParam
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toAmountString
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created By : Jonathan Darwin on April 25, 2022
 */
class ProductTagViewModel @AssistedInject constructor(
    @Assisted(PRODUCT_TAG_SOURCE_RAW) productTagSourceRaw: String,
    @Assisted(SHOP_BADGE) val shopBadge: String,
    @Assisted(AUTHOR_ID) val authorId: String,
    @Assisted(AUTHOR_TYPE) val authorType: String,
    @Assisted(INITIAL_SELECTED_PRODUCT) private val initialSelectedProduct: List<SelectedProductUiModel>,
    @Assisted(PRODUCT_TAG_CONFIG) private val productTagConfig: ContentProductTagConfig,
    private val repo: ProductTagRepository,
    private val userSession: UserSessionInterface,
    private val sharedPref: ProductTagPreference,
): ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted(PRODUCT_TAG_SOURCE_RAW) productTagSourceRaw: String,
            @Assisted(SHOP_BADGE) shopBadge: String,
            @Assisted(AUTHOR_ID) authorId: String,
            @Assisted(AUTHOR_TYPE) authorType: String,
            @Assisted(INITIAL_SELECTED_PRODUCT) initialSelectedProduct: List<SelectedProductUiModel>,
            @Assisted(PRODUCT_TAG_CONFIG) productTagConfig: ContentProductTagConfig,
        ): ProductTagViewModel
    }

    /** Public Getter */
    val isUser: Boolean
        get() = authorType == AUTHOR_USER

    val isSeller: Boolean
        get() = authorType == AUTHOR_SELLER

    val isShowCoachmarkGlobalTag: Boolean
        get() = sharedPref.isFirstGlobalTag() && isUser

    val productTagSourceList: List<ProductTagSource>
        get() = _productTagSourceList.value

    val selectedTagSource: ProductTagSource
        get() = _productTagSourceStack.value.lastOrNull() ?: ProductTagSource.Unknown

    val lastTaggedProductStateUnknown: Boolean
        get() = _lastTaggedProduct.value.state == PagedState.Unknown

    val lastPurchasedProductStateUnknown: Boolean
        get() = _lastPurchasedProduct.value.state == PagedState.Unknown

    val myShopStateUnknown: Boolean
        get() = _myShopProduct.value.state == PagedState.Unknown

    val globalStateProductStateUnknown: Boolean
        get() = _globalSearchProduct.value.state == PagedState.Unknown

    val globalStateShopStateUnknown: Boolean
        get() = _globalSearchShop.value.state == PagedState.Unknown

    val selectedShop: ShopUiModel
        get() = _shopProduct.value.shop

    val myShopSortList: List<SortUiModel>
        get() = _myShopSort.value.map {
            it.copy(isSelected = _myShopProduct.value.param.isParamFound(it.key, it.value))
        }

    val myShopQuery: String
        get() = _myShopProduct.value.param.query

    val globalSearchQuery: String
        get() = _globalSearchProduct.value.param.query

    val isSameAsInitialSelectedProduct: Boolean
        get() = initialSelectedProduct.sortedBy { it.id } == _selectedProduct.value.sortedBy { it.id }

    /** Config Public Getter */
    val isMultipleSelectionProduct: Boolean
        get() = productTagConfig.isMultipleSelectionProduct

    val maxSelectedProduct: Int
        get() = productTagConfig.maxSelectedProduct

    val backButton: ContentProductTagConfig.BackButton
        get() = productTagConfig.backButton

    val isShowActionBarDivider: Boolean
        get() = productTagConfig.isShowActionBarDivider

    val appLinkAfterAutocomplete: String
        get() = productTagConfig.appLinkAfterAutocomplete

    /** Flow */
    private val _productTagSourceList = MutableStateFlow<List<ProductTagSource>>(emptyList())
    private val _productTagSourceStack = MutableStateFlow(setOf(if(isSeller) ProductTagSource.MyShop else ProductTagSource.LastTagProduct))

    private val _lastTaggedProduct = MutableStateFlow(LastTaggedProductUiModel.Empty)
    private val _lastPurchasedProduct = MutableStateFlow(LastPurchasedProductUiModel.Empty)
    private val _myShopProduct = MutableStateFlow(MyShopProductUiModel.Empty)
    private val _globalSearchProduct = MutableStateFlow(GlobalSearchProductUiModel.Empty)
    private val _globalSearchShop = MutableStateFlow(GlobalSearchShopUiModel.Empty)
    private val _shopProduct = MutableStateFlow(ShopProductUiModel.Empty)

    private val _myShopSort = MutableStateFlow<List<SortUiModel>>(emptyList())

    private val _selectedProduct = MutableStateFlow<List<SelectedProductUiModel>>(emptyList())

    private val _isSubmitting = MutableStateFlow(false)

    /** Ui State */
    private val _productTagSourceUiState = combine(
        _productTagSourceList, _productTagSourceStack
    ) { productTagSourceList, productTagSourceStack ->
        ProductTagSourceUiState(
            productTagSourceList = productTagSourceList,
            productTagSourceStack = productTagSourceStack,
        )
    }

    private val _lastTaggedProductUiState = _lastTaggedProduct.map {
        LastTaggedProductUiState(
            products = it.products,
            nextCursor = it.nextCursor,
            state = it.state,
        )
    }

    private val _lastPurchasedProductUiState = _lastPurchasedProduct.map {
        LastPurchasedProductUiState(
            products = it.products,
            nextCursor = it.nextCursor,
            state = it.state,
            coachmark = it.coachmark,
            isCoachmarkShown = it.isCoachmarkShown,
        )
    }


    private val _myShopProductUiState = combine(
        _myShopProduct, _myShopSort,
    ) { myShopProduct, myShopSort ->
        MyShopProductUiState(
            products = myShopProduct.products,
            sorts = myShopSort,
            state = myShopProduct.state,
            param = myShopProduct.param,
        )
    }

    private val _globalSearchProductUiState = _globalSearchProduct.map {
        GlobalSearchProductUiState(
            products = it.products,
            quickFilters = it.quickFilters,
            sortFilters = it.sortFilters,
            state = it.state,
            param = it.param,
            suggestion = it.suggestion,
            ticker = it.ticker,
        )
    }

    private val _globalSearchShopUiState = _globalSearchShop.map {
        GlobalSearchShopUiState(
            shops = it.shops,
            quickFilters = it.quickFilters,
            sortFilters = it.sortFilters,
            state = it.state,
            param = it.param,
        )
    }

    private val _shopProductUiState = _shopProduct.map {
        ShopProductUiState(
            shop = it.shop,
            products = it.products,
            state = it.state,
            param = it.param,
        )
    }

    val uiState = combine(
        _productTagSourceUiState,
        _lastTaggedProductUiState,
        _lastPurchasedProductUiState,
        _myShopProductUiState,
        _globalSearchProductUiState,
        _globalSearchShopUiState,
        _shopProductUiState,
        _selectedProduct,
        _isSubmitting,
    ) { productTagSource, lastTaggedProduct, lastPurchasedProduct,
            myShopProduct, globalSearchProduct, globalSearchShop,
            shopProduct, selectedProduct, isSubmitting ->
        ProductTagUiState(
            productTagSource = productTagSource,
            lastTaggedProduct = lastTaggedProduct,
            lastPurchasedProduct = lastPurchasedProduct,
            myShopProduct = myShopProduct,
            globalSearchProduct = globalSearchProduct,
            globalSearchShop = globalSearchShop,
            shopProduct = shopProduct,
            selectedProduct = selectedProduct,
            isSubmitting = isSubmitting,
        )
    }

    /** Ui Event */
    private val _uiEvent = MutableSharedFlow<ProductTagUiEvent>()
    val uiEvent: Flow<ProductTagUiEvent>
        get() = _uiEvent

    init {
        processProductTagSource(productTagSourceRaw)
        _selectedProduct.update { initialSelectedProduct }
    }

    private fun processProductTagSource(productTagSourceRaw: String) {
        viewModelScope.launchCatchError(block = {
            val split = productTagSourceRaw.split(",")
            _productTagSourceList.value = split.map {
                ProductTagSource.mapFromString(it.trim())
            }
        }) { }
    }

    fun submitAction(action: ProductTagAction) {
        when(action) {
            is ProductTagAction.BackPressed -> handleBackPressed()
            ProductTagAction.ClickBreadcrumb -> handleClickBreadcrumb()
            ProductTagAction.OpenAutoCompletePage -> handleOpenAutoCompletePage()

            is ProductTagAction.SetDataFromAutoComplete -> handleSetDataFromAutoComplete(action.source, action.query, action.shopId, action.componentId)
            is ProductTagAction.SelectProductTagSource -> handleSelectProductTagSource(action.source)
            is ProductTagAction.ProductSelected -> handleProductSelected(action.product)
            ProductTagAction.ClickSaveButton -> handleClickSaveButton()

            /** Tagged Product */
            ProductTagAction.LoadLastTaggedProduct -> handleLoadLastTaggedProduct()

            /** Purchased Product */
            ProductTagAction.LoadLastPurchasedProduct -> handleLoadLastPurchasedProduct()

            /** My Shop Product */
            ProductTagAction.LoadMyShopProduct -> handleLoadMyShopProduct()
            is ProductTagAction.SearchMyShopProduct -> handleSearchMyShopProduct(action.query)
            ProductTagAction.OpenMyShopSortBottomSheet -> handleOpenMyShopSortBottomSheet()
            is ProductTagAction.ApplyMyShopSort -> handleApplyMyShopSort(action.selectedSort)

            /** Global Search Product */
            ProductTagAction.LoadGlobalSearchProduct -> handleLoadGlobalSearchProduct()
            ProductTagAction.SuggestionClicked -> handleSuggestionClicked()
            ProductTagAction.TickerClicked -> handleTickerClicked()
            ProductTagAction.CloseTicker -> handleCloseTicker()
            is ProductTagAction.SelectProductQuickFilter -> handleSelectProductQuickFilter(action.quickFilter)
            ProductTagAction.OpenProductSortFilterBottomSheet -> handleOpenProductSortFilterBottomSheet()
            is ProductTagAction.RequestProductFilterProductCount -> handleRequestProductFilterProductCount(action.selectedSortFilter)
            is ProductTagAction.ApplyProductSortFilter -> handleApplyProductSortFilter(action.selectedSortFilter)
            ProductTagAction.SwipeRefreshGlobalSearchProduct -> handleSwipeRefreshGlobalSearchProduct()

            /** Global Search Shop */
            ProductTagAction.LoadGlobalSearchShop -> handleLoadGlobalSearchShop()
            is ProductTagAction.ShopSelected -> handleShopSelected(action.shop)
            is ProductTagAction.SelectShopQuickFilter -> handleSelectShopQuickFilter(action.quickFilter)
            ProductTagAction.OpenShopSortFilterBottomSheet -> handleOpenShopSortFilterBottomSheet()
            is ProductTagAction.RequestShopFilterProductCount -> handleRequestShopFilterProductCount(action.selectedSortFilter)
            is ProductTagAction.ApplyShopSortFilter -> handleApplyShopSortFilter(action.selectedSortFilter)
            ProductTagAction.SwipeRefreshGlobalSearchShop -> handleSwipeRefreshGlobalSearchShop()

            /** Shop Product */
            ProductTagAction.LoadShopProduct -> handleLoadShopProduct()
            is ProductTagAction.SearchShopProduct -> handleSearchShopProduct(action.query)

            is ProductTagAction.LoadingSubmitProduct -> handleLoadingSubmit(action.isLoading)
        }
    }

    /** Handle Action */
    private fun handleBackPressed() {
        _productTagSourceStack.setValue { removeLast() }
    }

    private fun handleClickBreadcrumb() {
        viewModelScope.launch {
            _uiEvent.emit(ProductTagUiEvent.ShowSourceBottomSheet)
            sharedPref.setNotFirstGlobalTag()
        }
    }

    private fun handleOpenAutoCompletePage() {
        viewModelScope.launch {
            when(productTagConfig.isFullPageAutocomplete) {
                true -> {
                    _uiEvent.emit(ProductTagUiEvent.OpenAutoCompletePage(_globalSearchProduct.value.param.query))
                }
                else -> {
                    _productTagSourceStack.update {
                        val newStack = it.toMutableSet()
                        newStack.add(ProductTagSource.Autocomplete)
                        newStack
                    }
                }
            }
        }
    }

    private fun handleSetDataFromAutoComplete(source: ProductTagSource, query: String, shopId: String, componentId: String) {
        viewModelScope.launchCatchError(block = {
            when(source) {
                ProductTagSource.GlobalSearch -> {
                    _globalSearchProduct.setValue {
                        val prevParam = _globalSearchProduct.value.param.apply {
                            this.prevQuery = this.query
                            this.query = query
                        }
                        GlobalSearchProductUiModel.Empty.copy(param = initParam(prevParam).copy().apply {
                            this.componentId = componentId
                        })
                    }

                    _globalSearchShop.setValue {
                        val prevParam = _globalSearchShop.value.param.apply {
                            this.prevQuery = this.query
                            this.query = query
                        }
                        GlobalSearchShopUiModel.Empty.copy(param = initParam(prevParam).copy().apply {
                            this.componentId = componentId
                        })
                    }
                }
                ProductTagSource.Shop -> {
                    val shop = repo.getShopInfoByID(listOf(shopId.toLong()))
                    _shopProduct.setValue {
                        ShopProductUiModel.Empty.copy(
                            shop = shop,
                            param = param.copy().apply { this.query = query },
                        )
                    }
                }
                else -> {}
            }

            submitAction(ProductTagAction.SelectProductTagSource(source))
        }) {
            _uiEvent.emit(ProductTagUiEvent.ShowError(it) {
                submitAction(ProductTagAction.SetDataFromAutoComplete(source, query, shopId, componentId))
            })
        }
    }

    private fun handleSelectProductTagSource(source: ProductTagSource) {
        val finalSource = if(isNeedToShowDefaultSource(source)) ProductTagSource.LastTagProduct else source
        _productTagSourceStack.setValue { setOf(finalSource) }
    }

    private fun handleProductSelected(product: ProductUiModel) {
        if (_isSubmitting.value) return

        viewModelScope.launch {
            if(isMultipleSelectionProduct) {

                val currSelectedProduct = _selectedProduct.value
                val newSelectedProduct = if(currSelectedProduct.isProductFound(product)) {
                    currSelectedProduct.filter { it.id != product.id }
                }
                else {
                    val currSelectedProductSize = _selectedProduct.value.size

                    if(currSelectedProductSize < maxSelectedProduct) {
                        currSelectedProduct.toMutableList().apply {
                            add(SelectedProductUiModel.createOnlyId(id = product.id))
                        }.toList()
                    }
                    else {
                        _uiEvent.emit(ProductTagUiEvent.MaxSelectedProductReached)

                        currSelectedProduct
                    }
                }

                _selectedProduct.value = newSelectedProduct
            }
            else {
                _uiEvent.emit(ProductTagUiEvent.FinishProductTag(listOf(product.toSelectedProduct())))
            }
        }
    }

    private fun handleClickSaveButton() {
        viewModelScope.launch {
            _uiEvent.emit(ProductTagUiEvent.FinishProductTag(_selectedProduct.value))
        }
    }

    private fun handleLoadLastTaggedProduct() {
        viewModelScope.launchCatchError(block = {
            val currLastProduct = _lastTaggedProduct.value

            if(currLastProduct.state.isLoading || currLastProduct.state.isNextPage.not()) return@launchCatchError

            _lastTaggedProduct.setValue {
                copy(state = PagedState.Loading)
            }

            val pagedDataList = repo.getLastTaggedProducts(
                authorId = authorId,
                authorType = authorType,
                cursor = currLastProduct.nextCursor,
                limit = LIMIT_PER_PAGE,
            )

            _lastTaggedProduct.setValue {
                copy(
                    products = products + pagedDataList.dataList,
                    nextCursor = pagedDataList.nextCursor,
                    state = PagedState.Success(
                        hasNextPage = pagedDataList.hasNextPage,
                    )
                )
            }
        }) {
            _lastTaggedProduct.setValue {
                copy(
                    state = PagedState.Error(it)
                )
            }
        }
    }

    private fun handleLoadLastPurchasedProduct() {
        viewModelScope.launchCatchError(block = {
            val currLastProduct = _lastPurchasedProduct.value

            if(currLastProduct.state.isLoading) return@launchCatchError

            _lastPurchasedProduct.setValue {
                copy(state = PagedState.Loading)
            }

            val response = repo.getLastPurchasedProducts(
                cursor = currLastProduct.nextCursor,
                limit = LIMIT_LAST_PURCHASE_PRODUCT_PER_PAGE,
            )

            _lastPurchasedProduct.value = response
        }) {
            _lastPurchasedProduct.setValue {
                copy(
                    state = PagedState.Error(it)
                )
            }
        }
    }

    private fun handleLoadMyShopProduct() {
        viewModelScope.launchCatchError(block = {
            val myShopProduct = _myShopProduct.value

            if(myShopProduct.state.isLoading || myShopProduct.state.isNextPage.not()) return@launchCatchError

            _myShopProduct.setValue {
                copy(state = PagedState.Loading)
            }

            val newParam = myShopProduct.param.copy().apply {
                shopId = userSession.shopId
                userId = userSession.userId
            }

            val result = repo.searchAceProducts(param = newParam)

            /** Update Param */
            newParam.start = result.pagedData.nextCursor.toInt()

            _myShopProduct.setValue {
                copy(
                    products = products + result.pagedData.dataList,
                    param = newParam,
                    state = PagedState.Success(
                        hasNextPage = result.pagedData.hasNextPage,
                    )
                )
            }
        }) {
            _myShopProduct.setValue {
                copy(
                    state = PagedState.Error(it)
                )
            }
        }
    }

    private fun handleSearchMyShopProduct(query: String) {
        if(_myShopProduct.value.param.query == query) return

        val newParam = _myShopProduct.value.param.apply {
            resetPagination()
            this.query = query
        }
        _myShopProduct.setValue { MyShopProductUiModel.Empty.copy(param = newParam) }

        handleLoadMyShopProduct()
    }

    private fun handleOpenMyShopSortBottomSheet() {
        viewModelScope.launchCatchError(block = {
            if(_myShopSort.value.isEmpty()) {
                val prevParam = _myShopProduct.value.param
                val param = initParam(prevParam).apply {
                    source = SearchParamUiModel.SOURCE_SEARCH_PRODUCT
                }

                repo.getSortFilter(param).data.sort
                    .map { item ->
                        SortUiModel(
                            text = item.name,
                            key = item.key,
                            value = item.value,
                            isSelected = false,
                        )
                    }.also { _myShopSort.setValue { it } }
            }

            _uiEvent.emit(ProductTagUiEvent.OpenMyShopSortBottomSheet)
        }) {
            _uiEvent.emit(ProductTagUiEvent.ShowError(it) {
                submitAction(ProductTagAction.OpenMyShopSortBottomSheet)
            })
        }
    }

    private fun handleApplyMyShopSort(selectedSort: SortUiModel) {
        val currState = _myShopProduct.value
        if(currState.param.isParamFound(selectedSort.key, selectedSort.value)) return

        val prevParam = currState.param
        val newParam = initParam(prevParam).apply {
            addParam(selectedSort.key, selectedSort.value)
        }

        _myShopProduct.setValue { MyShopProductUiModel.Empty.copy(param = newParam) }

        handleLoadMyShopProduct()
    }

    private fun handleLoadGlobalSearchProduct() {
        viewModelScope.launchCatchError(block = {
            val globalSearchProduct = _globalSearchProduct.value

            if(globalSearchProduct.state.isLoading || globalSearchProduct.state.isNextPage.not()) return@launchCatchError

            _globalSearchProduct.setValue {
                copy(state = PagedState.Loading)
            }

            val quickFilters = repo.getQuickFilter(
                query = globalSearchProduct.param.query,
                extraParams = globalSearchProduct.param.joinToString(),
            )

            val newParam = globalSearchProduct.param.copy().apply {
                userId = userSession.userId
            }

            val result = repo.searchAceProducts(param = newParam)

            if(newParam.isFirstPage) {
                _uiEvent.emit(ProductTagUiEvent.HitGlobalSearchProductTracker(result.header, newParam))
            }

            /** Update Param */
            newParam.start = result.pagedData.nextCursor.toInt()

            _globalSearchProduct.setValue {
                copy(
                    products = products + result.pagedData.dataList,
                    quickFilters = quickFilters,
                    param = newParam,
                    state = PagedState.Success(
                        hasNextPage = result.pagedData.hasNextPage,
                    ),
                    suggestion = result.suggestion,
                    ticker = result.ticker,
                )
            }
        }) {
            _globalSearchProduct.setValue {
                copy(
                    state = PagedState.Error(it)
                )
            }
        }
    }

    private fun handleSuggestionClicked() {
        val suggestionQuery = _globalSearchProduct.value.suggestion.suggestion
        if(suggestionQuery.isEmpty()) return

        _globalSearchProduct.setValue {
            val prevParamProduct = _globalSearchProduct.value.param.apply {
                this.prevQuery = this.query
                this.query = suggestionQuery
            }
            GlobalSearchProductUiModel.Empty.copy(param = initParam(prevParamProduct))
        }

        _globalSearchShop.setValue {
            val prevParamShop = _globalSearchShop.value.param.apply {
                this.prevQuery = this.query
                this.query = suggestionQuery
            }
            GlobalSearchShopUiModel.Empty.copy(param = initParam(prevParamShop))
        }

        handleLoadGlobalSearchProduct()
        handleLoadGlobalSearchShop()
    }

    private fun handleTickerClicked() {
        val tickerParam = _globalSearchProduct.value.ticker.query
        if(tickerParam.isEmpty()) return

        val prevParam = _globalSearchProduct.value.param
        val newParam = initParam(prevParam).apply {
            tickerParam.toMapParam().forEach {
                addParam(it.key, it.value)
            }
        }

        _globalSearchProduct.setValue {
            GlobalSearchProductUiModel.Empty.copy(param = newParam)
        }

        handleLoadGlobalSearchProduct()
    }

    private fun handleCloseTicker() {
        _globalSearchProduct.setValue {
            copy(ticker = TickerUiModel())
        }
    }

    private fun handleSelectProductQuickFilter(quickFilter: QuickFilterUiModel) {
        val currState = _globalSearchProduct.value

        val newParam = currState.param.copy().apply {
            resetPagination()

            if(isParamFound(quickFilter.key, quickFilter.value))
                removeParam(quickFilter.key, quickFilter.value)
            else addParam(quickFilter.key, quickFilter.value)
        }

        _globalSearchProduct.setValue {
            GlobalSearchProductUiModel.Empty.copy(param = newParam)
        }

        handleLoadGlobalSearchProduct()
    }

    private fun handleOpenProductSortFilterBottomSheet() {
        viewModelScope.launchCatchError(block = {
            val currState = _globalSearchProduct.value

            val prevParam = currState.param
            val param = initParam(prevParam).apply {
                source = SearchParamUiModel.SOURCE_SEARCH_PRODUCT
            }

            val sortFilters = if(currState.sortFilters.isEmpty()) {
                repo.getSortFilter(param).also {
                    _globalSearchProduct.setValue { copy(sortFilters = it) }
                }
            } else currState.sortFilters

            _uiEvent.emit(ProductTagUiEvent.OpenProductSortFilterBottomSheet(currState.param, sortFilters))
        }) {
            _uiEvent.emit(ProductTagUiEvent.ShowError(it) {
                submitAction(ProductTagAction.OpenProductSortFilterBottomSheet)
            })
        }
    }

    private fun handleRequestProductFilterProductCount(selectedSortFilter: Map<String, Any>) {
        viewModelScope.launchCatchError(block = {
            val result = repo.getSortFilterProductCount(SearchParamUiModel(HashMap(selectedSortFilter)))

            _uiEvent.emit(ProductTagUiEvent.SetProductFilterProductCount(NetworkResult.Success(result)))
        }) {
            _uiEvent.emit(ProductTagUiEvent.SetProductFilterProductCount(NetworkResult.Error(it)))
        }
    }

    private fun handleApplyProductSortFilter(selectedSortFilter: Map<String, String>) {
        val prevParam = _globalSearchProduct.value.param
        val newParam = initParam(prevParam)

        selectedSortFilter.forEach { newParam.addParam(it.key, it.value) }

        _globalSearchProduct.setValue {
            GlobalSearchProductUiModel.Empty.copy(param = newParam)
        }

        handleLoadGlobalSearchProduct()
    }

    private fun handleSwipeRefreshGlobalSearchProduct() {
        val newParam = _globalSearchProduct.value.param.apply { resetPagination() }
        _globalSearchProduct.setValue {
            GlobalSearchProductUiModel.Empty.copy(param = newParam)
        }

        handleLoadGlobalSearchProduct()
    }

    private fun handleLoadGlobalSearchShop() {
        viewModelScope.launchCatchError(block = {
            val globalSearchShop = _globalSearchShop.value

            if(globalSearchShop.state.isLoading || globalSearchShop.state.isNextPage.not()) return@launchCatchError

            _globalSearchShop.setValue {
                copy(state = PagedState.Loading)
            }

            val newParam = globalSearchShop.param.copy().apply {
                userId = userSession.userId
                pageSource = SearchParamUiModel.SOURCE_SEARCH_SHOP
            }

            val quickFilters = repo.getQuickFilter(
                query = newParam.query,
                extraParams = newParam.joinToString(),
            )

            val result = repo.searchAceShops(param = newParam)

            if(newParam.isFirstPage) {
                _uiEvent.emit(ProductTagUiEvent.HitGlobalSearchShopTracker(result.header, newParam))
            }

            /** Update Param */
            newParam.start = result.pagedData.nextCursor.toInt()

            _globalSearchShop.setValue {
                copy(
                    shops = shops + result.pagedData.dataList,
                    quickFilters = quickFilters,
                    state = PagedState.Success(
                        hasNextPage = result.pagedData.hasNextPage,
                    ),
                    param = newParam,
                )
            }
        }) {
            _globalSearchShop.setValue {
                copy(
                    state = PagedState.Error(it)
                )
            }
        }
    }

    private fun handleShopSelected(shop: ShopUiModel) {
        if(shop.isShopAccessible) {
            _shopProduct.setValue { ShopProductUiModel.Empty.copy(shop = shop) }
            _productTagSourceStack.setValue { toMutableSet().apply { add(ProductTagSource.Shop) } }
        }
    }

    private fun handleSelectShopQuickFilter(quickFilter: QuickFilterUiModel) {
        val currState = _globalSearchShop.value

        val newParam = currState.param.copy().apply {
            resetPagination()

            if(isParamFound(quickFilter.key, quickFilter.value))
                removeParam(quickFilter.key, quickFilter.value)
            else addParam(quickFilter.key, quickFilter.value)
        }

        _globalSearchShop.setValue {
            GlobalSearchShopUiModel.Empty.copy(param = newParam)
        }

        handleLoadGlobalSearchShop()
    }

    private fun handleOpenShopSortFilterBottomSheet() {
        viewModelScope.launchCatchError(block = {
            val currState = _globalSearchShop.value

            val prevParam = currState.param
            val param = initParam(prevParam).apply {
                source = SearchParamUiModel.SOURCE_SEARCH_SHOP
                pageSource = SearchParamUiModel.SOURCE_SEARCH_SHOP
            }

            val sortFilters = if(currState.sortFilters.isEmpty()) {
                repo.getSortFilter(param).also {
                    _globalSearchShop.setValue { copy(sortFilters = it) }
                }
            } else currState.sortFilters

            _uiEvent.emit(ProductTagUiEvent.OpenShopSortFilterBottomSheet(currState.param, sortFilters))
        }) {
            _uiEvent.emit(ProductTagUiEvent.ShowError(it) {
                submitAction(ProductTagAction.OpenShopSortFilterBottomSheet)
            })
        }
    }

    private fun handleRequestShopFilterProductCount(selectedSortFilter: Map<String, Any>) {
        viewModelScope.launchCatchError(block = {
            /** Need to remove "device" param somehow */
            val param = SearchParamUiModel(HashMap(selectedSortFilter)).apply {
                device = ""
            }
            val result = repo.searchAceShops(param)

            _uiEvent.emit(ProductTagUiEvent.SetShopFilterProductCount(NetworkResult.Success(result.totalShop.toAmountString())))
        }) {
            _uiEvent.emit(ProductTagUiEvent.SetShopFilterProductCount(NetworkResult.Error(it)))
        }
    }

    private fun handleApplyShopSortFilter(selectedSortFilter: Map<String, String>) {
        val prevParam = _globalSearchShop.value.param
        val newParam = initParam(prevParam)

        selectedSortFilter.forEach { newParam.addParam(it.key, it.value) }

        _globalSearchShop.setValue {
            GlobalSearchShopUiModel.Empty.copy(param = newParam)
        }

        handleLoadGlobalSearchShop()
    }

    private fun handleSwipeRefreshGlobalSearchShop() {
        val newParam = _globalSearchShop.value.param.apply { resetPagination() }
        _globalSearchShop.setValue {
            GlobalSearchShopUiModel.Empty.copy(param = newParam)
        }

        handleLoadGlobalSearchShop()
    }

    private fun handleLoadShopProduct() {
        viewModelScope.launchCatchError(block = {
            val shopProduct = _shopProduct.value

            if(shopProduct.state.isLoading || shopProduct.state.isNextPage.not()) return@launchCatchError

            _shopProduct.setValue {
                copy(state = PagedState.Loading)
            }

            val newParam = shopProduct.param.copy().apply {
                shopId = shopProduct.shop.shopId
                userId = userSession.userId
            }

            val result = repo.searchAceProducts(param = newParam)

            val nextCursor = result.pagedData.nextCursor.toInt()
            newParam.start = nextCursor

            _shopProduct.setValue {
                copy(
                    products = products + result.pagedData.dataList,
                    param = newParam,
                    state = PagedState.Success(
                        hasNextPage = result.pagedData.hasNextPage,
                    )
                )
            }
        }) {
            _shopProduct.setValue {
                copy(
                    state = PagedState.Error(it)
                )
            }
        }
    }

    private fun handleSearchShopProduct(query: String) {
        if(_shopProduct.value.param.query == query) return

        _shopProduct.setValue {

            ShopProductUiModel.Empty.copy(
                shop = shop,
                param = initParam(_shopProduct.value.param).apply { this.query = query }
            )
        }
        handleLoadShopProduct()
    }

    private fun handleLoadingSubmit(isLoading: Boolean) {
        _isSubmitting.value = isLoading
    }

    /** Util */
    private fun isNeedToShowDefaultSource(source: ProductTagSource): Boolean {
        return source == ProductTagSource.GlobalSearch && _globalSearchProduct.value.param.query.isEmpty()
    }

    private fun initParam(prevParam: SearchParamUiModel): SearchParamUiModel {
        return SearchParamUiModel.Empty.apply {
            this.prevQuery = prevParam.prevQuery
            this.query = prevParam.query
            this.componentId = prevParam.componentId
        }
    }

    companion object {
        private const val LIMIT_PER_PAGE = 20
        private const val LIMIT_LAST_PURCHASE_PRODUCT_PER_PAGE = 5
    }
}
