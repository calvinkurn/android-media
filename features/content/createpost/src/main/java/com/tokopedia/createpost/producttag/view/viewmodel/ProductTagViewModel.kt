package com.tokopedia.createpost.producttag.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.createpost.producttag.view.uimodel.ProductTagSource
import com.tokopedia.createpost.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.createpost.producttag.view.uimodel.state.ProductTagSourceUiState
import com.tokopedia.createpost.producttag.view.uimodel.state.ProductTagUiState
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine


/**
 * Created By : Jonathan Darwin on April 25, 2022
 */
class ProductTagViewModel @AssistedInject constructor(
    @Assisted("productTagSourceRaw") productTagSourceRaw: String,
    @Assisted("shopBadge") val shopBadge: String,
): ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("productTagSourceRaw") productTagSourceRaw: String,
            @Assisted("shopBadge") shopBadge: String,
        ): ProductTagViewModel
    }

    /** Public Getter */
    val productTagSourceList: List<ProductTagSource>
        get() = _productTagSourceList.value

    /** Flow */
    private val _productTagSourceList = MutableStateFlow<List<ProductTagSource>>(emptyList())
    private val _selectedProductTagSource = MutableStateFlow<ProductTagSource>(ProductTagSource.LastTagProduct)

    /** Ui State */
    private val _productTagSourceUiState = combine(
        _productTagSourceList, _selectedProductTagSource
    ) { productTagSourceList, selectedProductTagSource ->
        ProductTagSourceUiState(
            productTagSourceList = productTagSourceList,
            selectedProductTagSource = selectedProductTagSource,
        )
    }

    val uiState = combine(
        _productTagSourceUiState,
        _productTagSourceUiState,
    ) { productTagSource, _ ->
        ProductTagUiState(
            productTagSource = productTagSource,
        )
    }

    init {
        processProductTagSource(productTagSourceRaw)
    }

    fun submitAction(action: ProductTagAction) {
        when(action) {
            is ProductTagAction.SelectProductTagSource -> handleSelectProductTagSource(action.source)
        }
    }

    private fun processProductTagSource(productTagSourceRaw: String) {
        viewModelScope.launchCatchError(block = {
            val split = productTagSourceRaw.split(",")
            _productTagSourceList.value = split.map {
                ProductTagSource.mapFromString(it)
            }
        }) { }
    }

    /** Handle Action */
    private fun handleSelectProductTagSource(source: ProductTagSource) {
        _selectedProductTagSource.value = source
    }
}