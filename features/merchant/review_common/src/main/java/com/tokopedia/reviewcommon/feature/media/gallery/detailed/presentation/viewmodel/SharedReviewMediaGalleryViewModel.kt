package com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.viewmodel

import android.os.Bundle
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cachemanager.CacheManager
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.reviewcommon.extension.getSavedState
import com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.uimodel.LoadingStateItemUiModel
import com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.uimodel.MediaItemUiModel
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductrevGetReviewMedia
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.usecase.GetDetailedReviewMediaUseCase
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.usecase.ToggleLikeReviewUseCase
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.uistate.DetailedReviewMediaGalleryOrientationUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
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

        private const val SAVED_STATE_PRODUCT_ID = "savedStateProductId"
        private const val SAVED_GET_DETAILED_REVIEW_MEDIA_RESULT = "savedGetDetailedReviewMediaResult"
        private const val SAVED_ORIENTATION_UI_STATE = "savedOrientationUiState"
        private const val SAVED_OVERLAY_VISIBILITY = "savedOverlayVisibility"

        const val EXTRAS_PRODUCT_ID = "extrasProductId"
        const val EXTRAS_TARGET_MEDIA_NUMBER = "extrasTargetMediaNumber"
        const val EXTRAS_PRELOADED_DETAILED_REVIEW_MEDIA_RESULT = "extrasPreloadedReviewMediaResult"

        const val UNINITIALIZED_PRODUCT_ID_VALUE = ""
        const val UNINITIALIZED_MEDIA_NUMBER_TO_LOAD_VALUE = -1
        const val INITIAL_MEDIA_NUMBER_TO_LOAD_VALUE = 1
    }

    private val _productID = MutableStateFlow(UNINITIALIZED_PRODUCT_ID_VALUE)
    val productID: StateFlow<String>
        get() = _productID
    private val _mediaNumberToLoad = MutableStateFlow(UNINITIALIZED_MEDIA_NUMBER_TO_LOAD_VALUE)
    val mediaNumberToLoad: StateFlow<Int>
        get() = _mediaNumberToLoad
    private val _detailedReviewMediaResult = MutableStateFlow<ProductrevGetReviewMedia?>(null)
    val detailedReviewMediaResult: StateFlow<ProductrevGetReviewMedia?>
        get() = _detailedReviewMediaResult
    private val _currentMediaItem = MutableStateFlow<MediaItemUiModel?>(null)
    val currentMediaItem: StateFlow<MediaItemUiModel?>
        get() = _currentMediaItem
    private val _orientationUiState = MutableStateFlow<DetailedReviewMediaGalleryOrientationUiState>(DetailedReviewMediaGalleryOrientationUiState.Portrait)
    val orientationUiState: StateFlow<DetailedReviewMediaGalleryOrientationUiState>
        get() = _orientationUiState
    private val _overlayVisibility = MutableStateFlow(true)
    val overlayVisibility: StateFlow<Boolean>
        get() = _overlayVisibility
    private val _toggleLikeRequest = MutableStateFlow<Pair<String, Int>?>(null)

    private var loadMoreDetailedReviewMediaJob: Job? = null

    init {
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
        launch {
            _toggleLikeRequest.collect {
                if (it != null) {
                    launchNewToggleLikeJob(it)
                }
            }
        }
        launch {
            _currentMediaItem.collectLatest {
                if (it is LoadingStateItemUiModel) {
                    _mediaNumberToLoad.value = it.mediaNumber
                }
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
        }, onError = {})
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

    fun saveState(outState: Bundle) {
        outState.putSerializable(SAVED_GET_DETAILED_REVIEW_MEDIA_RESULT, _detailedReviewMediaResult.value)
        outState.putString(SAVED_STATE_PRODUCT_ID, _productID.value)
        outState.putSerializable(SAVED_ORIENTATION_UI_STATE, _orientationUiState.value)
        outState.putSerializable(SAVED_OVERLAY_VISIBILITY, _overlayVisibility.value)
    }

    fun restoreState(savedState: Bundle) {
        _detailedReviewMediaResult.value = savedState.getSavedState(SAVED_GET_DETAILED_REVIEW_MEDIA_RESULT, _detailedReviewMediaResult.value)
        _productID.value = savedState.getSavedState(SAVED_STATE_PRODUCT_ID, _productID.value) ?: _productID.value
        _orientationUiState.value = savedState.getSavedState(SAVED_ORIENTATION_UI_STATE, _orientationUiState.value) ?: _orientationUiState.value
        _overlayVisibility.value = savedState.getSavedState(SAVED_OVERLAY_VISIBILITY, _overlayVisibility.value) ?: _overlayVisibility.value
    }

    fun tryGetPreloadedData(cacheManager: CacheManager) {
        launchCatchError(block = {
            _mediaNumberToLoad.value = cacheManager.get(
                EXTRAS_TARGET_MEDIA_NUMBER,
                Int::class.java,
                INITIAL_MEDIA_NUMBER_TO_LOAD_VALUE
            ) ?: INITIAL_MEDIA_NUMBER_TO_LOAD_VALUE
            _detailedReviewMediaResult.value = cacheManager.get(
                EXTRAS_PRELOADED_DETAILED_REVIEW_MEDIA_RESULT,
                ProductrevGetReviewMedia::class.java,
                _detailedReviewMediaResult.value
            ) ?: _detailedReviewMediaResult.value
            _productID.value = cacheManager.getString(
                EXTRAS_PRODUCT_ID,
                _productID.value
            ) ?: _productID.value
        }, onError = {})
    }

    fun updateCurrentMediaItem(mediaItem: MediaItemUiModel?) {
        _currentMediaItem.value = mediaItem
    }

    fun requestToggleLike(feedbackID: String, invertedLikeStatus: Int) {
        _toggleLikeRequest.value = feedbackID to invertedLikeStatus
    }

    fun requestPortraitMode() {
        _orientationUiState.value = DetailedReviewMediaGalleryOrientationUiState.Portrait
    }

    fun requestLandscapeMode() {
        _orientationUiState.value = DetailedReviewMediaGalleryOrientationUiState.Landscape
    }

    fun toggleOverlayVisibility() {
        _overlayVisibility.update { !it }
    }

    fun getProductId(): String {
        return _productID.value
    }
}