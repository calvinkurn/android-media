package com.tokopedia.content.product.preview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.product.preview.data.repository.ProductPreviewRepository
import com.tokopedia.content.product.preview.view.uimodel.BottomNavUiModel
import com.tokopedia.content.product.preview.view.uimodel.ContentUiModel
import com.tokopedia.content.product.preview.view.uimodel.ProductPreviewAction
import com.tokopedia.content.product.preview.view.uimodel.ProductPreviewAction.FetchMiniInfo
import com.tokopedia.content.product.preview.view.uimodel.ProductPreviewAction.FetchReview
import com.tokopedia.content.product.preview.view.uimodel.ProductPreviewAction.InitializeProductMainData
import com.tokopedia.content.product.preview.view.uimodel.ProductPreviewAction.Navigate
import com.tokopedia.content.product.preview.view.uimodel.ProductPreviewAction.ProductAction
import com.tokopedia.content.product.preview.view.uimodel.ProductPreviewAction.ProductActionFromResult
import com.tokopedia.content.product.preview.view.uimodel.ProductPreviewAction.ProductSelected
import com.tokopedia.content.product.preview.view.uimodel.ProductPreviewEvent
import com.tokopedia.content.product.preview.view.uimodel.ReviewUiModel
import com.tokopedia.content.product.preview.view.uimodel.finalPrice
import com.tokopedia.content.product.preview.view.uimodel.product.ProductIndicatorUiModel
import com.tokopedia.content.product.preview.viewmodel.state.ProductUiState
import com.tokopedia.content.product.preview.viewmodel.utils.EntrySource
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

    // TODO: check number
    private val _uiEvent = MutableSharedFlow<ProductPreviewEvent>(20)
    val uiEvent get() = _uiEvent

    private var _productVideoLastDuration: Long = 0L
    val productVideoLastDuration: Long
        get() = _productVideoLastDuration

    private val _review = MutableStateFlow(emptyList<ReviewUiModel>())
    val review: Flow<List<ReviewUiModel>>
        get() = _review // TODO: add state

    val productUiState: Flow<ProductUiState>
        get() = combine(
            _productContentState,
            _productIndicatorState
        ) { productContent, productIndicator ->
            ProductUiState(
                productContent = productContent,
                productIndicator = productIndicator
            )
        }

    private val _miniInfo = MutableStateFlow(BottomNavUiModel.Empty)
    val miniInfo: Flow<BottomNavUiModel>
        get() = _miniInfo

    fun onAction(action: ProductPreviewAction) {
        when (action) {
            InitializeProductMainData -> handleInitializeProductMainData()
            FetchReview -> getReview()
            FetchMiniInfo -> getMiniInfo()
            ProductActionFromResult -> handleProductAction(_miniInfo.value)
            is ProductAction -> handleProductAction(action.model)
            is Navigate -> navigate(action.appLink)
            is ProductSelected -> handleProductSelected(action.position)
            is SetProductVideoLastDuration -> handleSetProductVideoLastDuration(action.duration)
        }
    }

    private fun getReview() {
        viewModelScope.launchCatchError(block = {
            _review.value =
                repo.getReview(param.productPreviewData.productId, 1) // TODO: add pagination
        }) {}
    }

    private fun getMiniInfo() {
        viewModelScope.launchCatchError(block = {
            _miniInfo.value = repo.getProductMiniInfo(param.productPreviewData.productId)
        }) {}
    }

    private fun addToCart(model: BottomNavUiModel) {
        requiredLogin(model) {
            viewModelScope.launchCatchError(
                block = {
                    val result = repo.addToCart(
                        param.productPreviewData.productId,
                        model.title,
                        model.shop.id,
                        model.price.finalPrice.toDoubleOrZero()
                    )

                    if (result) {
                        _uiEvent.emit(
                            ProductPreviewEvent.ShowSuccessToaster(
                                type = ProductPreviewEvent.ShowSuccessToaster.Type.ATC,
                                message = ProductPreviewEvent.ShowSuccessToaster.Type.ATC.textRes
                            )
                        )
                    } else {
                        throw MessageErrorException()
                    }
                }
            ) {
                _uiEvent.emit(ProductPreviewEvent.ShowErrorToaster(it) { addToCart(model) })
            }
        }
    }

    private fun remindMe(model: BottomNavUiModel) {
        requiredLogin(model) {
            viewModelScope.launchCatchError(
                block = {
                    val result = repo.remindMe(param.productPreviewData.productId)

                    if (result.isSuccess) {
                        _uiEvent.emit(
                            ProductPreviewEvent.ShowSuccessToaster(
                                type = ProductPreviewEvent.ShowSuccessToaster.Type.Remind,
                                message = ProductPreviewEvent.ShowSuccessToaster.Type.Remind.textRes
                            )
                        )
                    } else {
                        throw MessageErrorException()
                    }
                }
            ) {
                _uiEvent.emit(ProductPreviewEvent.ShowErrorToaster(it) { remindMe(model) })
            }
        }
    }

    private fun handleProductAction(model: BottomNavUiModel) {
        when (model.buttonState) {
            BottomNavUiModel.ButtonState.OOS -> remindMe(model)
            BottomNavUiModel.ButtonState.Active -> addToCart(model)
            else -> {}
        }
    }

    private fun navigate(appLink: String) {
        viewModelScope.launch {
            _uiEvent.emit(ProductPreviewEvent.NavigateEvent(appLink))
        }
    }

    private fun <T> requiredLogin(data: T, fn: () -> Unit) { // T for parsing data.
        if (userSessionInterface.isLoggedIn) {
            fn()
        } else {
            viewModelScope.launch {
                _uiEvent.emit(ProductPreviewEvent.LoginEvent(data))
            }
        }
    }

    private fun handleInitializeProductMainData() {
        _productContentState.value = param.productPreviewData.content
        _productIndicatorState.value = param.productPreviewData.indicator
    }

    private fun handleInitializeReviewMainData(page: Int) {
        viewModelScope.launchCatchError(block = {
            _review.value =
                repo.getReview(param.productPreviewData.productId, page) // TODO: add pagination
        }) {}
    }

    private fun handleSetProductVideoLastDuration(duration: Long) {
        _productVideoLastDuration = duration
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
