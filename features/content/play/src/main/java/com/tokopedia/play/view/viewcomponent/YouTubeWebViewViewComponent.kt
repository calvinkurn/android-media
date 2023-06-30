package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.youtube_player.YoutubeWebViewEventListener
import com.tokopedia.play.R
import com.tokopedia.youtube_player.YoutubeWebView

/**
 * Created by kenny.hadisaputra on 30/06/23
 */
class YouTubeWebViewViewComponent(
    container: ViewGroup,
    lifecycleOwner: LifecycleOwner,
) : ViewComponent(container, R.id.scrollable_host_youtube) {

    private val youtubeView = rootView.findViewById<YoutubeWebView>(R.id.youtube_webview)

    private var mVideoId: String? = null

    private val onPlayerReadyListener = object : YoutubeWebViewEventListener.EventPlayerReady {
        override fun onPlayerReady() {
            youtubeView.isPlayerReady = true
            startVideoFromBeginning()
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
            playerReady = onPlayerReadyListener
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
}
