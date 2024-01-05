package com.tokopedia.play.broadcaster.shorts.view.manager.videoPreview

import androidx.compose.runtime.MutableState
import com.tokopedia.play.broadcaster.shorts.view.compose.VideoState
import com.tokopedia.play_common.player.PlayVideoWrapper
import com.tokopedia.play_common.state.PlayVideoState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on January 05, 2024
 */
class VideoPreviewManager @Inject constructor(
    val videoWrapper: PlayVideoWrapper,
) {

    private val _videoState = MutableStateFlow<VideoState>(VideoState.Unknown)
    val videoState = _videoState.asStateFlow()

    private val videoListener = object : PlayVideoWrapper.Listener {
        override fun onPlayerStateChanged(state: PlayVideoState) {
            super.onPlayerStateChanged(state)
            when (state) {
                is PlayVideoState.Ended -> {
                    videoWrapper.videoPlayer.seekTo(1)
                    videoWrapper.pause(true)

                    _videoState.update { VideoState.Pause }
                }
                is PlayVideoState.Pause -> {
                    _videoState.update { VideoState.Pause }
                }
                else -> {}
            }
        }
    }

    init {
        videoWrapper.addListener(videoListener)
    }

    fun pause() {
        videoWrapper.pause(false)
        _videoState.update { VideoState.Pause }
    }

    fun play() {
        videoWrapper.resume()
        _videoState.update { VideoState.Play }
    }

    fun dispose() {
        videoWrapper.removeListener(videoListener)
        videoWrapper.stop()
        videoWrapper.release()
    }
}
