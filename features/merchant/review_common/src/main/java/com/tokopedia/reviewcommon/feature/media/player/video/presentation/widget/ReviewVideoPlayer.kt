package com.tokopedia.reviewcommon.feature.media.player.video.presentation.widget

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.LoadControl
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.reviewcommon.feature.media.player.video.data.cache.MediaPlayerCache
import java.lang.ref.WeakReference

class ReviewVideoPlayer(
    private val context: Context
) {
    companion object {
        //Minimum Video you want to buffer while Playing
        private const val MIN_BUFFER_DURATION = 3000

        //Max Video you want to buffer during PlayBack
        private const val MAX_BUFFER_DURATION = 18000

        //Min Video you want to buffer before start Playing it
        private const val MIN_PLAYBACK_START_BUFFER = 2000

        //Min video You want to buffer when user resumes video
        private const val MIN_PLAYBACK_RESUME_BUFFER = 2000
    }

    private var listenerRef: WeakReference<ReviewVideoPlayerListener?>? = null
    private var playerViewRef: WeakReference<PlayerView?>? = null
    private var volume: Float = 0f
        set(value) {
            field = value
            exoPlayer?.volume = value
        }

    private var loadControl: LoadControl = DefaultLoadControl.Builder()
        .setBufferDurationsMs(
            MIN_BUFFER_DURATION,
            MAX_BUFFER_DURATION,
            MIN_PLAYBACK_START_BUFFER,
            MIN_PLAYBACK_RESUME_BUFFER
        )
        .createDefaultLoadControl()

    private var exoPlayer: SimpleExoPlayer? = null
        set(value) {
            field = value
            value.attachVideoPlayerStateListener()
            value.attachControllerView()
            value?.volume = volume
        }
    private var playerControlRef: WeakReference<PlayerControlView?>? = null
        set(value) {
            field = value
            exoPlayer?.attachControllerView()
        }

    init {
        exoPlayer?.volume = 0f
    }

    private fun getExoPlayerInstance(): SimpleExoPlayer {
        return exoPlayer ?: SimpleExoPlayer.Builder(context)
            .setTrackSelector(DefaultTrackSelector(context))
            .setLoadControl(loadControl)
            .build().also { exoPlayer = it }
    }

    private fun SimpleExoPlayer?.attachVideoPlayerStateListener() {
        this?.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)
                val isPlaying = playWhenReady && playbackState == Player.STATE_READY
                val isBuffering = playWhenReady && playbackState == Player.STATE_BUFFERING
                val isPaused = !playWhenReady && playbackState == Player.STATE_READY
                val isPreloading = !playWhenReady && playbackState == Player.STATE_BUFFERING
                val isEnded = playbackState == Player.STATE_ENDED
                if (isPlaying) {
                    listenerRef?.get()?.onReviewVideoPlayerIsPlaying()
                } else if (isBuffering) {
                    listenerRef?.get()?.onReviewVideoPlayerIsBuffering()
                } else if (isPaused) {
                    listenerRef?.get()?.onReviewVideoPlayerIsPaused()
                } else if (isPreloading) {
                    listenerRef?.get()?.onReviewVideoPlayerIsPreloading()
                } else if (isEnded) {
                    listenerRef?.get()?.onReviewVideoPlayerIsEnded()
                }
            }

            override fun onPlayerError(error: ExoPlaybackException) {
                super.onPlayerError(error)
                listenerRef?.get()?.onReviewVideoPlayerError()
            }
        })
    }

    private fun SimpleExoPlayer?.attachControllerView() {
        playerControlRef?.get()?.player = this
    }

    private fun getMediaSourceBySource(context: Context, uri: Uri): MediaSource {
        val defaultDataSourceFactory =
            DefaultDataSourceFactory(context, Util.getUserAgent(context, "Tokopedia Android"))
        val dataSourceFactory =
            CacheDataSourceFactory(MediaPlayerCache.getInstance(context), defaultDataSourceFactory)
        val mediaSource = when (val type = Util.inferContentType(uri)) {
            C.TYPE_SS -> SsMediaSource.Factory(dataSourceFactory)
            C.TYPE_DASH -> DashMediaSource.Factory(dataSourceFactory)
            C.TYPE_HLS -> HlsMediaSource.Factory(dataSourceFactory)
            C.TYPE_OTHER -> ProgressiveMediaSource.Factory(dataSourceFactory)
            else -> throw IllegalStateException("Unsupported type: $type")
        }
        return mediaSource.createMediaSource(uri)
    }

    fun initializeVideoPlayer(
        uri: String,
        newPlayerView: PlayerView?,
        newListener: ReviewVideoPlayerListener,
        shouldPrepare: Boolean
    ) {
        if (uri.isBlank() || newPlayerView == null) return
        val oldPlayerView = playerViewRef?.get()
        val exoPlayer = exoPlayer ?: getExoPlayerInstance()
        playerViewRef = WeakReference(newPlayerView)
        listenerRef = WeakReference(newListener)
        if (shouldPrepare) {
            val mediaSource = getMediaSourceBySource(context, Uri.parse(uri))
            exoPlayer.prepare(mediaSource)
        }
        PlayerView.switchTargetView(exoPlayer, oldPlayerView, newPlayerView)
    }

    fun restorePlaybackState(positionMs: Long, playWhenReady: Boolean) {
        exoPlayer?.seekTo(positionMs)
        if (playWhenReady) play() else pause()
    }

    fun cleanupVideoPlayer() {
        playerViewRef?.get()?.player = null
        playerControlRef?.get()?.player = null
        playerViewRef = null
        listenerRef = null
        exoPlayer?.release()
        exoPlayer = null
    }

    fun setPlayerController(playerControlViewReviewMediaGallery: PlayerControlView?) {
        playerControlRef = WeakReference(playerControlViewReviewMediaGallery)
    }

    fun getCurrentPosition(): Long {
        return exoPlayer?.currentPosition.orZero()
    }

    fun play() {
        exoPlayer?.playWhenReady = true
    }

    fun pause() {
        exoPlayer?.playWhenReady = false
    }

    fun mute() {
        volume = 0f
    }

    fun unmute() {
        volume = 1f
    }
}

interface ReviewVideoPlayerListener {
    fun onReviewVideoPlayerIsPlaying()
    fun onReviewVideoPlayerIsBuffering()
    fun onReviewVideoPlayerIsPaused()
    fun onReviewVideoPlayerIsPreloading()
    fun onReviewVideoPlayerIsEnded()
    fun onReviewVideoPlayerError()
}