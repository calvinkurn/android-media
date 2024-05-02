package com.tokopedia.content.product.preview.view.components.player

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.C.TYPE_DASH
import com.google.android.exoplayer2.C.TYPE_HLS
import com.google.android.exoplayer2.C.TYPE_OTHER
import com.google.android.exoplayer2.C.TYPE_SS
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.BehindLiveWindowException
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import java.util.concurrent.TimeUnit

class ProductPreviewExoPlayer(private val context: Context) {

    private var videoStateListener: VideoStateListener? = null

    private val loadControl = DefaultLoadControl.Builder()
        .setBackBuffer(
            TimeUnit.MINUTES.toMillis(MINIMUM_BUFFER_TIME).toInt(),
            true
        )
        .setBufferDurationsMs(
            TimeUnit.SECONDS.toMillis(DEFAULT_BUFFER_TIME).toInt(),
            DefaultLoadControl.DEFAULT_MAX_BUFFER_MS,
            TimeUnit.SECONDS.toMillis(MINIMUM_BUFFER_TIME).toInt(),
            TimeUnit.SECONDS.toMillis(MINIMUM_BUFFER_TIME).toInt()
        ).createDefaultLoadControl()

    private val _exoPlayer = SimpleExoPlayer.Builder(context)
        .setTrackSelector(DefaultTrackSelector(context))
        .setLoadControl(loadControl)
        .build()
    val exoPlayer: SimpleExoPlayer
        get() = _exoPlayer

    init {
        exoPlayer.volume = UN_MUTE_VOLUME
        exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
        exoPlayer.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                val isReady = Player.STATE_READY
                val isPlaying = playWhenReady && playbackState == isReady
                val isReadyToPlay = !playWhenReady && playbackState == isReady
                val isBuffering = playWhenReady && playbackState == Player.STATE_BUFFERING
                val isEnded = playWhenReady && playbackState == Player.STATE_ENDED

                when {
                    isPlaying -> {
                        videoStateListener?.onVideoReadyToPlay(true)
                    }

                    isReadyToPlay -> {
                        videoStateListener?.onVideoReadyToPlay(false)
                    }

                    isBuffering -> {
                        videoStateListener?.onBuffering()
                    }

                    isEnded -> {
                        videoStateListener?.onVideoEnded()
                        ended()
                    }
                }
            }

            override fun onPlayerError(error: ExoPlaybackException) {
                super.onPlayerError(error)

                if (error.type != ExoPlaybackException.TYPE_SOURCE) return
                if (error.sourceException !is BehindLiveWindowException) return
            }
        })
    }

    fun setVideoListener(listener: VideoStateListener) {
        videoStateListener = listener
    }

    fun start(
        videoUrl: String,
        isMute: Boolean,
        playWhenReady: Boolean = true
    ) {
        if (videoUrl.isBlank()) return

        val mediaSource = getMediaSourceByUri(context, Uri.parse(videoUrl))
        toggleVideoVolume(isMute)
        exoPlayer.repeatMode = Player.REPEAT_MODE_OFF
        exoPlayer.playWhenReady = playWhenReady
        exoPlayer.prepare(mediaSource, true, false)
    }

    fun seekDurationTo(duration: Long) {
        exoPlayer.seekTo(duration)
    }

    fun resume() {
        exoPlayer.playWhenReady = true
    }

    fun reset() {
        exoPlayer.seekTo(START_DURATION)
    }

    fun pause() {
        exoPlayer.playWhenReady = false
    }

    fun stop() {
        exoPlayer.playWhenReady = false
        exoPlayer.stop()
    }

    private fun ended() {
        exoPlayer.playWhenReady = false
        exoPlayer.seekTo(START_DURATION)
    }

    fun release() {
        exoPlayer.release()
    }

    fun toggleVideoVolume(isMute: Boolean) {
        if (isMute) {
            exoPlayer.volume = MUTE_VOLUME
        } else {
            exoPlayer.volume = UN_MUTE_VOLUME
        }
    }

    private fun getMediaSourceByUri(
        context: Context,
        uri: Uri
    ): MediaSource {
        val sourceFactory = getDataSourceFactory(context)

        val mediaSource = when (val type = Util.inferContentType(uri)) {
            TYPE_SS -> SsMediaSource.Factory(sourceFactory)
            TYPE_DASH -> DashMediaSource.Factory(sourceFactory)
            TYPE_HLS -> HlsMediaSource.Factory(sourceFactory).setAllowChunklessPreparation(true)
            TYPE_OTHER -> ProgressiveMediaSource.Factory(sourceFactory)
            else -> error("Unsupported type: $type")
        }
        return mediaSource.createMediaSource(uri)
    }

    private fun getDataSourceFactory(context: Context): DataSource.Factory {
        return DefaultHttpDataSourceFactory(
            Util.getUserAgent(context, APPLICATION_NAME)
        )
    }

    interface VideoStateListener {
        fun onVideoReadyToPlay(isPlaying: Boolean)
        fun onBuffering()
        fun onVideoEnded()
    }

    companion object {
        private const val START_DURATION = 0L
        private const val MINIMUM_BUFFER_TIME = 1L
        private const val DEFAULT_BUFFER_TIME = 30L
        private const val UN_MUTE_VOLUME = 1f
        private const val MUTE_VOLUME = 0F
        private const val APPLICATION_NAME = "Tokopedia Android"
    }
}
