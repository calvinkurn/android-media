package com.tokopedia.play.widget.player

import android.content.Context
import android.net.Uri
import android.os.CountDownTimer
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ClippingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.tokopedia.play.widget.ui.model.PlayWidgetType
import com.tokopedia.play_common.util.PlayConnectionCommon
import java.time.Duration
import java.util.concurrent.TimeUnit

/**
 * Created by mzennis on 09/10/20.
 */
open class PlayVideoPlayer(val context: Context, cardType: PlayWidgetType) {
    private var autoStopTimer: CountDownTimer? = null

    var listener: VideoPlayerListener? = null
    var videoUrl: String? = null
    var shouldCache: Boolean = false

    var maxDurationCellularInSeconds: Int = 0
    var maxDurationWifiInSeconds: Int = 0

    private val shouldForceLowest = cardType != PlayWidgetType.Jumbo

    private val trackSelector = DefaultTrackSelector(context).apply {
        parameters = DefaultTrackSelector.ParametersBuilder(context)
            .setForceLowestBitrate(shouldForceLowest).build()
    }

    private val exoPlayer: SimpleExoPlayer = SimpleExoPlayer.Builder(context)
        .setTrackSelector(trackSelector)
        .build()

    init {
        exoPlayer.volume = 0F
        exoPlayer.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_ENDED -> {
                        whenIsPlayingChanged(isPlaying = false)
                    }
                    Player.STATE_READY -> {
                        whenIsPlayingChanged(isPlaying = true)
                    }
                    Player.STATE_BUFFERING -> {
                        // don't do anything when buffering, just follow the previous state
                    }
                    else -> whenIsPlayingChanged(isPlaying = false)
                }
            }
        })
    }

    private fun whenIsPlayingChanged(isPlaying: Boolean) {
        listener?.onIsPlayingChanged(isPlaying)
    }

    fun start() {
        if (videoUrl == null || videoUrl?.isBlank() == true) return

        val mediaSource = getMediaSourceBySource(
            context,
            Uri.parse(videoUrl),
            shouldCache,
            Duration.ofSeconds(
                if (PlayConnectionCommon.isConnectCellular(context)) {
                    maxDurationCellularInSeconds
                } else {
                    maxDurationWifiInSeconds
                }.toLong()
            )
        )

        exoPlayer.playWhenReady = true
        exoPlayer.prepare(mediaSource, true, false)
    }

    fun mute(shouldMute: Boolean) {
        exoPlayer.volume = if (shouldMute) 0f else 100f
    }

    fun repeat(shouldRepeat: Boolean) {
        exoPlayer.repeatMode = if (shouldRepeat) ExoPlayer.REPEAT_MODE_ONE else ExoPlayer.REPEAT_MODE_OFF
    }

    fun stop() {
        autoStopTimer?.cancel()
        exoPlayer.stop()
    }

    fun restart() {
        start()
    }

    fun release() {
        try {
            exoPlayer.release()
        } catch (throwable: Throwable) {
        }
    }

    fun getPlayer(): ExoPlayer = exoPlayer

    interface VideoPlayerListener {
        fun onIsPlayingChanged(isPlaying: Boolean)
    }

    private fun getMediaSourceBySource(
        context: Context,
        uri: Uri,
        shouldCache: Boolean,
        durationToPlay: Duration
    ): MediaSource {
        val defaultDataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, "Tokopedia Android"))
        val dataSourceFactory = if (shouldCache) {
            CacheDataSourceFactory(PlayWidgetVideoCache.getInstance(context), defaultDataSourceFactory)
        } else {
            defaultDataSourceFactory
        }

        val mediaSourceFactory = when (val type = Util.inferContentType(uri)) {
            C.TYPE_SS -> SsMediaSource.Factory(dataSourceFactory)
            C.TYPE_DASH -> DashMediaSource.Factory(dataSourceFactory)
            C.TYPE_HLS -> HlsMediaSource.Factory(dataSourceFactory)
            C.TYPE_OTHER -> ProgressiveMediaSource.Factory(dataSourceFactory)
            else -> throw IllegalStateException("Unsupported type: $type")
        }

        return if (durationToPlay.isZero || durationToPlay.isNegative) {
            mediaSourceFactory.createMediaSource(uri)
        } else {
            ClippingMediaSource(
                mediaSourceFactory.createMediaSource(uri),
                0,
                TimeUnit.NANOSECONDS.toMicros(durationToPlay.toNanos()),
                true,
                true,
                true
            )
        }
    }

    private fun configureAutoStop(durationLimit: Int?) {
        if (durationLimit == null) return

        autoStopTimer?.cancel()
        autoStopTimer = createStopTimer(durationLimit)
        autoStopTimer?.start()
    }

    private fun createStopTimer(durationLimit: Int): CountDownTimer {
        return object : CountDownTimer(durationLimit.toLong() * 1000, 1000) {
            override fun onFinish() {
                exoPlayer.stop()
            }

            override fun onTick(millisUntilFinished: Long) { }
        }
    }
}
