package com.tokopedia.video_widget

import android.view.View
import android.widget.ImageView
import androidx.annotation.IdRes
import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class VideoPlayerController(
    private var rootView: View?,
    @IdRes private val videoViewId: Int,
    @IdRes private val imageViewId: Int
) : ExoPlayerListener, VideoPlayer {
    private var videoView = rootView?.findViewById<VideoPlayerView>(videoViewId)
    private var imageView = rootView?.findViewById<ImageView>(imageViewId)
    private var videoURL = ""
    private var videoPlayerStateFlow: MutableStateFlow<VideoPlayerState>? = null
    private val helper by lazy(LazyThreadSafetyMode.NONE) { VideoPlayerViewHelper(videoView) }

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

    override fun playVideo(exoPlayer: ExoPlayer): Flow<VideoPlayerState> {
        if (!hasVideo) return flowOf(VideoPlayerState.NoVideo)
        videoPlayerStateFlow = MutableStateFlow(VideoPlayerState.Starting)
        helper.setExoPlayer(exoPlayer)
        helper.play(videoURL)
        return videoPlayerStateFlow as Flow<VideoPlayerState>
    }

    override fun stopVideo() {
        helper.stop()
        helper.removeExoPlayer()
    }
}
