package com.tokopedia.createpost.producttag.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.createpost.producttag.domain.repository.ProductTagRepository
import com.tokopedia.createpost.producttag.util.extension.setValue
import com.tokopedia.createpost.producttag.view.uimodel.*
import com.tokopedia.createpost.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.createpost.producttag.view.uimodel.event.ProductTagUiEvent
import com.tokopedia.createpost.producttag.view.uimodel.state.LastPurchasedProductUiState
import com.tokopedia.createpost.producttag.view.uimodel.state.LastTaggedProductUiState
import com.tokopedia.createpost.producttag.view.uimodel.state.ProductTagSourceUiState
import com.tokopedia.createpost.producttag.view.uimodel.state.ProductTagUiState
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


/**
 * Created By : Jonathan Darwin on April 25, 2022
 */
class ProductTagViewModel @AssistedInject constructor(
    @Assisted("productTagSourceRaw") productTagSourceRaw: String,
    @Assisted("shopBadge") val shopBadge: String,
    @Assisted("authorId") private val authorId: String,
    @Assisted("authorType") private val authorType: String,
    private val repo: ProductTagRepository,
): ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("productTagSourceRaw") productTagSourceRaw: String,
            @Assisted("shopBadge") shopBadge: String,
            @Assisted("authorId") authorId: String,
            @Assisted("authorType") authorType: String,
        ): ProductTagViewModel
    }

    /** Public Getter */
    val productTagSourceList: List<ProductTagSource>
        get() = _productTagSourceList.value

    /** Flow */
    private val _productTagSourceList = MutableStateFlow<List<ProductTagSource>>(emptyList())
    private val _selectedProductTagSource = MutableStateFlow<ProductTagSource>(ProductTagSource.LastTagProduct)

    private val _lastTaggedProduct = MutableStateFlow(LastTaggedProductUiModel.Empty)
    private val _lastPurchasedProduct = MutableStateFlow(LastPurchasedProductUiModel.Empty)

    /** Ui State */
    private val _productTagSourceUiState = combine(
        _productTagSourceList, _selectedProductTagSource
    ) { productTagSourceList, selectedProductTagSource ->
        ProductTagSourceUiState(
            productTagSourceList = productTagSourceList,
            selectedProductTagSource = selectedProductTagSource,
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

    val uiState = combine(
        _productTagSourceUiState,
        _lastTaggedProductUiState,
        _lastPurchasedProductUiState,
    ) { productTagSource, lastTaggedProduct, lastPurchasedProduct ->
        ProductTagUiState(
            productTagSource = productTagSource,
            lastTaggedProduct = lastTaggedProduct,
            lastPurchasedProduct = lastPurchasedProduct,
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
            is ProductTagAction.SelectProductTagSource -> handleSelectProductTagSource(action.source)
            is ProductTagAction.ProductSelected -> handleProductSelected(action.product)

            /** Load Tagged Product */
            ProductTagAction.LoadLastTaggedProduct -> handleLoadLastTaggedProduct()

            /** Load Purchased Product */
            ProductTagAction.LoadLastPurchasedProduct -> handleLoadLastPurchasedProduct()
        }
    }

    /** Handle Action */
    private fun handleSelectProductTagSource(source: ProductTagSource) {
        _selectedProductTagSource.value = source
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

    companion object {
        private const val LIMIT_PER_PAGE = 20
        private const val LIMIT_LAST_PURCHASE_PRODUCT_PER_PAGE = 5
    }
}