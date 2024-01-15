package com.tokopedia.content.product.preview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.product.preview.data.repository.ProductPreviewRepository
import com.tokopedia.content.product.preview.view.uimodel.ContentUiModel
import com.tokopedia.content.product.preview.view.uimodel.ReviewUiModel
import com.tokopedia.content.product.preview.view.uimodel.product.ProductIndicatorUiModel
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewUiAction
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewUiAction.InitializeProductMainData
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewUiAction.InitializeReviewMainData
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewUiAction.ProductSelected
import com.tokopedia.content.product.preview.viewmodel.state.ProductPreviewUiState
import com.tokopedia.content.product.preview.viewmodel.utils.EntrySource
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update

class ProductPreviewViewModel @AssistedInject constructor(
    @Assisted private val param: EntrySource,
    private val repo: ProductPreviewRepository,
    private val userSessionInterface: UserSessionInterface
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(param: EntrySource): ProductPreviewViewModel
    }

    private val _productContentState = MutableStateFlow(emptyList<ContentUiModel>())
    private val _productIndicatorState = MutableStateFlow(emptyList<ProductIndicatorUiModel>())
    private val _reviewContentState = MutableStateFlow(emptyList<ReviewUiModel>())

    private val _review = MutableStateFlow(emptyList<ReviewUiModel>())
    val review: Flow<List<ReviewUiModel>>
        get() = _review // TODO: add state

    val productReviewUiState: Flow<ProductPreviewUiState>
        get() = combine(
            _productContentState,
            _productIndicatorState,
            _reviewContentState
        ) { productContent, productIndicator, reviewContentState ->
            ProductPreviewUiState(
                productContent = productContent,
                productIndicator = productIndicator,
                reviewContent = reviewContentState
            )
        }

    fun onAction(action: ProductPreviewUiAction) {
        when (action) {
            is InitializeProductMainData -> handleInitializeProductMainData()
            is InitializeReviewMainData -> handleInitializeReviewMainData(action.page)
            is ProductSelected -> handleProductSelected(action.position)
        }
    }

    private fun handleInitializeProductMainData() {
        _productContentState.value = param.productPreviewData.content
        _productIndicatorState.value = param.productPreviewData.indicator
    }

    private fun handleInitializeReviewMainData(page: Int) {
        viewModelScope.launchCatchError(block = {
            _reviewContentState.value =
                repo.getReview(param.productPreviewData.productId, page) // TODO: add pagination
        }) {}
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

    private fun getReview() {
        viewModelScope.launchCatchError(block = {
            _review.value =
                repo.getReview(param.productPreviewData.productId, 1) // TODO: add pagination
        }) {}
    }
}
