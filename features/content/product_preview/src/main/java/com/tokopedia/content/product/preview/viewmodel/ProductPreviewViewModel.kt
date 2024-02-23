package com.tokopedia.content.product.preview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.product.preview.data.repository.ProductPreviewRepository
import com.tokopedia.content.product.preview.utils.ProductPreviewSharedPreference
import com.tokopedia.content.product.preview.view.uimodel.BottomNavUiModel
import com.tokopedia.content.product.preview.view.uimodel.MediaType
import com.tokopedia.content.product.preview.view.uimodel.finalPrice
import com.tokopedia.content.product.preview.view.uimodel.pager.ProductPreviewTabUiModel
import com.tokopedia.content.product.preview.view.uimodel.pager.ProductPreviewTabUiModel.Companion.TAB_PRODUCT_KEY
import com.tokopedia.content.product.preview.view.uimodel.pager.ProductPreviewTabUiModel.Companion.productTab
import com.tokopedia.content.product.preview.view.uimodel.pager.ProductPreviewTabUiModel.Companion.reviewTab
import com.tokopedia.content.product.preview.view.uimodel.product.ProductUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewContentUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewLikeUiState.ReviewLikeStatus
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewPaging
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewReportUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewUiModel
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.CheckInitialSource
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.ClickMenu
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.FetchMiniInfo
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.FetchReview
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.FetchReviewByIds
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.HasVisitCoachMark
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.InitializeProductMainData
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.InitializeReviewMainData
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.Like
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.LikeFromResult
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.Navigate
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.ProductAction
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.ProductActionFromResult
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.ProductMediaSelected
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.ProductMediaVideoEnded
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.ReviewContentScrolling
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.ReviewContentSelected
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.ReviewMediaSelected
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.SubmitReport
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.TabSelected
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.ToggleReviewWatchMode
import com.tokopedia.content.product.preview.viewmodel.event.ProductPreviewEvent
import com.tokopedia.content.product.preview.viewmodel.event.ProductPreviewEvent.UnknownSourceData
import com.tokopedia.content.product.preview.viewmodel.state.ProductReviewUiState
import com.tokopedia.content.product.preview.viewmodel.utils.ProductPreviewSourceModel
import com.tokopedia.content.product.preview.viewmodel.utils.ProductPreviewSourceModel.ProductSourceData
import com.tokopedia.content.product.preview.viewmodel.utils.ProductPreviewSourceModel.ReviewSourceData
import com.tokopedia.content.product.preview.viewmodel.utils.ProductPreviewSourceModel.UnknownSource
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
import java.util.*

