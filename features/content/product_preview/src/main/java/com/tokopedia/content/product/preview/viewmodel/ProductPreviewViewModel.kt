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
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.product.preview.data.repository.ProductPreviewRepository
import com.tokopedia.content.product.preview.view.uimodel.ProductPreviewAction
import com.tokopedia.content.product.preview.view.uimodel.ReviewUiModel
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
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
    @Assisted private val param: EntrySource,
    private val repo: ProductPreviewRepository,
    private val userSessionInterface: UserSessionInterface,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(param: EntrySource): ProductPreviewViewModel
    }

    //TODO: add uiState
    //TODO: add uiEvent

    private val _review = MutableStateFlow(emptyList<ReviewUiModel>())
    val review : Flow<List<ReviewUiModel>>
        get() = _review //TODO: add state

    fun onAction(action: ProductPreviewAction) {
        when(action) {
            ProductPreviewAction.FetchReview -> getReview()
            else -> {}
        }
    }

    private fun getReview() {
        viewModelScope.launchCatchError(block = {
            _review.value = repo.getReview(param.productId, 1) //TODO: add pagination
        }) {}
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
