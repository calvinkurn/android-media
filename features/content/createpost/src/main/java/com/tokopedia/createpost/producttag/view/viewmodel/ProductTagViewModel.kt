package com.tokopedia.createpost.producttag.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.createpost.producttag.domain.repository.ProductTagRepository
import com.tokopedia.createpost.producttag.util.extension.combine
import com.tokopedia.createpost.producttag.util.extension.currentSource
import com.tokopedia.createpost.producttag.util.extension.setValue
import com.tokopedia.createpost.producttag.view.uimodel.*
import com.tokopedia.createpost.producttag.util.PRODUCT_TAG_SOURCE_RAW
import com.tokopedia.createpost.producttag.util.SHOP_BADGE
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
    @Assisted("authorId") private val authorId: String,
    @Assisted("authorType") private val authorType: String,
    private val repo: ProductTagRepository,
    private val userSession: UserSessionInterface,
): ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted(PRODUCT_TAG_SOURCE_RAW) productTagSourceRaw: String,
            @Assisted(SHOP_BADGE) shopBadge: String,
            @Assisted("authorId") authorId: String,
            @Assisted("authorType") authorType: String,
        ): ProductTagViewModel
    }

    /** Public Getter */
    val productTagSourceList: List<ProductTagSource>
        get() = _productTagSourceList.value

    val selectedTagSource: ProductTagSource
        get() = _productTagSourceStack.value.currentSource

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
            query = it.query,
        )
    }

    private val _globalSearchProductUiState = _globalSearchProduct.map {
        GlobalSearchProductUiState(
            products = it.products,
            nextCursor = it.nextCursor,
            state = it.state,
            query = it.query,
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
            query = it.query,
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
        _productTagSourceStack.setValue {
            toMutableSet().apply {
                lastOrNull()?.let { remove(it) }
            }
        }
    }

    private fun handleSelectProductTagSource(source: ProductTagSource) {
        _productTagSourceStack.setValue { setOf(source) }
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

            val pagedDataList = repo.searchAceProducts(
                rows = LIMIT_PER_PAGE,
                start = myShopProduct.nextCursor,
                query = myShopProduct.query,
                shopId = userSession.shopId,
                userId = "",
                sort = 9 /** TODO: gonna change this later */
            )

            _myShopProduct.setValue {
                copy(
                    products = products + pagedDataList.dataList,
                    nextCursor = pagedDataList.nextCursor.toInt(),
                    state = PagedState.Success(
                        hasNextPage = pagedDataList.hasNextPage,
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
                query = query,
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

            val pagedDataList = repo.searchAceProducts(
                rows = LIMIT_PER_PAGE,
                start = globalSearchProduct.nextCursor,
                query = globalSearchProduct.query,
                shopId = "",
                userId = userSession.userId,
                sort = 9 /** TODO: gonna change this later */
            )

            _globalSearchProduct.setValue {
                copy(
                    products = products + pagedDataList.dataList,
                    nextCursor = pagedDataList.nextCursor.toInt(),
                    state = PagedState.Success(
                        hasNextPage = pagedDataList.hasNextPage,
                    )
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
        _shopProduct.setValue { ShopProductUiModel.Empty.copy(shop = shop) }
        _productTagSourceStack.setValue { toMutableSet().apply { add(ProductTagSource.Shop) } }
    }

    private fun handleLoadShopProduct() {
        viewModelScope.launchCatchError(block = {
            val shopProduct = _shopProduct.value

            if(shopProduct.state.isLoading || shopProduct.state.isNextPage.not()) return@launchCatchError

            _shopProduct.setValue {
                copy(state = PagedState.Loading)
            }

            val pagedDataList = repo.searchAceProducts(
                rows = LIMIT_PER_PAGE,
                start = shopProduct.nextCursor,
                query = shopProduct.query,
                shopId = shopProduct.shop.shopId,
                userId = "",
                sort = 9 /** TODO: gonna change this later */
            )

            _shopProduct.setValue {
                copy(
                    products = products + pagedDataList.dataList,
                    nextCursor = pagedDataList.nextCursor.toInt(),
                    state = PagedState.Success(
                        hasNextPage = pagedDataList.hasNextPage,
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
                query = query,
            )
        }
        handleLoadShopProduct()
    }

    companion object {
        private const val LIMIT_PER_PAGE = 20
        private const val LIMIT_LAST_PURCHASE_PRODUCT_PER_PAGE = 5
    }
}