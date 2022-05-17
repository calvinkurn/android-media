package com.tokopedia.createpost.producttag.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.createpost.producttag.domain.repository.ProductTagRepository
import com.tokopedia.createpost.producttag.util.AUTHOR_ID
import com.tokopedia.createpost.producttag.util.AUTHOR_TYPE
import com.tokopedia.createpost.producttag.util.extension.combine
import com.tokopedia.createpost.producttag.util.extension.currentSource
import com.tokopedia.createpost.producttag.util.extension.setValue
import com.tokopedia.createpost.producttag.view.uimodel.*
import com.tokopedia.createpost.producttag.util.PRODUCT_TAG_SOURCE_RAW
import com.tokopedia.createpost.producttag.util.SHOP_BADGE
import com.tokopedia.createpost.producttag.util.extension.removeLast
import com.tokopedia.createpost.producttag.view.uimodel.ProductTagSource
import com.tokopedia.createpost.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.createpost.producttag.view.uimodel.event.ProductTagUiEvent
import com.tokopedia.createpost.producttag.view.uimodel.state.*
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
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
    @Assisted(AUTHOR_ID) private val authorId: String,
    @Assisted(AUTHOR_TYPE) private val authorType: String,
    private val repo: ProductTagRepository,
    private val userSession: UserSessionInterface,
): ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted(PRODUCT_TAG_SOURCE_RAW) productTagSourceRaw: String,
            @Assisted(SHOP_BADGE) shopBadge: String,
            @Assisted(AUTHOR_ID) authorId: String,
            @Assisted(AUTHOR_TYPE) authorType: String,
        ): ProductTagViewModel
    }

    /** Public Getter */
    val productTagSourceList: List<ProductTagSource>
        get() = _productTagSourceList.value

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

    /** Flow */
    private val _productTagSourceList = MutableStateFlow<List<ProductTagSource>>(emptyList())
    private val _productTagSourceStack = MutableStateFlow<Set<ProductTagSource>>(setOf(ProductTagSource.Unknown))

    private val _lastTaggedProduct = MutableStateFlow(LastTaggedProductUiModel.Empty)
    private val _lastPurchasedProduct = MutableStateFlow(LastPurchasedProductUiModel.Empty)
    private val _myShopProduct = MutableStateFlow(MyShopProductUiModel.Empty)
    private val _globalSearchProduct = MutableStateFlow(GlobalSearchProductUiModel.Empty)
    private val _globalSearchShop = MutableStateFlow(GlobalSearchShopUiModel.Empty)
    private val _shopProduct = MutableStateFlow(ShopProductUiModel.Empty)

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

    private val _myShopProductUiState = _myShopProduct.map {
        MyShopProductUiState(
            products = it.products,
            nextCursor = it.nextCursor,
            state = it.state,
            param = it.param,
        )
    }

    private val _globalSearchProductUiState = _globalSearchProduct.map {
        GlobalSearchProductUiState(
            products = it.products,
            quickFilters = it.quickFilters,
            nextCursor = it.nextCursor,
            state = it.state,
            param = it.param,
            suggestion = it.suggestion,
            ticker = it.ticker,
        )
    }

    private val _globalSearchShopUiState = _globalSearchShop.map {
        GlobalSearchShopUiState(
            shops = it.shops,
            nextCursor = it.nextCursor,
            state = it.state,
            query = it.query,
        )
    }

    private val _shopProductUiState = _shopProduct.map {
        ShopProductUiState(
            shop = it.shop,
            products = it.products,
            nextCursor = it.nextCursor,
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
    ) { productTagSource, lastTaggedProduct, lastPurchasedProduct,
            myShopProduct, globalSearchProduct, globalSearchShop,
            shopProduct ->
        ProductTagUiState(
            productTagSource = productTagSource,
            lastTaggedProduct = lastTaggedProduct,
            lastPurchasedProduct = lastPurchasedProduct,
            myShopProduct = myShopProduct,
            globalSearchProduct = globalSearchProduct,
            globalSearchShop = globalSearchShop,
            shopProduct = shopProduct,
        )
    }

    /** Ui Event */
    private val _uiEvent = MutableSharedFlow<ProductTagUiEvent>()
    val uiEvent: Flow<ProductTagUiEvent>
        get() = _uiEvent

    init {
        processProductTagSource(productTagSourceRaw)
    }

    private fun processProductTagSource(productTagSourceRaw: String) {
        viewModelScope.launchCatchError(block = {
            val split = productTagSourceRaw.split(",")
            _productTagSourceList.value = split.map {
                ProductTagSource.mapFromString(it)
            }
        }) { }
    }

    fun submitAction(action: ProductTagAction) {
        when(action) {
            is ProductTagAction.BackPressed -> handleBackPressed()
            ProductTagAction.ClickBreadcrumb -> handleClickBreadcrumb()
            ProductTagAction.ClickSearchBar -> handleClickSearchBar()

            is ProductTagAction.SetDataFromAutoComplete -> handleSetDataFromAutoComplete(action.source, action.query, action.shopId)
            is ProductTagAction.SelectProductTagSource -> handleSelectProductTagSource(action.source)
            is ProductTagAction.ProductSelected -> handleProductSelected(action.product)

            /** Tagged Product */
            ProductTagAction.LoadLastTaggedProduct -> handleLoadLastTaggedProduct()

            /** Purchased Product */
            ProductTagAction.LoadLastPurchasedProduct -> handleLoadLastPurchasedProduct()

            /** My Shop Product */
            ProductTagAction.LoadMyShopProduct -> handleLoadMyShopProduct()
            is ProductTagAction.SearchMyShopProduct -> handleSearchMyShopProduct(action.query)

            /** Global Search Product */
            ProductTagAction.LoadGlobalSearchProduct -> handleLoadGlobalSearchProduct()
            ProductTagAction.TickerClicked -> handleTickerClicked()
            ProductTagAction.CloseTicker -> handleCloseTicker()
            is ProductTagAction.SelectQuickFilter -> handleSelectQuickFilter(action.quickFilter)

            /** Global Search Shop */
            ProductTagAction.LoadGlobalSearchShop -> handleLoadGlobalSearchShop()
            is ProductTagAction.ShopSelected -> handleShopSelected(action.shop)

            /** Shop Product */
            ProductTagAction.LoadShopProduct -> handleLoadShopProduct()
            is ProductTagAction.SearchShopProduct -> handleSearchShopProduct(action.query)
        }
    }

    /** Handle Action */
    private fun handleBackPressed() {
        _productTagSourceStack.setValue { removeLast() }
    }

    private fun handleClickBreadcrumb() {
        viewModelScope.launch {
            when(_productTagSourceStack.value.size) {
                1 -> {
                    _uiEvent.emit(ProductTagUiEvent.ShowSourceBottomSheet)
                }
                2 -> {
                    _productTagSourceStack.setValue { removeLast() }
                }
            }
        }
    }

    private fun handleClickSearchBar() {
        viewModelScope.launch {
            _uiEvent.emit(ProductTagUiEvent.OpenAutoCompletePage(_globalSearchProduct.value.param.query))
        }
    }

    private fun handleSetDataFromAutoComplete(source: ProductTagSource, query: String, shopId: String) {
        viewModelScope.launchCatchError(block = {
            when(source) {
                ProductTagSource.GlobalSearch -> {
                    val newParam = SearchParamUiModel.Empty.apply {
                        this.query = query
                    }
                    _globalSearchProduct.setValue {
                        GlobalSearchProductUiModel.Empty.copy(param = newParam)
                    }
                    _globalSearchShop.setValue {
                        GlobalSearchShopUiModel.Empty.copy(query = query)
                    }
                }
                ProductTagSource.Shop -> {
                    val shop = getShopInfo(shopId)
                    _shopProduct.setValue {
                        ShopProductUiModel.Empty.copy(
                            shop = shop,
                            param = param.copy().apply { this.query = query },
                        )
                    }
                }
                else -> {}
            }
        }) { }
    }

    private fun handleSelectProductTagSource(source: ProductTagSource) {
        if(_productTagSourceStack.value.size == 1) {
            val finalSource = if(isNeedToShowDefaultSource(source)) ProductTagSource.LastTagProduct
                                else source
            _productTagSourceStack.setValue { setOf(finalSource) }
        }
    }

    private fun handleProductSelected(product: ProductUiModel) {
        viewModelScope.launch {
            _uiEvent.emit(ProductTagUiEvent.ProductSelected(product))
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
            }

            val result = repo.searchAceProducts(param = newParam)

            /** Update Param */
            val nextCursor = result.pagedData.nextCursor.toInt()
            newParam.start = nextCursor

            _myShopProduct.setValue {
                copy(
                    products = products + result.pagedData.dataList,
                    nextCursor = nextCursor,
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
        _myShopProduct.setValue { MyShopProductUiModel.Empty.copy(
                param = param.copy().apply {
                    this.query = query
                }
            )
        }
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
                extraParams = "" /** TODO: empty for not */
            )

            val newParam = globalSearchProduct.param.copy().apply {
                userId = userSession.userId
            }

            val result = repo.searchAceProducts(param = newParam)

            /** Update Param */
            val nextCursor = result.pagedData.nextCursor.toInt()
            newParam.start = nextCursor

            _globalSearchProduct.setValue {
                copy(
                    products = products + result.pagedData.dataList,
                    quickFilters = quickFilters,
                    nextCursor = nextCursor,
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

    private fun handleTickerClicked() {
        if(_globalSearchProduct.value.ticker.query.isEmpty()) return

        /** TODO: refresh global search && apply additional param from query */
    }

    private fun handleCloseTicker() {
        _globalSearchProduct.setValue {
            copy(ticker = TickerUiModel())
        }
    }

    private fun handleSelectQuickFilter(quickFilter: QuickFilterUiModel) {
        _globalSearchProduct.setValue {
            copy(
                selectedQuickFilters = selectedQuickFilters.toMutableList().apply {
                    val isExists = firstOrNull { it == quickFilter } != null
                    if(isExists) remove(quickFilter)
                    else add(quickFilter)
                }
            )
        }

        /** TODO: it should refresh the page */
    }

    private fun handleLoadGlobalSearchShop() {
        viewModelScope.launchCatchError(block = {
            val globalSearchShop = _globalSearchShop.value

            if(globalSearchShop.state.isLoading || globalSearchShop.state.isNextPage.not()) return@launchCatchError

            _globalSearchShop.setValue {
                copy(state = PagedState.Loading)
            }

            val pagedDataList = repo.searchAceShops(
                rows = LIMIT_PER_PAGE,
                start = globalSearchShop.nextCursor,
                query = globalSearchShop.query,
                sort = 9 /** TODO: gonna change this later */
            )

            _globalSearchShop.setValue {
                copy(
                    shops = shops + pagedDataList.dataList,
                    nextCursor = pagedDataList.nextCursor.toInt(),
                    state = PagedState.Success(
                        hasNextPage = pagedDataList.hasNextPage,
                    )
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

    private fun handleLoadShopProduct() {
        viewModelScope.launchCatchError(block = {
            val shopProduct = _shopProduct.value

            if(shopProduct.state.isLoading || shopProduct.state.isNextPage.not()) return@launchCatchError

            _shopProduct.setValue {
                copy(state = PagedState.Loading)
            }

            val newParam = shopProduct.param.copy().apply {
                shopId = shopProduct.shop.shopId
            }

            val result = repo.searchAceProducts(param = newParam)

            val nextCursor = result.pagedData.nextCursor.toInt()
            newParam.start = nextCursor

            _shopProduct.setValue {
                copy(
                    products = products + result.pagedData.dataList,
                    nextCursor = nextCursor,
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
        _shopProduct.setValue { ShopProductUiModel.Empty.copy(
                shop = shop,
                param = param.copy().apply { this.query = query }
            )
        }
        handleLoadShopProduct()
    }

    /** Util */
    private suspend fun getShopInfo(shopId: String): ShopUiModel {
        /** TODO: gonna hit GQL from shop_page || ask BE to modify the applink and provide shopName and shopBadge.
         * iOS team get it from SuggestionData which we can't use bcs the autocomplete module
         * forces us to use applink,
         * the other ways is consume the shopInfo from ace_search_product, but if the product is not found,
         * we can't get the shopInfo and will leave the breadcrumb empty :(
         * */
        return ShopUiModel(
            shopId = shopId,
            shopName = "Testing saja",
            shopImage = "",
            shopLocation = "",
            shopGoldShop = 1,
            shopStatus = 1,
            isOfficial = true,
            isPMPro = true,
        )
    }

    private fun isNeedToShowDefaultSource(source: ProductTagSource): Boolean {
        return source == ProductTagSource.GlobalSearch && _globalSearchProduct.value.param.query.isEmpty()
    }

    companion object {
        private const val LIMIT_PER_PAGE = 20
        private const val LIMIT_LAST_PURCHASE_PRODUCT_PER_PAGE = 5
    }
}