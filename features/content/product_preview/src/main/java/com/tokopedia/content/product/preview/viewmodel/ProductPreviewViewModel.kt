package com.tokopedia.content.product.preview.viewmodel

import androidx.lifecycle.ViewModel
import com.tokopedia.content.product.preview.view.uimodel.ContentUiModel
import com.tokopedia.content.product.preview.view.uimodel.product.ProductContentUiModel
import com.tokopedia.content.product.preview.view.uimodel.product.ProductIndicatorUiModel
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewUiAction
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewUiAction.InitializeProductMainData
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewUiAction.ProductSelected
import com.tokopedia.content.product.preview.viewmodel.state.ProductPreviewUiState
import com.tokopedia.content.product.preview.viewmodel.utils.EntrySource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update

/**
 * @author by astidhiyaa on 06/12/23
 */
class ProductPreviewViewModel @AssistedInject constructor(
    @Assisted private val param: EntrySource
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(param: EntrySource): ProductPreviewViewModel
    }

    private val _productContentState = MutableStateFlow(emptyList<ContentUiModel>())
    private val _productIndicatorState = MutableStateFlow(emptyList<ProductIndicatorUiModel>())

    val productUiState: Flow<ProductPreviewUiState>
        get() = combine(
            _productContentState,
            _productIndicatorState
        ) { productContent, productIndicator ->
            ProductPreviewUiState(
                productContent = productContent,
                productIndicator = productIndicator
            )
        }

    fun submitAction(action: ProductPreviewUiAction) {
        when (action) {
            is InitializeProductMainData -> handleInitializeProductMainData(action.data)
            is ProductSelected -> handleProductSelected(action.position)
        }
    }

    private fun handleInitializeProductMainData(data: ProductContentUiModel) {
        _productContentState.value = data.content
        _productIndicatorState.value = data.indicator
    }

    private fun handleProductSelected(position: Int) {
        _productContentState.update {
            it.mapIndexed { index, contentUiModel ->
                contentUiModel.copy(selected = index == position)
            }
        }
        _productIndicatorState.update {
            it.mapIndexed { index, productIndicatorUiModel ->
                productIndicatorUiModel.copy(selected = index == position)
            }
        }
    }
}
