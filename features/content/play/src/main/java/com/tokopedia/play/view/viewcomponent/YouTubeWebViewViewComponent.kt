package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play.R
import com.tokopedia.play.util.video.state.BufferSource
import com.tokopedia.play.util.video.state.PlayViewerVideoState
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.youtube_player.YoutubeWebView
import com.tokopedia.youtube_player.YoutubeWebViewEventListener

/**
 * Created by kenny.hadisaputra on 30/06/23
 */
class YouTubeWebViewViewComponent(
    container: ViewGroup,
    lifecycleOwner: LifecycleOwner,
    listener: Listener
) : ViewComponent(container, R.id.scrollable_host_youtube) {

    private val youtubeView = rootView.findViewById<YoutubeWebView>(R.id.youtube_webview)

    private var mVideoId: String? = null

    private val onPlayerReadyListener = object : YoutubeWebViewEventListener.EventPlayerReady {
        override fun onPlayerReady() {
            youtubeView.isPlayerReady = true
            startVideoFromBeginning()
        }
    }

    private val onEndedListener = object : YoutubeWebViewEventListener.EventVideoEnded {
        override fun onVideoEnded(time: Int) {
            listener.onStateChanged(this@YouTubeWebViewViewComponent, PlayViewerVideoState.End)
        }
    }

    private val onPlayingListener = object : YoutubeWebViewEventListener.EventVideoPlaying {
        override fun onVideoPlaying(time: Int) {
            listener.onStateChanged(this@YouTubeWebViewViewComponent, PlayViewerVideoState.Play)
        }
    }

    private val onPausedListener = object : YoutubeWebViewEventListener.EventVideoPaused {
        override fun onVideoPaused(time: Int) {
            listener.onStateChanged(this@YouTubeWebViewViewComponent, PlayViewerVideoState.Pause)
        }
    }

    private val onBufferingListener = object : YoutubeWebViewEventListener.EventVideoBuffering {
        override fun onVideoBuffering() {
            listener.onStateChanged(
                this@YouTubeWebViewViewComponent,
                PlayViewerVideoState.Buffer(BufferSource.Unknown)
            )
        }
    }

    private val onErrorListener = object : YoutubeWebViewEventListener.EventError {
        override fun onError(errorCode: Int) {
            listener.onStateChanged(
                this@YouTubeWebViewViewComponent,
                PlayViewerVideoState.Error(
                    Exception("Error YouTube iFrame Player with code: $errorCode")
                )
            )
        }
    }

    init {
        lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_PAUSE -> youtubeView.pause()
                    Lifecycle.Event.ON_RESUME -> youtubeView.play()
                    Lifecycle.Event.ON_DESTROY -> youtubeView.releaseWebView()
                    else -> {}
                }
            }
        })

        youtubeView.initialize(
            youtubeEventVideoEnded = onEndedListener,
            youtubeEventVideoPlaying = onPlayingListener,
            youtubeEventVideoPaused = onPausedListener,
            youtubeEventVideoBuffering = onBufferingListener,
            youtubeEventError = onErrorListener,
            playerReady = onPlayerReadyListener,
            options = YoutubeWebView.PlayerOptions(enableFullScreen = false)
        )
    }

    fun loadVideo(youtubeId: String) {
        if (mVideoId == youtubeId) return

        mVideoId = youtubeId
        startVideoFromBeginning()
    }

    fun play() {
        youtubeView.play()
    }

    fun pause() {
        youtubeView.pause()
    }

    fun release() {
        youtubeView.releaseWebView()
    }

    private fun startVideoFromBeginning() {
        if (!youtubeView.isPlayerReady) return
        val videoId = mVideoId ?: return

        youtubeView.loadVideo(videoId, 0)
    }

    interface Listener {
        fun onStateChanged(view: YouTubeWebViewViewComponent, state: PlayViewerVideoState)
    }
}
