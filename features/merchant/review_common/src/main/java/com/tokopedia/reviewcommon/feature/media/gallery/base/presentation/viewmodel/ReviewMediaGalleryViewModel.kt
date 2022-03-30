package com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.viewmodel

import android.os.Bundle
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isLessThanZero
import com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.uimodel.LoadingStateItemUiModel
import com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.uimodel.MediaItemUiModel
import com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.uistate.ReviewMediaGalleryAdapterUiState
import com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.uistate.ReviewMediaGalleryUiState
import com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.uistate.ReviewMediaGalleryViewPagerUiState
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductrevGetReviewMedia
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.uistate.DetailedReviewMediaGalleryOrientationUiState
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.viewmodel.SharedReviewMediaGalleryViewModel
import com.tokopedia.reviewcommon.feature.media.player.image.presentation.uimodel.ImageMediaItemUiModel
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.model.VideoMediaItemUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReviewMediaGalleryViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    companion object {
        private const val STATE_FLOW_STOP_TIMEOUT_MILLIS = 5000L
        const val SAVED_STATE_MEDIA_GALLERY_ADAPTER_UI_STATE = "savedStateMediaAdapterUiState"
        const val SAVED_STATE_MEDIA_GALLERY_VIEW_PAGER_UI_STATE = "savedStateMediaViewPagerUiState"
    }

    private val _orientationUiState = MutableStateFlow<DetailedReviewMediaGalleryOrientationUiState>(DetailedReviewMediaGalleryOrientationUiState.Portrait)
    private val _viewPagerUiState = MutableStateFlow(ReviewMediaGalleryViewPagerUiState())
    private val _mediaItems = MutableStateFlow<List<MediaItemUiModel>>(emptyList())
    private val adapterUiState = _mediaItems.mapLatest {
        ReviewMediaGalleryAdapterUiState(it)
    }.stateIn(
        scope = this,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_STOP_TIMEOUT_MILLIS),
        initialValue = ReviewMediaGalleryAdapterUiState(_mediaItems.value)
    )
    val uiState: StateFlow<ReviewMediaGalleryUiState> = combine(
        adapterUiState, _viewPagerUiState, ::mapUiState
    ).stateIn(
        scope = this,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_STOP_TIMEOUT_MILLIS),
        initialValue = ReviewMediaGalleryUiState(adapterUiState.value, _viewPagerUiState.value)
    )
    val currentMediaItem = uiState.mapLatest { uiState ->
        uiState.adapterUiState.mediaItemUiModels.getOrNull(
            uiState.viewPagerUiState.currentPagerPosition
        )
    }.stateIn(
        scope = this,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_STOP_TIMEOUT_MILLIS),
        initialValue = null
    )

    // workaround for viewpager2 issue where page changed after screen rotation
    private var changingConfiguration: Boolean = false

    private fun mapUiState(
        adapterUiState: ReviewMediaGalleryAdapterUiState,
        viewPagerUiState: ReviewMediaGalleryViewPagerUiState
    ): ReviewMediaGalleryUiState {
        val currentUiState = uiState.value
        val firstMediaItem = currentUiState.adapterUiState.mediaItemUiModels.firstOrNull()
        val showedMediaItem = currentUiState.adapterUiState.mediaItemUiModels.getOrNull(
            viewPagerUiState.currentPagerPosition
        )
        return if (firstMediaItem is LoadingStateItemUiModel && showedMediaItem is LoadingStateItemUiModel) {
            val expectedMediaPosition = adapterUiState.mediaItemUiModels.indexOfFirst {
                it !is LoadingStateItemUiModel && it.mediaNumber == firstMediaItem.mediaNumber
            }
            if (expectedMediaPosition.isLessThanZero()) {
                ReviewMediaGalleryUiState(adapterUiState, viewPagerUiState)
            } else if (viewPagerUiState.currentPagerPosition == expectedMediaPosition) {
                ReviewMediaGalleryUiState(adapterUiState, viewPagerUiState)
            } else {
                onPageSelected(expectedMediaPosition)
                currentUiState
            }
        } else {
            ReviewMediaGalleryUiState(adapterUiState, viewPagerUiState)
        }
    }

    private fun ProductrevGetReviewMedia.getReviewImageByID(
        imageId: String,
        imageNumber: Int,
        showSeeMore: Boolean,
        totalMediaCount: Int
    ): ImageMediaItemUiModel? {
        return detail.reviewGalleryImages.find {
            it.attachmentId == imageId
        }?.let {
            ImageMediaItemUiModel(it.fullsizeURL, imageNumber, showSeeMore, totalMediaCount)
        }
    }

    private fun ProductrevGetReviewMedia.getReviewVideoByID(
        videoId: String,
        videoNumber: Int,
        showSeeMore: Boolean,
        totalMediaCount: Int
    ): VideoMediaItemUiModel? {
        return detail.reviewGalleryVideos.find {
            it.attachmentId == videoId
        }?.let {
            VideoMediaItemUiModel(it.url, videoNumber, showSeeMore, totalMediaCount)
        }
    }

    fun onPageSelected(position: Int) {
        if (!changingConfiguration) {
            _viewPagerUiState.update {
                it.copy(currentPagerPosition = position)
            }
        }
    }

    fun saveUiState(outState: Bundle) {
        outState.putParcelable(SAVED_STATE_MEDIA_GALLERY_ADAPTER_UI_STATE, adapterUiState.value)
        outState.putParcelable(SAVED_STATE_MEDIA_GALLERY_VIEW_PAGER_UI_STATE, _viewPagerUiState.value)
    }

    fun restoreUiState(savedInstanceState: Bundle) {
        savedInstanceState.getParcelable<ReviewMediaGalleryViewPagerUiState>(
            SAVED_STATE_MEDIA_GALLERY_VIEW_PAGER_UI_STATE
        )?.let { savedReviewMediaGalleryViewPagerUiState ->
            _viewPagerUiState.value = savedReviewMediaGalleryViewPagerUiState
        }
    }

    suspend fun updateDetailedReviewMediaResult(
        response: ProductrevGetReviewMedia?,
        mediaNumberToLoad: Int,
        showSeeMore: Boolean,
        totalMediaCount: Int
    ) {
        withContext(dispatchers.computation) {
            val mediaItems: MutableList<MediaItemUiModel> = response?.let { responseData ->
                responseData.reviewMedia.mapIndexedNotNull { index, reviewMedia ->
                    if (reviewMedia.imageId.isNotBlank()) {
                        responseData.getReviewImageByID(
                            reviewMedia.imageId,
                            reviewMedia.mediaNumber,
                            showSeeMore && index == responseData.reviewMedia.size - 1,
                            totalMediaCount
                        )
                    } else if (reviewMedia.videoId.isNotBlank()) {
                        responseData.getReviewVideoByID(
                            reviewMedia.videoId,
                            reviewMedia.mediaNumber,
                            showSeeMore && index == responseData.reviewMedia.size - 1,
                            totalMediaCount
                        )
                    } else {
                        null
                    }
                }
            }.orEmpty().toMutableList()
            // if current media items is empty but our newest response is not empty then let the ui show
            // the load state first before submitting our newest media items
            if (mediaItems.isNotEmpty() && _mediaItems.value.isEmpty()) {
                val initialMediaItems = listOf(LoadingStateItemUiModel(mediaNumber = mediaNumberToLoad))
                _mediaItems.value = initialMediaItems
                uiState.first { it.adapterUiState.mediaItemUiModels == initialMediaItems }
            }
            if (mediaNumberToLoad != SharedReviewMediaGalleryViewModel.UNINITIALIZED_MEDIA_NUMBER_TO_LOAD_VALUE) {
                if (mediaItems.isEmpty()) {
                    mediaItems.add(LoadingStateItemUiModel(mediaNumber = mediaNumberToLoad))
                } else {
                    if (response?.hasPrev == true) {
                        mediaItems.add(
                            Int.ZERO,
                            LoadingStateItemUiModel(mediaNumber = mediaItems.first().mediaNumber - 1)
                        )
                    }
                    if (response?.hasNext == true && !showSeeMore) {
                        mediaItems.add(LoadingStateItemUiModel(mediaNumber = mediaItems.last().mediaNumber + 1))
                    }
                }
            }
            _mediaItems.value = mediaItems
        }
    }

    fun requestToggleViewPagerSwipe(enable: Boolean) {
        _viewPagerUiState.update { it.copy(enableUserInput = enable) }
    }

    fun updateOrientationUiState(orientationUiState: DetailedReviewMediaGalleryOrientationUiState) {
        changingConfiguration = _orientationUiState.value != orientationUiState
        _orientationUiState.value = orientationUiState
    }
}