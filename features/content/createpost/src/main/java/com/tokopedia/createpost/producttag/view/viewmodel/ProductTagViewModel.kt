package com.tokopedia.createpost.producttag.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.createpost.producttag.domain.repository.ProductTagRepository
import com.tokopedia.createpost.producttag.util.extension.setValue
import com.tokopedia.createpost.producttag.view.uimodel.*
import com.tokopedia.createpost.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.createpost.producttag.view.uimodel.state.LastTaggedProductUiState
import com.tokopedia.createpost.producttag.view.uimodel.state.ProductTagSourceUiState
import com.tokopedia.createpost.producttag.view.uimodel.state.ProductTagUiState
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map


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
            state = it.state,
        )
    }

    val uiState = combine(
        _productTagSourceUiState,
        _lastTaggedProductUiState,
    ) { productTagSource, lastTaggedProduct ->
        ProductTagUiState(
            productTagSource = productTagSource,
            lastTaggedProduct = lastTaggedProduct,
        )
    }

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

            /** Load Tagged Product */
            ProductTagAction.LoadLastTaggedProduct -> handleLoadLastTaggedProduct()
        }
    }

    /** Handle Action */
    private fun handleSelectProductTagSource(source: ProductTagSource) {
        _selectedProductTagSource.value = source
    }

    private fun handleLoadLastTaggedProduct() {
        viewModelScope.launchCatchError(block = {
            val currState = _lastTaggedProduct.value.state

            if(currState.isLoading || currState.isNextPage.not()) return@launchCatchError

            _lastTaggedProduct.setValue {
                copy(state = PagedState.Loading)
            }

            val pagedDataList = repo.getLastTaggedProducts(
                authorId = authorId,
                authorType = authorType,
                cursor = if(currState is PagedState.Success) currState.nextCursor else "",
                limit = 10, /** TODO: gonna change this later */
            )

            _lastTaggedProduct.setValue {
                copy(
                    products = products + pagedDataList.dataList,
                    state = PagedState.Success(
                        hasNextPage = pagedDataList.hasNextPage,
                        nextCursor = pagedDataList.nextCursor,
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
}