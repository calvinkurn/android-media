package com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.viewmodel

import android.os.Bundle
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cachemanager.CacheManager
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.reviewcommon.R
import com.tokopedia.reviewcommon.extension.getSavedState
import com.tokopedia.reviewcommon.feature.media.detail.presentation.uimodel.ReviewDetailUiModel
import com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.uimodel.LoadingStateItemUiModel
import com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.uimodel.MediaItemUiModel
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductrevGetReviewMedia
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.usecase.GetDetailedReviewMediaUseCase
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.usecase.ToggleLikeReviewUseCase
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.activity.DetailedReviewMediaGalleryActivity.Companion.TOASTER_KEY_ERROR_GET_REVIEW_MEDIA
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.uimodel.DetailedReviewActionMenuUiModel
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.uimodel.ToasterUiModel
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.uistate.ActionMenuBottomSheetUiState
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.uistate.OrientationUiState
import com.tokopedia.reviewcommon.uimodel.StringRes
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ceil

class SharedReviewMediaGalleryViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers,
    private val getDetailedReviewMediaUseCase: GetDetailedReviewMediaUseCase,
    private val toggleLikeReviewUseCase: ToggleLikeReviewUseCase
) : BaseViewModel(dispatchers.io) {

    companion object {
        private const val FLOW_TIMEOUT_MILLIS = 5000L

        const val SAVED_STATE_PRODUCT_ID = "savedStateProductId"
        const val SAVED_STATE_SHOW_SEE_MORE = "savedStateShowSeeMore"
        const val SAVED_STATE_GET_DETAILED_REVIEW_MEDIA_RESULT = "savedStateGetDetailedReviewMediaResult"
        const val SAVED_STATE_ORIENTATION_UI_STATE = "savedStateOrientationUiState"
        const val SAVED_STATE_OVERLAY_VISIBILITY = "savedStateOverlayVisibility"
        const val SAVED_STATE_SHOW_ACTION_MENU_BOTTOM_SHEET = "savedStateShowActionMenuBottomSheet"

        const val EXTRAS_PRODUCT_ID = "extrasProductId"
        const val EXTRAS_SHOP_ID = "extrasShopId"
        const val EXTRAS_IS_PRODUCT_REVIEW = "extrasIsProductReview"
        const val EXTRAS_IS_FROM_GALLERY = "extrasIsFromGallery"
        const val EXTRAS_TARGET_MEDIA_NUMBER = "extrasTargetMediaNumber"
        const val EXTRAS_SHOW_SEE_MORE = "extrasShowSeeMore"
        const val EXTRAS_PRELOADED_DETAILED_REVIEW_MEDIA_RESULT = "extrasPreloadedReviewMediaResult"

        const val UNINITIALIZED_PRODUCT_ID_VALUE = ""
        const val UNINITIALIZED_SHOP_ID_VALUE = ""
        const val UNINITIALIZED_MEDIA_NUMBER_TO_LOAD_VALUE = -1
        const val INITIAL_MEDIA_NUMBER_TO_LOAD_VALUE = 1
    }

    private val _productID = MutableStateFlow(UNINITIALIZED_PRODUCT_ID_VALUE)
    private val _shopID = MutableStateFlow(UNINITIALIZED_SHOP_ID_VALUE)
    private val _isProductReview = MutableStateFlow(false)
    private val _isFromGallery = MutableStateFlow(false)
    private val _showDetailedReviewActionMenuBottomSheet = MutableStateFlow(false)
    private val _toasterQueue = MutableSharedFlow<ToasterUiModel>()
    val toasterQueue: Flow<ToasterUiModel>
        get() = _toasterQueue
    private val _toasterEventActionClickQueue = MutableSharedFlow<String>()
    val toasterEventActionClickQueue: Flow<String>
        get() = _toasterEventActionClickQueue
    private val _mediaNumberToLoad = MutableStateFlow(UNINITIALIZED_MEDIA_NUMBER_TO_LOAD_VALUE)
    val mediaNumberToLoad: StateFlow<Int>
        get() = _mediaNumberToLoad
    private val _showSeeMore = MutableStateFlow(false)
    val showSeeMore: StateFlow<Boolean>
        get() = _showSeeMore
    private val _detailedReviewMediaResult = MutableStateFlow<ProductrevGetReviewMedia?>(null)
    val detailedReviewMediaResult: StateFlow<ProductrevGetReviewMedia?>
        get() = _detailedReviewMediaResult
    private val _currentMediaItem = MutableStateFlow<MediaItemUiModel?>(null)
    val currentMediaItem: StateFlow<MediaItemUiModel?>
        get() = _currentMediaItem
    private val _currentReviewDetail = MutableStateFlow<ReviewDetailUiModel?>(null)
    val currentReviewDetail: StateFlow<ReviewDetailUiModel?>
        get() = _currentReviewDetail
    private val _orientationUiState = MutableStateFlow<OrientationUiState>(OrientationUiState.Portrait)
    val orientationUiState: StateFlow<OrientationUiState>
        get() = _orientationUiState
    private val _overlayVisibility = MutableStateFlow(true)
    val overlayVisibility: StateFlow<Boolean>
        get() = _overlayVisibility
    private val _toggleLikeRequest = MutableStateFlow<Pair<String, Int>?>(null)
    private val _detailedReviewActionMenu = _currentReviewDetail.mapLatest {
        if (it?.isReportable == true) {
            listOf(DetailedReviewActionMenuUiModel(StringRes(R.string.review_action_menu_report)))
        } else emptyList()
    }

    val actionMenuBottomSheetUiState = combine(
        _showDetailedReviewActionMenuBottomSheet,
        _detailedReviewActionMenu,
        _currentReviewDetail
    ) { showDetailedReviewActionMenuBottomSheet, detailedReviewActionMenu, currentReviewDetail ->
        if (currentReviewDetail != null) {
            if (showDetailedReviewActionMenuBottomSheet) {
                ActionMenuBottomSheetUiState.Showing(
                    detailedReviewActionMenu,
                    currentReviewDetail.feedbackID,
                    currentReviewDetail.shopID
                )
            } else {
                ActionMenuBottomSheetUiState.Hidden(
                    detailedReviewActionMenu,
                    currentReviewDetail.feedbackID,
                    currentReviewDetail.shopID
                )
            }
        } else {
            ActionMenuBottomSheetUiState.Hidden(
                detailedReviewActionMenu,
                currentReviewDetail?.feedbackID.orEmpty(),
                currentReviewDetail?.shopID.orEmpty()
            )
        }
    }.stateIn(
        scope = this,
        started = SharingStarted.WhileSubscribed(FLOW_TIMEOUT_MILLIS),
        initialValue = ActionMenuBottomSheetUiState.Hidden(emptyList(), "", "")
    )

    private var loadMoreDetailedReviewMediaJob: Job? = null

    init {
        handleLoadReviewMedia()
        handleToggleLike()
        handleMediaNumberToLoadUpdate()
    }

    private fun handleLoadReviewMedia() {
        launch {
            combine(
                _productID,
                _currentMediaItem.distinctUntilChangedBy { it?.mediaNumber }
            ) { productID, currentMediaItem ->
                val mediaNumberToLoad = if (currentMediaItem is LoadingStateItemUiModel) {
                    currentMediaItem.mediaNumber
                } else UNINITIALIZED_MEDIA_NUMBER_TO_LOAD_VALUE
                val mediaLoaded = _detailedReviewMediaResult.value?.reviewMedia?.any {
                    it.mediaNumber == mediaNumberToLoad
                }.orFalse()
                if (productID.isNotBlank() && mediaNumberToLoad.isMoreThanZero() && !mediaLoaded) {
                    productID to ceil(mediaNumberToLoad.toFloat() / GetDetailedReviewMediaUseCase.DEFAULT_LIMIT.toFloat()).toInt()
                } else null
            }.collectLatest { it ->
                it?.let { params ->
                    loadMoreDetailedReviewMediaJob = loadMoreDetailedReviewMediaJob?.takeIf {
                        !it.isCompleted
                    } ?: getReviewMedia(params.first, params.second)
                }
            }
        }
    }

    private fun handleToggleLike() {
        launch { _toggleLikeRequest.collect { if (it != null) launchNewToggleLikeJob(it) } }
    }

    private fun handleMediaNumberToLoadUpdate() {
        launch {
            _currentMediaItem.collectLatest {
                if (it is LoadingStateItemUiModel) _mediaNumberToLoad.value = it.mediaNumber
            }
        }
    }

    private fun getReviewMedia(productID: String, pageToLoad: Int): Job {
        return launchCatchError(block = {
            getDetailedReviewMediaUseCase.setParams(productID, pageToLoad)
            getDetailedReviewMediaUseCase.executeOnBackground().let { newResponse ->
                _detailedReviewMediaResult.update { oldResponse ->
                    if (oldResponse == null) {
                        newResponse.productrevGetReviewMedia
                    } else {
                        mergeDetailedReviewMediaUseCaseResult(
                            oldResponse,
                            newResponse.productrevGetReviewMedia,
                            pageToLoad
                        )
                    }
                }
            }
        }, onError = {
            enqueueToaster(
                ToasterUiModel(
                    key = TOASTER_KEY_ERROR_GET_REVIEW_MEDIA,
                    message = StringRes(R.string.review_media_gallery_failed_load_detailed_review_error_message),
                    type = Toaster.TYPE_ERROR,
                    duration = Toaster.LENGTH_INDEFINITE,
                    actionText = StringRes(R.string.review_media_gallery_failed_load_detailed_review_error_action_text)
                )
            )
        })
    }

    private fun launchNewToggleLikeJob(params: Pair<String, Int>) {
        launchCatchError(block = {
            toggleLikeReviewUseCase.setParams(params.first, params.second)
            toggleLikeReviewUseCase.executeOnBackground().also { response ->
                _detailedReviewMediaResult.update { currentDetailedReviewMediaResult ->
                    currentDetailedReviewMediaResult?.copy(
                        detail = currentDetailedReviewMediaResult.detail.copy(
                            reviewDetail = currentDetailedReviewMediaResult.detail.reviewDetail.map { reviewDetail ->
                                if (reviewDetail.feedbackId == params.first) {
                                    reviewDetail.copy(
                                        totalLike = response.productrevLikeReview.totalLike,
                                        isLiked = response.productrevLikeReview.isLiked()
                                    )
                                } else reviewDetail
                            }
                        )
                    )
                }
            }
        }, onError = {
            // noop
        })
    }

    private fun mergeDetailedReviewMediaUseCaseResult(
        oldResponse: ProductrevGetReviewMedia,
        newResponse: ProductrevGetReviewMedia,
        pageToLoad: Int
    ): ProductrevGetReviewMedia {
        val mergedReviewImages = if (pageToLoad == getPrevPage()) {
            newResponse.reviewMedia.plus(oldResponse.reviewMedia)
        } else {
            oldResponse.reviewMedia.plus(newResponse.reviewMedia)
        }
        val mergedReviewDetail = if (pageToLoad == getPrevPage()) {
            newResponse.detail.reviewDetail.plus(oldResponse.detail.reviewDetail)
        } else {
            oldResponse.detail.reviewDetail.plus(newResponse.detail.reviewDetail)
        }
        val mergedReviewGalleryImages = if (pageToLoad == getPrevPage()) {
            newResponse.detail.reviewGalleryImages.plus(oldResponse.detail.reviewGalleryImages)
        } else {
            oldResponse.detail.reviewGalleryImages.plus(newResponse.detail.reviewGalleryImages)
        }
        return oldResponse.copy(
            reviewMedia = mergedReviewImages,
            detail = oldResponse.detail.copy(
                reviewDetail = mergedReviewDetail,
                reviewGalleryImages = mergedReviewGalleryImages,
                mediaCountFmt = newResponse.detail.mediaCountFmt,
                mediaCount = newResponse.detail.mediaCount
            ),
            hasNext = if (pageToLoad == getPrevPage()) oldResponse.hasNext else newResponse.hasNext,
            hasPrev = if (pageToLoad == getPrevPage()) newResponse.hasPrev else oldResponse.hasPrev
        )
    }

    private fun getPrevPage(): Int {
        val firstLoaded = _detailedReviewMediaResult.value?.reviewMedia?.firstOrNull()?.mediaNumber.orZero()
        return (firstLoaded / GetDetailedReviewMediaUseCase.DEFAULT_LIMIT)
    }

    fun retryGetReviewMedia() {
        _currentMediaItem.value?.let { currentMediaItem ->
            val mediaNumberToLoad = currentMediaItem.mediaNumber
            val mediaLoaded = _detailedReviewMediaResult.value?.reviewMedia?.any {
                it.mediaNumber == mediaNumberToLoad
            }.orFalse()
            if (_productID.value.isNotBlank() && mediaNumberToLoad.isMoreThanZero() && !mediaLoaded) {
                loadMoreDetailedReviewMediaJob = loadMoreDetailedReviewMediaJob?.takeIf {
                    !it.isCompleted
                } ?: getReviewMedia(
                    _productID.value,
                    ceil(mediaNumberToLoad.toFloat() / GetDetailedReviewMediaUseCase.DEFAULT_LIMIT.toFloat()).toInt()
                )
            }
        }
    }

    fun saveState(outState: Bundle) {
        outState.putSerializable(SAVED_STATE_GET_DETAILED_REVIEW_MEDIA_RESULT, _detailedReviewMediaResult.value)
        outState.putString(SAVED_STATE_PRODUCT_ID, _productID.value)
        outState.putBoolean(SAVED_STATE_SHOW_SEE_MORE, _showSeeMore.value)
        outState.putSerializable(SAVED_STATE_ORIENTATION_UI_STATE, _orientationUiState.value)
        outState.putBoolean(SAVED_STATE_OVERLAY_VISIBILITY, _overlayVisibility.value)
        outState.putBoolean(SAVED_STATE_SHOW_ACTION_MENU_BOTTOM_SHEET, _showDetailedReviewActionMenuBottomSheet.value)
    }

    fun restoreState(savedState: Bundle) {
        _detailedReviewMediaResult.value = savedState.getSavedState(SAVED_STATE_GET_DETAILED_REVIEW_MEDIA_RESULT, _detailedReviewMediaResult.value)
        _productID.value = savedState.getSavedState(SAVED_STATE_PRODUCT_ID, _productID.value) ?: _productID.value
        _showSeeMore.value = savedState.getSavedState(SAVED_STATE_SHOW_SEE_MORE, _showSeeMore.value) ?: _showSeeMore.value
        _orientationUiState.value = savedState.getSavedState(SAVED_STATE_ORIENTATION_UI_STATE, _orientationUiState.value) ?: _orientationUiState.value
        _overlayVisibility.value = savedState.getSavedState(SAVED_STATE_OVERLAY_VISIBILITY, _overlayVisibility.value) ?: _overlayVisibility.value
        _showDetailedReviewActionMenuBottomSheet.value = savedState.getSavedState(SAVED_STATE_SHOW_ACTION_MENU_BOTTOM_SHEET, _showDetailedReviewActionMenuBottomSheet.value) ?: _showDetailedReviewActionMenuBottomSheet.value
    }

    fun tryGetPreloadedData(cacheManager: CacheManager) {
        _mediaNumberToLoad.value = cacheManager.get(
            EXTRAS_TARGET_MEDIA_NUMBER,
            Int::class.java,
            INITIAL_MEDIA_NUMBER_TO_LOAD_VALUE
        ) ?: INITIAL_MEDIA_NUMBER_TO_LOAD_VALUE
        _showSeeMore.value = cacheManager.get(
            EXTRAS_SHOW_SEE_MORE,
            Boolean::class.java,
            _showSeeMore.value
        ) ?: _showSeeMore.value
        _detailedReviewMediaResult.value = cacheManager.get(
            EXTRAS_PRELOADED_DETAILED_REVIEW_MEDIA_RESULT,
            ProductrevGetReviewMedia::class.java,
            _detailedReviewMediaResult.value
        ) ?: _detailedReviewMediaResult.value
        _productID.value = cacheManager.getString(
            EXTRAS_PRODUCT_ID,
            _productID.value
        ) ?: _productID.value
        _shopID.value = cacheManager.getString(
            EXTRAS_SHOP_ID,
            _shopID.value
        ) ?: _shopID.value
        _isProductReview.value = cacheManager.get(
            EXTRAS_IS_PRODUCT_REVIEW,
            Boolean::class.java,
            _isProductReview.value
        ) ?: _isProductReview.value
        _isFromGallery.value = cacheManager.get(
            EXTRAS_IS_FROM_GALLERY,
            Boolean::class.java,
            _isFromGallery.value
        ) ?: _isFromGallery.value
    }

    fun updateCurrentMediaItem(mediaItem: MediaItemUiModel?) {
        _currentMediaItem.value = mediaItem
    }

    fun updateReviewDetailItem(reviewDetail: ReviewDetailUiModel?) {
        _currentReviewDetail.value = reviewDetail
    }

    fun requestToggleLike(feedbackID: String, invertedLikeStatus: Int) {
        _toggleLikeRequest.value = feedbackID to invertedLikeStatus
    }

    fun requestPortraitMode() {
        _orientationUiState.value = OrientationUiState.Portrait
    }

    fun requestLandscapeMode() {
        _orientationUiState.value = OrientationUiState.Landscape
    }

    fun toggleOverlayVisibility() {
        _overlayVisibility.update { !it }
    }

    fun getProductId(): String {
        return _productID.value
    }

    fun getShopId(): String {
        return _shopID.value
    }

    fun isProductReview(): Boolean {
        return _isProductReview.value
    }

    fun isFromGallery(): Boolean {
        return _isFromGallery.value
    }

    fun showActionMenuBottomSheet() {
        _showDetailedReviewActionMenuBottomSheet.value = true
    }

    fun dismissActionMenuBottomSheet() {
        _showDetailedReviewActionMenuBottomSheet.value = false
    }

    fun enqueueToaster(model: ToasterUiModel) {
        launch { _toasterQueue.emit(model) }
    }

    fun toasterEventActionClicked(key: String) {
        launch { _toasterEventActionClickQueue.emit(key) }
    }

    fun getTotalMediaCount(): Long {
        return _detailedReviewMediaResult.value?.detail?.mediaCount.orZero()
    }
}