class ProductPreviewViewModel @AssistedInject constructor(
    @Assisted val productPreviewSource: ProductPreviewSourceModel,
    private val repo: ProductPreviewRepository,
    private val userSessionInterface: UserSessionInterface,
    private val productPrevSharedPref: ProductPreviewSharedPreference
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(productPreviewSource: ProductPreviewSourceModel): ProductPreviewViewModel
    }

    private val reviewSourceId: String
        get() {
            return when (val source = productPreviewSource.source) {
                is ReviewSourceData -> {
                    source.reviewSourceId
                }

                else -> ""
            }
        }

    private val attachmentSourceId: String
        get() {
            return when (val source = productPreviewSource.source) {
                is ReviewSourceData -> {
                    source.attachmentSourceId
                }

                else -> ""
            }
        }

    private val _tabContentState = MutableStateFlow(ProductPreviewTabUiModel.Empty)
    private val _productMediaState = MutableStateFlow(ProductUiModel.Empty)
    private val _reviewContentState = MutableStateFlow(ReviewUiModel.Empty)
    private val _bottomNavContentState = MutableStateFlow(BottomNavUiModel.Empty)
    private val _reviewPosition = MutableStateFlow(0)

    private val _uiEvent = MutableSharedFlow<ProductPreviewEvent>(0)
    val uiEvent get() = _uiEvent

    val uiState: Flow<ProductReviewUiState>
        get() = combine(
            _tabContentState,
            _productMediaState,
            _reviewContentState,
            _bottomNavContentState
        ) { tabContentState, productMedia, reviewContent, bottomNavContent ->
            ProductReviewUiState(
                tabsUiModel = tabContentState,
                productUiModel = productMedia,
                reviewUiModel = reviewContent,
                bottomNavUiModel = bottomNavContent
            )
        }

    private val currentReview: ReviewContentUiModel
        get() {
            val isEmpty = _reviewContentState.value.reviewContent.isEmpty()
            val isInScope = _reviewPosition.value in 0 until _reviewContentState.value.reviewContent.size
            return if (isEmpty || !isInScope) {
                ReviewContentUiModel.Empty
            } else {
                _reviewContentState.value.reviewContent[_reviewPosition.value]
            }
        }
    private val currentTabPosition = MutableStateFlow(-1)
    private val currentProductMediaPosition: Int
        get() {
            val position = _productMediaState.value.productMedia.indexOfFirst { it.selected }
            return position.coerceAtLeast(0)
        }
    private val currentProductMediaSize: Int
        get() {
            return _productMediaState.value.productMedia.size
        }
    private val currentTabKey: String
        get() {
            val tabPosition = currentTabPosition.value.coerceAtLeast(0)
            val tabSize = _tabContentState.value.tabs.size
            return _tabContentState.value.tabs[tabPosition.coerceAtMost(tabSize)].key
        }

    private var autoScrollProductMedia: Timer? = null

    val hasVisit get() = productPrevSharedPref.hasVisited()

    fun onAction(action: ProductPreviewAction) {
        when (action) {
            CheckInitialSource -> handleCheckInitialSource()
            FetchMiniInfo -> handleFetchMiniInfo()
            InitializeProductMainData -> handleInitializeProductMainData()
            InitializeReviewMainData -> handleInitializeReviewMainData()
            ProductActionFromResult -> handleProductAction(_bottomNavContentState.value)
            LikeFromResult -> handleLikeFromResult(false)
            FetchReviewByIds -> handleFetchReviewByIds()
            ToggleReviewWatchMode -> handleReviewWatchMode()
            HasVisitCoachMark -> productPrevSharedPref.setHasVisit()
            ProductMediaVideoEnded -> handleProductMediaVideoEnded()
            is ProductMediaSelected -> handleProductMediaSelected(action.position)
            is ReviewContentSelected -> handleReviewContentSelected(action.position)
            is ReviewContentScrolling -> handleReviewContentScrolling(action.position, action.isScrolling)
            is ReviewMediaSelected -> handleReviewMediaSelected(action.position)
            is TabSelected -> handleTabSelected(action.position)
            is FetchReview -> handleFetchReview(action.isRefresh, action.page)
            is ProductAction -> addToChart(action.model)
            is Navigate -> handleNavigate(action.appLink)
            is SubmitReport -> handleSubmitReport(action.model)
            is ClickMenu -> handleClickMenu(action.isFromLogin)
            is Like -> handleLikeFromResult(action.isDoubleTap)
        }
    }

    private fun handleCheckInitialSource() {
        viewModelScope.launchCatchError(block = {
            when (val source = productPreviewSource.source) {
                is ProductSourceData -> updateTabProductSource(source)
                is ReviewSourceData -> updateTabReviewSource()
                else -> error("Unknown Source Data")
            }
        }) {
            _uiEvent.emit(UnknownSourceData)
        }
    }

    private fun updateTabProductSource(source: ProductSourceData) {
        val tabs = if (source.hasReviewMedia) {
            listOf(productTab, reviewTab)
        } else {
            listOf(productTab)
        }
        _tabContentState.update { ProductPreviewTabUiModel(tabs = tabs) }
    }

    private fun updateTabReviewSource() {
        val tabs = listOf(reviewTab)
        _tabContentState.update { ProductPreviewTabUiModel(tabs = tabs) }
    }

    private fun handleFetchMiniInfo() {
        viewModelScope.launchCatchError(block = {
            _bottomNavContentState.value = repo.getProductMiniInfo(productPreviewSource.productId)
        }) {
            _uiEvent.emit(ProductPreviewEvent.FailFetchMiniInfo(it))
        }
    }

    private fun handleInitializeProductMainData() {
        val source = productPreviewSource.source as? ProductSourceData ?: return
        _productMediaState.update { it.copy(productMedia = source.productSourceList) }
    }

    private fun handleInitializeReviewMainData() {
        viewModelScope.launchCatchError(block = {
            when (productPreviewSource.source) {
                is ProductSourceData -> handleFetchReview(isRefresh = true, page = 1)
                is ReviewSourceData -> handleFetchReviewByIds()
                UnknownSource -> _uiEvent.emit(UnknownSourceData)
            }
        }) { _uiEvent.emit(UnknownSourceData) }
    }

    private fun handleProductMediaVideoEnded() {
        val nextPosition = currentProductMediaPosition.plus(1)
        handleProductMediaSelected(nextPosition)
    }

    private fun handleProductMediaSelected(position: Int) {
        val currentPos = _productMediaState.value.productMedia.indexOfFirst { it.selected }
        if (currentPos < 0 || currentPos == position) return

        _productMediaState.update { productUiModel ->
            productUiModel.copy(
                productMedia = productUiModel.productMedia.mapIndexed { index, media ->
                    media.copy(selected = index == position)
                }
            )
        }

        emitTrackAllHorizontalScrollEvent()

        when (_productMediaState.value.productMedia[position].type) {
            MediaType.Image -> scheduleAutoScrollProductMedia()
            else -> autoScrollProductMedia?.cancel()
        }
    }

    private fun handleReviewContentSelected(position: Int) {
        if (_reviewPosition.value == position) return
        if (_reviewPosition.value < position) emitTrackReviewNextVerticalScrollEvent()

        _reviewPosition.value = position
        updateReviewToUnWatchMode()
    }

    private fun handleReviewContentScrolling(position: Int, isScrolling: Boolean) {
        updateReviewContentScrollingState(position, isScrolling)
        emitTrackAllHorizontalScrollEvent()
    }

    private fun handleReviewMediaSelected(mediaPosition: Int) {
        _reviewContentState.update { reviewUiModel ->
            reviewUiModel.copy(
                reviewContent = reviewUiModel.reviewContent.mapIndexed { indexContent, reviewContent ->
                    if (indexContent == _reviewPosition.value) {
                        reviewContent.copy(
                            medias = reviewContent.medias.mapIndexed { indexMedia, reviewMedia ->
                                reviewMedia.copy(selected = indexMedia == mediaPosition)
                            },
                            mediaSelectedPosition = mediaPosition
                        )
                    } else {
                        reviewContent
                    }
                }
            )
        }
    }

    private fun handleTabSelected(position: Int) {
        currentTabPosition.value = position
        if (currentTabKey == TAB_PRODUCT_KEY) {
            when (_productMediaState.value.productMedia[position].type) {
                MediaType.Image -> scheduleAutoScrollProductMedia()
                else -> autoScrollProductMedia?.cancel()
            }
        } else {
            autoScrollProductMedia?.cancel()
        }
    }

    private fun handleFetchReviewByIds() {
        viewModelScope.launchCatchError(block = {
            _reviewContentState.update { review -> review.copy(reviewPaging = ReviewPaging.Load) }

            val ids = listOf(reviewSourceId)
            val response = repo.getReviewByIds(ids = ids)
            _reviewContentState.update { review ->
                val reviewList = response.reviewContent.mapIndexed { index, reviewContent ->
                    if (index == 0 && reviewContent.reviewId == reviewSourceId) {
                        reviewContent.copy(
                            mediaSelectedPosition = getMediaSourcePosition(response.reviewContent)
                        )
                    } else {
                        reviewContent
                    }
                }
                review.copy(
                    reviewContent = reviewList,
                    reviewPaging = response.reviewPaging
                )
            }

            handleFetchReview(isRefresh = false, page = 1)
        }, onError = {
                _reviewContentState.update { review ->
                    review.copy(
                        reviewPaging = ReviewPaging.Error(throwable = it) {
                            handleFetchReviewByIds()
                        }
                    )
                }
            })
    }

    private fun handleFetchReview(isRefresh: Boolean, page: Int) {
        if (isRefresh) {
            _reviewContentState.update { review -> review.copy(reviewPaging = ReviewPaging.Load) }
        }
        viewModelScope.launchCatchError(block = {
            val response = repo.getReview(productPreviewSource.productId, page)
            val newList = buildList {
                if (_reviewContentState.value.reviewContent.isNotEmpty()) {
                    val newResponse = response.reviewContent.filterNot {
                        it.reviewId == reviewSourceId
                    }
                    addAll(_reviewContentState.value.reviewContent + newResponse)
                } else {
                    addAll(response.reviewContent)
                }
            }
            _reviewContentState.update { review ->
                review.copy(reviewContent = newList, reviewPaging = response.reviewPaging)
            }
        }) {
            _reviewContentState.update { review ->
                review.copy(
                    reviewPaging = ReviewPaging.Error(throwable = it) {
                        handleFetchReview(isRefresh, page)
                    }
                )
            }
        }
    }

    private fun scheduleAutoScrollProductMedia() {
        autoScrollProductMedia?.cancel()

        if (currentProductMediaSize == 1) return
        autoScrollProductMedia = Timer().apply {
            scheduleAtFixedRate(
                object : TimerTask() {
                    override fun run() {
                        var position = currentProductMediaPosition.plus(1)
                        if (position >= currentProductMediaSize) {
                            position = 0
                        }
                        handleProductMediaSelected(position)
                    }
                },
                DELAY_AUTO_SCROLL_PRODUCT_MEDIA,
                DELAY_AUTO_SCROLL_PRODUCT_MEDIA
            )
        }
    }

    private fun emitTrackAllHorizontalScrollEvent() {
        viewModelScope.launch {
            _uiEvent.emit(ProductPreviewEvent.TrackAllHorizontalScroll)
        }
    }

    private fun emitTrackReviewNextVerticalScrollEvent() {
        viewModelScope.launch {
            _uiEvent.emit(ProductPreviewEvent.TrackReviewNextVerticalScroll)
        }
    }

    private fun updateReviewContentScrollingState(position: Int, isScrolling: Boolean) {
        _reviewContentState.update { data ->
            data.copy(
                reviewContent = data.reviewContent.mapIndexed { index, reviewContentUiModel ->
                    if (index == position) {
                        reviewContentUiModel.copy(isScrolling = isScrolling)
                    } else {
                        reviewContentUiModel.copy(isScrolling = false)
                    }
                }
            )
        }
    }

    private fun updateReviewToUnWatchMode() {
        _reviewContentState.update { review ->
            review.copy(
                reviewContent = review.reviewContent.map { reviewContent ->
                    reviewContent.copy(isWatchMode = false)
                }
            )
        }
    }

    private fun getMediaSourcePosition(review: List<ReviewContentUiModel>): Int {
        val mediaPosition = review.first().medias.indexOfFirst { it.mediaId == attachmentSourceId }
        return if (mediaPosition < 0) 0 else mediaPosition
    }

    private fun addToChart(model: BottomNavUiModel) {
        requiredLogin(model) {
            viewModelScope.launchCatchError(
                block = {
                    val result = repo.addToCart(
                        productPreviewSource.productId,
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
                    ) { addToChart(model) }
                )
            }
        }
    }

    private fun remindMe(model: BottomNavUiModel) {
        requiredLogin(model) {
            viewModelScope.launchCatchError(
                block = {
                    val result = repo.remindMe(productPreviewSource.productId)

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

    private fun handleProductAction(model: BottomNavUiModel) {
        when (model.buttonState) {
            BottomNavUiModel.ButtonState.OOS -> remindMe(model)
            BottomNavUiModel.ButtonState.Active -> addToChart(model)
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

    private fun handleLikeFromResult(isDoubleTap: Boolean) {
        if (isDoubleTap && currentReview.likeState.state == ReviewLikeStatus.Like) return

        val status = currentReview.likeState
        requiredLogin(status) {
            viewModelScope.launchCatchError(block = {
                val state = repo.likeReview(status, currentReview.reviewId)
                _reviewContentState.update { reviews ->
                    reviews.copy(
                        reviewContent = reviews.reviewContent.map { review ->
                            if (review.reviewId == currentReview.reviewId) {
                                review.copy(
                                    likeState = state.copy(
                                        withAnimation = isDoubleTap
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

    private fun handleReviewWatchMode() {
        _reviewContentState.update { review ->
            review.copy(
                reviewContent = review.reviewContent.map { reviewContent ->
                    if (reviewContent.reviewId == currentReview.reviewId) {
                        reviewContent.copy(isWatchMode = !reviewContent.isWatchMode)
                    } else {
                        reviewContent.copy(isWatchMode = false)
                    }
                }
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        autoScrollProductMedia?.cancel()
    }

    companion object {
        private const val DELAY_AUTO_SCROLL_PRODUCT_MEDIA = 3000L
    }
}
