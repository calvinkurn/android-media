package com.tokopedia.content.product.preview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.product.preview.data.repository.ProductPreviewRepository
import com.tokopedia.content.product.preview.view.uimodel.BottomNavUiModel
import com.tokopedia.content.product.preview.view.uimodel.ContentUiModel
import com.tokopedia.content.product.preview.view.uimodel.ProductPreviewAction
import com.tokopedia.content.product.preview.view.uimodel.ProductPreviewAction.*
import com.tokopedia.content.product.preview.view.uimodel.ProductPreviewEvent
import com.tokopedia.content.product.preview.view.uimodel.ReportUiModel
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
import kotlinx.coroutines.flow.updateAndGet
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

    private val _reviewIndex = MutableStateFlow(0)

    private val reviewPosition get() = _reviewIndex.value

    private val currentReview
        get() = if (_review.value.isNotEmpty() && reviewPosition in 0 until _review.value.size)
            _review.value[reviewPosition] else ReviewUiModel.Empty

    fun onAction(action: ProductPreviewAction) {
        when (action) {
            InitializeProductMainData -> handleInitializeProductMainData()
            is ProductSelected -> handleProductSelected(action.position)
            FetchReview -> getReview()
            FetchMiniInfo -> getMiniInfo()
            is ProductAction -> handleProductAction(action.model)
            ProductActionFromResult -> handleProductAction(_miniInfo.value)
            is Navigate -> navigate(action.appLink)
            is SubmitReport -> submitReport(action.model)
            is ClickMenu -> menuOnClicked(action.isFromLogin)
            is UpdateReviewPosition -> updateReviewIndex(action.index)
            else -> {}
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
                _uiEvent.emit(ProductPreviewEvent.ShowErrorToaster(it, ProductPreviewEvent.ShowErrorToaster.Type.ATC) { addToCart(model) })
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

    private fun submitReport(model: ReportUiModel) {
        viewModelScope.launchCatchError(block = {
            val result = repo.submitReport(model, currentReview.reviewId)
            if (result) _uiEvent.emit(ProductPreviewEvent.ShowSuccessToaster(type = ProductPreviewEvent.ShowSuccessToaster.Type.Report)) else throw MessageErrorException()
        }) {
            _uiEvent.emit(ProductPreviewEvent.ShowErrorToaster(it, ProductPreviewEvent.ShowErrorToaster.Type.ATC) {
                submitReport(model)
            })
        }
    }

    private fun menuOnClicked(isFromLogin: Boolean) {
        val status = _review.updateAndGet { review ->
            if (isFromLogin.not()) review
            else review.map { model ->
                model.copy(menus = model.menus.copy(isReportable = userSessionInterface.isLoggedIn && model.author.id != userSessionInterface.userId))
            }
        }.getOrNull(reviewPosition)?.menus ?: return

        requiredLogin(status) {
            viewModelScope.launch {
                _uiEvent.emit(ProductPreviewEvent.ShowMenuSheet(status))
            }
        }
    }

    private fun updateReviewIndex(position: Int) {
        _reviewIndex.value = position
    }
}
