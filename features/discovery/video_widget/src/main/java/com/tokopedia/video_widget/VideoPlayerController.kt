package com.tokopedia.video_widget

import android.view.View
import android.widget.ImageView
import androidx.annotation.IdRes
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class VideoPlayerController(
    private val rootView: View,
    @IdRes private val videoViewId: Int,
    @IdRes private val imageViewId: Int,
) : ExoPlayerListener, VideoPlayer {
    private val videoView by lazy(LazyThreadSafetyMode.NONE) {
        rootView.findViewById<VideoPlayerView>(videoViewId)
    }

    private val imageView by lazy(LazyThreadSafetyMode.NONE) {
        rootView.findViewById<ImageView>(imageViewId)
    }

    private var videoURL = ""
    private var videoPlayerStateFlow: MutableStateFlow<VideoPlayerState>? = null
    private val helper: VideoPlayerViewHelper by lazy(LazyThreadSafetyMode.NONE) {
        VideoPlayerViewHelper.Builder(rootView.context, videoView)
            .setExoPlayerEventsListener(this)
            .create()
    }

    fun setVideoURL(videoURL: String) {
        this.videoURL = videoURL
    }

    fun clear() {
        helper.onViewDetach()
    }

    override fun onPlayerIdle() {
        imageView?.show()
        videoView?.hide()
    }

    override fun onPlayerBuffering() {
        videoPlayerStateFlow?.tryEmit(VideoPlayerState.Buffering)
    }

    override fun onPlayerPlaying() {
        imageView?.hide()
        videoView?.show()
        videoPlayerStateFlow?.tryEmit(VideoPlayerState.Playing)
    }

    override fun onPlayerPaused() {
        imageView?.show()
        videoView?.hide()
        videoPlayerStateFlow?.tryEmit(VideoPlayerState.Paused)
    }

    override fun onPlayerEnded() {
        imageView?.show()
        videoView?.hide()
        videoPlayerStateFlow?.tryEmit(VideoPlayerState.Ended)
    }

    override fun onPlayerError(errorString: String?) {
        videoPlayerStateFlow?.tryEmit(VideoPlayerState.Error(errorString ?: ""))
    }

    override val hasVideo: Boolean
        get() = videoURL.isNotBlank()

    override fun playVideo(): Flow<VideoPlayerState> {
        if (!hasVideo) return flowOf(VideoPlayerState.NoVideo)
        videoPlayerStateFlow = MutableStateFlow(VideoPlayerState.Starting)
        helper.play(videoURL)
        return videoPlayerStateFlow as Flow<VideoPlayerState>
    }

    override fun stopVideo() {
        helper.stop()
    }
}