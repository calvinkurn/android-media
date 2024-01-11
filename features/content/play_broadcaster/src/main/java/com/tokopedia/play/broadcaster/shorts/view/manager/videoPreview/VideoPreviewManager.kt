package com.tokopedia.play.broadcaster.shorts.view.manager.videoPreview

import android.content.Context
import android.net.ConnectivityManager
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
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
    @ApplicationContext context: Context,
    val videoWrapper: PlayVideoWrapper,
) {
    private val connectivityManager: ConnectivityManager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val isWifi: Boolean
        get() = !connectivityManager.isActiveNetworkMetered

    private val _videoState = MutableStateFlow(VideoState.Unknown)
    val videoState = _videoState.asStateFlow()

    private val videoListener = object : PlayVideoWrapper.Listener {
        override fun onPlayerStateChanged(state: PlayVideoState) {
            super.onPlayerStateChanged(state)
            when (state) {
                is PlayVideoState.Ended -> {
                    videoWrapper.reset()
                    videoWrapper.pause(true)

                    _videoState.update { VideoState.Pause }
                }
                is PlayVideoState.Pause -> {
                    if (_videoState.value == VideoState.Unknown) {
                        if (isWifi) play() else pause()
                    } else {
                        _videoState.update { VideoState.Pause }
                    }
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
