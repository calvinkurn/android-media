package com.tokopedia.content.product.preview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.product.preview.data.repository.ProductPreviewRepository
import com.tokopedia.content.product.preview.view.uimodel.BottomNavUiModel
import com.tokopedia.content.product.preview.view.uimodel.finalPrice
import com.tokopedia.content.product.preview.view.uimodel.product.ProductUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewContentUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewLikeUiState
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewPaging
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewReportUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewUiModel
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.AddToChart
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.ClickMenu
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.FetchMiniInfo
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.FetchReview
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.InitializeProductMainData
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.Like
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.LikeFromResult
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.Navigate
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.ProductActionFromResult
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.ProductSelected
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.SubmitReport
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.UpdateReviewPosition
import com.tokopedia.content.product.preview.viewmodel.event.ProductPreviewEvent
import com.tokopedia.content.product.preview.viewmodel.state.ProductReviewUiState
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

    val productData: ProductUiModel
        get() = param.productUiModel

    private val _productContentState = MutableStateFlow(ProductUiModel.Empty)
    private val _reviewContentState = MutableStateFlow(ReviewUiModel.Empty)
    private val _bottomNavContentState = MutableStateFlow(BottomNavUiModel.Empty)
    private val _reviewPosition = MutableStateFlow(0)

    private val _uiEvent = MutableSharedFlow<ProductPreviewEvent>(20)
    val uiEvent get() = _uiEvent

    val uiState: Flow<ProductReviewUiState>
        get() = combine(
            _productContentState,
            _bottomNavContentState,
            _reviewContentState
        ) { productContent, bottomNavContent, reviewContent ->
            ProductReviewUiState(
                productUiModel = productContent,
                reviewUiModel = reviewContent,
                bottomNavUiModel = bottomNavContent
            )
        }

    private val currentReview: ReviewContentUiModel
        get() {
            return if (_reviewContentState.value.reviewContent.isEmpty() ||
                _reviewPosition.value in 0 until _reviewContentState.value.reviewContent.size
            ) {
                ReviewContentUiModel.Empty
            } else {
                _reviewContentState.value.reviewContent[_reviewPosition.value]
            }
        }

    fun onAction(action: ProductPreviewAction) {
        when (action) {
            InitializeProductMainData -> handleInitializeProductMainData()
            FetchMiniInfo -> handleFetchMiniInfo()
            ProductActionFromResult -> handleProductAction()
            LikeFromResult -> handleLikeFromResult()
            is ProductSelected -> handleProductSelected(action.position)
            is FetchReview -> handleFetchReview(action.isRefresh)
            is AddToChart -> handleAddToCart(action.model)
            is Navigate -> handleNavigate(action.appLink)
            is SubmitReport -> handleSubmitReport(action.model)
            is ClickMenu -> handleClickMenu(action.isFromLogin)
            is UpdateReviewPosition -> handleUpdateReviewPosition(action.index)
            is Like -> handleLikeFromResult(action.item)
            is ProductPreviewAction.ReviewMediaSelected -> handleReviewMediaSelected(action.position)
        }
    }

    private fun handleInitializeProductMainData() {
        _productContentState.value = _productContentState.value.copy(
            content = param.productUiModel.content
        )
    }

    private fun handleFetchMiniInfo() {
        viewModelScope.launchCatchError(block = {
            _bottomNavContentState.value = repo.getProductMiniInfo(param.productUiModel.productId)
        }) {
            // TODO: what happen when fail fetching bottom nav info?
        }
    }

    private fun handleProductSelected(position: Int) {
        _productContentState.update { productUiModel ->
            productUiModel.copy(
                content = productUiModel.content.mapIndexed { index, content ->
                    content.copy(selected = index == position)
                }
            )
        }
    }

    private fun handleFetchReview(isRefresh: Boolean) {
        val state = _reviewContentState.value.reviewPaging
        val page = when {
            isRefresh -> 1
            state is ReviewPaging.Success && state.hasNextPage -> state.page + 1
            else -> return
        }
        if (isRefresh) {
            _reviewContentState.update { review -> review.copy(reviewPaging = ReviewPaging.Load) }
        }
        viewModelScope.launchCatchError(block = {
            val response = repo.getReview(param.productUiModel.productId, page)
            val newList = buildList {
                if (_reviewContentState.value.reviewContent.isNotEmpty()) {
                    addAll(_reviewContentState.value.reviewContent + response.reviewContent)
                } else {
                    addAll(response.reviewContent)
                }
            }
            _reviewContentState.update { review ->
                review.copy(reviewContent = newList, reviewPaging = response.reviewPaging)
            }
        }) {
            _reviewContentState.update { review ->
                review.copy(reviewPaging = ReviewPaging.Error(it))
            }
        }
    }

    private fun handleAddToCart(model: BottomNavUiModel) {
        requiredLogin(model) {
            viewModelScope.launchCatchError(
                block = {
                    val result = repo.addToCart(
                        param.productUiModel.productId,
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
                _uiEvent.emit(
                    ProductPreviewEvent.ShowErrorToaster(
                        it,
                        ProductPreviewEvent.ShowErrorToaster.Type.ATC
                    ) { handleAddToCart(model) }
                )
            }
        }
    }

    private fun remindMe(model: BottomNavUiModel) {
        requiredLogin(model) {
            viewModelScope.launchCatchError(
                block = {
                    val result = repo.remindMe(param.productUiModel.productId)

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
                _uiEvent.emit(
                    ProductPreviewEvent.ShowErrorToaster(it) { remindMe(model) }
                )
            }
        }
    }

    private fun handleProductAction() {
        val model = _bottomNavContentState.value
        when (model.buttonState) {
            BottomNavUiModel.ButtonState.OOS -> remindMe(model)
            BottomNavUiModel.ButtonState.Active -> handleAddToCart(model)
            else -> {}
        }
    }

    private fun handleNavigate(appLink: String) {
        viewModelScope.launch {
            _uiEvent.emit(ProductPreviewEvent.NavigateEvent(appLink))
        }
    }

    private fun <T> requiredLogin(data: T, fn: () -> Unit) {
        if (userSessionInterface.isLoggedIn) {
            fn()
        } else {
            viewModelScope.launch {
                _uiEvent.emit(ProductPreviewEvent.LoginEvent(data))
            }
        }
    }

    private fun handleSubmitReport(model: ReviewReportUiModel) {
        viewModelScope.launchCatchError(block = {
            val result = repo.submitReport(model, currentReview.reviewId)
            if (result) {
                _uiEvent.emit(
                    ProductPreviewEvent.ShowSuccessToaster(
                        type = ProductPreviewEvent.ShowSuccessToaster.Type.Report
                    )
                )
            } else {
                throw MessageErrorException()
            }
        }) {
            _uiEvent.emit(
                ProductPreviewEvent.ShowErrorToaster(
                    it,
                    ProductPreviewEvent.ShowErrorToaster.Type.Report
                ) {
                    handleSubmitReport(model)
                }
            )
        }
    }

    private fun handleClickMenu(isFromLogin: Boolean) {
        val status = _reviewContentState.updateAndGet { review ->
            review.copy(
                reviewContent = if (isFromLogin.not()) {
                    review.reviewContent
                } else {
                    review.reviewContent.map { model ->
                        model.copy(
                            menus = model.menus.copy(
                                isReportable = userSessionInterface.isLoggedIn &&
                                    model.author.id != userSessionInterface.userId
                            )
                        )
                    }
                }
            )
        }.reviewContent.getOrNull(_reviewPosition.value)?.menus ?: return

        requiredLogin(status) {
            viewModelScope.launch {
                _uiEvent.emit(ProductPreviewEvent.ShowMenuSheet(status))
            }
        }
    }

    private fun handleUpdateReviewPosition(position: Int) {
        _reviewPosition.value = position
    }

    private fun handleLikeFromResult(status: ReviewLikeUiState = currentReview.likeState) {
        if (status.withAnimation && !userSessionInterface.isLoggedIn) return

        requiredLogin(status) {
            viewModelScope.launchCatchError(block = {
                val state = repo.likeReview(status, currentReview.reviewId)
                _reviewContentState.update { reviews ->
                    reviews.copy(
                        reviewContent =
                        reviews.reviewContent.map { review ->
                            if (review.reviewId == currentReview.reviewId) {
                                review.copy(
                                    likeState =
                                    state.copy(
                                        withAnimation = status.withAnimation
                                    )
                                )
                            } else {
                                review
                            }
                        }
                    )
                }
            }) {
                _uiEvent.emit(ProductPreviewEvent.ShowErrorToaster(it) {})
            }
        }
    }

    private fun handleReviewMediaSelected(position: Int) {
        _reviewContentState.update {
            it.copy(
                reviewContent = it.reviewContent.mapIndexed { indexContent, reviewContent ->
                    if (indexContent == _reviewPosition.value) {
                        reviewContent.copy(
                            medias = reviewContent.medias.mapIndexed { indexMedia, reviewMedia ->
                                reviewMedia.copy(
                                    selected = indexMedia == position
                                )
                            }
                        )
                    } else {
                        reviewContent
                    }
                }
            )
        }
    }
}
