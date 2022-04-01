package com.tokopedia.reviewcommon.feature.media.player.controller.presentation.viewmodel

import android.os.Bundle
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.reviewcommon.extension.combine
import com.tokopedia.reviewcommon.extension.getSavedState
import com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.uimodel.LoadingStateItemUiModel
import com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.uimodel.MediaItemUiModel
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductrevGetReviewMedia
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.uistate.OrientationUiState
import com.tokopedia.reviewcommon.feature.media.player.controller.presentation.uistate.ReviewMediaPlayerControllerUiState
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.model.VideoMediaItemUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class ReviewMediaPlayerControllerViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    companion object {
        private const val STATE_FLOW_STOP_TIMEOUT_MILLIS = 5000L

        private const val SAVED_STATE_MUTED = "savedStateMuted"
    }

    private val _currentMediaItem = MutableStateFlow<MediaItemUiModel?>(null)
    private val _getDetailedReviewMediaResult = MutableStateFlow<ProductrevGetReviewMedia?>(null)
    private val _orientationUiState = MutableStateFlow<OrientationUiState>(OrientationUiState.Portrait)
    private val _overlayVisibility = MutableStateFlow(true)
    private val _muted = MutableStateFlow(true)

    private val shouldShowVideoPlayerController = _currentMediaItem.mapLatest { currentMediaItem ->
        currentMediaItem is VideoMediaItemUiModel
    }.stateIn(
        scope = this,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_STOP_TIMEOUT_MILLIS),
        initialValue = false
    )
    private val totalMedia = _getDetailedReviewMediaResult.mapLatest {
        it?.detail?.mediaCount.orZero().toInt()
    }.stateIn(
        scope = this,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_STOP_TIMEOUT_MILLIS),
        initialValue = Int.ZERO
    )
    private val shouldShowMediaCounter = combine(
        totalMedia, _currentMediaItem
    ) { totalMedia, currentMediaItem ->
        totalMedia.isMoreThanZero() && currentMediaItem?.mediaNumber.isMoreThanZero() &&
        currentMediaItem !is LoadingStateItemUiModel
    }.stateIn(
        scope = this,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_STOP_TIMEOUT_MILLIS),
        initialValue = false
    )
    private val shouldShowMediaCounterLoader = _currentMediaItem.mapLatest { currentMediaItem ->
        currentMediaItem is LoadingStateItemUiModel
    }.stateIn(
        scope = this,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_STOP_TIMEOUT_MILLIS),
        initialValue = false
    )

    val uiState = combine(
        shouldShowVideoPlayerController,
        shouldShowMediaCounter,
        shouldShowMediaCounterLoader,
        _muted,
        _orientationUiState,
        _overlayVisibility,
        _currentMediaItem,
        totalMedia
    ) { shouldShowVideoPlayerController, shouldShowMediaCounter, shouldShowMediaCounterLoader,
        muted, orientationUiState, overlayVisibility, currentMediaItem, totalMedia ->
        ReviewMediaPlayerControllerUiState(
            shouldShowVideoPlayerController = shouldShowVideoPlayerController,
            shouldShowMediaCounter = shouldShowMediaCounter,
            shouldShowMediaCounterLoader = shouldShowMediaCounterLoader,
            muted = muted,
            orientationUiState = orientationUiState,
            overlayVisibility = overlayVisibility,
            currentGalleryPosition = currentMediaItem?.mediaNumber.orZero(),
            totalMedia = totalMedia
        )
    }.stateIn(
        scope = this,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_STOP_TIMEOUT_MILLIS),
        initialValue = ReviewMediaPlayerControllerUiState(
            shouldShowVideoPlayerController.value,
            shouldShowMediaCounter.value,
            shouldShowMediaCounterLoader.value,
            _muted.value,
            _orientationUiState.value,
            _overlayVisibility.value,
            _currentMediaItem.value?.mediaNumber.orZero(),
            totalMedia.value
        )
    )

    fun updateGetDetailedReviewMediaResult(response: ProductrevGetReviewMedia?) {
        _getDetailedReviewMediaResult.value = response
    }

    fun updateCurrentMediaItem(currentMediaItem: MediaItemUiModel?) {
        _currentMediaItem.value = currentMediaItem
    }

    fun updateOrientationUiState(orientationUiState: OrientationUiState) {
        _orientationUiState.value = orientationUiState
    }

    fun unmute() {
        _muted.value = false
    }

    fun mute() {
        _muted.value = true
    }

    fun saveState(outState: Bundle) {
        outState.putBoolean(SAVED_STATE_MUTED, _muted.value)
    }

    fun restoreState(savedInstanceState: Bundle) {
        _muted.value = savedInstanceState.getSavedState(SAVED_STATE_MUTED, _muted.value) ?: _muted.value
    }

    fun updateOverlayVisibility(visible: Boolean) {
        _overlayVisibility.value = visible
    }
}