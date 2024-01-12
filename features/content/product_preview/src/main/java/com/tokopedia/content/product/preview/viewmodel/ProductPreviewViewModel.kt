package com.tokopedia.content.product.preview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.product.preview.data.repository.ProductPreviewRepository
import com.tokopedia.content.product.preview.view.uimodel.BottomNavUiModel
import com.tokopedia.content.product.preview.view.uimodel.ContentUiModel
import com.tokopedia.content.product.preview.view.uimodel.LikeUiState
import com.tokopedia.content.product.preview.view.uimodel.ProductPreviewAction
import com.tokopedia.content.product.preview.view.uimodel.ProductPreviewEvent
import com.tokopedia.content.product.preview.view.uimodel.ReportUiModel
import com.tokopedia.content.product.preview.view.uimodel.ReviewUiModel
import com.tokopedia.content.product.preview.view.uimodel.finalPrice
import com.tokopedia.content.product.preview.view.uimodel.product.ProductContentUiModel
import com.tokopedia.content.product.preview.view.uimodel.product.ProductIndicatorUiModel
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewUiAction
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewUiAction.InitializeProductMainData
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewUiAction.ProductSelected
import com.tokopedia.content.product.preview.viewmodel.state.ProductPreviewUiState
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

    //TODO: check number
    private val _uiEvent = MutableSharedFlow<ProductPreviewEvent>(20)
    val uiEvent get() = _uiEvent

    private val _review = MutableStateFlow(emptyList<ReviewUiModel>())
    val review: Flow<List<ReviewUiModel>>
        get() = _review //TODO: add state

    private val _miniInfo = MutableStateFlow(BottomNavUiModel.Empty)
    val miniInfo: Flow<BottomNavUiModel>
        get() = _miniInfo

    private val _reviewPosition = MutableStateFlow(0)
    private val reviewPosition get() = _reviewPosition.value

    private val currentReview
        get() = if (_review.value.isNotEmpty() && reviewPosition >= 0)
            _review.value[reviewPosition] else ReviewUiModel.Empty

    fun onAction(action: ProductPreviewAction) {
        when (action) {
            ProductPreviewAction.FetchReview -> getReview()
            ProductPreviewAction.FetchMiniInfo -> getMiniInfo()
            is ProductPreviewAction.ProductAction -> addToCart(action.model)
            ProductPreviewAction.AtcFromResult -> addToCart(_miniInfo.value)
            is ProductPreviewAction.Navigate -> navigate(action.appLink)
            is ProductPreviewAction.SubmitReport -> submitReport(action.model)
            is ProductPreviewAction.ClickMenu -> menuOnClicked(action.isFromLogin)
            is ProductPreviewAction.UpdateReviewPosition -> updateReviewIndex(action.index)
            is ProductPreviewAction.Like -> like(status = action.item)
            ProductPreviewAction.LikeFromResult -> like()
            else -> {}
        }
    }

    private fun getReview() {
        viewModelScope.launchCatchError(block = {
            _review.value = repo.getReview(param.productId, 1) //TODO: add pagination
        }) {}
    }

    private fun getMiniInfo() {
        viewModelScope.launchCatchError(block = {
            _miniInfo.value = repo.getProductMiniInfo(param.productId)
        }) {}
    }

    private fun addToCart(model: BottomNavUiModel) {
        requiredLogin(model) {
            viewModelScope.launchCatchError(block = {
                val result = repo.addToCart(
                    param.productId,
                    model.title,
                    model.shop.id,
                    model.price.finalPrice.toDoubleOrZero()
                )

                if (result) _uiEvent.emit(ProductPreviewEvent.ShowSuccessToaster(type = ProductPreviewEvent.ShowSuccessToaster.Type.ATC)) else throw MessageErrorException()
            }
            ) {
                _uiEvent.emit(ProductPreviewEvent.ShowErrorToaster(it) { addToCart(model) })
            }
        }
    }

    private fun navigate(appLink: String) {
        viewModelScope.launch {
            _uiEvent.emit(ProductPreviewEvent.NavigateEvent(appLink))
        }
    }

    private fun <T> requiredLogin(data: T, fn: () -> Unit) { //T for parsing data.
        if (userSessionInterface.isLoggedIn) {
            fn()
        } else {
            viewModelScope.launch {
                _uiEvent.emit(
                    ProductPreviewEvent.LoginEvent(data)
                )

            }
        }
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
            InitializeProductMainData -> handleInitializeProductMainData()
            is ProductSelected -> handleProductSelected(action.position)
        }
    }

    private fun handleInitializeProductMainData() {
        _productContentState.value = mockData().content
        _productIndicatorState.value = mockData().indicator
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
            _uiEvent.emit(ProductPreviewEvent.ShowErrorToaster(it) {
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
        _reviewPosition.value = position
    }

    private fun like(status: LikeUiState = currentReview.likeState) {
        if (status.withAnimation && !userSessionInterface.isLoggedIn) return

        requiredLogin(status) {
            viewModelScope.launchCatchError(block = {
                val state = repo.likeReview(status, currentReview.reviewId)
                _review.update { review ->
                    review.map { model ->
                        if (model.reviewId == currentReview.reviewId) model.copy(likeState = state.copy(withAnimation = status.withAnimation))
                        else model
                    }
                }
            }) {
                _uiEvent.emit(ProductPreviewEvent.ShowErrorToaster(it) {})
            }
        }
    }
}

/**
 * TODO: implement real data
 */
fun mockData() = ProductContentUiModel(
    productId = "productID_123",
    content = listOf(
        ContentUiModel(
            type = ContentUiModel.MediaType.Video,
            url = "https://vod-stream.tokopedia.net/view/adaptive.m3u8?id=f01396ff94ae71eeae0987c7371d0102"
        ),
        ContentUiModel(
            type = ContentUiModel.MediaType.Image,
            url = "https://images.tokopedia.net/img/cache/700/VqbcmM/2022/12/12/ca158fc4-699a-495e-aaac-229b4f8ed1aa.png"
        ),
        ContentUiModel(
            type = ContentUiModel.MediaType.Unknown,
            url = "https://images.tokopedia.net/img/cache/700/hDjmkQ/2023/2/4/6a3db555-a5e9-4bc1-9c17-1753ad105b92.jpg"
        ),
        ContentUiModel(
            type = ContentUiModel.MediaType.Image,
            url = "https://images.tokopedia.net/img/cache/700/VqbcmM/2021/9/14/9d770fbf-2bbd-4206-8511-56df29a6f4be.png"
        ),
        ContentUiModel(
            type = ContentUiModel.MediaType.Image,
            url = "https://images.tokopedia.net/img/cache/700/VqbcmM/2022/8/25/1f559a48-03f3-4656-b77f-3caf0fcc94d2.png"
        ),
        ContentUiModel(
            type = ContentUiModel.MediaType.Unknown,
            url = "https://vod-stream.tokopedia.net/view/adaptive.m3u8?id=f01396ff94ae71eeae0987c7371d0102"
        ),
        ContentUiModel(
            type = ContentUiModel.MediaType.Image,
            url = "https://images.tokopedia.net/img/cache/700/VqbcmM/2022/12/12/ca158fc4-699a-495e-aaac-229b4f8ed1aa.png"
        ),
        ContentUiModel(
            type = ContentUiModel.MediaType.Image,
            url = "https://images.tokopedia.net/img/cache/700/hDjmkQ/2023/2/4/6a3db555-a5e9-4bc1-9c17-1753ad105b92.jpg"
        ),
        ContentUiModel(
            type = ContentUiModel.MediaType.Unknown,
            url = "https://images.tokopedia.net/img/cache/700/VqbcmM/2021/9/14/9d770fbf-2bbd-4206-8511-56df29a6f4be.png"
        ),
        ContentUiModel(
            type = ContentUiModel.MediaType.Image,
            url = "https://images.tokopedia.net/img/cache/700/VqbcmM/2022/8/25/1f559a48-03f3-4656-b77f-3caf0fcc94d2.png"
        )
    ),
    indicator = listOf(
        ProductIndicatorUiModel(
            indicatorId = "1",
            selected = true,
            variantName = "Variant 1",
            content = ContentUiModel(
                type = ContentUiModel.MediaType.Video,
                url = "https://images.tokopedia.net/img/cache/700/VqbcmM/2022/12/12/ca158fc4-699a-495e-aaac-229b4f8ed1aa.png"
            )
        ),
        ProductIndicatorUiModel(
            indicatorId = "2",
            selected = false,
            variantName = "Variant 2",
            content = ContentUiModel(
                type = ContentUiModel.MediaType.Image,
                url = "https://images.tokopedia.net/img/cache/700/VqbcmM/2022/12/12/ca158fc4-699a-495e-aaac-229b4f8ed1aa.png"
            )
        ),
        ProductIndicatorUiModel(
            indicatorId = "3",
            selected = false,
            variantName = "Variant 3",
            content = ContentUiModel(
                type = ContentUiModel.MediaType.Image,
                url = "https://images.tokopedia.net/img/cache/700/hDjmkQ/2023/2/4/6a3db555-a5e9-4bc1-9c17-1753ad105b92.jpg"
            )
        ),
        ProductIndicatorUiModel(
            indicatorId = "4",
            selected = false,
            variantName = "Variant 4",
            content = ContentUiModel(
                type = ContentUiModel.MediaType.Image,
                url = "https://images.tokopedia.net/img/cache/700/VqbcmM/2021/9/14/9d770fbf-2bbd-4206-8511-56df29a6f4be.png"
            )
        ),
        ProductIndicatorUiModel(
            indicatorId = "5",
            selected = false,
            variantName = "Variant 5",
            content = ContentUiModel(
                type = ContentUiModel.MediaType.Image,
                url = "https://images.tokopedia.net/img/cache/700/VqbcmM/2022/8/25/1f559a48-03f3-4656-b77f-3caf0fcc94d2.png"
            )
        ),
        ProductIndicatorUiModel(
            indicatorId = "6",
            selected = false,
            variantName = "Variant 6",
            content = ContentUiModel(
                type = ContentUiModel.MediaType.Image,
                url = "https://images.tokopedia.net/img/cache/700/hDjmkQ/2023/2/4/6a3db555-a5e9-4bc1-9c17-1753ad105b92.jpg"
            )
        ),
        ProductIndicatorUiModel(
            indicatorId = "7",
            selected = false,
            variantName = "Variant 7",
            content = ContentUiModel(
                type = ContentUiModel.MediaType.Image,
                url = "https://images.tokopedia.net/img/cache/700/VqbcmM/2021/9/14/9d770fbf-2bbd-4206-8511-56df29a6f4be.png"
            )
        ),
        ProductIndicatorUiModel(
            indicatorId = "8",
            selected = false,
            variantName = "Variant 8",
            content = ContentUiModel(
                type = ContentUiModel.MediaType.Image,
                url = "https://images.tokopedia.net/img/cache/700/VqbcmM/2022/8/25/1f559a48-03f3-4656-b77f-3caf0fcc94d2.png"
            )
        ),
        ProductIndicatorUiModel(
            indicatorId = "9",
            selected = false,
            variantName = "Variant 9",
            content = ContentUiModel(
                type = ContentUiModel.MediaType.Video,
                url = "https://images.tokopedia.net/img/cache/700/VqbcmM/2022/12/12/ca158fc4-699a-495e-aaac-229b4f8ed1aa.png"
            )
        ),
        ProductIndicatorUiModel(
            indicatorId = "10",
            selected = false,
            variantName = "Variant 10",
            content = ContentUiModel(
                type = ContentUiModel.MediaType.Image,
                url = "https://images.tokopedia.net/img/cache/700/VqbcmM/2022/12/12/ca158fc4-699a-495e-aaac-229b4f8ed1aa.png"
            )
        )
    )
)
