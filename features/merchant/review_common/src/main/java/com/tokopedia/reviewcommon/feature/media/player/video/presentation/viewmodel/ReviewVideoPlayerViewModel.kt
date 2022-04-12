package com.tokopedia.reviewcommon.feature.media.player.video.presentation.viewmodel

import android.graphics.Bitmap
import android.os.Bundle
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.reviewcommon.extension.getSavedState
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.uistate.ReviewVideoErrorUiState
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.uistate.ReviewVideoPlaybackUiState
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.uistate.ReviewVideoPlayerUiState
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.uistate.ReviewVideoThumbnailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ReviewVideoPlayerViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    companion object {
        private const val FLOW_TIMEOUT_MILLIS = 5000L

        const val SAVED_STATE_VIDEO_PLAYER_UI_STATE = "savedStateVideoPlayerUiState"
        const val SAVED_STATE_PLAYBACK_UI_STATE = "savedStatePlaybackUiState"
    }

    private val _videoPlaybackUiState: MutableStateFlow<ReviewVideoPlaybackUiState> =
        MutableStateFlow(ReviewVideoPlaybackUiState.Inactive())
    private val _videoPlayerUiState: MutableStateFlow<ReviewVideoPlayerUiState> =
        MutableStateFlow(ReviewVideoPlayerUiState.Initial())
    private val _videoThumbnailUiState: MutableStateFlow<ReviewVideoThumbnailUiState> =
        MutableStateFlow(ReviewVideoThumbnailUiState.Hidden())
    private val _videoErrorUiState: MutableStateFlow<ReviewVideoErrorUiState> =
        MutableStateFlow(ReviewVideoErrorUiState.Hidden)
    private val _connectedToWifi: MutableStateFlow<Boolean> =
        MutableStateFlow(false)

    val videoPlaybackUiState: StateFlow<ReviewVideoPlaybackUiState>
        get() = _videoPlaybackUiState
    val videoErrorUiState: StateFlow<ReviewVideoErrorUiState>
        get() = _videoErrorUiState
    val videoPlayerUiState: StateFlow<ReviewVideoPlayerUiState>
        get() = combine(
            _videoPlaybackUiState,
            _videoPlayerUiState,
            _connectedToWifi
        ) { playbackUiState, playerUiState, wifiConnectivityStatus ->
            if (playerUiState is ReviewVideoPlayerUiState.RestoringState) {
                val isOnPlayingState = playbackUiState is ReviewVideoPlaybackUiState.Playing
                val isOnBufferingState = playbackUiState is ReviewVideoPlaybackUiState.Buffering
                val shouldPlayWhenActive = wifiConnectivityStatus || (playbackUiState is ReviewVideoPlaybackUiState.Inactive && playbackUiState.shouldPlayWhenActive)
                playerUiState.copy(
                    playWhenReady = isOnPlayingState || isOnBufferingState || shouldPlayWhenActive,
                    presentationTimeMs = playbackUiState.currentPosition
                )
            } else playerUiState
        }.stateIn(
            scope = this,
            started = SharingStarted.WhileSubscribed(FLOW_TIMEOUT_MILLIS),
            initialValue = _videoPlayerUiState.value
        )
    val videoThumbnailUiState: StateFlow<ReviewVideoThumbnailUiState>
        get() = combine(
            _videoPlaybackUiState,
            _videoThumbnailUiState
        ) { playbackUiState, thumbnailUiState ->
            if (
                playbackUiState is ReviewVideoPlaybackUiState.Ended ||
                playbackUiState is ReviewVideoPlaybackUiState.Inactive
            ) {
                ReviewVideoThumbnailUiState.Showed(thumbnailUiState.videoThumbnail)
            } else {
                ReviewVideoThumbnailUiState.Hidden(thumbnailUiState.videoThumbnail)
            }
        }.stateIn(
            scope = this,
            started = SharingStarted.WhileSubscribed(FLOW_TIMEOUT_MILLIS),
            initialValue = _videoThumbnailUiState.value
        )

    private fun restoreSavedPlaybackUiState(savedInstanceState: Bundle) {
        _videoPlaybackUiState.value = savedInstanceState.getSavedState(
            SAVED_STATE_PLAYBACK_UI_STATE,
            _videoPlaybackUiState.value
        )!!
    }

    private fun restoreVideoPlayerUiState(savedInstanceState: Bundle) {
        _videoPlayerUiState.value = savedInstanceState.getSavedState(
            SAVED_STATE_VIDEO_PLAYER_UI_STATE,
            _videoPlayerUiState.value
        )!!
    }

    fun setVideoUri(videoUri: String) {
        _videoPlayerUiState.value = ReviewVideoPlayerUiState.Initial(videoUri)
    }

    fun setVideoPlayerStateToChangingConfiguration() {
        _videoPlayerUiState.update {
            ReviewVideoPlayerUiState.ChangingConfiguration(it.videoUri)
        }
    }

    fun resetVideoPlayerState() {
        _videoPlayerUiState.update {
            ReviewVideoPlayerUiState.Initial(it.videoUri)
        }
    }

    fun setVideoPlayerStateToRestoring() {
        _videoPlayerUiState.update {
            ReviewVideoPlayerUiState.RestoringState(it.videoUri)
        }
    }

    fun setVideoPlayerStateToReadyToPlay() {
        _videoPlayerUiState.update {
            ReviewVideoPlayerUiState.ReadyToPlay(it.videoUri)
        }
    }

    fun setPlaybackStateToPlaying(currentPosition: Long) {
        _videoPlaybackUiState.update {
            if (_videoPlayerUiState.value is ReviewVideoPlayerUiState.ReadyToPlay) {
                ReviewVideoPlaybackUiState.Playing(currentPosition)
            } else it
        }
    }

    fun setPlaybackStateToBuffering(currentPosition: Long) {
        _videoPlaybackUiState.update {
            if (_videoPlayerUiState.value is ReviewVideoPlayerUiState.ReadyToPlay) {
                ReviewVideoPlaybackUiState.Buffering(currentPosition)
            } else it
        }
    }

    fun setPlaybackStateToPaused(currentPosition: Long) {
        _videoPlaybackUiState.update {
            if (_videoPlayerUiState.value is ReviewVideoPlayerUiState.ReadyToPlay) {
                ReviewVideoPlaybackUiState.Paused(currentPosition)
            } else it
        }
    }

    fun setPlaybackStateToPreloading(currentPosition: Long) {
        _videoPlaybackUiState.update {
            if (_videoPlayerUiState.value is ReviewVideoPlayerUiState.ReadyToPlay) {
                ReviewVideoPlaybackUiState.Preloading(currentPosition)
            } else it
        }
    }

    fun setPlaybackStateToEnded(currentPosition: Long) {
        _videoPlaybackUiState.update {
            if (_videoPlayerUiState.value is ReviewVideoPlayerUiState.ReadyToPlay) {
                ReviewVideoPlaybackUiState.Ended(currentPosition)
            } else it
        }
    }

    fun setPlaybackStateToError(currentPosition: Long) {
        _videoPlaybackUiState.update {
            if (_videoPlayerUiState.value is ReviewVideoPlayerUiState.ReadyToPlay) {
                ReviewVideoPlaybackUiState.Error(currentPosition)
            } else it
        }
    }

    fun setPlaybackStateToInactive(currentPosition: Long) {
        _videoPlaybackUiState.update {
            if (_videoPlayerUiState.value is ReviewVideoPlayerUiState.ReadyToPlay) {
                val isOnPlayingState = it is ReviewVideoPlaybackUiState.Playing
                val isOnBufferingState = it is ReviewVideoPlaybackUiState.Buffering
                val shouldPlayWhenActive = it is ReviewVideoPlaybackUiState.Inactive && it.shouldPlayWhenActive
                ReviewVideoPlaybackUiState.Inactive(
                    currentPosition = currentPosition,
                    shouldPlayWhenActive = isOnPlayingState || isOnBufferingState || shouldPlayWhenActive
                )
            } else it
        }
    }

    fun updateVideoThumbnail(bitmap: Bitmap?) {
        _videoThumbnailUiState.update {
            when (it) {
                is ReviewVideoThumbnailUiState.Hidden -> it.copy(videoThumbnail = bitmap)
                is ReviewVideoThumbnailUiState.Showed -> {
                    if (bitmap == null) {
                        ReviewVideoThumbnailUiState.Hidden()
                    } else {
                        ReviewVideoThumbnailUiState.Showed(videoThumbnail = bitmap)
                    }
                }
            }
        }
    }

    fun showVideoThumbnail() {
        _videoThumbnailUiState.update {
            if (it.videoThumbnail == null) {
                ReviewVideoThumbnailUiState.Hidden()
            } else {
                ReviewVideoThumbnailUiState.Showed(videoThumbnail = it.videoThumbnail)
            }
        }
    }

    fun hideVideoThumbnail() {
        _videoThumbnailUiState.update {
            ReviewVideoThumbnailUiState.Hidden(it.videoThumbnail)
        }
    }

    fun showVideoError() {
        _videoErrorUiState.value = ReviewVideoErrorUiState.Showing
    }

    fun hideVideoError() {
        _videoErrorUiState.value = ReviewVideoErrorUiState.Hidden
    }

    fun saveUiState(outState: Bundle) {
        _videoPlaybackUiState.value.let {
            outState.putParcelable(SAVED_STATE_PLAYBACK_UI_STATE, it)
        }
        _videoPlayerUiState.value.let {
            outState.putParcelable(SAVED_STATE_VIDEO_PLAYER_UI_STATE, it)
        }
    }

    fun restoreUiState(savedInstanceState: Bundle) {
        restoreVideoPlayerUiState(savedInstanceState)
        restoreSavedPlaybackUiState(savedInstanceState)
    }

    fun updateWifiConnectivityStatus(connected: Boolean) {
        _connectedToWifi.value = connected
    }
}