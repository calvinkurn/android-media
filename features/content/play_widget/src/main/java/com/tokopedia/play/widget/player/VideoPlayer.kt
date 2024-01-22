package com.tokopedia.play.widget.player

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.EventListener
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ClippingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultLoadErrorHandlingPolicy
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy
import com.google.android.exoplayer2.util.Util
import com.tokopedia.kotlin.extensions.view.ZERO
import kotlin.time.Duration

/**
 * Created by kenny.hadisaputra on 20/10/23
 */
class VideoPlayer(private val context: Context) {

    private val exoPlayer = SimpleExoPlayer.Builder(context)
        .build()

    val player: Player = exoPlayer

    private val timer by lazy {
        VideoPlayerTimer(player) { stop() }
    }

    var playbackPosition: Long = 0

    init {
        exoPlayer.addListener(object : EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            }
        })
    }

    fun addPlayerListener(listener: EventListener) {
        exoPlayer.addListener(listener)
    }

    fun removePlayerListener(listener: EventListener) {
        exoPlayer.removeListener(listener)
    }

    fun loadUri(uri: Uri, config: Config = Config()) {
        exoPlayer.prepare(getMediaSource(uri, config), true, true)
        setupTimer(config)
    }

    fun start(config: Config = Config()) {
        setupTimer(config)
        exoPlayer.playWhenReady = true
        exoPlayer.seekTo(playbackPosition)
    }

    fun pause() {
        exoPlayer.playWhenReady = false
        playbackPosition = exoPlayer.currentPosition
    }

    fun stop() {
        exoPlayer.stop()
        playbackPosition = Long.ZERO
    }

    fun release() {
        exoPlayer.release()
    }

    fun mute(shouldMute: Boolean) {
        exoPlayer.volume = if (shouldMute) 0f else 100f
    }

    fun repeat(shouldRepeat: Boolean) {
        exoPlayer.repeatMode = if (shouldRepeat) {
            ExoPlayer.REPEAT_MODE_ONE
        } else {
            ExoPlayer.REPEAT_MODE_OFF
        }
    }

    private fun setupTimer(config: Config) {
        if (config.isLive && config.resetState && config.duration.isValid) {
            timer.setup(config.duration)
        }
    }

    private fun getMediaSource(uri: Uri, config: Config): MediaSource {
        val dataSourceFactory = DefaultDataSourceFactory(context, getUserAgent())
        val mediaSourceFactory = getMediaSourceFactory(uri, dataSourceFactory)
        val mediaSource = mediaSourceFactory.createMediaSource(uri)
        return if (config.duration.isValid && !config.isLive) {
            ClippingMediaSource(mediaSource, config.duration.inWholeMicroseconds)
        } else {
            mediaSource
        }
    }

    private fun getUserAgent(): String {
        return Util.getUserAgent(context, "Tokopedia Android")
    }

    private fun getMediaSourceFactory(
        uri: Uri,
        dataSourceFactory: DataSource.Factory
    ): MediaSourceFactory {
        val errorHandlingPolicy = getDefaultLoadErrorHandlingPolicy()
        return when (val type = Util.inferContentType(uri)) {
            C.TYPE_SS -> SsMediaSource.Factory(dataSourceFactory)
                .setLoadErrorHandlingPolicy(errorHandlingPolicy)
            C.TYPE_DASH -> DashMediaSource.Factory(dataSourceFactory)
                .setLoadErrorHandlingPolicy(errorHandlingPolicy)
            C.TYPE_HLS -> HlsMediaSource.Factory(dataSourceFactory)
                .setLoadErrorHandlingPolicy(errorHandlingPolicy)
            C.TYPE_OTHER -> ProgressiveMediaSource.Factory(dataSourceFactory)
                .setLoadErrorHandlingPolicy(errorHandlingPolicy)
            else -> throw IllegalStateException("Unsupported type: $type")
        }
    }

    private fun getDefaultLoadErrorHandlingPolicy(): LoadErrorHandlingPolicy {
        return DefaultLoadErrorHandlingPolicy()
    }

    private val Duration.isValid: Boolean
        get() = this != Duration.INFINITE && this != Duration.ZERO

    data class Config(
        val duration: Duration = Duration.INFINITE,
        val isLive: Boolean = false,
        val resetState: Boolean = true
    )
}
