package com.tokopedia.review.feature.media.gallery.base.presentation.viewmodel

import android.os.CountDownTimer
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cachemanager.CacheManager
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isLessThanZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.review.feature.media.gallery.base.di.qualifier.ReviewMediaGalleryGson
import com.tokopedia.review.feature.media.gallery.base.presentation.uimodel.LoadingStateItemUiModel
import com.tokopedia.review.feature.media.gallery.base.presentation.uimodel.MediaItemUiModel
import com.tokopedia.review.feature.media.gallery.base.presentation.uistate.AdapterUiState
import com.tokopedia.review.feature.media.gallery.base.presentation.uistate.ReviewMediaGalleryUiState
import com.tokopedia.review.feature.media.gallery.base.presentation.uistate.ViewPagerUiState
import com.tokopedia.review.feature.media.gallery.detailed.presentation.uistate.OrientationUiState
import com.tokopedia.review.feature.media.gallery.detailed.presentation.viewmodel.SharedReviewMediaGalleryViewModel
import com.tokopedia.review.feature.media.player.image.presentation.uimodel.ImageMediaItemUiModel
import com.tokopedia.review.feature.media.player.video.presentation.model.VideoMediaItemUiModel
import com.tokopedia.reviewcommon.extension.isMoreThanZero
import com.tokopedia.reviewcommon.extension.put
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductrevGetReviewMedia
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
    private val dispatchers: CoroutineDispatchers,
    @ReviewMediaGalleryGson private val gson: Gson
) : BaseViewModel(dispatchers.io) {

    companion object {
        private const val STATE_FLOW_STOP_TIMEOUT_MILLIS = 5000L
        private const val VIEW_PAGER_FREEZE_DURATION = 500L
        const val SAVED_STATE_MEDIA_GALLERY_ADAPTER_UI_STATE = "savedStateMediaAdapterUiState"
        const val SAVED_STATE_MEDIA_GALLERY_VIEW_PAGER_UI_STATE = "savedStateMediaViewPagerUiState"
    }

    private val _orientationUiState = MutableStateFlow(OrientationUiState())
    private val _viewPagerUiState = MutableStateFlow(ViewPagerUiState())
    val viewPagerUiState: StateFlow<ViewPagerUiState>
        get() = _viewPagerUiState
    private val _mediaItems = MutableStateFlow<List<MediaItemUiModel>>(emptyList())
    private val adapterUiState = _mediaItems.mapLatest {
        AdapterUiState(it)
    }.stateIn(
        scope = this,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_STOP_TIMEOUT_MILLIS),
        initialValue = AdapterUiState(_mediaItems.value)
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

    private val enableViewPagerTimer = object: CountDownTimer(VIEW_PAGER_FREEZE_DURATION, VIEW_PAGER_FREEZE_DURATION) {
        override fun onTick(millisUntilFinished: Long) {
            // noop
        }

        override fun onFinish() {
            requestToggleViewPagerSwipe(true)
        }
    }
    // workaround for viewpager2 issue where page changed after screen rotation
    private var changingConfiguration: Boolean = false

    private fun mapUiState(
        adapterUiState: AdapterUiState,
        viewPagerUiState: ViewPagerUiState
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
            ImageMediaItemUiModel(
                it.attachmentId,
                it.fullsizeURL,
                imageNumber,
                showSeeMore,
                totalMediaCount,
                it.feedbackId
            )
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
            VideoMediaItemUiModel(
                it.attachmentId,
                it.url,
                videoNumber,
                showSeeMore,
                totalMediaCount,
                it.feedbackId
            )
        }
    }

    fun onPageSelected(position: Int) {
        if (!changingConfiguration) {
            enableViewPagerTimer.cancel()
            requestToggleViewPagerSwipe(false)
            _viewPagerUiState.update {
                if (it.currentPagerPosition != position) {
                    it.copy(
                        previousPagerPosition = it.currentPagerPosition,
                        currentPagerPosition = position
                    )
                } else it
            }
            enableViewPagerTimer.start()
        }
    }

    fun saveUiState(cacheManager: CacheManager) {
        enableViewPagerTimer.cancel()
        requestToggleViewPagerSwipe(true)
        cacheManager.put(
            customId = SAVED_STATE_MEDIA_GALLERY_ADAPTER_UI_STATE,
            objectToPut = adapterUiState.value,
            gson = gson
        )
        cacheManager.put(SAVED_STATE_MEDIA_GALLERY_VIEW_PAGER_UI_STATE, _viewPagerUiState.value)
    }

    fun restoreUiState(cacheManager: CacheManager) {
        cacheManager.get(
            SAVED_STATE_MEDIA_GALLERY_VIEW_PAGER_UI_STATE,
            ViewPagerUiState::class.java,
            _viewPagerUiState.value
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
                    if ((reviewMedia.imageId.isNotBlank() && reviewMedia.imageId.toLongOrNull() == null) || reviewMedia.imageId.toLongOrZero().isMoreThanZero()) {
                        responseData.getReviewImageByID(
                            imageId = reviewMedia.imageId,
                            imageNumber = reviewMedia.mediaNumber,
                            showSeeMore = showSeeMore && index == responseData.reviewMedia.size - 1,
                            totalMediaCount = totalMediaCount
                        )
                    } else if ((reviewMedia.videoId.isNotBlank() && reviewMedia.videoId.toLongOrNull() == null) || reviewMedia.videoId.toLongOrZero().isMoreThanZero()) {
                        responseData.getReviewVideoByID(
                            videoId = reviewMedia.videoId,
                            videoNumber = reviewMedia.mediaNumber,
                            showSeeMore = showSeeMore && index == responseData.reviewMedia.size - 1,
                            totalMediaCount = totalMediaCount
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
                if (response == null || mediaItems.isEmpty()) {
                    mediaItems.add(LoadingStateItemUiModel(mediaNumber = mediaNumberToLoad))
                } else {
                    if (response.hasPrev) {
                        mediaItems.add(
                            Int.ZERO,
                            LoadingStateItemUiModel(mediaNumber = mediaItems.first().mediaNumber - 1)
                        )
                    }
                    if (response.hasNext && !showSeeMore) {
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

    fun updateOrientationUiState(orientationUiState: OrientationUiState) {
        changingConfiguration = _orientationUiState.value != orientationUiState
        _orientationUiState.value = orientationUiState
    }
}